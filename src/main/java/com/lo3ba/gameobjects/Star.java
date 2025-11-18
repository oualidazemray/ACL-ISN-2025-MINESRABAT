package com.lo3ba.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Star {
    private int x, y, width, height;
    private BufferedImage starImage;
    private boolean collected = false;

    public Star(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setStarImage(BufferedImage starImage) {
        this.starImage = starImage;
    }

    public void render(Graphics2D g) {
        if (!collected && starImage != null) {
            g.drawImage(starImage, x, y, width, height, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }

    public void reset() {
        collected = false;
    }
}
