import javax.swing.JFrame;

public class windowGUI extends JFrame{

    private static windowGUI window;
    private static String title = "CARCASSONNE - NICOLAS RICARDO ENCISO";

    private windowGUI(){}

    public static windowGUI getWindow(){
        if(window == null){
            window = new windowGUI();
        }
        return window;
    }

    public static void setWindowConfig(){
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle(title);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}
