import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;

public class CityTile extends Tile{
    public CityTile(String typeTile, int[] coords) {
        super(typeTile, coords);
        sourceImg = "assets/city.png";
        try{
            icon = ImageIO.read(new FileInputStream(sourceImg));
            icon = icon.getScaledInstance(tileWidth, tileHeight, Image.SCALE_DEFAULT);
            this.setIcon(new ImageIcon(icon));
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
