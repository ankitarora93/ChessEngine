/* Author: Ankit Arora
 * Email: arora.ankit7@gmail.com
 * Description: Main java class containing the chess game code for:
 * 				- Creating the GUI by calling UserInterface class
 * 				- Contains minimax algorithm with alpha-beta pruning
 * 				- Contains the function to generate the list of possible moves 
 * 				- Contains the representation of the chessBoard in the form of a two dimensional array
 * */



import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Chess {
    
    //representation of the chess board using arrays
    //Note 1: Black pieces are represented using small letters and white pieces are represented using 
    //capital letters
    static String chessBoard[][]={
        {"r","k","b","q","a","b","k","r"},
        {"p","p","p","p","p","p","p","p"},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {"P","P","P","P","P","P","P","P"},
        {"R","K","B","Q","A","B","K","R"}};
    
    static int kingPositionC, kingPositionL; /* kingPostionC (where C stands for Capital) will monitor
    poisition of the white king and kingPositionL will monitor the position of black king  */
    
    static int humanAsWhite = -1; //variable to specify who plays as white. 1 = human as white, 0 = human as black
    
    static int globalDepth = 4;
    
    public static void main(String[] args) {
        
        //setting the value of the variable kingPositionC and kingPositionL. We scan through the whole board and keep increasing the value of these 
        //variables until we find them
        
        //setting the value for kingPositionC
        while(!"A".equals(chessBoard[kingPositionC / 8][kingPositionC % 8]))
        {
            kingPositionC++;
        }
        
        //setting the value for kingPostionL
        while(!"a".equals(chessBoard[kingPositionL / 8][kingPositionL % 8]))
        {
            kingPositionL++;
        }
        
        JFrame f = new JFrame("Chess Engine"); //a frame to add various chess features to it
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        UserInterface ui = new UserInterface();
        
        f.add(ui); // adding the JPanel to the JFrame
        
        f.setSize(500, 500);
        f.setVisible(true); 
        
        
        //Testing the function possible moves
      //  System.out.println(possibleMoves());
        
        Object[] option={"Computer","Human"};
        humanAsWhite=JOptionPane.showOptionDialog(null, "Who should play as white?", "ABC Options", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
        
        if (humanAsWhite == 0) 
        {
            makeMove(alphaBeta(globalDepth, 1000000, -1000000, "", 0));
            flipBoard();
            f.repaint();
        }
        
       // makeMove(alphaBeta(globalDepth, 1000000, -1000000, "", 0));
        makeMove("7655 ");
        undoMove("7655 ");
        
        //printing the chessboard
        for(int i = 0; i < 8; i++)
        {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
        
        
    }
    
    
    
    
    /* Function for alpha-beta algorithm
       This will return the move + score. The first five characters will be the move the rest will be the score
    */
    public static String alphaBeta(int depth, int beta, int alpha, String move, int player)
    {
        String list = possibleMoves(); //possibleMoves returns a list of possible moves
        
        //if we reach a depth of zero or the "list" variable is empty(ie, we don't have any possible moves) then we don't have to search anymore and we return
        if(depth == 0 || list.length() == 0)
        {
            return move+(Rating.rating(list.length(), depth) * (player * 2 - 1)); //Note: We are supposed to negate the score after each turn. For example, from your perspective, the
                                                       //score may be -10 but from the other person's perspective the score will be 10. Therefore, we need
                                                       //to negate the score after every turn. 
                                                       //The value of the player will be "1" or "0". Therefore, we are doing "player * 2 - 1" so that we 
                                                       //always get a value of "-1" to negate the score
        }
        
        //sort later. We will be sorting later on so that alpha-beta can prune faster
        
        player = 1 - player; //either 1 or 0
        
        //for each of the moves saved in the variable "list" 
        for(int i = 0; i < list.length(); i = i + 5)
        {
            makeMove(list.substring(i, i + 5));
            
            flipBoard(); //flipping the board. Now we don't have to write functions for move generation for white as well as black. We will just flip the
                         //board and use the same function to generate the move.
                         
            String returnString = alphaBeta(depth - 1, beta, alpha, list.substring(i, i + 5), player);
            
            int value = Integer.valueOf(returnString.substring(5)); //We need to pick the move and the score apart. "value" will store the score value
            
            flipBoard(); //again flipping the board
            
            undoMove(list.substring(i, i + 5)); //undoing the move after making it. We are travelling the whole game tree and collecting data as we move
                                                //along. Once we have the data, we undo that move. We return the move as well as the score in this function
                                
            if(player == 0)
            {
                if(value <= beta)
                {
                    beta = value;
                    
                    if(depth == globalDepth)
                    {
                        move = returnString.substring(0, 5);
                    }
                }
            }
            
            else
            {
                if(value > alpha)
                {
                    alpha = value;
                    
                    if(depth == globalDepth)
                    {
                        move = returnString.substring(0, 5);
                    }
                }
            }
            
            if(alpha >= beta) //if alpha is greater than beta then we do not let the for-loop complete. We do this by returning "move + beta" or 
            {                 //"move + alpha" in between the for loop
                if(player == 0)
                {
                    return move + beta;
                }
                
                else
                {
                    return move + alpha;
                }
            }
        }
        
        if(player == 0)
        {
            return move + beta;
        }
        
        else
        {
            return move + alpha;
        }
    }
    
    
    
    
    
    /* To flip the board upside down so that we don't have to write mehtods for black pieces and we can use functions written for white pieces to 
       generate the move*/
    public static void flipBoard()
    {
          String tempPiece; //this variable will hold the temporary peice that needs to be swapped
          
          for(int i = 0; i < 32; i++)
          {
              int r = i / 8; //row 
              int c = i % 8; //column
              
              if(Character.isUpperCase(chessBoard[r][c].charAt(0)))
              {
                  tempPiece = chessBoard[r][c].toLowerCase();
              }
              
              else
              {
                  tempPiece = chessBoard[r][c].toUpperCase();
              }
              
              if (Character.isUpperCase(chessBoard[7-r][7-c].charAt(0))) 
              {
                chessBoard[r][c]=chessBoard[7-r][7-c].toLowerCase();
              } 
              
              else 
              {
                chessBoard[r][c]=chessBoard[7-r][7-c].toUpperCase();
              }
            
              chessBoard[7-r][7-c]=tempPiece;
        }
        
        int kingTemp=kingPositionC;
        kingPositionC=63-kingPositionL;
        kingPositionL=63-kingTemp;
          
    }
    
    
    
    
    
    /* This function is to make the moves that will be generated by the function "possibleMoves"*/
    public static void makeMove(String move)
    {
        /* We have one special kind of move, pawn promotion. It will have a "P" at the end. Np other move will have a "P" at the end and only a pawn 
           promotion will have a "P" at the end
        */
        if(move.charAt(4) != 'P') //for regular moves (and not pawn promotion)
        {
            //A move is of the form x1,y1,x2,y2,capturedPiece
            //ie, x2,y2(destination) should turn into x1,y1 and x1,y1 should now be empty
            
            //LHS is desitination. RHS is where the chess piece initially is and we are moving it.
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] 
                    = chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            
            //LHS is the source from the where the piece is moved. Thereofore we are making that position " " as the piece has moved from there.
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";
            
            //Updating the position of the king which will be used to check if the king is safe or not
            if("A".equals(chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]))
            {
                kingPositionC = 8 * Character.getNumericValue(move.charAt(2)) + Character.getNumericValue(move.charAt(3));
            }
        }
        
        else //if pawn promotion 
        {
            chessBoard[1][Character.getNumericValue(move.charAt(0))] = " ";
            chessBoard[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(3)); //we have the new piece at location 3
        }
        
    }
    
    
    
    
    /* This function is to undo the move that has been already made*/
    public static void undoMove(String move)
    {
        if(move.charAt(4) != 'P') //for regular moves (and not pawn promotion)
        {
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] 
                    = chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = String.valueOf(move.charAt(4));
            
            //added later. Don't remember what this does. 
            if("A".equals(chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]))
            {
                kingPositionC = 8 * Character.getNumericValue(move.charAt(0)) + Character.getNumericValue(move.charAt(1));
            }
        }
        
        else 
        {
            chessBoard[1][Character.getNumericValue(move.charAt(0))] = "P";
            chessBoard[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(2)); 
        }
    
    }
    
    
    
    
    
    //A method to return the set of possible moves
    /* The format of the returned string will be as follows:
        
        (row1, col1, row2, col2, capturedPiece)
    
        row1, col1 will give us the original position of the piece
        row2, col2 will give us the position of the square where the piece is moved
        capturedPiece will tell us if there is any piece captured by this peice while moving to the 
        new location
    */
    public static String possibleMoves() 
    {
        String list="";
        
        for (int i=0; i<64; i++) 
        {
            switch (chessBoard[i/8][i%8]) 
            {
                case "P": list+=Moves.posibleP(i);
                    break;
                    
                case "R": list+=Moves.posibleR(i);
                    break;
                
                case "K": list+=Moves.posibleK(i);
                    break;
                
                case "B": list+=Moves.posibleB(i);
                    break;
                
                case "Q": list+=Moves.posibleQ(i);
                    break;
                
                case "A": list+=Moves.posibleA(i);
                    break;
            }
        }
        return list;//x1,y1,x2,y2,captured piece
    }
    
    
}
