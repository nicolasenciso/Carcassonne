import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

public class MenuGUI extends JPanel{
    private static MenuGUI menu;
    private Image backgroundMenuImg;
    final static int originalTileSize = 20;
    final static int scale = 3;
    final static int tileSize = originalTileSize * scale;
    final static int maxWidthTiles = 10;
    final static int maxHeightTiles = 7;
    final static int screenWidth = tileSize * maxWidthTiles;
    final static int screenHeight = tileSize * maxHeightTiles;

    private MenuGUI(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        try {
            backgroundMenuImg = ImageIO.read(new File("assets/backMenu.png"));
            g.drawImage(backgroundMenuImg, 0, 0, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int fontSize = 70;
        int xTitle = (int) ((screenWidth / 2)*0.2);
        int yTitle = (int) ((screenHeight / 2) * 0.4);
        setTitleGame(g2, fontSize);
        g.drawString("CARCASSONNE", xTitle, yTitle);
        setShadowTitle(g2, fontSize);
        g.drawString("CARCASSONNE", xTitle + 5, yTitle + 5);
    }

    public static MenuGUI getMenuGUI(){
        if(menu == null){
            menu = new MenuGUI();
        }
        return menu;
    }

    private static Graphics2D setShadowTitle(Graphics2D g2, int fontSize){
        g2.setFont(new Font("Bitstream Charter", Font.BOLD | Font.ITALIC, fontSize));
        g2.setColor(new Color(102, 102, 0));
        return g2;
    }

    private static Graphics2D setTitleGame(Graphics2D g2, int fontSize){
        g2.setFont(new Font("Bitstream Charter", Font.BOLD | Font.ITALIC, fontSize));
        g2.setColor(new Color(204, 204, 0));
        return g2;
    }

}
