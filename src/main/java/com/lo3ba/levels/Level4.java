package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Platform.PlatformType;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Spike.SpikeType;
import com.lo3ba.gameobjects.Door;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level4 extends Level {

    public Level4(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450;
        init();
    }
    
    @Override
    public void init() {
        platforms.clear();
        spikes.clear();

        // Floor platforms
        platforms.add(new Platform(0, 500, 200, 32, PlatformType.FLOOR));
        platforms.add(new Platform(600, 500, 200, 32, PlatformType.FLOOR));

        // Narrow platforms zigzag with variety
        platforms.add(new Platform(150, 450, 80, 32, PlatformType.CRATE));
        platforms.add(new Platform(250, 400, 64, 32, PlatformType.STONE));
        platforms.add(new Platform(350, 350, 80, 32, PlatformType.METAL));
        platforms.add(new Platform(450, 300, 64, 32, PlatformType.BRICK));
        platforms.add(new Platform(550, 350, 80, 32, PlatformType.CRATE));

        // Ceiling spikes with variety
        spikes.add(new Spike(200, 0, 32, 32, SpikeType.ICE));
        spikes.add(new Spike(300, 0, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(400, 0, 32, 32, SpikeType.NORMAL));
        spikes.add(new Spike(500, 0, 32, 32, SpikeType.FIRE));

        // Platform spikes
        spikes.add(new Spike(250, 368, 32, 32, SpikeType.ICE));
        spikes.add(new Spike(450, 268, 32, 32, SpikeType.FIRE));

        // Floor spikes with different types
        spikes.add(new Spike(300, 468, 32, 32, SpikeType.POISON));
        spikes.add(new Spike(400, 468, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(500, 468, 32, 32, SpikeType.BONE));

        door = new Door(700, 420, 50, 80);

        setImagesForObjects();
    }

    @Override
    public void update() {
        completed = false;
        Rectangle playerBounds = player.getBounds();
        boolean landed = false;

        // Platform collision
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