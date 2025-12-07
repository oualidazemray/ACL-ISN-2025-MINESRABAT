package com.lo3ba.gameobjects;

import com.lo3ba.util.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bomb {
    private int x, y;
    private int width, height;
    private boolean collected = false;
    private Rectangle bounds;
    private BufferedImage sprite;

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 32;
        this.bounds = new Rectangle(x, y, width, height);
        loadAssets();
    }

    private void loadAssets() {
        sprite = ResourceManager.loadTexture("bomb.png");
    }

    public void collect() {
        collected = true;
        // Play sound if available
        try {
            javax.sound.sampled.Clip clip = ResourceManager.loadSound("bomb_collect.wav");
            if (clip != null) {
                clip.setFramePosition(0);
                clip.start();
            }
        } catch (Exception e) {
            // Ignore if sound not found
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void render(Graphics2D g) {
        if (!collected) {
            if (sprite != null) {
                g.drawImage(sprite, x, y, width, height, null);
            } else {
                // Fallback
                g.setColor(Color.BLACK);
                g.fillOval(x, y, width, height);
                g.setColor(Color.RED);
                g.fillOval(x + 8, y + 8, 16, 16); // Fuse
            }
        }
    }
}
