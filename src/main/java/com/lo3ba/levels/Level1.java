package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level1 extends Level {
    private List<Platform> platforms;
    private List<Spike> spikes;
    private Door door;
    
    public Level1(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450;
        init();
    }
    
    private void init() {
        platforms = new ArrayList<>();
        spikes = new ArrayList<>();
        
        // Floor
        platforms.add(new Platform(0, 500, 800, 100));
        
        // Steps
        platforms.add(new Platform(300, 450, 100, 20));
        platforms.add(new Platform(450, 400, 100, 20));
        
        // Simple spike
        spikes.add(new Spike(400, 468, 32, 32));
        
        // Door at the end
        door = new Door(700, 420, 50, 80);
    }
    
    @Override
    public void update() {
        completed = false;
        
        Rectangle playerBounds = player.getBounds();
        boolean landed = false;
        
        // Platform collision - ONLY check when falling down
        for (Platform platform : platforms) {
            if (player.getVelocityY() > 0) { // Only when falling
                double playerBottom = player.getY() + Player.HEIGHT;
                double playerBottomPrev = playerBottom - player.getVelocityY();
                
                boolean horizontalOverlap = player.getX() + Player.WIDTH > platform.bounds.x && 
                                             player.getX() < platform.bounds.x + platform.bounds.width;
                
                if (horizontalOverlap && 
                    playerBottomPrev <= platform.bounds.y && 
                    playerBottom >= platform.bounds.y) {
                    
                    player.setY(platform.bounds.y - Player.HEIGHT);
                    player.setOnGround(true);
                    landed = true;
                    break;
                }
            }
        }
        
        // Spike collision - with small grace period at spawn (FIX retained)
        if (player.getY() < spawnY + 10) { 
            for (Spike spike : spikes) {
                // Reduced hitbox
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
        // --- 1. Draw Game Objects (Platform rendering reverted) ---
        
        // Draw platforms (REVERTED to solid gray rectangles)
        g.setColor(new Color(100, 100, 100));
        for (Platform platform : platforms) {
            g.fillRect(platform.bounds.x, platform.bounds.y, 
                       platform.bounds.width, platform.bounds.height);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(platform.bounds.x, platform.bounds.y, 
                       platform.bounds.width, platform.bounds.height);
            g.setColor(new Color(100, 100, 100));
        }
        
        // Draw spikes (Retained original image/fallback logic)
        for (Spike spike : spikes) {
            if (spikeImg != null) {
                g.drawImage(spikeImg, spike.bounds.x, spike.bounds.y, 
                            spike.bounds.width, spike.bounds.height, null);
            } else {
                g.setColor(Color.RED);
                int[] xPoints = {spike.bounds.x, spike.bounds.x + spike.bounds.width/2, spike.bounds.x + spike.bounds.width};
                int[] yPoints = {spike.bounds.y + spike.bounds.height, spike.bounds.y, spike.bounds.y + spike.bounds.height};
                g.fillPolygon(xPoints, yPoints, 3);
            }
        }
        
        // Draw door (Retained original image/fallback logic)
        if (doorImg != null) {
            g.drawImage(doorImg, door.bounds.x, door.bounds.y, 
                        door.bounds.width, door.bounds.height, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillRect(door.bounds.x, door.bounds.y, door.bounds.width, door.bounds.height);
        }

        // --- 2. UI Drawing REMOVED from here to fix duplication ---
    }
    
    @Override
    public void reset() {
        completed = false;
    }
    
    // Inner classes for level objects
    public class Platform {
        Rectangle bounds;
        public Platform(int x, int y, int width, int height) { bounds = new Rectangle(x, y, width, height); }
    }
    
    public class Spike {
        Rectangle bounds;
        public Spike(int x, int y, int width, int height) { bounds = new Rectangle(x, y, width, height); }
    }
    
    public class Door {
        Rectangle bounds;
        public Door(int x, int y, int width, int height) { bounds = new Rectangle(x, y, width, height); }
    }
}