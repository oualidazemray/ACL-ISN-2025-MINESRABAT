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

public class Level2 extends Level {

      public Level2(Player player) {
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

        requiredStars = 3; // 2 stars to open door

        // === START PLATFORM ===
        platforms.add(new Platform(0, 450, 150, 32, PlatformType.FLOOR));

        // === ASCENDING PATH - Left side climb ===
        platforms.add(new Platform(180, 390, 80, 32, PlatformType.CRATE));
        platforms.add(new Platform(120, 320, 70, 32, PlatformType.STONE));
        
        platforms.add(new Platform(220, 260, 80, 32, PlatformType.METAL));
        spikes.add(new Spike(250, 228, 32, 32, SpikeType.FIRE));
        
        platforms.add(new Platform(140, 200, 70, 32, PlatformType.BRICK));
        
        // === TOP SECTION - Horizontal challenge ===
        platforms.add(new Platform(240, 150, 90, 32, PlatformType.STONE));
        platforms.add(new Platform(370, 120, 80, 32, PlatformType.METAL));
        spikes.add(new Spike(400, 88, 32, 32, SpikeType.ELECTRIC));
        
        platforms.add(new Platform(490, 160, 70, 32, PlatformType.CRATE));
        platforms.add(new Platform(600, 130, 90, 32, PlatformType.BRICK));
        
        // === PEAK PLATFORM ===
        platforms.add(new Platform(720, 100, 100, 32, PlatformType.STONE));
        spikes.add(new Spike(750, 68, 32, 32, SpikeType.POISON));

        // === DESCENDING PATH - Right side ===
        platforms.add(new Platform(820, 180, 80, 32, PlatformType.METAL));
        platforms.add(new Platform(760, 250, 70, 32, PlatformType.CRATE));
        spikes.add(new Spike(760, 218, 32, 32, SpikeType.NORMAL));
        
        platforms.add(new Platform(680, 320, 80, 32, PlatformType.BRICK));
        platforms.add(new Platform(600, 380, 70, 32, PlatformType.STONE));
        
        // === FINAL DESCENT ===
        platforms.add(new Platform(520, 440, 90, 32, PlatformType.METAL));
        platforms.add(new Platform(420, 490, 100, 32, PlatformType.FLOOR));
        spikes.add(new Spike(450, 458, 32, 32, SpikeType.BONE));

        // === FLOOR SPIKES - Punishment zone ===
        for (int i = 0; i < 12; i++) {
            spikes.add(new Spike(i * 40, 568, 32, 32, SpikeType.ICE));
        }
        
        // === GAP HAZARDS ===
        spikes.add(new Spike(330, 538, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(370, 538, 32, 32, SpikeType.ELECTRIC));

        // === STARS - Strategic collection path ===
        stars.add(new Star(200, 350, 32, 32));      // Star 1 - Easy first jump
       stars.add(new Star(100, 350, 32, 32));      // Star 1 - Easy first jump
        stars.add(new Star(610, 340, 32, 32));      // Star 6 - Mid descent

        // === DOOR - Requires all 6 stars ===
        door = new Door(440, 410, 50, 80);

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
            stuckTimer++;
            if (stuckTimer >= 300) { // 5 seconds at 60 FPS
                player.die();
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
