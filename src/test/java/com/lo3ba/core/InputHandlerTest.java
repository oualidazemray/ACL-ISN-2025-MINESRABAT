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

    @Mock
    private Player mockPlayer;

    private Component dummyComponent = new Component() {};

    @BeforeEach
    void setUp() {
        inputHandler = new InputHandler(mockPlayer);
    }

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
    @DisplayName("Pressing Left Arrow calls moveLeft() once")
    void shouldCallMoveLeftWhenPressingLeftArrow() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_LEFT));
        verify(mockPlayer, times(1)).moveLeft();
    }

    @Test
    @DisplayName("Pressing Left Arrow should not call moveRight()")
    void shouldNotCallMoveRightWhenPressingLeftArrow() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_LEFT));
        verify(mockPlayer, never()).moveRight();
    }

    @Test
    @DisplayName("Pressing Left Arrow should not call stopMoving()")
    void shouldNotCallStopMovingWhenPressingLeftArrow() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_LEFT));
        verify(mockPlayer, never()).stopMoving();
    }


    @Test
    @DisplayName("Pressing 'A' should call moveLeft()")
    void testKeyPressed_A() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_A));
        verify(mockPlayer, times(1)).moveLeft();
    }

    @Test
    @DisplayName("Pressing LEFT should call moveLeft() once")
    void shouldCallMoveLeftWhenLeftArrowPressed() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_LEFT));
        verify(mockPlayer, times(1)).moveLeft();
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
    }
    
    @Test
    @DisplayName("Pressing UP or 'W' should call jump()")
    void testKeyPressed_UpOrW() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_UP));
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_W));
        verify(mockPlayer, times(2)).jump();
    }

    // =========================================================================
    //                            KEY RELEASED TESTS
    // =========================================================================

    @Test
    @DisplayName("Releasing LEFT while RIGHT is held should call moveRight() again")
    void shouldMoveRightAfterReleasingLeftWhenRightHeld() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_RIGHT));
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_LEFT));
        inputHandler.keyReleased(createKeyReleasedEvent(KeyEvent.VK_LEFT));
        verify(mockPlayer, times(2)).moveRight();
    }

    @Test
    @DisplayName("Pressing LEFT while RIGHT is held should call stopMoving()")
    void shouldStopWhenLeftPressedWhileRightHeld() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_RIGHT));
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_LEFT));
        verify(mockPlayer, times(1)).stopMoving();
    }

    @Test
    @DisplayName("Releasing LEFT while RIGHT is held should never call moveLeft()")
    void shouldNotCallMoveLeftWhenLeftReleasedWhileRightHeld() {
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_RIGHT));
        inputHandler.keyPressed(createKeyPressedEvent(KeyEvent.VK_LEFT));
        inputHandler.keyReleased(createKeyReleasedEvent(KeyEvent.VK_LEFT));
        verify(mockPlayer, never()).moveLeft();
    }

}