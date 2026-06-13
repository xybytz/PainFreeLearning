package com.playfulminds;

import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Manages the game state, level generation, and smooth rendering loops.
 */
public class GameController {
    
    private GamePanel gamePanel;
    private int score;
    private Timer gameLoopTimer;
    private AudioManager audioManager;
    
    private List<VisualAsset> assets;
    private VisualAsset targetAsset;
    
    // Particle system for reward
    private List<Particle> particles;
    private boolean isRewardAnimationPlaying;
    
    private boolean isHighContrastMode;
    private Random random = new Random();

    public GameController() {
        this.score = 0;
        this.audioManager = new AudioManager();
        this.assets = new ArrayList<>();
        this.particles = new ArrayList<>();
        this.isRewardAnimationPlaying = false;
        this.isHighContrastMode = false;
        
        initializeGame();
    }

    public void setGamePanel(GamePanel panel) {
        this.gamePanel = panel;
    }

    private void initializeGame() {
        generateNewLevel();
        gameLoopTimer = new Timer(16, e -> update()); // ~60fps
        gameLoopTimer.start();
    }
    
    private void generateNewLevel() {
        assets.clear();
        
        List<VisualAsset.ObjectType> allTypes = new ArrayList<>();
        for (VisualAsset.ObjectType type : VisualAsset.ObjectType.values()) {
            allTypes.add(type);
        }
        Collections.shuffle(allTypes);
        
        // Calculate dynamic difficulty
        int successfulRounds = score / 10;
        int numObjects = Math.min(6, 2 + (successfulRounds / 3)); // Starts at 2, increases every 3 rounds, max 6
        
        int targetIndex = random.nextInt(numObjects);
        
        for (int i = 0; i < numObjects; i++) {
            VisualAsset.ObjectType type = allTypes.get(i);
            
            // Dynamic Layout: evenly spaced horizontally
            int totalWidth = 800;
            int spacing = totalWidth / (numObjects + 1);
            int x = (spacing * (i + 1)) - 50; // -50 to center the 100px width asset
            int y = 250;
            
            String name = type.toString().toLowerCase();
            String soundId = "find_" + name;
            
            VisualAsset asset = new VisualAsset(x, y, 100, 100, type, soundId, name);
            assets.add(asset);
            
            if (i == targetIndex) {
                targetAsset = asset;
            }
        }
        
        // Play instruction for the new level
        if (targetAsset != null) {
            audioManager.playInstruction(targetAsset.getAssociatedSoundId());
        }
    }

    public void toggleSensoryMode() {
        isHighContrastMode = !isHighContrastMode;
        if (gamePanel != null) gamePanel.repaint();
    }

    public void update() {
        boolean needsRepaint = false;

        for (VisualAsset asset : assets) {
            asset.updateAnimation();
            needsRepaint = true;
        }

        if (isRewardAnimationPlaying) {
            for (int i = particles.size() - 1; i >= 0; i--) {
                Particle p = particles.get(i);
                p.update();
                if (p.life <= 0) {
                    particles.remove(i);
                }
            }
            if (particles.isEmpty()) {
                isRewardAnimationPlaying = false;
                generateNewLevel();
            }
            needsRepaint = true;
        }

        if (needsRepaint && gamePanel != null) {
            gamePanel.repaint();
        }
    }

    public void render(Graphics2D g2d) {
        // Draw premium background gradient
        if (isHighContrastMode) {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, 800, 600);
        } else {
            LinearGradientPaint bgGradient = new LinearGradientPaint(
                    0, 0, 0, 600,
                    new float[]{0.0f, 1.0f},
                    new Color[]{new Color(250, 252, 255), new Color(220, 235, 255)}
            );
            g2d.setPaint(bgGradient);
            g2d.fillRect(0, 0, 800, 600);
        }
        
        // Draw score as golden stars
        int visualTokens = Math.min(score / 10, 10);
        for (int i = 0; i < visualTokens; i++) {
            drawSmallStar(g2d, 30 + (i * 40), 40);
        }

        // Draw assets
        for (VisualAsset asset : assets) {
            asset.draw(g2d);
        }
        
        // Draw particles
        if (isRewardAnimationPlaying) {
            for (Particle p : particles) {
                p.draw(g2d);
            }
        }
    }
    
    private void drawSmallStar(Graphics2D g2d, int x, int y) {
        Path2D star = new Path2D.Double();
        int rOuter = 15;
        int rInner = 6;
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 5 * i - Math.PI / 2;
            int r = (i % 2 == 0) ? rOuter : rInner;
            double px = x + Math.cos(angle) * r;
            double py = y + Math.sin(angle) * r;
            if (i == 0) star.moveTo(px, py);
            else star.lineTo(px, py);
        }
        star.closePath();
        g2d.setColor(new Color(255, 215, 0));
        g2d.fill(star);
    }

    public void handleClick(int x, int y) {
        if (isRewardAnimationPlaying) return;
        
        for (VisualAsset asset : assets) {
            if (asset.contains(x, y)) {
                if (asset == targetAsset) {
                    score += 10;
                    triggerReward(x, y);
                } else {
                    audioManager.playSound("error");
                }
                break;
            }
        }
    }

    public void handleMouseMove(int x, int y) {
        if (isRewardAnimationPlaying) return;
        for (VisualAsset asset : assets) {
            asset.setHovered(asset.contains(x, y));
        }
    }
    
    public void handleDrag(int x, int y) {}

    public void triggerReward(int startX, int startY) {
        audioManager.playSound("success");
        isRewardAnimationPlaying = true;
        particles.clear();
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle(startX, startY));
        }
    }

    // Inner class for Confetti Particles
    private class Particle {
        double x, y;
        double vx, vy;
        Color color;
        int life;
        
        Particle(int x, int y) {
            this.x = x;
            this.y = y;
            double angle = random.nextDouble() * 2 * Math.PI;
            double speed = random.nextDouble() * 8 + 2;
            this.vx = Math.cos(angle) * speed;
            this.vy = Math.sin(angle) * speed;
            this.life = random.nextInt(30) + 30; // 30-60 frames
            
            Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};
            this.color = colors[random.nextInt(colors.length)];
        }
        
        void update() {
            x += vx;
            y += vy;
            vy += 0.3; // gravity
            life--;
        }
        
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(255, life * 8)));
            g2d.fillOval((int)x, (int)y, 8, 8);
        }
    }
}