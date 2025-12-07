package com.lo3ba.core;

import com.lo3ba.util.ResourceManager;
import com.lo3ba.util.ScaleManager;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {
    private double x, y;
    private double velocityX = 0;
    private double velocityY = 0;
    
    private static final double GRAVITY = 0.6;
    private static final double JUMP_STRENGTH = -12;
    private static final double MOVE_SPEED = 5;
    private static final double MAX_FALL_SPEED = 15;
    
    public static final int WIDTH = 42;
    public static final int HEIGHT = 42;
    
    private boolean onGround = false;
    private boolean dead = false;
    private int deathTimer = 0;
    private int deaths = 0; // The internal counter field
    private int health = 100; // Player health
    private int bombCount = 0; // NEW
    private static final int MAX_BOMBS = 3; // NEW
    
    // Enhancement system reference
    private GameLoop gameLoop;
    private boolean wasOnGround = false;
    private double previousY = 0;
    
    private BufferedImage sprite;
    private Clip jumpSound;
    private Clip deathSound;
    
    // Avatar customization
    private String avatarSpriteFile = "player.png"; // Default avatar
    
    // Reused bounds rectangle to avoid per-frame allocations
    private final Rectangle bounds = new Rectangle(0, 0, WIDTH, HEIGHT);
    
    public Player(double x, double y) {
        this(x, y, "player.png", null);
    }
    
    public Player(double x, double y, GameLoop gameLoop) {
        this(x, y, "player.png", gameLoop);
    }
    
    public Player(double x, double y, String avatarSpriteFile) {
        this(x, y, avatarSpriteFile, null);
    }
    
    public Player(double x, double y, String avatarSpriteFile, GameLoop gameLoop) {
        this.x = x;
        this.y = y;
        this.avatarSpriteFile = avatarSpriteFile;
        this.gameLoop = gameLoop;
        loadAssets();
    }
    
    private void loadAssets() {
        sprite = ResourceManager.loadTexture(avatarSpriteFile);
        jumpSound = ResourceManager.loadSound("jump.wav");
        deathSound = ResourceManager.loadSound("death.wav");
    }
    
    public void setAvatarSprite(String spriteFile) {
        this.avatarSpriteFile = spriteFile;
        sprite = ResourceManager.loadTexture(spriteFile);
    }
    
    private void playSound(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    public void update() {
        if (dead) {
            deathTimer++;
            return;
        }
        
        // Track previous state for landing detection
        wasOnGround = onGround;
        previousY = y;
        
        // Apply gravity
        velocityY += GRAVITY;
        if (velocityY > MAX_FALL_SPEED) {
            velocityY = MAX_FALL_SPEED;
        }
        
        // Update position
        x += velocityX;
        y += velocityY;
        
        // Reset ground state (will be set by level collision detection)
        onGround = false;
    }
    
    /**
     * Call this after collision detection to trigger landing effects
     */
    public void checkLandingEffects() {
        if (onGround && !wasOnGround && gameLoop != null) {
            double fallDistance = y - previousY;
            if (fallDistance > 3) {
                // Create dust particles on landing
                int dustCount = (int)Math.min(12, fallDistance / 2);
                gameLoop.getParticleSystem().createDustParticles(
                    x + WIDTH/2, 
                    y + HEIGHT, 
                    dustCount
                );
                
                // Camera shake for hard landings
                if (fallDistance > 12) {
                    gameLoop.getCameraShake().shake(2f + (float)(fallDistance / 10), 6);
                }
            }
        }
    }
    
    public void moveLeft() {
        if (!dead) velocityX = -MOVE_SPEED;
    }
    
    public void moveRight() {
        if (!dead) velocityX = MOVE_SPEED;
    }
    
    public void stopMoving() {
        velocityX = 0;
    }
    
    public void jump() {
        if (onGround && !dead) {
            velocityY = JUMP_STRENGTH;
            onGround = false;
            playSound(jumpSound);
        }
    }
    
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
        if (onGround) {
            velocityY = 0;
        }
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            die();
        }
    }

    public void die() {
        if (!dead) {
            dead = true;
            deaths++; // Incrementing the death counter
            deathTimer = 0;
            playSound(deathSound);
            
            // ENHANCEMENT: Death effects
            if (gameLoop != null) {
                // Camera shake
                gameLoop.getCameraShake().shake(10f, 20);
                
                // Explosion particles
                gameLoop.getParticleSystem().createExplosionParticles(
                    x + WIDTH/2, 
                    y + HEIGHT/2, 
                    30, 
                    new Color(255, 100, 100)
                );
            }
        }
    }
    
    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        velocityX = 0;
        velocityY = 0;
        dead = false;
        deathTimer = 0;
        onGround = false;
        health = 100; // Reset health on level reset
        bombCount = 0; // Reset bombs
    }

    public void collectBomb() {
        if (bombCount < MAX_BOMBS) {
            bombCount++;
            // Sound is handled by Bomb class
        }
    }

    public boolean useBomb() {
        if (bombCount > 0) {
            bombCount--;
            return true;
        }
        return false;
    }
    
    public void render(Graphics2D g) {
        // Apply scaling transformation
        ScaleManager sm = ScaleManager.getInstance();
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.scale(sm.getScaleX(), sm.getScaleY());
        
        if (sprite != null) {
            g2d.drawImage(sprite, (int)x, (int)y, WIDTH, HEIGHT, null);
        } else {
            g2d.setColor(Color.CYAN);
            g2d.fillRect((int)x, (int)y, WIDTH, HEIGHT);
        }
        
        g2d.dispose();
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public double getVelocityY() { return velocityY; }
    public double getVelocityX() { return velocityX; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
    public boolean isOnGround() { return onGround; }
    public boolean isDead() { return dead; }
    public int getDeathTimer() { return deathTimer; }
    
    // Corrected getter name to match Level1.java UI call
    public int getDeathCount() { return deaths; }

    public int getHealth() { return health; }
    public int getBombCount() { return bombCount; } // NEW
    
    public String getAvatarSpriteFile() { return avatarSpriteFile; }

    // Reuse the bounds rectangle instead of allocating a new one each frame
    public Rectangle getBounds() {
        bounds.setBounds((int)x, (int)y, WIDTH, HEIGHT);
        return bounds;
    }
    
    public int getDeaths() {
        return deaths;
    }
    
    // ENHANCEMENT: Getter for GameLoop reference
    public GameLoop getGameLoop() {
        return gameLoop;
    }
}