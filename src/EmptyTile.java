import java.awt.*;

public class EmptyTile extends Tile{
    public EmptyTile(String typeTile, int[] coords) {
        super(typeTile, coords);
        this.setBackground(new Color(255,255,255,150));
    }
}
