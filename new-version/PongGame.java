import javax.swing.SwingUtilities;

/**
 * Main entry point for the Ping Pong game.
 * Initializes the game frame on the Event Dispatch Thread for thread safety.
 */
public class PongGame {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new GameFrame());
  }
}