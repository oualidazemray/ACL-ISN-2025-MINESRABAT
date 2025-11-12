package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Door;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level8 extends Level {

    public Level8(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450; // Starting position
        init();
    }
    @Override
    public  void init() {
        platforms.clear();
        spikes.clear();

        // === START PLATFORM ===
        platforms.add(new Platform(0, 500, 100, 100));

        // === PRECISION PLATFORMS ===
        platforms.add(new Platform(130, 450, 40, 15));
        platforms.add(new Platform(200, 400, 40, 15));
        platforms.add(new Platform(270, 350, 40, 15));
        platforms.add(new Platform(340, 300, 40, 15));
        platforms.add(new Platform(410, 250, 40, 15));
        platforms.add(new Platform(480, 300, 40, 15));
        platforms.add(new Platform(550, 350, 40, 15));
        platforms.add(new Platform(620, 400, 40, 15));

        // === END PLATFORM ===
        platforms.add(new Platform(690, 450, 110, 150));

        // === FLOOR SPIKES ===
        for (int i = 2; i < 14; i++) {
            spikes.add(new Spike(i * 50, 568, 32, 32));
        }

        // === PLATFORM SPIKES ===
        spikes.add(new Spike(200, 368, 32, 32));
        spikes.add(new Spike(340, 268, 32, 32));
        spikes.add(new Spike(480, 268, 32, 32));
        spikes.add(new Spike(620, 368, 32, 32));

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
