package com.lo3ba.levels;

import com.lo3ba.core.Player;
import com.lo3ba.util.ScaleManager;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Platform.PlatformType;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Spike.SpikeType;
import com.lo3ba.gameobjects.Checkpoint;
import com.lo3ba.gameobjects.Door;
import com.lo3ba.gameobjects.Star;

import java.awt.*;

/**
 * LEVEL 7: SKY FORTRESS
 * Theme: High platforms with long jumps
 * Difficulty: Hard
 * Focus: Long jumps and high-risk platforming
 */
public class Level7 extends Level {

    public Level7(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 448; // Fixed: platform at Y=490, player HEIGHT=42, spawn at 490-42=448
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        spikes.clear();
        stars.clear();

        requiredStars = 7;

        // === GROUND FLOOR (START) ===
        platforms.add(new Platform(0, 490, 120, 110, PlatformType.FLOOR));
        
        // === ASCENDING TO SKY FORTRESS ===
        platforms.add(new Platform(160, 420, 70, 32, PlatformType.STONE));
        platforms.add(new Platform(280, 350, 75, 32, PlatformType.BRICK));
        platforms.add(new Platform(190, 280, 70, 32, PlatformType.METAL));
        
        // === SKY FORTRESS - HIGH PLATFORMS ===
        platforms.add(new Platform(320, 200, 90, 32, PlatformType.STONE));
        platforms.add(new Platform(480, 160, 85, 32, PlatformType.CRATE));
        spikes.add(new Spike(500, 128, 32, 32, SpikeType.ELECTRIC));
        
        platforms.add(new Platform(630, 200, 80, 32, PlatformType.BRICK));
        platforms.add(new Platform(770, 240, 75, 32, PlatformType.METAL));
        
        // === FORTRESS TOWERS ===
        platforms.add(new Platform(650, 120, 70, 32, PlatformType.STONE));
        spikes.add(new Spike(680, 88, 32, 32, SpikeType.FIRE));  // Keep only 1
        
        // === DESCENT FROM SKY ===
        platforms.add(new Platform(900, 180, 80, 32, PlatformType.CRATE));
        platforms.add(new Platform(850, 280, 70, 32, PlatformType.BRICK));
        platforms.add(new Platform(900, 370, 80, 32, PlatformType.METAL));
        
        // === LANDING PLATFORM ===
        platforms.add(new Platform(820, 450, 180, 50, PlatformType.FLOOR));

        // === DEADLY FLOOR - REDUCED ===
        // Only 4 spikes instead of 20
        spikes.add(new Spike(200, 568, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(400, 568, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(600, 568, 32, 32, SpikeType.ELECTRIC));
        spikes.add(new Spike(800, 568, 32, 32, SpikeType.ELECTRIC));
        
        // === POISON BOMBS - REMOVED ===
        // All poison bombs removed for better balance

        // === STARS ===
        stars.add(new Star(190, 380, 32, 32));      // Star 1 - First climb
        stars.add(new Star(300, 310, 32, 32));      // Star 2 - Mid climb
        stars.add(new Star(220, 240, 32, 32));      // Star 3 - Upper left
        stars.add(new Star(360, 160, 32, 32));      // Star 4 - Fortress entrance
        stars.add(new Star(510, 120, 32, 32));      // Star 5 - High fortress
        stars.add(new Star(680, 80, 32, 32));       // Star 6 - Tower peak
        stars.add(new Star(930, 140, 32, 32));      // Star 7 - Descent

        // === DOOR ===
        door = new Door(880, 370, 50, 80);
        
        // ENHANCEMENT: Checkpoints for hard level
        checkpoints.add(new Checkpoint(310, 168, 300, 158)); // After first climb
        checkpoints.add(new Checkpoint(860, 208, 850, 168)); // Sky fortress section

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
