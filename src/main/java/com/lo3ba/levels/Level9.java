package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Platform.PlatformType;
import com.lo3ba.gameobjects.Spike.SpikeType;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Door;
import com.lo3ba.gameobjects.Star;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level9 extends Level {

    public Level9(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 50; // Platform at y=100, spawn at 50
        init();
    }
    @Override
    public  void init() {
        platforms.clear();
        spikes.clear();

        // === START PLATFORM ===
        platforms.add(new Platform(0, 100, 100, 32, PlatformType.FLOOR));

        // === PRECISION JUMPS SECTION ===
        platforms.add(new Platform(150, 150, 80, 32, PlatformType.CRATE));
        spikes.add(new Spike(150, 118, 32, 32, SpikeType.NORMAL));

        platforms.add(new Platform(245, 200, 64, 32, PlatformType.STONE));
        platforms.add(new Platform(340, 250, 80, 32, PlatformType.METAL));
        spikes.add(new Spike(340, 218, 32, 32, SpikeType.FIRE));

        platforms.add(new Platform(435, 300, 64, 32, PlatformType.BRICK));
        platforms.add(new Platform(530, 350, 80, 32, PlatformType.CRATE));
        spikes.add(new Spike(530, 318, 32, 32, SpikeType.POISON));

        platforms.add(new Platform(625, 400, 64, 32, PlatformType.STONE));

        // === FINAL PLATFORM ===
        platforms.add(new Platform(700, 450, 100, 32, PlatformType.FLOOR));

        // === DEATH FLOOR ===
        for (int i = 0; i < 16; i++) {
            spikes.add(new Spike(i * 50, 568, 32, 32, SpikeType.BONE));
        }

        // === CEILING OF DEATH ===
        for (int i = 0; i < 16; i++) {
            spikes.add(new Spike(i * 50, 0, 32, 32, SpikeType.ICE));
        }

        // === DOOR ===
        door = new Door(730, 370, 50, 80);

        setImagesForObjects();
    }

    @Override
    public void update() {
        completed = false;
        Rectangle playerBounds = player.getBounds();

        // === PLATFORM COLLISION ===
        for (Platform platform : platforms) {
            Rectangle platBounds = platform.getBounds();

            if (player.getVelocityY() > 0) { // Only when falling
                double playerBottom = player.getY() + Player.HEIGHT;
                double playerBottomPrev = playerBottom - player.getVelocityY();

                if (player.getX() + Player.WIDTH > platBounds.x &&
                    player.getX() < platBounds.x + platBounds.width) {

                    if (playerBottomPrev <= platBounds.y && playerBottom >= platBounds.y) {
                        player.setY(platBounds.y - Player.HEIGHT);
                        player.setOnGround(true);
                        break;
                    }
                }
            }
        }

        // === SPIKE COLLISION ===
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

        // === FALL OFF SCREEN ===
        if (player.getY() > GameLoop.BASE_HEIGHT + 50) {
            player.die();
        }
    }

    @Override
    public void render(Graphics2D g) {
        // === PLATFORMS ===
        g.setColor(new Color(100, 100, 100));
        for (Platform platform : platforms) {
            g.fillRect(platform.getBounds().x, platform.getBounds().y,
                       platform.getBounds().width, platform.getBounds().height);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(platform.getBounds().x, platform.getBounds().y,
                       platform.getBounds().width, platform.getBounds().height);
            g.setColor(new Color(100, 100, 100));
        }

        // === SPIKES ===
        for (Spike spike : spikes) {
            if (spikeImg != null) {
                g.drawImage(spikeImg, spike.getBounds().x, spike.getBounds().y,
                            spike.getBounds().width, spike.getBounds().height, null);
            }
        }

        // === DOOR ===
        door.render(g);
    }

    @Override
    public void reset() {
        completed = false;
    }
}
