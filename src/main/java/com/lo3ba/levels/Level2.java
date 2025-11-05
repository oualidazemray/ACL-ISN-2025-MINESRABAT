package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Import is necessary to access the inner classes of Level1
import com.lo3ba.levels.Level1.*;

public class Level2 extends Level {
    // Must reference inner classes using Level1 prefix
    private List<Level1.Platform> platforms;
    private List<Level1.Spike> spikes;
    private Level1.Door door;
    
    // We need a Level1 instance to call the super constructor of the inner classes
    private Level1 level1; 

    public Level2(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450; 
        
        // Initialize the Level1 instance here so it's available for MovingSpike constructor
        level1 = new Level1(player); 
        init();
    }

    // MovingSpike class (Unique to Level 2, extends Level1's inner Spike class)
    private class MovingSpike extends Level1.Spike {
        private double baseY;
        private double time = 0;
        private double angle = 0;

        public MovingSpike(Level1 level, int x, int y, int w, int h) {
            // Correct way to call super constructor of a non-static inner class
            level.super(x, y, w, h); 
            this.baseY = y;
        }

        public void update() {
            time += 0.05;
            angle += 6;
            if (angle >= 360) angle -= 360;

            // Small sine-wave motion
            bounds.y = (int) (baseY + Math.sin(time) * 4);
        }

        public void render(Graphics2D g, Image spikeImg) {
            int cx = bounds.x + bounds.width / 2;
            int cy = bounds.y + bounds.height / 2;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(cx, cy);
            g2.rotate(Math.toRadians(angle));

            if (spikeImg != null) {
                g2.drawImage(spikeImg, -bounds.width / 2, -bounds.height / 2,
                             bounds.width, bounds.height, null);
            } else {
                g2.setColor(Color.RED);
                int[] xPoints = {-bounds.width / 2, 0, bounds.width / 2};
                int[] yPoints = {bounds.height / 2, -bounds.height / 2, bounds.height / 2};
                g2.fillPolygon(xPoints, yPoints, 3);
            }

            g2.dispose();
        }
    }

    private void init() {
        // level1 is already initialized in the constructor
        platforms = new ArrayList<>();
        spikes = new ArrayList<>();

        // Floor: Split floor with a gap in the middle
        platforms.add(level1.new Platform(0, 500, 250, 100));
        platforms.add(level1.new Platform(350, 500, 450, 100));

        // Moving spikes are positioned in the gap (250-350)
        spikes.add(new MovingSpike(level1, 260, 468, 32, 32));
        spikes.add(new MovingSpike(level1, 292, 468, 32, 32));
        spikes.add(new MovingSpike(level1, 324, 468, 32, 32)); 

        // Floating platforms
        platforms.add(level1.new Platform(150, 350, 100, 20));
        platforms.add(level1.new Platform(300, 300, 100, 20));
        platforms.add(level1.new Platform(500, 350, 100, 20));

        door = level1.new Door(700, 420, 50, 80);
    }

    @Override
    public void update() {
        completed = false;

        Rectangle playerBounds = player.getBounds();

        // Platform collision (Copied from Level 1)
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
        
        // Spike update (Unique to Level 2)
        for (Level1.Spike spike : spikes) {
            if (spike instanceof MovingSpike) {
                ((MovingSpike) spike).update();
            }
        }

        // Spike collision with small grace period (Copied from Level 1 - THE FIX)
        if (player.getY() < spawnY + 10) { 
            for (Level1.Spike spike : spikes) {
                // Use the reduced hitbox
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

        // Draw spikes
        for (Level1.Spike spike : spikes) {
            if (spike instanceof MovingSpike) {
                ((MovingSpike) spike).render(g, spikeImg);
            } else {
                if (spikeImg != null) {
                    g.drawImage(spikeImg, spike.bounds.x, spike.bounds.y,
                                 spike.bounds.width, spike.bounds.height, null);
                } else {
                    g.setColor(Color.RED);
                    int[] xPoints = {spike.bounds.x, spike.bounds.x + spike.bounds.width / 2, spike.bounds.x + spike.bounds.width};
                    int[] yPoints = {spike.bounds.y + spike.bounds.height, spike.bounds.y, spike.bounds.y + spike.bounds.height};
                    g.fillPolygon(xPoints, yPoints, 3);
                }
            }
        }

        // Draw door
        if (doorImg != null) {
            g.drawImage(doorImg, door.bounds.x, door.bounds.y,
                         door.bounds.width, door.bounds.height, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillRect(door.bounds.x, door.bounds.y, door.bounds.width, door.bounds.height);
        }
    }

    @Override
    public void reset() {
        completed = false;
    }
}