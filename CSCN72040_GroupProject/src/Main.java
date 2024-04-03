import javax.swing.*;

/**
 * The main class responsible for starting the Weather Application.
 */
public class Main {
    /**
     * The main method that starts the Weather Application GUI.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Start the Swing GUI in the event dispatch thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Display the Weather Application GUI
                new Interface().setVisible(true);
            }
        });
    }
}
