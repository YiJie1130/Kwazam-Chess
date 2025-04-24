// Wrote by Shu Qi
// Model
class Biz extends Piece {
    // Constructor
    public Biz(String color, int row, int col) {
        super("biz", color, row, col); // Call to super class
    }
    
    // Valid movement for Biz
    public boolean isValidMovement(int col, int row, int turn) {
        return Math.abs(col - this.col) * Math.abs(row - this.row) == 2;
    }
}