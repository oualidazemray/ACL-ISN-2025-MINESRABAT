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

public class Level8 extends Level {

    public Level8(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 400;
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        spikes.clear();

        // === START PLATFORM ===
        platforms.add(new Platform(0, 450, 150, 32, PlatformType.FLOOR));

        // === FIRST SECTION ===
        platforms.add(new Platform(200, 400, 80, 32, PlatformType.CRATE));
        spikes.add(new Spike(260, 368, 32, 32, SpikeType.NORMAL));

        // === SECOND SECTION ===
        platforms.add(new Platform(320, 350, 64, 32, PlatformType.STONE));
        platforms.add(new Platform(440, 300, 80, 32, PlatformType.METAL));
        spikes.add(new Spike(470, 268, 32, 32, SpikeType.FIRE));

        // === THIRD SECTION ===
        platforms.add(new Platform(560, 250, 64, 32, PlatformType.BRICK));
        spikes.add(new Spike(590, 218, 32, 32, SpikeType.POISON));

        // === TOP AREA ===
        platforms.add(new Platform(680, 200, 80, 32, PlatformType.CRATE));
        platforms.add(new Platform(780, 150, 100, 32, PlatformType.STONE));

        // === DESCENDING SECTION ===
        platforms.add(new Platform(700, 280, 64, 32, PlatformType.METAL));
        spikes.add(new Spike(700, 248, 32, 32, SpikeType.ELECTRIC));

        platforms.add(new Platform(620, 360, 80, 32, PlatformType.BRICK));
        platforms.add(new Platform(540, 420, 64, 32, PlatformType.CRATE));
        spikes.add(new Spike(540, 388, 32, 32, SpikeType.BONE));

        platforms.add(new Platform(440, 470, 100, 32, PlatformType.FLOOR));
        platforms.add(new Platform(340, 520, 100, 32, PlatformType.FLOOR));

        // === FLOOR TRAPS ===
        for (int i = 0; i < 18; i++) {
            spikes.add(new Spike(i * 50, 568, 32, 32, SpikeType.ICE));
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
        if (door.isOpen() && checkCollision(playerBounds, door.getBounds())) {
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
        door.render(g);
    }

    @Override
    public void reset() {
        completed = false;
    }
}
