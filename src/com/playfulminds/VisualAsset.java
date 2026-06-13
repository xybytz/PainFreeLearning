package com.playfulminds;

import java.awt.BasicStroke;
import java.awt.Color;
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
        APPLE, TREE, STAR, CUP, BALL, CAR, SHOE, HOUSE, BIRD, FISH
    }

    private Rectangle boundingBox;
    private ObjectType objectType;
    private String associatedSoundId;
    private String name;
    
    private boolean isInteractive;
    private boolean isHovered;
    private double currentScale = 1.0;
    private double targetScale = 1.0;

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
    }

    public void draw(Graphics2D g2d) {
        AffineTransform old = g2d.getTransform();
        
        // Calculate center for scaling
        int cx = boundingBox.x + boundingBox.width / 2;
        int cy = boundingBox.y + boundingBox.height / 2;
        
        g2d.translate(cx, cy);
        g2d.scale(currentScale, currentScale);
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
        }

        g2d.setTransform(old);
    }

    private void drawApple(Graphics2D g2d) {
        // Stem
        g2d.setColor(new Color(101, 67, 33));
        g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(boundingBox.x + boundingBox.width / 2, boundingBox.y + 20, boundingBox.x + boundingBox.width / 2 + 10, boundingBox.y + 5);
        
        // Leaf
        g2d.setColor(new Color(50, 200, 50));
        g2d.fillOval(boundingBox.x + boundingBox.width / 2 + 5, boundingBox.y + 5, 20, 15);

        // Body (Gradient)
        LinearGradientPaint p = new LinearGradientPaint(
                boundingBox.x, boundingBox.y, boundingBox.x, boundingBox.y + boundingBox.height,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(255, 90, 90), new Color(200, 20, 20)});
        g2d.setPaint(p);
        
        Path2D appleShape = new Path2D.Double();
        appleShape.moveTo(boundingBox.x + boundingBox.width / 2, boundingBox.y + 25);
        appleShape.curveTo(boundingBox.x + boundingBox.width, boundingBox.y, 
                           boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height, 
                           boundingBox.x + boundingBox.width / 2, boundingBox.y + boundingBox.height);
        appleShape.curveTo(boundingBox.x, boundingBox.y + boundingBox.height, 
                           boundingBox.x, boundingBox.y, 
                           boundingBox.x + boundingBox.width / 2, boundingBox.y + 25);
        g2d.fill(appleShape);
    }

    private void drawTree(Graphics2D g2d) {
        // Trunk
        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRect(boundingBox.x + boundingBox.width / 2 - 10, boundingBox.y + 50, 20, 50);

        // Leaves (Three overlapping circles)
        LinearGradientPaint p = new LinearGradientPaint(
                boundingBox.x, boundingBox.y, boundingBox.x, boundingBox.y + 70,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(80, 220, 80), new Color(34, 139, 34)});
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

        LinearGradientPaint p = new LinearGradientPaint(
                boundingBox.x, boundingBox.y, boundingBox.x, boundingBox.y + boundingBox.height,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(255, 230, 80), new Color(255, 160, 0)});
        g2d.setPaint(p);
        g2d.fill(star);
    }

    private void drawCup(Graphics2D g2d) {
        // Handle
        g2d.setColor(new Color(100, 150, 255));
        g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawOval(boundingBox.x + 60, boundingBox.y + 30, 30, 40);

        // Body
        LinearGradientPaint p = new LinearGradientPaint(
                boundingBox.x, boundingBox.y, boundingBox.x + 70, boundingBox.y,
                new float[]{0.0f, 0.5f, 1.0f},
                new Color[]{new Color(150, 200, 255), new Color(220, 240, 255), new Color(100, 150, 255)});
        g2d.setPaint(p);
        g2d.fillRoundRect(boundingBox.x + 10, boundingBox.y + 20, 60, 70, 15, 15);
    }

    private void drawBall(Graphics2D g2d) {
        Ellipse2D ball = new Ellipse2D.Double(boundingBox.x + 10, boundingBox.y + 10, 80, 80);
        
        LinearGradientPaint p = new LinearGradientPaint(
                boundingBox.x + 10, boundingBox.y + 10, boundingBox.x + 90, boundingBox.y + 90,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(255, 100, 255), new Color(150, 0, 150)});
        g2d.setPaint(p);
        g2d.fill(ball);

        // Stripes
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.setStroke(new BasicStroke(8));
        g2d.drawArc(boundingBox.x + 30, boundingBox.y + 10, 40, 80, 45, 90);
        g2d.drawArc(boundingBox.x - 10, boundingBox.y + 10, 40, 80, 315, 90);
    }

    private void drawCar(Graphics2D g2d) {
        // Body
        LinearGradientPaint p = new LinearGradientPaint(
                boundingBox.x, boundingBox.y, boundingBox.x, boundingBox.y + boundingBox.height,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(255, 80, 80), new Color(180, 20, 20)});
        g2d.setPaint(p);
        g2d.fillRoundRect(boundingBox.x + 10, boundingBox.y + 40, 80, 30, 10, 10);
        g2d.fillRoundRect(boundingBox.x + 25, boundingBox.y + 20, 50, 30, 15, 15);
        
        // Windows
        g2d.setColor(new Color(150, 200, 255));
        g2d.fillRoundRect(boundingBox.x + 30, boundingBox.y + 25, 15, 15, 5, 5);
        g2d.fillRoundRect(boundingBox.x + 55, boundingBox.y + 25, 15, 15, 5, 5);
        
        // Wheels
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillOval(boundingBox.x + 20, boundingBox.y + 60, 20, 20);
        g2d.fillOval(boundingBox.x + 60, boundingBox.y + 60, 20, 20);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(boundingBox.x + 25, boundingBox.y + 65, 10, 10);
        g2d.fillOval(boundingBox.x + 65, boundingBox.y + 65, 10, 10);
    }

    private void drawShoe(Graphics2D g2d) {
        LinearGradientPaint p = new LinearGradientPaint(
                boundingBox.x, boundingBox.y, boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(50, 100, 220), new Color(20, 40, 120)});
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
        
        // Laces
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(boundingBox.x + 45, boundingBox.y + 45, boundingBox.x + 55, boundingBox.y + 40);
        g2d.drawLine(boundingBox.x + 50, boundingBox.y + 52, boundingBox.x + 60, boundingBox.y + 47);
        g2d.drawLine(boundingBox.x + 55, boundingBox.y + 59, boundingBox.x + 65, boundingBox.y + 54);
    }

    private void drawHouse(Graphics2D g2d) {
        // Base
        LinearGradientPaint p = new LinearGradientPaint(
                boundingBox.x, boundingBox.y + 40, boundingBox.x, boundingBox.y + 90,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(240, 230, 200), new Color(200, 190, 160)});
        g2d.setPaint(p);
        g2d.fillRect(boundingBox.x + 20, boundingBox.y + 40, 60, 50);
        
        // Roof
        g2d.setColor(new Color(200, 50, 50));
        Path2D roof = new Path2D.Double();
        roof.moveTo(boundingBox.x + 10, boundingBox.y + 40);
        roof.lineTo(boundingBox.x + 50, boundingBox.y + 10);
        roof.lineTo(boundingBox.x + 90, boundingBox.y + 40);
        roof.closePath();
        g2d.fill(roof);
        
        // Door
        g2d.setColor(new Color(100, 50, 20));
        g2d.fillRect(boundingBox.x + 40, boundingBox.y + 60, 20, 30);
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(boundingBox.x + 53, boundingBox.y + 75, 5, 5); // doorknob
        
        // Window
        g2d.setColor(new Color(150, 200, 255));
        g2d.fillRect(boundingBox.x + 25, boundingBox.y + 50, 15, 15);
    }

    private void drawBird(Graphics2D g2d) {
        // Body
        g2d.setColor(new Color(255, 200, 50));
        g2d.fillOval(boundingBox.x + 20, boundingBox.y + 30, 50, 40);
        
        // Head
        g2d.fillOval(boundingBox.x + 50, boundingBox.y + 20, 30, 30);
        
        // Eye
        g2d.setColor(Color.BLACK);
        g2d.fillOval(boundingBox.x + 65, boundingBox.y + 28, 5, 5);
        
        // Beak
        g2d.setColor(Color.ORANGE);
        Path2D beak = new Path2D.Double();
        beak.moveTo(boundingBox.x + 75, boundingBox.y + 30);
        beak.lineTo(boundingBox.x + 90, boundingBox.y + 35);
        beak.lineTo(boundingBox.x + 75, boundingBox.y + 40);
        beak.closePath();
        g2d.fill(beak);
        
        // Wing
        g2d.setColor(new Color(220, 170, 30));
        Path2D wing = new Path2D.Double();
        wing.moveTo(boundingBox.x + 30, boundingBox.y + 40);
        wing.curveTo(boundingBox.x + 40, boundingBox.y + 60, boundingBox.x + 60, boundingBox.y + 60, boundingBox.x + 50, boundingBox.y + 40);
        wing.closePath();
        g2d.fill(wing);
    }

    private void drawFish(Graphics2D g2d) {
        // Body
        LinearGradientPaint p = new LinearGradientPaint(
                boundingBox.x, boundingBox.y, boundingBox.x, boundingBox.y + boundingBox.height,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(255, 150, 50), new Color(255, 80, 0)});
        g2d.setPaint(p);
        
        Path2D body = new Path2D.Double();
        body.moveTo(boundingBox.x + 20, boundingBox.y + 50);
        body.curveTo(boundingBox.x + 40, boundingBox.y + 20, boundingBox.x + 80, boundingBox.y + 20, boundingBox.x + 90, boundingBox.y + 50);
        body.curveTo(boundingBox.x + 80, boundingBox.y + 80, boundingBox.x + 40, boundingBox.y + 80, boundingBox.x + 20, boundingBox.y + 50);
        body.closePath();
        g2d.fill(body);
        
        // Tail
        g2d.setColor(new Color(255, 100, 0));
        Path2D tail = new Path2D.Double();
        tail.moveTo(boundingBox.x + 25, boundingBox.y + 50);
        tail.lineTo(boundingBox.x + 5, boundingBox.y + 30);
        tail.lineTo(boundingBox.x + 5, boundingBox.y + 70);
        tail.closePath();
        g2d.fill(tail);
        
        // Eye
        g2d.setColor(Color.WHITE);
        g2d.fillOval(boundingBox.x + 70, boundingBox.y + 40, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(boundingBox.x + 75, boundingBox.y + 43, 4, 4);
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