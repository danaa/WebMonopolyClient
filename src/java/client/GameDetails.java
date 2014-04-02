package client;


/**
 * this class represents game details in a monopoly game client
 * stores information received from the server
 * @author Dana Akerman
 */
public class GameDetails 
{
    
    // data members
    //-----------------------------------------------------------------------------------
    
    private String _gameName;
    private int _totalHumanPlayers;
    private int _currHumanPlayers;
    private int _computerPlayers;
    private boolean _autoDice;
    private String _status;
    
    // c'tor
    //------------------------------------------------------------------------
    
    /**
     * constructs GameDetails
     * @param gameName the name of the game
     * @param totalHuman the total number of human players
     * @param currHuman the current number of human players
     * @param compPlayers the number of computer players
     * @param autoDice indicates if the game is played with automatic dice
     * @param status the status of the game
     */
    public GameDetails(String gameName, int totalHuman, int currHuman, int compPlayers, boolean autoDice, String status)
    {
        _gameName = gameName;
        _totalHumanPlayers = totalHuman;
        _currHumanPlayers = currHuman;
        _computerPlayers = compPlayers;
        _autoDice = autoDice;
        _status = status;
    }
    
    // methods
    //------------------------------------------------------------------------
    
    /**
     * gets the game name
     * @return the game name
     */
    public String getGameName(){return _gameName;}
    
    //------------------------------------------------------------------------
    
    /**
     * gets the total number of human players
     * @return the total number of human players
     */
    public int getTotalHumanPlayers(){return _totalHumanPlayers;}
    
    //------------------------------------------------------------------------
    
    /**
     * gets the current number of human players
     * @return the current number of human players
     */
    public int currHumanPlayers(){return _currHumanPlayers;}
    
    //------------------------------------------------------------------------
    
    /**
     * gets the number of computer players
     * @return the number of computer players
     */
    public int getCompPlayers(){return _computerPlayers;}
    
    //------------------------------------------------------------------------
    
    /**
     * checks if the game played with automatic dice
     * @return true if the game played with automatic dice
     */
    public boolean getAutoDice(){return _autoDice;}
    
    //------------------------------------------------------------------------
    
    /**
     * gets the status of the game
     * @return the status of the game
     */
    public String getStatus(){return _status;}
    
}
