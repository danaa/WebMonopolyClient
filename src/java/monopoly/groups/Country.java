package monopoly.groups;


import monopoly.squares.CityAsset;


/**
 * this class represents a country in a monopoly game client
 * @author Dana Akerman
 */
public class Country extends AssetGroup 
{
    
    // methods
    //-------------------------------------------------------------------

    /**
     * initializes the country from the XMLcountry Country object created from the 
     * schema generated class 'Country'
     * @param XMLcountry the XMLcountry Country object created from the schema generated class 'Country'
     * @throws NullPointerException if 'XMLcountry' is null
     */
    
    public void init(generated.Country XMLcountry) 
    {
        if (XMLcountry == null) 
            throw new NullPointerException("XMLcountry is null");


        int numCities = XMLcountry.getSize();
        this.setName(XMLcountry.getName());
        this.setIconName(XMLcountry.getIcon());

        // init cities
        for (int j = 0; j < numCities; j++) 
        {
            generated.City XMLcity = XMLcountry.getCity().get(j);
            CityAsset myCity = new CityAsset(this);
            myCity.init(XMLcity);
            this.addAsset(myCity);
        }
    }

    //-------------------------------------------------------------------
    
    @Override
    public String toString() 
    {
        return ("country: " + _name);
    }
}
