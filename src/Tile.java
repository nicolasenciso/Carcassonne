import javax.swing.*;
import java.awt.*;

public abstract class Tile extends JButton {

    public String typeTile;
    public String[] coords;
    public Image icon;
    public byte[] directions;

    public Tile(String typeTile, String[] coords, byte[] directions){
        this.typeTile = typeTile;
        this.coords = coords;
        this.directions = directions;
    }

}
