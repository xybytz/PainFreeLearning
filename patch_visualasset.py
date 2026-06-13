import sys

with open('src/com/playfulminds/VisualAsset.java', 'r') as f:
    content = f.read()

old_header = """import java.awt.BasicStroke;
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
        g2d.fillOval(boundingBox.x + 5, boundingBox.y + boundingBox.height - 10, boundingBox.width - 10, 20);"""

new_header = """import java.awt.AlphaComposite;
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
        g2d.fillOval(boundingBox.x + 5, boundingBox.y + boundingBox.height - 10, boundingBox.width - 10, 20);"""

content = content.replace(old_header, new_header)

old_footer = """        g2d.setTransform(old);
    }"""

new_footer = """        g2d.setTransform(old);
        g2d.setComposite(oldComposite);
    }

    public void setTargetAlpha(double a) { this.targetAlpha = a; }
    public double getAlpha() { return alpha; }
    public void setTargetScaleX(double s) { this.targetScaleX = s; }
    public void setScaleX(double s) { this.scaleX = s; this.targetScaleX = s; }
    public double getScaleX() { return scaleX; }
    public void triggerShake() { this.shakeTicks = 30; }"""

content = content.replace(old_footer, new_footer)

with open('src/com/playfulminds/VisualAsset.java', 'w') as f:
    f.write(content)

print("Updated VisualAsset.java")
