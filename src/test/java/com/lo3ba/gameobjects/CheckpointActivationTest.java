package com.lo3ba.gameobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ORDERING: Test checkpoint activation sequence
 */
class CheckpointActivationTest {
    
    @Test
    void testCheckpointActivation() {
        Checkpoint checkpoint = new Checkpoint(100, 100, 50, 50);
        
        assertFalse(checkpoint.isActivated(), "Should start inactive");
        
        checkpoint.activate();
        assertTrue(checkpoint.isActivated(), "Should be active after activation");
        
        checkpoint.reset();
        assertFalse(checkpoint.isActivated(), "Should be inactive after reset");
    }
}
