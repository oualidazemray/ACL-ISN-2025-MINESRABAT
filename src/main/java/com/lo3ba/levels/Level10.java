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

public class Level10 extends Level {

    public Level10(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450;
        init();
    }
    @Override
    public  void init() {
        platforms.clear();
        spikes.clear();

        // === START AREA ===
        platforms.add(new Platform(0, 500, 120, 32, PlatformType.FLOOR));

        // === FIRST SECTION - narrow jumps ===
        platforms.add(new Platform(150, 450, 80, 32, PlatformType.CRATE));
        spikes.add(new Spike(150, 418, 32, 32, SpikeType.NORMAL));

        platforms.add(new Platform(220, 400, 64, 32, PlatformType.STONE));

        // === SECOND SECTION - climb up ===
        platforms.add(new Platform(290, 350, 80, 32, PlatformType.METAL));
        spikes.add(new Spike(290, 318, 32, 32, SpikeType.FIRE));
        platforms.add(new Platform(360, 300, 64, 32, PlatformType.BRICK));

        platforms.add(new Platform(430, 250, 80, 32, PlatformType.CRATE));
        spikes.add(new Spike(430, 218, 32, 32, SpikeType.POISON));

        // === THIRD SECTION - spike maze ===
        platforms.add(new Platform(500, 200, 150, 32, PlatformType.STONE));
        spikes.add(new Spike(520, 168, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(590, 168, 32, 32, SpikeType.ICE));

        // === FOURTH SECTION - descend carefully ===
        platforms.add(new Platform(680, 250, 64, 32, PlatformType.METAL));
        platforms.add(new Platform(650, 300, 80, 32, PlatformType.BRICK));
        spikes.add(new Spike(650, 268, 32, 32, SpikeType.BONE));

        platforms.add(new Platform(620, 350, 64, 32, PlatformType.CRATE));
        platforms.add(new Platform(590, 400, 80, 32, PlatformType.STONE));
        spikes.add(new Spike(590, 368, 32, 32, SpikeType.FIRE));

        // === FINAL PLATFORM ===
        platforms.add(new Platform(560, 450, 240, 32, PlatformType.FLOOR));

        // === CEILING SPIKES ===
        for (int i = 0; i < 10; i++) {
            spikes.add(new Spike(i * 80, 0, 32, 32, SpikeType.ICE));
        }

        // === FLOOR SPIKES ===
        for (int i = 3; i < 11; i++) {
            spikes.add(new Spike(i * 50, 568, 32, 32, SpikeType.POISON));
        }

        // === ADDITIONAL DANGER SPIKES ===
        spikes.add(new Spike(220, 368, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(360, 268, 32, 32, SpikeType.BONE));

        // === DOOR ===
        door = new Door(730, 370, 50, 80);
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
