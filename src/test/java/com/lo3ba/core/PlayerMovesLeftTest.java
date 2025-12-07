package com.lo3ba.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RIGHT: Test that player moves left correctly
 */
class PlayerMovesLeftTest {
    
    @Test
    void testPlayerMovesLeft() {
        Player player = new Player(100, 100);
        double initialX = player.getX();
        
        player.moveLeft();
        player.update();
        
        double newX = player.getX();
        assertTrue(newX < initialX, "Player should move left (X position decreases)");
        assertEquals(-5.0, player.getVelocityX(), 0.001, "Left velocity should be -5.0");
    }
}
