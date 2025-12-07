package com.lo3ba.gameobjects;

import com.lo3ba.util.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion {
    private int x, y;
    private int frame = 0;
    private int maxFrames = 8;
    private int frameDelay = 3; // Updates per frame
    private int delayCounter = 0;
    private boolean finished = false;
    private BufferedImage spriteSheet;
    private int frameWidth = 32;
    private int frameHeight = 32;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
        loadAssets();
    }

    private void loadAssets() {
        spriteSheet = ResourceManager.loadTexture("explosion.png");
        // Play sound
        try {
            javax.sound.sampled.Clip clip = ResourceManager.loadSound("explosion.wav");
            if (clip != null) {
                clip.setFramePosition(0);
                clip.start();
            }
        } catch (Exception e) {
            // Ignore
        }
    }

    public void update() {
        delayCounter++;
        if (delayCounter >= frameDelay) {
            delayCounter = 0;
            frame++;
            if (frame >= maxFrames) {
                finished = true;
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void render(Graphics2D g) {
        if (!finished) {
            if (spriteSheet != null) {
                // Assuming sprite sheet is horizontal strip
                int sx = frame * frameWidth;
                g.drawImage(spriteSheet, x, y, x + frameWidth * 2, y + frameHeight * 2, // Scale up 2x
                           sx, 0, sx + frameWidth, frameHeight, null);
            } else {
                // Fallback animation
                g.setColor(new Color(255, 100, 0, 255 - (frame * 30)));
                int size = 20 + (frame * 10);
                g.fillOval(x + 32 - size/2, y + 32 - size/2, size, size);
            }
        }
    }
}
