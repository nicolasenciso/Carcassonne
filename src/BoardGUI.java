import javax.swing.*;
import java.awt.Toolkit;
import java.awt.Dimension;

//Singleton class
public class BoardGUI {

    private static BoardGUI gameBoard;
    private final JFrame gameFrame;
    private static final int defaultSize = 11;
    private static int boardSize;

    // No further instances, class only
    private BoardGUI(int size){
        this.boardSize = size;

        Toolkit screen = Toolkit.getDefaultToolkit();
        Dimension screenSize = screen.getScreenSize();

        this.gameFrame = new JFrame("Carcassonne_NicolasRicardoEnciso");
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setResizable(false);
        this.gameFrame.setVisible(true);

    }

    public static BoardGUI createGameBoard(int size){
        if(gameBoard == null){gameBoard = new BoardGUI(size);}
        return gameBoard;
    }

    public static BoardGUI getGameBoard(){
        if(gameBoard == null){gameBoard = new BoardGUI(defaultSize);}
        return gameBoard;
    }

    public static int getSizeBoard(){
        return boardSize;
    }

}
