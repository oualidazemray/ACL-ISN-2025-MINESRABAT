package com.lo3ba.ui;

import com.lo3ba.gameobjects.Avatar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

public class LevelVictoryScreen extends JPanel {
    private Font retroFont;
    private Font titleFont;
    private OnVictoryActionListener listener;
    private int starsCollected;
    private int totalStars;
    private int deathCount;
    private Avatar currentAvatar;
    private Timer animationTimer;
    private float alpha = 0f;

    public interface OnVictoryActionListener {
        void onNextLevel();
        void onRetry();
        void onMainMenu();
    }

    public LevelVictoryScreen(OnVictoryActionListener listener) {
        this.listener = listener;
        setLayout(null);
        setOpaque(false); // Transparent background to show game behind (dimmed)
        loadFonts();
        createButtons();
        
        // Add component listener to handle resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                repositionButtons();
            }
        });
        
        // Animation timer for fade-in effect
        animationTimer = new Timer(30, e -> {
            alpha += 0.05f;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                animationTimer.stop();
            }
            repaint();
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            this.alpha = 0f;
            if (animationTimer != null) animationTimer.start();
            
            // Play level complete sound
            try {
                javax.sound.sampled.Clip clip = com.lo3ba.util.ResourceManager.loadSound("level_complete.wav");
                if (clip != null) {
                    clip.setFramePosition(0);
                    clip.start();
                }
            } catch (Exception e) {
                System.err.println("Failed to play level complete sound");
            }
        } else {
            if (animationTimer != null) animationTimer.stop();
        }
    }

    public void setStats(int stars, int totalStars, int deaths, Avatar avatar) {
        this.starsCollected = stars;
        this.totalStars = totalStars;
        this.deathCount = deaths;
        this.currentAvatar = avatar;
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

    private void createButtons() {
        int buttonWidth = 200;
        int buttonHeight = 50;
        int startY = 350;
        int spacing = 20;

        // Next Level Button
        createButton("NEXT LEVEL", 400 - buttonWidth / 2, startY, buttonWidth, buttonHeight, () -> {
            if (listener != null) listener.onNextLevel();
        });

        // Retry Button
        createButton("RETRY", 400 - buttonWidth / 2, startY + buttonHeight + spacing, buttonWidth, buttonHeight, () -> {
            if (listener != null) listener.onRetry();
        });

        // Main Menu Button
        createButton("MAIN MENU", 400 - buttonWidth / 2, startY + (buttonHeight + spacing) * 2, buttonWidth, buttonHeight, () -> {
            if (listener != null) listener.onMainMenu();
        });
    }

    private void createButton(String text, int x, int y, int w, int h, Runnable action) {
        JPanel btn = new JPanel() {
            private boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        SwingUtilities.invokeLater(action);
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (hovered) {
                    g2d.setColor(new Color(100, 200, 100));
                    g2d.fillRoundRect(0, 0, w, h, 15, 15);
                    g2d.setColor(new Color(50, 150, 50));
                } else {
                    g2d.setColor(new Color(80, 180, 80));
                    g2d.fillRoundRect(0, 0, w, h, 15, 15);
                    g2d.setColor(new Color(40, 120, 40));
                }
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(0, 0, w - 1, h - 1, 15, 15);

                g2d.setFont(retroFont);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (w - fm.stringWidth(text)) / 2;
                int textY = (h + fm.getAscent()) / 2 - 4;

                g2d.setColor(Color.BLACK);
                g2d.drawString(text, textX + 2, textY + 2);
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, textX, textY);
            }
        };
        btn.setBounds(x, y, w, h);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btn);
    }

    private void repositionButtons() {
        Component[] components = getComponents();
        if (components.length < 3) return;
        
        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = getWidth() / 2 - buttonWidth / 2;
        int startY = 350;
        int spacing = 20;
        
        // Reposition all button components
        if (components.length >= 1) components[0].setBounds(centerX, startY, buttonWidth, buttonHeight);
        if (components.length >= 2) components[1].setBounds(centerX, startY + buttonHeight + spacing, buttonWidth, buttonHeight);
        if (components.length >= 3) components[2].setBounds(centerX, startY + (buttonHeight + spacing) * 2, buttonWidth, buttonHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Semi-transparent dark overlay
        g2d.setColor(new Color(0, 0, 0, (int)(200 * alpha)));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (alpha > 0.1f) {
            // Title
            String title = "LEVEL COMPLETE!";
            g2d.setFont(titleFont);
            FontMetrics fm = g2d.getFontMetrics();
            int titleX = (getWidth() - fm.stringWidth(title)) / 2;
            int titleY = 150;

            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.drawString(title, titleX + 4, titleY + 4);
            g2d.setColor(new Color(255, 215, 0)); // Gold
            g2d.drawString(title, titleX, titleY);

            // Stats Box
            int boxW = 400;
            int boxH = 150;
            int boxX = (getWidth() - boxW) / 2;
            int boxY = 180;

            g2d.setColor(new Color(50, 50, 50, 200));
            g2d.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
            g2d.setColor(new Color(200, 200, 200));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);

            // Stats Text
            g2d.setFont(retroFont);
            g2d.setColor(Color.WHITE);
            
            String starsText = "STARS: " + starsCollected + " / " + totalStars;
            String deathsText = "DEATHS: " + deathCount;
            
            g2d.drawString(starsText, boxX + 40, boxY + 60);
            g2d.drawString(deathsText, boxX + 40, boxY + 100);
            
            // Draw Stars Icon (Simple circles for now)
            g2d.setColor(Color.YELLOW);
            for(int i=0; i<starsCollected; i++) {
                g2d.fillOval(boxX + 250 + (i*30), boxY + 40, 20, 20);
            }
            g2d.setColor(Color.GRAY);
            for(int i=starsCollected; i<totalStars; i++) {
                g2d.drawOval(boxX + 250 + (i*30), boxY + 40, 20, 20);
            }
        }

        g2d.dispose();
    }
}
