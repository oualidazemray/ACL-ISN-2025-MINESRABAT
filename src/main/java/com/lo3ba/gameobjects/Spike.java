package com.lo3ba.gameobjects;

import java.awt.Rectangle;

public class Spike {
    public Rectangle bounds;
    
    public Spike(int x, int y, int width, int height) {
        bounds = new Rectangle(x, y, width, height);
    }
}