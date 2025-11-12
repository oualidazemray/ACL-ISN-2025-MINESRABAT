package com.lo3ba.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Door {
    private Rectangle bounds;
    private BufferedImage doorImage;
    
    public Door(int x, int y, int width, int height) {
        this.bounds = new Rectangle(x, y, width, height);
    }
    
    public void setDoorImage(BufferedImage image) {
        this.doorImage = image;
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public int getX() { return bounds.x; }
    public int getY() { return bounds.y; }
    public int getWidth() { return bounds.width; }
    public int getHeight() { return bounds.height; }
    
    public void render(Graphics2D g) {
        if (doorImage != null) {
            g.drawImage(doorImage, bounds.x, bounds.y, bounds.width, bounds.height, null);
        } else {
            // Fallback: draw green rectangle
            g.setColor(Color.GREEN);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }
}