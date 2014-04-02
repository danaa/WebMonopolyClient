package monopoly;


import client.Server;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * this class represents a start servlet of a web monopoly game
 * this servlet handles the login to the game
 * @author dana
 */
public class StartServlet extends HttpServlet 
{
    

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        
        // no caching!
        response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
        response.setHeader("Pragma","no-cache"); //HTTP 1.0
        response.setDateHeader ("Expires", 0); //prevents caching at the proxy server       
 
        String playerName = request.getParameter("playerName");
        
        if (request.getParameter("option").equals("start")) // user chose to start a new game
        {
            
            // get new game parameters from the form
            String gameName = request.getParameter("gameName");
            String numHuman = request.getParameter("humanPlayers");
            String numComp = request.getParameter("compPlayers");
            String isAutoDice = request.getParameter("isAutoDice");

            int human = Integer.parseInt(numHuman);
            int comp = Integer.parseInt(numComp);
            boolean isAuto = (isAutoDice != null)? true : false;
                    
            try
            {
                // try to start the game and join it
                Server.startGame(gameName, human, comp, isAuto);
                this.joinGame(gameName, playerName, request, response);
            }
            catch(Error e)
            {
                // if there's an error show the message in the index
                this.showMessageInIndex( e.getMessage(), request, response);
            }
        } 
        
        else // user chose to join game
        {
            try
            {  
                // get the game name
                String gameName;
                List<String> lst = Server.getWaitingGames();
                
                gameName = (lst.isEmpty())? null : lst.get(0);
                
                // try to join it
                this.joinGame(gameName, playerName, request, response);
            }
            catch(Error e)
            {
                this.showMessageInIndex( e.getMessage(), request, response);
            }

        }
        
    }
    
    //-------------------------------------------------------------------------------------
    
    /**
     * joins the game
     * @param gameName the name of the game
     * @param playerName the player name
     * @param request servlet request
     * @param response servlet response
     */
    private void joinGame(String gameName, String playerName, HttpServletRequest request, HttpServletResponse response)
    {
        int id = Server.joinGame(gameName, playerName);
        request.getSession().setAttribute("id", id);
        this.sendRequest("/GameServlet", request, response);  
    }
    
    //-------------------------------------------------------------------------------------
    
    /**
     * shows a message in the index page
     * @param message the message to show
     * @param request servlet request
     * @param response servlet response
     */
    private void showMessageInIndex(String message, HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("message", message);
        this.sendRequest("/index.jsp", request, response);
    }
    
    //-------------------------------------------------------------------------------------
    
    /**
     * sends a request to anothor servlet or jsp
     * @param toWho the path of the servlet/jsp
     * @param request servlet request
     * @param response servlet response
     */
    private void sendRequest(String toWho, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            this.getServletContext().getRequestDispatcher(toWho).forward(request, response);
        }
        catch(Exception ex)
        {
            Logger.getLogger(StartServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //-------------------------------------------------------------------------------------

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        processRequest(request, response);
    }
    
    //-------------------------------------------------------------------------------------

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        processRequest(request, response);
    }
    
    //-------------------------------------------------------------------------------------

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() 
    {
        return "This is a start servlet";
    }// </editor-fold>
}
