package monopoly.squares;


/**
 * this class represents a simple square (start, jail, parking, go to jail, card square) 
 * in a monopoly game client
 * @author Dana Akerman
 */
public class SimpleSquare extends Square
{
    
    //constants
    //----------------------------------------------------------
    
    public static final int START_SQUARE = 0;
    public static final int JAIL_OR_FREE_PASS = 1;
    public static final int GO_TO_JAIL = 3;
    public static final int PARKING = 4;
    public static final int SURPRISE = 5;
    public static final int WARRANT = 6;
    
    // data members
    //----------------------------------------------------------
    
    private int _type;
    
    // methods
    //----------------------------------------------------------
    
    @Override
    public boolean isAsset(){return false;}
    
    //----------------------------------------------------------
    
    /**
     * construsts a simple square
     * @param type the type of the square
     * @throws NullPointerException if type is illegal
     */
    public SimpleSquare(int type)
    {
        super();
        this.setType(type);
    }
    
    //----------------------------------------------------------
    
    /**
     * gets the type of the square
     * @return the type of the square
     */
    public int getType(){return _type;}
    
    //----------------------------------------------------------
    
    /**
     * sets the type of the square
     * @param type the type to set
     * @throws IllegalArgumentException if type is illegal
     */
    public final void setType(int type)
    {
        if(type >= START_SQUARE && type <= WARRANT)
        {
            _type = type;
        }
        else
            throw new IllegalArgumentException("illegal square type");
    }
    
    //----------------------------------------------------------
    
    @Override
    public String toString()
    {
        switch(_type)
        {
            case START_SQUARE:
                return "start";
            case JAIL_OR_FREE_PASS:
                return "jail<br>free pass";
            case GO_TO_JAIL:
                return "go to jail";
            case PARKING:
                return "parking";
            case SURPRISE:
                return "surprise";
            case WARRANT:
                return "warrant";
            default:
                return null;
        }
    }
}
