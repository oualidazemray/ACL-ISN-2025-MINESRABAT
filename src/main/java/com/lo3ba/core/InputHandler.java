package com.lo3ba.core;

import java.awt.event.KeyEvent;

public class InputHandler {
    private Player player;
    private LevelManager levelManager;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    
    public InputHandler(Player player, LevelManager levelManager) {
        this.player = player;
        this.levelManager = levelManager;
    }
    
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        switch (key) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = true;
                updateMovement();
                break;
                
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = true;
                updateMovement();
                break;
                
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                player.jump();
                break;
                
            case KeyEvent.VK_E:
                if (levelManager != null && levelManager.getCurrentLevel() != null) {
                    levelManager.getCurrentLevel().tryUseBomb();
                }
                break;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        switch (key) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = false;
                updateMovement();
                break;
                
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = false;
                updateMovement();
                break;
        }
    }
    
    // Make this public so the game loop can call it every frame to apply current input state
    public void updateMovement() {
        if (leftPressed && !rightPressed) {
            player.moveLeft();
        } else if (rightPressed && !leftPressed) {
            player.moveRight();
        } else {
            player.stopMoving();
        }
    }
}