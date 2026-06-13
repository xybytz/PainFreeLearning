package com.playfulminds;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Represents interactive everyday objects for Early Intervention therapy.
 * Rendered with modern, premium aesthetics (shadows, gradients, hover states).
 */
public class VisualAsset {
    
    public enum ObjectType {
        APPLE, TREE, STAR, CUP, BALL, CAR, SHOE, HOUSE, BIRD, FISH,
        CAT, FLOWER, SUN, MOON, BOAT, HAT, SOCK, CHAIR, CLOCK, UMBRELLA
    }

    private Rectangle boundingBox;
    private ObjectType objectType;
    private String associatedSoundId;
    private String name;
    
    private boolean isInteractive;
    private boolean isHovered;
    private double currentScale = 1.0;
    private double targetScale = 1.0;
    
    // Animation properties
    private double alpha = 1.0;
    private double targetAlpha = 1.0;
    private double scaleX = 1.0;
    private double targetScaleX = 1.0;
    private int shakeTicks = 0;
    private double shakeOffset = 0.0;

    public VisualAsset(int x, int y, int width, int height, ObjectType objectType, String soundId, String name) {
        this.boundingBox = new Rectangle(x, y, width, height);
        this.objectType = objectType;
        this.associatedSoundId = soundId;
        this.name = name;
        this.isInteractive = true;
    }

    public void updateAnimation() {
        // Smooth scaling for hover effect
        targetScale = isHovered ? 1.1 : 1.0;
        currentScale += (targetScale - currentScale) * 0.2;
        
        // Alpha fade
        alpha += (targetAlpha - alpha) * 0.1;
        
        // Flip scale
        scaleX += (targetScaleX - scaleX) * 0.15;
        
        // Shake
        if (shakeTicks > 0) {
            shakeTicks--;
            shakeOffset = Math.sin(shakeTicks * 0.8) * 10.0;
        } else {
            shakeOffset = 0;
        }
    }

    public void draw(Graphics2D g2d) {
        AffineTransform old = g2d.getTransform();
        Composite oldComposite = g2d.getComposite();
        
        // Apply Alpha
        if (alpha < 1.0) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) Math.max(0, Math.min(1, alpha))));
        }
        
        // Calculate center for scaling
        int cx = boundingBox.x + boundingBox.width / 2;
        int cy = boundingBox.y + boundingBox.height / 2;
        
        g2d.translate(cx + shakeOffset, cy);
        g2d.scale(currentScale * Math.max(0.01, scaleX), currentScale);
        g2d.translate(-cx, -cy);

        // Draw Drop Shadow
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillOval(boundingBox.x + 5, boundingBox.y + boundingBox.height - 10, boundingBox.width - 10, 20);

        // Draw Object
        switch (objectType) {
            case APPLE: drawApple(g2d); break;
            case TREE: drawTree(g2d); break;
            case STAR: drawStar(g2d); break;
            case CUP: drawCup(g2d); break;
            case BALL: drawBall(g2d); break;
            case CAR: drawCar(g2d); break;
            case SHOE: drawShoe(g2d); break;
            case HOUSE: drawHouse(g2d); break;
            case BIRD: drawBird(g2d); break;
            case FISH: drawFish(g2d); break;
            case CAT: drawCat(g2d); break;
            case FLOWER: drawFlower(g2d); break;
            case SUN: drawSun(g2d); break;
            case MOON: drawMoon(g2d); break;
            case BOAT: drawBoat(g2d); break;
            case HAT: drawHat(g2d); break;
            case SOCK: drawSock(g2d); break;
            case CHAIR: drawChair(g2d); break;
            case CLOCK: drawClock(g2d); break;
            case UMBRELLA: drawUmbrella(g2d); break;
        }

        g2d.setTransform(old);
        g2d.setComposite(oldComposite);
    }

    public void setTargetAlpha(double a) { this.targetAlpha = a; }
    public double getAlpha() { return alpha; }
    public void setTargetScaleX(double s) { this.targetScaleX = s; }
    public void setScaleX(double s) { this.scaleX = s; this.targetScaleX = s; }
    public double getScaleX() { return scaleX; }
    public void triggerShake() { this.shakeTicks = 30; }

    private void drawApple(Graphics2D g2d) {
        g2d.setColor(new Color(101, 67, 33));
        g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(boundingBox.x + boundingBox.width / 2, boundingBox.y + 20, boundingBox.x + boundingBox.width / 2 + 10, boundingBox.y + 5);
        g2d.setColor(new Color(50, 200, 50));
        g2d.fillOval(boundingBox.x + boundingBox.width / 2 + 5, boundingBox.y + 5, 20, 15);
        LinearGradientPaint p = new LinearGradientPaint(boundingBox.x, boundingBox.y, boundingBox.x, boundingBox.y + boundingBox.height, new float[]{0.0f, 1.0f}, new Color[]{new Color(255, 90, 90), new Color(200, 20, 20)});
        g2d.setPaint(p);
        Path2D appleShape = new Path2D.Double();
        appleShape.moveTo(boundingBox.x + boundingBox.width / 2, boundingBox.y + 25);
        appleShape.curveTo(boundingBox.x + boundingBox.width, boundingBox.y, boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height, boundingBox.x + boundingBox.width / 2, boundingBox.y + boundingBox.height);
        appleShape.curveTo(boundingBox.x, boundingBox.y + boundingBox.height, boundingBox.x, boundingBox.y, boundingBox.x + boundingBox.width / 2, boundingBox.y + 25);
        g2d.fill(appleShape);
    }

    private void drawTree(Graphics2D g2d) {
        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRect(boundingBox.x + boundingBox.width / 2 - 10, boundingBox.y + 50, 20, 50);
        LinearGradientPaint p = new LinearGradientPaint(boundingBox.x, boundingBox.y, boundingBox.x, boundingBox.y + 70, new float[]{0.0f, 1.0f}, new Color[]{new Color(80, 220, 80), new Color(34, 139, 34)});
        g2d.setPaint(p);
        g2d.fillOval(boundingBox.x + 10, boundingBox.y + 20, 50, 50);
        g2d.fillOval(boundingBox.x + 40, boundingBox.y + 20, 50, 50);
        g2d.fillOval(boundingBox.x + 25, boundingBox.y, 50, 50);
    }

    private void drawStar(Graphics2D g2d) {
        Path2D star = new Path2D.Double();
        int cx = boundingBox.x + boundingBox.width / 2;
        int cy = boundingBox.y + boundingBox.height / 2;
        int rOuter = 50;
        int rInner = 20;
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 5 * i - Math.PI / 2;
            int r = (i % 2 == 0) ? rOuter : rInner;
            double px = cx + Math.cos(angle) * r;
            double py = cy + Math.sin(angle) * r;
            if (i == 0) star.moveTo(px, py);
            else star.lineTo(px, py);
        }
        star.closePath();
        LinearGradientPaint p = new LinearGradientPaint(boundingBox.x, boundingBox.y, boundingBox.x, boundingBox.y + boundingBox.height, new float[]{0.0f, 1.0f}, new Color[]{new Color(255, 230, 80), new Color(255, 160, 0)});
        g2d.setPaint(p);
        g2d.fill(star);
    }

    private void drawCup(Graphics2D g2d) {
        g2d.setColor(new Color(100, 150, 255));
        g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawOval(boundingBox.x + 60, boundingBox.y + 30, 30, 40);
        LinearGradientPaint p = new LinearGradientPaint(boundingBox.x, boundingBox.y, boundingBox.x + 70, boundingBox.y, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{new Color(150, 200, 255), new Color(220, 240, 255), new Color(100, 150, 255)});
        g2d.setPaint(p);
        g2d.fillRoundRect(boundingBox.x + 10, boundingBox.y + 20, 60, 70, 15, 15);
    }

    private void drawBall(Graphics2D g2d) {
        Ellipse2D ball = new Ellipse2D.Double(boundingBox.x + 10, boundingBox.y + 10, 80, 80);
        LinearGradientPaint p = new LinearGradientPaint(boundingBox.x + 10, boundingBox.y + 10, boundingBox.x + 90, boundingBox.y + 90, new float[]{0.0f, 1.0f}, new Color[]{new Color(255, 100, 255), new Color(150, 0, 150)});
        g2d.setPaint(p);
        g2d.fill(ball);
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.setStroke(new BasicStroke(8));
        g2d.drawArc(boundingBox.x + 30, boundingBox.y + 10, 40, 80, 45, 90);
        g2d.drawArc(boundingBox.x - 10, boundingBox.y + 10, 40, 80, 315, 90);
    }

    private void drawCar(Graphics2D g2d) {
        LinearGradientPaint p = new LinearGradientPaint(boundingBox.x, boundingBox.y, boundingBox.x, boundingBox.y + boundingBox.height, new float[]{0.0f, 1.0f}, new Color[]{new Color(255, 80, 80), new Color(180, 20, 20)});
        g2d.setPaint(p);
        g2d.fillRoundRect(boundingBox.x + 10, boundingBox.y + 40, 80, 30, 10, 10);
        g2d.fillRoundRect(boundingBox.x + 25, boundingBox.y + 20, 50, 30, 15, 15);
        g2d.setColor(new Color(150, 200, 255));
        g2d.fillRoundRect(boundingBox.x + 30, boundingBox.y + 25, 15, 15, 5, 5);
        g2d.fillRoundRect(boundingBox.x + 55, boundingBox.y + 25, 15, 15, 5, 5);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillOval(boundingBox.x + 20, boundingBox.y + 60, 20, 20);
        g2d.fillOval(boundingBox.x + 60, boundingBox.y + 60, 20, 20);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(boundingBox.x + 25, boundingBox.y + 65, 10, 10);
        g2d.fillOval(boundingBox.x + 65, boundingBox.y + 65, 10, 10);
    }

    private void drawShoe(Graphics2D g2d) {
        LinearGradientPaint p = new LinearGradientPaint(boundingBox.x, boundingBox.y, boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height, new float[]{0.0f, 1.0f}, new Color[]{new Color(50, 100, 220), new Color(20, 40, 120)});
        g2d.setPaint(p);
        Path2D shoe = new Path2D.Double();
        shoe.moveTo(boundingBox.x + 20, boundingBox.y + 30);
        shoe.lineTo(boundingBox.x + 40, boundingBox.y + 70);
        shoe.lineTo(boundingBox.x + 80, boundingBox.y + 70);
        shoe.curveTo(boundingBox.x + 90, boundingBox.y + 70, boundingBox.x + 90, boundingBox.y + 50, boundingBox.x + 70, boundingBox.y + 50);
        shoe.lineTo(boundingBox.x + 50, boundingBox.y + 40);
        shoe.lineTo(boundingBox.x + 40, boundingBox.y + 30);
        shoe.closePath();
        g2d.fill(shoe);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(boundingBox.x + 45, boundingBox.y + 45, boundingBox.x + 55, boundingBox.y + 40);
        g2d.drawLine(boundingBox.x + 50, boundingBox.y + 52, boundingBox.x + 60, boundingBox.y + 47);
        g2d.drawLine(boundingBox.x + 55, boundingBox.y + 59, boundingBox.x + 65, boundingBox.y + 54);
    }

    private void drawHouse(Graphics2D g2d) {
        LinearGradientPaint p = new LinearGradientPaint(boundingBox.x, boundingBox.y + 40, boundingBox.x, boundingBox.y + 90, new float[]{0.0f, 1.0f}, new Color[]{new Color(240, 230, 200), new Color(200, 190, 160)});
        g2d.setPaint(p);
        g2d.fillRect(boundingBox.x + 20, boundingBox.y + 40, 60, 50);
        g2d.setColor(new Color(200, 50, 50));
        Path2D roof = new Path2D.Double();
        roof.moveTo(boundingBox.x + 10, boundingBox.y + 40);
        roof.lineTo(boundingBox.x + 50, boundingBox.y + 10);
        roof.lineTo(boundingBox.x + 90, boundingBox.y + 40);
        roof.closePath();
        g2d.fill(roof);
        g2d.setColor(new Color(100, 50, 20));
        g2d.fillRect(boundingBox.x + 40, boundingBox.y + 60, 20, 30);
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(boundingBox.x + 53, boundingBox.y + 75, 5, 5);
        g2d.setColor(new Color(150, 200, 255));
        g2d.fillRect(boundingBox.x + 25, boundingBox.y + 50, 15, 15);
    }

    private void drawBird(Graphics2D g2d) {
        g2d.setColor(new Color(255, 200, 50));
        g2d.fillOval(boundingBox.x + 20, boundingBox.y + 30, 50, 40);
        g2d.fillOval(boundingBox.x + 50, boundingBox.y + 20, 30, 30);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(boundingBox.x + 65, boundingBox.y + 28, 5, 5);
        g2d.setColor(Color.ORANGE);
        Path2D beak = new Path2D.Double();
        beak.moveTo(boundingBox.x + 75, boundingBox.y + 30);
        beak.lineTo(boundingBox.x + 90, boundingBox.y + 35);
        beak.lineTo(boundingBox.x + 75, boundingBox.y + 40);
        beak.closePath();
        g2d.fill(beak);
        g2d.setColor(new Color(220, 170, 30));
        Path2D wing = new Path2D.Double();
        wing.moveTo(boundingBox.x + 30, boundingBox.y + 40);
        wing.curveTo(boundingBox.x + 40, boundingBox.y + 60, boundingBox.x + 60, boundingBox.y + 60, boundingBox.x + 50, boundingBox.y + 40);
        wing.closePath();
        g2d.fill(wing);
    }

    private void drawFish(Graphics2D g2d) {
        LinearGradientPaint p = new LinearGradientPaint(boundingBox.x, boundingBox.y, boundingBox.x, boundingBox.y + boundingBox.height, new float[]{0.0f, 1.0f}, new Color[]{new Color(255, 150, 50), new Color(255, 80, 0)});
        g2d.setPaint(p);
        Path2D body = new Path2D.Double();
        body.moveTo(boundingBox.x + 20, boundingBox.y + 50);
        body.curveTo(boundingBox.x + 40, boundingBox.y + 20, boundingBox.x + 80, boundingBox.y + 20, boundingBox.x + 90, boundingBox.y + 50);
        body.curveTo(boundingBox.x + 80, boundingBox.y + 80, boundingBox.x + 40, boundingBox.y + 80, boundingBox.x + 20, boundingBox.y + 50);
        body.closePath();
        g2d.fill(body);
        g2d.setColor(new Color(255, 100, 0));
        Path2D tail = new Path2D.Double();
        tail.moveTo(boundingBox.x + 25, boundingBox.y + 50);
        tail.lineTo(boundingBox.x + 5, boundingBox.y + 30);
        tail.lineTo(boundingBox.x + 5, boundingBox.y + 70);
        tail.closePath();
        g2d.fill(tail);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(boundingBox.x + 70, boundingBox.y + 40, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(boundingBox.x + 75, boundingBox.y + 43, 4, 4);
    }

    private void drawCat(Graphics2D g2d) {
        g2d.setColor(new Color(120, 120, 120)); // Gray Cat
        g2d.fillOval(boundingBox.x + 25, boundingBox.y + 30, 50, 45); // face
        Path2D ear1 = new Path2D.Double();
        ear1.moveTo(boundingBox.x + 25, boundingBox.y + 45);
        ear1.lineTo(boundingBox.x + 35, boundingBox.y + 15);
        ear1.lineTo(boundingBox.x + 45, boundingBox.y + 35);
        g2d.fill(ear1);
        Path2D ear2 = new Path2D.Double();
        ear2.moveTo(boundingBox.x + 75, boundingBox.y + 45);
        ear2.lineTo(boundingBox.x + 65, boundingBox.y + 15);
        ear2.lineTo(boundingBox.x + 55, boundingBox.y + 35);
        g2d.fill(ear2);
        
        g2d.setColor(Color.WHITE);
        g2d.fillOval(boundingBox.x + 35, boundingBox.y + 45, 10, 10); // eye 1
        g2d.fillOval(boundingBox.x + 55, boundingBox.y + 45, 10, 10); // eye 2
        g2d.setColor(Color.BLACK);
        g2d.fillOval(boundingBox.x + 38, boundingBox.y + 48, 4, 4);
        g2d.fillOval(boundingBox.x + 58, boundingBox.y + 48, 4, 4);
        g2d.setColor(Color.PINK);
        g2d.fillOval(boundingBox.x + 46, boundingBox.y + 55, 8, 6); // nose
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(boundingBox.x + 20, boundingBox.y + 55, boundingBox.x + 30, boundingBox.y + 60);
        g2d.drawLine(boundingBox.x + 80, boundingBox.y + 55, boundingBox.x + 70, boundingBox.y + 60);
    }

    private void drawFlower(Graphics2D g2d) {
        g2d.setColor(new Color(50, 200, 50));
        g2d.fillRect(boundingBox.x + 47, boundingBox.y + 50, 6, 40); // stem
        
        g2d.setColor(new Color(255, 100, 150));
        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3;
            int px = boundingBox.x + 35 + (int) (Math.cos(angle) * 15);
            int py = boundingBox.y + 25 + (int) (Math.sin(angle) * 15);
            g2d.fillOval(px, py, 30, 30);
        }
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(boundingBox.x + 40, boundingBox.y + 30, 20, 20); // center
    }

    private void drawSun(Graphics2D g2d) {
        g2d.setColor(new Color(255, 150, 0));
        g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            int cx = boundingBox.x + 50;
            int cy = boundingBox.y + 50;
            g2d.drawLine(cx + (int)(Math.cos(angle)*20), cy + (int)(Math.sin(angle)*20), 
                         cx + (int)(Math.cos(angle)*40), cy + (int)(Math.sin(angle)*40));
        }
        g2d.setColor(new Color(255, 220, 50));
        g2d.fillOval(boundingBox.x + 25, boundingBox.y + 25, 50, 50);
    }

    private void drawMoon(Graphics2D g2d) {
        Path2D moon = new Path2D.Double();
        moon.moveTo(boundingBox.x + 60, boundingBox.y + 10);
        moon.curveTo(boundingBox.x + 10, boundingBox.y + 10, boundingBox.x + 10, boundingBox.y + 90, boundingBox.x + 60, boundingBox.y + 90);
        moon.curveTo(boundingBox.x + 30, boundingBox.y + 70, boundingBox.x + 30, boundingBox.y + 30, boundingBox.x + 60, boundingBox.y + 10);
        
        LinearGradientPaint p = new LinearGradientPaint(boundingBox.x, boundingBox.y, boundingBox.x + 100, boundingBox.y + 100, new float[]{0.0f, 1.0f}, new Color[]{new Color(255, 255, 150), new Color(200, 200, 50)});
        g2d.setPaint(p);
        g2d.fill(moon);
    }

    private void drawBoat(Graphics2D g2d) {
        // Hull
        g2d.setColor(new Color(139, 69, 19));
        Path2D hull = new Path2D.Double();
        hull.moveTo(boundingBox.x + 10, boundingBox.y + 60);
        hull.lineTo(boundingBox.x + 90, boundingBox.y + 60);
        hull.lineTo(boundingBox.x + 70, boundingBox.y + 80);
        hull.lineTo(boundingBox.x + 30, boundingBox.y + 80);
        hull.closePath();
        g2d.fill(hull);
        // Mast
        g2d.setColor(Color.BLACK);
        g2d.fillRect(boundingBox.x + 48, boundingBox.y + 20, 4, 40);
        // Sail
        g2d.setColor(Color.WHITE);
        Path2D sail = new Path2D.Double();
        sail.moveTo(boundingBox.x + 50, boundingBox.y + 20);
        sail.lineTo(boundingBox.x + 90, boundingBox.y + 55);
        sail.lineTo(boundingBox.x + 50, boundingBox.y + 55);
        sail.closePath();
        g2d.fill(sail);
    }

    private void drawHat(Graphics2D g2d) {
        g2d.setColor(new Color(50, 150, 200));
        g2d.fillArc(boundingBox.x + 20, boundingBox.y + 30, 60, 60, 0, 180);
        g2d.setColor(new Color(30, 100, 150));
        g2d.fillRoundRect(boundingBox.x + 10, boundingBox.y + 60, 80, 10, 10, 10); // brim
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(boundingBox.x + 45, boundingBox.y + 25, 10, 10); // button on top
    }

    private void drawSock(Graphics2D g2d) {
        g2d.setColor(new Color(255, 100, 100));
        g2d.fillRoundRect(boundingBox.x + 30, boundingBox.y + 10, 30, 60, 10, 10); // leg
        g2d.fillRoundRect(boundingBox.x + 30, boundingBox.y + 50, 50, 30, 15, 15); // foot
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(boundingBox.x + 30, boundingBox.y + 10, 30, 15); // cuff
        g2d.fillArc(boundingBox.x + 65, boundingBox.y + 50, 15, 30, 270, 180); // toe
        g2d.fillArc(boundingBox.x + 25, boundingBox.y + 50, 15, 30, 90, 180); // heel
    }

    private void drawChair(Graphics2D g2d) {
        g2d.setColor(new Color(160, 82, 45));
        g2d.fillRoundRect(boundingBox.x + 25, boundingBox.y + 55, 50, 10, 5, 5); // seat
        g2d.fillRect(boundingBox.x + 30, boundingBox.y + 15, 40, 40); // backrest
        
        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRect(boundingBox.x + 30, boundingBox.y + 65, 8, 25); // leg L
        g2d.fillRect(boundingBox.x + 62, boundingBox.y + 65, 8, 25); // leg R
    }

    private void drawClock(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval(boundingBox.x + 10, boundingBox.y + 10, 80, 80);
        g2d.setColor(new Color(40, 40, 40));
        g2d.setStroke(new BasicStroke(6));
        g2d.drawOval(boundingBox.x + 10, boundingBox.y + 10, 80, 80);
        g2d.fillOval(boundingBox.x + 46, boundingBox.y + 46, 8, 8); // center
        
        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(boundingBox.x + 50, boundingBox.y + 50, boundingBox.x + 50, boundingBox.y + 25); // hour
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(boundingBox.x + 50, boundingBox.y + 50, boundingBox.x + 70, boundingBox.y + 50); // minute
    }

    private void drawUmbrella(Graphics2D g2d) {
        g2d.setColor(new Color(200, 50, 200));
        g2d.fillArc(boundingBox.x + 10, boundingBox.y + 20, 80, 60, 0, 180);
        
        g2d.setColor(new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(boundingBox.x + 50, boundingBox.y + 50, boundingBox.x + 50, boundingBox.y + 80); // pole
        g2d.drawArc(boundingBox.x + 40, boundingBox.y + 70, 10, 10, 180, 180); // handle J
    }

    public boolean contains(int x, int y) {
        if (!isInteractive) return false;
        return boundingBox.contains(x, y);
    }

    public void setHovered(boolean hovered) {
        this.isHovered = hovered;
    }

    public String getAssociatedSoundId() { return associatedSoundId; }
    public String getName() { return name; }
    public ObjectType getObjectType() { return objectType; }
}