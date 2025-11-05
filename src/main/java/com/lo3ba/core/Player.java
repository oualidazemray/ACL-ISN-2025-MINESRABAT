package com.lo3ba.core;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.InputStream;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player {
    private double x, y;
    private double velocityX = 0;
    private double velocityY = 0;
    
    private static final double GRAVITY = 0.6;
    private static final double JUMP_STRENGTH = -12;
    private static final double MOVE_SPEED = 5;
    private static final double MAX_FALL_SPEED = 15;
    
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;
    
    private boolean onGround = false;
    private boolean dead = false;
    private int deathTimer = 0;
    private int deaths = 0; // The internal counter field
    
    private BufferedImage sprite;
    private Clip jumpSound;
    private Clip deathSound;
    
    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        loadAssets();
    }
    
    private void loadAssets() {
        try {
            sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/textures/player.png"));
            jumpSound = loadSound("assets/sounds/jump.wav");
            deathSound = loadSound("assets/sounds/death.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Clip loadSound(String path) {
    try {
        InputStream ais = getClass().getClassLoader().getResourceAsStream(path);
        if (ais != null) {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(ais);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } else {
            System.err.println("⚠ Sound not found: " + path);
        }
    } catch (Exception e) {
        System.err.println("⚠ Could not load sound: " + path);
    }
    return null;
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
    
    public void die() {
        if (!dead) {
            dead = true;
            deaths++; // Incrementing the death counter
            deathTimer = 0;
            playSound(deathSound);
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
}
    
    public void render(Graphics2D g) {
        if (sprite != null) {
            g.drawImage(sprite, (int)x, (int)y, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect((int)x, (int)y, WIDTH, HEIGHT);
        }
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public double getVelocityY() { return velocityY; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
    public boolean isOnGround() { return onGround; }
    public boolean isDead() { return dead; }
    public int getDeathTimer() { return deathTimer; }
    
    // Corrected getter name to match Level1.java UI call
    public int getDeathCount() { return deaths; } 
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, WIDTH, HEIGHT);
    }
}