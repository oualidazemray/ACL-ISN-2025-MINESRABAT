package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.lo3ba.levels.Level1.*; // To access Platform, Spike, Door

public class Level3 extends Level {
    private List<Level1.Platform> platforms;
    private List<Level1.Spike> spikes;
    private Level1.Door door;
    
    // Assuming spikeImg and doorImg are fields inherited from the parent Level class.
    
    public Level3(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 150; 	// Platform at y=200, spawn at 150
        init();
    }
    
    private void init() {
        // Must instantiate Level1 to create non-static inner classes (Platform, Spike, Door)
        Level1 level1 = new Level1(player); 
        platforms = new ArrayList<>();
        spikes = new ArrayList<>();
        
        // High starting platform
        platforms.add(level1.new Platform(0, 200, 150, 20));
        
        // Descending platforms with spikes
        platforms.add(level1.new Platform(200, 250, 80, 20));
        spikes.add(level1.new Spike(200, 218, 32, 32));
        
        platforms.add(level1.new Platform(330, 300, 80, 20));
        
        platforms.add(level1.new Platform(460, 350, 80, 20));
        spikes.add(level1.new Spike(500, 318, 32, 32));
        
        platforms.add(level1.new Platform(590, 400, 80, 20));
        
        // Floor with spikes
        platforms.add(level1.new Platform(0, 500, 800, 100));
        for (int i = 0; i < 15; i++) {
            spikes.add(level1.new Spike(i * 50 + 10, 468, 32, 32));
        }
        
        door = level1.new Door(700, 320, 50, 80);
    }
    
    @Override
    public void update() {
        completed = false;
        
        Rectangle playerBounds = player.getBounds();
        
        // Platform collision (Unchanged from your logic)
        for (Level1.Platform platform : platforms) {
            Rectangle platBounds = platform.bounds;
            
            if (player.getVelocityY() > 0) { // Only when falling
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
                // Reduce hitbox slightly for more forgiving collision (from Level1 logic)
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
        // Changed back to BASE_HEIGHT + 50 for consistency with Level1
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
            // Assuming spikeImg is available and loaded (e.g., inherited from Level)
            if (spikeImg != null) {
                g.drawImage(spikeImg, spike.bounds.x, spike.bounds.y, 
                           spike.bounds.width, spike.bounds.height, null);
            }
            // Removed fallback red triangle drawing
        }
        
        // Draw door - Image ONLY
        if (doorImg != null) {
            g.drawImage(doorImg, door.bounds.x, door.bounds.y, 
                        door.bounds.width, door.bounds.height, null);
        }
        // Removed fallback green rectangle drawing
    }
    
    @Override
    public void reset() {
        completed = false;
    }
}