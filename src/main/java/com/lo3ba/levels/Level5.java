package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.lo3ba.levels.Level1.*; // To access Platform, Spike, Door

public class Level5 extends Level {
    private List<Level1.Platform> platforms;
    private List<Level1.Spike> spikes;
    private Level1.Door door;
    
    // Assuming spikeImg and doorImg are fields inherited from the parent Level class.
    
    public Level5(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450; // Spawning on the floor at (0, 500)
        init();
    }
    
    private void init() {
        Level1 level1 = new Level1(player);
        platforms = new ArrayList<>();
        spikes = new ArrayList<>();
        
        // Split level - two paths
        platforms.add(level1.new Platform(0, 500, 150, 100)); // Starting Platform
        
        // Upper path (harder but shorter)
        platforms.add(level1.new Platform(180, 350, 70, 15));
        platforms.add(level1.new Platform(280, 300, 70, 15));
        platforms.add(level1.new Platform(380, 250, 70, 15));
        spikes.add(level1.new Spike(380, 218, 32, 32));
        platforms.add(level1.new Platform(480, 250, 70, 15));
        platforms.add(level1.new Platform(580, 300, 70, 15));
        
        // Lower path (easier but longer)
        platforms.add(level1.new Platform(180, 450, 70, 15));
        platforms.add(level1.new Platform(280, 450, 70, 15));
        spikes.add(level1.new Spike(320, 418, 32, 32));
        platforms.add(level1.new Platform(380, 450, 70, 15));
        platforms.add(level1.new Platform(480, 450, 70, 15));
        platforms.add(level1.new Platform(580, 400, 70, 15));
        
        // End platform
        platforms.add(level1.new Platform(680, 350, 120, 150));
        
        // Floor spikes
        for (int i = 3; i < 12; i++) {
            // Note: y=568 is below the screen/floor at y=500, check if this is intentional
            spikes.add(level1.new Spike(i * 50, 568, 32, 32)); 
        }
        
        door = level1.new Door(720, 270, 50, 80);
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
        // Only check for spike collision if the player is outside the initial spawn Y buffer.
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
        
        // Fall off screen (Using GameLoop.BASE_HEIGHT + 50 is more consistent with Level 1)
        if (player.getY() > GameLoop.BASE_HEIGHT + 50) { 
            player.die();
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        g.setColor(new Color(100, 100, 100));
        for (Level1.Platform platform : platforms) {
            g.fillRect(platform.bounds.x, platform.bounds.y, 
                       platform.bounds.width, platform.bounds.height);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(platform.bounds.x, platform.bounds.y, 
                       platform.bounds.width, platform.bounds.height);
            g.setColor(new Color(100, 100, 100));
        }
        
        // Draw spikes - Image ONLY
        for (Level1.Spike spike : spikes) {
            if (spikeImg != null) {
                g.drawImage(spikeImg, spike.bounds.x, spike.bounds.y, 
                            spike.bounds.width, spike.bounds.height, null);
            } 
            // Removed red triangle fallback
        }
        
        // Draw door - Image ONLY
        if (doorImg != null) {
            g.drawImage(doorImg, door.bounds.x, door.bounds.y, 
                        door.bounds.width, door.bounds.height, null);
        } 
        // Removed green rectangle fallback
    }
    
    @Override
    public void reset() {
        completed = false;
    }
}