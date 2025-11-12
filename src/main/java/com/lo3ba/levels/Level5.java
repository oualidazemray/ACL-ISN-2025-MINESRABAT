package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Door;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level5 extends Level {

    public Level5(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450;
        init();
    }
    @Override
    public void init() {
        platforms.clear();
        spikes.clear();

        // Split level - two paths
        platforms.add(new Platform(0, 500, 150, 100)); // Starting Platform

        // Upper path (harder but shorter)
        platforms.add(new Platform(180, 350, 70, 15));
        platforms.add(new Platform(280, 300, 70, 15));
        platforms.add(new Platform(380, 250, 70, 15));
        spikes.add(new Spike(380, 218, 32, 32));
        platforms.add(new Platform(480, 250, 70, 15));
        platforms.add(new Platform(580, 300, 70, 15));

        // Lower path (easier but longer)
        platforms.add(new Platform(180, 450, 70, 15));
        platforms.add(new Platform(280, 450, 70, 15));
        spikes.add(new Spike(320, 418, 32, 32));
        platforms.add(new Platform(380, 450, 70, 15));
        platforms.add(new Platform(480, 450, 70, 15));
        platforms.add(new Platform(580, 400, 70, 15));

        // End platform
        platforms.add(new Platform(680, 350, 120, 150));

        // Floor spikes
        for (int i = 3; i < 12; i++) {
            spikes.add(new Spike(i * 50, 568, 32, 32));
        }

        door = new Door(720, 270, 50, 80);

        setImagesForObjects();
    }

    @Override
    public void update() {
        completed = false;
        Rectangle playerBounds = player.getBounds();
        boolean landed = false;

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
                    landed = true;
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

        // Door collision
        if (checkCollision(playerBounds, door.getBounds())) {
            completed = true;
        }

        // Fall off screen
        if (player.getY() > GameLoop.BASE_HEIGHT + 50) {
            player.die();
        }
    }

    @Override
    public void reset() {
        completed = false;
    }
}
