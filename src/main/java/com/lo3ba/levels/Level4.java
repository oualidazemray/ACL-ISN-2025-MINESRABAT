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
 * LEVEL 4: MOVING MADNESS
 * Theme: Introduction to moving platforms
 * Difficulty: Medium
 * Focus: Timing and moving platform mechanics
 */
public class Level4 extends Level {

    public Level4(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 478; // Fixed: platform at Y=520, player HEIGHT=42, spawn at 520-42=478
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        movingPlatforms.clear();
        spikes.clear();
        stars.clear();

        requiredStars = 5;

        // === START PLATFORM ===
        platforms.add(new Platform(0, 520, 150, 80, PlatformType.FLOOR));

        // === MOVING PLATFORM SECTION ===
        // Horizontal mover 1
        movingPlatforms.add(new MovingPlatform(200, 450, 350, 450, 100, 32, 1.5, PlatformType.METAL));
        
        // Vertical mover
        movingPlatforms.add(new MovingPlatform(450, 400, 450, 280, 90, 32, 2.0, PlatformType.STONE));
        
        // Horizontal mover 2 (opposite direction)
        movingPlatforms.add(new MovingPlatform(580, 350, 720, 350, 90, 32, 1.8, PlatformType.CRATE));

        // === STATIC PLATFORMS ===
        platforms.add(new Platform(550, 480, 100, 32, PlatformType.BRICK));
        platforms.add(new Platform(850, 420, 130, 32, PlatformType.STONE));

        // === HAZARDS ===
        // Spike traps below moving platforms
        for (int i = 0; i < 10; i++) {
            SpikeType type = (i == 3 || i == 7) ? SpikeType.POISON : SpikeType.ELECTRIC;
            spikes.add(new Spike(200 + i * 50, 568, 32, 32, type));
        }
        spikes.add(new Spike(570, 516, 32, 32, SpikeType.FIRE));
        spikes.add(new Spike(602, 516, 32, 32, SpikeType.FIRE));
        // Poison bombs on static platforms
        spikes.add(new Spike(575, 448, 32, 32, SpikeType.POISON));
        spikes.add(new Spike(870, 388, 32, 32, SpikeType.POISON));

        // === STARS ===
        stars.add(new Star(270, 410, 32, 32));      // Star 1 - On horizontal mover
        stars.add(new Star(470, 240, 32, 32));      // Star 2 - Top of vertical mover
        stars.add(new Star(650, 310, 32, 32));      // Star 3 - On second horizontal mover
        stars.add(new Star(590, 440, 32, 32));      // Star 4 - Between platforms
        stars.add(new Star(850, 380, 32, 32));      // Star 5 - Final platform

        // === DOOR ===
        door = new Door(900, 340, 50, 80);

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
