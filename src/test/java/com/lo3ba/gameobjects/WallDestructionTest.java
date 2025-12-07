package com.lo3ba.gameobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ERROR: Test wall destruction
 */
class WallDestructionTest {
    
    @Test
    void testWallDestruction() {
        BreakableWall wall = new BreakableWall(200, 200);
        
        assertFalse(wall.isDestroyed(), "Wall should start not destroyed");
        
        wall.destroy();
        
        assertTrue(wall.isDestroyed(), "Wall should be destroyed");
        
        // Destroying again should not cause errors
        assertDoesNotThrow(() -> wall.destroy(), "Multiple destroy calls should be safe");
    }
}
