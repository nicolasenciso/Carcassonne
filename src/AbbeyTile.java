import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AbbeyTile extends Tile{
    public AbbeyTile(String typeTile, int[] coords) {
        super(typeTile, coords);
        try{
            icon = ImageIO.read(new FileInputStream("assets/abbey.png"));
            icon = icon.getScaledInstance(tileWidth, tileHeight, Image.SCALE_DEFAULT);
            this.setIcon(new ImageIcon(icon));
        }catch (Exception e){
            System.out.println(e);
        }
    }

}