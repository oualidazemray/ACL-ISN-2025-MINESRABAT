package com.lo3ba.gameobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CONFORMANCE: Test bomb collection
 */
class BombCollectionTest {
    
    @Test
    void testBombCollection() {
        Bomb bomb = new Bomb(100, 100);
        
        assertFalse(bomb.isCollected(), "Bomb should start uncollected");
        
        bomb.collect();
        
        assertTrue(bomb.isCollected(), "Bomb should be collected");
    }
}
