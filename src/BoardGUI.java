import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

//Singleton class
public class BoardGUI extends JPanel implements ActionListener {

    private static BoardGUI gameBoard;
    private static final String titleName = "CARCASSONNE";
    private Image backgroundMenuImg;
    final static int originalTileSize = 16;
    final static int scale = 3;
    final static int tileSize = originalTileSize * scale;
    private static int screenWidth = tileSize * 30;
    private static int screenHeight = tileSize * 23;

    // No further instances, class only
    private BoardGUI(){

        //TODO: resizable for value size board less 11 increase scale
        // set check tile compatibility on engine module
        // check when the game starts maybe make it in another class like play

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);

        this.setLayout(new BorderLayout());

        windowGUI gameWindow = windowGUI.getWindow();
        gameWindow.remove(MenuGUI.getMenuGUI());
        gameWindow.add(this);
        gameWindow.pack();

    }

    public static BoardGUI getGameBoard(){
        if(gameBoard == null){
            gameBoard = new BoardGUI();
        }
        return gameBoard;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        try {
            backgroundMenuImg = ImageIO.read(new File("assets/backMenu.png"));
            backgroundMenuImg = backgroundMenuImg.getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);
            g.drawImage(backgroundMenuImg, 0, 0, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int fontSize = 70;
        int xTitle = (screenWidth / 3);
        g2.setFont(new Font("Bitstream Charter", Font.BOLD | Font.ITALIC, fontSize));
        g2.setColor(new Color(204, 204, 0));
        g.drawString(titleName, xTitle, tileSize);

        g2.setFont(new Font("Bitstream Charter", Font.BOLD | Font.ITALIC, fontSize));
        g2.setColor(new Color(102, 102, 0));
        g.drawString(titleName, xTitle + 5, tileSize + 5);
    }
}
