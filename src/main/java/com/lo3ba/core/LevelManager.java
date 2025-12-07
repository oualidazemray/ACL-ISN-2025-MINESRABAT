package com.lo3ba.core;

import com.lo3ba.levels.*;
import com.lo3ba.util.ResourceManager;

import javax.sound.sampled.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LevelManager {
    private Player player;
    private Level[] levels;
    private int currentLevelIndex = 0;
    private Clip doorSound;
    
    public LevelManager(Player player) {
        this(player, 1); 
    }
    
    public LevelManager(Player player, int startLevel) {
        System.out.println("LevelManager constructor called with startLevel: " + startLevel);
        this.player = player;
        this.currentLevelIndex = startLevel - 1; // Convert to 0-based index
        initializeLevels();
        loadSounds();

        // Initialize player at the correct starting position
        Level startingLevel = levels[currentLevelIndex];
        player.reset(startingLevel.getSpawnX(), startingLevel.getSpawnY());
        System.out.println("LevelManager initialized with level " + startLevel);
    }
    
    private void initializeLevels() {
        levels = new Level[] {
            new Level1(player),
            new Level2(player),
            new Level3(player),
            new Level4(player),
            new Level5(player),
            new Level6(player),
            new Level7(player),
            new Level8(player),
            new Level9(player),
            new Level10(player)
        };
    }
    
    private void loadSounds() {
        doorSound = ResourceManager.loadSound("door.wav");
    }
    
    public void update() {
        Level currentLevel = levels[currentLevelIndex];
        currentLevel.update();
        
        // Auto-advancement removed. GameLoop handles this now.
    }
    
    public void nextLevel() {
        if (doorSound != null) {
            doorSound.setFramePosition(0);
            doorSound.start();
        }
        
        currentLevelIndex++;
        if (currentLevelIndex >= levels.length) {
            currentLevelIndex = 0; // Loop back to first level
        }
        
        Level nextLevel = levels[currentLevelIndex];
        player.reset(nextLevel.getSpawnX(), nextLevel.getSpawnY());
    }
    
    public void resetCurrentLevel() {
        Level currentLevel = levels[currentLevelIndex];
        currentLevel.reset();
    }
    
    public Level getCurrentLevel() {
        return levels[currentLevelIndex];
    }
    
    public int getCurrentLevelNumber() {
        return currentLevelIndex + 1;
    }
    
    public BufferedImage loadImage(String filename) {
        try {
            return ImageIO.read(getClass().getClassLoader()
                .getResourceAsStream("assets/textures/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}