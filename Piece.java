// Wrote by Yi Jie 
// Model and Template Method
class Piece {
    String type;
    String color;
    int row, col;
    int x, y;

    // Piece constructor
    public Piece(String type, String color, int row, int col) {
        this.type = type; // Piece type
        this.color = color; // Piece color
        this.row = row; // Piece row
        this.col = col; // Piece column
        this.x = col * 84; // Adjust the column of piece
        this.y = row * 84; // Adjust the row of piece
    }
    
    public void setType(String type) {this.type = type;} // Set the piece type
    public boolean isValidMovement(int col, int row, int turn) {return true;} // Return the valid movement of the piece
    public boolean affrontPiece(int col, int row) {return false;} // Return the block piece
}


