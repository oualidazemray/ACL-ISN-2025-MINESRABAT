package com.lo3ba.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Checkpoint flag that saves player progress in a level
 */
public class Checkpoint {
    private Rectangle bounds;
    private boolean activated;
    private int animationFrame;
    private int spawnX;
    private int spawnY;
    
    // Animation
    private static final int FLAG_WIDTH = 40;
    private static final int FLAG_HEIGHT = 60;
    private static final int ANIMATION_SPEED = 5;
    
    public Checkpoint(int x, int y, int spawnX, int spawnY) {
        this.bounds = new Rectangle(x, y, FLAG_WIDTH, FLAG_HEIGHT);
        this.activated = false;
        this.animationFrame = 0;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }
    
    public void update() {
        if (activated) {
            animationFrame++;
        }
    }
    
    public void activate() {
        if (!activated) {
            activated = true;
            animationFrame = 0;
        }
    }
    
    public void render(Graphics2D g) {
        // Pole
        g.setColor(new Color(100, 70, 40));
        g.fillRect(bounds.x + FLAG_WIDTH/2 - 2, bounds.y, 4, FLAG_HEIGHT);
        
        // Flag
        Color flagColor;
        if (activated) {
            // Animated green when activated
            int pulse = (int)(Math.sin(animationFrame / 10.0) * 20 + 235);
            flagColor = new Color(100, pulse, 100);
        } else {
            // Gray when inactive
            flagColor = new Color(150, 150, 150);
        }
        
        g.setColor(flagColor);
        
        // Flag shape (waving effect if activated)
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        
        xPoints[0] = bounds.x + FLAG_WIDTH/2;
        yPoints[0] = bounds.y + 5;
        
        xPoints[1] = bounds.x + FLAG_WIDTH - 5;
        yPoints[1] = bounds.y + 10;
        
        if (activated) {
            // Wave effect
            int wave = (int)(Math.sin(animationFrame / 5.0) * 3);
            xPoints[2] = bounds.x + FLAG_WIDTH - 5 + wave;
        } else {
            xPoints[2] = bounds.x + FLAG_WIDTH - 5;
        }
        yPoints[2] = bounds.y + 25;
        
        xPoints[3] = bounds.x + FLAG_WIDTH/2;
        yPoints[3] = bounds.y + 20;
        
        g.fillPolygon(xPoints, yPoints, 4);
        
        // Flag outline
        g.setColor(flagColor.darker());
        g.setStroke(new BasicStroke(2));
        g.drawPolygon(xPoints, yPoints, 4);
        
        // Sparkle effect when activated
        if (activated && animationFrame % 20 < 10) {
            g.setColor(new Color(255, 255, 255, 200));
            g.fillOval(bounds.x + FLAG_WIDTH - 10, bounds.y + 8, 6, 6);
        }
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public boolean isActivated() {
        return activated;
    }
    
    public int getSpawnX() {
        return spawnX;
    }
    
    public int getSpawnY() {
        return spawnY;
    }
    
    public void reset() {
        activated = false;
        animationFrame = 0;
    }
}
