package com.lo3ba.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.event.KeyEvent;
import java.awt.Component;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InputHandlerTest {

    private InputHandler inputHandler;

    // Mock the Player interface/class
    @Mock
    private Player mockPlayer;

    // Helper component required to construct a KeyEvent
    private Component dummyComponent = new Component() {};

    @BeforeEach
    void setUp() {
        // Initialize the handler with the mocked Player
        inputHandler = new InputHandler(mockPlayer);
    }

    // --- Helper Methods to generate KeyEvents ---

    private KeyEvent createKeyPressedEvent(int keyCode) {
        return new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }

    private KeyEvent createKeyReleasedEvent(int keyCode) {
        return new KeyEvent(dummyComponent, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }

    // =========================================================================
    //                            KEY PRESSED TESTS
    // =========================================================================

    @Test
    @DisplayName("Pressing Left Arrow should call moveLeft()")
    void testKeyPressed_LeftArrow() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_LEFT));
        verify(mockPlayer, times(1)).moveLeft();
        verify(mockPlayer, never()).moveRight();
        verify(mockPlayer, never()).stopMoving();
    }

    @Test
    @DisplayName("Pressing 'A' should call moveLeft()")
    void testKeyPressed_A() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_A));
        verify(mockPlayer, times(1)).moveLeft();
    }

    @Test
    @DisplayName("Pressing Right Arrow should call moveRight()")
    void testKeyPressed_RightArrow() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_RIGHT));
        verify(mockPlayer, times(1)).moveRight();
        verify(mockPlayer, never()).moveLeft();
        verify(mockPlayer, never()).stopMoving();
    }

    @Test
    @DisplayName("Pressing 'D' should call moveRight()")
    void testKeyPressed_D() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_D));
        verify(mockPlayer, times(1)).moveRight();
    }

    @Test
    @DisplayName("Pressing SPACE should call jump()")
    void testKeyPressed_Space() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_SPACE));
        verify(mockPlayer, times(1)).jump();
        verify(mockPlayer, never()).moveLeft();
        verify(mockPlayer, never()).moveRight();
    }
    
    @Test
    @DisplayName("Pressing UP or 'W' should call jump()")
    void testKeyPressed_UpOrW() {
        // Test UP
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_UP));
        // Test W
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_W));
        
        // jump() should have been called twice in total
        verify(mockPlayer, times(2)).jump();
    }

    // =========================================================================
    //                            KEY RELEASED TESTS
    // =========================================================================

    @Test
    @DisplayName("Releasing Left Arrow when only Left was held should call stopMoving()")
    void testKeyReleased_LeftArrow_Stop() {
        // 1. Press Left (initial state: leftPressed=true, moveLeft() called)
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_LEFT));
        
        // 2. Clear calls to verify final state
        // (Note: Mockito keeps track of all calls, reset is often useful in complex tests)
        // For simplicity, let's verify the final state directly after the release.
        
        // 3. Release Left (final state: leftPressed=false)
        inputHandler.keyReleased(createKeyReleasedEvent(KeyEvent.VK_LEFT));
        
        // The release event should trigger updateMovement() which calls stopMoving()
        verify(mockPlayer, times(1)).stopMoving();
        verify(mockPlayer, times(1)).moveLeft(); // moveLeft was called once during keyPress
        verify(mockPlayer, never()).moveRight();
    }

    @Test
    @DisplayName("Releasing Left when Right is still held should call moveRight()")
    void testKeyReleased_Left_ContinueRight() {
        // 1. Press Right
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_RIGHT));
        // 2. Press Left
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_LEFT));
        
        // At this point, updateMovement should have been called twice (once for right, then for left), 
        // leading to two calls to moveRight, and then one call to stopMoving (as both are true).
        // The implementation calls updateMovement every time:
        // P1: R=T -> moveRight()
        // P2: L=T -> stopMoving()

        // 3. Release Left (R=T, L=F)
        inputHandler.keyReleased(createKeyReleasedEvent(KeyEvent.VK_LEFT));
        
        // Expected final call: moveRight()
        verify(mockPlayer, times(2)).moveRight(); // Called on R press, and on L release
        verify(mockPlayer, times(1)).stopMoving(); // Called on L press
        verify(mockPlayer, never()).moveLeft();
    }

    @Test
    @DisplayName("Releasing Right when Left is still held should call moveLeft()")
    void testKeyReleased_Right_ContinueLeft() {
        // 1. Press Left
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_LEFT));
        // 2. Press Right
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_RIGHT));
        
        // At this point: moveLeft(), then stopMoving()
        
        // 3. Release Right (L=T, R=F)
        inputHandler.keyReleased(createKeyReleasedEvent(KeyEvent.VK_RIGHT));
        
        // Expected final call: moveLeft()
        verify(mockPlayer, times(2)).moveLeft(); // Called on L press, and on R release
        verify(mockPlayer, times(1)).stopMoving(); // Called on R press
        verify(mockPlayer, never()).moveRight();
    }
}