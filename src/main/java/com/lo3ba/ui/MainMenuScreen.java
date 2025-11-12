package com.lo3ba.ui;

import javax.swing.*;
import java.awt.*;

import java.awt.geom.*;

public class MainMenuScreen extends JPanel {
    private JButton playButton;
    private JButton repeatButton;
    private JButton quitButton;
    private JButton levelsButton;
    private OnButtonClickListener listener;
    private Timer glowTimer;
    private float titleGlow = 0f;
    private boolean glowUp = true;

    public interface OnButtonClickListener {
        void onPlay();
        void onRepeat();
        void onQuit();
        void onLevels();
    }

    public MainMenuScreen(OnButtonClickListener listener) {
        this.listener = listener;
        setLayout(new GridBagLayout());
        setOpaque(true);
        initUI();
        startAnimations();
    }



    private void initUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(30, 20, 30, 20);

        // Logo/Icon Panel
        JPanel logoPanel = createLogoPanel();
        gbc.gridy = 0;
        add(logoPanel, gbc);

        // Title with animated glow
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                String text = "LO3BA GAME";
                Font font = new Font("Impact", Font.BOLD, 64);
                g2d.setFont(font);
                
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                
                // Glow layers
                for (int i = 8; i > 0; i--) {
                    int alpha = (int) (25 * titleGlow * (i / 8.0f));
                    g2d.setColor(new Color(255, 215, 0, alpha));
                    g2d.drawString(text, x, y);
                }
                
                // Main text with gradient
                GradientPaint gradient = new GradientPaint(
                    x, y - fm.getAscent(), new Color(255, 255, 100),
                    x, y, new Color(255, 200, 0)
                );
                g2d.setPaint(gradient);
                g2d.drawString(text, x, y);
                
                // Outline
                g2d.setColor(new Color(139, 69, 19));
                g2d.setStroke(new BasicStroke(2));
                Font outlineFont = font.deriveFont(Font.BOLD);
                g2d.setFont(outlineFont);
                
                g2d.dispose();
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(600, 120));
        gbc.gridy = 1;
        add(titlePanel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("🎮 READY TO PLAY? 🎮", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        subtitleLabel.setForeground(new Color(200, 200, 255));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 20, 40, 20);
        add(subtitleLabel, gbc);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4, 1, 0, 15));
        buttonsPanel.setOpaque(false);

        playButton = createMenuButton("> PLAY", new Color(76, 175, 80), new Color(56, 142, 60), "🎯");
        levelsButton = createMenuButton("|| LEVELS", new Color(156, 39, 176), new Color(123, 31, 162), "🏆");
        repeatButton = createMenuButton("> REPEAT", new Color(33, 150, 243), new Color(25, 118, 210), "🔄");
        quitButton = createMenuButton("x  QUIT", new Color(244, 67, 54), new Color(211, 47, 47), "🚪");

        // Add action listeners
        playButton.addActionListener(e -> {
            System.out.println("PLAY button clicked");
            if (listener != null) listener.onPlay();
        });

        repeatButton.addActionListener(e -> {
            if (listener != null) listener.onRepeat();
        });

        quitButton.addActionListener(e -> {
            if (listener != null) listener.onQuit();
        });

        levelsButton.addActionListener(e -> {
            if (listener != null) listener.onLevels();
        });

        buttonsPanel.add(playButton);
        buttonsPanel.add(levelsButton);
        buttonsPanel.add(repeatButton);
        buttonsPanel.add(quitButton);

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 60, 40, 60);
        add(buttonsPanel, gbc);

        // Footer
        JLabel footerLabel = new JLabel("© 2025 LO3BA GAME", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(100, 100, 100));
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 20, 10, 20);
        add(footerLabel, gbc);
    }

    private JButton createMenuButton(String text, Color baseColor, Color hoverColor, String emoji) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;
            private float hoverScale = 1.0f;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Scale effect
                if (isHovered) {
                    hoverScale = Math.min(1.05f, hoverScale + 0.02f);
                } else {
                    hoverScale = Math.max(1.0f, hoverScale - 0.02f);
                }
                
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.fillRoundRect(4, 4, w - 8, h - 8, 25, 25);
                
                // Button background with gradient
                Color topColor = isHovered ? hoverColor.brighter() : baseColor;
                Color bottomColor = isHovered ? hoverColor : baseColor.darker();
                GradientPaint gradient = new GradientPaint(0, 0, topColor, 0, h, bottomColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, w, h, 25, 25);
                
                // Shine effect
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.fillRoundRect(5, 5, w - 10, h / 2, 20, 20);
                
                // Border
                g2d.setColor(new Color(255, 255, 255, isHovered ? 200 : 120));
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(1, 1, w - 2, h - 2, 25, 25);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(280, 65));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover animation
        Timer hoverTimer = new Timer(20, e -> button.repaint());
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.putClientProperty("isHovered", true);
                hoverTimer.start();
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.putClientProperty("isHovered", false);
                hoverTimer.start();
            }
        });
        
        return button;
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                
                // Outer ring
                g2d.setColor(new Color(100, 100, 255, 150));
                g2d.setStroke(new BasicStroke(4));
                g2d.drawOval(cx - 45, cy - 45, 90, 90);
                
                // Inner circles with gradient
                RadialGradientPaint gradient = new RadialGradientPaint(
                    cx, cy, 35,
                    new float[]{0f, 0.6f, 1f},
                    new Color[]{new Color(150, 150, 255), new Color(100, 100, 255), new Color(50, 50, 200)}
                );
                g2d.setPaint(gradient);
                g2d.fillOval(cx - 35, cy - 35, 70, 70);
                
                // Play symbol
                g2d.setColor(Color.WHITE);
                int[] xPoints = {cx - 10, cx + 15, cx - 10};
                int[] yPoints = {cy - 15, cy, cy + 15};
                g2d.fillPolygon(xPoints, yPoints, 3);
                
                g2d.dispose();
            }
        };
        
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(100, 100));
        return logoPanel;
    }

    private void startAnimations() {
        glowTimer = new Timer(30, e -> {
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

            repaint();
        });
        glowTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(10, 10, 30),
            0, getHeight(), new Color(30, 10, 50)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.dispose();
    }

    public void show() {
        setVisible(true);
        requestFocusInWindow();
        if (glowTimer != null && !glowTimer.isRunning()) {
            glowTimer.start();
        }
    }

    public void hide() {
        setVisible(false);
        if (glowTimer != null) {
            glowTimer.stop();
        }
    }


}