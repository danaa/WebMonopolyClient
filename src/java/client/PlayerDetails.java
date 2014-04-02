package client;


/**
 * this class represents player details in a monopoly game client
 * stores information received from the server
 * @author dana
 */
public class PlayerDetails 
{
    // data members
    //-----------------------------------------------------------------------------------
    
    private String _name;
    private boolean _isHuman;
    private boolean _isActive;
    private int _cash;
    
    // c'tor
    //-----------------------------------------------------------------------------------
    
    /**
     * constructs player details
     * @param name the name of the player
     * @param isHuman indicates if the player is human
     * @param isActive indicates if the player is active in the game
     * @param cash the amount of cash
     */
    public PlayerDetails(String name, boolean isHuman, boolean isActive, int cash)
    {
        _name = name;
        _isHuman = isHuman;
        _isActive = isActive;
        _cash = cash;
    }
    
    // methods
    //-----------------------------------------------------------------------------------
    
    /**
     * gets the name of the player
     * @return the name of the player
     */
    public String getName(){return _name;}
    
    //-----------------------------------------------------------------------------------
    
    /**
     * checks if the player is human
     * @return true if the player is human
     */
    public boolean isHuman(){return _isHuman;}
    
    //-----------------------------------------------------------------------------------
   
    /**
     * checks if the player is active
     * @return true if the player is active
     */
    public boolean isActive(){return _isActive;}
    
    //-----------------------------------------------------------------------------------
    
    /**
     * gets the cash amount the player has
     * @return the cash amount the player has
     */
    public int getCash(){return _cash;}
    
    
}
