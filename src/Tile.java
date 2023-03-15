import javax.swing.*;
import java.awt.*;

public abstract class Tile extends JButton {

    public String typeTile;
    public int scale = 5;
    public int tileWidth = 115;
    public int tileHeight = 80;
    public int[] coords;
    public Image icon;

    public Tile(String typeTile, int[] coords){
        this.typeTile = typeTile;
        this.coords = coords;
        //this.setBorder(BorderFactory.createEmptyBorder());
    }

}
