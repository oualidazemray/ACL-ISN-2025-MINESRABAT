package com.lo3ba.gameobjects;


import java.awt.*;

/**
 * A platform that moves between two points.
 * Movement behavior is defined by this class itself, while visual appearance
 * is determined by the PlatformType passed to the constructor.
 */
public class MovingPlatform extends Platform {
    private int startX, startY;
    private int endX, endY;
    private double speed;
    private double currentX, currentY;
    private boolean movingForward = true;
    
    // Track movement for player physics
    private double deltaX = 0;
    private double deltaY = 0;

    public MovingPlatform(int startX, int startY, int endX, int endY, double speed, PlatformType type) {
        super(startX, startY, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT, type);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.speed = speed;
        this.currentX = startX;
        this.currentY = startY;
    }
    
    // Constructor with custom size
    public MovingPlatform(int startX, int startY, int endX, int endY, int width, int height, double speed, PlatformType type) {
        super(startX, startY, width, height, type);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.speed = speed;
        this.currentX = startX;
        this.currentY = startY;
    }

    public void update() {
        double targetX = movingForward ? endX : startX;
        double targetY = movingForward ? endY : startY;
        
        double dx = targetX - currentX;
        double dy = targetY - currentY;
        double distance = Math.sqrt(dx*dx + dy*dy);
        
        deltaX = 0;
        deltaY = 0;
        
        if (distance <= speed) {
            // Reached target
            deltaX = targetX - currentX;
            deltaY = targetY - currentY;
            currentX = targetX;
            currentY = targetY;
            movingForward = !movingForward;
        } else {
            // Move towards target
            double angle = Math.atan2(dy, dx);
            deltaX = Math.cos(angle) * speed;
            deltaY = Math.sin(angle) * speed;
            currentX += deltaX;
            currentY += deltaY;
        }
        
        // Update the visual/collision bounds in the parent class
        setPosition((int)currentX, (int)currentY);
    }
    
    // Accessors for movement delta (used to move player)
    public double getDeltaX() { return deltaX; }
    public double getDeltaY() { return deltaY; }
    
    // Override getX/Y to return current double precision position cast to int
    @Override
    public int getX() { return (int)currentX; }
    
    @Override
    public int getY() { return (int)currentY; }
}
