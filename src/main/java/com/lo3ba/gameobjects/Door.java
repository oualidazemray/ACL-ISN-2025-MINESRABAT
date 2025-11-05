package com.lo3ba.gameobjects;

import java.awt.Rectangle;

public class Door {
    public Rectangle bounds;
    
    public Door(int x, int y, int width, int height) {
        bounds = new Rectangle(x, y, width, height);
    }
}