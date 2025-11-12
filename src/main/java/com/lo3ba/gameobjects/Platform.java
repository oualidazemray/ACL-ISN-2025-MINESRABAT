package com.lo3ba.gameobjects;

import java.awt.*;

public class Platform {
    private Rectangle bounds;
    
    public Platform(int x, int y, int width, int height) {
        this.bounds = new Rectangle(x, y, width, height);
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public int getX() { return bounds.x; }
    public int getY() { return bounds.y; }
    public int getWidth() { return bounds.width; }
    public int getHeight() { return bounds.height; }
    
    public void render(Graphics2D g) {
        g.setColor(new Color(100, 100, 100));
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}