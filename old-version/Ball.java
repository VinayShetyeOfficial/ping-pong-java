import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Ball extends Rectangle {
	
	Random random;
	int xVelocity;  // How fast the ball will move along x-axis.
	int yVelocity;  // How fast the ball will move along y-axis.
	int initialSpeed = 2;
	
	// Constructor to create a new ball object with specified position, width and height
	Ball(int x, int y, int width, int height){
		super(x, y, width, height);
		random = new Random();
		
		// Set random direction for xVelocity
		int randomXDirection = random.nextInt(2);
		if(randomXDirection == 0){
			randomXDirection--;
		}
		setXDirection(randomXDirection*initialSpeed);
		
		// Set random direction for yVelocity
		int randomYDirection = random.nextInt(2);
		if(randomYDirection == 0){
			randomYDirection--;
		}
		setYDirection(randomYDirection*initialSpeed);
	}
	
	// Method to set the xVelocity of the ball
	public void setXDirection(int randomXDirection){
		xVelocity = randomXDirection;
	}
	
	// Method to set the yVelocity of the ball
	public void setYDirection(int randomYDirection){
		yVelocity = randomYDirection;
	}
	
	// Method to move the ball based on its xVelocity and yVelocity
	public void move(){
		x += xVelocity;
		y += yVelocity;
	}
	
	// Method to draw the ball on the screen
	public void draw(Graphics g){
		g.setColor(Color.white);
		g.fillOval(x, y, height, width);
	}
}
