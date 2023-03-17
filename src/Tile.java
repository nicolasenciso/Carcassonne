import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

// Factory pattern
public abstract class Tile extends JButton {

    // TODO: automatic scale of tileSize depending of board size
    public String typeTile;
    public ActionButtonsController actioner;
    public int scale = 5;
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
                    "No more empty tiles available, END OF THE GAME !. YOUR SCORE IS : ",
                    "No empty tiles", JOptionPane.INFORMATION_MESSAGE);
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
                    "Your tiles can't play, END OF THE GAME !. YOUR SCORE IS : ",
                    "Tiles can't play", JOptionPane.INFORMATION_MESSAGE);
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
            }else{
                // game board tiles
                if(!isOpaque() && GameEngine.getTileSelected() != null){
                    validatePlacement();
                }
            }
        }
    }

}
