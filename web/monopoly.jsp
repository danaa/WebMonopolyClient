<%-- 
    represents the game page
    Author     : Dana Akerman
--%>

<%@page import="monopoly.squares.CityAsset"%>
<%@page import="monopoly.squares.Asset"%>
<%@page import="monopoly.squares.Square"%>
<%@page import="monopoly.Game"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<head>
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Monopoly</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="css/same-style-all-browsers.css">
    <link rel="stylesheet" href="css/monopoly.css">
    
    <%!
        
        // functions
        //------------------------------------------------------------------------

        private String getIconBySquareIndex(int index, HttpServletRequest request)
        {
            String path = "css/images/dynamicSquares/";
            Game game = (Game)request.getSession().getAttribute("game");
            Square board[] = game.getBoard();
            
            Square square = board[index];
            
            if(square instanceof Asset)
            {
                Asset asset = (Asset)square;
                String icon = asset.getGroup().getIconName();
                return path + icon;
            }
            
            return "";
        }
        
        //------------------------------------------------------------------------
        
        private String getInfoBySquareIndex(int index, HttpServletRequest request)
        {
            Game game = (Game)request.getSession().getAttribute("game");
            Square board[] = game.getBoard();
            
            Square square = board[index];
            
            if(square instanceof Asset)
            {
                return ((Asset)square).showInfo();
            }
            
            return "";
        }
        
        //------------------------------------------------------------------------
        
        private String getAssetNameBySquareIndex(int index, HttpServletRequest request)
        {
            Game game = (Game)request.getSession().getAttribute("game");
            Square board[] = game.getBoard();
            
            Square square = board[index];
            
            if(square instanceof Asset)
            {
                return ((Asset)square).getName();
            }
            
            return "";
        }
    %>
    
   
    
    
    <style type ="text/css">
     
    <%
        // print the css
    
    
        Game game = (Game)request.getSession().getAttribute("game");
        Square board[] = game.getBoard();
        for(int i = 0; i < board.length; i++)
        {
            Square square = board[i];
            if(square instanceof Asset)
            {
                out.println("#cell" + (i+1) + " div.level1{background: url(" + getIconBySquareIndex(i,request)
                        + ") no-repeat center top; height: 60%; width:100%; padding:0; margin:0}");
            }
        }
    %>    

    </style>

</head>

<body>

    <div id="container">

        <div id="main">
            
            <div id="monopoly-frame" class="board">
                
                <div class="cell northwest level0" id="cell1" title = "start square">
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>
                            </div>
                        </div>
                    </div>        
                </div>
                
                <div class="cell north level0" id="cell2" title = <%= "'" + getInfoBySquareIndex(1, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>   
                            </div>  
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(1, request) %> </p>
                </div>
                
                <div class="cell north level0" id="cell3" title= <%= "'" + getInfoBySquareIndex(2, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>    
                            </div>   
                        </div>
                    </div>  
                    <p> <%= getAssetNameBySquareIndex(2, request) %> </p>
                </div>
                
                <div class="cell north level0" id="cell4" title= "surprise">
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>  
                            </div>
                        </div>
                    </div> 
                </div>
                
                <div class="cell north level0" id="cell5" title= <%= "'" + getInfoBySquareIndex(4, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>   
                            </div>  
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(4, request) %> </p>
                </div>
                
                <div class="cell north level0" id="cell6" title= <%= "'" + getInfoBySquareIndex(5, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>      
                            </div>    
                        </div> 
                    </div>
                    <p> <%= getAssetNameBySquareIndex(5, request) %> </p>
                </div>
                
                <div class="cell north level0" id="cell7" title= <%= "'" + getInfoBySquareIndex(6, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>       
                            </div>   
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(6, request) %> </p>
                </div>
                
                <div class="cell north level0" id="cell8" title =<%= "'" + getInfoBySquareIndex(7, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>  
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(7, request) %> </p>
                </div>
                
                <div class="cell north level0" id="cell9" title = "warrant">
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>  
                            </div>  
                        </div>   
                    </div> 
                     <p> <%= getAssetNameBySquareIndex(8, request) %> </p>
                </div>
                    
                <div class="cell northeast level0" id="cell10" title ="jail/free pass">
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>    
                            </div> 
                        </div> 
                    </div>
                </div>
                
                <div class="cell east level0" id="cell11" title =<%= "'" + getInfoBySquareIndex(10, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'> 
                            </div> 
                        </div>
                    </div> 
                    <p> <%= getAssetNameBySquareIndex(10, request) %> </p>
                </div>
                    
                <div class="cell east level0"  id="cell12" title =<%= "'" + getInfoBySquareIndex(11, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>  
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(11, request) %> </p>
                </div>
                    
                <div class="cell east level0"  id="cell13" title =<%= "'" + getInfoBySquareIndex(12, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>          
                            </div> 
                        </div>  
                    </div>   
                    <p> <%= getAssetNameBySquareIndex(12, request) %> </p>
                </div>
                
                <div class="cell east level0"  id="cell14" title =<%= "'" + getInfoBySquareIndex(13, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>   
                            </div>  
                        </div>  
                    </div>
                    <p> <%= getAssetNameBySquareIndex(13, request) %> </p>
                </div>
                
                <div class="cell east level0"  id="cell15" title =<%= "'" + getInfoBySquareIndex(14, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>
                                
                            </div> 
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(14, request) %> </p>
                </div>
                
                <div class="cell east level0"  id="cell16" title =<%= "'" + getInfoBySquareIndex(15, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>  
                            </div>   
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(15, request) %> </p>
                </div>
                
                <div class="cell east level0"  id="cell17" title =<%= "'" + getInfoBySquareIndex(16, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>   
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(16, request) %> </p>
                </div>
                
                    <div class="cell east level0"  id="cell18" title =<%= "'" + getInfoBySquareIndex(17, request) + "'"%>>
                        <div class='level1'>
                            <div class='level2'>
                                <div class='level3'> 
                                </div>
                            </div>
                        </div>
                        <p> <%= getAssetNameBySquareIndex(17, request)%> </p>
                    </div>
                
                <div class="cell southeast level0" id="cell19" title = "parking">
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>   
                            </div>
                        </div>
                    </div>
                </div>
                    
                <div class="cell south level0"  id="cell20" title =<%= "'" + getInfoBySquareIndex(19, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>     
                            </div>    
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(19, request)%> </p>
                </div>
                
                <div class="cell south level0"  id="cell21" title =<%= "'" + getInfoBySquareIndex(20, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>    
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(20, request)%> </p>
                </div>
                
                <div class="cell south level0"  id="cell22" title =<%= "'" + getInfoBySquareIndex(21, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'> 
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(21, request)%> </p>
                </div>
                    
                <div class="cell south level0"  id="cell23" title = "warrant">
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>  
                            </div>
                        </div>
                    </div>
                </div>
                    
                <div class="cell south level0"  id="cell24" title =<%= "'" + getInfoBySquareIndex(23, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>     
                            </div> 
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(23, request)%> </p>
                </div>
                    
                <div class="cell south level0"  id="cell25" title =<%= "'" + getInfoBySquareIndex(24, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>  
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(24, request)%> </p>
                </div>
                    
                <div class="cell south level0"  id="cell26" title =<%= "'" + getInfoBySquareIndex(25, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>    
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(25, request)%> </p>
                </div>
                    
                <div class="cell south level0" id="cell27" title =<%= "'" + getInfoBySquareIndex(26, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>
                            </div> 
                        </div> 
                    </div>  
                    <p> <%= getAssetNameBySquareIndex(26, request)%> </p>
                </div>
                    
                <div class="cell southwest level0"  id="cell28" title = "go to jail">
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>     
                            </div>  
                        </div>
                    </div>
                </div>
                
                <div class="cell west level0"  id="cell29" title =<%= "'" + getInfoBySquareIndex(28, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>  
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(28, request)%> </p>
                </div>
                
                <div class="cell west level0"  id="cell30" title =<%= "'" + getInfoBySquareIndex(29, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>     
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(29, request)%> </p>
                </div>
                
                <div class="cell west level0"  id="cell31" title =<%= "'" + getInfoBySquareIndex(30, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'> 
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(30, request)%> </p>
                </div>
                
                <div class="cell west level0"  id="cell32" title =<%= "'" + getInfoBySquareIndex(31, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>  
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(31, request)%> </p>
                </div>
                
                <div class="cell west level0"  id="cell33" title =<%= "'" + getInfoBySquareIndex(32, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>  
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(32, request)%> </p>
                </div>
                
                <div class="cell west level0"  id="cell34" title =<%= "'" + getInfoBySquareIndex(33, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'> 
                            </div>
                        </div>
                    </div>
                    <p> <%= getAssetNameBySquareIndex(33, request)%> </p>
                </div>
               
                <div class="cell west level0"  id="cell35" title =<%= "'" + getInfoBySquareIndex(34, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>
                            </div>
                        </div> 
                    </div>
                    <p> <%= getAssetNameBySquareIndex(34, request)%> </p>
                </div>
                
                <div class="cell west level0"  id="cell36" title =<%= "'" + getInfoBySquareIndex(35, request) + "'"%>>
                    <div class='level1'>
                        <div class='level2'>
                            <div class='level3'>
                            </div>   
                        </div>  
                    </div>
                    <p> <%= getAssetNameBySquareIndex(35, request)%> </p>
                </div>

                    
            </div>  

        </div>

    </div>


    <script type="text/javascript" src="js/libs/jquery-1.6.1.min.js"></script>
    <script type="text/javascript" src='js/monopoly.js'></script>
    <!--[if lt IE 7 ]><script src="js/libs/dd_belatedpng.js"></script>
      <script>DD_belatedPNG.fix("img, .png_bg");</script><![endif]-->

</body>
</html>