package com.lo3ba.gameobjects;

import com.lo3ba.util.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall {
    private int x, y;
    private int width, height;
    private boolean destroyed = false;
    private Rectangle bounds;
    private BufferedImage sprite;

    public BreakableWall(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 64;
        this.height = 64;
        this.bounds = new Rectangle(x, y, width, height);
        loadAssets();
    }
    
    // Constructor with custom size
    public BreakableWall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
        loadAssets();
    }

    private void loadAssets() {
        sprite = ResourceManager.loadTexture("breakable_wall.png");
    }

    public void destroy() {
        destroyed = true;
        // Sound will be handled by Level or Explosion
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Rectangle getBounds() {
        return bounds;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void render(Graphics2D g) {
        if (!destroyed) {
            if (sprite != null) {
                // Tile the sprite if wall is larger
                int tilesX = (int) Math.ceil((double) width / sprite.getWidth());
                int tilesY = (int) Math.ceil((double) height / sprite.getHeight());
                
                for (int tx = 0; tx < tilesX; tx++) {
                    for (int ty = 0; ty < tilesY; ty++) {
                        int drawX = x + (tx * sprite.getWidth());
                        int drawY = y + (ty * sprite.getHeight());
                        
                        int drawW = Math.min(sprite.getWidth(), x + width - drawX);
                        int drawH = Math.min(sprite.getHeight(), y + height - drawY);
                        
                        g.drawImage(sprite, drawX, drawY, drawX + drawW, drawY + drawH, 0, 0, drawW, drawH, null);
                    }
                }
            } else {
                // Fallback
                g.setColor(new Color(139, 69, 19)); // Brown
                g.fillRect(x, y, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, width, height);
                // Draw cracks
                g.drawLine(x, y, x + width, y + height);
                g.drawLine(x + width, y, x, y + height);
            }
        }
    }
}
