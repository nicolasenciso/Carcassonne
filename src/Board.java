import javax.swing.*;

//Singleton class
public class Board {

    private static Board gameBoard;
    private final JFrame gameFrame;
    private static final int defaultSize = 11;
    private static int boardSize;

    // No further instances, class only
    private Board(int size){
        this.boardSize = size;
        this.gameFrame = new JFrame("JBoard");
    }

    public static Board createGameBoard(int size){
        if(gameBoard == null){gameBoard = new Board(size);}
        return gameBoard;
    }

    public static Board getGameBoard(){
        if(gameBoard == null){gameBoard = new Board(defaultSize);}
        return gameBoard;
    }

    public static int getSizeBoard(){
        return boardSize;
    }

}
