package com.lo3ba.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Spike {
    private Rectangle bounds;
    private BufferedImage spikeImage;
    // Reused hitbox rectangle to avoid per-frame allocations
    private final Rectangle hitbox = new Rectangle();

    public Spike(int x, int y, int width, int height) {
        this.bounds = new Rectangle(x, y, width, height);
    }
    
    public void setSpikeImage(BufferedImage image) {
        this.spikeImage = image;
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public Rectangle getHitbox() {
        // Slightly reduced hitbox for more forgiving collision; reuse cached rectangle
        hitbox.setBounds(bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4);
        return hitbox;
    }
    
    public int getX() { return bounds.x; }
    public int getY() { return bounds.y; }
    public int getWidth() { return bounds.width; }
    public int getHeight() { return bounds.height; }
    
    public void render(Graphics2D g) {
        if (spikeImage != null) {
            g.drawImage(spikeImage, bounds.x, bounds.y, bounds.width, bounds.height, null);
        } else {
            // Fallback: draw red triangle
            g.setColor(Color.RED);
            int[] xPoints = {bounds.x, bounds.x + bounds.width/2, bounds.x + bounds.width};
            int[] yPoints = {bounds.y + bounds.height, bounds.y, bounds.y + bounds.height};
            g.fillPolygon(xPoints, yPoints, 3);
        }
    }
}