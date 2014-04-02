package monopoly.groups;


import monopoly.squares.Asset;
import java.util.ArrayList;


/**
 * this class represents an asset group in a monopoly client
 * @author Dana Akerman
 */
public class AssetGroup 
{
    
    // data members
    //-------------------------------------------------------

    private String _iconName;
    protected String _name;
    protected ArrayList<Asset> _assets;

    // c'tor
    //-------------------------------------------------------
    
    /**
     * constructs a new asset group. called only by the derived classes
     */
    protected AssetGroup() 
    {
        _assets = new ArrayList<Asset>();
    }

    // methods
    //-------------------------------------------------------
    
    /**
     * returns the name of the asset group
     * @return the name of the asset group
     */
    public String getName(){return _name;}

    //-------------------------------------------------------------------
    
    /**
     * gets the icon name
     * @return the icon name
     */
    public String getIconName(){return _iconName;}

    //-------------------------------------------------------------------
    
    /**
     * returns the number of assets in this group
     * @return the number of assets in this group
     */
    public int getNumAssets(){return _assets.size();}

    //-------------------------------------------------------------------
    
    /**
     * gets an asset by the given index
     * @param index the index of the asset
     * @return the asset in the index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public Asset getAssetByIndex(int index) 
    {
        if (index >= 0 || index < _assets.size()) 
        {
            return _assets.get(index);
        } 
        else 
            throw new IndexOutOfBoundsException("asset index is out of bounds");
    }

    //-------------------------------------------------------------------
    
    /**
     * gets a string with the icon name
     * sets the group icon name
     * @throws NullPointerException if iconName is null
     */
    public void setIconName(String iconName) 
    {
        if (iconName != null) 
        {
            _iconName = iconName;
        } 
        else 
            throw new NullPointerException("icon name is null");
    }

    //-------------------------------------------------------------------
    
    /**
     * adds an asset to the group
     * @param asset the asset to add
     * @throws NullPointerException if 'asset' is null
     */
    public void addAsset(Asset asset) 
    {
        if (asset != null) 
        {
            _assets.add(asset);
        } 
        else 
            throw new NullPointerException("asset is null");
    }

    //-------------------------------------------------------------------
    
    /**
     * sets the name of the group
     * @param name the name of the group
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
}
