import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class GameEngine {

    private static int boardSize;

    private static int numTurns = 0;
    private static int countAbbeys = 0;
    private static int countCities = 0;

    private static String[] typeTiles = new String[]{"empty", "road", "abbey", "city"};
    private static GameEngine myGameEngine;
    private static JPanel gameGrid;
    private static JPanel lateralPanel;
    private static Tile selectedTile;
    private static Map<String, Serializable> probIntervals;
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
        setProbabilitiesIntervals();
        generateInitialTilesToPlay();

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
        int x = 0;
        int y = 0;
        for(int i = 0; i < boardSize*boardSize; i++){

            if(i % boardSize == 0 && i != 0){
                y += 1;
                x = 0;
            }
            // case middle tile always a four-way road
            if(x == boardSize/2 && y == boardSize/2){
                gameGrid.add(TileGenerator.getTile(typeTiles[1], new int[]{x, y}, false));
            }else{
                gameGrid.add(TileGenerator.getTile(typeTiles[0], new int[]{x, y}, addDirectionalRoads));
            }
            x += 1;
        }
        boardGame.add(gameGrid, BorderLayout.CENTER);
    }

    private void setLateralPanel(){
        lateralPanel = new JPanel();
        lateralPanel.setBorder(new EmptyBorder(10,10,10,10));
        lateralPanel.setLayout(new GridLayout(5,1));
        boardGame.add(lateralPanel, BorderLayout.EAST);
    }


    private void setProbabilitiesIntervals(){
        Integer arrProbs[] = new Integer[]{probAbbeys, probCities, probRoads};
        probIntervals = new HashMap<>();
        Arrays.sort(arrProbs, Collections.reverseOrder());
        Integer lastProb = 0;

        for(Integer prob: arrProbs){
            String intervalOwner = "";
            if(prob == probAbbeys && !probIntervals.containsKey("abbey")){
                intervalOwner = "abbey";
            }else if(prob == probCities && !probIntervals.containsKey("city")){
                intervalOwner = "city";
            }else if(prob == probRoads && !probIntervals.containsKey("road")){
                intervalOwner = "road";
            }
            probIntervals.put(intervalOwner, new int[]{lastProb + 1, prob + lastProb});
            lastProb = prob + lastProb;
        }
    }

    private Tile getProbTile(){
        int randomNum = new Random().nextInt(100);
        for(Map.Entry<String, Serializable> entry : probIntervals.entrySet()){
            if(((int [])entry.getValue())[0] <= randomNum  &&  randomNum <= ((int[]) entry.getValue())[1]){
                return TileGenerator.getTile(entry.getKey(), new int[]{0,0}, addDirectionalRoads);
            }
        }
        return null;
    }

    private Tile dealtTile(){
        Tile newTile = getProbTile();

        // 15 turns rule
        if((numTurns == 5 && countCities == 0) ||
                (numTurns == 10 && countCities < 2) ||
                (numTurns == 14 && countCities < 3)){
            newTile = TileGenerator.getTile("city", new int[]{0,0}, addDirectionalRoads);
            countCities += 1;
        }
        if(numTurns == 15 && countAbbeys == 0){
            newTile = TileGenerator.getTile("abbey", new int[]{0,0}, addDirectionalRoads);
            countAbbeys += 1;
        }

        return newTile;
    }
    private void generateInitialTilesToPlay(){
        Tile tilesToPlay [] = new Tile[]{dealtTile(), dealtTile(), dealtTile(), dealtTile()};
        int index = 1;
        for(Tile tt : tilesToPlay){
            tt.setName("dealt" + index);
            lateralPanel.add(tt);
            index += 1;
        }
        Tile discardTile = TileGenerator.getTile("dealt", new int[]{0,0}, addDirectionalRoads);
        discardTile.setName("discard");
        discardTile.setEnabled(false);
        lateralPanel.add(discardTile);
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
