package com.lo3ba.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RIGHT: Test that player moves right correctly
 */
class PlayerMovesRightTest {
    
    @Test
    void testPlayerMovesRight() {
        Player player = new Player(100, 100);
        double initialX = player.getX();
        
        player.moveRight();
        player.update();
        
        double newX = player.getX();
        assertTrue(newX > initialX, "Player should move right (X position increases)");
        assertEquals(5.0, player.getVelocityX(), 0.001, "Right velocity should be 5.0");
    }
}
