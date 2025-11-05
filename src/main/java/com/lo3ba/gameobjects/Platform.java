package com.lo3ba.gameobjects;

import java.awt.Rectangle;

public class Platform {
    public Rectangle bounds;
    
    public Platform(int x, int y, int width, int height) {
        bounds = new Rectangle(x, y, width, height);
    }
}