package com.lo3ba.effects;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Generic particle system for visual effects
 * Supports various particle types: dust, sparkle, explosion, trail
 */
public class ParticleSystem {
    private List<Particle> particles;
    private Random random;
    
    public ParticleSystem() {
        particles = new CopyOnWriteArrayList<>(); // Thread-safe list
        random = new Random();
    }
    
    /**
     * Create dust particles (landing, walking)
     */
    public void createDustParticles(double x, double y, int count) {
        for (int i = 0; i < count; i++) {
            double vx = (random.nextDouble() - 0.5) * 3;
            double vy = -random.nextDouble() * 2 - 1;
            Color color = new Color(200, 180, 150, 200);
            particles.add(new Particle(x, y, vx, vy, 3 + random.nextInt(3), 30, color, ParticleType.DUST));
        }
    }
    
    /**
     * Create sparkle particles (star collection)
     */
    public void createSparkleParticles(double x, double y, int count) {
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = random.nextDouble() * 4 + 2;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;
            Color color = new Color(255, 255, 0, 255);
            particles.add(new Particle(x, y, vx, vy, 4 + random.nextInt(3), 40, color, ParticleType.SPARKLE));
        }
    }
    
    /**
     * Create explosion particles (death)
     */
    public void createExplosionParticles(double x, double y, int count, Color baseColor) {
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = random.nextDouble() * 6 + 3;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;
            int r = Math.min(255, baseColor.getRed() + random.nextInt(50));
            int g = Math.min(255, baseColor.getGreen() + random.nextInt(50));
            int b = Math.min(255, baseColor.getBlue() + random.nextInt(50));
            Color color = new Color(r, g, b, 255);
            particles.add(new Particle(x, y, vx, vy, 5 + random.nextInt(4), 50, color, ParticleType.EXPLOSION));
        }
    }
    
    /**
     * Create trail particles (moving platforms)
     */
    public void createTrailParticle(double x, double y, Color color) {
        double vx = (random.nextDouble() - 0.5) * 0.5;
        double vy = random.nextDouble() * 0.5;
        particles.add(new Particle(x, y, vx, vy, 2 + random.nextInt(2), 20, color, ParticleType.TRAIL));
    }
    
    /**
     * Update all particles
     */
    public void update() {
        // CopyOnWriteArrayList is thread-safe, can safely remove while iterating
        particles.removeIf(p -> {
            p.update();
            return p.isDead();
        });
    }
    
    /**
     * Render all particles
     */
    public void render(Graphics2D g) {
        for (Particle p : particles) {
            p.render(g);
        }
    }
    
    /**
     * Get particle count (for debugging/performance)
     */
    public int getParticleCount() {
        return particles.size();
    }
    
    /**
     * Clear all particles
     */
    public void clear() {
        particles.clear();
    }
    
    // Inner class for individual particles
    private class Particle {
        private double x, y;
        private double vx, vy;
        private int size;
        private int lifetime;
        private int age;
        private Color color;
        private ParticleType type;
        
        public Particle(double x, double y, double vx, double vy, int size, int lifetime, Color color, ParticleType type) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.size = size;
            this.lifetime = lifetime;
            this.age = 0;
            this.color = color;
            this.type = type;
        }
        
        public void update() {
            x += vx;
            y += vy;
            
            // Apply gravity for certain particle types
            if (type == ParticleType.DUST || type == ParticleType.EXPLOSION) {
                vy += 0.2; // Gravity
            }
            
            // Air resistance
            vx *= 0.98;
            vy *= 0.98;
            
            age++;
        }
        
        public void render(Graphics2D g) {
            float alpha = 1.0f - ((float)age / lifetime);
            if (alpha < 0) alpha = 0;
            if (alpha > 1) alpha = 1;
            
            Color renderColor = new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                (int)(color.getAlpha() * alpha)
            );
            
            g.setColor(renderColor);
            
            if (type == ParticleType.SPARKLE) {
                // Draw star shape for sparkles
                int[] xPoints = {(int)x, (int)(x + size/2), (int)x, (int)(x - size/2)};
                int[] yPoints = {(int)(y - size), (int)y, (int)(y + size), (int)y};
                g.fillPolygon(xPoints, yPoints, 4);
                // Cross
                g.drawLine((int)x, (int)(y - size), (int)x, (int)(y + size));
                g.drawLine((int)(x - size), (int)y, (int)(x + size), (int)y);
            } else {
                // Draw circle for other particles
                g.fill(new Ellipse2D.Double(x - size/2, y - size/2, size, size));
            }
        }
        
        public boolean isDead() {
            return age >= lifetime;
        }
    }
    
    public enum ParticleType {
        DUST,
        SPARKLE,
        EXPLOSION,
        TRAIL
    }
}
