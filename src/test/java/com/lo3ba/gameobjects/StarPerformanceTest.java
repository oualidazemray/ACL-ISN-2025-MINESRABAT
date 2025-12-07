package com.lo3ba.gameobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PERFORMANCE: Test creating many stars
 */
class StarPerformanceTest {
    
    @Test
    void testCreateManyStars() {
        long startTime = System.currentTimeMillis();
        
        Star[] stars = new Star[100];
        for (int i = 0; i < 100; i++) {
            stars[i] = new Star(i * 10, i * 10, 32, 32);
        }
        
        for (Star star : stars) {
            star.collect();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertEquals(100, stars.length, "Should have 100 stars");
        assertTrue(duration < 1000, "Creating and collecting 100 stars should be fast, took: " + duration + "ms");
    }
}
