import java.awt.event.*;

// Wrote by Yi Jie and Xin Yuen
// Controller
public class Input extends MouseAdapter{
    Board board;
    Pieces pieces;
    private int xOffset, yOffset;

    // Input constructor
    public Input(Board board, Pieces pieces){
        this.board = board;
        this.pieces = pieces;
    }
    
    // Set Offset
    public void setOffsets(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    
    // Function of pressed the piece
    @Override
    public void mousePressed(MouseEvent e){
        if (board.isGameOver) return; 
        
        int col = (e.getX() - xOffset) / 84;
        int row = (e.getY() - yOffset) / 84;
        
        Piece pieceXY = pieces.getPiece(col, row); // Get the current pressed piece
        if (pieceXY != null){
            board.selectedPiece = pieceXY;
        }
    }
    
    // Function of dragged the piece
    @Override
    public void mouseDragged(MouseEvent e){
        if (board.isGameOver) return;
        
        if(board.selectedPiece != null){
            board.selectedPiece.x = e.getX() - xOffset - 84 / 2;
            board.selectedPiece.y = e.getY() - yOffset - 84 / 2;
            
            board.repaintBoard(); // Repaint the board
        }
    }
    
    // Function of released the piece after dragged
    @Override
    public void mouseReleased(MouseEvent e){
        if (board.isGameOver) return;
        
        int col = (e.getX() - xOffset) / 84;
        int row = (e.getY() - yOffset) / 84;
        
        if(board.selectedPiece != null){
            if(board.selectedPiece.color.equals(board.currentPlayer)){
                Move move = new Move(pieces, board.selectedPiece, col, row); // Pass the position for piece movement
            
                // If the piece is valid to move
                if(pieces.isValidMove(move, board.turn)){
                    pieces.makeMove(move); // Move the piece
    
                    board.logMove(move); // Display the movement 
                    
                    // switch to next player
                    board.currentPlayer = board.currentPlayer.equals("blue") ? "red" : "blue";
                    
                    // When two player move their piece, one turn be added
                    if(board.currentPlayer.equals("blue")){
                        board.turn++;
                    }

                    pieces.updatePieceTypes(board.turn); //Update type of Tor and Xor
                    pieces.flipBoard(); // Flipped the board
                    board.updateTurnDisplay(); //Update turn display
                    board.repaintBoard(); // Repaint board
                }
                else{
                    // reset position
                    board.selectedPiece.x = board.selectedPiece.col * 84 + xOffset;
                    board.selectedPiece.y = board.selectedPiece.row * 84 + yOffset;
                }
            }
            else{
                // reset position
                board.selectedPiece.x = board.selectedPiece.col * 84 + xOffset;
                board.selectedPiece.y = board.selectedPiece.row * 84 + yOffset;
            }
        }
        
        board.selectedPiece = null; // Reset the selected piece to null
        board.repaintBoard(); // Repaint the board
    }
}