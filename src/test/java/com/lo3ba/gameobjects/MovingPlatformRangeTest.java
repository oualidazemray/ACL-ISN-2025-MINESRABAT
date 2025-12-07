package com.lo3ba.gameobjects;

import com.lo3ba.gameobjects.Platform.PlatformType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RANGE: Test moving platform movement range
 */
class MovingPlatformRangeTest {
    
    @Test
    void testPlatformMovementRange() {
        MovingPlatform platform = new MovingPlatform(0, 0, 100, 0, 2.0, PlatformType.FLOOR);
        
        assertEquals(0, platform.getX(), "Should start at X=0");
        
        // Update many times
        for (int i = 0; i < 100; i++) {
            platform.update();
            int x = platform.getX();
            assertTrue(x >= 0 && x <= 100, "X should stay between 0 and 100, got: " + x);
        }
    }
}
