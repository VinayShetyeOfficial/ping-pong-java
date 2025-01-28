import java.awt.*;

/**
 * Manages and displays the game score.
 * Handles score tracking and rendering for both players.
 */
public class Score extends Rectangle {
	private static int GAME_WIDTH; // Width of the game window
	private static int GAME_HEIGHT; // Height of the game window
	private int player1; // To hold score of Player1
	private int player2; // To hold score of Player2

	/**
	 * Constructs a new score manager.
	 * 
	 * @param GAME_WIDTH  Width of the game window
	 * @param GAME_HEIGHT Height of the game window
	 */
	public Score(int GAME_WIDTH, int GAME_HEIGHT) {
		this.GAME_WIDTH = GAME_WIDTH;
		this.GAME_HEIGHT = GAME_HEIGHT;
	}

	/**
	 * Renders the current score and center line on the screen.
	 * 
	 * @param g Graphics context for rendering
	 */
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// Create a gradient background for the score area
		GradientPaint gradient = new GradientPaint(
				0, 0, new Color(40, 40, 40, 150),
				0, 50, new Color(0, 0, 0, 0));
		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, GAME_WIDTH, 50);

		// Draw scores with modern font and style
		g.setFont(new Font("Arial", Font.BOLD, 48));
		g.setColor(new Color(66, 135, 245)); // Blue for player 1
		g.drawString(String.format("%02d", player1), (GAME_WIDTH / 2) - 85, 45);

		g.setColor(new Color(245, 66, 66)); // Red for player 2
		g.drawString(String.format("%02d", player2), (GAME_WIDTH / 2) + 20, 45);

		// Draw center line with dashed style from top to bottom
		g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 10 }, 0));
		g2d.setColor(new Color(255, 255, 255, 100));
		g2d.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);
	}

	// Add getters for scores
	public int getPlayer1Score() {
		return player1;
	}

	public int getPlayer2Score() {
		return player2;
	}

	public void incrementPlayer1() {
		player1++;
	}

	public void incrementPlayer2() {
		player2++;
	}

	public void resetScore() {
		player1 = 0;
		player2 = 0;
	}
}