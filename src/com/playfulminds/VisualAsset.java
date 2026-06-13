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
        APPLE, TREE, STAR, CUP, BALL
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