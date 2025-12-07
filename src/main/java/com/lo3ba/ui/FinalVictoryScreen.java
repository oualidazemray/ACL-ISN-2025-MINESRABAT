package com.lo3ba.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FinalVictoryScreen extends JPanel {
    private Font retroFont;
    private Font titleFont;
    private OnFinalVictoryActionListener listener;
    private Timer animationTimer;
    private List<Confetti> confettiList = new ArrayList<>();

    public interface OnFinalVictoryActionListener {
        void onPlayAgain();
        void onMainMenu();
        void onExit();
    }

    public FinalVictoryScreen(OnFinalVictoryActionListener listener) {
        this.listener = listener;
        setLayout(null);
        setBackground(new Color(20, 20, 40));
        loadFonts();
        createButtons();
        initConfetti();

        // Add component listener to handle resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                repositionButtons();
            }
        });

        animationTimer = new Timer(30, e -> {
            updateConfetti();
            repaint();
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            if (animationTimer != null) animationTimer.start();
            
            // Play victory music
            try {
                javax.sound.sampled.Clip clip = com.lo3ba.util.ResourceManager.loadSound("victory_music.wav");
                if (clip != null) {
                    clip.setFramePosition(0);
                    clip.start();
                }
            } catch (Exception e) {
                System.err.println("Failed to play victory music");
            }
        } else {
            if (animationTimer != null) animationTimer.stop();
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
                titleFont = new Font("Impact", Font.BOLD, 60);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retroFont = new Font("Arial", Font.BOLD, 16);
            titleFont = new Font("Impact", Font.BOLD, 60);
        }
    }

    private void initConfetti() {
        for (int i = 0; i < 100; i++) {
            confettiList.add(new Confetti());
        }
    }

    private void updateConfetti() {
        for (Confetti c : confettiList) {
            c.update();
        }
    }

    private void createButtons() {
        int buttonWidth = 220;
        int buttonHeight = 50;
        int startY = 400;
        int spacing = 20;

        createButton("PLAY AGAIN", 400 - buttonWidth / 2, startY, buttonWidth, buttonHeight, new Color(100, 200, 100), () -> {
            if (listener != null) listener.onPlayAgain();
        });

        createButton("MAIN MENU", 400 - buttonWidth / 2, startY + buttonHeight + spacing, buttonWidth, buttonHeight, new Color(100, 100, 200), () -> {
            if (listener != null) listener.onMainMenu();
        });

        createButton("EXIT GAME", 400 - buttonWidth / 2, startY + (buttonHeight + spacing) * 2, buttonWidth, buttonHeight, new Color(200, 100, 100), () -> {
            if (listener != null) listener.onExit();
        });
    }

    private void createButton(String text, int x, int y, int w, int h, Color baseColor, Runnable action) {
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
                    g2d.setColor(baseColor.brighter());
                } else {
                    g2d.setColor(baseColor);
                }
                g2d.fillRoundRect(0, 0, w, h, 15, 15);
                
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, w - 1, h - 1, 15, 15);

                g2d.setFont(retroFont);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (w - fm.stringWidth(text)) / 2;
                int textY = (h + fm.getAscent()) / 2 - 4;

                g2d.setColor(new Color(0,0,0,100));
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
        
        int buttonWidth = 220;
        int buttonHeight = 50;
        int centerX = getWidth() / 2 - buttonWidth / 2;
        int startY = 400;
        int spacing = 20;
        
        // Reposition all button components
        if (components.length >= 1) components[0].setBounds(centerX, startY, buttonWidth, buttonHeight);
        if (components.length >= 2) components[1].setBounds(centerX, startY + buttonHeight + spacing, buttonWidth, buttonHeight);
        if (components.length >= 3) components[2].setBounds(centerX, startY + (buttonHeight + spacing) * 2, buttonWidth, buttonHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw confetti
        for (Confetti c : confettiList) {
            c.draw(g2d);
        }

        // Title
        String title = "VICTORY!";
        String subtitle = "YOU SAVED THE WORLD";
        
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        int titleY = 150;

        // Glow effect
        g2d.setColor(new Color(255, 200, 0, 100));
        for(int i=0; i<5; i++) {
            g2d.drawString(title, titleX+i, titleY+i);
        }
        
        g2d.setColor(Color.YELLOW);
        g2d.drawString(title, titleX, titleY);

        g2d.setFont(retroFont);
        fm = g2d.getFontMetrics();
        int subX = (getWidth() - fm.stringWidth(subtitle)) / 2;
        g2d.setColor(Color.WHITE);
        g2d.drawString(subtitle, subX, titleY + 50);
    }

    private class Confetti {
        float x, y, speedY, speedX;
        Color color;
        int size;

        public Confetti() {
            reset();
            y = (float) (Math.random() * getHeight()); // Start scattered
        }

        void reset() {
            x = (float) (Math.random() * getWidth());
            y = -10;
            speedY = 2 + (float) (Math.random() * 3);
            speedX = -1 + (float) (Math.random() * 2);
            size = 5 + (int) (Math.random() * 5);
            color = new Color(
                (int)(Math.random()*255),
                (int)(Math.random()*255),
                (int)(Math.random()*255)
            );
        }

        void update() {
            y += speedY;
            x += speedX;
            if (y > getHeight()) {
                reset();
            }
        }

        void draw(Graphics2D g) {
            g.setColor(color);
            g.fillRect((int)x, (int)y, size, size);
        }
    }
}
