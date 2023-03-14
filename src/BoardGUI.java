import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

//Singleton class
public class BoardGUI extends JPanel implements ActionListener {

    private static BoardGUI gameBoard;
    private Image backgroundMenuImg;
    private BorderLayout screenGrid;
    final static int originalTileSize = 20;
    final static int scale = 3;
    final static int tileSize = originalTileSize * scale;
    private static int screenWidth = tileSize * 10;
    private static int screenHeight = tileSize * 10;
    private static int boardSize;
    private static int probAbbeys;
    private static int probCities;
    private static int probRoads;
    private static boolean addDirectionalRoads;

    // No further instances, class only
    private BoardGUI(int boardSize, int probAbbeys, int probCities, int probRoads, boolean addDirectionalRoads){
        this.boardSize = boardSize;
        this.probAbbeys = probAbbeys;
        this.probCities = probCities;
        this.probRoads = probRoads;
        this.addDirectionalRoads = addDirectionalRoads;

        screenWidth = (tileSize * boardSize) + (tileSize * 7);
        screenHeight = (tileSize * boardSize) + (tileSize * 5);

        this.screenGrid = new BorderLayout();


        //TODO: resizable for value size board less 11 increase scale
        // set check tile compatibility on engine module
        // check when the game starts maybe make it in another class like play

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
        setLayout(screenGrid);

        windowGUI gameWindow = windowGUI.getWindow();
        gameWindow.remove(MenuGUI.getMenuGUI());
        gameWindow.add(this);
        gameWindow.pack();

    }

    public static BoardGUI getGameBoard(int boardSize, int probAbbeys, int probCities,
                                           int probRoads, boolean addDirectionalRoads){
        if(gameBoard == null){
            gameBoard = new BoardGUI(boardSize, probAbbeys, probCities, probRoads, addDirectionalRoads);
        }
        return gameBoard;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        try {
            backgroundMenuImg = ImageIO.read(new File("assets/backMenu.png"));
            backgroundMenuImg = backgroundMenuImg.getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);
            g.drawImage(backgroundMenuImg, 0, 0, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
