package com.lo3ba.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RIGHT: Test that player can jump
 */
class PlayerJumpTest {
    
    @Test
    void testPlayerJump() {
        Player player = new Player(100, 100);
        player.setOnGround(true);
        
        player.jump();
        
        assertEquals(-12.0, player.getVelocityY(), 0.001, "Jump should set velocity to -12.0");
        assertFalse(player.isOnGround(), "Player should not be on ground after jumping");
    }
}
