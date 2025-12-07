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
    private JButton backButton;
    private int unlockedLevels = 1;
    private java.util.Set<Integer> completedLevels = new java.util.HashSet<>();
    private Timer animationTimer;
    private float titleGlow = 0f;
    private boolean glowUp = true;
    private List<Star> stars = new ArrayList<>();

    public interface OnLevelSelectedListener {
        void onLevelSelected(int level);
        void onBack();
    }

    public LevelSelectScreen(int unlockedLevels, java.util.Set<Integer> completedLevels, OnLevelSelectedListener listener) {
        this.listener = listener;
        this.unlockedLevels = unlockedLevels;
        this.completedLevels = completedLevels != null ? completedLevels : new java.util.HashSet<>();
        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));
        
        loadFonts();
        initStars();
        createLevelButtons();
        createBackButton();
        
        // Apply unlocked state
        updateUnlockedLevels();
        
        // Add component listener to handle resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                repositionButtons();
            }
        });
        
        animationTimer = new Timer(30, e -> {
            updateAnimations();
            repaint();
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (animationTimer != null && !animationTimer.isRunning()) {
            animationTimer.start();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    private void loadFonts() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (is != null) {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
                retroFont = baseFont.deriveFont(16f);
                titleFont = baseFont.deriveFont(48f);
            } else {
                retroFont = new Font("Arial", Font.BOLD, 16);
                titleFont = new Font("Impact", Font.BOLD, 56);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retroFont = new Font("Arial", Font.BOLD, 16);
            titleFont = new Font("Impact", Font.BOLD, 56);
        }
    }

    private void initStars() {
        for (int i = 0; i < 80; i++) {
            stars.add(new Star());
        }
    }

    private void updateAnimations() {
        // Title glow
        if (glowUp) {
            titleGlow += 0.02f;
            if (titleGlow >= 1.0f) {
                titleGlow = 1.0f;
                glowUp = false;
            }
        } else {
            titleGlow -= 0.02f;
            if (titleGlow <= 0.3f) {
                titleGlow = 0.3f;
                glowUp = true;
            }
        }

        // Stars
        for (Star s : stars) {
            s.update();
        }
    }

    private void createBackButton() {
        backButton = new JButton("BACK") {
            private boolean isHovered = false;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Background
                if (isHovered) {
                    g2d.setColor(new Color(200, 50, 50, 200));
                } else {
                    g2d.setColor(new Color(150, 30, 30, 150));
                }
                g2d.fillRoundRect(0, 0, w, h, 15, 15);
                
                // Border
                g2d.setColor(new Color(255, 100, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, w-1, h-1, 15, 15);
                
                // Text
                g2d.setFont(retroFont);
                FontMetrics fm = g2d.getFontMetrics();
                int tx = (w - fm.stringWidth(getText())) / 2;
                int ty = (h + fm.getAscent()) / 2 - 4;
                
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), tx, ty);
                
                g2d.dispose();
            }
        };
        
        backButton.setBounds(20, 20, 120, 40);
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                try {
                    java.lang.reflect.Field f = backButton.getClass().getDeclaredField("isHovered");
                    f.setAccessible(true);
                    f.setBoolean(backButton, true);
                    backButton.repaint();
                } catch (Exception ex) {}
            }
            public void mouseExited(MouseEvent e) {
                try {
                    java.lang.reflect.Field f = backButton.getClass().getDeclaredField("isHovered");
                    f.setAccessible(true);
                    f.setBoolean(backButton, false);
                    backButton.repaint();
                } catch (Exception ex) {}
            }
            public void mouseClicked(MouseEvent e) {
                if (listener != null) listener.onBack();
            }
        });
        
        add(backButton);
    }

    private void createLevelButtons() {
        levelButtons = new ArrayList<>();

        int buttonSize = 90;
        int spacing = 25;
        int cols = 5;
        int totalWidth = (buttonSize * cols) + (spacing * (cols - 1));
        int startX = (1000 - totalWidth) / 2;
        int startY = 220;

        for (int i = 0; i < 10; i++) {
            int row = i / cols;
            int col = i % cols;
            int x = startX + (col * (buttonSize + spacing));
            int y = startY + (row * (buttonSize + spacing + 20));

            LevelButton btn = new LevelButton(i + 1, x, y, buttonSize, buttonSize);
            levelButtons.add(btn);
            add(btn);

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!btn.isVisible() || !btn.isEnabled()) return;
                    
                    // Can only play level N if level N-1 is completed (or if it's level 1)
                    int levelNum = btn.getLevel();
                    boolean canPlay = (levelNum == 1) || completedLevels.contains(levelNum - 1);
                    
                    if (btn.isUnlocked() && canPlay && listener != null) {
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

    public void updateUnlockedLevels(int levels, java.util.Set<Integer> completedLevels) {
        this.unlockedLevels = Math.min(levels, 10);
        this.completedLevels = completedLevels != null ? completedLevels : new java.util.HashSet<>();
        updateUnlockedLevels();
    }

    private void updateUnlockedLevels() {
        if (levelButtons == null) return;
        for (int i = 0; i < levelButtons.size(); i++) {
            boolean unlocked = i < unlockedLevels;
            boolean completed = completedLevels.contains(i + 1);
            levelButtons.get(i).setUnlocked(unlocked);
            levelButtons.get(i).setCompleted(completed);
        }
    }

    private void repositionButtons() {
        if (levelButtons == null || levelButtons.isEmpty()) return;
        
        int buttonSize = 90;
        int spacing = 25;
        int cols = 5;
        int totalWidth = (buttonSize * cols) + (spacing * (cols - 1));
        int startX = (getWidth() - totalWidth) / 2;
        int startY = 220;
        
        for (int i = 0; i < levelButtons.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int x = startX + (col * (buttonSize + spacing));
            int y = startY + (row * (buttonSize + spacing + 20));
            levelButtons.get(i).setBounds(x, y, buttonSize, buttonSize);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Background Gradient
        GradientPaint gp = new GradientPaint(0, 0, new Color(10, 10, 25), 0, getHeight(), new Color(25, 20, 40));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw Stars
        for (Star s : stars) s.draw(g2d);

        // Draw Title
        drawTitle(g2d);

        g2d.dispose();
    }

    private void drawTitle(Graphics2D g2d) {
        String title = "SELECT LEVEL";
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int titleW = fm.stringWidth(title);
        int titleX = (getWidth() - titleW) / 2;
        int titleY = 120;

        // Glow effect
        for (int i = 0; i < 10; i++) {
            float alpha = 0.1f * titleGlow * (1.0f - i / 10.0f);
            g2d.setColor(new Color(156, 39, 176, (int)(255 * alpha))); // Purple glow
            g2d.drawString(title, titleX - i, titleY - i);
            g2d.drawString(title, titleX + i, titleY + i);
        }

        // Main Text
        GradientPaint textGp = new GradientPaint(titleX, titleY - 50, new Color(200, 100, 255), titleX, titleY, new Color(100, 50, 200));
        g2d.setPaint(textGp);
        g2d.drawString(title, titleX, titleY);

        // Outline
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawString(title, titleX, titleY);
    }

    private class Star {
        float x, y, size, speed;
        float alpha;

        Star() {
            reset();
            y = (float) (Math.random() * getHeight());
        }

        void reset() {
            x = (float) (Math.random() * getWidth());
            y = -10;
            size = 1 + (float) (Math.random() * 3);
            speed = 0.5f + (float) (Math.random() * 1.5f);
            alpha = 0.2f + (float) (Math.random() * 0.8f);
        }

        void update() {
            y += speed;
            if (y > getHeight()) reset();
        }

        void draw(Graphics2D g) {
            g.setColor(new Color(255, 255, 255, (int)(255 * alpha)));
            g.fill(new Ellipse2D.Float(x, y, size, size));
        }
    }

    // Enhanced LevelButton class
    class LevelButton extends JPanel {
        private int level;
        private boolean unlocked;
        private boolean completed; // New field
        private boolean hovered;
        private float hoverScale = 1.0f;

        public LevelButton(int level, int x, int y, int width, int height) {
            this.level = level;
            this.unlocked = false;
            this.completed = false;
            setBounds(x, y, width, height);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (unlocked) {
                        hovered = true;
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                }
            });
        }

        public int getLevel() { return level; }
        public boolean isUnlocked() { return unlocked; }

        public void setUnlocked(boolean unlocked) {
            this.unlocked = unlocked;
            repaint();
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Hover scale logic
            if (hovered && hoverScale < 1.1f) hoverScale += 0.05f;
            if (!hovered && hoverScale > 1.0f) hoverScale -= 0.05f;
            
            int drawW = (int)(w * hoverScale);
            int drawH = (int)(h * hoverScale);
            int dx = (w - drawW)/2;
            int dy = (h - drawH)/2;

            if (unlocked) {
                // Determine if completed or just unlocked
                if (completed) {
                    // COMPLETED STATE - Green/Gold theme
                    // Glow
                    if (hovered) {
                        g2d.setColor(new Color(255, 215, 0, 120)); // Gold glow
                        g2d.fillRoundRect(dx-5, dy-5, drawW+10, drawH+10, 25, 25);
                    }

                    // Background - Green gradient for completed
                    GradientPaint bg = new GradientPaint(0, 0, new Color(40, 120, 40), 0, h, new Color(20, 80, 20));
                    g2d.setPaint(bg);
                    g2d.fillRoundRect(dx, dy, drawW, drawH, 20, 20);

                    // Border - Gold for completed
                    g2d.setColor(new Color(255, 215, 0));
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRoundRect(dx, dy, drawW, drawH, 20, 20);

                    // Level Number
                    g2d.setFont(retroFont.deriveFont(24f));
                    String levelStr = String.valueOf(level);
                    FontMetrics fm = g2d.getFontMetrics();
                    int tx = (w - fm.stringWidth(levelStr)) / 2;
                    int ty = (h + fm.getAscent()) / 2 - 10;

                    g2d.setColor(Color.WHITE);
                    g2d.drawString(levelStr, tx, ty);

                    // Checkmark icon for completed
                    g2d.setColor(new Color(255, 215, 0));
                    g2d.setStroke(new BasicStroke(3));
                    int cx = w/2;
                    int cy = h/2 + 15;
                    g2d.drawLine(cx - 10, cy, cx - 3, cy + 7);
                    g2d.drawLine(cx - 3, cy + 7, cx + 10, cy - 10);
                    
                } else {
                    // UNLOCKED BUT NOT COMPLETED - Blue theme with clock
                    // Glow
                    if (hovered) {
                        g2d.setColor(new Color(100, 200, 255, 100));
                        g2d.fillRoundRect(dx-5, dy-5, drawW+10, drawH+10, 25, 25);
                    }

                    // Background
                    GradientPaint bg = new GradientPaint(0, 0, new Color(60, 60, 100), 0, h, new Color(40, 40, 80));
                    g2d.setPaint(bg);
                    g2d.fillRoundRect(dx, dy, drawW, drawH, 20, 20);

                    // Border
                    g2d.setColor(new Color(100, 150, 255));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(dx, dy, drawW, drawH, 20, 20);

                    // Level Number
                    g2d.setFont(retroFont.deriveFont(24f));
                    String levelStr = String.valueOf(level);
                    FontMetrics fm = g2d.getFontMetrics();
                    int tx = (w - fm.stringWidth(levelStr)) / 2;
                    int ty = (h + fm.getAscent()) / 2 - 10;

                    g2d.setColor(Color.WHITE);
                    g2d.drawString(levelStr, tx, ty);

                    // Clock icon for in-progress
                    g2d.setColor(new Color(200, 200, 200));
                    int clockX = w/2;
                    int clockY = h/2 + 15;
                    int clockR = 12;
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(clockX - clockR, clockY - clockR, clockR * 2, clockR * 2);
                    g2d.drawLine(clockX, clockY, clockX, clockY - 8); // Hour hand
                    g2d.drawLine(clockX, clockY, clockX + 6, clockY); // Minute hand
                }
            } else {
                // Locked
                g2d.setColor(new Color(30, 30, 40, 200));
                g2d.fillRoundRect(dx, dy, drawW, drawH, 20, 20);
                
                g2d.setColor(new Color(60, 60, 70));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(dx, dy, drawW, drawH, 20, 20);

                // Lock Icon
                g2d.setColor(new Color(100, 100, 100));
                int lx = w/2 - 10;
                int ly = h/2 - 12;
                g2d.fillRoundRect(lx, ly + 8, 20, 16, 5, 5);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawArc(lx + 2, ly, 16, 16, 0, 180);
            }

            g2d.dispose();
        }
    }
}