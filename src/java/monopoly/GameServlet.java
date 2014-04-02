package monopoly;


import client.*;
import communication.Event;
import generated.Monopoly;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import monopoly.players.Player;
import monopoly.squares.Asset;
import monopoly.squares.CityAsset;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * this class represents s game servelt
 * the game servlet handles requesets during the game
 * @author Dana Akerman
 */
public class GameServlet extends HttpServlet 
{
    
    // constants
    //------------------------------------------------------------------------
    
    public static final String CONTENT_TYPE_JSON = "application/json";

    // methods
    //------------------------------------------------------------------------
    
    /** 
     * Processes requests for <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {

        String playerName = request.getParameter("playerName");

        if(playerName != null) // we got here with player name param means we just joined
        {
         
            // save needed attributes in session
            request.getSession().setAttribute("playerName", playerName);
            request.getSession().setAttribute("lastEvent", 0);  // no events yet
            request.getSession().setAttribute("isActive", true);
            ConcurrentLinkedQueue<Event> eventsQue = new ConcurrentLinkedQueue<Event>();
            request.getSession().setAttribute("eventsQue", eventsQue);

            // construct the inner game model and save it in session
            String xmlString = Server.getGameBoardXML();
            Monopoly temp = MonopolyUtils.createObjectFromXmlString(xmlString);
            Game gameModel = new Game(temp);         
            
            // get game name
            List<String> gameName = Server.getWaitingGames();
            if (gameName.isEmpty()) 
            {
                gameName = Server.getActiveGames();
            }
            gameModel.setName(gameName.get(0));

            // set game model as sttribute in session
            request.getSession().setAttribute("game", gameModel);

            this.getServletContext().getRequestDispatcher("/monopoly.jsp").forward(request, response);
        }
        
        else // we got here with answer/resign parameter from user within the game
        {
            
            String action = request.getParameter("action");
            
            Integer id = (Integer)request.getSession().getAttribute("id");
            Integer lastEvent = (Integer)request.getSession().getAttribute("lastEvent");

            boolean succeed = false;
            
            if(action.equals("buy"))
            {
                boolean answer = false;
                String answerStr = request.getParameter("answer");
                if(answerStr.equals("yes"))
                {
                    answer = true;
                }
                else if(answerStr.equals("no"))
                {
                    answer = false;
                }
                
                succeed = Server.buy(id, lastEvent, answer);
                
            }
            else if(action.equals("dices"))
            {
                String dice1 = request.getParameter("cube1");
                String dice2 = request.getParameter("cube2");
         
                succeed = Server.setDiceRollResults(id, lastEvent, Integer.parseInt(dice1), Integer.parseInt(dice2));
               
            }
            else // resign
            {
                 succeed = Server.resign(id);
                 request.getSession().setAttribute("isActive", false);  
            }
            
            if(!succeed) // the user is not in the game
            {
                request.getSession().setAttribute("isActive", false);
            }
            
        }
            
    }
    
    //------------------------------------------------------------------------
    
    /**
     * checks if the given event refers to this client
     * @param e the event
     * @param request servlet request
     * @return true if the given event refers to this client
     */
    private boolean isMyEvent(Event e, HttpServletRequest request)
    {
        return (e.getPlayerName().getValue().equals(request.getSession().getAttribute("playerName")));
    }
    
    //------------------------------------------------------------------------
    
    /**
     * gets the name of the player this event refers to
     * if it refers to this client returns "you"
     * @param e the event
     * @param request servlet request
     * @return the name of the player this event refers to
     */
    private String whosEvent(Event e, HttpServletRequest request)
    {
          String name = e.getPlayerName().getValue();
          if(this.isMyEvent(e, request))
          {
              name = "you";
          }
          return name;
    }
    
    //------------------------------------------------------------------------
    
    /**
     * handles an event
     * @param e the event to handle
     * @param json JSONObject to write the event in
     * @param request servlet request
     * @param response servlet response
     */
    private void eventHandler(Event e, JSONObject json, HttpServletRequest request, HttpServletResponse response)
    {
        
        switch(e.getEventType())
        {
            case 1: // start
                
                json.put("line", "game started");
                break;
                
            case 2: // game over
                
                this.addMessageToJson(json, "game over... redirecting to index", "message");
                request.getSession().setAttribute("isActive", false);
                json.put("redirect", true);
                
                break;
                
            case 3: // game winner
                
                if(this.isMyEvent(e, request))
                {
                    this.addMessageToJson(json, "congratulations! you win!", "closableMessage");
                }
                break;
                
            case 4: // player resigned
                
                this.performResign(json, e, request);
                break;
                
            case 5: // player lost
                
                this.performPlayerLost(json, e, request);
                break;
                
            case 6: // prompt player to roll dice
                
                if(this.isMyEvent(e, request))
                {
                    json.put("dices", true);
                }
                break;
                
            case 7: // dice roll
                
                this.performRollDice(json, e, request);
                break;
                
            case 8: // move
                
                this.performMove(e, request);
                break;
                
            case 9: // passed start square
                
                json.put("line", this.whosEvent(e, request) + " passed on start square");
                break;
                
            case 10: // landed on start square
                
                json.put("line", this.whosEvent(e, request) + " landed on start square");
                break;
                
            case 11: // sent to jail message
                
                String s = "was";
                if(this.isMyEvent(e, request))
                {
                    s = "were";
                }
                json.put("line", this.whosEvent(e, request) + " commited a horrible crime and " + s + " sent to jail");
                break;
                
            case 12: // prompt to buy asset
                
                if(this.isMyEvent(e, request))
                {
                    this.performPromptBuy("asset", e, request, json);
                }
                break;
                
            case 13: // prompt to buy house
                
                if(this.isMyEvent(e, request))
                {
                    this.performPromptBuy("house", e, request, json);
                }
                break;
                
            case 14: // asset bought
                
                this.performAssetBought(e, request);
                break;
                
            case 15: // house bought
                
                this.performHouseBought(e, request);
                break;
                
            case 16: // surprise card
                
                this.performCardPicked("surprise", e, request, json);      
                break;
                
            case 17: // warrant card
                
                this.performCardPicked("warrant", e, request, json);
                break;
                
            case 18: // pardon card received
                
                json.put("line", this.whosEvent(e, request) + " recieved a pardon card");
                break;
                
            case 19: // payment
                
                this.performPayment(json, e, request);
                break;
                
            case 20: // pardon card used
                
                json.put("line", this.whosEvent(e, request) + " used a pardon card to get out of jail");
                break;
        }
        
    }
    
    //------------------------------------------------------------------------
    
    /**
     * prints the json
     * @param json the json to print
     * @param response servlet response
     */
    private void sendJson(JSONObject json, HttpServletResponse response)
    {
        response.setContentType(CONTENT_TYPE_JSON);
        response.setHeader("pragma-", "no-cache");
        PrintWriter out = null;
        
        try 
        {
            out = response.getWriter();
            out.println(json.toString());
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(GameServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            out.close();
        }
    }
    
    //-----------------------------------------------------------------
    
    /**
     * removes a player from the game
     * @param name the name of the player to remove
     * @param request servlet request
     */
    private void removePlayer(String name, HttpServletRequest request)
    {
        Game game = (Game)request.getSession().getAttribute("game");
        Player player = game.getPlayerByName(name);
        player.remove();

        player.clearAssets();
    }
    
    //-----------------------------------------------------------------
    
    /**
     * performs resign event
     * @param json JSONobject to write the event in
     * @param e the event
     * @param request servlet request
     */
    private void performResign(JSONObject json, Event e, HttpServletRequest request)
    {
        String name = e.getPlayerName().getValue();
        
        this.removePlayer(name, request);
        
        if (this.isMyEvent(e, request)) 
        {
            this.addMessageToJson(json, "forced resign by server. redirecting to index", "message");
            request.getSession().setAttribute("isActive", false);
            json.put("redirect", true);
        } 
        else 
        {
            json.put("line", name + " has left the game");
        }
    }
    
    //-----------------------------------------------------------------
    
    /**
     * performs dice roll event
     * @param json JSONobject to write the event in
     * @param e the event
     * @param request servlet request
     */
    private void performRollDice(JSONObject json, Event e, HttpServletRequest request)
    {
        int dice1 = e.getFirstDiceResult();
        int dice2 = e.getSecondDiceResult();
        JSONObject diceJson = new JSONObject();
        diceJson.put("dice1", dice1);
        diceJson.put("dice2", dice2);
        json.put("dice", diceJson);

        String name = this.whosEvent(e, request);
        json.put("line", name + " rolled " + dice1 + " " + dice2);
    }
    
    //-----------------------------------------------------------------
    
    /**
     * performs player lost event
     * @param json JSONobject to write the event in
     * @param e the event
     * @param request servlet request
     */
    private void performPlayerLost(JSONObject json, Event e, HttpServletRequest request)
    {
        String name = e.getPlayerName().getValue();
        
        this.removePlayer(name, request);
        
        if (this.isMyEvent(e, request)) 
        {
            this.addMessageToJson(json, "boohoo.. you lose :( redirecting to index", "message");
            request.getSession().setAttribute("isActive", false);
            json.put("redirect", true);
        } 
        else 
        {
            json.put("line", name + " bankrupt and left the game");
        }
    }
    
    //-----------------------------------------------------------------
    
    /**
     * performs move event
     * @param e the event
     * @param request servlet request
     */
    private void performMove(Event e, HttpServletRequest request)
    {
        String playerName = e.getPlayerName().getValue();
        Game game = (Game)request.getSession().getAttribute("game");
        
        Player player = game.getPlayerByName(playerName);
        int pos2 = e.getNextBoardSquareID();
        player.setPosition(pos2);
    }
    
    //----------------------------------------------------------------
    
     /**
     * performs payment event
     * assumes the given event is a payment event
     * @param e the event
     * @param request servlet request
     */
    private void performPayment(JSONObject json, Event e, HttpServletRequest request)
    {
        Game game = (Game)request.getSession().getAttribute("game");
        
        int amount = e.getPaymentAmount();
        Player player = game.getPlayerByName(e.getPlayerName().getValue());

        if (e.isPaymentToOrFromTreasury()) 
        {
            if (e.isPaymemtFromUser()) 
            {
                json.put("line", this.whosEvent(e, request) + " paid " + amount + "$ to treasury"); 
                player.reduceCash(amount);
            } 
            else // user recieve money from treasury
            {
                json.put("line", this.whosEvent(e, request) + " got " + amount + "$ from treasury"); 
                player.addCash(amount);
            }
        } 
        else // payment to or from other player
        {
            Player other = game.getPlayerByName(e.getPaymentToPlayerName().getValue());
            
            String otherName = other.getName();
            if(otherName.equals((String)request.getSession().getAttribute("playerName")))
            {
               otherName = "you";
            }

            if (e.isPaymemtFromUser()) 
            {
                json.put("line", this.whosEvent(e, request) + " paid " + amount + "$ to " + otherName); 
                player.reduceCash(amount);
                other.addCash(amount);
            } 
            else // user recieve money from other player
            {
                json.put("line", otherName + " paid " + amount + "$ to " + this.whosEvent(e, request));
                player.addCash(amount);
                other.reduceCash(amount);
            }
        }
    }
    
    //------------------------------------------------------------------------
    
     /**
     * performs an asset bought event
     * assumes the given event is an asset bought event
     * @param e the event
     * @param request servlet request
     */
    private void performAssetBought(Event e, HttpServletRequest request)
    {
        Game game = (Game)request.getSession().getAttribute("game");
        String name = e.getPlayerName().getValue();
        
        int pos = e.getBoardSquareID();
        Asset asset = (Asset)game.getSquareByIndex(pos);
        asset.setAddress(pos + 1);
        game.getPlayerByName(name).addAsset(asset); 
    }
    
    //------------------------------------------------------------------------
    
    /**
     * performs a house bought event
     * assumes the given event is a house bought event
     * @param e the event
     * @param request servlet request
     */
    private void performHouseBought(Event e, HttpServletRequest request)
    {
        Game game = (Game)request.getSession().getAttribute("game");
        
        int pos = e.getBoardSquareID();
        CityAsset city = (CityAsset)game.getSquareByIndex(pos);
        city.addHouse();
    }
    
    //------------------------------------------------------------------------
    
    /**
     * performs a card event
     * @param type the type of the card
     * @param e the event
     * @param request servlet request
     * @param json JSONobject to write the event in
     */
    private void performCardPicked(String type, Event e, HttpServletRequest request, JSONObject json)
    {
        String text = e.getEventMessage().getValue();
        String playerName = this.whosEvent(e, request);
        String message = playerName + " recieved a " + type + " card:<br>";
        
        JSONObject cardJson = new JSONObject();
        cardJson.put("text", message + text);
        json.put(type, cardJson);
    }
    
    //------------------------------------------------------------------------
    
    /**
     * perform prompt buy event
     * @param what what to buy
     * @param e the event
     * @param request servlet request
     * @param json JSONobject to write the event in
     */
    private void performPromptBuy(String what, Event e, HttpServletRequest request, JSONObject json)
    {
        Game game = (Game)request.getSession().getAttribute("game");
        Asset asset = (Asset)game.getSquareByIndex(e.getBoardSquareID());
        this.addQuestionToJson(json, "would you like to buy a house?");
        int price;
        
        if(what.equals("house"))
        {
            price = ((CityAsset)asset).getHouseCostPrice();
            what = "a house";
        }
        else // asset
        {
            price = asset.getPrice();
            what = asset.getName();
        }
            
        this.addQuestionToJson(json, "would you like to buy " + what + " for " + price + "$ ?");
    }
    
    //------------------------------------------------------------------------
    
    /**
     * adds all the players info (name, assets, position, cash) to a json object
     * @param json JSONobject to write the info in
     * @param request servlet request
     */
    private void addPlayersInfoToJson(JSONObject json, HttpServletRequest request)
    {
        
        Game game = (Game)request.getSession().getAttribute("game");
        ArrayList<Player> players = game.getPlayers();
        JSONArray playersJson = new JSONArray();
        
        for(int i = 0; i < players.size(); i++)
        {
            Player player = players.get(i);
            
            if(player.isActive())
            {
                JSONObject playerJson = new JSONObject();
                playerJson.put("id", i+1);
                playerJson.put("name", player.getName());
                playerJson.put("location", player.getPosition()+1);
                playerJson.put("fonds", player.getCash());
                JSONArray assetsJson = new JSONArray();
                
                ArrayList<Asset> assets = player.getAssets();
                for(int j = 0; j < assets.size(); j++)
                {
                    Asset asset = assets.get(j);
                    JSONObject assetJson = new JSONObject();
                    assetJson.put("address", asset.getAddress());
                    if (asset instanceof CityAsset) 
                    {
                        CityAsset city = (CityAsset) asset;
                        int numHouses = city.getNumHouses();
                        if (numHouses > 0) 
                        {
                            assetJson.put("houses", numHouses);
                        }
                    }
                    assetsJson.put(assetJson);
                }
                
                playerJson.put("assets", assetsJson);
                playersJson.put(playerJson);
            }
            
        }
        
        json.put("players", playersJson);
    }
    
     //------------------------------------------------------------------------
    
    /**
     * adds a question dialog to json object
     * @param json JSONobject to write the dialog in
     * @param question the question to ask
     */
    private void addQuestionToJson(JSONObject json, String question)
    {
        JSONObject dialogJson = new JSONObject();
        dialogJson.put("question", question);
        dialogJson.put("option1", "yes");
        dialogJson.put("option2", "no");
        json.put("dialog", dialogJson);
    }
    
    //------------------------------------------------------------------------
    
    /**
     * adds a message to a json object
     * @param json JSONobject to write the event in
     * @param message the message to add
     * @param type the type of the message - message(popup) or closableMessage
     */
    private void addMessageToJson(JSONObject json, String message, String type)
    {
        JSONObject messageJson = new JSONObject();
        messageJson.put("text", message);
        json.put(type, messageJson);
    }
    
    //------------------------------------------------------------------------
    
    /**
     * polls events from the web service
     * @param json JSONobject to write the event in
     * @param request servlet request
     * @param response servlet response
     */
    private void pollEventsFromServer(JSONObject json, HttpServletRequest request, HttpServletResponse response)
    {

        ConcurrentLinkedQueue<Event> eventsQue = (ConcurrentLinkedQueue<Event>) request.getSession().getAttribute("eventsQue");

        Integer lastEvent = (Integer) request.getSession().getAttribute("lastEvent");
        List<Event> events = Server.getAllEvents(lastEvent.intValue());

        if (!events.isEmpty()) 
        {
            eventsQue.addAll(events);
            request.getSession().setAttribute("lastEvent", lastEvent + events.size());
        } 

        Event e = eventsQue.poll();
        if (e != null) 
        {
            this.eventHandler(e, json, request, response);
        }
        
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        
        JSONObject json = new JSONObject();
        Boolean isActive = (Boolean)request.getSession().getAttribute("isActive");
        
        if(!isActive)
        {
            this.addMessageToJson(json, "forced resign by server. redirecting to index", "message");
            json.put("redirect", true);
        }
        else // active
        {

            Game game = ((Game) request.getSession().getAttribute("game"));
            GameDetails gameDetails = Server.getGameDetails(game.getName());

            // if the game hasn't started or just started
            if (gameDetails != null && game.getPlayers().size() < gameDetails.getCompPlayers() + gameDetails.getTotalHumanPlayers()) 
            {
            
                // get players details
                List<PlayerDetails> details = Server.getPlayersDetails(game.getName());

                for (int i = 0; i < details.size(); i++) 
                {
                    PlayerDetails playerDetails = details.get(i);

                    // add player to inner model
                    Player p = game.getPlayerByName(playerDetails.getName());

                    if (p == null) // add only if player doesnt exist
                    {
                        Player player = new Player(playerDetails.getName(), Game.START_CASH, true);
                        game.addPlayer(player);
                    
                        if(playerDetails.isHuman())
                        {
                            int playersToJoin = gameDetails.getTotalHumanPlayers()-gameDetails.currHumanPlayers();
                            if(playersToJoin > 0)
                            {
                                json.put("line","waiting for " + (gameDetails.getTotalHumanPlayers()-gameDetails.currHumanPlayers()) + " players to join");
                            }
                        }
                    }
                }
            } 
                
            else // game is active, poll events from server and process one at a time
            {
                // poll events
                this.pollEventsFromServer(json, request, response);
            }
        }
        
        this.addPlayersInfoToJson(json, request);
        this.sendJson(json, response);
        
        if(!isActive)
        {
            request.getSession().invalidate();
        }
    }
    
    //------------------------------------------------------------------------

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        processRequest(request, response);
    }
    
    //------------------------------------------------------------------------

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() 
    {
        return "Short description";
    }// </editor-fold>
}
