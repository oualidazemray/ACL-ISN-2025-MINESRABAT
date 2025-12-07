package com.lo3ba.effects;

/**
 * Camera shake effect for impactful visual feedback
 */
public class CameraShake {
    private float intensity;
    private int duration;
    private int elapsed;
    private double offsetX;
    private double offsetY;
    
    public CameraShake() {
        intensity = 0;
        duration = 0;
        elapsed = 0;
    }
    
    /**
     * Start a camera shake
     * @param intensity How strong the shake is (pixels)
     * @param duration How long it lasts (frames)
     */
    public void shake(float intensity, int duration) {
        this.intensity = intensity;
        this.duration = duration;
        this.elapsed = 0;
    }
    
    /**
     * Update shake state
     */
    public void update() {
        if (elapsed < duration) {
            elapsed++;
            
            // Calculate decay factor
            float factor = 1.0f - ((float)elapsed / duration);
            float currentIntensity = intensity * factor;
            
            // Random offset
            offsetX = (Math.random() - 0.5) * 2 * currentIntensity;
            offsetY = (Math.random() - 0.5) * 2 * currentIntensity;
        } else {
            offsetX = 0;
            offsetY = 0;
        }
    }
    
    /**
     * Get current X offset
     */
    public int getOffsetX() {
        return (int)offsetX;
    }
    
    /**
     * Get current Y offset
     */
    public int getOffsetY() {
        return (int)offsetY;
    }
    
    /**
     * Check if shake is active
     */
    public boolean isActive() {
        return elapsed < duration;
    }
    
    /**
     * Stop shake immediately
     */
    public void stop() {
        elapsed = duration;
        offsetX = 0;
        offsetY = 0;
    }
}
