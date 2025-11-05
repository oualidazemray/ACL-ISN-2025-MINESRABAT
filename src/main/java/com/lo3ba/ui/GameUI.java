package com.lo3ba.ui;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class GameUI {
    private JButton replayButton;
    private JButton quitButton;
    private OnButtonClickListener listener;
    private JPanel parent;

    public interface OnButtonClickListener {
        void onReplay();
        void onQuit();
    }

    public GameUI(JPanel parent, int width, int height, OnButtonClickListener listener) {
        this.listener = listener;
        this.parent = parent;
        createButtons(parent, width, height);
    }

    private void createButtons(JPanel parent, int width, int height) {
        replayButton = createRetroButton("REPLAY");
        quitButton = createRetroButton("QUIT");

        positionButtons(width, height);

        // Button actions
        replayButton.addActionListener(e -> {
            if (listener != null) listener.onReplay();
        });

        quitButton.addActionListener(e -> {
            if (listener != null) listener.onQuit();
        });

        // Initially hidden
        replayButton.setVisible(false);
        quitButton.setVisible(false);

        // Add to parent
        parent.add(replayButton);
        parent.add(quitButton);
    }

    private void positionButtons(int width, int height) {
        int buttonWidth = 250;
        int buttonHeight = 70;
        int centerX = width / 2 - buttonWidth / 2;

        replayButton.setBounds(centerX, height / 2 + 50, buttonWidth, buttonHeight);
        quitButton.setBounds(centerX, height / 2 + 130, buttonWidth, buttonHeight);
    }

    public void updatePositions(int width, int height) {
        positionButtons(width, height);
    }

    private JButton createRetroButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Anti-aliasing OFF for pixel-perfect look
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                    RenderingHints.VALUE_ANTIALIAS_OFF);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                
                // Draw background with gradient
                if (getModel().isRollover()) {
                    // Hover state - bright red/orange gradient
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 50, 0),
                        0, getHeight(), new Color(200, 0, 0)
                    );
                    g2d.setPaint(gp);
                } else {
                    // Normal state - black background
                    g2d.setColor(Color.BLACK);
                }
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw pixelated border (thick)
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 0)); // Yellow border on hover
                } else {
                    g2d.setColor(new Color(255, 100, 0)); // Orange border
                }
                
                // Outer border (thick pixel style)
                for (int i = 0; i < 5; i++) {
                    g2d.drawRect(i, i, getWidth() - 1 - (i * 2), getHeight() - 1 - (i * 2));
                }
                
                // Draw text with gradient effect
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textHeight) / 2 - 4;
                
                if (getModel().isRollover()) {
                    // Hover text - bright yellow
                    g2d.setColor(new Color(255, 255, 100));
                } else {
                    // Normal text - gradient from orange to yellow
                    GradientPaint textGp = new GradientPaint(
                        x, y - textHeight, new Color(255, 200, 0),
                        x, y, new Color(255, 100, 0)
                    );
                    g2d.setPaint(textGp);
                }
                
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };

        // Load Press Start 2P font
        try {
            InputStream is = GameUI.class.getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (is != null) {
                Font retroFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f);
                button.setFont(retroFont);
            } else {
                System.err.println("⚠ Font not found: /fonts/PressStart2P-Regular.ttf");
                button.setFont(new Font("Monospaced", Font.BOLD, 18));
            }
        } catch (Exception e) {
            e.printStackTrace();
            button.setFont(new Font("Monospaced", Font.BOLD, 18));
        }

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public void show() {
        replayButton.setVisible(true);
        quitButton.setVisible(true);
    }

    public void hide() {
        replayButton.setVisible(false);
        quitButton.setVisible(false);
    }
}