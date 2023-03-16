import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;

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
    public byte[] directions;
    public Image icon;

    public Tile(String typeTile, int[] coords){
        this.typeTile = typeTile;
        this.coords = coords;
        this.setOpaque(false);
        actioner = new ActionButtonsController();
        this.setAction(actioner);
        this.withDirections = false;
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
            System.out.println(e);
        }
        // agregar modificar valor en arraylist de fichas
    }

    private void validatePlacement(){
        switch (GameEngine.getTileSelected().typeTile){
            case "abbey":
                if(validateAbbey()){ transferTile(); }
                break;
            case "road":
                break;
            case "city":
                break;
        }
    }

    private boolean validateAbbey(){
        if(this.typeTile == "empty"){
            int x = this.coords[0];
            int y = this.coords[1];
            for(Tile tile : GameEngine.getArrayTilesOnBoard()){
                // check up, right, down and left
                if(tile.typeTile != "empty"){
                    if((tile.coords[0] == x) && (tile.coords[1] == y + 1)){ return true; }
                    if((tile.coords[0] == x + 1) && (tile.coords[1] == y)){ return true; }
                    if((tile.coords[0] == x) && (tile.coords[1] == y - 1)){ return true; }
                    if((tile.coords[0] == x - 1) && (tile.coords[1] == y)){ return true; }
                }

            }
        }
        return false;
    }

    private class ActionButtonsController extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {

            // Using opaque as a flag to know a tile was selected only lateral panel ones
            if(getName().contains("dealt")){
                if(isOpaque()){
                    setBorder(BorderFactory.createEmptyBorder());
                    setOpaque(false);
                    GameEngine.setTileSelected(null);
                }else if(!isOpaque() && GameEngine.getTileSelected() == null){
                    setBorder(new LineBorder(Color.GREEN, 8));
                    setOpaque(true);
                    GameEngine.setTileSelected((Tile) e.getSource());
                }
            }else{
                // game board tiles
                if(!isOpaque() && GameEngine.getTileSelected() != null){
                    validatePlacement();
                }
            }
        }
    }

}
