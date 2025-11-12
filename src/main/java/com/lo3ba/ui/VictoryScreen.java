package com.lo3ba.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;

public class VictoryScreen extends JPanel {
    private JButton nextLevelButton;
    private JButton repeatButton;
    private JButton quitButton;
    private JButton homeButton;
    private OnButtonClickListener listener;
    private Timer animationTimer;
    private float glowIntensity = 0f;
    private boolean glowIncreasing = true;

    public interface OnButtonClickListener {
        void onNextLevel();
        void onRepeat();
        void onQuit();
        void onHome();
    }

    public VictoryScreen(OnButtonClickListener listener) {
        this.listener = listener;
        initUI();
        startAnimation();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 20, 20, 20);

        // Victory icon panel
        JPanel iconPanel = createStarPanel();
        gbc.gridy = 0;
        add(iconPanel, gbc);

        // Title
        JLabel titleLabel = new JLabel("LEVEL COMPLETE!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 48));
        titleLabel.setForeground(new Color(255, 215, 0));
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 30, 20);
        add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("🎉 VICTORY! 🎉", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        subtitleLabel.setForeground(new Color(255, 255, 150));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 20, 40, 20);
        add(subtitleLabel, gbc);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        GridBagConstraints bgc = new GridBagConstraints();
        bgc.insets = new Insets(10, 10, 10, 10);
        bgc.fill = GridBagConstraints.NONE;

        // Buttons
        nextLevelButton = createStyledButton("▶ Next Level", new Color(76, 175, 80), new Color(56, 142, 60));
        repeatButton = createStyledButton("↻ Repeat", new Color(33, 150, 243), new Color(25, 118, 210));
        homeButton = createStyledButton("⌂ Home", new Color(255, 152, 0), new Color(245, 124, 0));
        quitButton = createStyledButton("✕ Quit", new Color(244, 67, 54), new Color(211, 47, 47));

        // Add buttons to grid
        bgc.gridx = 0; bgc.gridy = 0; buttonsPanel.add(nextLevelButton, bgc);
        bgc.gridx = 1; bgc.gridy = 0; buttonsPanel.add(repeatButton, bgc);
        bgc.gridx = 0; bgc.gridy = 1; buttonsPanel.add(homeButton, bgc);
        bgc.gridx = 1; bgc.gridy = 1; buttonsPanel.add(quitButton, bgc);

        // Add action listeners
        nextLevelButton.addActionListener(e -> { if (listener != null) listener.onNextLevel(); });
        repeatButton.addActionListener(e -> { if (listener != null) listener.onRepeat(); });
        homeButton.addActionListener(e -> { if (listener != null) listener.onHome(); });
        quitButton.addActionListener(e -> { if (listener != null) listener.onQuit(); });

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 40, 20, 40);
        add(buttonsPanel, gbc);
    }

    private JButton createStyledButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean isHovered = getModel().isRollover();
                Color topColor = isHovered ? hoverColor.brighter() : baseColor;
                Color bottomColor = isHovered ? hoverColor : baseColor.darker();

                GradientPaint gradient = new GradientPaint(0, 0, topColor, 0, getHeight(), bottomColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);

                g2d.dispose();
                super.paintComponent(g);
            }

            @Override
            public void paintBorder(Graphics g) { /* No border */ }
        };

        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(180, 60));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JPanel createStarPanel() {
        JPanel starPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                drawStar(g2d, centerX, centerY, 40, 20, 5);
                g2d.dispose();
            }

            private void drawStar(Graphics2D g2d, int x, int y, int outerR, int innerR, int points) {
                double angle = Math.PI / points;
                Polygon star = new Polygon();

                for (int i = 0; i < points * 2; i++) {
                    double r = (i % 2 == 0) ? outerR : innerR;
                    double theta = i * angle - Math.PI / 2;
                    int px = (int) (x + r * Math.cos(theta));
                    int py = (int) (y + r * Math.sin(theta));
                    star.addPoint(px, py);
                }

                for (int i = 5; i > 0; i--) {
                    int alpha = (int) (30 * glowIntensity * (i / 5.0f));
                    g2d.setColor(new Color(255, 215, 0, alpha));
                    g2d.setStroke(new BasicStroke(i * 3));
                    g2d.draw(star);
                }

                RadialGradientPaint gradient = new RadialGradientPaint(
                        x, y, outerR,
                        new float[]{0f, 0.7f, 1f},
                        new Color[]{new Color(255, 255, 150), new Color(255, 215, 0), new Color(218, 165, 32)}
                );
                g2d.setPaint(gradient);
                g2d.fill(star);

                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(star);
            }
        };

        starPanel.setOpaque(false);
        starPanel.setPreferredSize(new Dimension(120, 120));
        return starPanel;
    }

    private void startAnimation() {
        animationTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (glowIncreasing) {
                    glowIntensity += 0.05f;
                    if (glowIntensity >= 1.0f) {
                        glowIntensity = 1.0f;
                        glowIncreasing = false;
                    }
                } else {
                    glowIntensity -= 0.05f;
                    if (glowIntensity <= 0.3f) {
                        glowIntensity = 0.3f;
                        glowIncreasing = true;
                    }
                }
                repaint();
            }
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Dark gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(10, 10, 30),
            getWidth(), getHeight(), new Color(40, 40, 60)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Subtle border
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 25, 25);

        g2d.dispose();
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
    }
}
