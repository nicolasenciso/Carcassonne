import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

public class MenuGUI extends JPanel implements ActionListener {
    private static MenuGUI menu;
    private static int defaultSizeBoard = 11;
    private Image backgroundMenuImg;
    final static int originalTileSize = 20;
    final static int scale = 3;
    final static int tileSize = originalTileSize * scale;
    final static int maxWidthTiles = 10;
    final static int maxHeightTiles = 7;
    final static int screenWidth = tileSize * maxWidthTiles;
    final static int screenHeight = tileSize * maxHeightTiles;

    private JButton newGameButton;
    private JTextField sizeBoardTextField;
    private JLabel sizeBoardLabel;
    private JCheckBox addRouteDirection;
    private JTextField probAbbeys;
    private JTextField probRoads;
    private JTextField probCities;
    private JLabel probAbbLabel;
    private JLabel probRoadLabel;
    private JLabel probCityLabel;
    private JLabel titleProbs;

    private MenuGUI(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(this);

        sizeBoardTextField = new JTextField(String.valueOf(defaultSizeBoard));
        sizeBoardLabel = new JLabel("Board size");
        addRouteDirection = new JCheckBox("Direction road tiles");

        titleProbs = new JLabel("Probabilities:");
        probAbbLabel = new JLabel("Abbeys:");
        probCityLabel = new JLabel("Cities:");
        probRoadLabel = new JLabel("Roads:");

        probAbbeys = new JTextField();
        probCities = new JTextField();
        probRoads = new JTextField();
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

    private void setAndAddNewGameButton(int xNewGameButton, int yNewGameButton, int widthNewGameButton,
                                               int heightNewGameButton, JButton newGameButton){
        newGameButton.setBounds(xNewGameButton, yNewGameButton, widthNewGameButton, heightNewGameButton);
        newGameButton.setBorderPainted(false);
        this.add(newGameButton);
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

        int xNewGameButton = xTitle + (tileSize*2);
        int yNewGameButton = yTitle + (int) (tileSize*0.7);
        int widthNewGameButton = tileSize * 4;
        int heightNewGameButton = (int)(tileSize*0.7);

        int xBackRect = xTitle + tileSize;
        int yBackRect = yTitle + tileSize * 2;
        int widthBackRect = tileSize * 6;
        int heightBackRect = tileSize * 2;

        int xSizeBoard = xBackRect + (int ) (tileSize*1.5);
        int ySizeBoard = yBackRect + 10;
        int widthSizeBoard = tileSize - 20;
        int heightSizeBoard = tileSize - 30;

        int xSizeBoardLabel = xBackRect + 10;
        int ySizeBoardLabel = yBackRect + 10;
        int widthSizeBoardLabel = widthSizeBoard * 2;

        int yAddRouteDirection = ySizeBoardLabel + tileSize;
        int widthAddRouteDirection = tileSize * 3;

        // add new game button to panel
        String titleName = "CARCASSONNE";
        setAndAddNewGameButton(xNewGameButton, yNewGameButton, widthNewGameButton, heightNewGameButton, newGameButton);

        // add text field for board size
        sizeBoardTextField.setBounds(xSizeBoard, ySizeBoard, widthSizeBoard, heightSizeBoard);
        add(sizeBoardTextField);

        // add title with shadow
        setTitleGame(g2, fontSize);
        g.drawString(titleName, xTitle, yTitle);
        setShadowTitle(g2, fontSize);
        g.drawString(titleName, xTitle + 5, yTitle + 5);

        // add back rect to settings
        g.setColor(new Color(224,224,224, 150));
        g.fillRect(xBackRect, yBackRect, widthBackRect, heightBackRect);

        // add text box for size board
        sizeBoardLabel.setBounds(xSizeBoardLabel, ySizeBoardLabel, widthSizeBoardLabel, heightSizeBoard);
        add(sizeBoardLabel);

        // add checkbox roads with direction
        addRouteDirection.setBounds(xSizeBoardLabel, yAddRouteDirection, widthAddRouteDirection, heightSizeBoard);
        addRouteDirection.setBackground(new Color(224,224,224, 190));
        add(addRouteDirection);


    }

    // Listener to know what to do when click buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel empty = new JPanel();
        empty.setPreferredSize(new Dimension(screenWidth+100, screenHeight+100));
        empty.setBackground(Color.BLUE);
        windowGUI gameWindow = windowGUI.getWindow();
        gameWindow.remove(this);
        gameWindow.add(empty);
        gameWindow.pack();
    }
}
