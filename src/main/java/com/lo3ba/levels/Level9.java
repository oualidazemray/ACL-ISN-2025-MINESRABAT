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
 * LEVEL 9: THE ASCENSION
 * Theme: Vertical climb to the summit
 * Difficulty: Very Hard
 * Focus: Continuous upward platforming with no safety
 */
public class Level9 extends Level {

    public Level9(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 528; // Fixed: platform at Y=570, player HEIGHT=42, spawn at 570-42=528
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        spikes.clear();
        stars.clear();

        requiredStars = 8;

        // === BASE ===
        platforms.add(new Platform(0, 570, 120, 30, PlatformType.FLOOR));
        
        // === TOWER CLIMB - LEFT SIDE ===
        platforms.add(new Platform(30, 510, 65, 32, PlatformType.STONE));
        platforms.add(new Platform(140, 460, 70, 32, PlatformType.BRICK));
        spikes.add(new Spike(160, 428, 32, 32, SpikeType.BONE));
        
        platforms.add(new Platform(60, 400, 65, 32, PlatformType.METAL));
        platforms.add(new Platform(180, 350, 70, 32, PlatformType.CRATE));
        spikes.add(new Spike(200, 318, 32, 32, SpikeType.ELECTRIC));
        
        // === MID TOWER ===
        platforms.add(new Platform(100, 290, 65, 32, PlatformType.STONE));
        platforms.add(new Platform(250, 250, 75, 32, PlatformType.BRICK));
        spikes.add(new Spike(285, 218, 32, 32, SpikeType.FIRE));  // Keep only 1
        
        // === SWITCH TO RIGHT SIDE ===
        platforms.add(new Platform(380, 220, 70, 32, PlatformType.METAL));
        platforms.add(new Platform(500, 180, 75, 32, PlatformType.CRATE));
        
        // === UPPER TOWER ===
        platforms.add(new Platform(640, 140, 65, 32, PlatformType.STONE));
        spikes.add(new Spike(655, 108, 32, 32, SpikeType.ELECTRIC));
        
        platforms.add(new Platform(780, 100, 70, 32, PlatformType.BRICK));
        platforms.add(new Platform(900, 140, 80, 32, PlatformType.METAL));
        
        // === SUMMIT PLATFORMS ===
        platforms.add(new Platform(850, 220, 70, 32, PlatformType.STONE));
        platforms.add(new Platform(780, 290, 75, 32, PlatformType.CRATE));
        platforms.add(new Platform(870, 350, 80, 32, PlatformType.BRICK));
        
        // === PEAK ===
        platforms.add(new Platform(920, 420, 80, 32, PlatformType.FLOOR));

        // === GROUND HAZARDS - REDUCED ===
        // Only 4 spikes instead of 18
        spikes.add(new Spike(200, 568, 32, 32, SpikeType.BONE));
        spikes.add(new Spike(400, 568, 32, 32, SpikeType.BONE));
        spikes.add(new Spike(600, 568, 32, 32, SpikeType.BONE));
        spikes.add(new Spike(800, 568, 32, 32, SpikeType.BONE));
        
        // === POISON BOMBS - REMOVED ===
        // All poison bombs removed for balance

        // === STARS ===
        stars.add(new Star(55, 470, 32, 32));       // Star 1 - Base
        stars.add(new Star(160, 420, 32, 32));      // Star 2 - Early climb
        stars.add(new Star(85, 360, 32, 32));       // Star 3 - Left path
        stars.add(new Star(200, 310, 32, 32));      // Star 4 - Mid climb
        stars.add(new Star(280, 210, 32, 32));      // Star 5 - Upper left
        stars.add(new Star(410, 180, 32, 32));      // Star 6 - Switch
        stars.add(new Star(810, 60, 32, 32));       // Star 7 - Peak
        stars.add(new Star(885, 310, 32, 32));      // Star 8 - Descent

        // === DOOR ===
        door = new Door(950, 340, 50, 80);
        
        // ENHANCEMENT: Checkpoints for very hard level
        checkpoints.add(new Checkpoint(240, 218, 230, 218)); // Mid tower
        checkpoints.add(new Checkpoint(490, 148, 480, 148)); // Switch point

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
