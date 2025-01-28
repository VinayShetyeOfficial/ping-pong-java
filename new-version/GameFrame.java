import java.awt.Color;
import javax.swing.JFrame;

/**
 * Main window frame for the Ping Pong game.
 * Sets up the game window and initializes the game panel.
 */
public final class GameFrame extends JFrame {

	// Instance variable for the GamePanel
	private final GamePanel panel;

	/**
	 * Constructs the game window with appropriate settings.
	 * Initializes a new GamePanel and configures window properties.
	 */
	GameFrame() {
		panel = new GamePanel();
		initFrame();
		panel.startGame(); // Start the game thread after frame initialization
	}

	private void initFrame() {
		add(panel);
		setTitle("Pong Game");
		setResizable(false);
		setBackground(Color.black);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
