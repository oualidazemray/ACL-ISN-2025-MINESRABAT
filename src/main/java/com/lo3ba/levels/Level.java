package com.lo3ba.levels;

import com.lo3ba.core.Player;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Level {
    protected Player player;
    protected boolean completed = false;
    protected int spawnX, spawnY;
    
    // Keeping BufferedImage type as used in your original file
    protected BufferedImage platformImg; 
    protected BufferedImage spikeImg;
    protected BufferedImage doorImg;
    
    public Level(Player player) {
        this.player = player;
        loadTextures();
    }
    
    protected void loadTextures() {
        try {
            // Reverting platformImg back to load background.png (as per your original Level.java)
            platformImg = ImageIO.read(getClass().getClassLoader()
                .getResourceAsStream("assets/textures/background.png")); 
            
            spikeImg = ImageIO.read(getClass().getClassLoader()
                .getResourceAsStream("assets/textures/spike.png"));
            
            doorImg = ImageIO.read(getClass().getClassLoader()
                .getResourceAsStream("assets/textures/door.png"));
        } catch (IOException e) {
            System.err.println("Error loading level textures:");
            e.printStackTrace();
        }
    }
    
    public abstract void update();
    public abstract void render(Graphics2D g);
    public abstract void reset();
    
    public boolean isCompleted() {
        return completed;
    }
    
    public int getSpawnX() {
        return spawnX;
    }
    
    public int getSpawnY() {
        return spawnY;
    }
    
    protected boolean checkCollision(Rectangle a, Rectangle b) {
        return a.intersects(b);
    }
}