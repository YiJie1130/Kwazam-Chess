// Wrote by Wei Khong
// Model
class Sau extends Piece {
    // Constructor
    public Sau(String color, int row, int col) {
        // Call to super class which is in Pieces class
        super("sau", color, row, col);
    }
    
    // Valid movement for Sau
    public boolean isValidMovement(int col, int row, int turn) {
        return !(row > this.row + 1 || col > this.col + 1 || row < this.row - 1 || col < this.col - 1);
    }
}