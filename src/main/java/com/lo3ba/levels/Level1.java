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

public class Level1 extends Level {

    public Level1(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450;
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        spikes.clear();
        stars.clear();

        requiredStars = 2; // 2 stars to open door

      // === MAIN FLOOR - Split with dangerous gap ===
        platforms.add(new Platform(0, 500, 320, 100, PlatformType.FLOOR));
        platforms.add(new Platform(520, 500, 480, 100, PlatformType.FLOOR));

        // === STARTING AREA - Tutorial section ===
        platforms.add(new Platform(180, 420, 100, 32, PlatformType.STONE));
        
        // === FIRST CHALLENGE - Climb up ===
        platforms.add(new Platform(320, 360, 80, 32, PlatformType.CRATE));
        platforms.add(new Platform(240, 290, 80, 32, PlatformType.METAL));
        
        // === FLOATING PLATFORMS - Gap crossing ===
        platforms.add(new Platform(380, 330, 70, 32, PlatformType.BRICK));
        platforms.add(new Platform(480, 380, 80, 32, PlatformType.METAL));
        
        // === LANDING ZONE ===
        platforms.add(new Platform(600, 420, 100, 32, PlatformType.STONE));
        
        // === FINAL PLATFORM - Door area ===
        platforms.add(new Platform(720, 350, 120, 32, PlatformType.CRATE));

        // === SPIKE HAZARDS - Gap floor ===
        for (int i = 0; i < 7; i++) {
            spikes.add(new Spike(320 + i * 32, 468, 32, 32, SpikeType.ELECTRIC));
        }
        
        // === PLATFORM HAZARDS - Adds risk to jumps ===
        spikes.add(new Spike(350, 328, 32, 32, SpikeType.FIRE));
        spikes.add(new Spike(510, 348, 32, 32, SpikeType.NORMAL));

        // === STARS - Strategic placement ===
        stars.add(new Star(210, 410, 32, 32));      // Star 1 - Easy warmup

        stars.add(new Star(750, 310, 32, 32));      // Star 4 - Near door

        // === DOOR - Requires all 4 stars ===
        door = new Door(770, 270, 50, 80);

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
        for (Spike spike : spikes) {
            Rectangle spikeHitbox = spike.getHitbox();

            if (checkCollision(playerBounds, spikeHitbox)) {
                player.die();
            }
        }

        // Star collection and door open
        checkStarCollection();
        checkDoorOpen();

        // Door collision
        if (door != null && !door.isOpen() && checkCollision(playerBounds, door.getBounds())) {
            // Door is closed, player cannot pass
            // Push player back or prevent movement
            player.setX(player.getX() - player.getVelocityX());
            player.setY(player.getY() - player.getVelocityY());
        } else if (door != null && door.isOpen() && checkCollision(playerBounds, door.getBounds())) {
            // Door is open, player can pass to next level
            completed = true;
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
