package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.lo3ba.levels.Level1.*; // To access Platform, Spike, Door

public class Level7 extends Level {
    private List<Level1.Platform> platforms;
    private List<Level1.Spike> spikes;
    private Level1.Door door;
    
    // Assuming spikeImg and doorImg are fields inherited from the parent Level class.
    
    public Level7(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450; // Spawning on the platform at (0, 500)
        init();
    }
    
    private void init() {
        Level1 level1 = new Level1(player);
        platforms = new ArrayList<>();
        spikes = new ArrayList<>();
        
        // Start
        platforms.add(level1.new Platform(0, 500, 150, 100)); // Starting Platform
        
        // Maze-like structure
        platforms.add(level1.new Platform(200, 450, 150, 20));
        spikes.add(level1.new Spike(250, 418, 32, 32));
        
        // Vertical wall
        platforms.add(level1.new Platform(400, 200, 20, 300));
        
        // Upper section
        platforms.add(level1.new Platform(200, 250, 180, 20));
        spikes.add(level1.new Spike(300, 218, 32, 32));
        
        // Drop down section
        platforms.add(level1.new Platform(450, 350, 100, 20));
        spikes.add(level1.new Spike(450, 318, 32, 32));
        spikes.add(level1.new Spike(518, 318, 32, 32));
        
        platforms.add(level1.new Platform(600, 450, 100, 20));
        
        // Final platform
        platforms.add(level1.new Platform(650, 500, 150, 100));
        
        // Scattered floor spikes
        spikes.add(level1.new Spike(150, 568, 32, 32));
        spikes.add(level1.new Spike(550, 568, 32, 32));
        
        door = level1.new Door(730, 420, 50, 80);
    }
    
    @Override
    public void update() {
        completed = false;
        
        Rectangle playerBounds = player.getBounds();
        
        // Platform collision (Unchanged and correct)
        for (Level1.Platform platform : platforms) {
            Rectangle platBounds = platform.bounds;
            
            if (player.getVelocityY() > 0) {
                double playerBottom = player.getY() + Player.HEIGHT;
                double playerBottomPrev = playerBottom - player.getVelocityY();
                
                if (player.getX() + Player.WIDTH > platBounds.x && 
                    player.getX() < platBounds.x + platBounds.width) {
                    
                    if (playerBottomPrev <= platBounds.y && playerBottom >= platBounds.y) {
                        player.setY(platBounds.y - Player.HEIGHT);
                        player.setOnGround(true);
                        break; 
                    }
                }
            }
        }
        
        // 🌟 THE FIX: Spike collision - Re-introduced small grace period from Level 1 🌟
        if (player.getY() < spawnY + 10) { 
            for (Level1.Spike spike : spikes) {
                // Apply the reduced hitbox from Level 1 for forgiving collision
                Rectangle spikeHitbox = new Rectangle(
                    spike.bounds.x + 4, 
                    spike.bounds.y + 4, 
                    spike.bounds.width - 8, 
                    spike.bounds.height - 8
                );
                
                if (checkCollision(playerBounds, spikeHitbox)) {
                    player.die();
                }
            }
        }
        // ------------------------------------------------------------------
        
        // Door collision
        if (checkCollision(playerBounds, door.bounds)) {
            completed = true;
        }
        
        // Fall off screen 
        if (player.getY() > GameLoop.BASE_HEIGHT + 50) { 
            player.die();
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        // Draw platforms
        g.setColor(new Color(100, 100, 100));
        for (Level1.Platform platform : platforms) {
            g.fillRect(platform.bounds.x, platform.bounds.y, 
                       platform.bounds.width, platform.bounds.height);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(platform.bounds.x, platform.bounds.y, 
                       platform.bounds.width, platform.bounds.height);
            g.setColor(new Color(100, 100, 100));
        }
        
        // Draw spikes - Image ONLY (Removed color fallback)
        for (Level1.Spike spike : spikes) {
            if (spikeImg != null) {
                g.drawImage(spikeImg, spike.bounds.x, spike.bounds.y, 
                            spike.bounds.width, spike.bounds.height, null);
            } 
        }
        
        // Draw door - Image ONLY (Removed color fallback)
        if (doorImg != null) {
            g.drawImage(doorImg, door.bounds.x, door.bounds.y, 
                        door.bounds.width, door.bounds.height, null);
        } 
    }
    
    @Override
    public void reset() {
        completed = false;
    }
}