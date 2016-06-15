
/*
 * Author: Ankit Arora
 * Email: arora.ankit7@gmail.com
 * Description: This class hold the function "rating()" which is responsible for caculating the rating for each move and select the best move out of it so that 
    			the computer can play a good game    
*/
public class Rating {
    
    
    static int pawnBoard[][]={//attribute to http://chessprogramming.wikispaces.com/Simplified+evaluation+function
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        { 5,  5, 10, 25, 25, 10,  5,  5},
        { 0,  0,  0, 20, 20,  0,  0,  0},
        { 5, -5,-10,  0,  0,-10, -5,  5},
        { 5, 10, 10,-20,-20, 10, 10,  5},
        { 0,  0,  0,  0,  0,  0,  0,  0}};
    static int rookBoard[][]={
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  0,  0,  5,  5,  0,  0,  0}};
    static int knightBoard[][]={
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}};
    static int bishopBoard[][]={
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}};
    static int queenBoard[][]={
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}};
    static int kingMidBoard[][]={
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20,  0,  0,  0,  0, 20, 20},
        { 20, 30, 10,  0,  0, 10, 30, 20}};
    static int kingEndBoard[][]={
        {-50,-40,-30,-20,-20,-30,-40,-50},
        {-30,-20,-10,  0,  0,-10,-20,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-30,  0,  0,  0,  0,-30,-30},
        {-50,-30,-30,-30,-30,-30,-30,-50}};
    
    
    
    /* Function to provide the rating of the moves*/
    // "list" holds the number of moves. If it is empty then there is checkmate or stalemate
    // As the engine searches deeper in the game tree, then the accuracy of the rating changes
    public static int rating(int list, int depth)
    {
        int totalRating = 0; //variable to store the total rating for the best move possible. This will be the sum of all the rating defined by the funtion
                             //below
        int material = rateMaterial(); // argument for passing to the "ratePositional()" method
            
        totalRating += rateAttack();
        totalRating += rateMaterial();
        totalRating += rateMoveability(list, depth, material);
        totalRating += ratePositional(material);
        
        Chess.flipBoard(); //flipping the board for black pieces
        
        //Note: We are subtracting as each of the functions will only looking at one color. Therefore, we subtract for black and add for white pieces. 
        //This works the same way we used flipboard in "AlphaBetaChess.java" class
        totalRating -= rateAttack();
        totalRating -= rateMaterial();
        totalRating -= rateMoveability(list, depth, material);
        totalRating -= ratePositional(material);
        
        Chess.flipBoard(); //flipping the board back
        
        //positive will be from white's perspective and negative will be from black's perspective
        return -(totalRating + depth * 50); //for example, we the depth is 3 then we get 3 * 50 = 150 extra points for having a depth of 3
    }
    
    /* Function to rate the attack */
    public static int rateAttack()
    {
        int attackRating = 0;
        int tempKingPosition =Chess.kingPositionC; //variable to hold the original position of the king
        
        //Traversing the whole chess board
        for(int i = 0; i < 64; i++)
        {
            switch(Chess.chessBoard[i / 8][i % 8])
            {
                //Now we will move the king at various positions and check if the king is safe there.
                case "P":
                    Chess.kingPositionC = i; //placing the king at i to check if it is safe there
                    
                    if(!Moves.kingSafe())
                    {
                        attackRating -= 64;
                    }
                    
                    break;
                
                case "R":
                    Chess.kingPositionC = i; //placing the king at i to check if it is safe there
                    
                    if(!Moves.kingSafe())
                    {
                        attackRating -= 500;
                    }
                    
                    break;
                
                case "K":
                    Chess.kingPositionC = i; //placing the king at i to check if it is safe there
                    
                    if(!Moves.kingSafe())
                    {
                        attackRating -= 300;
                    }
                    
                    break;
                    
                case "B":
                    Chess.kingPositionC = i; //placing the king at i to check if it is safe there
                    
                    if(!Moves.kingSafe())
                    {
                        attackRating -= 300;
                    }
                    
                    break;
                    
                case "Q":
                    Chess.kingPositionC = i; //placing the king at i to check if it is safe there
                    
                    if(!Moves.kingSafe())
                    {
                        attackRating -= 900;
                    }
                    
                    break;
            }
        }
        
        Chess.kingPositionC = tempKingPosition; // setting the position of the king to its original position
        
        if(!Moves.kingSafe())
        {
            attackRating -= 200;
        }
        
        return attackRating / 2; //We divide by 2 because attacking is not as good as taking a piece. But the computer will think that it is. 
    }
    
    /* In this we provide rating based on the number of pieces on the board. Every peice has some value. 
       The whole material rating is based on these values.
    */
    public static int rateMaterial()
    {
        int materialRating = 0; //varible to calculate material rating
        int bishopCounter = 0; // if we lose one bishop then half of the board becomes unaccessible. Therefore, if we do not have two bishops but instead 
                               // we have only one bishop then there will be a penalty
        
        //Traversing the whole chess board
        for(int i = 0; i < 64; i++)
        {
            //There are different ratings in place to calculate material rating. We have a pawn worth 100, a bishop worth 300 and so on.
            //So, we are assigning a score for each piece
            switch(Chess.chessBoard[i / 8][i % 8])
            {
                case "P":
                    materialRating += 100; // a pawn is worth 100
                    break;
                
                case "R":
                    materialRating += 500; // a rook is worth 500
                    break;
                
                case "K":
                    materialRating += 300; // a knight is worth 300
                    break;
                    
                case "B":
                    bishopCounter += 1; // checking the number of bishops
                    break;
                    
                case "Q":
                    materialRating += 900; // a queen is worth 900
                    break;
            }
        }
        
        if(bishopCounter >= 2)
        {
            materialRating += 300 * bishopCounter;
        }
        
        else
        {
            if(bishopCounter == 1)
            {
                materialRating += 250; // if we have only one bishop then the worth will be only 250
            }
        }
        
        return materialRating;
    }
    
    /* To check for moveabilty which include check mate and stalemate where no more moves is possible*/
    /* To check how much moveabilty is possible*/
    public static int rateMoveability(int listLength, int depth, int material)
    {
        int moveabilityRating = 0;
        
        moveabilityRating += listLength; // to check how much moveability is possible(5 points per valid move)
        
        if(listLength == 0) //current side is in checkmate or stalemate
        {
            /*We need to check if it is checkmate or stalemate*/
            //checking if it is checkmate
            if(!Moves.kingSafe()) //if checkmate
            {
                moveabilityRating += -200000 * depth; // a very bad thing
            }
            
            else //if stalemate
            {
                moveabilityRating += -150000 * depth; // a very bad thing. A checkmate is a better ending than a stalemate
            }
            
        }
        
        return moveabilityRating;
    }
    
    /* Some positions are better than other positions. Therefore, this function makes use of the rating array defined at the beginning to calculate
       rating. 
       Note: The link is given at the beginning from where these rating arrays have been picked up.
    */
    public static int ratePositional(int material)
    {
        int counter=0;
        for (int i=0;i<64;i++) {
            switch (Chess.chessBoard[i/8][i%8]) {
                case "P": counter+=pawnBoard[i/8][i%8];
                    break;
                case "R": counter+=rookBoard[i/8][i%8];
                    break;
                case "K": counter+=knightBoard[i/8][i%8];
                    break;
                case "B": counter+=bishopBoard[i/8][i%8];
                    break;
                case "Q": counter+=queenBoard[i/8][i%8];
                    break;
                case "A": if (material>=1750) {counter+=kingMidBoard[i/8][i%8]; counter+=Moves.posibleA(Chess.kingPositionC).length()*10;} else
                {counter+=kingEndBoard[i/8][i%8]; counter+=Moves.posibleA(Chess.kingPositionC).length()*30;}
                    break;
            }
        }
        return counter;
    }
}
