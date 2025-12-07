package com.lo3ba.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RIGHT: Test that gravity is applied correctly
 */
class PlayerGravityTest {
    
    @Test
    void testGravityApplied() {
        Player player = new Player(100, 100);
        
        assertEquals(0.0, player.getVelocityY(), 0.001, "Initial vertical velocity should be 0");
        
        player.update();
        assertEquals(0.6, player.getVelocityY(), 0.001, "Velocity should increase by gravity (0.6)");
        
        player.update();
        assertEquals(1.2, player.getVelocityY(), 0.001, "Velocity should continue increasing");
    }
}
