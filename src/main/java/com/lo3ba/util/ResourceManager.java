package com.lo3ba.util;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Centralized resource management for loading game assets.
 * Provides consistent resource loading with proper error handling and fallbacks.
 * Implements caching to avoid redundant loading of the same resources.
 * 
 * @author Lo3ba Team
 * @version 2.0
 */
public class ResourceManager {
    private static final String TEXTURE_PATH = "assets/textures/";
    private static final String SOUND_PATH = "assets/sounds/";
    private static final String FONT_PATH = "fonts/";
    
    // Thread-safe caches for resources
    private static final Map<String, BufferedImage> textureCache = new ConcurrentHashMap<>();
    private static final Map<String, Font> fontCache = new ConcurrentHashMap<>();
    
    /**
     * Loads a texture from the resources folder.
     * Uses caching to avoid reloading the same texture multiple times.
     * 
     * @param filename the name of the texture file (e.g., "floor.png")
     * @return the loaded BufferedImage, or a fallback colored rectangle if loading fails
     */
    public static BufferedImage loadTexture(String filename) {
        // Check cache first
        BufferedImage cached = textureCache.get(filename);
        if (cached != null) {
            System.out.println("✓ Using cached texture: " + filename);
            return cached;
        }
        
        // Load from file
        try {
            InputStream is = ResourceManager.class.getClassLoader()
                .getResourceAsStream(TEXTURE_PATH + filename);
            
            if (is == null) {
                System.err.println("⚠️ Texture not found in resources: " + TEXTURE_PATH + filename);
                BufferedImage fallback = createFallbackTexture(filename);
                textureCache.put(filename, fallback); // Cache fallback too
                return fallback;
            }
            
            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                System.err.println("⚠️ Failed to decode texture: " + filename);
                BufferedImage fallback = createFallbackTexture(filename);
                textureCache.put(filename, fallback);
                return fallback;
            }
            
            // Cache the loaded image
            textureCache.put(filename, image);
            System.out.println("✓ Loaded and cached texture: " + filename);
            return image;
            
        } catch (IOException e) {
            System.err.println("⚠️ Error loading texture: " + filename);
            e.printStackTrace();
            BufferedImage fallback = createFallbackTexture(filename);
            textureCache.put(filename, fallback);
            return fallback;
        }
    }
    
    /**
     * Loads a sound clip from the resources folder.
     * 
     * @param filename the name of the sound file (e.g., "jump.wav")
     * @return the loaded Clip, or null if loading fails
     */
    public static Clip loadSound(String filename) {
        try {
            InputStream is = ResourceManager.class.getClassLoader()
                .getResourceAsStream(SOUND_PATH + filename);
            
            if (is == null) {
                System.err.println("⚠️ Sound not found in resources: " + SOUND_PATH + filename);
                return null;
            }
            
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            System.out.println("✓ Loaded sound: " + filename);
            return clip;
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("⚠️ Error loading sound: " + filename);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Loads a TrueType font from the resources folder.
     * Uses caching to avoid reloading the same font multiple times.
     * Cache key includes both filename and size.
     * 
     * @param filename the name of the font file (e.g., "PressStart2P-Regular.ttf")
     * @param size the font size
     * @return the loaded Font, or a fallback font if loading fails
     */
    public static Font loadFont(String filename, float size) {
        // Create cache key with size
        String cacheKey = filename + "_" + size;
        
        // Check cache first
        Font cached = fontCache.get(cacheKey);
        if (cached != null) {
            System.out.println("✓ Using cached font: " + cacheKey);
            return cached;
        }
        
        // Load from file
        try {
            InputStream is = ResourceManager.class.getClassLoader()
                .getResourceAsStream(FONT_PATH + filename);
            
            if (is == null) {
                System.err.println("⚠️ Font not found in resources: " + FONT_PATH + filename);
                Font fallback = new Font("Monospaced", Font.BOLD, (int)size);
                fontCache.put(cacheKey, fallback);
                return fallback;
            }
            
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            Font derivedFont = font.deriveFont(size);
            
            // Cache the loaded font
            fontCache.put(cacheKey, derivedFont);
            System.out.println("✓ Loaded and cached font: " + cacheKey);
            return derivedFont;
            
        } catch (FontFormatException | IOException e) {
            System.err.println("⚠️ Error loading font: " + filename);
            e.printStackTrace();
            Font fallback = new Font("Monospaced", Font.BOLD, (int)size);
            fontCache.put(cacheKey, fallback);
            return fallback;
        }
    }
    
    /**
     * Creates a simple colored fallback texture when the real texture fails to load.
     * The color is based on the filename to distinguish different missing textures.
     * 
     * @param filename the original filename (used to generate a consistent color)
     * @return a simple colored BufferedImage
     */
    private static BufferedImage createFallbackTexture(String filename) {
        int width = 64;
        int height = 64;
        BufferedImage fallback = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = fallback.createGraphics();
        
        // Generate a color based on filename hash for consistency
        int hash = filename.hashCode();
        Color color = new Color(
            100 + (Math.abs(hash) % 100),
            100 + (Math.abs(hash >> 8) % 100),
            100 + (Math.abs(hash >> 16) % 100)
        );
        
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        
        // Add border
        g.setColor(color.darker());
        g.drawRect(0, 0, width - 1, height - 1);
        
        // Add "X" to indicate missing texture
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("?", width / 2 - 8, height / 2 + 8);
        
        g.dispose();
        
        System.out.println("ℹ️ Created fallback texture for: " + filename);
        return fallback;
    }
    
    /**
     * Checks if a resource exists in the classpath.
     * 
     * @param path the relative path to the resource
     * @return true if the resource exists, false otherwise
     */
    public static boolean resourceExists(String path) {
        InputStream is = ResourceManager.class.getClassLoader().getResourceAsStream(path);
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                // Ignore
            }
            return true;
        }
        return false;
    }
    
    /**
     * Clears all cached resources to free memory.
     * Useful when switching between game states or for memory management.
     */
    public static void clearCaches() {
        int textureCount = textureCache.size();
        int fontCount = fontCache.size();
        
        textureCache.clear();
        fontCache.clear();
        
        System.out.println("ℹ️ Cleared caches - Textures: " + textureCount + ", Fonts: " + fontCount);
    }
    
    /**
     * Gets current cache statistics for debugging and monitoring.
     * 
     * @return formatted string with cache sizes
     */
    public static String getCacheStats() {
        return String.format("Cache Stats - Textures: %d, Fonts: %d",
            textureCache.size(), fontCache.size());
    }
    
    /**
     * Pre-loads commonly used resources into cache.
     * Call during loading screens to improve game responsiveness.
     */
    public static void preloadCommonResources() {
        System.out.println("ℹ️ Pre-loading common resources...");
        
        // Pre-load commonly used textures
        loadTexture("background.png");
        loadTexture("spike.png");
        loadTexture("doorClose.png");
        loadTexture("doorOpen.png");
        loadTexture("star.png");
        
        // Pre-load font
        loadFont("PressStart2P-Regular.ttf", 16f);
        
        System.out.println("ℹ️ Pre-loading complete: " + getCacheStats());
    }
}
