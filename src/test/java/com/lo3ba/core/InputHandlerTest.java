package com.lo3ba.core;

import org.junit.jupiter.api.*;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {
    private InputHandler inputHandler;
    private Player player;
    private LevelManager levelManager;

    @BeforeEach
    void setup() {
        player=new Player(100, 400);
        levelManager=new LevelManager(player,1);
        inputHandler=new InputHandler(player,levelManager);
    }

    @Test
    void testConflict() {
        KeyEvent left = new KeyEvent(new JPanel(),0,0,0,KeyEvent.VK_LEFT, ' ');
        KeyEvent right = new KeyEvent(new JPanel(),0,0,0,KeyEvent.VK_RIGHT, ' ');
        
        inputHandler.keyPressed(left);
        inputHandler.keyPressed(right);
        inputHandler.updateMovement();

        assertDoesNotThrow(()->inputHandler.updateMovement());
    }

    @Test
    void testBombSafeUsage() {
        KeyEvent pressE=new KeyEvent(new JPanel(),0,0,0,KeyEvent.VK_E,'e');
        assertDoesNotThrow(() -> inputHandler.keyPressed(pressE));
    }

    @AfterEach
    void tearDown() {
        inputHandler = null;
        player = null;
        levelManager = null;
    }
}