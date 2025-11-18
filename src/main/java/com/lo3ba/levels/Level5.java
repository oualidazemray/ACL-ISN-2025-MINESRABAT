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

public class Level5 extends Level {

      public Level5(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 400;
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        spikes.clear();
        stars.clear();

        // === THEME: "THE TOWER" - Climb to the peak and return ===

        // === START PLATFORM ===
        platforms.add(new Platform(0, 450, 140, 32, PlatformType.FLOOR));

        // === ASCENDING LEFT WALL - First climb ===
        platforms.add(new Platform(50, 380, 70, 32, PlatformType.STONE));
        platforms.add(new Platform(150, 320, 80, 32, PlatformType.BRICK));
        spikes.add(new Spike(180, 288, 32, 32, SpikeType.FIRE));
        
        platforms.add(new Platform(70, 250, 70, 32, PlatformType.METAL));
        platforms.add(new Platform(170, 190, 80, 32, PlatformType.CRATE));
        spikes.add(new Spike(190, 158, 32, 32, SpikeType.ELECTRIC));

        // === CENTER TOWER - Main vertical structure ===
        platforms.add(new Platform(280, 350, 100, 32, PlatformType.STONE));
        platforms.add(new Platform(320, 270, 90, 32, PlatformType.BRICK));
        spikes.add(new Spike(345, 238, 32, 32, SpikeType.POISON));
        
        platforms.add(new Platform(280, 190, 100, 32, PlatformType.METAL));
        platforms.add(new Platform(320, 110, 90, 32, PlatformType.CRATE));

        // === PEAK PLATFORMS - Highest point ===
        platforms.add(new Platform(440, 80, 120, 32, PlatformType.STONE));
        spikes.add(new Spike(470, 48, 32, 32, SpikeType.ICE));
        spikes.add(new Spike(510, 48, 32, 32, SpikeType.BONE));

        // === RIGHT SIDE DESCENT - Way down ===
        platforms.add(new Platform(590, 150, 80, 32, PlatformType.BRICK));
        platforms.add(new Platform(700, 210, 90, 32, PlatformType.METAL));
        spikes.add(new Spike(725, 178, 32, 32, SpikeType.NORMAL));
        
        platforms.add(new Platform(620, 280, 80, 32, PlatformType.CRATE));
        platforms.add(new Platform(720, 340, 90, 32, PlatformType.STONE));
        spikes.add(new Spike(745, 308, 32, 32, SpikeType.FIRE));

        // === LOWER RIGHT - Final descent ===
        platforms.add(new Platform(650, 410, 80, 32, PlatformType.BRICK));
        platforms.add(new Platform(560, 470, 100, 32, PlatformType.METAL));
        spikes.add(new Spike(590, 438, 32, 32, SpikeType.ELECTRIC));

        // === DOOR PLATFORM ===
        platforms.add(new Platform(420, 500, 120, 32, PlatformType.FLOOR));

        // === DEADLY FLOOR - Complete coverage ===
        for (int i = 0; i < 20; i++) {
            spikes.add(new Spike(i * 50, 568, 32, 32, SpikeType.ICE));
        }

        // === ADDITIONAL HAZARDS - Strategic gaps ===
        spikes.add(new Spike(240, 538, 32, 32, SpikeType.POISON));
        spikes.add(new Spike(280, 538, 32, 32, SpikeType.POISON));
        spikes.add(new Spike(320, 538, 32, 32, SpikeType.POISON));
        
        spikes.add(new Spike(520, 538, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(560, 538, 32, 32, SpikeType.ELECTRIC));

        // === STARS - 12 stars climbing the tower ===
        stars.add(new Star(70, 340, 32, 32));       // Star 1 - First climb
        stars.add(new Star(170, 280, 32, 32));      // Star 2 - Left wall mid
        stars.add(new Star(90, 210, 32, 32));       // Star 3 - Left wall high
        stars.add(new Star(200, 150, 32, 32));      // Star 4 - Left wall peak
        stars.add(new Star(310, 310, 32, 32));      // Star 5 - Center lower
        stars.add(new Star(340, 230, 32, 32));      // Star 6 - Center mid (avoid spike)
        stars.add(new Star(310, 150, 32, 32));      // Star 7 - Center high
        stars.add(new Star(340, 70, 32, 32));       // Star 8 - Near peak
        stars.add(new Star(490, 40, 32, 32));       // Star 9 - Peak platform (avoid spikes)
        stars.add(new Star(610, 110, 32, 32));      // Star 10 - Right descent start
        stars.add(new Star(640, 240, 32, 32));      // Star 11 - Right mid
        stars.add(new Star(680, 370, 32, 32));      // Star 12 - Right lower

        // === DOOR - Requires all 12 stars ===
        door = new Door(460, 420, 50, 80);

        requiredStars = 12; // Set required stars for door to open



        setImagesForObjects();
    }

    @Override
    public void update() {
        completed = false;

        Rectangle playerBounds = player.getBounds();

        // Platform collision - only when falling
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

        // Fall off screen
        if (player.getY() > GameLoop.BASE_HEIGHT + 50) {
            player.die();
        }
    }

    @Override
    public void reset() {
        super.reset();
    }
}
