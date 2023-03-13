import java.awt.*;

public class Main {
    public static void main(String[] args) {

//        String [] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//        for(String name: names){
//            System.out.println(name);
//        }

        windowGUI gameWindow = windowGUI.getWindow();
        MenuGUI gameMenu = MenuGUI.getMenuGUI();

        gameWindow.add(gameMenu);
        gameWindow.pack();
        gameWindow.setWindowConfig();


    }
}