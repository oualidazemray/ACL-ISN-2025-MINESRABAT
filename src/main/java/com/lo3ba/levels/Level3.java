package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Platform.PlatformType;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Spike.SpikeType;
import com.lo3ba.gameobjects.Door;
import com.lo3ba.gameobjects.Star;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level3 extends Level {

      public Level3(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 400;
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        spikes.clear();
        stars.clear();

      requiredStars = 5; // 2 stars to open door

         // === THEME: "THE GAUNTLET" - Precision platforming with deadly floor ===

        // === START SAFE ZONE ===
        platforms.add(new Platform(0, 450, 140, 32, PlatformType.FLOOR));

        // === NARROW PILLAR PATH - Vertical challenge ===
        platforms.add(new Platform(170, 400, 50, 32, PlatformType.STONE));
        platforms.add(new Platform(250, 350, 50, 32, PlatformType.BRICK));
        spikes.add(new Spike(265, 318, 32, 32, SpikeType.FIRE));
        
        platforms.add(new Platform(170, 280, 50, 32, PlatformType.METAL));
        platforms.add(new Platform(250, 210, 50, 32, PlatformType.STONE));
        
        // === FLOATING ISLAND - High risk zone ===
        platforms.add(new Platform(360, 160, 100, 32, PlatformType.CRATE));
        spikes.add(new Spike(380, 128, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(420, 128, 32, 32, SpikeType.POISON));

        // === BRIDGE SECTION - Horizontal precision ===
        platforms.add(new Platform(500, 200, 60, 32, PlatformType.BRICK));
        platforms.add(new Platform(600, 180, 60, 32, PlatformType.METAL));
        spikes.add(new Spike(615, 148, 32, 32, SpikeType.NORMAL));
        
        platforms.add(new Platform(700, 220, 70, 32, PlatformType.STONE));

        // === DESCENT ZIGZAG - Controlled falling ===
        platforms.add(new Platform(800, 280, 60, 32, PlatformType.BRICK));
        spikes.add(new Spike(815, 248, 32, 32, SpikeType.BONE));
        
        platforms.add(new Platform(720, 340, 60, 32, PlatformType.CRATE));
        platforms.add(new Platform(800, 400, 70, 32, PlatformType.METAL));
        spikes.add(new Spike(820, 368, 32, 32, SpikeType.ELECTRIC));

        // === FINAL STRETCH - Landing platforms ===
        platforms.add(new Platform(680, 460, 80, 32, PlatformType.STONE));
        platforms.add(new Platform(560, 500, 100, 32, PlatformType.FLOOR));
        
        // === DOOR PLATFORM ===
        platforms.add(new Platform(420, 480, 100, 32, PlatformType.FLOOR));

        // === DEADLY FLOOR - Complete coverage ===
        for (int i = 0; i < 20; i++) {
            spikes.add(new Spike(i * 50, 568, 32, 32, SpikeType.ICE));
        }

        // === ADDITIONAL HAZARDS - Gap traps ===
        spikes.add(new Spike(320, 538, 32, 32, SpikeType.POISON));
        spikes.add(new Spike(360, 538, 32, 32, SpikeType.POISON));
        spikes.add(new Spike(640, 538, 32, 32, SpikeType.FIRE));
        spikes.add(new Spike(680, 538, 32, 32, SpikeType.FIRE));

        // === STARS - 8 stars for maximum challenge ===
        stars.add(new Star(185, 360, 32, 32));      // Star 1 - First pillar
        stars.add(new Star(185, 240, 32, 32));      // Star 2 - High pillar
        stars.add(new Star(265, 170, 32, 32));      // Star 3 - Peak pillar
        stars.add(new Star(370, 120, 32, 32));      // Star 4 - Floating island (left side, away from spikes)
    
        stars.add(new Star(600, 460, 32, 32));      // Star 8 - Final stretch

        // === DOOR - Requires all 8 stars ===
        door = new Door(450, 400, 50, 80);
        setImagesForObjects();
    }

    @Override
    public void update() {
        completed = false;

        Rectangle playerBounds = player.getBounds();

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

        // Spike collision
        checkSpikeCollision();

        // Star collection and door open
        checkStarCollection();
        checkDoorOpen();

        // Door collision
        if (door != null && !door.isOpen() && checkCollision(playerBounds, door.getBounds())) {
            // Door is closed, player cannot pass
            // Push player back or prevent movement
            player.setX(player.getX() - player.getVelocityX());
            player.setY(player.getY() - player.getVelocityY());
            stuckTimer++;
            if (stuckTimer >= 300) { // 5 seconds at 60 FPS
                // Reset level: reposition player to initial place with 0 stars collected
                player.reset(spawnX, spawnY);
                super.reset(); // Reset stars
                stuckTimer = 0;
            }
        } else if (door != null && door.isOpen() && checkCollision(playerBounds, door.getBounds())) {
            // Door is open, player can pass to next level
            completed = true;
        } else {
            stuckTimer = 0; // Reset timer if not touching closed door
        }

        // Fall off screen
        if (player.getY() > GameLoop.BASE_HEIGHT + 50) {
            player.die();
        }
    }

    @Override
    public void reset() {
        super.reset();
    }
}
