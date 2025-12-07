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
 * LEVEL 5: THE GAUNTLET
 * Theme: Precision platforming with dense obstacles
 * Difficulty: Medium-Hard
 * Focus: Precise jumps and careful navigation
 */
public class Level5 extends Level {

    public Level5(Player player) {
        super(player);
        spawnX = 50;
        spawnY = 478; // Fixed: platform at Y=520, player HEIGHT=42, spawn at 520-42=478
        init();
    }

    @Override
    public void init() {
        platforms.clear();
        spikes.clear();
        stars.clear();

        requiredStars = 4;  // Made easier: was 6

        // === START ===
        platforms.add(new Platform(0, 520, 140, 80, PlatformType.FLOOR));

        // === GAUNTLET SECTION - WIDER PLATFORMS FOR EASIER NAVIGATION ===
        platforms.add(new Platform(180, 470, 90, 32, PlatformType.STONE));  // Made wider
        // Removed fire spike
        
        platforms.add(new Platform(310, 420, 85, 32, PlatformType.CRATE));  // Made wider
        // Removed electric spike
        
        platforms.add(new Platform(440, 370, 90, 32, PlatformType.BRICK));  // Made wider
        spikes.add(new Spike(445, 338, 32, 32, SpikeType.BONE));  // Kept 1 spike
        // Removed second bone spike
        
        platforms.add(new Platform(550, 320, 85, 32, PlatformType.METAL));  // Made wider
        
        // === UPPER LEVEL ===
        platforms.add(new Platform(350, 240, 100, 32, PlatformType.STONE));
        platforms.add(new Platform(500, 200, 90, 32, PlatformType.CRATE));
        spikes.add(new Spike(525, 168, 32, 32, SpikeType.FIRE));
        
        // === DESCENT PATH ===
        platforms.add(new Platform(640, 260, 75, 32, PlatformType.BRICK));
        platforms.add(new Platform(760, 330, 85, 32, PlatformType.METAL));
        platforms.add(new Platform(680, 400, 90, 32, PlatformType.STONE));
        
        // === FINAL ZONE ===
        platforms.add(new Platform(820, 460, 160, 40, PlatformType.FLOOR));

        // === FLOOR HAZARDS - REDUCED ===
        // Only a few spikes on the floor instead of a full gauntlet
        spikes.add(new Spike(200, 568, 32, 32, SpikeType.NORMAL));
        spikes.add(new Spike(320, 568, 32, 32, SpikeType.NORMAL));
        spikes.add(new Spike(440, 568, 32, 32, SpikeType.NORMAL));
        
        // === POISON BOMBS ON PLATFORMS - REMOVED ===
        // All poison bombs removed to make level easier

        // === STARS ===
        stars.add(new Star(210, 430, 32, 32));      // Star 1
        stars.add(new Star(335, 380, 32, 32));      // Star 2
        stars.add(new Star(465, 330, 32, 32));      // Star 3
        stars.add(new Star(380, 200, 32, 32));      // Star 4 - Upper
        stars.add(new Star(530, 160, 32, 32));      // Star 5 - Highest
        stars.add(new Star(710, 360, 32, 32));      // Star 6 - Descent

        // === DOOR ===
        door = new Door(880, 380, 50, 80);

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
