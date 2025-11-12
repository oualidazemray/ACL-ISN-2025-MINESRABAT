package com.lo3ba.levels;

import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Door;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    // Debug rendering: draw platform visual bounds (gray), collision bounds (yellow),
    // spikes visual bounds (red) and hitboxes (cyan). This is called from GameLoop when debug is enabled.
    public void debugRender(Graphics2D g) {
        // Draw platforms: visual (gray outline) + collision bounds (yellow)
        for (Platform p : platforms) {
            // Visual bounds (platform render uses these)
            g.setColor(Color.GRAY);
            g.drawRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());

            // Collision bounds (Platform.getBounds() used for collision)
            Rectangle coll = p.getBounds();
            if (coll != null) {
                g.setColor(Color.YELLOW);
                g.drawRect(coll.x, coll.y, coll.width, coll.height);
            }
        }

        // Draw spikes: visual (red) + hitbox (cyan)
        for (Spike s : spikes) {
            Rectangle vb = s.getBounds();
            if (vb != null) {
                g.setColor(Color.RED);
                g.drawRect(vb.x, vb.y, vb.width, vb.height);
            }
            Rectangle hb = s.getHitbox();
            if (hb != null) {
                g.setColor(Color.CYAN);
                g.drawRect(hb.x, hb.y, hb.width, hb.height);
            }
        }

        // Door bounds (if exists)
        if (door != null) {
            Rectangle db = door.getBounds();
            if (db != null) {
                g.setColor(Color.GREEN);
                g.drawRect(db.x, db.y, db.width, db.height);
            }
        }
    }
}