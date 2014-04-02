<%-- 
    represents the index
    Author     : Dana Akerman
--%>

<%@page import="client.GameDetails"%>
<%@page import="java.util.List"%>
<%@page import="client.Server"%>
<%@page import="java.io.PrintWriter"%>




<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>index</title>
        
        <%! 
        
        private String showCurrentGame(HttpServletRequest request)
        {
            List<String> name = Server.getWaitingGames();
            
            if(name.isEmpty())
            {
                name = Server.getActiveGames();
                if(name.isEmpty())
                {
                    return "there is no game<br>you can create one";
                }
            }
            
            GameDetails details = Server.getGameDetails(name.get(0));
            String info = "name: " + details.getGameName() + "<br> status: " + details.getStatus() + "<br>" 
                    + " human players: " + details.currHumanPlayers() + "/" + details.getTotalHumanPlayers();
            
            return info;
        } 
          
        %>
        
        <style type ="text/css">
            
            body
            {
                margin: 0;
                padding: 0;
                background: #fff;
            }
            
            #indexContainer
            {
                margin: 1em auto;
                width: 650px;
                height: 650px;
                background: #CBE9DF;
                left: 350px;
                position: absolute;
            }
            
            #header
            {
                width: 650px;
                height: 150px;
                background: slategrey url(css/images/monopolyStart.gif) no-repeat center center;
 
            }
            
            #formContainer
            {
                padding-top: 40px;
                width: 600px;
                background: #CBE9DF;
                margin: 0 0 0 20px;
            }
            
            #left
            {
                float: left;
                width: 300px;
                background: #CBE9DF;
            }
            
            #currGame
            {
                width: 200px;
                height: 150px;
                background: white;
                left: 30%;
                bottom: 10%;
                position: absolute;
                text-align: center;
                border: solid slategray;
            }
           

        </style>

    </head>

    <body>
  
        <div id = "indexContainer">
            
            <div id = "header">
            </div>
            
            <div id = "formContainer">
                <form id="form1" name="form1" method="post" action="StartServlet">
                <div id = "left">
                    
                    <%
                        String message = (String) request.getAttribute("message");
                        if (message != null) 
                        {
                            out.println("<font color = 'red'>Error! " + message + "</font><br>");
                        }
                    %>
                    
                    <p>what would you like to do?</p><br/>
                    <span>
                        <input type ="radio" name ="option" value ="join" /> join an existing game
                    </span>

                    <span>
                        <input type="radio" name ="option" value ="start" CHECKED/> start a new game
                    </span>

                    <span>
                        enter your name 
                        <input type="text" value="Dana" name="playerName" id="textfield" />
                    </span>
                         
                    <span>
                        <input type="submit" name="submit" id="button" value="Proceed" />
                    </span>
                </div>
                
                <div id = "right">
                    <p>
                        <b>create a new game:</b>
                    </p>

                    <p>
                        enter game name <input type="text" value="Dana" name="gameName" id="textfield" />
                    </p>

                    <p>
                        choose number of human players 
                        <select name="humanPlayers" size="1" id="select">
                            <option>1</option>
                            <option>2</option>
                            <option>3</option>
                            <option>4</option>
                            <option>5</option>
                            <option>6</option>
                        </select>
                    </p>

                    <p>
                        choose number of computer players
                        <select name="compPlayers" id="select2">
                            <option>0</option>
                            <option SELECTED>1</option>
                            <option>2</option>
                            <option>3</option>
                            <option>4</option>
                            <option>5</option>
                        </select>
                    </p>

                    <p>
                        use automatic dice roll 
                        <input type="checkbox" name="isAutoDice" id="checkbox" />
                    </p>
                    </div>
                    
                    <div id = "currGame">
                        <p>current game:<br/><br/> <%= showCurrentGame(request) %> </p>
                    </div>
                    
                </form>
            </div>
            
        </div> 

    </body>
</html>
