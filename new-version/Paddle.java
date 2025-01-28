import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Represents a player paddle in the Ping Pong game.
 * Handles paddle movement, input processing, and rendering.
 */
public class Paddle extends Rectangle {
  // id = 1 for Player1, id = 2 for Player2.
  private final int id;
  // How fast the paddle moves up and down when we press keys.
  private int yVelocity;
  // Speed of the paddle
  private static final int SPEED = 10;

  /**
   * Constructs a new paddle with specified position, dimensions, and player ID.
   * 
   * @param x      X-coordinate of the paddle
   * @param y      Y-coordinate of the paddle
   * @param width  Width of the paddle
   * @param height Height of the paddle
   * @param id     Player identifier (1 for left paddle, 2 for right paddle)
   */
  public Paddle(int x, int y, int width, int height, int id) {
    super(x, y, width, height);
    this.id = id;
  }

  /**
   * Processes keyboard input for paddle movement.
   * Player 1 uses W/S keys, Player 2 uses UP/DOWN arrows.
   * 
   * @param e KeyEvent containing the pressed key information
   */
  public void keyPressed(KeyEvent e) {
    switch (id) {
      case 1 -> {
        if (e.getKeyCode() == KeyEvent.VK_W) {
          setYDirection(-SPEED);
          move();
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
          setYDirection(SPEED);
          move();
        }
      }
      case 2 -> {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          setYDirection(-SPEED);
          move();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          setYDirection(SPEED);
          move();
        }
      }
    }
  }

  /**
   * Processes key release events to stop paddle movement.
   * 
   * @param e KeyEvent containing the released key information
   */
  public void keyReleased(KeyEvent e) {
    switch (id) {
      case 1 -> {
        if (e.getKeyCode() == KeyEvent.VK_W) {
          setYDirection(0);
          move();
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
          setYDirection(0);
          move();
        }
      }
      case 2 -> {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          setYDirection(0);
          move();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          setYDirection(0);
          move();
        }
      }
    }
  }

  // Set the vertical direction of player movement
  public void setYDirection(int yDirection) {
    yVelocity = yDirection;
  }

  /**
   * Updates the paddle's position based on its velocity.
   */
  public void move() {
    y += yVelocity;
  }

  /**
   * Renders the paddle on the screen.
   * Player 1's paddle is blue, Player 2's paddle is red.
   * 
   * @param g Graphics context for rendering
   */
  public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Create gradients for paddles
    Color mainColor = (id == 1) ? new Color(66, 135, 245) : // Blue for player 1
        new Color(245, 66, 66); // Red for player 2

    Color brightColor = (id == 1) ? new Color(100, 160, 255) : // Lighter blue
        new Color(255, 100, 100); // Lighter red

    GradientPaint gradient = new GradientPaint(
        x, y, mainColor,
        x + width, y, brightColor);

    g2d.setPaint(gradient);

    // Draw paddle with rounded corners
    RoundRectangle2D paddle = new RoundRectangle2D.Float(x, y, width, height, 10, 10);
    g2d.fill(paddle);

    // Add highlight effect
    g2d.setColor(new Color(255, 255, 255, 50));
    g2d.fillRoundRect(x + 2, y + 2, width / 2 - 2, height - 4, 8, 8);
  }
}