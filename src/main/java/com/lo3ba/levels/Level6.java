package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.lo3ba.levels.Level1.*; // To access Platform, Spike, Door

public class Level6 extends Level {
    private List<Level1.Platform> platforms;
    private List<Level1.Spike> spikes;
    private Level1.Door door;
    
    // Assuming spikeImg and doorImg are fields inherited from the parent Level class.
    
    public Level6(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 100; // Spawning above the platform at y=150
        init();
    }
    
    private void init() {
        Level1 level1 = new Level1(player);
        platforms = new ArrayList<>();
        spikes = new ArrayList<>();
        
        // Starting platform high up
        platforms.add(level1.new Platform(0, 150, 120, 20)); // Player lands here
        
        // Vertical descent with precise jumps
        platforms.add(level1.new Platform(150, 200, 50, 15));
        platforms.add(level1.new Platform(250, 250, 50, 15));
        spikes.add(level1.new Spike(250, 218, 32, 32));
        
        platforms.add(level1.new Platform(350, 300, 50, 15));
        platforms.add(level1.new Platform(450, 350, 50, 15));
        spikes.add(level1.new Spike(450, 318, 32, 32));
        
        platforms.add(level1.new Platform(550, 400, 50, 15));
        platforms.add(level1.new Platform(650, 450, 100, 20));
        
        // Floor is all spikes
        platforms.add(level1.new Platform(0, 550, 800, 50));
        for (int i = 0; i < 16; i++) {
            spikes.add(level1.new Spike(i * 50, 518, 32, 32));
        }
        
        // Ceiling spikes
        for (int i = 0; i < 16; i++) {
            spikes.add(level1.new Spike(i * 50, 0, 32, 32));
        }
        
        door = level1.new Door(700, 370, 50, 80);
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
        // This prevents immediate death right after spawn/reset.
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
        
        // Fall off screen (Using GameLoop.BASE_HEIGHT + 50 is often more consistent)
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