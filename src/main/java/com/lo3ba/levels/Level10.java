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
 * LEVEL 10: FINAL SHOWDOWN
 * Theme: Ultimate challenge combining all mechanics
 * Difficulty: Extreme
 * Focus: Everything at once - the ultimate test
 */
public class Level10 extends Level {

    public Level10(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 498; // Fixed: platform at Y=540, player HEIGHT=42, spawn at 540-42=498
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        movingPlatforms.clear();
        spikes.clear();
        stars.clear();

        requiredStars = 10; // All stars required!

        // === ENTRANCE GAUNTLET ===
        platforms.add(new Platform(0, 540, 110, 60, PlatformType.FLOOR));
        
        platforms.add(new Platform(150, 490, 55, 32, PlatformType.STONE));
        spikes.add(new Spike(165, 458, 32, 32, SpikeType.FIRE));
        
        platforms.add(new Platform(245, 450, 60, 32, PlatformType.BRICK));
        spikes.add(new Spike(275, 418, 32, 32, SpikeType.ELECTRIC));  // Keep only 1
        
        // === MOVING PLATFORM HELL ===
        movingPlatforms.add(new MovingPlatform(340, 400, 440, 400, 65, 32, 2.5, PlatformType.METAL));
        movingPlatforms.add(new MovingPlatform(490, 340, 490, 240, 70, 32, 2.0, PlatformType.STONE));
        
        // === NARROW SPIKE PASSAGE ===
        platforms.add(new Platform(320, 280, 60, 32, PlatformType.CRATE));
        spikes.add(new Spike(350, 248, 32, 32, SpikeType.BONE));  // Keep only 1
        
        platforms.add(new Platform(420, 220, 60, 32, PlatformType.BRICK));
        spikes.add(new Spike(435, 188, 32, 32, SpikeType.FIRE));
        
        // === VERTICAL SECTION ===
        platforms.add(new Platform(540, 170, 65, 32, PlatformType.METAL));
        spikes.add(new Spike(555, 138, 32, 32, SpikeType.ELECTRIC));
        
        platforms.add(new Platform(410, 120, 60, 32, PlatformType.STONE));
        movingPlatforms.add(new MovingPlatform(510, 100, 650, 100, 70, 32, 1.8, PlatformType.CRATE));
        
        // === HIGH PASSAGE ===
        platforms.add(new Platform(700, 150, 70, 32, PlatformType.BRICK));
        spikes.add(new Spike(735, 118, 32, 32, SpikeType.FIRE));  // Keep only 1
        
        platforms.add(new Platform(820, 200, 65, 32, PlatformType.METAL));
        platforms.add(new Platform(750, 260, 60, 32, PlatformType.STONE));
        
        // === DESCENT OF DOOM ===
        platforms.add(new Platform(840, 320, 60, 32, PlatformType.CRATE));
        spikes.add(new Spike(865, 288, 32, 32, SpikeType.ELECTRIC));  // Keep only 1
        
        platforms.add(new Platform(760, 400, 70, 32, PlatformType.BRICK));
        platforms.add(new Platform(870, 460, 75, 32, PlatformType.METAL));
        
        // === FINAL PLATFORM ===
        platforms.add(new Platform (800, 520, 200, 80, PlatformType.FLOOR));

        // === DEADLY FLOOR - REDUCED ===
        // Only 5 strategic spikes for final level instead of 17
        spikes.add(new Spike(200, 568, 32, 32, SpikeType.FIRE));
        spikes.add(new Spike(400, 568, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(600, 568, 32, 32, SpikeType.FIRE));
        spikes.add(new Spike(800, 568, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(950, 568, 32, 32, SpikeType.FIRE));
        
        // === FINAL SHOWDOWN POISON BOMBS - REMOVED ===
        // All 6 poison bombs removed for fairer final level

        // === STARS (All 10 required!) ===
        stars.add(new Star(175, 450, 32, 32));      // Star 1
        stars.add(new Star(265, 410, 32, 32));      // Star 2
        stars.add(new Star(390, 360, 32, 32));      // Star 3 - Moving
        stars.add(new Star(505, 200, 32, 32));      // Star 4 - Vertical mover
        stars.add(new Star(345, 240, 32, 32));      // Star 5
        stars.add(new Star(445, 180, 32, 32));      // Star 6
        stars.add(new Star(570, 130, 32, 32));      // Star 7 - High
        stars.add(new Star(580, 60, 32, 32));       // Star 8 - Horizontal mover
        stars.add(new Star(730, 110, 32, 32));      // Star 9 - Peak
        stars.add(new Star(780, 360, 32, 32));      // Star 10 - Descent

        // === VICTORY DOOR ===
        door = new Door(900, 440, 50, 80);
        
        // ENHANCEMENT: Checkpoints for extreme difficulty
        checkpoints.add(new Checkpoint(410, 88, 400, 88)); // After moving platforms
        checkpoints.add(new Checkpoint(810, 168, 800, 168)); // Before descent

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
