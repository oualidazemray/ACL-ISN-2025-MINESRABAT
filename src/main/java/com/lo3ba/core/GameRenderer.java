package com.lo3ba.core;

import com.lo3ba.levels.Level;
import com.lo3ba.util.ScaleManager;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Handles all rendering logic for the game.
 * Extracted from GameLoop to follow Single Responsibility Principle.
 * 
 * Responsibilities:
 * - Configure rendering hints for pixel-perfect retro graphics
 * - Render game world (background, level, player, particles)
 * - Render HUD (stats, counters)
 * - Render death screen
 * - Render debug overlay
 */
public class GameRenderer {
    private final BufferedImage backgroundImg;
    private final Font retroFont;

    /**
     * Creates a new GameRenderer.
     * 
     * @param retroFont Font to use for HUD and messages
     * @param backgroundImg Background image to render
     */
    public GameRenderer(Font retroFont, BufferedImage backgroundImg) {
        this.retroFont = retroFont;
        this.backgroundImg = backgroundImg;
    }

    /**
     * Main rendering method. Renders the entire game scene.
     * 
     * @param g Graphics context (will be converted to Graphics2D)
     * @param context Rendering context containing all game state
     */
    public void render(Graphics g, GameRenderContext context) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Configure rendering hints for pixel-perfect retro look
        configureRenderingHints(g2d);

        // Render game world
        renderBackground(g2d);
        renderGameWorld(g2d, context);

        // Render HUD and overlays
        renderHUD(g2d, context);
        renderDeathMessage(g2d, context);
        renderDebugOverlay(g2d, context);

        g2d.dispose();
    }

    /**
     * Configure Graphics2D for pixel-perfect retro rendering.
     */
    private void configureRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    }

    /**
     * Render the background image scaled to current window size.
     */
    private void renderBackground(Graphics2D g2d) {
        ScaleManager sm = ScaleManager.getInstance();
        if (backgroundImg != null) {
            g2d.drawImage(backgroundImg, 0, 0, sm.getCurrentWidth(), sm.getCurrentHeight(), null);
        }
    }

    /**
     * Render the game world (level, player, particles) with camera shake applied.
     */
    private void renderGameWorld(Graphics2D g2d, GameRenderContext context) {
        // Apply camera shake offset
        g2d.translate(context.getCameraShake().getOffsetX(), 
                     context.getCameraShake().getOffsetY());

        // Render level
        Level currentLevel = context.getCurrentLevel();
        if (currentLevel != null) {
            currentLevel.render(g2d);
        }

        // Render player
        context.getPlayer().render(g2d);

        // Render particles on top
        context.getParticleSystem().render(g2d);

        // Reset translation for UI
        g2d.translate(-context.getCameraShake().getOffsetX(), 
                     -context.getCameraShake().getOffsetY());
    }

    /**
     * Render the HUD (level info, stats, counters).
     */
    private void renderHUD(Graphics2D g2d, GameRenderContext context) {
        g2d.setFont(retroFont);
        Player player = context.getPlayer();
        LevelManager levelManager = context.getLevelManager();
        Level currentLevel = context.getCurrentLevel();

        // Level indicator with shadow
        drawTextWithShadow(g2d, "LEVEL: " + levelManager.getCurrentLevelNumber(), 
                          10, 30, new Color(255, 200, 0));

        // Deaths counter with shadow
        drawTextWithShadow(g2d, "DEATHS: " + player.getDeathCount(), 
                          10, 60, new Color(255, 100, 100));

        // Stars counter with shadow
        String starsText = "STARS: " + currentLevel.getCollectedStars() + 
                          " / " + currentLevel.getTotalStars();
        drawTextWithShadow(g2d, starsText, 10, 90, Color.YELLOW);

        // HP counter with shadow
        drawTextWithShadow(g2d, "HP: " + player.getHealth(), 10, 110, Color.GREEN);

        // Bomb counter with shadow
        drawTextWithShadow(g2d, "BOMBS: " + player.getBombCount(), 10, 140, Color.ORANGE);

        // Stuck timer (only show if stuck)
        int stuckTimer = currentLevel.getStuckTimer();
        if (stuckTimer > 0) {
            int remainingSeconds = (300 - stuckTimer) / 60 + 1;
            drawTextWithShadow(g2d, "STUCK: " + remainingSeconds + "s", 10, 120, Color.RED);
        }
    }

    /**
     * Render the death message if player is dead.
     */
    private void renderDeathMessage(Graphics2D g2d, GameRenderContext context) {
        if (!context.getPlayer().isDead()) {
            return;
        }

        Font bigFont = retroFont.deriveFont(32f);
        g2d.setFont(bigFont);
        String msg = "YOU DIED!";
        FontMetrics fm = g2d.getFontMetrics();
        int msgWidth = fm.stringWidth(msg);
        int x = context.getBaseWidth() / 2 - msgWidth / 2;
        int y = context.getBaseHeight() / 2 - 20;

        // Shadow
        g2d.setColor(Color.BLACK);
        g2d.drawString(msg, x + 3, y + 3);

        // Main text with gradient effect
        GradientPaint gp = new GradientPaint(
            x, y - 20, new Color(255, 50, 0),
            x, y + 10, new Color(200, 0, 0)
        );
        g2d.setPaint(gp);
        g2d.drawString(msg, x, y);
    }

    /**
     * Render debug overlay showing collision bounds.
     */
    private void renderDebugOverlay(Graphics2D g2d, GameRenderContext context) {
        if (!context.isDebugOverlay()) {
            return;
        }

        Level currentLevel = context.getCurrentLevel();
        if (currentLevel == null) {
            return;
        }

        // Player bounds (magenta)
        Rectangle pb = context.getPlayer().getBounds();
        g2d.setColor(Color.MAGENTA);
        g2d.drawRect(pb.x, pb.y, pb.width, pb.height);

        // Level debug shapes
        currentLevel.debugRender(g2d);

        // Debug legend
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g2d.setColor(Color.WHITE);
        g2d.drawString("DEBUG: F3 toggles overlay", 12, context.getBaseHeight() - 10);
    }

    /**
     * Helper method to draw text with a black shadow for better readability.
     * 
     * @param g2d Graphics context
     * @param text Text to draw
     * @param x X position
     * @param y Y position
     * @param color Main text color
     */
    private void drawTextWithShadow(Graphics2D g2d, String text, int x, int y, Color color) {
        // Shadow
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x + 2, y + 2);
        
        // Main text
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }
}
