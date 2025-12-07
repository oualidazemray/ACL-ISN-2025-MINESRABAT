package com.lo3ba.gameobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CARDINALITY: Test multiple checkpoints
 */
class MultipleCheckpointsTest {
    
    @Test
    void testMultipleCheckpoints() {
        Checkpoint cp1 = new Checkpoint(100, 100, 50, 50);
        Checkpoint cp2 = new Checkpoint(200, 100, 150, 50);
        Checkpoint cp3 = new Checkpoint(300, 100, 250, 50);
        
        // All should start inactive
        assertFalse(cp1.isActivated(), "CP1 should be inactive");
        assertFalse(cp2.isActivated(), "CP2 should be inactive");
        assertFalse(cp3.isActivated(), "CP3 should be inactive");
        
        // Activate second checkpoint
        cp2.activate();
        
        assertFalse(cp1.isActivated(), "CP1 still inactive");
        assertTrue(cp2.isActivated(), "CP2 now active");
        assertFalse(cp3.isActivated(), "CP3 still inactive");
    }
}
