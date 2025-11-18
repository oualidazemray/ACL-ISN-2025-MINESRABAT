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
        spawnY = 450; // Starting position
        init();
    }
    @Override
    public  void init() {
        platforms.clear();
        spikes.clear();

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

        // === PLATFORM COLLISION ===
        for (Platform platform : platforms) {
            Rectangle platBounds = platform.getBounds();

            if (player.getVelocityY() > 0) {
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

        // === FALL OUT OF MAP ===
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
