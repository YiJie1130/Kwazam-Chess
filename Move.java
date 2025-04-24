// Wrote by Yi Jie
// Model
public class Move {
    int previousCol, previousRow, newCol, newRow;
    Piece piece, capture;
    
    // Move Constructor
    public Move(Pieces pieces, Piece piece, int newCol, int newRow){
        this.previousCol = piece.col; // Previous piece column
        this.previousRow = piece.row; // Previous piece row
        this.newCol = newCol; // New column of the piece
        this.newRow = newRow; // New row of the piece
        this.piece = piece; // Get the instance of Piece class
        this.capture = pieces.getPiece(newCol, newRow); // Return the captured piece
    }
}