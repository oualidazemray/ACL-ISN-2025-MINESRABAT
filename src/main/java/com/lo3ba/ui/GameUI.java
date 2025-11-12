package com.lo3ba.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.io.InputStream;

public class GameUI {
    private JButton replayButton;
    private JButton quitButton;
    private JButton homeButton;
    private OnButtonClickListener listener;
    private JPanel parent;

    public interface OnButtonClickListener {
        void onRepeat();
        void onQuit();
        void onHome();
    }

    public GameUI(JPanel parent, int width, int height, OnButtonClickListener listener) {
        this.listener = listener;
        this.parent = parent;
        createButtons(parent, width, height);
    }

    private void createButtons(JPanel parent, int width, int height) {
        replayButton = createModernButton("↻ REPEAT", new Color(33, 150, 243), new Color(25, 118, 210));
        homeButton = createModernButton("⌂ HOME", new Color(255, 152, 0), new Color(245, 124, 0));
        quitButton = createModernButton("✕ QUIT", new Color(244, 67, 54), new Color(211, 47, 47));

        positionButtons(width, height);

        // Button actions
        replayButton.addActionListener(e -> {
            if (listener != null) listener.onRepeat();
        });

        quitButton.addActionListener(e -> {
            if (listener != null) listener.onQuit();
        });

        homeButton.addActionListener(e -> {
            if (listener != null) listener.onHome();
        });

        // Initially hidden
        replayButton.setVisible(false);
        quitButton.setVisible(false);
        homeButton.setVisible(false);

        // Add to parent
        parent.add(replayButton);
        parent.add(quitButton);
        parent.add(homeButton);
    }

    private void positionButtons(int width, int height) {
        int buttonWidth = 200;
        int buttonHeight = 60;
        int spacing = 15;
        int centerX = width / 2 - buttonWidth / 2;
        int startY = height / 2 + 30;

        replayButton.setBounds(centerX, startY, buttonWidth, buttonHeight);
        homeButton.setBounds(centerX, startY + buttonHeight + spacing, buttonWidth, buttonHeight);
        quitButton.setBounds(centerX, startY + (buttonHeight + spacing) * 2, buttonWidth, buttonHeight);
    }

    public void updatePositions(int width, int height) {
        positionButtons(width, height);
    }

    private JButton createModernButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 120));
                g2d.fillRoundRect(3, 3, w - 6, h - 6, 20, 20);

                // Use hover color if hovered, base color otherwise
                Boolean hovered = (Boolean) getClientProperty("isHovered");
                boolean isHovered = hovered != null && hovered;
                Color currentTop = isHovered ? hoverColor.brighter() : baseColor;
                Color currentBottom = isHovered ? hoverColor : baseColor.darker();

                // Button background with gradient
                GradientPaint gradient = new GradientPaint(0, 0, currentTop, 0, h, currentBottom);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, w, h, 20, 20);

                // Shine effect
                g2d.setColor(new Color(255, 255, 255, isHovered ? 60 : 40));
                g2d.fillRoundRect(5, 5, w - 10, h / 3, 15, 15);

                // Border
                g2d.setColor(new Color(255, 255, 255, isHovered ? 200 : 120));
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(1, 1, w - 2, h - 2, 20, 20);

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        // Load font
        try {
            InputStream is = GameUI.class.getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (is != null) {
                Font retroFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(16f);
                button.setFont(retroFont);
            } else {
                button.setFont(new Font("Arial", Font.BOLD, 18));
            }
        } catch (Exception e) {
            e.printStackTrace();
            button.setFont(new Font("Arial", Font.BOLD, 18));
        }

        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JButton)e.getSource()).putClientProperty("isHovered", true);
                ((JButton)e.getSource()).repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JButton)e.getSource()).putClientProperty("isHovered", false);
                ((JButton)e.getSource()).repaint();
            }
        });

        return button;
    }

    public void show() {
        replayButton.setVisible(true);
        quitButton.setVisible(true);
        homeButton.setVisible(true);
    }

    public void hide() {
        replayButton.setVisible(false);
        quitButton.setVisible(false);
        homeButton.setVisible(false);
    }
}