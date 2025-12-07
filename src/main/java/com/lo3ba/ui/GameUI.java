package com.lo3ba.ui;

import com.lo3ba.util.ScaleManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

public class GameUI extends JPanel {
    private JButton replayButton;
    private JButton quitButton;
    private JButton homeButton;
    private OnButtonClickListener listener;

    public interface OnButtonClickListener {
        void onRepeat();
        void onQuit();
        void onHome();
    }

    public GameUI(int width, int height, OnButtonClickListener listener) {
        this.listener = listener;
        setLayout(null);
        setOpaque(false); // Transparent so game is visible behind
        createButtons(width, height);
    }

    private void createButtons(int width, int height) {
        replayButton = createRetroButton("↻ REPEAT", new Color(33, 150, 243), new Color(25, 118, 210));
        homeButton = createRetroButton("⌂ HOME", new Color(255, 152, 0), new Color(245, 124, 0));
        quitButton = createRetroButton("✕ QUIT", new Color(244, 67, 54), new Color(211, 47, 47));

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

        // Add to self
        add(replayButton);
        add(quitButton);
        add(homeButton);
    }

    private void positionButtons(int width, int height) {
        int buttonWidth = 280;
        int buttonHeight = 70;
        int spacing = 20;
        
        // Center horizontally using ACTUAL window width
        int centerX = (width - buttonWidth) / 2;
        
        // Position vertically in center area using ACTUAL window height
        int totalHeight = (buttonHeight * 3) + (spacing * 2);
        int startY = (height - totalHeight) / 2 + 30;

        replayButton.setBounds(centerX, startY, buttonWidth, buttonHeight);
        homeButton.setBounds(centerX, startY + buttonHeight + spacing, buttonWidth, buttonHeight);
        quitButton.setBounds(centerX, startY + (buttonHeight + spacing) * 2, buttonWidth, buttonHeight);
    }

    public void updatePositions(int width, int height) {
        positionButtons(width, height);
    }

    private JButton createRetroButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                // Pixel-perfect rendering for retro look
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

                int w = getWidth();
                int h = getHeight();

                // Check hover state
                Boolean hovered = (Boolean) getClientProperty("isHovered");
                boolean isHovered = hovered != null && hovered;
                
                // Pixel art style shadow
                g2d.setColor(new Color(0, 0, 0, 200));
                g2d.fillRect(4, 4, w - 2, h - 2);

                // Main button background
                Color currentColor = isHovered ? hoverColor.brighter() : baseColor;
                g2d.setColor(currentColor);
                g2d.fillRect(0, 0, w - 4, h - 4);
                
                // Pixel art border (light top-left, dark bottom-right)
                g2d.setColor(currentColor.brighter().brighter());
                g2d.fillRect(0, 0, w - 4, 3); // Top
                g2d.fillRect(0, 0, 3, h - 4); // Left
                
                g2d.setColor(currentColor.darker().darker());
                g2d.fillRect(0, h - 7, w - 4, 3); // Bottom
                g2d.fillRect(w - 7, 0, 3, h - 4); // Right

                // Draw text with retro font
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                
                // Text shadow
                g2d.setColor(Color.BLACK);
                int textX = (w - fm.stringWidth(text)) / 2 + 2;
                int textY = ((h - fm.getHeight()) / 2) + fm.getAscent() + 2;
                g2d.drawString(text, textX, textY);
                
                // Main text
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, textX - 2, textY - 2);

                g2d.dispose();
            }
        };

        // Load retro font
        try {
            InputStream is = GameUI.class.getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (is != null) {
                Font retroFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f);
                button.setFont(retroFont);
            } else {
                button.setFont(new Font("Monospaced", Font.BOLD, 18));
            }
        } catch (Exception e) {
            e.printStackTrace();
            button.setFont(new Font("Monospaced", Font.BOLD, 18));
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
}