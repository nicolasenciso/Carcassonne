import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.*;

public class GameEngine {

    private static int boardSize;

    private static int numTurns = 0;
    private static int countAbbeys = 0;
    private static int countCities = 0;
    private static GameEngine myGameEngine;
    private static JPanel gameGrid;
    private static JPanel lateralPanel;
    private static Tile selectedTile;
    private static ArrayList<Tile> ArrayTilesOnBoard;
    private static ArrayList<Tile> ArrayTilesLateralPanel;
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

    private static BoardGUI boardGame;

    private GameEngine(){

    }

    public static GameEngine generateGameEngine(){
        if(myGameEngine == null){
            myGameEngine = new GameEngine();
        }
        return myGameEngine;
    }

    public static Tile getTileSelected(){
        return selectedTile;
    }

    public static void addOneTurn(){
        numTurns += 1;
    }

    public static int getNumTurns(){
        return numTurns;
    }

    public static int getBoardSize(){
        return boardSize;
    }

    public static ArrayList<Tile> getArrayTilesOnBoard(){
        return ArrayTilesOnBoard;
    }

    public static ArrayList<Tile> getArrayTilesLateralPanel(){
        return ArrayTilesLateralPanel;
    }

    public static void setTileSelected(Tile tile){
        selectedTile = tile;
    }

    public void setInitialGameParameters(int boardSize, int probAbbeys, int probCities, int probRoads, boolean addDirectionalRoads){
        this.boardSize = boardSize;
        this.probAbbeys = probAbbeys;
        this.probCities = probCities;
        this.probRoads = probRoads;
        this.addDirectionalRoads = addDirectionalRoads;
        this.boardGame = BoardGUI.getGameBoard();
        this.selectedTile = null;

        setGameScenario();
    }
    public void setGameScenario(){
        setLayoutWindows();
        setGameGrid();
        setProbabilitiesIntervals();
        setLateralPanel();
        generateTilesToPlay(null);
    }

    public static void startGame(){
        windowGUI gameWindow = windowGUI.getWindow();
        MenuGUI gameMenu = MenuGUI.getMenuGUI();
        gameWindow.add(gameMenu);
        gameWindow.pack();
        gameWindow.setWindowConfig();
    }

    private void setGameGrid(){
        ArrayTilesOnBoard = new ArrayList<>();
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
                Tile middleTile = TileGenerator.getTile("road", new int[]{x, y}, false);
                middleTile.setName("gameTile");
                gameGrid.add(middleTile);
                ArrayTilesOnBoard.add(middleTile);
            }else{
                Tile gameTile = TileGenerator.getTile("empty", new int[]{x, y}, addDirectionalRoads);
                gameTile.setName("gameTile");
                gameGrid.add(gameTile);
                ArrayTilesOnBoard.add(gameTile);
            }
            x += 1;
        }
        boardGame.add(gameGrid, BorderLayout.CENTER);
    }

    public static void restartTurns(){
        numTurns = 1;
        countCities = 0;
        countAbbeys = 0;
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

    private static Tile getProbTile(){
        int randomNum = new Random().nextInt(100);
        if(randomNum <= 0){ randomNum = 50; }
        for(Map.Entry<String, Serializable> entry : probIntervals.entrySet()){
            if(((int [])entry.getValue())[0] <= randomNum  &&  randomNum <= ((int[]) entry.getValue())[1]){
                return TileGenerator.getTile(entry.getKey(), new int[]{0,0}, addDirectionalRoads);
            }
        }
        return null;
    }

    public static Tile dealtTile(){
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

    private static void setLateralPanel(){
        lateralPanel = new JPanel();
        lateralPanel.setBorder(new EmptyBorder(10,10,10,10));
        lateralPanel.setLayout(new GridLayout(5,1));
        boardGame.add(lateralPanel, BorderLayout.EAST);
    }

    public static void generateTilesToPlay(ArrayList<String> tilesSpecs){

        for(Component comp : lateralPanel.getComponents()){
            if(comp instanceof JButton){ lateralPanel.remove(comp); }
        }
        lateralPanel.revalidate();
        lateralPanel.repaint();
        ArrayTilesLateralPanel = new ArrayList<>();
        int index = 1;
        if(tilesSpecs == null){
            Tile tilesToPlay [] = new Tile[]{dealtTile(), dealtTile(), dealtTile(), dealtTile()};
            for(Tile tt : tilesToPlay){
                tt.setName("dealt" + index);
                lateralPanel.add(tt);
                ArrayTilesLateralPanel.add(tt);
                index += 1;
            }
        }else{
            for(String st : tilesSpecs){
                Tile newTile = null;
                if(st.contains("add")){
                    newTile = dealtTile();
                }else{
                    String specs[] = st.split("&");
                    if(specs[0].contains("abbey")){
                        newTile = TileGenerator.getTile("abbey", new int[]{0,0}, addDirectionalRoads);
                    }else if(specs[0].contains("city")){
                        newTile = TileGenerator.getTile("city", new int[]{0,0}, addDirectionalRoads);
                    }else{
                        RoadTile newTile2 = new RoadTile("road", new int[]{0,0}, addDirectionalRoads);
                        newTile2.resetConfigRoad(specs[1].split("/")[1]);
                        newTile2.setName("dealt" + index);
                        lateralPanel.add(newTile2);
                        ArrayTilesLateralPanel.add(newTile2);
                    }
                }
                if(newTile != null){
                    newTile.setName("dealt" + index);
                    lateralPanel.add(newTile);
                    ArrayTilesLateralPanel.add(newTile);
                }
                index += 1;
            }
        }
        Tile discardTile = TileGenerator.getTile("dealt", new int[]{0,0}, addDirectionalRoads);
        discardTile.setName("discard");
        discardTile.setEnabled(false);
        ArrayTilesLateralPanel.add(discardTile);
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
