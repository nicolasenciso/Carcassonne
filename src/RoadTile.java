import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Map;
import static java.util.Map.entry;
import java.util.Random;

public class RoadTile extends Tile{

    private Map<String, Serializable> Directions = Map.ofEntries(
      entry("right-left.png", new byte[]{1,3}),
      entry("up-down.png", new byte[]{0,2}),
      entry("four-way.png", new byte[]{0,1,2,3}),
      entry("three-way-1.png", new byte[]{0,1,3}),
      entry("three-way-2.png", new byte[]{0,1,2}),
      entry("three-way-3.png", new byte[]{1,2,3}),
      entry("three-way-4.png", new byte[]{0,2,3}),
      entry("corner-1.png", new byte[]{1,2}),
      entry("corner-2.png", new byte[]{2,3}),
      entry("corner-3.png", new byte[]{0,3}),
      entry("corner-4.png", new byte[]{0,1})
    );

    public RoadTile(String typeTile, int[] coords, boolean withDirection) {
        super(typeTile, coords);
        this.withDirections = withDirection;

        if(withDirection){
            // random selection of type road, all same probability
            int randomNumber = new Random().nextInt(Directions.values().size());
            String keyIcon = (String) Directions.keySet().toArray()[randomNumber];
            this.directions = (byte[]) Directions.get(keyIcon);
            this.sourceImg = "assets/"+keyIcon;
            try{
                icon = ImageIO.read(new FileInputStream(sourceImg));
                icon = icon.getScaledInstance(tileWidth, tileHeight, Image.SCALE_DEFAULT);
                this.setIcon(new ImageIcon(icon));
            }catch (Exception e){
                System.out.println(e);
            }
        }else{
            this.sourceImg = "assets/four-way.png";
            try{
                icon = ImageIO.read(new FileInputStream(sourceImg));
                icon = icon.getScaledInstance(tileWidth, tileHeight, Image.SCALE_DEFAULT);
                this.setIcon(new ImageIcon(icon));
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public byte[] getDirections() {
        return directions;
    }
}
