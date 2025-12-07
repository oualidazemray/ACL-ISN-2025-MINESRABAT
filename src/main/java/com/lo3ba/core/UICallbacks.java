package com.lo3ba.core;

/**
 * Callback interface for GameLoop to handle UI events.
 * Implemented by GameLoop to maintain control over game state changes.
 */
public interface UICallbacks {
    /**
     * Called when the player completes a level.
     */
    void onLevelComplete();
    
    /**
     * Called when the player requests to return to the main menu.
     */
    void onReturnToMenu();
    
    /**
     * Called when the game should stop (e.g., quitting).
     */
    void onStopGame();
    
    /**
     * Called when the player wants to repeat the current level.
     */
    void onRepeatLevel();
    
    /**
     * Called when the player wants to proceed to the next level.
     */
    void onNextLevel();
    
    /**
     * Called when the pause menu should be toggled.
     */
    void onTogglePause();
    
    /**
     * Called when the app should exit.
     */
    void onExit();
}
