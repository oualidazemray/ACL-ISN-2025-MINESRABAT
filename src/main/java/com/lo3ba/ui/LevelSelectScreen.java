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
        
        loadFonts();
        initStars();
        createLevelButtons();
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
            
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    LevelButton button = (LevelButton) e.getSource();
                    if (button.isUnlocked() && listener != null) {
                        listener.onLevelSelected(button.getLevel());
                    }
                }
            });
            
            add(btn);
        }
        
        updateUnlockedLevels();
    }

    public void updateUnlockedLevels(int levels) {
        this.unlockedLevels = Math.min(levels, 10);
        updateUnlockedLevels();
    }

    private void updateUnlockedLevels() {
        for (int i = 0; i < levelButtons.size(); i++) {
            levelButtons.get(i).setUnlocked(i < unlockedLevels);
        }
    }

    public void show() {
        setVisible(true);
        requestFocusInWindow();
        if (animationTimer != null && !animationTimer.isRunning()) {
            animationTimer.start();
        }
    }

    public void hide() {
        setVisible(false);
        if (animationTimer != null) {
            animationTimer.stop();
        }
        // Stop all hover timers in level buttons
        for (LevelButton btn : levelButtons) {
            if (btn != null) {
                btn.stopHoverTimer();
            }
        }
    }

    private void startAnimations() {
        animationTimer = new Timer(40, e -> {
            // Update stars only if enabled
            if (starsEnabled) {
                for (Star star : stars) {
                    star.update();
                }
            }

            // Update title glow
            if (glowUp) {
                titleGlow += 0.04f;
                if (titleGlow >= 1.0f) {
                    titleGlow = 1.0f;
                    glowUp = false;
                }
            } else {
                titleGlow -= 0.04f;
                if (titleGlow <= 0.3f) {
                    titleGlow = 0.3f;
                    glowUp = true;
                }
            }

            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(15, 15, 40),
            0, getHeight(), new Color(40, 20, 60)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw stars only if enabled
        if (starsEnabled) {
            for (Star star : stars) {
                star.draw(g2d);
            }
        }
        
        // Draw decorative ground
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
            x = (float) (Math.random() * 1000);
            y = (float) (Math.random() * 600);
            size = (float) (Math.random() * 2 + 1);
            alpha = (float) (Math.random() * 0.5 + 0.3);
            twinkleSpeed = (float) (Math.random() * 0.02 + 0.01);
            twinkle = (float) Math.random();
        }
        
        public void update() {
            if (twinkleUp) {
                twinkle += twinkleSpeed;
                if (twinkle >= 1.0f) {
                    twinkle = 1.0f;
                    twinkleUp = false;
                }
            } else {
                twinkle -= twinkleSpeed;
                if (twinkle <= 0.3f) {
                    twinkle = 0.3f;
                    twinkleUp = true;
                }
            }
        }
        
        public void draw(Graphics2D g2d) {
            int a = (int) (alpha * twinkle * 255);
            g2d.setColor(new Color(200, 200, 255, a));
            g2d.fillOval((int) x, (int) y, (int) size, (int) size);
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
                Color topColor = hovered ? new Color(130, 200, 100) : new Color(100, 180, 80);
                Color bottomColor = hovered ? new Color(80, 160, 60) : new Color(60, 140, 50);
                GradientPaint gradient = new GradientPaint(0, 0, topColor, 0, h, bottomColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, w, h, 20, 20);
                
                // Shine effect
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.fillRoundRect(5, 5, w - 10, h / 3, 15, 15);
                
                // Border
                g2d.setColor(new Color(255, 255, 255, hovered ? 200 : 120));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(1, 1, w - 2, h - 2, 20, 20);
                
                // Inner glow
                g2d.setColor(new Color(200, 255, 150, 80));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(4, 4, w - 8, h - 8, 17, 17);
                
                // Level number
                g2d.setFont(new Font("Impact", Font.BOLD, 36));
                g2d.setColor(Color.WHITE);
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
                // Locked button with modern style
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(4, 4, w - 8, h - 8, 20, 20);
                
                // Button background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(80, 80, 100),
                    0, h, new Color(60, 60, 80)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, w, h, 20, 20);
                
                // Border
                g2d.setColor(new Color(100, 100, 120));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(1, 1, w - 2, h - 2, 20, 20);
                
                // Draw lock icon
                drawLockIcon(g2d, w / 2, h / 2);
            }
            
            g2d.dispose();
        }
        
        private void drawLockIcon(Graphics2D g2d, int cx, int cy) {
            // Lock body
            g2d.setColor(new Color(40, 40, 50));
            g2d.fillRoundRect(cx - 12, cy - 5, 24, 20, 5, 5);
            
            // Lock body border
            g2d.setColor(new Color(150, 150, 170));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(cx - 12, cy - 5, 24, 20, 5, 5);
            
            // Lock shackle
            g2d.setStroke(new BasicStroke(3));
            g2d.drawArc(cx - 10, cy - 20, 20, 20, 0, 180);
            
            // Keyhole
            g2d.setColor(new Color(150, 150, 170));
            g2d.fillOval(cx - 3, cy + 2, 6, 6);
            g2d.fillRect(cx - 2, cy + 6, 4, 5);
        }
        
        private void drawCompletionStar(Graphics2D g2d, int cx, int cy) {
            // Small gold star for completed levels
            int[] xPoints = new int[10];
            int[] yPoints = new int[10];
            int outerR = 8;
            int innerR = 4;
            
            for (int i = 0; i < 10; i++) {
                double angle = Math.PI / 5 * i - Math.PI / 2;
                int r = (i % 2 == 0) ? outerR : innerR;
                xPoints[i] = (int) (cx + r * Math.cos(angle));
                yPoints[i] = (int) (cy + r * Math.sin(angle));
            }
            
            // Star glow
            g2d.setColor(new Color(255, 215, 0, 100));
            for (int i = 3; i > 0; i--) {
                g2d.setStroke(new BasicStroke(i * 2));
                g2d.drawPolygon(xPoints, yPoints, 10);
            }
            
            // Star fill
            g2d.setColor(new Color(255, 215, 0));
            g2d.fillPolygon(xPoints, yPoints, 10);
            
            // Star border
            g2d.setColor(new Color(255, 255, 150));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawPolygon(xPoints, yPoints, 10);
        }
    }
}