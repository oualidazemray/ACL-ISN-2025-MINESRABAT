package com.lo3ba.gameobjects;

import com.lo3ba.util.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Platform {
    /**
     * Enum representing different visual/material platform types.
     * This determines the texture and visual appearance of the platform.
     * For behavioral variations (moving, breakable), use subclasses like MovingPlatform.
     */
    public enum PlatformType {
        FLOOR, STONE, CRATE, METAL, BRICK, ICE, LAVA
    }

    // Visual bounds used for rendering
    private final Rectangle visualBounds;
    // Collision bounds used for physics/collision checks
    private final Rectangle collisionBounds;
    
    // Platform type
    private PlatformType type;

    private static final int COLLISION_TOP_MARGIN = 0;
    
    // Standard platform dimensions matching the texture
    public static final int PLATFORM_WIDTH = 160;
    public static final int PLATFORM_HEIGHT = 32;

    // Texture images for different platform types
    private static BufferedImage floorTexture;
    private static BufferedImage stoneTexture;
    private static BufferedImage crateTexture;
    private static BufferedImage metalTexture;
    private static BufferedImage brickTexture;
    private static BufferedImage iceTexture;
    private static BufferedImage lavaTexture;

    static {
        // Load textures using ResourceManager for proper classpath loading
        floorTexture = ResourceManager.loadTexture("floor.png");
        stoneTexture = ResourceManager.loadTexture("platform_stone_64x32.png");
        crateTexture = ResourceManager.loadTexture("platform_crate_80x32.png");
        metalTexture = ResourceManager.loadTexture("platform_metal_80x32.png");
        brickTexture = ResourceManager.loadTexture("platform_brick_64x32.png");
        iceTexture = ResourceManager.loadTexture("platform_ice_80x32.png");
        lavaTexture = ResourceManager.loadTexture("platform_lava_64x32.png");
        
        // Verify the main floor texture has the correct dimensions
        if (floorTexture != null) {
            if (floorTexture.getWidth() != PLATFORM_WIDTH || floorTexture.getHeight() != PLATFORM_HEIGHT) {
                System.err.println("⚠️ Warning: floor.png dimensions are " + 
                    floorTexture.getWidth() + "x" + floorTexture.getHeight() + 
                    " but expected " + PLATFORM_WIDTH + "x" + PLATFORM_HEIGHT);
            }
        }
    }

    /**
     * Creates a platform with standard dimensions (160x32 px) and FLOOR type
     */
    public Platform(int x, int y) {
        this(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT, PlatformType.FLOOR);
    }

    /**
     * Creates a platform with custom dimensions and FLOOR type
     */
    public Platform(int x, int y, int width, int height) {
        this(x, y, width, height, PlatformType.FLOOR);
    }

    /**
     * Creates a platform with custom dimensions and specific type
     */
    public Platform(int x, int y, int width, int height, PlatformType type) {
        this.visualBounds = new Rectangle(x, y, width, height);
        int collisionY = y + COLLISION_TOP_MARGIN;
        int collisionHeight = Math.max(0, height - COLLISION_TOP_MARGIN);
        this.collisionBounds = new Rectangle(x, collisionY, width, collisionHeight);
        this.type = type;
    }

    public Rectangle getBounds() {
        return collisionBounds;
    }

    public int getX() { 
        return visualBounds.x; 
    }
    
    public int getY() { 
        return visualBounds.y; 
    }
    
    public int getWidth() { 
        return visualBounds.width; 
    }
    
    public int getHeight() { 
        return visualBounds.height; 
    }

    public void setPosition(int x, int y) {
        visualBounds.setLocation(x, y);
        collisionBounds.setLocation(x, y + COLLISION_TOP_MARGIN);
    }

    /**
     * Get the appropriate texture based on platform type
     */
    private BufferedImage getTextureForType() {
        switch (type) {
            case STONE: 
                return stoneTexture != null ? stoneTexture : floorTexture;
            case CRATE: 
                return crateTexture != null ? crateTexture : floorTexture;
            case METAL: 
                return metalTexture != null ? metalTexture : floorTexture;
            case BRICK: 
                return brickTexture != null ? brickTexture : floorTexture;
            case ICE: 
                return iceTexture != null ? iceTexture : floorTexture;
            case LAVA: 
                return lavaTexture != null ? lavaTexture : floorTexture;
            case FLOOR:
            default: 
                return floorTexture;
        }
    }

    public void render(Graphics2D g) {
        BufferedImage texture = getTextureForType();
        
        if (texture != null) {
            // Calculate how many times we need to tile the texture
            int tilesX = (int) Math.ceil((double) visualBounds.width / texture.getWidth());
            int tilesY = (int) Math.ceil((double) visualBounds.height / texture.getHeight());
            
            // Draw the texture tiled to cover the platform
            for (int tileX = 0; tileX < tilesX; tileX++) {
                for (int tileY = 0; tileY < tilesY; tileY++) {
                    int drawX = visualBounds.x + (tileX * texture.getWidth());
                    int drawY = visualBounds.y + (tileY * texture.getHeight());
                    
                    // Calculate the portion of the texture to draw (for edge tiles)
                    int drawWidth = Math.min(texture.getWidth(), 
                                            visualBounds.x + visualBounds.width - drawX);
                    int drawHeight = Math.min(texture.getHeight(), 
                                             visualBounds.y + visualBounds.height - drawY);
                    
                    // Draw the texture (or clipped portion for edges)
                    g.drawImage(texture, 
                               drawX, drawY, drawX + drawWidth, drawY + drawHeight,
                               0, 0, drawWidth, drawHeight,
                               null);
                }
            }
        } else {
            // Fallback rectangle if image not found
            g.setColor(new Color(100, 100, 100));
            g.fillRect(visualBounds.x, visualBounds.y, visualBounds.width, visualBounds.height);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(visualBounds.x, visualBounds.y, visualBounds.width, visualBounds.height);
        }
    }
}