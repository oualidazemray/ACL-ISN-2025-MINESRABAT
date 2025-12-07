package com.lo3ba.core;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class LevelManagerTest {
    private LevelManager levelManager;
    private Player player;

    @BeforeEach
    void setup() {
        player = new Player(100, 100);
        levelManager = new LevelManager(player, 1);
    }
 
    //BOUNDARY
    @Test
    void testInitialLevel() {
        assertEquals(1, levelManager.getCurrentLevelNumber());
    }

    //RIGHT
    @Test
    void testNextLevel() {
        levelManager.nextLevel();
        assertEquals(2, levelManager.getCurrentLevelNumber());
    }

    //RANGE/BOUNDARY
    @Test
    void testLevelLooping() {
        LevelManager managerLast=new LevelManager(player, 10);

        managerLast.nextLevel();
        assertEquals(1, managerLast.getCurrentLevelNumber());
    }

    //relation inverse
    @Test
    void testPlayerPositionOnNextLevel() {
        levelManager.nextLevel();
        assertEquals(levelManager.getCurrentLevel().getSpawnX(), player.getX());
    }

    @AfterEach
    void tearDown() {
        levelManager=null;
        player=null;
    }
}