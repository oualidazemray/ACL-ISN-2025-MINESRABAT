package com.lo3ba.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * BOUNDARY: Test player dies at zero health
 */
class PlayerDiesAtZeroHealthTest {
    
    @Test
    void testPlayerDiesAtZeroHealth() {
        Player player = new Player(100, 100);
        
        player.takeDamage(100);
        
        assertEquals(0, player.getHealth(), "Health should be exactly 0");
        assertTrue(player.isDead(), "Player should be dead at 0 health");
        assertEquals(1, player.getDeathCount(), "Death count should be 1");
    }
}
