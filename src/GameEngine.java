import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameEngine {

    private static int boardSize;

    private static int numTurns = 0;

    private static String[] typeTiles = new String[]{"road", "abbey", "city"};
    private static String[] typeRoads = new String[]{"right-left", "up-down", "four-way", "three-way", "corner"};
    private static GameEngine myGameEngine;
    private static JPanel gameGrid;
    private static JPanel lateralPanel;
    private static int probAbbeys;
    private static int probCities;
    private static int probRoads;
    private static boolean addDirectionalRoads;

    final static int originalTileSize = 16;
    final static int scale = 3;
    final static int tileSize = originalTileSize * scale;
    private static int screenWidth = tileSize * 30;
    private static int screenHeight = tileSize * 21;

    private BoardGUI boardGame;

    private GameEngine(){

    }

    public static GameEngine generateGameEngine(){
        if(myGameEngine == null){
            myGameEngine = new GameEngine();
        }
        return myGameEngine;
    }

    public void setInitialGameParameters(int boardSize, int probAbbeys, int probCities, int probRoads, boolean addDirectionalRoads){
        this.boardSize = boardSize;
        this.probAbbeys = probAbbeys;
        this.probCities = probCities;
        this.probRoads = probRoads;
        this.addDirectionalRoads = addDirectionalRoads;
        this.boardGame = BoardGUI.getGameBoard();

        setGameScenario();
    }

    public void setGameScenario(){

        setLayoutWindows();
        setGameGrid();
        setLateralPanel();
    }

    public static void startGame(){

        windowGUI gameWindow = windowGUI.getWindow();
        MenuGUI gameMenu = MenuGUI.getMenuGUI();

        gameWindow.add(gameMenu);
        gameWindow.pack();
        gameWindow.setWindowConfig();
    }

    private void setGameGrid(){
        gameGrid = new JPanel();
        gameGrid.setLayout(new GridLayout(boardSize, boardSize));
        for(int i = 0; i < boardSize*boardSize; i++){
            gameGrid.add(new AbbeyTile("abbey", new int[]{1,2}));
        }
        boardGame.add(gameGrid, BorderLayout.CENTER);
    }

    private void setLateralPanel(){
        lateralPanel = new JPanel();
        lateralPanel.setBorder(new EmptyBorder(10,10,10,10));
        lateralPanel.setLayout(new GridLayout(5,1));
        for(int i = 0; i < 5; i++){
            lateralPanel.add(new AbbeyTile("abbey", new int[]{1,2}));
        }
        boardGame.add(lateralPanel, BorderLayout.EAST);
    }

    private void setLayoutWindows(){
        boardGame.add(getEmptyPanel(false), BorderLayout.NORTH);
        boardGame.add(getEmptyPanel(false), BorderLayout.SOUTH);
        boardGame.add(getEmptyPanel(true), BorderLayout.WEST);
    }



    private static JPanel getEmptyPanel(boolean isVertical){
        JPanel emptyPanel = new JPanel();
        if (isVertical){
            emptyPanel.setPreferredSize(new Dimension(tileSize * 2, screenHeight));
        }else{
            emptyPanel.setPreferredSize(new Dimension(screenWidth, tileSize * 2));
        }
        emptyPanel.setOpaque(false);
        return emptyPanel;
    }
}
