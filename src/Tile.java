import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

// Factory pattern
public abstract class Tile extends JButton {

    public String typeTile;
    public ActionButtonsController actioner;
    public int tileWidth = 115;
    public String sourceImg;
    public int tileHeight = 80;
    public int[] coords;
    public boolean withDirections;
    public int[] directions;
    public Image icon;

    public Tile(String typeTile, int[] coords){
        this.typeTile = typeTile;
        this.coords = coords;
        this.setOpaque(false);
        actioner = new ActionButtonsController();
        this.setAction(actioner);
        this.withDirections = false;
        this.directions = new int[]{};

        if(GameEngine.getBoardSize() > 11 && GameEngine.getBoardSize() <= 21){
            this.tileHeight -= 30;
            this.tileWidth -= 30;
        }else if(GameEngine.getBoardSize() > 21){
            this.tileHeight -= 60;
            this.tileWidth -= 60;
        }else if(GameEngine.getBoardSize() < 11){
            this.tileWidth += 30;
            this.tileHeight += 30;
        }
    }

    public void transferTile(){
        Tile tileSelected = GameEngine.getTileSelected();
        this.typeTile = tileSelected.typeTile;
        this.sourceImg = tileSelected.sourceImg;

        if(tileSelected.typeTile == "road"){
            this.directions = tileSelected.directions;
            this.withDirections = tileSelected.withDirections;
        }
        try{
            icon = ImageIO.read(new FileInputStream(sourceImg));
            icon = icon.getScaledInstance(tileWidth, tileHeight, Image.SCALE_DEFAULT);
            this.setIcon(new ImageIcon(icon));
        }catch (Exception e){
            System.out.println("Error en transferTile, no existe " + sourceImg);
        }
        GameEngine.addOneTurn();
        turnManagement();
        replacePlayedTile();
        checkEndGame();
    }

    private int getScore(){
        int score = 0;
        for(Tile tile : GameEngine.getArrayTilesOnBoard()){
            if(tile.typeTile == "abbey"){
                Tile poss = null;
                // north
                poss = getTileByCoords(new int[]{tile.coords[0], tile.coords[1] - 1});
                if(poss != null && poss.typeTile != "empty"){ score += 1; }
                // north - east
                poss = getTileByCoords(new int[]{tile.coords[0] + 1, tile.coords[1] - 1});
                if(poss != null && poss.typeTile != "empty"){ score += 1; }
                // east
                poss = getTileByCoords(new int[]{tile.coords[0] + 1, tile.coords[1]});
                if(poss != null && poss.typeTile != "empty"){ score += 1; }
                // south - east
                poss = getTileByCoords(new int[]{tile.coords[0] + 1, tile.coords[1] - 1});
                if(poss != null && poss.typeTile != "empty"){ score += 1; }
                // south
                poss = getTileByCoords(new int[]{tile.coords[0], tile.coords[1] + 1});
                if(poss != null && poss.typeTile != "empty"){ score += 1; }
                // south west
                poss = getTileByCoords(new int[]{tile.coords[0] - 1, tile.coords[1] - 1});
                if(poss != null && poss.typeTile != "empty"){ score += 1; }
                // west
                poss = getTileByCoords(new int[]{tile.coords[0] - 1, tile.coords[1]});
                if(poss != null && poss.typeTile != "empty"){ score += 1; }
                // north west
                poss = getTileByCoords(new int[]{tile.coords[0] - 1, tile.coords[1] - 1});
                if(poss != null && poss.typeTile != "empty"){ score += 1; }
            }else if(tile.typeTile == "city"){
                score += 2;
                // BFS TRAVERSAL GRAPH
                ArrayList<String> visited = new ArrayList<>();
                LinkedList<Tile> queue = new LinkedList<>();
                queue.add(tile);
                while(queue.size() != 0){
                    Tile s = queue.poll();
                    visited.add(s.coords[0] + "," + s.coords[1]);
                    for(Tile adj : getAdjTiles(s)){
                        if(adj != null && adj.typeTile == "city" && !visited.contains(adj.coords[0] + "," + adj.coords[1])){
                            visited.add(adj.coords[0] + "," + adj.coords[1]);
                            score += 1;
                            queue.add(adj);
                        }
                    }
                }
            }else if(tile.typeTile == "road"){
                // roads already are chained accrding to directions
                score += 1;
            }
        }
        return score;
    }

    private ArrayList<Tile> getAdjTiles(Tile tile){
        ArrayList<Tile> adjs = new ArrayList<>();
        adjs.add(getTileByCoords(new int[]{tile.coords[0], tile.coords[1] - 1}));
        adjs.add(getTileByCoords(new int[]{tile.coords[0] + 1, tile.coords[1]}));
        adjs.add(getTileByCoords(new int[]{tile.coords[0], tile.coords[1] + 1}));
        adjs.add(getTileByCoords(new int[]{tile.coords[0] - 1, tile.coords[1]}));
        return adjs;
    }

    private Tile getTileByCoords(int coords []){
        Tile getTile = null;
        for(Tile tile : GameEngine.getArrayTilesOnBoard()){
            if(tile.coords[0] == coords[0] && tile.coords[1] == coords[1]){
                getTile = tile;
                break;
            }
        }
        return getTile;
    }

    private void checkEndGame(){

        // no more empty tiles available
        boolean emptyTilesAvailable = false;
        for(Tile tile : GameEngine.getArrayTilesOnBoard()){
            if(tile.typeTile == "empty"){
                emptyTilesAvailable = true;
                break;
            }
        }
        if (!emptyTilesAvailable){
            JOptionPane.showMessageDialog(null,
                    "No more empty tiles available. YOUR SCORE IS: " + getScore(),
                    "END OF GAME !", JOptionPane.INFORMATION_MESSAGE);
        }
        // cant play with given tiles
        ArrayList<Boolean> checkers = new ArrayList<>();
        for(Tile panelTile : GameEngine.getArrayTilesLateralPanel()){
            for(Tile boardTile : GameEngine.getArrayTilesOnBoard()){
                if(boardTile.typeTile == "empty"){
                    if(panelTile.typeTile.contains("abbey") || panelTile.typeTile.contains("city")){
                        if(validateAbbeyAndCity(boardTile)){
                            checkers.add(true);
                        }else{
                            checkers.add(false);
                        }
                    }else{
                        // case road
                        if(validateRoad(panelTile, boardTile)){
                            checkers.add(true);
                        }else{
                            checkers.add(false);
                        }
                    }
                }
            }
        }
        if (!checkers.contains((true))){
            JOptionPane.showMessageDialog(null,
                    "Your tiles can't play. YOUR SCORE IS: " + getScore(),
                    "END OF GAME !", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void replacePlayedTile(){
        ArrayList<String> tilesSpecs = new ArrayList<>();
        for(Tile tile : GameEngine.getArrayTilesLateralPanel()){
           if(tile.isOpaque()){
               tilesSpecs.add("add");
           }else if(!tile.isOpaque() && !tile.getName().contains("discard")){
               tilesSpecs.add(tile.typeTile + "&" + tile.sourceImg);
           }
        }
        GameEngine.generateTilesToPlay(tilesSpecs);
        GameEngine.setTileSelected(null);
        GameEngine.addOneTurn();
        turnManagement();
    }

    private void turnManagement(){
        // Discard tiles
        for(Tile tile : GameEngine.getArrayTilesLateralPanel()){
            if(tile.getName() == "discard"){
                if(GameEngine.getNumTurns() % 5 == 0 && GameEngine.getNumTurns() != 0){
                    tile.setEnabled(true);
                }else{
                    tile.setEnabled(false);
                }
            }
        }
        // restart counters turn 15
        if(GameEngine.getNumTurns() == 16){ GameEngine.restartTurns(); }
    }

    private void validatePlacement(){
        if(GameEngine.getTileSelected().typeTile != "road") {
            if(validateAbbeyAndCity(null)){
                transferTile();
            }
        }else{
            if(validateRoad(null, null)){
                transferTile();
            }
        }
    }

    private boolean validateAbbeyAndCity(Tile currentTile){

        if(currentTile == null){ currentTile = this; }
        if(currentTile.typeTile == "empty"){
            int x = currentTile.coords[0];
            int y = currentTile.coords[1];
            for(Tile tile : GameEngine.getArrayTilesOnBoard()){
                // check up, right, down and left
                if(tile.typeTile != "empty"){
                    if((tile.coords[0] == x) && (tile.coords[1] == y - 1)){ return true; }
                    if((tile.coords[0] == x + 1) && (tile.coords[1] == y)){ return true; }
                    if((tile.coords[0] == x) && (tile.coords[1] == y + 1)){ return true; }
                    if((tile.coords[0] == x - 1) && (tile.coords[1] == y)){ return true; }
                }
            }
        }
        return false;
    }

    private boolean validateRoad(Tile selected, Tile currentTile){
        if(currentTile == null){
            currentTile = this;
            selected = GameEngine.getTileSelected();;
        }
        if(currentTile.typeTile == "empty"){
            int x = currentTile.coords[0];
            int y = currentTile.coords[1];
            for(Tile tile : GameEngine.getArrayTilesOnBoard()){
                // check up, right, down and left AND if road continues there (directions)
                if(tile.typeTile == "road"){
                    if((tile.coords[0] == x) && (tile.coords[1] == y - 1) &&
                            (containsNum(selected.directions, 0)) &&
                            (containsNum(tile.directions, 2))){
                        return true;

                    }else if((tile.coords[0] == x + 1) && (tile.coords[1] == y) &&
                            (containsNum(selected.directions, 1))  &&
                            (containsNum(tile.directions, 3))){
                        return true;

                    }else if((tile.coords[0] == x) && (tile.coords[1] == y + 1) &&
                            (containsNum(selected.directions, 2))  &&
                            (containsNum(tile.directions, 0))){
                        return true;

                    }else if((tile.coords[0] == x - 1) && (tile.coords[1] == y) &&
                            (containsNum(selected.directions, 3))  &&
                            (containsNum(tile.directions, 1))){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean containsNum(int array[], int num){
        for(int i = 0; i < array.length; i++){
            if(array[i] == num){ return true; }
        }
        return false;
    }

    private void resetTilesPanel(){
        GameEngine.generateTilesToPlay(null);
        GameEngine.setTileSelected(null);
    }

    private class ActionButtonsController extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {

            // Using opaque as a flag to know a tile was selected only lateral panel ones
            if (getName().contains("dealt")) {
                if (isOpaque()) {
                    setBorder(BorderFactory.createEmptyBorder());
                    setOpaque(false);
                    GameEngine.setTileSelected(null);
                } else if (!isOpaque() && GameEngine.getTileSelected() == null) {
                    setBorder(new LineBorder(Color.GREEN, 8));
                    setOpaque(true);
                    GameEngine.setTileSelected((Tile) e.getSource());
                }
            }else if(getName().contains("discard")){
                checkEndGame();
                resetTilesPanel();
                checkEndGame();
            }else{
                // game board tiles
                if(!isOpaque() && GameEngine.getTileSelected() != null){
                    validatePlacement();
                }
            }
        }
    }

}
