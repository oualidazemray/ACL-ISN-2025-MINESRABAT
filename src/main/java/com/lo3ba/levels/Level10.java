package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import com.lo3ba.levels.Level1.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level10 extends Level {
    private List<Level1.Platform> platforms;
    private List<Level1.Spike> spikes;
    private Level1.Door door;
    
    public Level10(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450;
        init();
    }
    
    private void init() {
        Level1 level1 = new Level1(player);
        platforms = new ArrayList<>();
        spikes = new ArrayList<>();
        
        // The ultimate challenge - combination of everything
        
        // Starting area
        platforms.add(level1.new Platform(0, 500, 120, 100));
        
        // First section - narrow jumps
        platforms.add(level1.new Platform(150, 450, 35, 15));
        spikes.add(level1.new Spike(150, 418, 32, 32));
        
        platforms.add(level1.new Platform(220, 400, 35, 15));
        
        // Second section - go up
        platforms.add(level1.new Platform(290, 350, 35, 15));
        spikes.add(level1.new Spike(290, 318, 32, 32));
        
        platforms.add(level1.new Platform(360, 300, 35, 15));
        
        platforms.add(level1.new Platform(430, 250, 35, 15));
        spikes.add(level1.new Spike(430, 218, 32, 32));
        
        // Third section - platform with spike maze
        platforms.add(level1.new Platform(500, 200, 150, 20));
        spikes.add(level1.new Spike(520, 168, 32, 32));
        spikes.add(level1.new Spike(590, 168, 32, 32));
        
        // Fourth section - descend carefully
        platforms.add(level1.new Platform(680, 250, 35, 15));
        platforms.add(level1.new Platform(650, 300, 35, 15));
        spikes.add(level1.new Spike(650, 268, 32, 32));
        
        platforms.add(level1.new Platform(620, 350, 35, 15));
        platforms.add(level1.new Platform(590, 400, 35, 15));
        spikes.add(level1.new Spike(590, 368, 32, 32));
        
        // Final platform
        platforms.add(level1.new Platform(560, 450, 240, 150));
        
        // Spikes everywhere
        for (int i = 3; i < 11; i++) {
            spikes.add(level1.new Spike(i * 50, 568, 32, 32));
        }
        
        // Ceiling spikes
        for (int i = 0; i < 10; i++) {
            spikes.add(level1.new Spike(i * 80, 0, 32, 32));
        }
        
        // Additional danger spikes
        spikes.add(level1.new Spike(220, 368, 32, 32));
        spikes.add(level1.new Spike(360, 268, 32, 32));
        
        door = level1.new Door(730, 370, 50, 80);
    }
    
    @Override
    public void update() {
        completed = false;
        
        Rectangle playerBounds = player.getBounds();
        
        // Platform collision with better detection
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
                    }
                }
            }
        }
        
        for (Level1.Spike spike : spikes) {
            if (checkCollision(playerBounds, spike.bounds)) {
                player.die();
            }
        }
        
        if (checkCollision(playerBounds, door.bounds)) {
            completed = true;
        }
        
        if (player.getY() > GameLoop.HEIGHT) {
            player.die();
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        // Draw a special background effect for final level
        g.setColor(new Color(80, 0, 0, 30));
        g.fillRect(0, 0, GameLoop.WIDTH, GameLoop.HEIGHT);
        
        g.setColor(new Color(120, 100, 100));
        for (Level1.Platform platform : platforms) {
            g.fillRect(platform.bounds.x, platform.bounds.y, 
                      platform.bounds.width, platform.bounds.height);
            g.setColor(new Color(150, 50, 50));
            g.drawRect(platform.bounds.x, platform.bounds.y, 
                      platform.bounds.width, platform.bounds.height);
            g.setColor(new Color(120, 100, 100));
        }
        
        for (Level1.Spike spike : spikes) {
            if (spikeImg != null) {
                g.drawImage(spikeImg, spike.bounds.x, spike.bounds.y, 
                           spike.bounds.width, spike.bounds.height, null);
            } else {
                g.setColor(new Color(200, 0, 0));
                int[] xPoints = {spike.bounds.x, spike.bounds.x + spike.bounds.width/2, spike.bounds.x + spike.bounds.width};
                int[] yPoints = {spike.bounds.y + spike.bounds.height, spike.bounds.y, spike.bounds.y + spike.bounds.height};
                g.fillPolygon(xPoints, yPoints, 3);
            }
        }
        
        if (doorImg != null) {
            g.drawImage(doorImg, door.bounds.x, door.bounds.y, 
                       door.bounds.width, door.bounds.height, null);
        } else {
            g.setColor(new Color(0, 255, 100));
            g.fillRect(door.bounds.x, door.bounds.y, door.bounds.width, door.bounds.height);
        }
        
        // Victory message hint
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("FINAL LEVEL!", 350, 30);
    }
    
    @Override
    public void reset() {
        completed = false;
    }
}