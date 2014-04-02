/*
    monopoly.js:
    written by: Dana Akerman
*/

if (typeof GAME == "undefined") {
    GAME = 
    {
	    version: "1.0.1",
            build: 1
    };
}

GAME.AJAX_URL = "GameServlet"; // URL of Ajax call

GAME.ACTION = 
{  
    GET_STATE:  "state",
    BUY_ANSWER: "buy",
    CLIENT_DICES: "dices",
    RESIGN: "resign"
};

GAME.UPDATE_INTERVAL = 1000; // how often we check for state update (in milisec)
GAME.MAX_PLAYERS = 6; // support 2 to 6 players
GAME.MOVEMENT_DONE = "movementdone"; // event movementdone is fiered after all animations are done


// create a private scope and map $ to jQuery
(function($)
{

    // Player object
    GAME.Player = function(id)
    {
        this.id = id;
        this.cell = 1;
        this.element = $("<div id='player" + this.id + "' class='player-elem'></div>");
        this.element.appendTo($("#monopoly-frame"));
        this.element.css({top: 0, left: 0});
    };

    //reset visual presentation
    GAME.Player.prototype.reset = function()
    {
        var assetClassName = "player" + this.id + "-asset";
        // remove assets
        $("." + assetClassName).removeClass(assetClassName).removeClass("house3").removeClass("house2").removeClass("house1");
    };


    // update the player state
    GAME.Player.prototype.update = function(oState)
    {
        var player = this,
            position,
            parkingCount,
            targetCell,
            unregister = function()
            {
                GAME.monopoly.registerAnimation(-1);
            };

        $.each(oState.assets, function()
        {
            $("#cell" + this.address).addClass("player" + player.id + "-asset");
            if (this.houses)
            {
                $("#cell" + this.address).addClass("house" + this.houses);
            }
        });

        // is there someone allready parked on our cell? if so, add a small offset
        occupiedCellsMap = GAME.monopoly.getOccupiedCellsMap ();
        parkingCount = occupiedCellsMap[oState.location] ? occupiedCellsMap[oState.location] : 0;
        occupiedCellsMap[oState.location] = parkingCount + 1;

        if(this.cell !== oState.location)
        {
            GAME.monopoly.registerAnimation(1);
            while (this.cell !== oState.location)
            {
                this.cell = this.cell % 36 + 1;
                targetCell =  $("#cell" + this.cell);
                position  = targetCell.position();
                this.element.animate({top: position.top + (targetCell.height() / 2), left: position.left + (targetCell.width() / 2) + (parkingCount * 10) - 20},
                        200,  this.cell === oState.location ? unregister : $.noop);
            }
        }

        this.element.attr('title', oState.name);
    };

    GAME.Player.prototype.legend = function(oState, legend)
    {
        $("<li class='player" + oState.id + "'><strong>" + oState.name + "</strong>, " + oState.fonds + " $</li>").appendTo($("ul", legend));
    };

}(jQuery));

// make sure the $ is maped to the jQuery function at least inside our function

GAME.monopoly = function($)
{
    var players = [], // hold reference to all players
        occupiedCellsMap = [], // which cells are occupied - to calculate position in cell
        legend,
        dialog,
        console,
        dice1,
        dice2,
        dices, // dice dialog
        message, // popup
        closableMessage,
        warrant,
        surprise,
        resign,
        pendingAjaxCall,
        pendingAnimations = 0,
        ended = false; 

        // updatePlayer
        updatePlayer = function(n, player)
        {
            players[player.id-1].update(player);
        },

        // updateLegend
        updateLegend = function(n, player)
        {
            players[player.id-1].legend(player, legend);
        },

        // display the dialog that was passed
        showDialog = function(dialogWrap, isOn)
        {
            var collapsedWidth = 200,
                collapsedHeight = 200,
                targetTop,
                targetLeft,
                targetWidth,
                targetHeight,
                targetOpacity;

            if (isOn)
            {
                dialogWrap.css({
                        opacity: 'hide',
                        top: ($(window).height() - collapsedHeight) / 3,
                        left:($(window).width() - collapsedWidth) / 3,
                        width: collapsedWidth,
                        height: collapsedHeight
                     });
                targetWidth =  $(window).width() / 3;
                targetHeight =  $(window).height() / 3;
                targetTop =  $(window).height() / 6;
                targetLeft =  $(window).width() / 6;
                targetOpacity = 1;
            } 
            else 
            {
                targetWidth =  collapsedWidth;
                targetHeight = collapsedHeight;
                targetTop = ($(window).height() - collapsedHeight) / 3;
                targetLeft = ($(window).width() - collapsedWidth) / 3;
                targetOpacity = 0;
            }

            dialogWrap.removeClass("hidden")
                     .animate(
                         {
                             opacity: targetOpacity,
                             width: targetWidth,
                             height: targetHeight,
                             top: targetTop,
                             left: targetLeft
                         },
                         200,
                         function()
                         {
                            if (!isOn)
                            {
                                dialogWrap.addClass("hidden");    
                            }
                         }
                     );

        },

        //display a dialog and ask a question
        ask = function(quest)
        {
            dialog.find(".question").text(quest.question).end()
                    .find("#btn1").text(quest.option1).end()
                    .find("#btn2").text(quest.option2)
                    .end();
            showDialog(dialog, true);
        },
        
        // shows a message
        showMessage = function(object, mess, textType)
        {
            ($("#" + textType)).remove();
            object.append("<p id = '" + textType + "'><font size = '5'>" + mess.text + "</font></p>");
            showDialog(object, true);
        },

        //display a throw dices dialog
        throwDices = function()
        {
            dices.find("#cube1").val("").end().find("#cube2").val("");
            showDialog(dices, true);
        },
        
        // redirects to the index page
        doRedirect = function()
        {
            window.location.replace("/MonopolyWeb")
        },
        
        // gets a value, element id and img id
        // appends the mathcing dice image to the element according to the value
        showDiceByValue = function(value, id, imgId)
        {
            switch(value)
            {
                case 1:
                    $(id).append("<img id=" + imgId + " src = 'css/images/dice/dice_one.jpg' />");
                    break;
                case 2:
                    $(id).append("<img id=" + imgId + " src = 'css/images/dice/dice_two.jpg' />");
                    break;
                case 3:
                    $(id).append("<img id=" + imgId + " src = 'css/images/dice/dice_three.jpg' />");
                    break;
                case 4:
                    $(id).append("<img id=" + imgId + " src = 'css/images/dice/dice_four.jpg' />");
                    break;
                case 5:
                    $(id).append("<img id=" + imgId + " src = 'css/images/dice/dice_five.jpg' />");
                    break;
                case 6:
                    $(id).append("<img id=" + imgId + " src = 'css/images/dice/dice_six.jpg' />");
                    break;
            }
        },

        _registerAnimation = function(i)
        {
            pendingAnimations += i;
            if (i < 0 && pendingAnimations === 0){
                $("#monopoly-frame").trigger(GAME.MOVEMENT_DONE);
            }
        },

        // handle server update
        handleServerUpdate = function(o)
        {
            
            pendingAjaxCall = null;
            pendingAnimations = 1;


            if(o) // we got the json
            {
                // reset previous state
                occupiedCellsMap = [];
                for (i = 0; i < GAME.MAX_PLAYERS; i++)
                {
                    players[i].reset();
                }
                $("ul", legend).empty();
                
                // render the new state
                $.each(o.players, updatePlayer);
                $.each(o.players, updateLegend);

                if(o.dice)
                {
                    $("#first").remove();   
                    $("#second").remove();
                    showDiceByValue(o.dice.dice1, "#dice1", "first");
                    showDiceByValue(o.dice.dice2, "#dice2", "second");
                }
                else
                {
                    $("#first").remove();   
                    $("#second").remove();
                    $("#dice1").append("<img id='first' src = 'css/images/dice/dice.jpg' />");
                    $("#dice2").append("<img id='second' src = 'css/images/dice/dice.jpg' />");                    
                }
                
                if(o.redirect)
                {
                    setTimeout(doRedirect, 5000); 
                    ended = true;
                }
            

                // register for a "one time" event
                $("#monopoly-frame").one(GAME.MOVEMENT_DONE , function()
                {
                    if (o.dialog)
                    {
                        ask(o.dialog);
                    } 
                    else if (o.dices)
                    {
                        throwDices();
                    }
                    else
                    {
                     
                        if(o.message)
                        {
                            showMessage(message, o.message, "msgText");
                            setTimeout(hideMessage, 3000);
                        }
                        else if(o.closableMessage)
                        {
                            showMessage(closableMessage, o.closableMessage, "closableText");
                        }
                        else if(o.surprise)
                        {
                            showMessage(surprise, o.surprise, "surpriseText");
                        }
                        else if(o.warrant)
                        {
                            showMessage(warrant, o.warrant, "warrantText")
                        }
                        else if(o.line)
                        {
                            // get the current time
                            var dTime = new Date();
                            var hours = dTime.getHours();
                            var minute = dTime.getMinutes();
                            var seconds = dTime.getSeconds();
           
                            hours = ((hours > 12) ? hours - 12 : hours);
                            hours = (hours < 10)? "0" + hours : hours;
                            minute = (minute < 10)? "0" + minute : minute;
                            seconds = (seconds < 10)? "0" + seconds : seconds;
                            var time = (hours + ":" + minute + ":" + seconds);
                            
                            $("#console").prepend("<p> <font color = red>" + time + "> </font> " + o.line + "</p>");
                        }
                       
                        if(!ended)
                        {
                            setTimeout(updateState, GAME.UPDATE_INTERVAL);
                        }
                    }
                });        
                _registerAnimation(-1);
            }
            else // try again
            {
                setTimeout(updateState, GAME.UPDATE_INTERVAL);
            }
         },
         
        // hides a message
        hideMessage = function()
        {
            showDialog(message, false);
        },
         
        // ask server for updates
        updateState = function()
        {
            pendingAjaxCall = $.getJSON(GAME.AJAX_URL, "action=" + GAME.ACTION.GET_STATE, handleServerUpdate);
        };

        postBuyReply = function(answerToSend) 
        {
            return function() 
            {
                $.post(GAME.AJAX_URL, $.param({answer: answerToSend, action: GAME.ACTION.BUY_ANSWER}));
                setTimeout(updateState, 1000);
                showDialog(dialog, false);
            }
        };

    return {
        init:function()
        {
            var i;
            

            // create players legend
            legend = $("<div id='playersLegend'><ul></ul></div>");
            legend.appendTo($("#monopoly-frame"));
            
            // create console
            console = $("<div id = 'console'></div>");
            console.appendTo($("#monopoly-frame"))
            
            // create resign button
            resign = $("<div id='resign'><button id = 'btn4'>Resign</button></div>");
            resign.appendTo($("#monopoly-frame"));
            $("#btn4").click
            (
            function()
            {
                $.post(GAME.AJAX_URL, $.param({action: GAME.ACTION.RESIGN}));
                doRedirect();
            }
            )
            
            // create dices display
            dice1 = $("<div id ='dice1'></div>");
            dice1.appendTo($("#monopoly-frame"));
            
            dice2 = $("<div id ='dice2'></div>");
            dice2.appendTo($("#monopoly-frame"));
            
            // create pop up message
            message = $("<div id='messageDialog' class='dialog hidden'><div class='text'></div></div>");    
            message.appendTo($("#monopoly-frame"));
            
            // create closable message
            closableMessage = $("<div id ='messageDialog' class='dialog hidden'><div class='text'></div><div class='ft'><button id='btn7'>OK</button></div></div>");
            closableMessage.appendTo($("#monopoly-frame"));
            $("#btn7").click
            (
            function()
            {
                showDialog(closableMessage, false);
            }
            )
            
            // create warrant card
            warrant = $("<div id='warrant' class='dialog hidden'><div class='text'></div><div class='ft'><button id='btn5'>OK</button></div></div>");
            warrant.appendTo($("#monopoly-frame"))
            $("#btn5").click
            (
            function()
            {
                showDialog(warrant,false);
            }
            )
                
            // create surprise card
            surprise = $("<div id='surprise' class='dialog hidden'><div class='text'></div><div class='ft'><button id='btn6'>OK</button></div></div>");
            surprise.appendTo($("#monopoly-frame"))
            $("#btn6").click
            (
            function()
            {
                showDialog(surprise,false);
            }
            )
                
            // create a dialog for later server questions
            dialog = $("<div id='questDialog' class='dialog hidden'><div class='question'></div><div class='ft'><button id='btn1'>option1</button><button id='btn2'>option2</button></div></div>");
            dialog.appendTo($("#monopoly-frame"));
            $("#btn1").click(postBuyReply("yes"));
            $("#btn2").click(postBuyReply("no"));
            

            // create a dialog for manual dices input
            dices = $("<div id='dicesDialog' class='dialog hidden'>"
                  +"<div class='question'><p>Please roll the dice</p>"
                  +"<select id='cube1'><option value='1'>1</option><option value='2'>2</option>"
                  +"<option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option></select>"
                  +"  <select id='cube2'><option value='1'>1</option><option value='2'>2</option>"
                  +"<option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option></select>"
                  +"</div><div class='ft'><button id='btn3'>OK</button></div></div>");
            dices.appendTo($("#monopoly-frame"));
            $("#btn3").click(
            function()
            {
                $.post(GAME.AJAX_URL, $.param({action: GAME.ACTION.CLIENT_DICES, cube1: $("#cube1").val() ,cube2: $("#cube2").val()}));
                setTimeout(updateState, 1000);
                showDialog(dices, false);
            }
            );
            

            // build players array
            for (i = 0; i < GAME.MAX_PLAYERS; i++)
            {
                players[i] = new GAME.Player(i+1);
            }

            $.ajaxSetup ({
                // Disable caching of AJAX responses
                cache: false
            });
            
            // start checking for state update
            updateState();
        },

        registerAnimation: function(i){
            _registerAnimation(i);
        },

        getOccupiedCellsMap : function() {
            return occupiedCellsMap;
        }

    };

}(jQuery);

// on document ready - call init
jQuery(GAME.monopoly.init);



















