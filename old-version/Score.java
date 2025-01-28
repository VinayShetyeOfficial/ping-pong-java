import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Score extends Rectangle {
	  static int GAME_WIDTH; // Width of the game window
	  static int GAME_HEIGHT; // Height of the game window
	  int player1; // To hold score of Player1
	  int player2; // To hold score of Player2

	  // Constructor to set the width and height of the game window
	  Score(int GAME_WIDTH, int GAME_HEIGHT) {
	    Score.GAME_WIDTH = GAME_WIDTH;
	    Score.GAME_HEIGHT = GAME_HEIGHT;
	  }

	  // Method to draw the score on the game window
	  public void draw(Graphics g) {
	    g.setColor(Color.white); // Set the color of the score to white
	    g.setFont(new Font("Consolas", Font.PLAIN, 60));; // Set the font of the score
	    g.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT); // Draw the line in the middle of the game window

	    g.drawString(String.valueOf(player1 / 10) + String.valueOf(player1 % 10), (GAME_WIDTH / 2) - 85, 50); // Draw player 1's score
	    g.drawString(String.valueOf(player1 / 10) + String.valueOf(player2 % 10), (GAME_WIDTH / 2) + 20, 50); // Draw player 2's score
	  }
	}