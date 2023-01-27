import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

//GamePanel class extends JPanel and implements runnable interface
public class GamePanel extends JPanel implements Runnable {
	
	// constants for game width, height, ball and paddle dimensions
	static final int GAME_WIDTH = 1000;
	static final int GAME_HEIGHT = (int)(GAME_WIDTH*(0.55555));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int PADDLE_WIDTH = 25;
	static final int PADDLE_HEIGHT = 100;
	
	// game thread, image, graphics, random object, paddles and ball
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Paddle paddle1;
	Paddle paddle2;
	Ball ball;
	Score score;
	
	// constructor of GamePanel
	GamePanel(){
		// create new paddles and ball
		newPaddles();
		newBall();
		score = new Score(GAME_WIDTH, GAME_HEIGHT);
		
		// set focusable true and add key listener
		this.setFocusable(true);
		this.addKeyListener(new AL());
		
		// set preferred size of the panel
		this.setPreferredSize(SCREEN_SIZE);
		
		// start the game thread
		gameThread = new Thread(this);
		gameThread.start();
	};
	
	// method to create new ball
	public void newBall(){
		random = new Random();
		ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER, BALL_DIAMETER);
		
	}
	
	// method to create new paddles
	public void newPaddles(){
		paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
		paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
		
	}
	
	// paint method to draw the game elements
	public void paint(Graphics g){
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0 ,0, this);
	}
	
	public void draw(Graphics g){
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		score.draw(g);
	}
	
	public void move(){
		// move both paddles
		paddle1.move();
		paddle2.move();
		// move the ball
		ball.move();
	}
	
	public void checkCollision(){
		
		// Check for collision with top & bottom window edges
	    // if the ball's y-coordinate is less than or equal to 0,
	    // set the ball's y-direction to be the opposite of its current y-velocity
		if(ball.y <= 0){
			ball.setYDirection(-ball.yVelocity);
		}
		
		// if the ball's y-coordinate is greater than or equal to the game height minus the ball's diameter,
	    // set the ball's y-direction to be the opposite of its current y-velocity
		if(ball.y >= GAME_HEIGHT-BALL_DIAMETER){
			ball.setYDirection(-ball.yVelocity);
		}
		
		// Check for collision with left paddle
		if(ball.intersects(paddle1)){
			// change the x-velocity to be positive and increment by 1 (for added difficulty)
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; 
			
			// if the y-velocity is greater than 0, increment by 1 (for added difficulty)
		    // otherwise, decrement by 1
			if(ball.yVelocity > 0){
				ball.yVelocity++; 
			}
			else{
				ball.yVelocity--;
			}
			
			// set the x-direction to be the x-velocity and the y-direction to be the y-velocity
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		
		// Check for collision with right paddle
		if(ball.intersects(paddle2)){
			// change the x-velocity to be positive and increment by 1 (for added difficulty)
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; // Optonal for more difficulty 
			
			// if the y-velocity is greater than 0, increment by 1 (for added difficulty)
		      // otherwise, decrement by 1
			if(ball.yVelocity > 0){
				ball.yVelocity++; // Optonal for more difficulty 
			}
			else{
				ball.yVelocity--;
			}
			
			// set the x-direction to be the opposite of the x-velocity and the y-direction to be the y-velocity
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		
		
		// Check for collision with left and right window edges
	    // if the paddle's y-coordinate is less than or equal to 0, set the paddle's y-coordinate to be 0
		if(paddle1.y <= 0){
			paddle1.y = 0;
		}
		
		// if the paddle's y-coordinate is greater than or equal to the game height minus the paddle's height,
	    // set the paddle's y-coordinate to be the game height minus the paddle's height
		if(paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT)){
			paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;	// set the y position of paddle1 to the bottom edge if it goes below it
		}
		if(paddle2.y <= 0){
			paddle2.y = 0;	// set the y position of paddle2 to 0 if it goes above the top edge
		}
		if(paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT)){
			paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;	// set the y position of paddle2 to the bottom edge if it goes below it
		}
		
		// give player 1 point and and creates new paddles & ball
		if(ball.x <= 0){
			score.player2++;	// increase player 2's score by 1
			newPaddles();		// create new paddles
			newBall();			// create new ball
			System.out.println("pLAYER 2: "+score.player2);	// print player 2's updated score
		}
		if(ball.x >= GAME_WIDTH-BALL_DIAMETER){
			score.player1++;
			newPaddles();
			newBall();
			System.out.println("Player 1: "+score.player1);	// print player 1's updated score
		}
	}
	
	public void run(){
		// game loop
		long lastTime = System.nanoTime();		// get current time in nanoseconds
		double amountOfTicks = 60.0;			// number of ticks per second
		double ns = 1000000000 / amountOfTicks;	// 1 billion nanoseconds per tick
		double delta = 0;						// time since last tick
		while(true){
			long now = System.nanoTime();		// get current time in nanoseconds
			delta += (now - lastTime)/ns;		// add the elapsed time since last tick
			lastTime = now;
			if(delta >= 1){
				move();							// move the paddles and ball
				checkCollision();
				repaint();						// repaint the screen
				delta--;
			}
		}
	}
	
	public class AL extends KeyAdapter{			// inner class for handling keyboard input
		public void keyPressed(KeyEvent e){
			paddle1.keyPressed(e);				// call keyPressed method of paddle1
			paddle2.keyPressed(e);				// call keyPressed method of paddle2
		}
		  
		public void keyReleased(KeyEvent e){   	// inner class for handling keyboard input
			paddle1.keyReleased(e);				// call keyReleased method of paddle1
			paddle2.keyReleased(e);				// call keyReleased method of paddle2
		}
	}
}