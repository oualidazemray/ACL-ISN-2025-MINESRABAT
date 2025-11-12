package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Door;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level6 extends Level {

    public Level6(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 400; // Player starts near bottom-left area
        init();
    }
    @Override
    public void init() {
        platforms.clear();
        spikes.clear();

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

        // === OUT OF BOUNDS ===
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
