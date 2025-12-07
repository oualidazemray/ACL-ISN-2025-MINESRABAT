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
 * LEVEL 1: TUTORIAL VALLEY
 * Theme: Gentle introduction to basic mechanics
 * Difficulty: Easy
 * Focus: Basic movement, jumping, star collection
 */
public class Level1 extends Level {

    public Level1(Player player) {
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

        requiredStars = 3; // Collect 3 stars to open door

        // === TUTORIAL SECTION: SAFE ZONE ===
        // Main floor - safe starting area
        platforms.add(new Platform(0, 500, 400, 100, PlatformType.FLOOR));
        
        // First jump tutorial - simple step up
        platforms.add(new Platform(200, 440, 120, 32, PlatformType.STONE));
        
        // Second platform - teaches jumping distance
        platforms.add(new Platform(360, 380, 100, 32, PlatformType.CRATE));
        
        // === MID SECTION: SAFE PLATFORMING ===
        // Descending platforms
        platforms.add(new Platform(500, 420, 100, 32, PlatformType.BRICK));
        platforms.add(new Platform(640, 460, 120, 32, PlatformType.METAL));
        
        // === FINAL SECTION: LEVEL EXIT ===
        // Landing platform before door
        platforms.add(new Platform(800, 400, 150, 32, PlatformType.STONE));
        
        // === GENTLE HAZARD INTRODUCTION ===
        // A few safe spikes to show they exist (not in player's path)
        spikes.add(new Spike(420, 568, 32, 32, SpikeType.NORMAL));
        spikes.add(new Spike(452, 568, 32, 32, SpikeType.NORMAL));
        spikes.add(new Spike(484, 568, 32, 32, SpikeType.NORMAL));

        // === STAR PLACEMENT ===
        stars.add(new Star(250, 400, 32, 32));      // Star 1 - On first platform (easy)
        stars.add(new Star(390, 340, 32, 32));      // Star 2 - Above second platform (teaches jumping)
        stars.add(new Star(660, 420, 32, 32));      // Star 3 - On descending path

        // === DOOR (EXIT) ===
        door = new Door(870, 320, 50, 80);

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
