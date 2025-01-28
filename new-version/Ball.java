import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Represents the game ball in the Ping Pong game.
 * Handles ball movement, velocity, and rendering.
 */
public class Ball extends Rectangle {

	private final Random random;
	private int xVelocity;
	private int yVelocity;
	private static final int INITIAL_SPEED = 2;

	/**
	 * Constructs a new ball with specified position and dimensions.
	 * 
	 * @param x      X-coordinate of the ball
	 * @param y      Y-coordinate of the ball
	 * @param width  Width of the ball
	 * @param height Height of the ball
	 */
	public Ball(int x, int y, int width, int height) {
		super(x, y, width, height);
		random = new Random();
		initializeVelocity();
	}

	private void initializeVelocity() {
		int randomXDirection = random.nextInt(2) == 0 ? -1 : 1;
		int randomYDirection = random.nextInt(2) == 0 ? -1 : 1;
		setXDirection(randomXDirection * INITIAL_SPEED);
		setYDirection(randomYDirection * INITIAL_SPEED);
	}

	/**
	 * Method to set the xVelocity of the ball
	 * 
	 * @param randomXDirection Horizontal velocity direction
	 */
	public void setXDirection(int randomXDirection) {
		xVelocity = randomXDirection;
	}

	/**
	 * Method to set the yVelocity of the ball
	 * 
	 * @param randomYDirection Vertical velocity direction
	 */
	public void setYDirection(int randomYDirection) {
		yVelocity = randomYDirection;
	}

	/**
	 * Updates the ball's position based on its velocity.
	 */
	public void move() {
		x += xVelocity;
		y += yVelocity;
	}

	/**
	 * Renders the ball on the screen.
	 * 
	 * @param g Graphics context for rendering
	 */
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// Enable antialiasing for smoother rendering
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Create a gradient for the ball
		RadialGradientPaint gradient = new RadialGradientPaint(
				new Point2D.Float(x + width / 2, y + height / 2),
				width / 2,
				new float[] { 0.0f, 1.0f },
				new Color[] { Color.WHITE, new Color(200, 200, 200) });

		g2d.setPaint(gradient);
		g2d.fillOval(x, y, width, height);

		// Add a subtle shadow
		g2d.setColor(new Color(0, 0, 0, 50));
		g2d.drawOval(x, y, width, height);
	}

	public int getXVelocity() {
		return xVelocity;
	}

	public int getYVelocity() {
		return yVelocity;
	}

	/**
	 * Sets the ball's velocity components.
	 * 
	 * @param xVelocity Horizontal velocity
	 * @param yVelocity Vertical velocity
	 */
	public void setVelocity(int xVelocity, int yVelocity) {
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}
}
