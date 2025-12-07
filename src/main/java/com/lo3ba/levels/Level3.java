package com.lo3ba.levels;

import com.lo3ba.core.Player;
import com.lo3ba.util.ScaleManager;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Platform.PlatformType;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Spike.SpikeType;
import com.lo3ba.gameobjects.Door;
import com.lo3ba.gameobjects.Star;

import java.awt.*;

/**
 * LEVEL 3: PLATFORM PEAKS
 * Theme: Vertical climb with precise platforming
 * Difficulty: Medium
 * Focus: Vertical platforming and longer jumps
 */
public class Level3 extends Level {

    public Level3(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 500;
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        spikes.clear();
        stars.clear();

        requiredStars = 5;

        // === BASE FLOOR ===
        platforms.add(new Platform(0, 550, 180, 50, PlatformType.FLOOR));

        // === ASCENDING PLATFORMS - LEFT SIDE ===
        platforms.add(new Platform(50, 480, 80, 32, PlatformType.STONE));
        platforms.add(new Platform(180, 410, 80, 32, PlatformType.CRATE));
        platforms.add(new Platform(100, 340, 80, 32, PlatformType.BRICK));
        platforms.add(new Platform(220, 270, 90, 32, PlatformType.METAL));

        // === TOP PLATFORM ===
        platforms.add(new Platform(350, 180, 120, 32, PlatformType.STONE));
        spikes.add(new Spike(380, 148, 32, 32, SpikeType.FIRE));

        // === DESCENDING PATH - RIGHT SIDE ===
        platforms.add(new Platform(510, 230, 85, 32, PlatformType.CRATE));
        platforms.add(new Platform(640, 290, 90, 32, PlatformType.BRICK));
        platforms.add(new Platform(560, 360, 85, 32, PlatformType.STONE));
        platforms.add(new Platform(680, 420, 100, 32, PlatformType.METAL));

        // === FINAL ZONE ===
        platforms.add(new Platform(550, 500, 200, 32, PlatformType.FLOOR));

        // === HAZARDS ===
        // Floor spikes with poison bombs mixed in
        for (int i = 0; i < 8; i++) {
            SpikeType type = (i == 2 || i == 5) ? SpikeType.POISON : SpikeType.ELECTRIC;
            spikes.add(new Spike(180 + i * 35, 568, 32, 32, type));
        }
        // Platform hazards
        spikes.add(new Spike(200, 378, 32, 32, SpikeType.NORMAL));
        spikes.add(new Spike(550, 468, 32, 32, SpikeType.BONE));
        // Poison bombs on platforms
        spikes.add(new Spike(125, 308, 32, 32, SpikeType.POISON));
        spikes.add(new Spike(570, 328, 32, 32, SpikeType.POISON));

        // === STARS ===
        stars.add(new Star(60, 440, 32, 32));       // Star 1 - First climb
        stars.add(new Star(200, 370, 32, 32));      // Star 2 - Mid left
        stars.add(new Star(250, 230, 32, 32));      // Star 3 - Upper left
        stars.add(new Star(390, 140, 32, 32));      // Star 4 - Peak
        stars.add(new Star(670, 250, 32, 32));      // Star 5 - Right descent

        // === DOOR ===
        door = new Door(650, 420, 50, 80);

        setImagesForObjects();
    }

    @Override
    public void update() {
        standardUpdate();
    }

    @Override
    public void reset() {
        super.reset();
    }
}
