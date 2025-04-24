// Wrote by Yi Jie
// Model
public class CheckSauTargetPosition {
    Pieces pieces;
    
    // Constructor
    public CheckSauTargetPosition(Pieces pieces) {
        this.pieces = pieces;
    }

    public boolean isSauChecked(Move move) {
        Piece sau = pieces.findSau(move.piece.color);
        if (sau == null) {
            return false; // If Sau doesn't exist, it's not in check.
        }

        int sauCol = sau.col; 
        int sauRow = sau.row;

        if (move.piece.type.equals("sau")) {
            sauCol = move.newCol;
            sauRow = move.newRow;
        }

        return hitByTor(sauCol, sauRow, 0, 1) || //up
               hitByTor(sauCol, sauRow, 1, 0) || //right
               hitByTor(sauCol, sauRow, 0, -1) || //down
               hitByTor(sauCol, sauRow, -1, 0) || //left
               
               hitByXor(sauCol, sauRow, -1, -1) || //up left
               hitByXor(sauCol, sauRow, 1, -1) || //up right
               hitByXor(sauCol, sauRow, 1, 1) || //down right
               hitByXor(sauCol, sauRow, -1, 1) || //down left
               
               hitByBiz(sauCol, sauRow) ||
               hitBySau(sauCol, sauRow);
    }

    private boolean hitByTor(int sauCol, int sauRow, int colVal, int rowVal) {
        for (int i = 1; i < 8; i++) {
            Piece piece = pieces.getPiece(sauCol + (i * colVal), sauRow + (i * rowVal));
            if (piece != null) {
                // Check the enemy is Tor or not
                if (!piece.color.equals(pieces.getBoardInstance().currentPlayer) && piece.type.equals("tor")) {
                    return true;
                }
                break;
            }
        }
        return false; 
    }

    private boolean hitByXor(int sauCol, int sauRow, int colVal, int rowVal) {
        for (int i = 1; i < 8; i++) {
            Piece piece = pieces.getPiece(sauCol + (i * colVal), sauRow + (i * rowVal));
            if (piece != null) {
                if (!piece.color.equals(pieces.getBoardInstance().currentPlayer) && piece.type.equals("xor")) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean hitByBiz(int sauCol, int sauRow) {
        // 8 possible for biz to move
        return checkBiz(pieces.getPiece(sauCol - 1, sauRow - 2)) ||
               checkBiz(pieces.getPiece(sauCol + 1, sauRow - 2)) ||
               checkBiz(pieces.getPiece(sauCol + 2, sauRow - 1)) ||
               checkBiz(pieces.getPiece(sauCol + 2, sauRow + 1)) ||
               checkBiz(pieces.getPiece(sauCol + 1, sauRow + 2)) ||
               checkBiz(pieces.getPiece(sauCol - 1, sauRow + 2)) ||
               checkBiz(pieces.getPiece(sauCol - 2, sauRow + 1)) ||
               checkBiz(pieces.getPiece(sauCol - 2, sauRow - 1));
    }

    private boolean checkBiz(Piece k) {
        return k != null && !k.color.equals(pieces.getBoardInstance().currentPlayer) && k.type.equals("biz");
    }

    private boolean hitBySau(int sauCol, int sauRow) {
        return checkSau(pieces.getPiece(sauCol, sauRow - 1)) || //up
               checkSau(pieces.getPiece(sauCol, sauRow + 1)) || //down
               checkSau(pieces.getPiece(sauCol - 1, sauRow)) || //left
               checkSau(pieces.getPiece(sauCol + 1, sauRow)) || //right
               checkSau(pieces.getPiece(sauCol - 1, sauRow - 1)) || //up left
               checkSau(pieces.getPiece(sauCol + 1, sauRow - 1)) || //up right
               checkSau(pieces.getPiece(sauCol + 1, sauRow + 1)) || //down right
               checkSau(pieces.getPiece(sauCol - 1, sauRow + 1)); //down left
    }

    private boolean checkSau(Piece s) {
        return s != null && !s.color.equals(pieces.getBoardInstance().currentPlayer) && s.type.equals("sau");
    }
}