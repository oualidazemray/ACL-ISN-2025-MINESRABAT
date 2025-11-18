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

public class Level4 extends Level {

      public Level4(Player player) {
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

     
      // === THEME: "SPIKE MAZE" - Navigate through spike corridors ===

        // === START AND END SAFE ZONES ===
        platforms.add(new Platform(0, 500, 180, 32, PlatformType.FLOOR));
        platforms.add(new Platform(720, 500, 280, 32, PlatformType.FLOOR));

        // === MAIN PATH - Wide platforms with spike obstacles ===
        platforms.add(new Platform(200, 450, 120, 32, PlatformType.STONE));
        spikes.add(new Spike(240, 418, 32, 32, SpikeType.FIRE));
        
        platforms.add(new Platform(360, 400, 140, 32, PlatformType.CRATE));
        spikes.add(new Spike(390, 368, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(430, 368, 32, 32, SpikeType.POISON));

        platforms.add(new Platform(540, 350, 120, 32, PlatformType.METAL));
        spikes.add(new Spike(570, 318, 32, 32, SpikeType.NORMAL));

        // === UPPER ROUTE - Bonus star path ===
        platforms.add(new Platform(280, 280, 90, 32, PlatformType.BRICK));
        spikes.add(new Spike(300, 248, 32, 32, SpikeType.BONE));
        
        platforms.add(new Platform(420, 220, 100, 32, PlatformType.STONE));
        spikes.add(new Spike(450, 188, 32, 32, SpikeType.ICE));

        platforms.add(new Platform(560, 260, 90, 32, PlatformType.CRATE));

        // === LOWER ROUTE - Alternative path ===
        platforms.add(new Platform(180, 520, 100, 32, PlatformType.BRICK));
        platforms.add(new Platform(320, 540, 120, 32, PlatformType.METAL));
        spikes.add(new Spike(360, 508, 32, 32, SpikeType.FIRE));
        
        platforms.add(new Platform(480, 520, 100, 32, PlatformType.STONE));
        platforms.add(new Platform(620, 540, 80, 32, PlatformType.BRICK));

        // === FINAL APPROACH ===
        platforms.add(new Platform(680, 450, 100, 32, PlatformType.CRATE));
        spikes.add(new Spike(710, 418, 32, 32, SpikeType.ELECTRIC));

        // === CEILING HAZARDS - Height limit ===
        for (int i = 0; i < 10; i++) {
            spikes.add(new Spike(200 + i * 60, 0, 32, 32, SpikeType.ICE));
        }

        // === FLOOR HAZARDS - Gap dangers ===
        for (int i = 0; i < 3; i++) {
            spikes.add(new Spike(180 + i * 32, 568, 32, 32, SpikeType.POISON));
        }
        for (int i = 0; i < 4; i++) {
            spikes.add(new Spike(440 + i * 32, 568, 32, 32, SpikeType.ELECTRIC));
        }
        for (int i = 0; i < 3; i++) {
            spikes.add(new Spike(660 + i * 32, 568, 32, 32, SpikeType.BONE));
        }

        // === STARS - 10 stars, multiple routes ===
        stars.add(new Star(220, 410, 32, 32));      // Star 1 - First platform
        stars.add(new Star(270, 410, 32, 32));      // Star 2 - First platform end
        stars.add(new Star(370, 360, 32, 32));      // Star 3 - Main path (left side)
  
        stars.add(new Star(590, 220, 32, 32));      // Star 8 - Upper right
        stars.add(new Star(370, 500, 32, 32));      // Star 9 - Lower route
        stars.add(new Star(650, 500, 32, 32));      // Star 10 - Lower end

        // === DOOR - Requires all 10 stars ===
        door = new Door(820, 420, 50, 80);
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
