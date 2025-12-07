package com.lo3ba.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainMenuScreen extends JPanel {
    private Font retroFont;
    private Font titleFont;
    private JButton playButton;
    private JButton repeatButton;
    private JButton quitButton;
    private JButton levelsButton;
    private OnButtonClickListener listener;
    private Timer animationTimer;
    private float titleGlow = 0f;
    private boolean glowUp = true;
    private List<Star> stars = new ArrayList<>();

    public interface OnButtonClickListener {
        void onPlay();
        void onRepeat();
        void onQuit();
        void onLevels();
    }

    public MainMenuScreen(OnButtonClickListener listener) {
        this.listener = listener;
        setLayout(null); // Absolute layout for precise positioning
        loadFonts();
        initStars();
        createButtons();
        
        // Add component listener to handle resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                repositionButtons();
            }
        });
        
        // Animation timer
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
                titleFont = baseFont.deriveFont(56f);
            } else {
                retroFont = new Font("Arial", Font.BOLD, 16);
                titleFont = new Font("Impact", Font.BOLD, 64);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retroFont = new Font("Arial", Font.BOLD, 16);
            titleFont = new Font("Impact", Font.BOLD, 64);
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

    private void createButtons() {
        int buttonWidth = 280;
        int buttonHeight = 60;
        int centerX = 1000 / 2 - buttonWidth / 2;
        int startY = 280;
        int spacing = 20;

        playButton = createModernButton("PLAY GAME", new Color(0, 200, 83), centerX, startY);
        levelsButton = createModernButton("LEVEL SELECT", new Color(156, 39, 176), centerX, startY + buttonHeight + spacing);
        repeatButton = createModernButton("CONTINUE", new Color(33, 150, 243), centerX, startY + (buttonHeight + spacing) * 2);
        quitButton = createModernButton("EXIT", new Color(244, 67, 54), centerX, startY + (buttonHeight + spacing) * 3);

        playButton.addActionListener(e -> { if (listener != null) listener.onPlay(); });
        levelsButton.addActionListener(e -> { if (listener != null) listener.onLevels(); });
        repeatButton.addActionListener(e -> { if (listener != null) listener.onRepeat(); });
        quitButton.addActionListener(e -> { if (listener != null) listener.onQuit(); });

        add(playButton);
        add(levelsButton);
        add(repeatButton);
        add(quitButton);
    }

    private void repositionButtons() {
        if (playButton == null) return;
        
        int buttonWidth = 280;
        int buttonHeight = 60;
        int centerX = getWidth() / 2 - buttonWidth / 2;
        int startY = 280;
        int spacing = 20;
        
        playButton.setBounds(centerX, startY, buttonWidth, buttonHeight);
        levelsButton.setBounds(centerX, startY + buttonHeight + spacing, buttonWidth, buttonHeight);
        repeatButton.setBounds(centerX, startY + (buttonHeight + spacing) * 2, buttonWidth, buttonHeight);
        quitButton.setBounds(centerX, startY + (buttonHeight + spacing) * 3, buttonWidth, buttonHeight);
    }

    private JButton createModernButton(String text, Color color, int x, int y) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;
            private float hoverAlpha = 0f;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Hover animation state
                if (isHovered && hoverAlpha < 1.0f) hoverAlpha += 0.1f;
                if (!isHovered && hoverAlpha > 0f) hoverAlpha -= 0.1f;
                if (hoverAlpha < 0) hoverAlpha = 0;
                if (hoverAlpha > 1) hoverAlpha = 1;

                // Background
                g2d.setColor(new Color(20, 20, 40, 200));
                g2d.fillRoundRect(0, 0, w, h, 15, 15);

                // Border / Glow
                Color glowColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100 + (int)(155 * hoverAlpha));
                g2d.setColor(glowColor);
                g2d.setStroke(new BasicStroke(2f + 2f * hoverAlpha));
                g2d.drawRoundRect(1, 1, w - 2, h - 2, 15, 15);

                // Inner fill on hover
                if (hoverAlpha > 0) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(50 * hoverAlpha)));
                    g2d.fillRoundRect(2, 2, w - 4, h - 4, 15, 15);
                }

                // Text
                g2d.setFont(retroFont);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (w - fm.stringWidth(text)) / 2;
                int textY = (h + fm.getAscent()) / 2 - 4;

                // Text Shadow
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.drawString(text, textX + 2, textY + 2);

                // Text Color
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, textX, textY);

                g2d.dispose();
            }
        };

        button.setBounds(x, y, 280, 60);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.putClientProperty("hovered", true);
                // Trigger repaint via timer in main loop or immediate
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.putClientProperty("hovered", false);
            }
        });
        
        // Custom logic to link mouse listener to paint state
        button.addMouseListener(new MouseAdapter() {
             public void mouseEntered(MouseEvent e) { 
                 try {
                     java.lang.reflect.Field f = button.getClass().getDeclaredField("isHovered");
                     f.setAccessible(true);
                     f.setBoolean(button, true);
                 } catch (Exception ex) {}
             }
             public void mouseExited(MouseEvent e) {
                 try {
                     java.lang.reflect.Field f = button.getClass().getDeclaredField("isHovered");
                     f.setAccessible(true);
                     f.setBoolean(button, false);
                 } catch (Exception ex) {}
             }
        });

        return button;
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

        // Draw Footer
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(new Color(100, 100, 150));
        String footer = "© 2025 LO3BA GAME - DEVIL EVIL 2 STYLE";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(footer, (getWidth() - fm.stringWidth(footer)) / 2, getHeight() - 20);

        g2d.dispose();
    }

    private void drawTitle(Graphics2D g2d) {
        String title = "LO3BA GAME";
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int titleW = fm.stringWidth(title);
        int titleX = (getWidth() - titleW) / 2;
        int titleY = 150;

        // Glow effect
        for (int i = 0; i < 10; i++) {
            float alpha = 0.1f * titleGlow * (1.0f - i / 10.0f);
            g2d.setColor(new Color(0, 255, 255, (int)(255 * alpha)));
            g2d.drawString(title, titleX - i, titleY - i);
            g2d.drawString(title, titleX + i, titleY + i);
        }

        // Main Text
        GradientPaint textGp = new GradientPaint(titleX, titleY - 50, new Color(0, 200, 255), titleX, titleY, new Color(0, 100, 200));
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
}