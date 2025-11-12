package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Door;

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
        platforms.add(new Platform(0, 100, 100, 20));

        // === PRECISION JUMPS SECTION ===
        platforms.add(new Platform(150, 150, 45, 15));
        spikes.add(new Spike(150, 118, 32, 32));

        platforms.add(new Platform(245, 200, 45, 15));
        platforms.add(new Platform(340, 250, 45, 15));
        spikes.add(new Spike(340, 218, 32, 32));

        platforms.add(new Platform(435, 300, 45, 15));
        platforms.add(new Platform(530, 350, 45, 15));
        spikes.add(new Spike(530, 318, 32, 32));

        platforms.add(new Platform(625, 400, 45, 15));

        // === FINAL PLATFORM ===
        platforms.add(new Platform(700, 450, 100, 150));

        // === DEATH FLOOR ===
        for (int i = 0; i < 16; i++) {
            spikes.add(new Spike(i * 50, 568, 32, 32));
        }

        // === CEILING OF DEATH ===
        for (int i = 0; i < 16; i++) {
            spikes.add(new Spike(i * 50, 0, 32, 32));
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
