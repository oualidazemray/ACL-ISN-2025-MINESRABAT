package com.lo3ba.levels;

import com.lo3ba.core.Player;
import com.lo3ba.util.ScaleManager;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Platform.PlatformType;
import com.lo3ba.gameobjects.MovingPlatform;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Spike.SpikeType;
import com.lo3ba.gameobjects.Door;
import com.lo3ba.gameobjects.Star;

import java.awt.*;

/**
 * LEVEL 6: CRYSTAL CAVES
 * Theme: Tight spaces with moving platforms and hazards
 * Difficulty: Hard
 * Focus: Combined challenges - tight platforming + moving platforms
 */
public class Level6 extends Level {

    public Level6(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 448; // Fixed: platform at Y=490, player HEIGHT=42, spawn at 490-42=448
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        movingPlatforms.clear();
        spikes.clear();
        stars.clear();

        requiredStars = 6;

        // === START CAVE ===
        platforms.add(new Platform(0, 490, 150, 110, PlatformType.FLOOR));
        
        // === NARROW PASSAGES ===
        platforms.add(new Platform(180, 440, 80, 32, PlatformType.STONE));
        // Removed bone spike
        
        platforms.add(new Platform(300, 390, 75, 32, PlatformType.BRICK));
        spikes.add(new Spike(325, 358, 32, 32, SpikeType.ELECTRIC));  // Keep only 1

        // === MOVING PLATFORM CAVERN ===
        // Vertical mover in tight space
        movingPlatforms.add(new MovingPlatform(420, 380, 420, 260, 80, 32, 1.8, PlatformType.METAL));
        
        // === UPPER CAVERN ===
        platforms.add(new Platform(300, 220, 90, 32, PlatformType.CRATE));
        platforms.add(new Platform(440, 180, 85, 32, PlatformType.STONE));
        spikes.add(new Spike(475, 148, 32, 32, SpikeType.FIRE));  // Keep only 1
        
        // === MOVING BRIDGE ===
        movingPlatforms.add(new MovingPlatform(560, 240, 700, 240, 90, 32, 2.0, PlatformType.STONE));
        
        // === EXIT CAVERN ===
        platforms.add(new Platform(750, 300, 100, 32, PlatformType.BRICK));
        platforms.add(new Platform(700, 420, 120, 32, PlatformType.METAL));
        platforms.add(new Platform(860, 450, 140, 50, PlatformType.FLOOR));

        // === FLOOR HAZARDS - REDUCED ===
        // Only 4 spikes on floor instead of 15
        spikes.add(new Spike(200, 568, 32, 32, SpikeType.BONE));
        spikes.add(new Spike(350, 568, 32, 32, SpikeType.BONE));
        spikes.add(new Spike(500, 568, 32, 32, SpikeType.BONE));
        spikes.add(new Spike(650, 568, 32, 32, SpikeType.BONE));
        
        // === POISON BOMBS - REMOVED ===
        // All poison bombs removed for better balance

        // === STARS ===
        stars.add(new Star(210, 400, 32, 32));      // Star 1 - First jump
        stars.add(new Star(325, 350, 32, 32));      // Star 2 - Narrow passage
        stars.add(new Star(440, 140, 32, 32));      // Star 3 - High platform
        stars.add(new Star(630, 200, 32, 32));      // Star 4 - Moving bridge
        stars.add(new Star(780, 260, 32, 32));      // Star 5 - Exit cavern
        stars.add(new Star(730, 380, 32, 32));      // Star 6 - Lower exit

        // === DOOR ===
        door = new Door(920, 370, 50, 80);

        setImagesForObjects();
    }

    @Override
    public void update() {
        completed = false;
        Rectangle playerBounds = player.getBounds();

        // Update moving platforms first
        for (MovingPlatform mp : movingPlatforms) {
            mp.update();
        }

        // Platform collision - only when falling
        for (Platform platform : platforms) {
            if (player.getVelocityY() > 0) {
                double playerBottom = player.getY() + Player.HEIGHT;
                double playerBottomPrev = playerBottom - player.getVelocityY();
                
                boolean horizontalOverlap = player.getX() + Player.WIDTH > platform.getBounds().x &&
                                            player.getX() < platform.getBounds().x + platform.getBounds().width;
                
                if (horizontalOverlap &&
                    playerBottomPrev <= platform.getBounds().y &&
                    playerBottom >= platform.getBounds().y) {
                    
                    player.setY(platform.getBounds().y - Player.HEIGHT);
                    player.setOnGround(true);
                    break;
                }
            }
        }

        // Moving platform collision
        for (MovingPlatform mp : movingPlatforms) {
            if (player.getVelocityY() > 0) {
                double playerBottom = player.getY() + Player.HEIGHT;
                double playerBottomPrev = playerBottom - player.getVelocityY();
                
                boolean horizontalOverlap = player.getX() + Player.WIDTH > mp.getBounds().x &&
                                            player.getX() < mp.getBounds().x + mp.getBounds().width;
                
                if (horizontalOverlap &&
                    playerBottomPrev <= mp.getBounds().y &&
                    playerBottom >= mp.getBounds().y) {
                    
                    player.setY(mp.getBounds().y - Player.HEIGHT);
                    player.setOnGround(true);
                    
                    // Move player with platform
                    player.setX(player.getX() + mp.getDeltaX());
                    player.setY(player.getY() + mp.getDeltaY());
                    break;
                }
            }
        }

        checkSpikeCollision();
        checkStarCollection();
        checkDoorOpen();

        // Door collision
        if (door != null) {
            if (!door.isOpen() && checkCollision(playerBounds, door.getBounds())) {
                // Push player back from closed door
                player.setX(player.getX() - player.getVelocityX());
                player.setY(player.getY() - player.getVelocityY());
            } else if (door.isOpen() && checkCollision(playerBounds, door.getBounds())) {
                // Level completed when entering open door
                completed = true;
            }
        }

        // Fall off screen
        if (player.getY() > ScaleManager.BASE_HEIGHT + 50) {
            player.die();
        }
    }

    @Override
    public void reset() {
        super.reset();
    }
}
