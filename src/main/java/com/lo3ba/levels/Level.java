package com.lo3ba.levels;

import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public abstract class Level {
    protected Player player;
    protected boolean completed = false;
    protected int spawnX, spawnY;
    
    protected List<Platform> platforms;
    protected List<Spike> spikes;
    protected Door door;
    
    protected BufferedImage platformImg;
    protected BufferedImage spikeImg;
    protected BufferedImage doorImg;
    
    public Level(Player player) {
        this.player = player;
        this.platforms = new ArrayList<>();
        this.spikes = new ArrayList<>();
        loadTextures();
    }
    
    protected void loadTextures() {
        try {
            platformImg = ImageIO.read(getClass().getClassLoader()
                .getResourceAsStream("assets/textures/background.png"));
            spikeImg = ImageIO.read(getClass().getClassLoader()
                .getResourceAsStream("assets/textures/spike.png"));
            doorImg = ImageIO.read(getClass().getClassLoader()
                .getResourceAsStream("assets/textures/door.png"));
            
            // Set images to game objects (will be done in init)
        } catch (IOException e) {
            System.err.println("⚠ Could not load textures: " + e.getMessage());
        }
    }
    
    protected void setImagesForObjects() {
        // Set spike images
        for (Spike spike : spikes) {
            spike.setSpikeImage(spikeImg);
        }
        
        // Set door image
        if (door != null) {
            door.setDoorImage(doorImg);
        }
    }
    
    public abstract void init();
    public abstract void update();
    
    public void render(Graphics2D g) {
        // Render platforms
        for (Platform platform : platforms) {
            platform.render(g);
        }
        
        // Render spikes
        for (Spike spike : spikes) {
            spike.render(g);
        }
        
        // Render door
        if (door != null) {
            door.render(g);
        }
    }
    
    public void reset() {
        completed = false;
    }
    
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