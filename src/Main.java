import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import view.MainMenu;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}
