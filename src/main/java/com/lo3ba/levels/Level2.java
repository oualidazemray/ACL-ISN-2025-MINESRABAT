package com.lo3ba.levels;

import com.lo3ba.core.GameLoop;
import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.*;
import java.awt.*;

public class Level2 extends Level {

    public Level2(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 450;
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        spikes.clear();

        // Floor with gap
        platforms.add(new Platform(0, 500, 250, 100));
        platforms.add(new Platform(350, 500, 450, 100));

        // Gap with spikes at bottom
        spikes.add(new Spike(260, 468, 32, 32));
        spikes.add(new Spike(292, 468, 32, 32));

        // Floating platforms
        platforms.add(new Platform(150, 350, 100, 20));
        platforms.add(new Platform(300, 300, 100, 20));
        platforms.add(new Platform(500, 350, 100, 20));

        // Spikes on platforms
        spikes.add(new Spike(350, 468, 32, 32));
        spikes.add(new Spike(450, 468, 32, 32));

        // Door at the end
        door = new Door(700, 420, 50, 80);

        setImagesForObjects();
    }

    @Override
    public void update() {
        completed = false;

        Rectangle playerBounds = player.getBounds();

        // Platform collision (only when falling)
        for (Platform platform : platforms) {
            if (player.getVelocityY() > 0) {
                double playerBottom = player.getY() + Player.HEIGHT;
                double playerBottomPrev = playerBottom - player.getVelocityY();

                Rectangle platBounds = platform.getBounds();

                boolean horizontalOverlap = player.getX() + Player.WIDTH > platBounds.x &&
                                            player.getX() < platBounds.x + platBounds.width;

                if (horizontalOverlap && playerBottomPrev <= platBounds.y && playerBottom >= platBounds.y) {
                    player.setY(platBounds.y - Player.HEIGHT);
                    player.setOnGround(true);
                    break;
                }
            }
        }

        // Spike collision
        for (Spike spike : spikes) {
            if (checkCollision(playerBounds, spike.getHitbox())) {
                player.die();
                break;
            }
        }

        // Door collision
        if (door != null && checkCollision(playerBounds, door.getBounds())) {
            completed = true;
        }

        // Fall off screen
        if (player.getY() > GameLoop.BASE_HEIGHT + 50) {
            player.die();
        }
    }

    @Override
    public void render(Graphics2D g) {
        // Draw platforms
        g.setColor(new Color(100, 100, 100));
        for (Platform platform : platforms) {
            Rectangle bounds = platform.getBounds();
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.setColor(new Color(100, 100, 100));
        }

        // Draw spikes
        for (Spike spike : spikes) {
            Rectangle bounds = spike.getBounds();
            if (spikeImg != null) {
                g.drawImage(spikeImg, bounds.x, bounds.y, bounds.width, bounds.height, null);
            } else {
                g.setColor(Color.RED);
                int[] xPoints = {bounds.x, bounds.x + bounds.width / 2, bounds.x + bounds.width};
                int[] yPoints = {bounds.y + bounds.height, bounds.y, bounds.y + bounds.height};
                g.fillPolygon(xPoints, yPoints, 3);
            }
        }

        // Draw door
        if (doorImg != null) {
            g.drawImage(doorImg, door.getBounds().x, door.getBounds().y,
                        door.getBounds().width, door.getBounds().height, null);
        } else {
            g.setColor(Color.GREEN);
            Rectangle bounds = door.getBounds();
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    @Override
    public void reset() {
        completed = false;
    }
}
