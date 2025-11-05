package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.lo3ba.levels.Level1.*; // To access Platform, Spike, Door

public class Level4 extends Level {
    private List<Level1.Platform> platforms;
    private List<Level1.Spike> spikes;
    private Level1.Door door;
    
    // Assuming spikeImg and doorImg are fields inherited from the parent Level class.
    
    public Level4(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450; 	// Spawning on the floor (0, 500)
        init();
    }
    
    private void init() {
        Level1 level1 = new Level1(player);
        platforms = new ArrayList<>();
        spikes = new ArrayList<>();
        
        // Floor
        platforms.add(level1.new Platform(0, 500, 200, 100)); // Player spawns here (x=50, y=450)
        platforms.add(level1.new Platform(600, 500, 200, 100));
        
        // Narrow platforms zigzag
        platforms.add(level1.new Platform(150, 450, 60, 15)); // Platform is at y=450, same as spawnY. This is safe.
        platforms.add(level1.new Platform(250, 400, 60, 15));
        platforms.add(level1.new Platform(350, 350, 60, 15));
        platforms.add(level1.new Platform(450, 300, 60, 15));
        platforms.add(level1.new Platform(550, 350, 60, 15));
        
        // Ceiling spikes
        for (int i = 2; i < 6; i++) {
            spikes.add(level1.new Spike(i * 100, 0, 32, 32));
        }
        
        // Platform spikes
        spikes.add(level1.new Spike(250, 368, 32, 32));
        spikes.add(level1.new Spike(450, 268, 32, 32));
        
        // Floor spikes - these are in the gap between 200 and 600
        spikes.add(level1.new Spike(300, 468, 32, 32));
        spikes.add(level1.new Spike(400, 468, 32, 32));
        spikes.add(level1.new Spike(500, 468, 32, 32));
        
        door = level1.new Door(700, 420, 50, 80);
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
        // This prevents immediate death if the player's initial bounding box slightly overlaps with a spike on respawn.
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
        
        // Fall off screen (Using GameLoop.BASE_HEIGHT + 50 is often safer/more consistent)
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