/* Author: Ankit Arora
 * Email: arora.ankit7@gmail.com
 * Description: To define the GUI for the chess game*/



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class UserInterface extends JPanel implements MouseListener, MouseMotionListener{
    static int mouseX, mouseY; // to record position when we click the mouse
    static int newMouseX, newMouseY; //to record the position when we release the mouse
    static int squareSize = 32; //the size of boxes on the chess board
    
    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        
        this.setBackground(Color.yellow);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        //Making a check pattern for the chess board. We will be creating boxes in pairs of two, ie, light and dark, therefore "i = i + 2" in the for-loop
        //This method of generating the board seems complicated. There are other methods also. Will change this later.
        for(int i = 0; i < 64; i = i + 2)
        {
            g.setColor(new Color(255, 200, 100));
            g.fillRect((i%8+(i/8)%2)*squareSize, (i/8)*squareSize, squareSize, squareSize);
            
            g.setColor(new Color(150, 50, 30));
            g.fillRect(((i+1)%8-((i+1)/8)%2)*squareSize, ((i+1)/8)*squareSize, squareSize, squareSize);
        }
        
        //placing the chess pieces on the board
        Image chessPiecesImage;
        chessPiecesImage=new ImageIcon("ChessPieces.png").getImage();
        for (int i=0;i<64;i++) {
            int j=-1,k=-1; //co-ordinates for the chess pieces in "chessPiecesImage" image.
            switch (Chess.chessBoard[i/8][i%8]) {
                case "P": j=5; k=0;
                    break;
                case "p": j=5; k=1;
                    break;
                case "R": j=2; k=0;
                    break;
                case "r": j=2; k=1;
                    break;
                case "K": j=4; k=0;
                    break;
                case "k": j=4; k=1;
                    break;
                case "B": j=3; k=0;
                    break;
                case "b": j=3; k=1;
                    break;
                case "Q": j=1; k=0;
                    break;
                case "q": j=1; k=1;
                    break;
                case "A": j=0; k=0;
                    break;
                case "a": j=0; k=1;
                    break;
            }
            if (j!=-1 && k!=-1) { // we are multiplying "j" and "k" by 64 because each image is of the size 64
                g.drawImage(chessPiecesImage, (i%8)*squareSize, (i/8)*squareSize, (i%8+1)*squareSize, (i/8+1)*squareSize, j*64, k*64, (j+1)*64, (k+1)*64, this);
            }
        }
        /*g.setColor(Color.BLUE);
        g.fillRect(x-20, y-20, 40, 40);
        g.setColor(new Color(190,81,215));
        g.fillRect(40, 20, 80, 50);
        
        g.drawString("Ankit Arora", x, y);
        
        Image chessPieceImage = null;
        chessPieceImage = new ImageIcon("ChessPieces.png").getImage();
        g.drawImage(chessPieceImage, x, y, this);*/
    }
    
    @Override
    public void mouseMoved(MouseEvent e) 
    {
        
    }
    
    @Override
    public void mousePressed(MouseEvent e) 
    {
        if((e.getX() < 8 * squareSize) && (e.getY() < 8 * squareSize)) // perform some action only when the mouse is clicked within the chessboard
        {
            //if inside the board
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();            
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) 
    {
        /* Note how we are using "mouseX/squareSize" and "mouseY/squareSize" down below in the code. This is because the size of the square is 
           "squareSize". So when we click at a place in the board and divide it by "squareSize" we get the location of the piece in terms of array 
            position. For example, if we click in anywhere in the second box then we will get a value between 1 and 2 which will be rounded down to 1. 
            Which is exactly the value that we want and pass it to the function makeMove.
        */
        if (e.getX()<8*squareSize &&e.getY()<8*squareSize) {
            //if inside the board
            newMouseX=e.getX();
            newMouseY=e.getY();
            if (e.getButton()==MouseEvent.BUTTON1) {
                String dragMove;
                if (newMouseY/squareSize==0 && mouseY/squareSize==1 && "P".equals(Chess.chessBoard[mouseY/squareSize][mouseX/squareSize])) {
                    //pawn promotion
                    dragMove=""+mouseX/squareSize+newMouseX/squareSize+Chess.chessBoard[newMouseY/squareSize][newMouseX/squareSize]+"QP";
                } else {
                    //regular move
                    dragMove=""+mouseY/squareSize+mouseX/squareSize+newMouseY/squareSize+newMouseX/squareSize+Chess.chessBoard[newMouseY/squareSize][newMouseX/squareSize];
                }
                String userPosibilities=Chess.possibleMoves();
                
                //Now that we have the possible moves, we need to check if "dragMove" is a part of it. We just replace the occurence of "dragMove" with 
                //null string. If the result has the length smaller than the original that means that "dragMove" was present in it, which means that 
                //the move that we are trying to make using the mouse is inface valid and we can go ahead with that. So we call "makeMove" function 
                //and call the repaint() method to draw the chessboard again
                //Once the human has made the move, we will flip the board and let the computer make a move and then flip the board back.
                if (userPosibilities.replaceAll(dragMove, "").length()<userPosibilities.length()) {
                    //if valid move
                    Chess.makeMove(dragMove); //move made by human
                    Chess.flipBoard(); //board flip
                    Chess.makeMove(Chess.alphaBeta(Chess.globalDepth, 1000000, -1000000, "", 0)); //move made by computer
                    Chess.flipBoard(); //flip board back
                    repaint(); //show on screen
                }
            }
            repaint();
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) 
    {
        
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
}
