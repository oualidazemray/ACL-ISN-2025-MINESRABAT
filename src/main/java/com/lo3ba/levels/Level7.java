package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Door;

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
        platforms.add(new Platform(0, 500, 150, 100)); // Starting platform

        // === MAZE-LIKE STRUCTURE ===
        platforms.add(new Platform(200, 450, 150, 20));
        spikes.add(new Spike(250, 418, 32, 32));

        // Vertical wall (obstacle)
        platforms.add(new Platform(400, 200, 20, 300));

        // === UPPER SECTION ===
        platforms.add(new Platform(200, 250, 180, 20));
        spikes.add(new Spike(300, 218, 32, 32));

        // === DROP DOWN SECTION ===
        platforms.add(new Platform(450, 350, 100, 20));
        spikes.add(new Spike(450, 318, 32, 32));
        spikes.add(new Spike(518, 318, 32, 32));

        platforms.add(new Platform(600, 450, 100, 20));

        // === FINAL PLATFORM ===
        platforms.add(new Platform(650, 500, 150, 100));

        // === SCATTERED FLOOR SPIKES ===
        spikes.add(new Spike(150, 568, 32, 32));
        spikes.add(new Spike(550, 568, 32, 32));

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

        // === DOOR COLLISION ===
        if (checkCollision(playerBounds, door.getBounds())) {
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
        if (doorImg != null) {
            g.drawImage(doorImg, door.getBounds().x, door.getBounds().y,
                        door.getBounds().width, door.getBounds().height, null);
        }
    }

    @Override
    public void reset() {
        completed = false;
    }
}
