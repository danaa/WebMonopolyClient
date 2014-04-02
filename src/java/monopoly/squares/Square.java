package monopoly.squares;


/**
 * this class represents a square in a monopoly game web client
 * @author Dana Akerman
 */
public abstract class Square 
{
    // data members
    //---------------------------------------------------------
    
    private int _address;
    
    // methods
    //---------------------------------------------------------
    
    /**
     * checks if the square is an asset
     * @return true if the square is an asset
     */
    public abstract boolean isAsset();
    
    //---------------------------------------------------------
    
    /**
     * sets the address of the square
     * @param address the address to set
     */
    public void setAddress(int address)
    {
        _address = address;
    }
    
    //---------------------------------------------------------
    
    /**
     * gets the address of the square
     * @return the address of the square
     */
    public int getAddress()
    {
        return _address;
    }
}
