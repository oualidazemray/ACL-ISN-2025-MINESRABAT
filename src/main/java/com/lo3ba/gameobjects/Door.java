package com.lo3ba.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Door {
    private Rectangle bounds;

    private BufferedImage closedImage;
    private BufferedImage openImage;

    private boolean open = false;

    public Door(int x, int y, int width, int height) {
        this.bounds = new Rectangle(x, y, width, height);
    }

    // Assign images from Level
    public void setClosedImage(BufferedImage img) {
        this.closedImage = img;
    }

    public void setOpenImage(BufferedImage img) {
        this.openImage = img;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        this.open = true;
    }

    public void render(Graphics2D g) {
        BufferedImage img = open ? openImage : closedImage;

        if (img != null) {
            g.drawImage(img, bounds.x, bounds.y, bounds.width, bounds.height, null);
        } else {
            g.setColor(open ? Color.GREEN : Color.RED);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }
}
