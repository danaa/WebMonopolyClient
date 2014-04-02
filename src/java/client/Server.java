package client;


import communication.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * this class represent the communication with the web service
 * @author Dana Akerman
 */
public class Server 
{
    // statics
    //-----------------------------------------------------------------------------------
    
    private static MonopolyGamePortType _server = new MonopolyGame().getMonopolyGameHttpSoap11Endpoint();
    
    // methods
    //-----------------------------------------------------------------------------------
    
    /**
     * gets the game board schema from the server
     * @return the game board schema
     */
    public static String getGameBoardSchema()
    {
        return _server.getGameBoardSchema().getReturn().getValue();
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * gets the game board xml as a string from the server
     * @return the game board xml as a string
     */
    public static String getGameBoardXML()
    {
        return _server.getGameBoardXML().getReturn().getValue();
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * starts a new game in the server
     * shows an error message if action failed
     * @param gameName the name of the game
     * @param humanPlayers the number of human players
     * @param computerPlayers the number of computer players
     * @param autoDice indicates if the game played with automatic dice
     * @return true if action succeeded, false otherwise
     */
    public static boolean startGame(String gameName, int humanPlayers, int computerPlayers, boolean autoDice)
    {
        MonopolyResult res = _server.startGame(gameName, humanPlayers, computerPlayers, autoDice);
        
        if(hasError(res))
        {
            if(res != null)
            {
                throw new Error(res.getErrorMessage().getValue());
            }
            return false;
        }
        
        return true;
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * gets the game details from the server
     * @param gameName the name of the game
     * @return the game details or null if error occurred
     */
    public static GameDetails getGameDetails(String gameName)
    {
        GameDetailsResult res = _server.getGameDetails(gameName);
        
        if(hasError(res))
        {
            return null;
        }
        else
        {
            return new GameDetails(gameName,
                    res.getTotalHumanPlayers().intValue(),
                    res.getJoinedHumanPlayers().intValue(),
                    res.getTotalComputerPlayers().intValue(),
                    res.isIsAutomaticDiceRoll().booleanValue(),
                    res.getStatus().getValue());
        }
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * gets the waiting games list
     * @return the waiting games list
     */
    public static List<String> getWaitingGames()
    {
        GetWaitingGamesResponse res = _server.getWaitingGames();
        
        if(res != null)
        {
            return res.getReturn();
        }
        else
        {
            return Collections.EMPTY_LIST;
        }
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * gets the active games list
     * @return the active games list
     */
    public static List<String> getActiveGames()
    {
        GetActiveGamesResponse res = _server.getActiveGames();
        
        if(res != null)
        {
            return res.getReturn();
        }
        else
        {
            return Collections.EMPTY_LIST;
        }
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * asks the server to join the game
     * shows an error message if action failed
     * @param gameName the name of the game
     * @param playersName the name of the player
     * @return a unique id from the server or -1 if action failed
     */
    public static int joinGame(String gameName, String playersName)
    {
        IDResult res = _server.joinGame(gameName, playersName);
        
        if(!hasError(res))
        {
            return res.getResult();
        }
        else
        {
            if(res != null)
            {
                throw new Error(res.getErrorMessage().getValue());
                //GUIclient.getGUI().showMessage("error", res.getErrorMessage().getValue());
            }
            return -1;
        }
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * gets a list of the players details from the server
     * @param gameName the name of the game
     * @return a list of the players details from the server or empty list if error occurred
     */
    public static List<PlayerDetails> getPlayersDetails(String gameName)
    {
        List<PlayerDetails> players = new LinkedList<PlayerDetails>();
        PlayerDetailsResult res = _server.getPlayersDetails(gameName);
        
        if (hasError(res)) 
        {
            return Collections.EMPTY_LIST;
        } 
        else 
        {
            for (int i = 0 ; i < res.getNames().size() ; i++) 
            {
                players.add(new PlayerDetails(
                        res.getNames().get(i),
                        res.getIsHumans().get(i),
                        res.getIsActive().get(i),
                        res.getMoney().get(i)));
            }
            return players;
        }
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * gets events from the server
     * @param eventID the last event id this client has
     * @return a list of events or empty list if error occurred
     */
    public static List<Event> getAllEvents(int eventID)
    {
        EventArrayResult res = _server.getAllEvents(eventID);
          
        if(hasError(res))
        {
            return Collections.EMPTY_LIST;
        }
        else
        {
            return res.getResults();
        }
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * sets the dice results in the server
     * @param playerID the id of this client
     * @param eventID the last event id of this client
     * @param dice1 the first dice
     * @param dice2 the second dice
     * @return true if action succeed, false otherwise
     */
    public static boolean setDiceRollResults(int playerID, int eventID, int dice1, int dice2)
    {
        MonopolyResult res = _server.setDiceRollResults(playerID, eventID, dice1, dice2);
        
        if(hasError(res))
        {
            return false;
        }

        return true;
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * sets the buy decision for this client in the server
     * @param playerID the client id
     * @param eventID the last event id this client has
     * @param buy the decision
     * @return true if action succeed, false otherwise
     */
    public static boolean buy(int playerID, int eventID, boolean buy)
    {
        MonopolyResult res = _server.buy(playerID, eventID, buy);
        
        if(hasError(res))
        {
            return false;
        }
         
        return true;
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * asks the server to resign
     * @param playerID the client id
     * @return true if action succeed, false otherwise
     */
    public static boolean resign(int playerID)
    {
        MonopolyResult res = _server.resign(playerID);
        
        if(hasError(res))
        {
            return false;
        }
        
        return true;
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * checks if the answer from the server contains an error
     * @param monopolyResult the answer from the server
     * @return true if there is an error, false otherwise
     */
    private static boolean hasError(MonopolyResult monopolyResult) 
    {
        if (monopolyResult == null || monopolyResult.isError())
        {
            return true;
        } 
        else 
        {
            return false;
        }
    }
    
   
   
}
