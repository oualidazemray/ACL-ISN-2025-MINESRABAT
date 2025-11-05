package com.lo3ba.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelSelectScreen extends JPanel {
    private Font retroFont;
    private Font titleFont;
    private OnLevelSelectedListener listener;
    private List<LevelButton> levelButtons;
    private int unlockedLevels = 1; // Start with only level 1 unlocked

    public interface OnLevelSelectedListener {
        void onLevelSelected(int level);
    }

    public LevelSelectScreen(OnLevelSelectedListener listener) {
        this.listener = listener;
        setLayout(null);
        setBackground(new Color(100, 200, 255)); // Sky blue
        
        loadFonts();
        createLevelButtons();
    }

    private void loadFonts() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (is != null) {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
                retroFont = baseFont.deriveFont(16f);
                titleFont = baseFont.deriveFont(32f);
            } else {
                retroFont = new Font("Monospaced", Font.BOLD, 16);
                titleFont = new Font("Monospaced", Font.BOLD, 32);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retroFont = new Font("Monospaced", Font.BOLD, 16);
            titleFont = new Font("Monospaced", Font.BOLD, 32);
        }
    }

    private void createLevelButtons() {
        levelButtons = new ArrayList<>();
        
        int buttonSize = 80;
        int spacing = 20;
        int startX = 150;
        int startY = 250;
        
        for (int i = 0; i < 10; i++) {
            int x = startX + (i * (buttonSize + spacing));
            LevelButton btn = new LevelButton(i + 1, x, startY, buttonSize, buttonSize);
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

    public void setUnlockedLevels(int levels) {
        this.unlockedLevels = Math.min(levels, 10);
        updateUnlockedLevels();
    }

    private void updateUnlockedLevels() {
        for (int i = 0; i < levelButtons.size(); i++) {
            levelButtons.get(i).setUnlocked(i < unlockedLevels);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        // Draw ground (bottom part)
        g2d.setColor(new Color(139, 90, 43)); // Brown ground
        g2d.fillRect(0, getHeight() - 150, getWidth(), 150);
        
        // Draw grass line
        g2d.setColor(new Color(34, 139, 34));
        g2d.fillRect(0, getHeight() - 155, getWidth(), 10);
        
        // Draw title banner
        int bannerHeight = 80;
        int bannerY = 50;
        
        // Banner shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(105, bannerY + 5, getWidth() - 210, bannerHeight, 20, 20);
        
        // Banner background (dark brown/purple)
        g2d.setColor(new Color(80, 50, 80));
        g2d.fillRoundRect(100, bannerY, getWidth() - 200, bannerHeight, 20, 20);
        
        // Banner border (orange)
        g2d.setColor(new Color(255, 150, 50));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(100, bannerY, getWidth() - 200, bannerHeight, 20, 20);
        
        // Inner border (white)
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(110, bannerY + 10, getWidth() - 220, bannerHeight - 20, 15, 15);
        
        // Draw "SELECT LEVEL" text
        g2d.setFont(titleFont);
        String title = "SELECT LEVEL";
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = (getWidth() - titleWidth) / 2;
        int titleY = bannerY + (bannerHeight + fm.getAscent()) / 2 - 5;
        
        // Text shadow
        g2d.setColor(Color.BLACK);
        g2d.drawString(title, titleX + 3, titleY + 3);
        
        // Text main color
        g2d.setColor(Color.WHITE);
        g2d.drawString(title, titleX, titleY);
        
        // Draw instructions
        g2d.setFont(retroFont.deriveFont(12f));
        String instructions = "Click on a level to start playing!";
        int instWidth = g2d.getFontMetrics().stringWidth(instructions);
        g2d.setColor(new Color(50, 50, 50));
        g2d.drawString(instructions, (getWidth() - instWidth) / 2, 180);
        
        g2d.dispose();
    }

    // Inner class for level buttons
    class LevelButton extends JPanel {
        private int level;
        private boolean unlocked;
        private boolean hovered;

        public LevelButton(int level, int x, int y, int width, int height) {
            this.level = level;
            this.unlocked = false;
            setBounds(x, y, width, height);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (unlocked) {
                        hovered = true;
                        repaint();
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        public int getLevel() { return level; }
        public boolean isUnlocked() { return unlocked; }
        
        public void setUnlocked(boolean unlocked) {
            this.unlocked = unlocked;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            
            if (unlocked) {
                // Unlocked button - orange/yellow
                if (hovered) {
                    g2d.setColor(new Color(255, 200, 50));
                } else {
                    g2d.setColor(new Color(255, 165, 0));
                }
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Border
                g2d.setColor(new Color(200, 100, 0));
                g2d.setStroke(new BasicStroke(4));
                g2d.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
                
                // Inner border
                g2d.setColor(new Color(255, 220, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(6, 6, getWidth() - 12, getHeight() - 12);
                
                // Level number
                g2d.setFont(retroFont.deriveFont(24f));
                g2d.setColor(Color.WHITE);
                String levelStr = String.valueOf(level);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(levelStr)) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                
                // Shadow
                g2d.setColor(new Color(100, 50, 0));
                g2d.drawString(levelStr, textX + 2, textY + 2);
                
                // Main text
                g2d.setColor(Color.WHITE);
                g2d.drawString(levelStr, textX, textY);
                
            } else {
                // Locked button - gray with lock icon
                g2d.setColor(new Color(120, 120, 120));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Border
                g2d.setColor(new Color(80, 80, 80));
                g2d.setStroke(new BasicStroke(4));
                g2d.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
                
                // Draw simple lock icon
                int lockSize = 30;
                int lockX = (getWidth() - lockSize) / 2;
                int lockY = (getHeight() - lockSize) / 2;
                
                // Lock body
                g2d.setColor(new Color(60, 60, 60));
                g2d.fillRect(lockX + 5, lockY + 15, lockSize - 10, 15);
                
                // Lock shackle
                g2d.setStroke(new BasicStroke(4));
                g2d.drawArc(lockX + 8, lockY, lockSize - 16, 20, 0, 180);
            }
            
            g2d.dispose();
        }
    }
}