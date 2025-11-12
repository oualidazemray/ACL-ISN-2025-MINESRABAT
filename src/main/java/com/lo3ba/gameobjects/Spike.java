package com.lo3ba.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Spike {
    // Enum for different spike types
    public enum SpikeType {
        NORMAL,    // Regular metal spikes
        ICE,       // Icicle spikes
        FIRE,      // Flame/lava spikes
        POISON,    // Toxic/green spikes
        ELECTRIC,  // Lightning/electric spikes
        BONE       // Skeletal bone spikes
    }
    
    private Rectangle bounds;
    private SpikeType type;
    // Reused hitbox rectangle to avoid per-frame allocations
    private final Rectangle hitbox = new Rectangle();

    // Texture images for different spike types
    private static BufferedImage normalSpike;
    private static BufferedImage iceSpike;
    private static BufferedImage fireSpike;
    private static BufferedImage poisonSpike;
    private static BufferedImage electricSpike;
    private static BufferedImage boneSpike;

    static {
        try {
            String basePath = "C:\\Users\\khalil\\Documents\\GitHub\\ACL-ISN-2025-MINESRABAT\\src\\main\\resources\\assets\\textures\\";
            
            // Try to load spike textures (optional - will use fallback if not found)
            try {
                normalSpike = ImageIO.read(new File(basePath + "spike_normal_32x32.png"));
            } catch (IOException e) {
                System.out.println("Normal spike texture not found, will use fallback");
            }
            
            try {
                iceSpike = ImageIO.read(new File(basePath + "spike_ice_32x32.png"));
            } catch (IOException e) {
                System.out.println("Ice spike texture not found, will use fallback");
            }
            
            try {
                fireSpike = ImageIO.read(new File(basePath + "spike_fire_32x32.png"));
            } catch (IOException e) {
                System.out.println("Fire spike texture not found, will use fallback");
            }
            
            try {
                poisonSpike = ImageIO.read(new File(basePath + "spike_poison_32x32.png"));
            } catch (IOException e) {
                System.out.println("Poison spike texture not found, will use fallback");
            }
            
            try {
                electricSpike = ImageIO.read(new File(basePath + "spike_electric_32x32.png"));
            } catch (IOException e) {
                System.out.println("Electric spike texture not found, will use fallback");
            }
            
            try {
                boneSpike = ImageIO.read(new File(basePath + "spike_bone_32x32.png"));
            } catch (IOException e) {
                System.out.println("Bone spike texture not found, will use fallback");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("⚠️ Failed to load spike textures. Using fallback rendering.");
        }
    }

    /**
     * Creates a spike with NORMAL type
     */
    public Spike(int x, int y, int width, int height) {
        this(x, y, width, height, SpikeType.NORMAL);
    }
    
    /**
     * Creates a spike with specific type
     */
    public Spike(int x, int y, int width, int height, SpikeType type) {
        this.bounds = new Rectangle(x, y, width, height);
        this.type = type;
    }
    
    /**
     * Legacy method for compatibility - can be deprecated
     */
    @Deprecated
    public void setSpikeImage(BufferedImage image) {
        // This method is kept for backward compatibility but is no longer needed
        // The texture is now determined by the spike type
    }
    
    /**
     * Get the appropriate texture based on spike type
     */
    private BufferedImage getTextureForType() {
        switch (type) {
            case ICE:
                return iceSpike;
            case FIRE:
                return fireSpike;
            case POISON:
                return poisonSpike;
            case ELECTRIC:
                return electricSpike;
            case BONE:
                return boneSpike;
            case NORMAL:
            default:
                return normalSpike;
        }
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public Rectangle getHitbox() {
        // Slightly reduced hitbox for more forgiving collision; reuse cached rectangle
        hitbox.setBounds(bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4);
        return hitbox;
    }
    
    public int getX() { return bounds.x; }
    public int getY() { return bounds.y; }
    public int getWidth() { return bounds.width; }
    public int getHeight() { return bounds.height; }
    public SpikeType getType() { return type; }
    
    public void render(Graphics2D g) {
        BufferedImage texture = getTextureForType();
        
        if (texture != null) {
            g.drawImage(texture, bounds.x, bounds.y, bounds.width, bounds.height, null);
        } else {
            // Fallback: draw colored triangle based on type
            drawFallbackSpike(g);
        }
    }
    
    /**
     * Draw a fallback spike when texture is not available
     */
    private void drawFallbackSpike(Graphics2D g) {
        Color spikeColor;
        
        switch (type) {
            case ICE:
                spikeColor = new Color(173, 216, 230); // Light blue
                break;
            case FIRE:
                spikeColor = new Color(255, 69, 0); // Orange-red
                break;
            case POISON:
                spikeColor = new Color(0, 255, 0); // Bright green
                break;
            case ELECTRIC:
                spikeColor = new Color(255, 255, 0); // Yellow
                break;
            case BONE:
                spikeColor = new Color(245, 245, 220); // Beige
                break;
            case NORMAL:
            default:
                spikeColor = Color.RED;
                break;
        }
        
        g.setColor(spikeColor);
        int[] xPoints = {bounds.x, bounds.x + bounds.width/2, bounds.x + bounds.width};
        int[] yPoints = {bounds.y + bounds.height, bounds.y, bounds.y + bounds.height};
        g.fillPolygon(xPoints, yPoints, 3);
        
        // Add outline for better visibility
        g.setColor(spikeColor.darker());
        g.drawPolygon(xPoints, yPoints, 3);
        
        // Special effects for certain types
        if (type == SpikeType.ELECTRIC) {
            // Draw lightning effect
            g.setColor(new Color(255, 255, 255, 150));
            g.drawLine(bounds.x + bounds.width/2, bounds.y, bounds.x + bounds.width/2, bounds.y + bounds.height);
        } else if (type == SpikeType.FIRE) {
            // Draw flame flicker
            g.setColor(new Color(255, 255, 0, 100));
            g.fillOval(bounds.x + bounds.width/4, bounds.y + bounds.height/2, bounds.width/2, bounds.height/3);
        }
    }
}