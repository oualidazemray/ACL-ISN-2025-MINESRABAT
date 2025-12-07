package com.lo3ba.gameobjects;

import com.lo3ba.gameobjects.Platform.PlatformType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CROSS-CHECK: Test platform position after updates
 */
class MovingPlatformPositionTest {
    
    @Test
    void testPlatformPosition() {
        MovingPlatform platform = new MovingPlatform(0, 0, 100, 0, 10.0, PlatformType.FLOOR);
        
        // After 10 updates at speed 10, should be at or near position 100
        for (int i = 0; i < 10; i++) {
            platform.update();
        }
        
        int x = platform.getX();
        assertTrue(x >= 95 && x <= 100, "After 10 updates, should be near X=100, got: " + x);
    }
}
