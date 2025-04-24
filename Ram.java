// Wrote by Yi Jie and Xin Yuen
// Model and State Pattern
class Ram extends Piece {
    private Pieces pieces;
    public int direction;

    // Constructor
    public Ram(String color, int row, int col, Pieces pieces) {
        super("ram", color, row, col); // Call to super class
        this.pieces = pieces;
        this.direction = -1; //Make sure all Ram either blue or red only move up before they reach the end of the board
    }

    // Valid movement for Ram
    public boolean isValidMovement(int col, int row, int turn) {
        Board boardInstance = pieces.getBoardInstance();

        // Change direction of pieces when one team of piece reach the end of the board
        if (this.direction == -1 && this.row == 0) {
            this.direction = 1; //Reaches the top, reverse direction
        } else if (this.direction == 1 && this.row == 7) {
            this.direction = -1; //Reaches the bottom, reverse direction
        }

        // Make sure Ram move one step at a time
        if (this.col == col && row == this.row + direction) {
            Piece targetPiece = pieces.getPiece(col, row);

            // Check target position Ram want to move is empty or opponent's piece which is not the same team pieces
            if (targetPiece == null || !targetPiece.color.equals(this.color)) {
                return true;
            }
        }

        return false; //Invalid move
    }
}