package com.lo3ba.gameobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * INVERSION: Test star collection and reset
 */
class StarCollectionResetTest {
    
    @Test
    void testStarCollectionAndReset() {
        Star star = new Star(100, 100, 32, 32);
        
        assertFalse(star.isCollected(), "Star should start uncollected");
        
        star.collect();
        assertTrue(star.isCollected(), "Star should be collected");
        
        star.reset();
        assertFalse(star.isCollected(), "Star should be uncollected after reset");
    }
}
