package monopoly.squares;


import monopoly.groups.AssetGroup;
import monopoly.groups.SimpleAssetGroup;


/**
 * this class represent a simple asset (util, transportation) in a monopoly game web client
 * @author Dana Akerman
 */

public class SimpleAsset extends Asset 
{
    
    // data members
    //--------------------------------------------------------------

    private int _cost;
    private int _rent;

    // c'tor
    //--------------------------------------------------------------
    
    /**
     * constructs a new simple asset
     * @param group the group this asset belongs to
     * @throws NullPointerException if 'group' is null
     */
    public SimpleAsset(AssetGroup group) 
    {
        super(group);
    }

    // methods
    //--------------------------------------------------------------
    
    /**
     * initializes the simple asset from the XMLasset Asset object created from the 
     * schema generated class 'Asset'
     * @param XMLasset the XMLasset object created from the schema generated class 'Asset'
     * @throws NullPointerException if 'XMLasset' is null
     */
    public void init(generated.Asset XMLasset) 
    {
        if (XMLasset == null) 
            throw new NullPointerException("XMLasset is null");

        this.setName(XMLasset.getName());
        this.setCostPrice(XMLasset.getCosts().get(0).intValue());
        this.setRentPrice(XMLasset.getCosts().get(1).intValue());
    }

    //--------------------------------------------------------------
    
    @Override
    public int getRentPrice(){return _rent;}

    //--------------------------------------------------------------
    
    /**
     * sets the cost of the asset
     * @param cost - the cost of the asset
     * @throws IllegalArgumentException if cost is none-positive
     */
    public void setCostPrice(int cost) 
    {
        if (cost > 0) 
        {
            _cost = cost;
        } 
        else 
            throw new IllegalArgumentException("illegal cost input");
    }

    //--------------------------------------------------------------
    
    /**
     * sets the rent of this asset
     * @param rent - the rent price of this asset
     * @throws IllegalArgumentException if rent is none-positive
     */
    public void setRentPrice(int rent) 
    {
        if (rent > 0) 
        {
            _rent = rent;
        } 
        else 
            throw new IllegalArgumentException("illegal rent input");
    }
    
    @Override
    public int getPrice() 
    {
        return _cost;
    }

    //--------------------------------------------------------------
    
    @Override
    public String showInfo() 
    {
        return ("asset cost: " + _cost + "$&#10rent: " + _rent + "$&#10whole group rent: "
                + ((SimpleAssetGroup) _group).getSpecialRentPriceForGroup() + "$");
    }

    //--------------------------------------------------------------
    
    @Override
    public String toString() 
    {
        return ("asset: " + _name + "\ngroup: " + _group.getName());
    }

  
}
