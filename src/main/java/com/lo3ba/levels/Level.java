package com.lo3ba.levels;

import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.MovingPlatform;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Door;
import com.lo3ba.gameobjects.Star;
import com.lo3ba.gameobjects.Checkpoint;
import com.lo3ba.gameobjects.Bomb; // NEW
import com.lo3ba.gameobjects.BreakableWall; // NEW
import com.lo3ba.gameobjects.Explosion; // NEW
import com.lo3ba.util.ScaleManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Level {
    protected Player player;
    protected boolean completed = false;
    protected int spawnX, spawnY;
    protected int requiredStars = 0; // Number of stars needed to open the door

    protected List<Platform> platforms;
    protected List<MovingPlatform> movingPlatforms;
    protected List<Spike> spikes;
    protected Door door;
    protected List<Star> stars;
    protected List<Checkpoint> checkpoints; // ENHANCEMENT
    protected Checkpoint lastCheckpoint; // ENHANCEMENT
    protected List<Bomb> bombs; // NEW
    protected List<BreakableWall> breakableWalls; // NEW
    protected List<Explosion> explosions; // NEW
    protected int stuckTimer = 0;

    protected BufferedImage platformImg;
    protected BufferedImage spikeImg;
    protected BufferedImage doorClosedImg; // NEW
    protected BufferedImage doorOpenImg;   // NEW
    protected BufferedImage starImg;
   

    public Level(Player player) {
        this.player = player;
        this.platforms = new ArrayList<>();
        this.movingPlatforms = new ArrayList<>();
        this.spikes = new ArrayList<>();
        this.stars = new ArrayList<>();
        this.checkpoints = new ArrayList<>(); // ENHANCEMENT
        this.lastCheckpoint = null; // ENHANCEMENT
        this.bombs = new ArrayList<>(); // NEW
        this.breakableWalls = new ArrayList<>(); // NEW
        this.explosions = new ArrayList<>(); // NEW
        loadTextures();
    }

    protected void loadTextures() {
        try {
            platformImg = ImageIO.read(getClass().getClassLoader()
                    .getResourceAsStream("assets/textures/background.png"));
            spikeImg = ImageIO.read(getClass().getClassLoader()
                    .getResourceAsStream("assets/textures/spike.png"));
            doorClosedImg = ImageIO.read(getClass().getClassLoader()
                    .getResourceAsStream("assets/textures/doorClose.png")); // NEW
            doorOpenImg = ImageIO.read(getClass().getClassLoader()
                    .getResourceAsStream("assets/textures/doorOpen.png"));  // NEW
            starImg = ImageIO.read(getClass().getClassLoader()
                    .getResourceAsStream("assets/textures/star.png"));

        } catch (IOException e) {
            System.err.println("⚠ Could not load textures: " + e.getMessage());
        }
    }

    protected void setImagesForObjects() {
        // Set spike images
        for (Spike spike : spikes) {
            spike.setSpikeImage(spikeImg);
        }

        // Set door images
        if (door != null) {
            door.setClosedImage(doorClosedImg);
            door.setOpenImage(doorOpenImg);
        }

        // Set star images
        for (Star star : stars) {
            star.setStarImage(starImg);
        }
    }

    public abstract void init();
    public abstract void update();

    public void render(Graphics2D g) {
        // Apply scaling transformation
        ScaleManager sm = ScaleManager.getInstance();
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.scale(sm.getScaleX(), sm.getScaleY());
        
        for (Platform platform : platforms) {
            platform.render(g2d);
        }
        for (MovingPlatform mp : movingPlatforms) {
            mp.render(g2d);
        }
        for (Spike spike : spikes) {
            spike.render(g2d);
        }
        for (Star star : stars) {
            star.render(g2d);
        }
        
        // ENHANCEMENT: Render checkpoints
        for (Checkpoint cp : checkpoints) {
            cp.render(g2d);
        }
        
        if (door != null) {
            door.render(g2d);
        }
        for (Bomb bomb : bombs) {
            bomb.render(g2d);
        }
        for (BreakableWall wall : breakableWalls) {
            wall.render(g2d);
        }
        for (Explosion exp : explosions) {
            exp.render(g2d);
        }
        
        g2d.dispose();
    }

    public void reset() {
        completed = false;
        for (Star star : stars) {
            star.reset();
        }
        // ENHANCEMENT: Reset checkpoints
        for (Checkpoint cp : checkpoints) {
            cp.reset();
        }
        lastCheckpoint = null;
        // Reset bombs (re-add them if needed, but simple reset might not be enough if we remove them from list)
        // For now, Level subclasses should clear and re-add bombs in init() which is called on reset?
        // Actually Level.reset() is called by GameLoop.restartLevel() which calls init().
        // So we just need to clear lists here if init doesn't.
        // But init() clears lists. So we are good.
    }
    
    // ENHANCEMENT: Update checkpoints
    protected void updateCheckpoints() {
        Rectangle playerBounds = player.getBounds();
        for (Checkpoint cp : checkpoints) {
            cp.update();
            
            if (!cp.isActivated() && checkCollision(playerBounds, cp.getBounds())) {
                cp.activate();
                lastCheckpoint = cp;
                
                // Sparkle effect on activation
                if (player.getGameLoop() != null) {
                    player.getGameLoop().getParticleSystem().createSparkleParticles(
                        cp.getBounds().x + cp.getBounds().width/2,
                        cp.getBounds().y + cp.getBounds().height/2,
                        15
                    );
                }
            }
        }
    }
    public int getTotalStars() {
    return stars.size();
}

public int getCollectedStars() {
    int count = 0;
    for (Star star : stars) {
        if (star.isCollected()) count++;
    }
    return count;
}

    public boolean isCompleted() {
        return completed;
    }

    public boolean allStarsCollected() {
        for (Star star : stars) {
            if (!star.isCollected()) {
                return false;
            }
        }
        return true;
    }

    // ENHANCEMENT: Return checkpoint spawn if activated
    public int getSpawnX() {
        if (lastCheckpoint != null && lastCheckpoint.isActivated()) {
            return lastCheckpoint.getSpawnX();
        }
        return spawnX;
    }
    
    public int getSpawnY() {
        if (lastCheckpoint != null && lastCheckpoint.isActivated()) {
            return lastCheckpoint.getSpawnY();
        }
        return spawnY;
    }

    public int getStuckTimer() { return stuckTimer; }

    protected boolean checkCollision(Rectangle a, Rectangle b) {
        return a.intersects(b);
    }

    protected int getSpikeDamage(Spike.SpikeType type) {
        switch (type) {
            case NORMAL: return 1;
            case FIRE: return 3;
            case POISON: return 8;
            case ELECTRIC: return 4;
            case BONE: return 4;
            case ICE: return 2;
            default: return 1;
        }
    }

    protected void checkStarCollection() {
        Rectangle playerBounds = player.getBounds();
        for (Star star : stars) {
            if (!star.isCollected() && checkCollision(playerBounds, star.getBounds())) {
                star.collect();
                
                // ENHANCEMENT: Sparkle particles on collection
                if (player.getGameLoop() != null) {
                    player.getGameLoop().getParticleSystem().createSparkleParticles(
                        star.getBounds().x + star.getBounds().width/2,
                        star.getBounds().y + star.getBounds().height/2,
                        20
                    );
                }
            }
        }
    }

    protected void checkDoorOpen() {
        if (getCollectedStars() >= requiredStars && door != null && !door.isOpen()) {
            door.open();
        }
    }

    /**
     * Handles collision between player and static platforms.
     * Only processes collisions when player is falling (velocityY > 0).
     * Uses sweep collision detection to prevent tunneling through platforms.
     */
    protected void handlePlatformCollision() {
        for (Platform platform : platforms) {
            if (player.getVelocityY() > 0) {
                double playerBottom = player.getY() + Player.HEIGHT;
                double playerBottomPrev = playerBottom - player.getVelocityY();
                
                boolean horizontalOverlap = player.getX() + Player.WIDTH > platform.getBounds().x &&
                                            player.getX() < platform.getBounds().x + platform.getBounds().width;
                
                if (horizontalOverlap &&
                    playerBottomPrev <= platform.getBounds().y &&
                    playerBottom >= platform.getBounds().y) {
                    
                    player.setY(platform.getBounds().y - Player.HEIGHT);
                    player.setOnGround(true);
                    break;
                }
            }
        }
    }

    /**
     * Handles collision detection and response for door objects.
     * Closed doors block player movement; open doors complete the level.
     */
    protected void handleDoorCollision() {
        if (door == null) return;
        
        Rectangle playerBounds = player.getBounds();
        
        if (!door.isOpen() && checkCollision(playerBounds, door.getBounds())) {
            // Push player back from closed door
            player.setX(player.getX() - player.getVelocityX());
            player.setY(player.getY() - player.getVelocityY());
        } else if (door.isOpen() && checkCollision(playerBounds, door.getBounds())) {
            // Level completed when entering open door
            completed = true;
        }
    }

    /**
     * Handles fall-off-screen death condition.
     * Player dies if they fall below the screen bounds plus a small margin.
     */
    protected void handleFallOffScreen() {
        if (player.getY() > ScaleManager.BASE_HEIGHT + 50) {
            player.die();
        }
    }

    /**
     * Standard update method that handles common level mechanics.
     * This method can be called by level subclasses to handle all standard game logic:
     * - Moving platform updates
     * - Platform collision detection
     * - Spike collision
     * - Star collection
     * - Door opening/closing
     * - Checkpoint activation
     * - Bomb and wall mechanics
     * - Fall-off-screen death
     * 
     * Levels can override update() to add custom behavior or simply use this.
     */
    protected void standardUpdate() {
        completed = false;
        
        // Handle moving platforms if present
        if (!movingPlatforms.isEmpty()) {
            updateMovingPlatforms();
        }
        
        // Handle collision detection
        handlePlatformCollision();
        checkSpikeCollision();
        
        // Handle collectibles and objectives
        checkStarCollection();
        checkDoorOpen();
        handleDoorCollision();
        
        // Handle special mechanics
        if (!checkpoints.isEmpty()) {
            updateCheckpoints();
        }
        if (!bombs.isEmpty() || !breakableWalls.isEmpty()) {
            updateBombsAndWalls();
        }
        
        // Handle death conditions
        handleFallOffScreen();
    }

    public void checkSpikeCollision() {
        Rectangle playerBounds = player.getBounds();
        for (Spike spike : spikes) {
            Rectangle spikeHitbox = spike.getHitbox();
            if (checkCollision(playerBounds, spikeHitbox)) {
                player.takeDamage(getSpikeDamage(spike.getType()));
                if (player.getHealth() <= 0) {
                    player.die();
                }
            }
        }
    }

    protected void updateMovingPlatforms() {
        for (MovingPlatform mp : movingPlatforms) {
            mp.update();
            
            // Check for landing on the platform (similar to static platform logic)
            if (player.getVelocityY() > 0) {
                double playerBottom = player.getY() + Player.HEIGHT;
                double playerBottomPrev = playerBottom - player.getVelocityY();
                
                boolean horizontalOverlap = player.getX() + Player.WIDTH > mp.getBounds().x &&
                                            player.getX() < mp.getBounds().x + mp.getBounds().width;
                                            
                if (horizontalOverlap &&
                    playerBottomPrev <= mp.getBounds().y &&
                    playerBottom >= mp.getBounds().y) {
                    
                    player.setY(mp.getBounds().y - Player.HEIGHT);
                    player.setOnGround(true);
                    
                    // Move player with platform
                    player.setX(player.getX() + mp.getDeltaX());
                    player.setY(player.getY() + mp.getDeltaY());
                }
            }
            
            // Also check if player is already standing on it (for movement when not falling)
            // This handles horizontal platforms and platforms moving upward
            if (player.isOnGround()) {
                 double playerBottom = player.getY() + Player.HEIGHT;
                 boolean horizontalOverlap = player.getX() + Player.WIDTH > mp.getBounds().x &&
                                             player.getX() < mp.getBounds().x + mp.getBounds().width;
                 
                 // If player is on top (increased tolerance for better platform stickiness)
                 if (horizontalOverlap && Math.abs(playerBottom - mp.getBounds().y) < 8) {
                     player.setX(player.getX() + mp.getDeltaX());
                     player.setY(player.getY() + mp.getDeltaY());
                 }
            }
        }
    }



    public void updateBombsAndWalls() {
        Rectangle playerBounds = player.getBounds();
        
        // Collect bombs
        for (Bomb bomb : bombs) {
            if (!bomb.isCollected() && checkCollision(playerBounds, bomb.getBounds())) {
                bomb.collect();
                player.collectBomb();
            }
        }
        
        // Wall collision (prevent movement)
        for (BreakableWall wall : breakableWalls) {
            if (!wall.isDestroyed() && checkCollision(playerBounds, wall.getBounds())) {
                // Simple push back
                player.setX(player.getX() - player.getVelocityX());
                player.setY(player.getY() - player.getVelocityY());
            }
        }
        
        // Update explosions
        for (int i = 0; i < explosions.size(); i++) {
            Explosion exp = explosions.get(i);
            exp.update();
            if (exp.isFinished()) {
                explosions.remove(i);
                i--;
            }
        }
    }
    
    public void tryUseBomb() {
        if (player.useBomb()) {
            // Find wall in front of player (or near player)
            Rectangle searchArea = new Rectangle((int)player.getX() - 20, (int)player.getY() - 20, 
                                                Player.WIDTH + 40, Player.HEIGHT + 40);
            
            boolean wallDestroyed = false;
            for (BreakableWall wall : breakableWalls) {
                if (!wall.isDestroyed() && wall.getBounds().intersects(searchArea)) {
                    wall.destroy();
                    explosions.add(new Explosion(wall.getX(), wall.getY()));
                    wallDestroyed = true;
                    // Break? Or destroy all nearby? Let's destroy one.
                    break; 
                }
            }
            
            // If no wall found, maybe just spawn explosion at player?
            if (!wallDestroyed) {
                explosions.add(new Explosion((int)player.getX(), (int)player.getY()));
            }
        }
    }

    public void debugRender(Graphics2D g) {
        for (Platform p : platforms) {
            g.setColor(Color.GRAY);
            g.drawRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            Rectangle coll = p.getBounds();
            if (coll != null) {
                g.setColor(Color.YELLOW);
                g.drawRect(coll.x, coll.y, coll.width, coll.height);
            }
        }

        for (Spike s : spikes) {
            Rectangle vb = s.getBounds();
            if (vb != null) {
                g.setColor(Color.RED);
                g.drawRect(vb.x, vb.y, vb.width, vb.height);
            }
            Rectangle hb = s.getHitbox();
            if (hb != null) {
                g.setColor(Color.CYAN);
                g.drawRect(hb.x, hb.y, hb.width, hb.height);
            }
        }

        if (door != null) {
            Rectangle db = door.getBounds();
            if (db != null) {
                g.setColor(Color.GREEN);
                g.drawRect(db.x, db.y, db.width, db.height);
            }
        }
    }
}
