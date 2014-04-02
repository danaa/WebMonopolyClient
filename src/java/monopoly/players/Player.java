package monopoly.players;


import java.util.ArrayList;
import monopoly.squares.Asset;


/**
 * this class represents a simple player in a monopoly game client
 * @author Dana Akerman
 */
public class Player 
{
    
    
    // data members
    //---------------------------------------------------------
    
    private String _name;
    private int _cash;
    private int _position;
    private boolean _active;
    private ArrayList<Asset> _assets;
    
    // c'tor
    //---------------------------------------------------------
    
    /**
     * constructs a player
     * @param name the name of the player
     * @param cash the amount of cash
     * @param color the color of the player
     * @param iconName the name of the icon
     * @param isActive true if the client is active in the game
     * @throws NullPointerException if name, color or iconName are null
     * @throws IllegalArgumentException if cash is negative
     */
    public Player(String name, int cash, boolean isActive)
    {
        
        this.setName(name);
        this.setCash(cash);
        _assets = new ArrayList<Asset>();
        _position = 0;
        _active = isActive;
        
    }
    
    // methods & functions
    //---------------------------------------------------------
    
    /**
     * gets the name of the player
     * @return the name of the player
     */
    public String getName(){return _name;}
    
    //---------------------------------------------------------
    
    /**
     * gets the cash of the player
     * @return the cash of the player
     */
    public int getCash(){return _cash;}
    
    //---------------------------------------------------------
    
    /**
     * gets the player position
     * @return the player position
     */
    public int getPosition(){return _position;}
    
    //---------------------------------------------------------
    
    /**
     * checks if the player is active in the game
     * @return true if the player is active
     */
    public boolean isActive(){return _active;}
    
    //---------------------------------------------------------
    
    /**
     * gets the assets of the player
     * @return the assets of the player
     */
    public ArrayList<Asset> getAssets(){return _assets;}
    
    //---------------------------------------------------------
    
    /**
     * adds an asset to the player
     * @param asset the asset to add
     */
    public void addAsset(Asset asset)
    {
        _assets.add(asset);
    }
    
    //---------------------------------------------------------
    
    /**
     * clears the player's assets
     */
    public void clearAssets()
    {
        _assets.clear();
    }
    
    //---------------------------------------------------------
    
    /**
     * sets the name of the player
     * @param name the name to set
     * @throws NullPointerException if name is null
     */
    public final void setName(String name)
    {
        if(name != null)
        {
            _name = name;
        }
        else
            throw new NullPointerException("player name is null");
    }
    
    //---------------------------------------------------------
    
    /**
     * sets the position of the player
     * @param pos the position to set
     */
    public final void setPosition(int pos)
    {
        _position = pos;
    }
    
    //---------------------------------------------------------
    
    /**
     * sets the cash of the player
     * @param cash the cash to set
     * @throws IllegalArgumentException if cash is negative
     */
    public final void setCash(int cash)
    {
        if(cash >= 0)
        {
            _cash = cash;
        }
        else
            throw new IllegalArgumentException("illegal cash");
    }
    
    //---------------------------------------------------------
    
    /**
     * adds cash to the player
     * @param amount the amount of cash to add
     * assumes the amount is legal
     */
    public void addCash(int amount)
    {
        this.setCash(_cash + amount);
    }
    
    //---------------------------------------------------------
    
    /**
     * reduces cash from the player
     * @param amount the amount to reduce
     * assumes the amount is legal
     */
    public void reduceCash(int amount)
    {
        this.setCash(_cash - amount);
    }
    
    //---------------------------------------------------------
    
    /**
     * removes the player from the game
     */
    public void remove()
    {
        _active = false;
    }
    
    //---------------------------------------------------------
    
    @Override
    public String toString()
    {
        return _name + ", cash: " + _cash;
    }
}
