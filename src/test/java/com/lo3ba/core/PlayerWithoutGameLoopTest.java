package com.lo3ba.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * EXISTENCE: Test player works without GameLoop
 */
class PlayerWithoutGameLoopTest {
    
    @Test
    void testPlayerWithoutGameLoop() {
        assertDoesNotThrow(() -> {
            Player player = new Player(0, 0);
            player.update();
            player.jump();
            player.moveLeft();
            player.takeDamage(10);
        }, "Player should work without GameLoop");
    }
}
