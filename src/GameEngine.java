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
        for(int i = 0; i < boardSize*boardSize; i++){
            if(i==boardSize) {
                gameGrid.add(TileGenerator.getTile(typeTiles[1], new int[]{1, 1}, false));
            }else{
                gameGrid.add(TileGenerator.getTile(typeTiles[0], new int[]{1, 1}, false));
            }
        }
        boardGame.add(gameGrid, BorderLayout.CENTER);
    }

    private void setLateralPanel(){
        lateralPanel = new JPanel();
        lateralPanel.setBorder(new EmptyBorder(10,10,10,10));
        lateralPanel.setLayout(new GridLayout(5,1));
        for(int i = 0; i < 5; i++){
            lateralPanel.add(TileGenerator.getTile(typeTiles[0], new int[]{1, 1}, false));
        }
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
    private Tile[] generateInitialTilesToPlay(){
        Tile tilesToPlay [] = new Tile[]{getProbTile(), getProbTile(), getProbTile(), getProbTile()};

        ArrayList <Integer> indexAbbAndCity = new ArrayList<>();
        int numCitiesToPlay = 0;
        int numAbbeysToPlay = 0;

        for(int i = 0; i < tilesToPlay.length; i++){
            if(tilesToPlay[i].getTypeTile() == "abbey"){
                numAbbeysToPlay += 1;
                indexAbbAndCity.add(i);
            }
            if(tilesToPlay[i].getTypeTile() == "city"){
                numCitiesToPlay += 1;
                indexAbbAndCity.add(i);
            }
        }

        // check for 15 turns rule
        Collections.sort(indexAbbAndCity);

        // case less than 3 cities
        if(numCitiesToPlay == 0){
            if((numTurns == 5 && countCities == 0) ||
                    (numTurns == 10 && countCities < 2) ||
                    (numTurns == 14 && countCities < 3)){
                Tile newCity = TileGenerator.getTile("city", new int[]{0,0}, addDirectionalRoads);
                for(int i = 0; i < 4; i++){
                    if(!indexAbbAndCity.contains(i)){
                        tilesToPlay[i] = newCity;
                        break;
                    }
                }
                countCities += 1;
            }
        }
        // case less than 1 abbey
        if(numAbbeysToPlay == 0 && numTurns == 15 && countAbbeys == 0){
            Tile newAbbey = TileGenerator.getTile("abbey", new int[]{0,0}, addDirectionalRoads);
            for(int i = 0; i < 4; i++){
                if(!indexAbbAndCity.contains(i)){
                    tilesToPlay[i] = newAbbey;
                    break;
                }
            }
            countAbbeys += 1;
        }
        return tilesToPlay;
    }

    private void setLayoutWindows(){
        boardGame.add(getEmptyPanel(false), BorderLayout.NORTH);
        boardGame.add(getEmptyPanel(false), BorderLayout.SOUTH);
        boardGame.add(getEmptyPanel(true), BorderLayout.WEST);
    }

    public void addTurn(){
        this.numTurns += 1;
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
