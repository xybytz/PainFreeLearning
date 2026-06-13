package com.playfulminds;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The core JPanel where custom rendering (paintComponent) and interactions occur.
 */
public class GamePanel extends JPanel {
    
    private GameController controller;

    public GamePanel(GameController controller) {
        this.controller = controller;
        this.controller.setGamePanel(this);
        
        // Setup mouse listeners for interactions
        setupListeners();
        
        // Setup key listener for Sensory Mode
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_H) {
                    controller.toggleSensoryMode();
                }
            }
        });
    }

    private void setupListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleClick(e.getX(), e.getY());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                controller.handleDrag(e.getX(), e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                controller.handleMouseMove(e.getX(), e.getY());
            }
        };
        
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        // Apply rendering hints for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Delegate rendering to the controller
        controller.render(g2d);
    }
}
