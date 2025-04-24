import javax.swing.*;
import java.awt.*;

// Wrote by Yi Jie and Shu Qi
// Model
class Pieces {
    private Piece[][] board;
    private Image sauRed, sauBlue, ramRed, reverseRamRed, ramBlue, reverseRamBlue, torRed, torBlue, xorRed, xorBlue, bizRed, bizBlue;
    private Board boardInstance;
    CheckSauTargetPosition checkSauTargetPosition = new CheckSauTargetPosition(this);
    
    public Pieces(Board boardInstance) {
        this.boardInstance = boardInstance;
        board = new Piece[8][5]; // 8 rows and 5 columns
        
        // Load piece images
        sauRed = new ImageIcon("sau_red.png").getImage();
        sauBlue = new ImageIcon("sau_blue.png").getImage();
        ramRed = new ImageIcon("ram_red.png").getImage();
        reverseRamRed = new ImageIcon("reverse_ram_red.png").getImage();
        ramBlue = new ImageIcon("ram_blue.png").getImage();
        reverseRamBlue = new ImageIcon("reverse_ram_blue.png").getImage();
        torRed = new ImageIcon("tor_red.png").getImage();
        torBlue = new ImageIcon("tor_blue.png").getImage();
        xorRed = new ImageIcon("xor_red.png").getImage();
        xorBlue = new ImageIcon("xor_blue.png").getImage();
        bizRed = new ImageIcon("biz_red.png").getImage();
        bizBlue = new ImageIcon("biz_blue.png").getImage();

        // Initialize pieces
        initializePieces();
    }

    // Getter
    public Piece[][] getPieceArray() {
        return board;
    }
    
    //Put image to 2D array
    private void initializePieces() {
        // Red pieces
        board[0][0] = new Tor("red", 0, 0, this);
        board[0][1] = new Biz("red", 0, 1);
        board[0][2] = new Sau("red", 0, 2);
        board[0][3] = new Biz("red", 0, 3);
        board[0][4] = new Xor("red", 0, 4, this);

        for (int i = 0; i < 5; i++) {
            board[1][i] = new Ram("red", 1, i, this);
        }

        // Blue pieces
        board[7][0] = new Xor("blue", 7, 0, this);
        board[7][1] = new Biz("blue", 7, 1);
        board[7][2] = new Sau("blue", 7, 2);
        board[7][3] = new Biz("blue", 7, 3);
        board[7][4] = new Tor("blue", 7, 4, this);

        for (int i = 0; i < 5; i++) {
            board[6][i] = new Ram("blue", 6, i, this);
        }
    }

    // Draw piece function
    public void drawPieces(Graphics g, int xOffset, int yOffset) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 5; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    Image image = getImageForPiece(piece);
                    placePieces(g, image, col, row, xOffset, yOffset);
                }
            }
        }
    }

    // Getter to return the image based on specific condition
    private Image getImageForPiece(Piece piece) {
        switch (piece.type) {
            case "tor":
                return piece.color.equals("red") ? torRed : torBlue; //if equal red, return torRed; else, return torBlue
            case "biz":
                return piece.color.equals("red") ? bizRed : bizBlue; //if equal red, return bizRed; else, return bizBlue
            case "sau":
                return piece.color.equals("red") ? sauRed : sauBlue; //if equal red, return sauRed; else, return sauBlue
            case "ram":
                if (piece instanceof Ram) {
                    Ram ram = (Ram) piece;
                    if (ram.direction == 1) 
                        return piece.color.equals("red") ? reverseRamRed : reverseRamBlue;  // If Ram reach the top end of the board, return reverse Ram piece, else return the default Ram piece
                }
                return piece.color.equals("red") ? ramRed : ramBlue; //if equal red, return ramRed; else, return ramBlue
            case "xor":
                return piece.color.equals("red") ? xorRed : xorBlue; //if equal red, return xorRed; else, return xorBlue
            default:
                return null;
        }
    }

    // Place piece in board function
    private void placePieces(Graphics g, Image piece, int col, int row, int xOffset, int yOffset) {
        int x = col * 84 + xOffset + 16; // Adjust column to center the image
        int y = row * 84 + yOffset + 16; // Adjust row to center the image
        Graphics2D g2d = (Graphics2D) g;
        
        if(boardInstance.currentPlayer.equals("red")) {
            // Flip the image for the red player's view (rotate 180 degrees
            g2d.rotate(Math.toRadians(180), x + 26, y + 26);
        }
        
        g.drawImage(piece, x, y, 52, 52, null); // Draw the piece image
        
        if (boardInstance.currentPlayer.equals("red")) {
            g2d.rotate(Math.toRadians(-180), x + 26, y + 26); // Reset rotation
        }
    }
    
    // Getter piece
    public Piece getPiece(int col, int row){
        if(row >= 0 && row < 8 && col >= 0 && col < 5){
            return board[row][col];
        }
        return null;
    }
    
    // Make a movement for the piece
    public void makeMove(Move move){
        board[move.previousRow][move.previousCol] = null;
        board[move.newRow][move.newCol] = move.piece;
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.x = move.newCol * 84;
        move.piece.y = move.newRow * 84;
        
        // Check the direction of Ram Piece when Ram reach the top end of the board or bottom end of the board
        if (move.piece instanceof Ram) {
            Ram ram = (Ram) move.piece;
            if (ram.direction == -1 && ram.row == 0) {
                ram.direction = 1;
                boardInstance.repaintBoard(); // Refresh the board to update the Ram image
            } else if (ram.direction == 1 && ram.row == 7) {
                ram.direction = -1;
                boardInstance.repaintBoard(); // Refresh the board to update the Ram image
            }
        }
        
        //Play different sound based on capture sound or move sound
        if(move.capture != null){
            SoundManager.playSound("capture_sound.wav");
        }
        else{
            SoundManager.playSound("move_sound.wav");
        }
        
        // Check the captured piece is Sau
        if(move.capture != null && move.capture.type.equals("sau")) {
            String winner = move.capture.color.equals("blue") ? "Red Team" : "Blue Team";
            String opponent = move.capture.color.equals("blue") ? "Blue Team" : "Red Team";
            JOptionPane.showMessageDialog(boardInstance.pn, winner + " wins! " + opponent + "'s Sau has been captured.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            boardInstance.isGameOver = true;
        }
    }
    
    // Valid movement for all the piece
    public boolean isValidMove(Move move, int turn){
        //make sure player cannot move their opponent pieces
        if(!move.piece.color.equals(boardInstance.currentPlayer)){
            return false;
        }
        // Make sure player cannot capture their own pieces
        if(sameColor(move.piece, move.capture)){
            return false;
        }
        // Check the valid movement of all the piece
        if(!move.piece.isValidMovement(move.newCol, move.newRow, turn)){
            return false;
        }
        // Check if thre have blocked piece, then the piece cannot skip over the blocked piece
        if(move.piece.affrontPiece(move.newCol, move.newRow)){
            return false;
        }
        // Check the Sau valid movement 
        if (move.piece.type.equals("sau")) {
            // Temporarily make the move first to check if the sau is still under attack
            Piece originalPiece = board[move.newRow][move.newCol];
            board[move.newRow][move.newCol] = move.piece;
            board[move.previousRow][move.previousCol] = null;
    
            boolean isUnderAttack = checkSauTargetPosition.isSauChecked(move);
    
            // Undo the move
            board[move.previousRow][move.previousCol] = move.piece;
            board[move.newRow][move.newCol] = originalPiece;
    
            // Sau cannot move to the area that targeted by other opponent piece
            if (isUnderAttack) {
                return false;
            }
        }
        
        // Return valid movement
        return move.newRow >= 0 && move.newRow < 8 && move.newCol >= 0 && move.newCol < 5;
    }
    
    // Check the same color piece for the movement to avoid capture own team piece
    public boolean sameColor(Piece p1, Piece p2){
        if(p1 == null || p2 == null){
            return false;
        }
        return p1.color.equals(p2.color);
    }
    
    // Getter to return the instance of board
    public Board getBoardInstance() {
        return boardInstance;
    }
    
    // Update Xor and Tor after 2 turn
    public void updatePieceTypes(int turn) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 5; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    if (piece instanceof Tor || piece instanceof Xor) { // Checks if the piece object is an instance of the Tor class or Xor class.
                                                                        // Basicly like if type is tor or type is xor is true, then implement the if else statement
                        // Update the type based on the turn
                        if (turn % 4 == 0 || turn % 4 == 1) {
                            if (piece instanceof Tor) { // if true
                                piece.setType("tor"); // Tor movement
                            } else if (piece instanceof Xor) { // if true
                                piece.setType("xor"); // Xor movement
                            }
                        } else {
                            if (piece instanceof Tor) {
                                piece.setType("xor"); // Xor movement
                            } else if (piece instanceof Xor) {
                                piece.setType("tor"); // Tor movement
                            }
                        }
                    }
                }
            }
        }
    } 
    
    // Flipped board function for player view
    public void flipBoard() {
        Piece[][] flippedBoard = new Piece[8][5];
    
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {
                int flippedRow = 7-i; // Flipped the row
                int flippedCol = 4-j; // Flipped the column
                
                flippedBoard[flippedRow][flippedCol] = board[i][j];
                
                if (board[i][j] != null) {
                    board[i][j].row = flippedRow;
                    board[i][j].col = flippedCol;
                    board[i][j].x = flippedCol * 84;
                    board[i][j].y = flippedRow * 84;
                }
            }
        }
    
        board = flippedBoard;
    }
    
    // Get the Sau 
    public Piece findSau(String color){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 5; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.color.equals(color) && piece.type.equals("sau")) {
                    return piece;
                }
            }
        }
        return null; // Return null if no Sau piece is found
    }

    // Clear the pieces on the board
    public void clearBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 5; col++) {
                board[row][col] = null; // Clear the board
            }
        }
    }
}