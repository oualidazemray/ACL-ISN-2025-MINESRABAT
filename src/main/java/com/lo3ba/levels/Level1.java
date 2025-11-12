package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Door;

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

        // Floor
        platforms.add(new Platform(0, 500, 800, 100));

        // Steps
        platforms.add(new Platform(300, 450, 100, 20));
        platforms.add(new Platform(450, 400, 100, 20));

        // Simple spike
        spikes.add(new Spike(400, 468, 32, 32));

        // Door at the end
        door = new Door(700, 420, 50, 80);

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
            Rectangle spikeHitbox = new Rectangle(
                spike.getBounds().x + 4,
                spike.getBounds().y + 4,
                spike.getBounds().width - 8,
                spike.getBounds().height - 8
            );

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
