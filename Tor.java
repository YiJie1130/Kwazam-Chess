// Wrote by Yi Jie and Wei Khong
// Model
class Tor extends Piece {
    private Pieces pieces;
    
    // Constructor
    public Tor(String color, int row, int col, Pieces pieces) {
        super("tor", color, row, col); // Call to super class
        this.pieces = pieces;
    }
    
    // Valid movement for Tor
    public boolean isValidMovement(int col, int row, int turn) {
        // turn modulo 4 = [0, 1, 2, 3]
        int movementType = turn % 4; 
    
        if (movementType == 0 || movementType == 1) {
            // return Tor Movement
            return col == this.col || row == this.row; 
        } else {
            // return Xor Movement
            return Math.abs(col - this.col) == Math.abs(row - this.row);
        }
    }
    
    // Check the blocked piece area
    public boolean affrontPiece(int col, int row) {
        int movementType = (pieces.getBoardInstance().getTurn()) % 4;
        
        if (movementType == 0 || movementType == 1) {
            //left
            if(this.col > col)
                for(int i = this.col - 1; i > col; i--)
                    if(pieces.getPiece(i, this.row) != null)
                        return true;
                    
            //right
            if(this.col < col)
                for(int i = this.col + 1; i < col; i++)
                    if(pieces.getPiece(i, this.row) != null)
                        return true;
                 
            //up
            if(this.row > row)
                for(int i = this.row - 1; i > row; i--)
                    if(pieces.getPiece(this.col, i) != null)
                        return true;
                
            //down
            if(this.row < row)
                for(int i = this.row + 1; i < row; i++)
                    if(pieces.getPiece(this.col, i) != null)
                        return true;
        }
        else{
            //up left
            if(this.col > col && this.row > row)
                for(int i = 1; i < Math.abs(this.col - col); i++)
                    if(pieces.getPiece(this.col - i, this.row - i) != null)
                        return true;
                        
            //up right
            if(this.col < col && this.row > row)
                for(int i = 1; i < Math.abs(this.col - col); i++)
                    if(pieces.getPiece(this.col + i, this.row - i) != null)
                        return true;
                        
            //down left
            if(this.col > col && this.row < row)
                for(int i = 1; i < Math.abs(this.col - col); i++)
                    if(pieces.getPiece(this.col - i, this.row + i) != null)
                        return true;
                        
            //down right
            if(this.col < col && this.row < row)
                for(int i = 1; i < Math.abs(this.col - col); i++)
                    if(pieces.getPiece(this.col + i, this.row + i) != null)
                        return true;
        }
                    
        return false;
    }
}