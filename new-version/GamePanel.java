import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * Main game panel that manages the game loop and all game elements.
 * Handles rendering, game state management, collision detection, and user
 * input.
 * 
 * Features:
 * - Multiple game states (Menu, Playing, Paused, Game Over)
 * - Animated menu with star field background
 * - Countdown sequence before game start
 * - Score tracking and win condition
 * - Sound effects and background music
 * - Collision detection and ball physics
 * 
 * Controls:
 * - Menu navigation: Arrow keys and Enter
 * - Player 1: W/S keys
 * - Player 2: Up/Down arrows
 * - Pause: Escape
 */
public final class GamePanel extends JPanel implements Runnable {

	// constants for game width, height, ball and paddle dimensions
	private static final int GAME_WIDTH = 1000;
	private static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.55555));
	private static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
	private static final int BALL_DIAMETER = 20;
	private static final int PADDLE_WIDTH = 25;
	private static final int PADDLE_HEIGHT = 100;

	// game thread, image, graphics, random object, paddles and ball
	private Thread gameThread;
	private Image image;
	private Graphics graphics;
	private final Random random;
	private Paddle paddle1;
	private Paddle paddle2;
	private Ball ball;
	private final Score score;

	private static final int GAME_STATE_START = 0;
	private static final int GAME_STATE_PLAYING = 1;
	private static final int GAME_STATE_PAUSED = 2;
	private static final int GAME_STATE_GAME_OVER = 3;
	private static final int GAME_STATE_COUNTDOWN = 4;
	private int gameState = GAME_STATE_START;

	private static final int MENU_OPTION_PLAY = 0;
	private static final int MENU_OPTION_CONTROLS = 1;
	private static final int MENU_OPTION_EXIT = 2;
	private int selectedOption = MENU_OPTION_PLAY;
	private float titleGlow = 0;
	private boolean glowIncreasing = true;
	private final ArrayList<Star> stars = new ArrayList<>();

	private static final int WINNING_SCORE = 5; // Add winning score constant
	private boolean gameInitialized = false;
	private int countdownNumber = 3;
	private long countdownStartTime;

	// Inner class for background stars
	private class Star {
		float x, y;
		float speed;
		float brightness;

		Star() {
			x = random.nextFloat() * GAME_WIDTH;
			y = random.nextFloat() * GAME_HEIGHT;
			speed = random.nextFloat() * 2 + 1;
			brightness = random.nextFloat();
		}

		void update() {
			x -= speed;
			if (x < 0) {
				x = GAME_WIDTH;
				y = random.nextFloat() * GAME_HEIGHT;
			}
			// Ensure brightness stays between 0.3 and 1.0
			brightness = (float) (0.3 + (0.7 * Math.abs(Math.sin(System.currentTimeMillis() * 0.003 * speed))));
		}
	}

	// constructor of GamePanel
	GamePanel() {
		random = new Random();
		paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
		paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH,
				PADDLE_HEIGHT, 2);
		score = new Score(GAME_WIDTH, GAME_HEIGHT);

		// set focusable true and add key listener
		this.setFocusable(true);
		this.addKeyListener(new AL());

		// set preferred size of the panel
		this.setPreferredSize(SCREEN_SIZE);

		// Initialize stars
		for (int i = 0; i < 100; i++) {
			stars.add(new Star());
		}

		// Initialize sound
		SoundManager.init();
		SoundManager.playMenuMusic();
	}

	public void startGame() {
		gameThread = new Thread(this);
		gameThread.start();
		initializeGame();
	}

	private void initializeGame() {
		if (!gameInitialized) {
			newBall();
			newPaddles();
			score.resetScore();
			gameInitialized = true;
		}
	}

	// method to create new ball
	public void newBall() {
		// Randomize initial direction
		int initialXVelocity = (random.nextBoolean() ? 1 : -1) * 2;
		int initialYVelocity = random.nextInt(4) - 2; // Random value between -2 and 2

		ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2),
				(GAME_HEIGHT / 2) - (BALL_DIAMETER / 2),
				BALL_DIAMETER, BALL_DIAMETER);
		ball.setXDirection(initialXVelocity);
		ball.setYDirection(initialYVelocity);
	}

	// method to create new paddles
	public void newPaddles() {
		paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
		paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH,
				PADDLE_HEIGHT, 2);
	}

	// paint method to draw the game elements
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (gameState == GAME_STATE_START) {
			drawStartScreen(g2d);
		} else {
			image = createImage(getWidth(), getHeight());
			graphics = image.getGraphics();
			draw(graphics);
			g.drawImage(image, 0, 0, this);
		}
	}

	private void drawStartScreen(Graphics2D g) {
		// Draw animated starfield background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

		// Update and draw stars
		for (Star star : stars) {
			star.update();
			float clampedBrightness = Math.max(0.0f, Math.min(1.0f, star.brightness));
			g.setColor(new Color(1f, 1f, 1f, clampedBrightness));
			g.fillRect((int) star.x, (int) star.y, 2, 2);
		}

		// Draw glowing title with gaming font
		g.setFont(new Font("Arial Black", Font.BOLD, 80));
		GradientPaint gradient = new GradientPaint(
				0, GAME_HEIGHT / 2 - 100, new Color(66, 135, 245),
				GAME_WIDTH, GAME_HEIGHT / 2 - 100, new Color(245, 66, 66));
		g.setPaint(gradient);
		String title = "PING PONG";
		FontMetrics titleMetrics = g.getFontMetrics();
		int titleX = (GAME_WIDTH - titleMetrics.stringWidth(title)) / 2;

		// Add shadow effect to title
		g.setColor(new Color(0, 0, 0, 100));
		g.drawString(title, titleX + 4, GAME_HEIGHT / 3 + 4);

		// Draw main title
		g.setPaint(gradient);
		g.drawString(title, titleX, GAME_HEIGHT / 3);

		// Draw glow effect
		Composite originalComposite = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, titleGlow * 0.5f));
		g.drawString(title, titleX, GAME_HEIGHT / 3);
		g.setComposite(originalComposite);

		// Draw menu options with enhanced styling
		g.setFont(new Font("Segoe UI", Font.BOLD, 28));
		String[] options = { "PLAY GAME", "CONTROLS", "EXIT" };
		int menuStartY = GAME_HEIGHT / 2 + 50;
		int menuSpacing = 50;

		for (int i = 0; i < options.length; i++) {
			FontMetrics metrics = g.getFontMetrics();
			int y = menuStartY + (i * menuSpacing);

			if (i == selectedOption) {
				// Draw glowing selection
				g.setColor(new Color(255, 255, 255, 50));
				int rectWidth = metrics.stringWidth(options[i]) + 60;
				int rectHeight = 40;
				g.fillRoundRect(
						(GAME_WIDTH - rectWidth) / 2,
						y - rectHeight + 10,
						rectWidth,
						rectHeight,
						20,
						20);

				// Draw selected option
				g.setColor(Color.WHITE);
				g.drawString(options[i],
						(GAME_WIDTH - metrics.stringWidth(options[i])) / 2,
						y);
			} else {
				// Draw unselected option
				g.setColor(new Color(200, 200, 200));
				g.drawString(options[i],
						(GAME_WIDTH - metrics.stringWidth(options[i])) / 2,
						y);
			}
		}

		// Draw controls with enhanced styling
		if (selectedOption == MENU_OPTION_CONTROLS) {
			// Semi-transparent background panel
			g.setColor(new Color(0, 0, 0, 230));
			int panelWidth = GAME_WIDTH / 4;
			int panelHeight = 100;
			int margin = 2; // Even smaller bottom margin
			int panelX = GAME_WIDTH - panelWidth - 5; // Keep right margin at 5
			int panelY = GAME_HEIGHT - panelHeight - margin; // Almost at the bottom
			g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 2, 2);

			// Draw controls text with smaller font
			g.setFont(new Font("Segoe UI", Font.BOLD, 22));
			FontMetrics metrics = g.getFontMetrics();
			int textRightMargin = 15;

			// Player 1 controls
			g.setColor(new Color(66, 135, 245));
			String p1Text = "Player 1: W/S";
			g.drawString(p1Text,
					panelX + panelWidth - metrics.stringWidth(p1Text) - textRightMargin,
					panelY + 40);

			// Player 2 controls
			g.setColor(new Color(245, 66, 66));
			String p2Text = "Player 2: ";
			String p2Arrows = "↑/↓";
			g.drawString(p2Text,
					panelX + panelWidth - metrics.stringWidth(p2Text + p2Arrows) - textRightMargin,
					panelY + 75);

			// Draw arrows in bold and larger font
			g.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Slightly larger font for arrows
			metrics = g.getFontMetrics();
			g.drawString(p2Arrows,
					panelX + panelWidth - metrics.stringWidth(p2Arrows) - textRightMargin,
					panelY + 75);
		}
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// Create a dark gradient background
		GradientPaint backgroundGradient = new GradientPaint(
				0, 0, new Color(20, 20, 30),
				GAME_WIDTH, GAME_HEIGHT, new Color(40, 40, 60));
		g2d.setPaint(backgroundGradient);
		g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

		// Add subtle grid effect
		g2d.setColor(new Color(255, 255, 255, 15));
		for (int i = 0; i < GAME_WIDTH; i += 50) {
			g2d.drawLine(i, 0, i, GAME_HEIGHT);
		}
		for (int i = 0; i < GAME_HEIGHT; i += 50) {
			g2d.drawLine(0, i, GAME_WIDTH, i);
		}

		// Draw game elements
		paddle1.draw(g2d);
		paddle2.draw(g2d);
		if (gameState != GAME_STATE_START && gameState != GAME_STATE_COUNTDOWN) {
			ball.draw(g2d);
		}
		score.draw(g2d);

		// Draw countdown overlay
		if (gameState == GAME_STATE_COUNTDOWN) {
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Arial", Font.BOLD, 30));
			String startingText = "Starting in";
			FontMetrics metrics = g2d.getFontMetrics();
			g2d.drawString(startingText,
					(GAME_WIDTH - metrics.stringWidth(startingText)) / 2,
					GAME_HEIGHT / 2 - 30);

			g2d.setFont(new Font("Arial", Font.BOLD, 28));
			String countText = String.valueOf(countdownNumber);
			metrics = g2d.getFontMetrics();
			g2d.drawString(countText,
					(GAME_WIDTH - metrics.stringWidth(countText)) / 2,
					GAME_HEIGHT / 2 + 20);

			// Check if it's time to update countdown
			long currentTime = System.currentTimeMillis();
			if (currentTime - countdownStartTime >= 1000) {
				countdownNumber--;
				countdownStartTime = currentTime;
				if (countdownNumber < 1) {
					gameState = GAME_STATE_PLAYING;
				}
			}
		}

		// Draw pause/game over overlay
		if (gameState == GAME_STATE_PAUSED || gameState == GAME_STATE_GAME_OVER) {
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Arial", Font.BOLD, 36));
			String message = gameState == GAME_STATE_PAUSED ? "PAUSED"
					: "GAME OVER - " + (score.getPlayer1Score() > score.getPlayer2Score() ? "Player 1" : "Player 2")
							+ " Wins!";
			FontMetrics metrics = g2d.getFontMetrics();
			g2d.drawString(message,
					(GAME_WIDTH - metrics.stringWidth(message)) / 2,
					GAME_HEIGHT / 2);

			g2d.setFont(new Font("Arial", Font.PLAIN, 20));
			String subMessage = gameState == GAME_STATE_PAUSED ? "Press SPACE to continue"
					: "Press SPACE to play again";
			metrics = g2d.getFontMetrics();
			g2d.drawString(subMessage,
					(GAME_WIDTH - metrics.stringWidth(subMessage)) / 2,
					GAME_HEIGHT / 2 + 40);
		}
	}

	public void move() {
		// move both paddles
		paddle1.move();
		paddle2.move();
		// move the ball
		ball.move();
	}

	public void checkCollision() {
		// Constants for ball physics
		final int MAX_VELOCITY = 10;

		// Check for collision with top & bottom window edges
		// if the ball's y-coordinate is less than or equal to 0,
		// set the ball's y-direction to be the opposite of its current y-velocity
		if (ball.y <= 0) {
			ball.setYDirection(-ball.getYVelocity());
		}

		// if the ball's y-coordinate is greater than or equal to the game height minus
		// the ball's diameter,
		// set the ball's y-direction to be the opposite of its current y-velocity
		if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
			ball.setYDirection(-ball.getYVelocity());
		}

		// Check for collision with left paddle
		if (ball.intersects(paddle1)) {
			int newXVelocity = Math.min(Math.abs(ball.getXVelocity()) + 1, MAX_VELOCITY);
			float normalizedRelativeIntersectY = (paddle1.y + (PADDLE_HEIGHT / 2)) - (ball.y + (BALL_DIAMETER / 2));
			int newYVelocity = (int) (normalizedRelativeIntersectY / (PADDLE_HEIGHT / 2) * MAX_VELOCITY);
			ball.setVelocity(newXVelocity, newYVelocity);
			SoundManager.playPaddleHitSound();
		}

		// Check for collision with right paddle
		if (ball.intersects(paddle2)) {
			int newXVelocity = -Math.min(Math.abs(ball.getXVelocity()) + 1, MAX_VELOCITY);
			float normalizedRelativeIntersectY = (paddle2.y + (PADDLE_HEIGHT / 2)) - (ball.y + (BALL_DIAMETER / 2));
			int newYVelocity = (int) (normalizedRelativeIntersectY / (PADDLE_HEIGHT / 2) * MAX_VELOCITY);
			ball.setVelocity(newXVelocity, newYVelocity);
			SoundManager.playPaddleHitSound();
		}

		// Check for collision with left and right window edges
		// if the paddle's y-coordinate is less than or equal to 0, set the paddle's
		// y-coordinate to be 0
		if (paddle1.y <= 0) {
			paddle1.y = 0;
		}

		// if the paddle's y-coordinate is greater than or equal to the game height
		// minus the paddle's height,
		// set the paddle's y-coordinate to be the game height minus the paddle's height
		if (paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
			paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT; // set the y position of paddle1 to the bottom edge if it goes
														// below it
		}
		if (paddle2.y <= 0) {
			paddle2.y = 0; // set the y position of paddle2 to 0 if it goes above the top edge
		}
		if (paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
			paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT; // set the y position of paddle2 to the bottom edge if it goes
														// below it
		}

		// give player 1 point and and creates new paddles & ball
		if (ball.x <= 0) {
			score.incrementPlayer2();
			newPaddles(); // create new paddles
			newBall(); // create new ball
		}
		if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
			score.incrementPlayer1();
			newPaddles();
			newBall();
		}
	}

	private void checkGameOver() {
		if (score.getPlayer1Score() >= WINNING_SCORE || score.getPlayer2Score() >= WINNING_SCORE) {
			gameState = GAME_STATE_GAME_OVER;
			SoundManager.playGameOverSound();
		}
	}

	@Override
	public void run() {
		// game loop
		long lastTime = System.nanoTime(); // get current time in nanoseconds
		double amountOfTicks = 60.0; // number of ticks per second
		double ns = 1000000000 / amountOfTicks; // 1 billion nanoseconds per tick
		double delta = 0; // time since last tick
		while (true) {
			long now = System.nanoTime(); // get current time in nanoseconds
			delta += (now - lastTime) / ns; // add the elapsed time since last tick
			lastTime = now;
			if (delta >= 1) {
				if (gameState == GAME_STATE_PLAYING) {
					move(); // move the paddles and ball
					checkCollision();
					checkGameOver();
				}
				repaint(); // repaint the screen
				delta--;
			}
		}
	}

	public class AL extends KeyAdapter { // inner class for handling keyboard input
		@Override
		public void keyPressed(KeyEvent e) {
			if (gameState == GAME_STATE_START) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP -> {
						selectedOption = (selectedOption - 1 + 3) % 3;
						SoundManager.playButtonSound();
					}
					case KeyEvent.VK_DOWN -> {
						selectedOption = (selectedOption + 1) % 3;
						SoundManager.playButtonSound();
					}
					case KeyEvent.VK_ENTER -> {
						switch (selectedOption) {
							case MENU_OPTION_PLAY -> {
								gameState = GAME_STATE_COUNTDOWN;
								countdownNumber = 3;
								countdownStartTime = System.currentTimeMillis();
								SoundManager.stopGameMusic();
								SoundManager.playBallBounceSound();
							}
							case MENU_OPTION_EXIT -> System.exit(0);
						}
					}
				}
				repaint();
			} else {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_ESCAPE -> {
						if (gameState == GAME_STATE_PLAYING) {
							gameState = GAME_STATE_PAUSED;
							SoundManager.playButtonSound();
						}
					}
					case KeyEvent.VK_SPACE -> {
						if (gameState == GAME_STATE_PAUSED) {
							gameState = GAME_STATE_PLAYING;
							SoundManager.playButtonSound();
						} else if (gameState == GAME_STATE_GAME_OVER) {
							gameState = GAME_STATE_PLAYING;
							gameInitialized = false;
							initializeGame();
							SoundManager.playButtonSound();
						}
					}
					default -> {
						paddle1.keyPressed(e);
						paddle2.keyPressed(e);
					}
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) { // inner class for handling keyboard input
			if (gameState == GAME_STATE_PLAYING) {
				paddle1.keyReleased(e); // call keyReleased method of paddle1
				paddle2.keyReleased(e); // call keyReleased method of paddle2
			}
		}
	}
}