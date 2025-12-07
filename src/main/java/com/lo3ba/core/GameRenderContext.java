package com.lo3ba.core;

import com.lo3ba.levels.Level;
import com.lo3ba.effects.ParticleSystem;
import com.lo3ba.effects.CameraShake;

/**
 * Context object containing all data needed for game rendering.
 * This is a value object passed to GameRenderer to avoid tight coupling.
 */
public class GameRenderContext {
    private final Player player;
    private final Level currentLevel;
    private final LevelManager levelManager;
    private final ParticleSystem particleSystem;
    private final CameraShake cameraShake;
    private final boolean debugOverlay;
    private final int baseWidth;
    private final int baseHeight;

    public GameRenderContext(Player player, Level currentLevel, LevelManager levelManager,
                            ParticleSystem particleSystem, CameraShake cameraShake,
                            boolean debugOverlay, int baseWidth, int baseHeight) {
        this.player = player;
        this.currentLevel = currentLevel;
        this.levelManager = levelManager;
        this.particleSystem = particleSystem;
        this.cameraShake = cameraShake;
        this.debugOverlay = debugOverlay;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
    }

    public Player getPlayer() {
        return player;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public ParticleSystem getParticleSystem() {
        return particleSystem;
    }

    public CameraShake getCameraShake() {
        return cameraShake;
    }

    public boolean isDebugOverlay() {
        return debugOverlay;
    }

    public int getBaseWidth() {
        return baseWidth;
    }

    public int getBaseHeight() {
        return baseHeight;
    }
}
