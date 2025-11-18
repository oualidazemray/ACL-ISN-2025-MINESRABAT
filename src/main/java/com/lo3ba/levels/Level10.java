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
        // === BACKGROUND EFFECT ===
        g.setColor(new Color(80, 0, 0, 30));
        g.fillRect(0, 0, GameLoop.WIDTH, GameLoop.HEIGHT);

        // === PLATFORMS ===
        g.setColor(new Color(120, 100, 100));
        for (Platform platform : platforms) {
            g.fillRect(platform.getBounds().x, platform.getBounds().y,
                       platform.getBounds().width, platform.getBounds().height);
            g.setColor(new Color(150, 50, 50));
            g.drawRect(platform.getBounds().x, platform.getBounds().y,
                       platform.getBounds().width, platform.getBounds().height);
            g.setColor(new Color(120, 100, 100));
        }

        // === SPIKES ===
        for (Spike spike : spikes) {
            if (spikeImg != null) {
                g.drawImage(spikeImg, spike.getBounds().x, spike.getBounds().y,
                            spike.getBounds().width, spike.getBounds().height, null);
            } else {
                g.setColor(new Color(200, 0, 0));
                int[] xPoints = {spike.getBounds().x, spike.getBounds().x + spike.getBounds().width / 2, spike.getBounds().x + spike.getBounds().width};
                int[] yPoints = {spike.getBounds().y + spike.getBounds().height, spike.getBounds().y, spike.getBounds().y + spike.getBounds().height};
                g.fillPolygon(xPoints, yPoints, 3);
            }
        }

        // === DOOR ===
        door.render(g);

        // === FINAL LEVEL TITLE ===
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("FINAL LEVEL!", 350, 30);
    }

    @Override
    public void reset() {
        completed = false;
    }
}
