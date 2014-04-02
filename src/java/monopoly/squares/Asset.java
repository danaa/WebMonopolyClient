package monopoly.squares;


import monopoly.groups.AssetGroup;


/**
 * this class represent an asset in a monopoly game client
 * @author Dana Akerman
 */
public abstract class Asset extends Square
{
    
    // data members
    //----------------------------------------------------------------

    protected String _name;
    protected AssetGroup _group;

    // c'tor
    //----------------------------------------------------------------
    
    /**
     * constructs a new single asset. called only by derived classes
     * @param group the group this single asset belongs to
     * @throws NullPointerException if 'group' is null
     */
    protected Asset(AssetGroup group) 
    {
        super();
        this.setGroup(group);
    }

    // methods
    //---------------------------------------------------------
    
    /**
     * returns the name of the asset
     * @return the name of the asset
     */
    public String getName(){return _name;}

    //---------------------------------------------------------
    
    /**
     * returns the group this asset belongs to
     * @return the group this asset belongs to
     */
    public AssetGroup getGroup(){return _group;}

    //---------------------------------------------------------
    
    /**
     * returns the rent of the asset
     * @return the rent of the asset
     */
    public abstract int getRentPrice();
    
    //---------------------------------------------------------
    
    @Override
    public boolean isAsset(){return true;}
    
    public abstract int getPrice();
    
    //---------------------------------------------------------
    
    /**
     * sets the group
     * @param group the group this asset belongs to
     * @throws NullPointerException if 'group' is null
     */
    public final void setGroup(AssetGroup group) 
    {
        if (group != null) 
        {
            _group = group;
        } 
        else 
            throw new NullPointerException("group is null");
    }

    //---------------------------------------------------------
    
    /**
     * sets the name of the asset
     * @param name the name of the asset
     * @throws NullPointerException if 'name' is null
     */
    public void setName(String name) 
    {
        if (name != null) 
        {
            _name = name;
        } 
        else 
            throw new NullPointerException("name is null");
    }

    //---------------------------------------------------------
    
    /**
     * shows info about the asset
     */
    public abstract String showInfo();
}
