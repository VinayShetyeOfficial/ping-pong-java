import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GameFrame extends JFrame {
	
	// Instance variable for the GamePanel
	GamePanel panel;
	
	// Constructor to create a new GameFrame object
	GameFrame(){
		// Initialize the panel
		panel = new GamePanel();
		// Add the panel to the frame
		this.add(panel);
		// Set the title of the frame
		this.setTitle("Pong Game");
		// Disable resizing of the frame
		this.setResizable(false);
		// Set the background color of the frame
		this.setBackground(Color.black);
		// Set the default close operation of the frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Pack the frame to the preferred size
		this.pack();
		// Set the frame to be visible
		this.setVisible(true);
		// Center the frame on the screen
		this.setLocationRelativeTo(null);
	};
}
