import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Start the application on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 