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

public class Level6 extends Level {

      public Level6(Player player) {
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
        platforms.add(new Platform(0, 450, 150, 100)); // Safe spawn platform

        // === ASCENDING SECTION ===
        platforms.add(new Platform(200, 400, 80, 15));
        spikes.add(new Spike(260, 368, 32, 32));

        platforms.add(new Platform(320, 350, 80, 15));
        platforms.add(new Platform(440, 300, 80, 15));
        spikes.add(new Spike(470, 268, 32, 32));

        // Small tricky jump
        platforms.add(new Platform(560, 250, 80, 15));
        spikes.add(new Spike(590, 218, 32, 32));

        // === TOP AREA ===
        platforms.add(new Platform(660, 200, 60, 15));
        platforms.add(new Platform(750, 150, 100, 20)); // Top ledge with reward

        // === DESCENDING SECTION ===
        platforms.add(new Platform(700, 280, 60, 15));
        spikes.add(new Spike(700, 248, 32, 32));

        platforms.add(new Platform(620, 360, 60, 15));
        platforms.add(new Platform(540, 420, 60, 15));
        spikes.add(new Spike(540, 388, 32, 32));

        platforms.add(new Platform(440, 470, 100, 20));
        platforms.add(new Platform(340, 520, 100, 20));

        // === FLOOR TRAPS ===
        for (int i = 0; i < 18; i++) {
            spikes.add(new Spike(i * 50, 568, 32, 32));
        }

        // === DOOR ===
        door = new Door(350, 440, 50, 80);
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
