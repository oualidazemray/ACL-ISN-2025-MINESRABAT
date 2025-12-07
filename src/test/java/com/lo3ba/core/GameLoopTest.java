package com.lo3ba.core;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class GameLoopTest {

    private GameLoop gameLoop;

    @BeforeEach
    void setup() {
        gameLoop=new GameLoop(1, null, null);
    }

    @Test
    void testInitialLevelBoundary() {
        assertEquals(1, gameLoop.getCurrentLevel());
    }

    @Test
    void testPlayerExistence() {
        assertNotNull(gameLoop.getPlayer());
    }

    @Test
    void testPlayerResetLogic() {
        Player player = gameLoop.getPlayer();
        player.die(); 
        gameLoop.resetPlayer();
        assertFalse(player.isDead());
    }

    @AfterEach
    void tearDown() {
        gameLoop.stop();
        gameLoop=null;
    }
}