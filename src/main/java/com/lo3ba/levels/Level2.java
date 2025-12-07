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
 * LEVEL 2: SPIKE GARDENS
 * Theme: Introduction to spike hazards and timing
 * Difficulty: Easy-Medium
 * Focus: Avoiding spikes while platforming
 */
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
        stars.clear();

        requiredStars = 4;

        // === START ZONE ===
        platforms.add(new Platform(0, 500, 200, 100, PlatformType.FLOOR));
        
        // === SPIKE GARDEN PATH ===
        // Wide platforms with spike obstacles
        platforms.add(new Platform(250, 450, 150, 32, PlatformType.STONE));
        spikes.add(new Spike(290, 418, 32, 32, SpikeType.NORMAL)); // Spike on platform
        
        platforms.add(new Platform(440, 400, 140, 32, PlatformType.CRATE));
        spikes.add(new Spike(480, 368, 32, 32, SpikeType.FIRE));
        spikes.add(new Spike(512, 368, 32, 32, SpikeType.FIRE));
        
        // === ELEVATED SECTION ===
        platforms.add(new Platform(300, 320, 100, 32, PlatformType.BRICK));
        platforms.add(new Platform(450, 280, 120, 32, PlatformType.METAL));
        spikes.add(new Spike(485, 248, 32, 32, SpikeType.ELECTRIC));
        
        // === DESCENT PATH ===
        platforms.add(new Platform(620, 340, 100, 32, PlatformType.STONE));
        platforms.add(new Platform(760, 400, 120, 32, PlatformType.CRATE));
        
       // === FINAL PLATFORM ===
        platforms.add(new Platform(650, 480, 180, 32, PlatformType.FLOOR));
        
        // === FLOOR HAZARDS ===
        for (int i = 0; i < 6; i++) {
            spikes.add(new Spike(200 + i * 40, 568, 32, 32, i % 2 == 0 ? SpikeType.NORMAL : SpikeType.POISON));
        }
        
        // === POISON BOMBS ===
        spikes.add(new Spike(350, 482, 32, 32, SpikeType.POISON)); // Bomb near elevated section
        spikes.add(new Spike(590, 372, 32, 32, SpikeType.POISON)); // Bomb on descent

        // === STARS ===
        stars.add(new Star(310, 410, 32, 32));      // Star 1 - First platform
        stars.add(new Star(330, 280, 32, 32));      // Star 2 - Upper left
        stars.add(new Star(490, 240, 32, 32));      // Star 3 - High platform
        stars.add(new Star(770, 360, 32, 32));      // Star 4 - Descent path

        // === DOOR ===
        door = new Door(720, 400, 50, 80);

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
