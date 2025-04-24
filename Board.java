import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

// Wrote by Yi Jie
// View
public class Board{
    JPanel pn; // Container of the board
    Pieces pieces; // Purpose for using parameter in Pieces class
    Piece selectedPiece; // Purpose for using parameter in Piece class
    String currentPlayer = "blue"; // Make the first move for blue pieces
    JLabel turnDisplay; // Purpose to display the turn
    JTextArea moveLog; // Purpose for creating the text area
    String moveRecord; // Purpose for record the piece's position and movement 
    boolean isGameOver = false; 
    int cols = 5; // Column of the board 
    int rows = 8; // Row of the board 
    int turn = 0; // Default turn as 0
    
    public Board(){
        JFrame frame = new JFrame("Kwazam Chess"); // Set the frame title
        frame.setBounds(10, 10, 950, 800); // Set the bound of the screen when run the program
        frame.setLayout(new BorderLayout()); // Set the frame as borderLayout for using North, East, Center, West and South
        
        JPanel functionPanel = new JPanel(new BorderLayout());
        
        String text = "Kwazam Chess"; // Set the title of the board
        JLabel label = new JLabel(text, JLabel.CENTER);// Center-aligned label
        Font labelFont = new Font("Serif", Font.BOLD, 26); // Set the font type and font size for the board title
        label.setFont(labelFont);
        functionPanel.add(label, BorderLayout.NORTH); // Set the board title to north
        
        // Panel of the board
        pn = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                boolean white=true; // Set the white to true for purpose set color in the board 
                Graphics2D board = (Graphics2D) g; // Casting 2D array
                
                int boardWidth = cols * 84; // Calculate the width of the board 
                int boardHeight = rows * 84; // Calculate the height of the board
                int xOffset = (getWidth() - boardWidth) / 2; // Calculate the space left outside the board horizontally
                int yOffset = (getHeight() - boardHeight) / 2; // Calculate the space left outside the board vertically
                
                // Pass offsets to the Input class
                for (MouseListener listener : pn.getMouseListeners()) { // Return array of MouseListener object to pn
                    if (listener instanceof Input) { // If listener is Input object
                        ((Input) listener).setOffsets(xOffset, yOffset); // call setOffsets function
                    }
                }
                
                for(int x=0; x<cols; x++){
                    for(int y=0; y<rows; y++){
                        if(white){ //If true
                            board.setColor(Color.white.darker()); // Set color of board to darker white
                        }
                        else{
                            board.setColor(Color.white); // Set color of board to white
                        }
                        board.fillRect(x * 84 + xOffset, y * 84 + yOffset, 84, 84);//adjust the rectangle size
                        white=!white; // White(true) = Not white(false) ; Not white(false) = White(true)
                    }
                    white=!white;
                }
                
                if (selectedPiece != null) { // If the selected piece is not empty
                    for (int x = 0; x < cols; x++) {
                        for (int y = 0; y < rows; y++) {
                            Move move = new Move(pieces, selectedPiece, x, y);
                            if (pieces.isValidMove(move, turn)) { // Check whether the piece is valid to move or not
                                board.setColor(new Color(0, 255, 0, 100)); //(red, green, blue, transparency)
                                board.fillRect(x * 84 + xOffset, y * 84 + yOffset, 84, 84); // Draw the rectangle to board
                            }
                        }
                    }
                }
                
                pieces.drawPieces(board, xOffset, yOffset); //call to the drawPieces function in Pieces class with pass the board parameter
            }
        };
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(pn);
        frame.add(centerPanel, BorderLayout.CENTER); // Set the board to the center of the screen 
        
        //Create a text area to display the piece's position and movement at the right of the board
        moveLog = new JTextArea();
        moveLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveLog);
        scrollPane.setPreferredSize(new Dimension(196, 800));
        JPanel logPanel = new JPanel(new BorderLayout()); 
        logPanel.setPreferredSize(new Dimension(220, 800)); 
        logPanel.add(scrollPane, BorderLayout.EAST);
        frame.add(logPanel, BorderLayout.WEST);
        
        //Create a invisible area at the left to made the board center 
        JTextArea invisibleArea = new JTextArea();
        invisibleArea.setVisible(false); // Make it invisible
        JScrollPane invisibleScroll = new JScrollPane(invisibleArea);
        invisibleScroll.setPreferredSize(new Dimension(200, 800));
        JPanel invisiblePanel = new JPanel(new BorderLayout()); 
        invisiblePanel.setPreferredSize(new Dimension(220, 800)); 
        invisiblePanel.add(scrollPane, BorderLayout.WEST);
        frame.add(invisiblePanel, BorderLayout.EAST);
        
        // Create Save Game button for saving game
        JButton saveGameButton = new JButton("Save Game");
        saveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame("game_record.txt");
            }
        });
        
        // Create Load Game button for loading game
        JButton loadGameButton = new JButton("Load Game");
        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame("game_record.txt");
            }
        });
        
        JPanel functionButton = new JPanel();
        functionButton.add(saveGameButton);
        functionButton.add(loadGameButton);
        functionPanel.add(functionButton, BorderLayout.CENTER);
        frame.add(functionPanel, BorderLayout.NORTH);
        
        //Pieces Object
        pieces = new Pieces(this);
        
        // Call Listener
        pn.addMouseListener(new Input(this, pieces));
        pn.addMouseMotionListener(new Input(this, pieces));
        
        // Capitalize the first letter of currentPlayer
        String firstCapitalPlayer = capitalizeFirstLetter(currentPlayer);
        
        // Display the current team turn and turn
        turnDisplay = new JLabel("Current Turn: " + firstCapitalPlayer  + "  | Turn:  " + turn, JLabel.CENTER);
        Font displayFont = new Font("Sans serif", Font.BOLD, 15); 
        turnDisplay.setFont(displayFont);
        frame.add(turnDisplay, BorderLayout.SOUTH);
        
        frame.add(pn);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
    }
    
    // Make the first letter to capital letter function
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Return the original string if it's null or empty
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    // Repaint Board
    public void repaintBoard(){
        pn.repaint();
    }
    
    // Turn Display Update
    public void updateTurnDisplay(){
        String firstCapitalPlayer = capitalizeFirstLetter(currentPlayer);
        turnDisplay.setText("Current Turn: " + firstCapitalPlayer + " | Turn: " + turn);

        // Check if the game should end due to Sau's immobility
        if (!isGameOver) {
            checkSauMobility();
        }
    }
    
    // Count how many piece for specific team
    public int countRemainingPieces(String color) {
        int count = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Piece piece = pieces.getPiece(col, row);
                if (piece != null && piece.color.equals(color)) {
                    count++; // Increment the counter if the piece matches the color
                }
            }
        }
        return count; // Return total of pieces
    }
    
    // Method to check if the Sau can move
    private boolean canSauMove(Piece sau) {
        if (sau == null) 
            return false;

        // Check all possible moves for the Sau
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Move move = new Move(pieces, sau, col, row);
                if (pieces.isValidMove(move, turn)) {
                    return true; // Sau can move to this position
                }
            }
        }
        return false; // Sau cannot move to any position
    }
    
    // Method to check if the game should end due to Sau's immobility
    private void checkSauMobility() {
        if (countRemainingPieces(currentPlayer) == 1) {
            Piece redSau = pieces.findSau("red");
            Piece blueSau = pieces.findSau("blue");
    
            // Check if the current player's Sau cannot move
            Piece currentSau = (currentPlayer.equals("red")) ? redSau : blueSau;
            if (!canSauMove(currentSau)) {
                String winner = (currentPlayer.equals("red")) ? "Blue Team" : "Red Team";
                String opponent = (currentPlayer.equals("red")) ? "Red Team" : "Blue Team";
                
                JOptionPane.showMessageDialog(pn, winner + " wins! " + opponent + "'s Sau cannot move.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                moveLog.append("\n                   Game Over!");
                isGameOver = true;
            }
        }
    }

    // Getter of turn
    public int getTurn(){
        return turn;
    }
    
    // Save Game function
    public void saveGame(String filename) {
        try(FileWriter writer = new FileWriter(filename)){
            for(int row = 0; row < rows; row++){
                for(int col = 0; col < cols; col++){
                    Piece piece = pieces.getPiece(col, row);
                    if(piece != null){
                        writer.write(piece.type + "," + piece.color + "," + row + "," + col);
                        if (piece instanceof Ram) {
                            Ram ram = (Ram) piece;
                            writer.write("," + ram.direction); // Save the direction of the Ram
                        }
                        writer.write("\n");
                    }
                }
            }
            // Write the current player and turn to txt file
            writer.write("Current Player: " + currentPlayer + " team\n");
            writer.write("Turn: " + turn + "\n");
            
            // If the game end and game over, write Game Over to txt file
            if (isGameOver) {
                writer.write("Game Over!\n");
            }
        }
        // Catch the error and display it
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    // Load Game function
    public void loadGame(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            pieces.clearBoard(); // Clear all the piece in the board
            selectedPiece = null; // Clear the selected piece
            moveLog.setText(""); // Clear the move log before loading new moves
            isGameOver = false; // Reset the game over flag
            
            String line;
            boolean isFirstDisplay = true;
    
            // Read the file and update the turn and current player first
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Current Player: ")) {
                    currentPlayer = line.substring("Current Player: ".length()).replace(" team", "");
                } else if (line.startsWith("Turn: ")) {
                    turn = Integer.parseInt(line.substring("Turn: ".length()));
                } else if (line.contains("Game Over!")) {
                    isGameOver = true;
                }
            }
    
            // Reset the reader to read the file again for piece information
            reader.close();
            BufferedReader pieceReader = new BufferedReader(new FileReader(filename));
    
            while ((line = pieceReader.readLine()) != null) {
                if (!line.startsWith("Current Player: ") && !line.startsWith("Turn: ") && !line.contains("Game Over!")) {
                    String[] lines = line.split(",");
                    if (lines.length >= 4) {
                        // read from txt file and assign it to array
                        String type = lines[0]; 
                        String color = lines[1];
                        int row = Integer.parseInt(lines[2]);
                        int col = Integer.parseInt(lines[3]);
    
                        Piece piece = null;
                        switch (type) {
                            case "tor":
                                piece = new Tor(color, row, col, pieces);
                                break;
                            case "biz":
                                piece = new Biz(color, row, col);
                                break;
                            case "sau":
                                piece = new Sau(color, row, col);
                                break;
                            case "ram":
                                piece = new Ram(color, row, col, pieces);
                                if (lines.length == 5) {
                                    ((Ram) piece).direction = Integer.parseInt(lines[4]); // Restore the direction of the Ram
                                }
                                break;
                            case "xor":
                                piece = new Xor(color, row, col, pieces);
                                break;
                        }
                        if (piece != null) {
                            pieces.getPieceArray()[row][col] = piece;
    
                            // DIsplay the first Turn and current player first
                            if (isFirstDisplay) {
                                moveRecord = " Turn " + turn +
                                    "\n Current Player: " + currentPlayer.substring(0, 1).toUpperCase() + currentPlayer.substring(1).toLowerCase() + " Team\n\n";
                                moveLog.append(moveRecord);
                                isFirstDisplay = false; // Set flag to false after first display
                            }
    
                            // Append the piece information
                            moveRecord = " " + color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase() + " " +
                                type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase() + " at (" + col + ", " + row + ")\n";
                            moveLog.append(moveRecord);
                        }
                    }
                }
            }
    
            // If the last game save is game over, then display it
            if (isGameOver) {
                moveLog.append("\n                   Game Over!");
            }
    
            // Reinitialize mouse listeners
            for (MouseListener listener : pn.getMouseListeners()) {
                pn.removeMouseListener(listener);
            }
            for (MouseMotionListener listener : pn.getMouseMotionListeners()) {
                pn.removeMouseMotionListener(listener);
            }
            Input input = new Input(this, pieces);
            pn.addMouseListener(input);
            pn.addMouseMotionListener(input);
    
            // Repaint the board and update the turn display
            repaintBoard();
            updateTurnDisplay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Display the log movement of the pieces
    public void logMove(Move move) {
        if (move.capture != null) {
            if (move.capture.type.equals("sau")) {
                moveRecord = "\n Turn " + turn + " (" + move.piece.color.substring(0, 1).toUpperCase() + move.piece.color.substring(1).toLowerCase() + ")" + ": \n " + 
                    move.piece.type.substring(0, 1).toUpperCase() + move.piece.type.substring(1).toLowerCase() + "(" +
                    move.previousCol + ", " + move.previousRow + ") captures " + 
                    move.capture.color.substring(0, 1).toUpperCase() + move.capture.color.substring(1).toLowerCase() + " " + 
                    move.capture.type.substring(0, 1).toUpperCase() + move.capture.type.substring(1).toLowerCase() + "(" + move.newCol + ", " + move.newRow + ")\n\n" +
                    "                   Game Over!";
            }
            else {
                moveRecord = "\n Turn " + turn + " (" + move.piece.color.substring(0, 1).toUpperCase() + move.piece.color.substring(1).toLowerCase() + ")" + ": \n " + 
                    move.piece.type.substring(0, 1).toUpperCase() + move.piece.type.substring(1).toLowerCase() + "(" +
                    move.previousCol + ", " + move.previousRow + ") captures " + 
                    move.capture.color.substring(0, 1).toUpperCase() + move.capture.color.substring(1).toLowerCase() + " " + 
                    move.capture.type.substring(0, 1).toUpperCase() + move.capture.type.substring(1).toLowerCase() + "(" + move.newCol + ", " + move.newRow + ")";
            }
        }
        else{
            moveRecord = "\n Turn " + turn + " (" + move.piece.color.substring(0, 1).toUpperCase() + move.piece.color.substring(1).toLowerCase() + ")" + ": \n " + 
                move.piece.type.substring(0, 1).toUpperCase() + move.piece.type.substring(1).toLowerCase() + " moved from (" +
                move.previousCol + ", " + move.previousRow + ") to (" +
                move.newCol + ", " + move.newRow + ")";
        }
        moveLog.append(moveRecord + "\n");
    }
    
    // Main function to run the application
    public static void main(String[] args){
        Board b = new Board();
    }
}