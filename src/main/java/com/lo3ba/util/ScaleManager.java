package com.lo3ba.util;

/**
 * ScaleManager handles coordinate scaling between base design coordinates (1000x600)
 * and actual window dimensions. This allows the game to support different window sizes
 * while maintaining proper positioning and proportions.
 */
public class ScaleManager {
    private static ScaleManager instance;
    
    // Base design dimensions
    public static final int BASE_WIDTH = 1000;
    public static final int BASE_HEIGHT = 600;
    
    // Current actual dimensions
    private int currentWidth;
    private int currentHeight;
    
    // Scale factors
    private double scaleX;
    private double scaleY;
    
    private ScaleManager() {
        // Initialize with base dimensions
        currentWidth = BASE_WIDTH;
        currentHeight = BASE_HEIGHT;
        scaleX = 1.0;
        scaleY = 1.0;
    }
    
    /**
     * Get the singleton instance
     */
    public static ScaleManager getInstance() {
        if (instance == null) {
            instance = new ScaleManager();
        }
        return instance;
    }
    
    /**
     * Update the current window dimensions and recalculate scale factors
     */
    public void updateDimensions(int width, int height) {
        this.currentWidth = width;
        this.currentHeight = height;
        this.scaleX = (double) width / BASE_WIDTH;
        this.scaleY = (double) height / BASE_HEIGHT;
    }
    
    /**
     * Scale a base X coordinate to actual screen coordinate
     */
    public int scaleX(int baseX) {
        return (int) Math.round(baseX * scaleX);
    }
    
    /**
     * Scale a base Y coordinate to actual screen coordinate
     */
    public int scaleY(int baseY) {
        return (int) Math.round(baseY * scaleY);
    }
    
    /**
     * Scale a base width to actual width
     */
    public int scaleWidth(int baseWidth) {
        return (int) Math.round(baseWidth * scaleX);
    }
    
    /**
     * Scale a base height to actual height
     */
    public int scaleHeight(int baseHeight) {
        return (int) Math.round(baseHeight * scaleY);
    }
    
    /**
     * Convert actual X coordinate back to base coordinate
     */
    public int unscaleX(int actualX) {
        return (int) Math.round(actualX / scaleX);
    }
    
    /**
     * Convert actual Y coordinate back to base coordinate
     */
    public int unscaleY(int actualY) {
        return (int) Math.round(actualY / scaleY);
    }
    
    /**
     * Get current width
     */
    public int getCurrentWidth() {
        return currentWidth;
    }
    
    /**
     * Get current height
     */
    public int getCurrentHeight() {
        return currentHeight;
    }
    
    /**
     * Get X scale factor
     */
    public double getScaleX() {
        return scaleX;
    }
    
    /**
     * Get Y scale factor
     */
    public double getScaleY() {
        return scaleY;
    }
}
