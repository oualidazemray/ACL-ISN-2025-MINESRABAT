package com.lo3ba.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelSelectScreen extends JPanel {
    private Font retroFont;
    private Font titleFont;
    private OnLevelSelectedListener listener;
    private List<LevelButton> levelButtons;
    private int unlockedLevels = 1;
    private Timer animationTimer;
    private float titleGlow = 0f;
    private boolean glowUp = true;
    private List<Star> stars = new ArrayList<>();
    private boolean starsEnabled = false; // Set to false to disable stars

    public interface OnLevelSelectedListener {
        void onLevelSelected(int level);
        void onBack();
    }

    public LevelSelectScreen(int unlockedLevels, OnLevelSelectedListener listener) {
        this.listener = listener;
        this.unlockedLevels = unlockedLevels;
        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));
        setFocusable(true); // allow focus so it can receive input if needed

        loadFonts();
        initStars();
        createLevelButtons();
        // Apply unlocked state to the just-created buttons
        updateUnlockedLevels();
        startAnimations();
    }

    private void loadFonts() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (is != null) {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
                retroFont = baseFont.deriveFont(16f);
                titleFont = baseFont.deriveFont(32f);
            } else {
                retroFont = new Font("Arial", Font.BOLD, 16);
                titleFont = new Font("Impact", Font.BOLD, 42);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retroFont = new Font("Arial", Font.BOLD, 16);
            titleFont = new Font("Impact", Font.BOLD, 42);
        }
    }

    private void initStars() {
        for (int i = 0; i < 50; i++) {
            stars.add(new Star());
        }
    }

    private void createLevelButtons() {
        levelButtons = new ArrayList<>();

        int buttonSize = 90;
        int spacing = 15;
        int cols = 5;
        int rows = 2;
        int totalWidth = (buttonSize * cols) + (spacing * (cols - 1));
        int startX = (1000 - totalWidth) / 2;
        int startY = 220;

        for (int i = 0; i < 10; i++) {
            int row = i / cols;
            int col = i % cols;
            int x = startX + (col * (buttonSize + spacing));
            int y = startY + (row * (buttonSize + spacing + 10));

            LevelButton btn = new LevelButton(i + 1, x, y, buttonSize, buttonSize);
            levelButtons.add(btn);

            // Add the button to this panel so it actually receives mouse events and can paint itself
            add(btn);

            // Use the captured btn variable directly in the listener to avoid ambiguity
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Defensive: ignore clicks if button not visible or not enabled
                    if (!btn.isVisible() || !btn.isEnabled()) return;

                    System.out.println("[LevelSelect] Click detected on button: " + btn.getLevel() + " unlocked=" + btn.isUnlocked());

                    if (btn.isUnlocked() && listener != null) {
                        // Ensure listener is invoked on EDT (listener likely manipulates Swing)
                        SwingUtilities.invokeLater(() -> {
                            try {
                                listener.onLevelSelected(btn.getLevel());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                }
            });
        }
    }

    public void updateUnlockedLevels(int levels) {
        this.unlockedLevels = Math.min(levels, 10);
        updateUnlockedLevels();
    }

    private void updateUnlockedLevels() {
        if (levelButtons == null) return;
        for (int i = 0; i < levelButtons.size(); i++) {
            levelButtons.get(i).setUnlocked(i < unlockedLevels);
        }
    }

    // Use superclass visibility methods to avoid recursive calls to this override
    public void show() {
        super.setVisible(true);   // call super to avoid triggering this method again
        requestFocusInWindow();
        if (animationTimer != null && !animationTimer.isRunning()) {
            animationTimer.start();
        }
    }

    public void hide() {
        super.setVisible(false);  // call super to avoid triggering this method again
        if (animationTimer != null) {
            animationTimer.stop();
        }
        // Stop all hover timers in level buttons
        if (levelButtons != null) {
            for (LevelButton btn : levelButtons) {
                if (btn != null) {
                    btn.stopHoverTimer();
                }
            }
        }
    }

    private void startAnimations() {
        animationTimer = new Timer(30, e -> {
            // Update title glow
            if (glowUp) {
                titleGlow += 0.03f;
                if (titleGlow >= 1.0f) {
                    titleGlow = 1.0f;
                    glowUp = false;
                }
            } else {
                titleGlow -= 0.03f;
                if (titleGlow <= 0.4f) {
                    titleGlow = 0.4f;
                    glowUp = true;
                }
            }

            // Update stars
            for (Star s : stars) {
                s.update();
            }

            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        // Background gradient
        GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 30),
                0, getHeight(), new Color(40, 30, 50));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw stars (optional)
        if (starsEnabled) {
            for (Star s : stars) s.draw(g2d);
        }

        // Top/Title area
        g2d.setColor(new Color(30, 20, 50, 150));
        g2d.fillRect(0, getHeight() - 80, getWidth(), 80);

        // Draw title with glow effect
        drawTitle(g2d);

        // Draw instructions
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String instructions = "✨ Select a level to begin your adventure! ✨";
        FontMetrics fm = g2d.getFontMetrics();
        int instWidth = fm.stringWidth(instructions);

        // Instruction shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString(instructions, (getWidth() - instWidth) / 2 + 2, 182);

        // Instruction text
        g2d.setColor(new Color(200, 200, 255));
        g2d.drawString(instructions, (getWidth() - instWidth) / 2, 180);

        g2d.dispose();
    }

    private void drawTitle(Graphics2D g2d) {
        String title = "SELECT LEVEL";
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = (getWidth() - titleWidth) / 2;
        int titleY = 100;

        // Glow layers
        for (int i = 10; i > 0; i--) {
            int alpha = (int) (20 * titleGlow * (i / 10.0f));
            g2d.setColor(new Color(150, 150, 255, alpha));
            g2d.drawString(title, titleX, titleY);
        }

        // Main text with gradient
        GradientPaint textGradient = new GradientPaint(
                titleX, titleY - fm.getAscent(), new Color(200, 200, 255),
                titleX, titleY, new Color(150, 100, 255)
        );
        g2d.setPaint(textGradient);
        g2d.drawString(title, titleX, titleY);

        // Text outline
        g2d.setColor(new Color(50, 30, 80));
        g2d.setStroke(new BasicStroke(2));
    }

    // Star class for background animation
    private class Star {
        private float x, y, size, alpha, twinkleSpeed;
        private float twinkle;
        private boolean twinkleUp = true;

        public Star() {
            x = (float) (Math.random() * getWidth());
            y = (float) (Math.random() * (getHeight() - 200));
            size = 1f + (float) (Math.random() * 2.5f);
            alpha = 0.2f + (float) (Math.random() * 0.8f);
            twinkleSpeed = 0.01f + (float) (Math.random() * 0.03f);
            twinkle = (float) Math.random();
        }

        public void update() {
            if (twinkleUp) {
                twinkle += twinkleSpeed;
                if (twinkle >= 1f) {
                    twinkle = 1f;
                    twinkleUp = false;
                }
            } else {
                twinkle -= twinkleSpeed;
                if (twinkle <= 0f) {
                    twinkle = 0f;
                    twinkleUp = true;
                }
            }
        }

        public void draw(Graphics2D g2d) {
            Composite c = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * twinkle));
            g2d.setColor(new Color(255, 255, 255));
            g2d.fill(new Ellipse2D.Float(x, y, size, size));
            g2d.setComposite(c);
        }
    }

    // Enhanced LevelButton class
    class LevelButton extends JPanel {
        private int level;
        private boolean unlocked;
        private boolean hovered;
        private float hoverScale = 1.0f;
        private Timer hoverTimer;

        public LevelButton(int level, int x, int y, int width, int height) {
            this.level = level;
            this.unlocked = false;
            setBounds(x, y, width, height);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            hoverTimer = new Timer(20, e -> {
                if (hovered && hoverScale < 1.1f) {
                    hoverScale += 0.02f;
                } else if (!hovered && hoverScale > 1.0f) {
                    hoverScale -= 0.02f;
                }
                repaint();
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (unlocked) {
                        hovered = true;
                        hoverTimer.start();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    hoverTimer.start();
                }
            });
        }

        public int getLevel() { return level; }
        public boolean isUnlocked() { return unlocked; }

        public void setUnlocked(boolean unlocked) {
            this.unlocked = unlocked;
            repaint();
        }

        public void stopHoverTimer() {
            if (hoverTimer != null) {
                hoverTimer.stop();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (unlocked) {
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(4, 4, w - 8, h - 8, 20, 20);

                // Button background with gradient
                GradientPaint bg = new GradientPaint(0, 0, new Color(80, 80, 120),
                        0, h, new Color(60, 60, 90));
                g2d.setPaint(bg);
                int drawW = (int)(w * hoverScale);
                int drawH = (int)(h * hoverScale);
                int dx = (w - drawW)/2;
                int dy = (h - drawH)/2;
                g2d.fillRoundRect(dx, dy, drawW, drawH, 18, 18);

                // Level number
                g2d.setFont(retroFont);
                String levelStr = String.valueOf(level);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (w - fm.stringWidth(levelStr)) / 2;
                int textY = (h + fm.getAscent()) / 2 - 5;

                // Text shadow
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.drawString(levelStr, textX + 2, textY + 2);

                // Main text
                g2d.setColor(Color.WHITE);
                g2d.drawString(levelStr, textX, textY);

                // Draw stars for completed levels
                if (level < unlockedLevels) {
                    drawCompletionStar(g2d, w / 2, h - 15);
                }
            } else {
                // Locked look
                g2d.setColor(new Color(70, 70, 80));
                g2d.fillRoundRect(4, 4, w - 8, h - 8, 18, 18);
                drawLockIcon(g2d, w/2, h/2 - 6);
            }

            g2d.dispose();
        }

        private void drawLockIcon(Graphics2D g2d, int cx, int cy) {
            g2d.setColor(new Color(150, 150, 170));
            g2d.fillOval(cx - 10, cy - 6, 20, 14);
            g2d.fillRect(cx - 8, cy + 2, 16, 12);
            g2d.setColor(new Color(90, 90, 110));
            g2d.fillRect(cx - 2, cy + 6, 4, 6);
            g2d.setColor(new Color(150, 150, 170));
            g2d.fillOval(cx - 3, cy + 2, 6, 6);
            g2d.fillRect(cx - 2, cy + 6, 4, 5);
        }

        private void drawCompletionStar(Graphics2D g2d, int cx, int cy) {
            Polygon star = new Polygon();
            for (int i = 0; i < 5; i++) {
                double ang = Math.PI/2 + i * 2*Math.PI/5;
                int rx = (int) (cx + Math.cos(ang) * 6);
                int ry = (int) (cy - Math.sin(ang) * 6);
                star.addPoint(rx, ry);
            }
            g2d.setColor(new Color(255, 215, 0));
            g2d.fill(star);
            g2d.setColor(new Color(200, 150, 0));
            g2d.draw(star);
        }
    }
}