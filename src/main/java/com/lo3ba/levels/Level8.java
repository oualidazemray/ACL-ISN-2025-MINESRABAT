package com.lo3ba.levels;

import com.lo3ba.core.Player;
import com.lo3ba.util.ScaleManager;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Platform.PlatformType;
import com.lo3ba.gameobjects.MovingPlatform;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Spike.SpikeType;
import com.lo3ba.gameobjects.Checkpoint;
import com.lo3ba.gameobjects.Door;
import com.lo3ba.gameobjects.Star;

import java.awt.*;

/**
 * LEVEL 8: LAVA FACTORY
 * Theme: Dense spike patterns with timing challenges
 * Difficulty: Very Hard
 * Focus: Complex patterns and precise timing
 */
public class Level8 extends Level {

    public Level8(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 458; // Fixed: platform at Y=500, player HEIGHT=42, spawn at 500-42=458
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        movingPlatforms.clear();
        spikes.clear();
        stars.clear();

        requiredStars = 7;

        // === ENTRANCE ===
        platforms.add(new Platform(0, 500, 130, 100, PlatformType.FLOOR));
        
        // === LAVA PIT CROSSING ===
        platforms.add(new Platform(170, 450, 60, 32, PlatformType.METAL));
        spikes.add(new Spike(245, 418, 32, 32, SpikeType.FIRE));
        
        platforms.add(new Platform(290, 420, 55, 32, PlatformType.STONE));
        spikes.add(new Spike(315, 388, 32, 32, SpikeType.FIRE));  // Keep only 1
        
        // === FACTORY MACHINERY - MOVING PLATFORMS ===
        movingPlatforms.add(new MovingPlatform(380, 390, 480, 390, 70, 32, 2.0, PlatformType.METAL));
        movingPlatforms.add(new MovingPlatform(520, 320, 520, 240, 70, 32, 1.8, PlatformType.STONE));
        
        // === UPPER FACTORY ===
        platforms.add(new Platform(360, 260, 80, 32, PlatformType.BRICK));
        spikes.add(new Spike(395, 228, 32, 32, SpikeType.ELECTRIC));  // Keep only 1
        
        platforms.add(new Platform(490, 180, 75, 32, PlatformType.CRATE));
        platforms.add(new Platform(620, 210, 80, 32, PlatformType.METAL));
        spikes.add(new Spike(640, 178, 32, 32, SpikeType.FIRE));
        
        // === FURNACE SECTION ===
        platforms.add(new Platform(730, 280, 70, 32, PlatformType.STONE));
        spikes.add(new Spike(760, 248, 32, 32, SpikeType.FIRE));  // Keep only 1
        
        platforms.add(new Platform(660, 360, 80, 32, PlatformType.BRICK));
        platforms.add(new Platform(790, 420, 75, 32, PlatformType.METAL));
        
        // === EXIT ===
        platforms.add(new Platform(880, 460, 120, 40, PlatformType.FLOOR));

        // === LAVA FLOOR - REDUCED ===
        // Only 4 spikes instead of 18
        spikes.add(new Spike(200, 568, 32, 32, SpikeType.FIRE));
        spikes.add(new Spike(400, 568, 32, 32, SpikeType.FIRE));
        spikes.add(new Spike(600, 568, 32, 32, SpikeType.FIRE));
        spikes.add(new Spike(800, 568, 32, 32, SpikeType.FIRE));
        
        // === FACTORY POISON BOMBS - REMOVED ===
        // All poison bombs removed for balance

        // === STARS ===
        stars.add(new Star(195, 410, 32, 32));      // Star 1
        stars.add(new Star(310, 380, 32, 32));      // Star 2
        stars.add(new Star(430, 350, 32, 32));      // Star 3 - On moving platform
        stars.add(new Star(385, 220, 32, 32));      // Star 4 - Upper factory
        stars.add(new Star(520, 140, 32, 32));      // Star 5 - High platform
        stars.add(new Star(650, 170, 32, 32));      // Star 6
        stars.add(new Star(685, 320, 32, 32));      // Star 7 - Descent

        // === DOOR ===
        door = new Door(930, 380, 50, 80);
        
        // ENHANCEMENT: Checkpoints for very hard level
        checkpoints.add(new Checkpoint(480, 228, 470, 148)); // Mid factory
        checkpoints.add(new Checkpoint(720, 248, 710, 248)); // Before furnace

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
        
        // Update checkpoints
        if (!checkpoints.isEmpty()) {
            updateCheckpoints();
        }

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
