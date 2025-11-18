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

public class Level7 extends Level {

      public Level7(Player player) {
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

    // === START AREA ===
        platforms.add(new Platform(0, 500, 150, 32, PlatformType.FLOOR)); // Starting platform

        // === MAZE-LIKE STRUCTURE ===
        platforms.add(new Platform(200, 450, 150, 32, PlatformType.CRATE));
        spikes.add(new Spike(250, 418, 32, 32, SpikeType.NORMAL));

        // Vertical wall (obstacle)
        platforms.add(new Platform(400, 200, 20, 300, PlatformType.STONE));

        // === UPPER SECTION ===
        platforms.add(new Platform(200, 250, 180, 32, PlatformType.METAL));
        spikes.add(new Spike(300, 218, 32, 32, SpikeType.FIRE));

        // === DROP DOWN SECTION ===
        platforms.add(new Platform(450, 350, 100, 32, PlatformType.BRICK));
        spikes.add(new Spike(450, 318, 32, 32, SpikeType.POISON));
        spikes.add(new Spike(518, 318, 32, 32, SpikeType.ELECTRIC));

        platforms.add(new Platform(600, 450, 100, 32, PlatformType.CRATE));

        // === FINAL PLATFORM ===
        platforms.add(new Platform(650, 500, 150, 32, PlatformType.FLOOR));

        // === CEILING SPIKES ===
        spikes.add(new Spike(100, 0, 32, 32, SpikeType.ICE));
        spikes.add(new Spike(350, 0, 32, 32, SpikeType.BONE));

        // === SCATTERED FLOOR SPIKES ===
        spikes.add(new Spike(150, 568, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(550, 568, 32, 32, SpikeType.POISON));

        // === DOOR ===
        door = new Door(730, 420, 50, 80);
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
