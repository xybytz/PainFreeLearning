package com.playfulminds;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

/**
 * Application entry point. Initializes the JFrame and sets up the main container on the EDT.
 */
public class Main {
    public static void main(String[] args) {
        // Ensure UI creation happens on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Playful Minds: Audio-Visual Early Learning");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GameController controller = new GameController();
        GamePanel gamePanel = new GamePanel(controller);
        
        frame.add(gamePanel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}
