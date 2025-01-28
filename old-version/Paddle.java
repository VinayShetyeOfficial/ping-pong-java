import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Paddle extends Rectangle {
  // id = 1 for Player1, id = 2 for Player2.
  int id;
  // How fast the paddle moves up and down when we press keys.
  int yVelocity;
  // Speed of the paddle
  int speed = 10;

  /**
   * Paddle constructor
   * @param x x-coordinate of the paddle
   * @param y y-coordinate of the paddle
   * @param PADDLE_WIDTH width of the paddle
   * @param PADDLE_HEIGHT height of the paddle
   * @param id id of the paddle (1 for Player1, 2 for Player2)
   */
  Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id) {
    super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
    this.id = id;
  }

  /**
   * Handle key pressed event
   * @param e KeyEvent object representing the key pressed
   */
  public void keyPressed(KeyEvent e) {
    switch (id) {
    case 1:
      if (e.getKeyCode() == KeyEvent.VK_W) {
        setYDirection(-speed);
        move();
      }
      if (e.getKeyCode() == KeyEvent.VK_S) {
        setYDirection(speed);
        move();
      }
      break;
    case 2:
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        setYDirection(-speed);
        move();
      }
      if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        setYDirection(speed);
        move();
      }
      break;
    }
  }

  /**
   * Handle key released event
   * @param e KeyEvent object representing the key released
   */
  public void keyReleased(KeyEvent e) {
    switch (id) {
    case 1:
      if (e.getKeyCode() == KeyEvent.VK_W) {
        setYDirection(0);
        move();
      }
      if (e.getKeyCode() == KeyEvent.VK_S) {
        setYDirection(0);
        move();
      }
      break;
    case 2:
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        setYDirection(0);
        move();
      }
      if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        setYDirection(speed);
        move();
      }
      break;
    }
  }

  // Set the vertical direction of player movement
  public void setYDirection(int yDirection) {
    yVelocity = yDirection;
  }

  // Move player based on yVelocity
  public void move() {
    y = y + yVelocity;
  }

  // Draw player on screen
  public void draw(Graphics g) {
    if (id == 1) {
      g.setColor(Color.blue);
    } else {
      g.setColor(Color.red);
    }
    g.fillRect(x, y, width, height);
  }
}