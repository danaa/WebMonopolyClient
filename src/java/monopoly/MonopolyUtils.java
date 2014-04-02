package monopoly;


//import monopolyWsClient.squares.*;
//import gui.GUImanager;
//import gui.components.squares.*;
import java.awt.Font;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


/**
 * this class is a collection of static methods needed in a monopoly game client
 * @author Dana Akerman
 */
public class MonopolyUtils 
{
    
    // constants
    //-----------------------------------------------------------------------

    public static final String IMAGES_FOLDER = "/resources/images/";

    // functions
    //-----------------------------------------------------------------------
    
    /**
     * gets an image with the given path from the image root directory
     * @param path the path from the image root directory
     * @return the image, or null if image doesn't exist
     * @throws IOException if something went wrong with image retrieving
     */
    /*public static Image getImage(String path) 
    {
        if (path == null || path.isEmpty()) 
        {
            return null;
        }

        URL imageURL = GUIclient.class.getResource(IMAGES_FOLDER + path);
        
        if (imageURL == null) 
        {
            return null;
        }

        try 
        {
            return ImageIO.read(imageURL);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            return null;
        }
    }*/

    //-----------------------------------------------------------------------
    
    /**
     * gets an image icon with the given path from the image root directory
     * @param path the path from the image root directory
     * @return the image icon, or null if doesn't exist
     * @throws IOException if something went wrong with image retrieving
     */
    /*public static ImageIcon getImageIcon(String path) 
    {
        if (path == null || path.isEmpty()) 
        {
            return null;
        }

        Image image = getImage(path);
        
        if (image == null) 
        {
            return null;
        }

        return new ImageIcon(image);
    }*/

    //-----------------------------------------------------------------------
    
    /**
     * creates a matching GUI square to the given logic square
     * @param square the logic square
     * @return a matching GUI square
     * @throws NullPointerException if square is null
     */
    /*public static SquarePanel createMatchingSquarePanel(Square square) 
    {
        if (square == null) 
            throw new NullPointerException();
        

        if (square instanceof CityAsset) 
        {
            return new CitySquarePanel(square);
        } 
        else if (square instanceof SimpleAsset) 
        {
            return new SimpleAssetSquarePanel(square);
        } 
        else if (square instanceof SimpleSquare) 
        {
            
            int type = ((SimpleSquare) square).getType();
            
            switch (type) 
            {
                case SimpleSquare.START_SQUARE:

                    return new StaticSquarePanel(square, GUImanager.STATIC_SQUARES_FOLDER + "go.gif");

                case SimpleSquare.JAIL_OR_FREE_PASS:

                    return new StaticSquarePanel(square, GUImanager.STATIC_SQUARES_FOLDER + "jail.gif");

                case SimpleSquare.PARKING:

                    return new StaticSquarePanel(square, GUImanager.STATIC_SQUARES_FOLDER + "parking.gif");

                case SimpleSquare.GO_TO_JAIL:

                    return new StaticSquarePanel(square, GUImanager.STATIC_SQUARES_FOLDER + "go_to_jail.jpg");

                case SimpleSquare.SURPRISE:

                    return new StaticSquarePanel(square, GUImanager.STATIC_SQUARES_FOLDER + "quest.gif");

                case SimpleSquare.WARRANT:

                    return new StaticSquarePanel(square, GUImanager.STATIC_SQUARES_FOLDER + "exclamation.gif");

                default:
                    break;

            }
        }
        
        return null;
    }*/

    //-----------------------------------------------------------------------
    
    /**
     * gets a matching icon path to the given dice result
     * @param result the dice result
     * @return the matching icon path or null if doesn't exist
     */
    /*public static String getMatchingDice(int result) 
    {
        switch (result) 
        {

            case 1:
                return GUImanager.DICE_FOLDER + "dice_one.jpg";
            case 2:
                return GUImanager.DICE_FOLDER + "dice_two.jpg";
            case 3:
                return GUImanager.DICE_FOLDER + "dice_three.jpg";
            case 4:
                return GUImanager.DICE_FOLDER + "dice_four.jpg";
            case 5:
                return GUImanager.DICE_FOLDER + "dice_five.jpg";
            case 6:
                return GUImanager.DICE_FOLDER + "dice_six.jpg";
            default:
                return null;
        }
    }*/

    //-----------------------------------------------------------------------
    
    /**
     * gets the default label font with the given size and style
     * @param size the size of the font
     * @param style the style of the font
     */
    public static Font getLabelFont(int size, int style) 
    {
        return new Font(UIManager.getDefaults().getFont("Label.font").getFontName(), style, size);
    }

    //-----------------------------------------------------------------------
    
    /**
     * gets the default text area font with the given size and style
     * @param size the size of the font
     * @param style the style of the font
     */
    public static Font getTextAreaFont(int size, int style) 
    {
        return new Font(UIManager.getDefaults().getFont("TextArea.font").getFontName(), style, size);
    }

    //-----------------------------------------------------------------------
    
    /**
     * makes the thread sleep
     * @param millies the time of sleep in milliseconds
     */
    public static void sleep(long millies) 
    {
        try 
        {
            Thread.sleep(millies);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------
    
    /**
     * makes the runnable run on the EDT
     * @param doRun the runnable to run on the EDT
     */
    public static void invokeAndWait(Runnable doRun) 
    {
        try 
        {
            SwingUtilities.invokeAndWait(doRun);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        } 
        catch (InvocationTargetException e) 
        {
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------
    
    /**
     * unmarshals xml file into a Monopoly object using JAXB
     * @param xmlString the xml file name
     * @return the Monopoly object or null if xmlString is null or unmarshaling failed
     */
    public static generated.Monopoly createObjectFromXmlString(String xmlString) 
    {
        if (xmlString == null) 
        {
            return null;
        }

        try 
        {
            ByteArrayInputStream is = new ByteArrayInputStream(xmlString.getBytes());
            generated.Monopoly temp = new generated.Monopoly();
            JAXBContext jc;
            jc = JAXBContext.newInstance(generated.Monopoly.class);
            Unmarshaller u;
            u = jc.createUnmarshaller();
            temp = (generated.Monopoly) u.unmarshal(is);
            return temp;
        } 
        catch (JAXBException e) 
        {
            return null;
        }
    }
}
