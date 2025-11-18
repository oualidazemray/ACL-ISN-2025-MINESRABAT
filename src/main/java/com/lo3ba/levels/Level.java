package com.lo3ba.levels;

import com.lo3ba.core.Player;
import com.lo3ba.gameobjects.Platform;
import com.lo3ba.gameobjects.Spike;
import com.lo3ba.gameobjects.Door;
import com.lo3ba.gameobjects.Star;

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
    protected List<Spike> spikes;
    protected Door door;
    protected List<Star> stars;
    protected int stuckTimer = 0;

    protected BufferedImage platformImg;
    protected BufferedImage spikeImg;
    protected BufferedImage doorClosedImg; // NEW
    protected BufferedImage doorOpenImg;   // NEW
    protected BufferedImage starImg;
   

    public Level(Player player) {
        this.player = player;
        this.platforms = new ArrayList<>();
        this.spikes = new ArrayList<>();
        this.stars = new ArrayList<>();
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
        for (Platform platform : platforms) {
            platform.render(g);
        }
        for (Spike spike : spikes) {
            spike.render(g);
        }
        for (Star star : stars) {
            star.render(g);
        }
        if (door != null) {
            door.render(g);
        }
    }

    public void reset() {
        completed = false;
        for (Star star : stars) {
            star.reset();
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

    public int getSpawnX() { return spawnX; }
    public int getSpawnY() { return spawnY; }

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
            }
        }
    }

    protected void checkDoorOpen() {
        if (getCollectedStars() >= requiredStars && door != null && !door.isOpen()) {
            door.open();
        }
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
