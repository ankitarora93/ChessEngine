/* Author: Ankit Arora
 * Email: arora.ankit7@gmail.com	
 * Description: This class contains the function which are required to generate the moves for all the pieces and to check if the king is safe
 * 
 * */
public class Moves {
	
	/* Function to generate the list of possible moves for pawn*/
    public static String posibleP(int i) 
    {
        String list="", oldPiece;
        int r=i/8, c=i%8;
       
        for (int j=-1; j<=1; j+=2) 
        {
            try 
            {//capture
                if (Character.isLowerCase(Chess.chessBoard[r-1][c+j].charAt(0)) && i >= 16) 
                {
                    oldPiece=Chess.chessBoard[r-1][c+j];
                    Chess.chessBoard[r][c]=" ";
                    Chess.chessBoard[r-1][c+j]="P";
                    
                    if (kingSafe()) 
                    {
                        list=list+r+c+(r-1)+(c+j)+oldPiece;
                    }
                    
                    Chess.chessBoard[r][c]="P";
                    Chess.chessBoard[r-1][c+j]=oldPiece;
                }
            } catch (Exception e) {}
            
            try 
            {//promotion && capture
                if (Character.isLowerCase(Chess.chessBoard[r-1][c+j].charAt(0)) && i<16) 
                {
                    String[] temp={"Q","R","B","K"}; //list of pieces a pawn can be promoted to
                    
                    for (int k=0; k<4; k++) 
                    {
                        oldPiece=Chess.chessBoard[r-1][c+j];
                        Chess.chessBoard[r][c]=" ";
                        Chess.chessBoard[r-1][c+j]=temp[k];
                        
                        if (kingSafe()) 
                        {
                            //column1,column2,captured-piece,new-piece,P
                            list=list+c+(c+j)+oldPiece+temp[k]+"P";
                        }
                        
                        Chess.chessBoard[r][c]="P";
                        Chess.chessBoard[r-1][c+j]=oldPiece;
                    }
                }
            } catch (Exception e) {}
        }
        
        try 
        {//move one up
            if (" ".equals(Chess.chessBoard[r-1][c]) && i>=16) 
            {
                oldPiece=Chess.chessBoard[r-1][c];
                Chess.chessBoard[r][c]=" ";
                Chess.chessBoard[r-1][c]="P";
                
                if (kingSafe()) 
                {
                    list=list+r+c+(r-1)+c+oldPiece;
                }
                
                Chess.chessBoard[r][c]="P";
                Chess.chessBoard[r-1][c]=oldPiece;
            }
        } catch (Exception e) {}
        
        
        try 
        {//promotion && no capture
            if (" ".equals(Chess.chessBoard[r-1][c]) && i<16) 
            {
                String[] temp={"Q","R","B","K"};
                for (int k=0; k<4; k++) {
                    oldPiece=Chess.chessBoard[r-1][c];
                    Chess.chessBoard[r][c]=" ";
                    Chess.chessBoard[r-1][c]=temp[k];
             
                    if (kingSafe()) 
                    {
                        //column1,column2,captured-piece,new-piece,P
                        list=list+c+c+oldPiece+temp[k]+"P";
                    }
                    
                    Chess.chessBoard[r][c]="P";
                    Chess.chessBoard[r-1][c]=oldPiece;
                }
            }
        } catch (Exception e) {}
        
        try 
        {//move two up
            if (" ".equals(Chess.chessBoard[r-1][c]) && " ".equals(Chess.chessBoard[r-2][c]) && i >= 48) 
            {
                oldPiece=Chess.chessBoard[r-2][c];
                Chess.chessBoard[r][c]=" ";
                Chess.chessBoard[r-2][c]="P";
            
                if (kingSafe()) 
                {
                    list=list+r+c+(r-2)+c+oldPiece;
                }
                
                Chess.chessBoard[r][c]="P";
                Chess.chessBoard[r-2][c]=oldPiece;
            }
        } catch (Exception e) {}
        
        return list;
    }
    
    /* Function to generate possible moves for rook*/
    public static String posibleR(int i) 
    {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
    
        //Unlike other functions, we are using a single for-loop to generate moves for the rook. A rook can either move horizontally or vertically.
        for (int j=-1; j<=1; j+=2) 
        {
            // This block is to generate moves in the vertical direction
            try 
            {
                //Moving until we have blank spaces or we encounter a black piece(see the if-condition just below this
                while(" ".equals(Chess.chessBoard[r][c+temp*j]))
                {
                    oldPiece=Chess.chessBoard[r][c+temp*j];
                    Chess.chessBoard[r][c]=" ";
                    Chess.chessBoard[r][c+temp*j]="R";
                    
                    if (kingSafe()) 
                    {
                        list=list+r+c+r+(c+temp*j)+oldPiece;
                    }
                    
                    Chess.chessBoard[r][c]="R";
                    Chess.chessBoard[r][c+temp*j]=oldPiece;
                    
                    temp++;
                }
                
                if (Character.isLowerCase(Chess.chessBoard[r][c+temp*j].charAt(0))) 
                {
                    oldPiece=Chess.chessBoard[r][c+temp*j];
                    Chess.chessBoard[r][c]=" ";
                    Chess.chessBoard[r][c+temp*j]="R";
                    
                    if (kingSafe()) 
                    {
                        list=list+r+c+r+(c+temp*j)+oldPiece;
                    }
                    
                    Chess.chessBoard[r][c]="R";
                    Chess.chessBoard[r][c+temp*j]=oldPiece;
                }
            } catch (Exception e) {}
            
            temp=1;
            
            //This block is to generate moves in the horizontal direction
            try 
            {
                //Moving until we have blank spaces or we encounter a black piece(see the if-condition just below this
                while(" ".equals(Chess.chessBoard[r+temp*j][c]))
                {
                    oldPiece=Chess.chessBoard[r+temp*j][c];
                    Chess.chessBoard[r][c]=" ";
                    Chess.chessBoard[r+temp*j][c]="R";
                    
                    if (kingSafe()) 
                    {
                        list=list+r+c+(r+temp*j)+c+oldPiece;
                    }
                    
                    Chess.chessBoard[r][c]="R";
                    Chess.chessBoard[r+temp*j][c]=oldPiece;
                    temp++;
                }
                
                if (Character.isLowerCase(Chess.chessBoard[r+temp*j][c].charAt(0))) 
                {
                    oldPiece=Chess.chessBoard[r+temp*j][c];
                    Chess.chessBoard[r][c]=" ";
                    Chess.chessBoard[r+temp*j][c]="R";
                
                    if (kingSafe()) 
                    {
                        list=list+r+c+(r+temp*j)+c+oldPiece;
                    }
                    
                    Chess.chessBoard[r][c]="R";
                    Chess.chessBoard[r+temp*j][c]=oldPiece;
                }
            } catch (Exception e) {}
            
            temp=1;
        }
        
        return list;
    }
    
    /* Function to generate possible moves for knight*/
    //A knight's movement will be a little different from other pieces in the following ways
    // A knight doesn't have a stright or diagonal movement but L-shaped movement.
    // Also, a knight can "jump" over other pieces and is not bothered by any piece on its way
    public static String posibleK(int i) 
    {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        
        // There are a total of 8 places a knight can travel to.
        for (int j=-1; j<=1; j+=2) 
        {
            for (int k=-1; k<=1; k+=2) 
            {
                // This is to generate the first 4 places the knight can travel to.
                try 
                {
                    //checking if the position where the knight is supposed to "land" either contains a blank space or a black piece
                    if (Character.isLowerCase(Chess.chessBoard[r+j][c+k*2].charAt(0)) || " ".equals(Chess.chessBoard[r+j][c+k*2])) 
                    {
                        oldPiece=Chess.chessBoard[r+j][c+k*2];
                        Chess.chessBoard[r][c]=" ";
                    
                        if (kingSafe()) 
                        {
                            list=list+r+c+(r+j)+(c+k*2)+oldPiece;
                        }
                        
                        Chess.chessBoard[r][c]="K";
                        Chess.chessBoard[r+j][c+k*2]=oldPiece;
                    }
                } catch (Exception e) {}
                
                //This is to generate the next 4 places the knight can travel to.
                try 
                {
                    if (Character.isLowerCase(Chess.chessBoard[r+j*2][c+k].charAt(0)) || " ".equals(Chess.chessBoard[r+j*2][c+k])) 
                    {
                        oldPiece=Chess.chessBoard[r+j*2][c+k];
                        Chess.chessBoard[r][c]=" ";
                     
                        if (kingSafe()) 
                        {
                            list=list+r+c+(r+j*2)+(c+k)+oldPiece;
                        }
                        
                        Chess.chessBoard[r][c]="K";
                        Chess.chessBoard[r+j*2][c+k]=oldPiece;
                    }
                } catch (Exception e) {}
            }
        }
        
        return list;
    }
    
    /* Function to generate possible moves for Bishop*/
    //A bishops move is almost similar to queen's move except that the bishop can only move diagonally and not vertically or horizontally
    public static String posibleB(int i) 
    {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
    
        //Notice how we are increasing j by 2 and k by 2. This is because we want to skip horizontal and vertical movemenets
        //The rest of the code will be same as queen's movement 
        for (int j=-1; j<=1; j+=2) 
        {
            for (int k=-1; k<=1; k+=2) 
            {
                try 
                {
                    while(" ".equals(Chess.chessBoard[r+temp*j][c+temp*k]))
                    {
                        oldPiece=Chess.chessBoard[r+temp*j][c+temp*k];
                        Chess.chessBoard[r][c]=" ";
                        Chess.chessBoard[r+temp*j][c+temp*k]="B";
                        
                        if (kingSafe()) 
                        {
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        
                        Chess.chessBoard[r][c]="B";
                        Chess.chessBoard[r+temp*j][c+temp*k]=oldPiece;
                        
                        temp++;
                    }
                    
                    
                    if (Character.isLowerCase(Chess.chessBoard[r+temp*j][c+temp*k].charAt(0))) 
                    {
                        oldPiece=Chess.chessBoard[r+temp*j][c+temp*k];
                        Chess.chessBoard[r][c]=" ";
                        Chess.chessBoard[r+temp*j][c+temp*k]="B";
                     
                        if (kingSafe()) 
                        {
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        
                        Chess.chessBoard[r][c]="B";
                        Chess.chessBoard[r+temp*j][c+temp*k]=oldPiece;
                    }
                } catch (Exception e) {}
                
                temp=1;
            }
        }
        
        return list;
    }
    
    /* Function to generate possible moves for queen*/
    public static String posibleQ(int i) 
    {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
    
        //A queen can move to all the directions around it. There are 8 squares around the queen. We can need to proceed in every direction and check
        //for queen movement. Example, j = -1 and k = -1 denotes south-west direction and so on.
        for (int j=-1; j<=1; j++) 
        {
            for (int k=-1; k<=1; k++) 
            {
                if (j!=0 || k!=0) //j = 0 and k = 0 dentones the position where the queen is currently placed
                {
                    try //to check for out of bounds from the board
                    {
                        //While we keep on encoutering an empty space, we let the queen keep moving in that direction
                        while(" ".equals(Chess.chessBoard[r+temp*j][c+temp*k]))
                        {
                            oldPiece=Chess.chessBoard[r+temp*j][c+temp*k]; 
                            Chess.chessBoard[r][c]=" ";
                            Chess.chessBoard[r+temp*j][c+temp*k]="Q";
                            
                            if (kingSafe()) 
                            {
                                list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                            }
                            
                            Chess.chessBoard[r][c]="Q";
                            Chess.chessBoard[r+temp*j][c+temp*k]=oldPiece;
                            
                            temp++; //increasing the value of temp so that the queen can move further in that particular direction
                        }
                        
                        //If we encounter a black piece then we capture that piece 
                        if (Character.isLowerCase(Chess.chessBoard[r+temp*j][c+temp*k].charAt(0))) 
                        {
                            oldPiece=Chess.chessBoard[r+temp*j][c+temp*k];
                            Chess.chessBoard[r][c]=" ";
                            Chess.chessBoard[r+temp*j][c+temp*k]="Q";
                        
                            if (kingSafe()) 
                            {
                                list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                            }
                            
                            Chess.chessBoard[r][c]="Q";
                            Chess.chessBoard[r+temp*j][c+temp*k]=oldPiece;
                        }
                    } catch (Exception e) {}
                    
                    temp=1;
                }
            }
        }
        
        return list;
    }
    
    /* Function to generate possible moves for king*/
    public static String posibleA(int i) 
    {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        
        //There are a total of 8 positions a king can travel to
        for (int j=0; j<9; j++) 
        {
            if (j!=4) // j = 4 is the position of the king
            {
                try 
                {
                    //We can only move the king in a particular direction if that position is either empty or occupied by a black piece
                    if (Character.isLowerCase(Chess.chessBoard[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(Chess.chessBoard[r-1+j/3][c-1+j%3])) 
                    {
                        //saving the old piece and moving the king to that position
                        oldPiece=Chess.chessBoard[r-1+j/3][c-1+j%3];
                        Chess.chessBoard[r][c]=" ";
                        Chess.chessBoard[r-1+j/3][c-1+j%3]="A";
                        
                        int kingTemp=Chess.kingPositionC;
                        Chess.kingPositionC=i+(j/3)*8+j%3-9;
                    
                        //if the king is safe then we can proceed with this move. We add this move to the list of possible moves.
                        if (kingSafe()) 
                        {
                            list=list+r+c+(r-1+j/3)+(c-1+j%3)+oldPiece;
                        }
                        
                        Chess.chessBoard[r][c]="A";
                        Chess.chessBoard[r-1+j/3][c-1+j%3]=oldPiece;
                        Chess.kingPositionC=kingTemp;
                    }
                } catch (Exception e) {}
            }
        }
        //need to add casting later
        return list;
    }
    
    /* Function for rating*/
    public static int rating() {
        return 0;
    }
    
    /* Function to check if the king is safe or not*/
    /* In this function, we know the position of the king. From that position, we are tracing the moves of various pieces and checking if we encouter 
       that piece. If we do then it means that the king is not safe. 
    */
    public static boolean kingSafe() 
    {
        //bishop or queen
        int temp=1;
    
        for (int i=-1; i<=1; i+=2) 
        {
            for (int j=-1; j<=1; j+=2) 
            {
                try
                {
                    //Keep on moving until we have blank space
                    while(" ".equals(Chess.chessBoard[Chess.kingPositionC/8+temp*i][Chess.kingPositionC%8+temp*j])) 
                    {
                        temp++;
                    }
                    
                    //If we encounter a black bishop or a queen along the diagonal path, then this means that the king is not safe and we return false
                    if ("b".equals(Chess.chessBoard[Chess.kingPositionC/8+temp*i][Chess.kingPositionC%8+temp*j]) ||
                            "q".equals(Chess.chessBoard[Chess.kingPositionC/8+temp*i][Chess.kingPositionC%8+temp*j])) 
                    {
                        return false;
                    }
                    
                } catch (Exception e) {}
                
                temp=1;
            }
        }
        
        
        //rook or queen
        for (int i=-1; i<=1; i+=2) 
        {
            try {
                //Keep on moving until we have blank space
                while(" ".equals(Chess.chessBoard[Chess.kingPositionC/8][Chess.kingPositionC%8+temp*i])) 
                {
                    temp++;
                }
                
                //If we encounter a black rook or a queen along the diagonal path, then this means that the king is not safe and we return false
                if ("r".equals(Chess.chessBoard[Chess.kingPositionC/8][Chess.kingPositionC%8+temp*i]) ||
                        "q".equals(Chess.chessBoard[Chess.kingPositionC/8][Chess.kingPositionC%8+temp*i])) 
                {
                    return false;
                }
                
            } catch (Exception e) {}
            
            temp=1;
            
            try 
            {
                while(" ".equals(Chess.chessBoard[Chess.kingPositionC/8+temp*i][Chess.kingPositionC%8])) 
                {
                    temp++;
                }
                
                if ("r".equals(Chess.chessBoard[Chess.kingPositionC/8+temp*i][Chess.kingPositionC%8]) ||
                        "q".equals(Chess.chessBoard[Chess.kingPositionC/8+temp*i][Chess.kingPositionC%8])) 
                {
                    return false;
                }
                
            } catch (Exception e) {}
            
            temp=1;
        }
        
        
        //knight
        for (int i=-1; i<=1; i+=2) 
        {
            for (int j=-1; j<=1; j+=2) 
            {
                try 
                {
                    if ("k".equals(Chess.chessBoard[Chess.kingPositionC/8+i][Chess.kingPositionC%8+j*2])) 
                    {
                        return false;
                    }
                } catch (Exception e) {}
                
                try 
                {
                    if ("k".equals(Chess.chessBoard[Chess.kingPositionC/8+i*2][Chess.kingPositionC%8+j])) 
                    {
                        return false;
                    }
                    
                } catch (Exception e) {}
            }
        }
        
        
        //pawn
        if (Chess.kingPositionC>=16)  //if the king is at the first two rows then there is no way the it can threatened by a black pawn
        {
            try 
            {
                if ("p".equals(Chess.chessBoard[Chess.kingPositionC/8-1][Chess.kingPositionC%8-1])) 
                {
                    return false;
                }
            } catch (Exception e) {}
            
            
            try 
            {
                if ("p".equals(Chess.chessBoard[Chess.kingPositionC/80-1][Chess.kingPositionC%8+1])) 
                {
                    return false;
                }
            } catch (Exception e) {}
            
            //king
            for (int i=-1; i<=1; i++) 
            {
                for (int j=-1; j<=1; j++) 
                {
                    if (i!=0 || j!=0) 
                    {
                        try 
                        {
                            if ("a".equals(Chess.chessBoard[Chess.kingPositionC/8+i][Chess.kingPositionC%8+j])) 
                            {
                                return false;
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        }
        
        return true;
    }

}
