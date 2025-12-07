package com.lo3ba.ui;

import com.lo3ba.util.ScaleManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

/**
 * Professional pause menu overlay for the game
 */
public class PauseMenu extends JPanel {
    private JButton resumeButton;
    private JButton mainMenuButton;
    private JButton levelsButton;
    private JButton exitButton;
    private OnMenuActionListener listener;
    private boolean isVisible = false;

    public interface OnMenuActionListener {
        void onResume();
        void onMainMenu();
        void onLevels();
        void onExit();
    }

    public PauseMenu(OnMenuActionListener listener) {
        this.listener = listener;
        setLayout(null);
        setOpaque(false);
        createMenuButtons();
        setVisible(false);
    }

    private void createMenuButtons() {
        // Create buttons with retro styling
        resumeButton = createRetroButton("▶ RESUME", new Color(46, 204, 113), new Color(39, 174, 96));
        mainMenuButton = createRetroButton("⌂ MAIN MENU", new Color(52, 152, 219), new Color(41, 128, 185));
        levelsButton = createRetroButton("◉ LEVELS", new Color(155, 89, 182), new Color(142, 68, 173));
        exitButton = createRetroButton("✕ EXIT", new Color(231, 76, 60), new Color(192, 57, 43));

        positionButtons();

        // Button actions
        resumeButton.addActionListener(e -> {
            if (listener != null) listener.onResume();
        });

        mainMenuButton.addActionListener(e -> {
            if (listener != null) listener.onMainMenu();
        });

        levelsButton.addActionListener(e -> {
            if (listener != null) listener.onLevels();
        });

        exitButton.addActionListener(e -> {
            if (listener != null) listener.onExit();
        });

        add(resumeButton);
        add(mainMenuButton);
        add(levelsButton);
        add(exitButton);
    }

    private void positionButtons() {
        int baseWidth = ScaleManager.BASE_WIDTH;
        int baseHeight = ScaleManager.BASE_HEIGHT;
        
        int buttonWidth = 320;
        int buttonHeight = 70;
        int spacing = 20;
        
        // Center horizontally
        int centerX = (baseWidth - buttonWidth) / 2;
        
        // Position vertically centered
        int totalHeight = (buttonHeight * 4) + (spacing * 3);
        int startY = (baseHeight - totalHeight) / 2;

        resumeButton.setBounds(centerX, startY, buttonWidth, buttonHeight);
        mainMenuButton.setBounds(centerX, startY + (buttonHeight + spacing), buttonWidth, buttonHeight);
        levelsButton.setBounds(centerX, startY + (buttonHeight + spacing) * 2, buttonWidth, buttonHeight);
        exitButton.setBounds(centerX, startY + (buttonHeight + spacing) * 3, buttonWidth, buttonHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isVisible) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Semi-transparent dark overlay
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Draw "PAUSED" title
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            
            // Load retro font for title
            Font titleFont;
            try {
                InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
                if (is != null) {
                    titleFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(48f);
                } else {
                    titleFont = new Font("Monospaced", Font.BOLD, 48);
                }
            } catch (Exception e) {
                titleFont = new Font("Monospaced", Font.BOLD, 48);
            }
            
            g2d.setFont(titleFont);
            String title = "PAUSED";
            FontMetrics fm = g2d.getFontMetrics();
            int titleX = (ScaleManager.BASE_WIDTH - fm.stringWidth(title)) / 2;
            int titleY = 100;
            
            // Title shadow
            g2d.setColor(Color.BLACK);
            g2d.drawString(title, titleX + 3, titleY + 3);
            
            // Title text
            g2d.setColor(new Color(255, 215, 0));
            g2d.drawString(title, titleX, titleY);
            
            // Hint text
            Font hintFont = new Font("Monospaced", Font.PLAIN, 14);
            g2d.setFont(hintFont);
            String hint = "Press F or ESC to resume";
            fm = g2d.getFontMetrics();
            int hintX = (ScaleManager.BASE_WIDTH - fm.stringWidth(hint)) / 2;
            int hintY = ScaleManager.BASE_HEIGHT - 50;
            
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawString(hint, hintX, hintY);
            
            g2d.dispose();
        }
    }

    @Override
    public void setVisible(boolean visible) {
        this.isVisible = visible;
        super.setVisible(visible);
        repaint();
    }

    private JButton createRetroButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                int w = getWidth();
                int h = getHeight();

                Boolean hovered = (Boolean) getClientProperty("isHovered");
                boolean isHovered = hovered != null && hovered;
                
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 220));
                g2d.fillRect(4, 4, w - 2, h - 2);

                // Button background
                Color currentColor = isHovered ? hoverColor.brighter() : baseColor;
                g2d.setColor(currentColor);
                g2d.fillRect(0, 0, w - 4, h - 4);
                
                // 3D border
                g2d.setColor(currentColor.brighter().brighter());
                g2d.fillRect(0, 0, w - 4, 3);
                g2d.fillRect(0, 0, 3, h - 4);
                
                g2d.setColor(currentColor.darker().darker());
                g2d.fillRect(0, h - 7, w - 4, 3);
                g2d.fillRect(w - 7, 0, 3, h - 4);

                // Text
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                
                g2d.setColor(Color.BLACK);
                int textX = (w - fm.stringWidth(text)) / 2 + 2;
                int textY = ((h - fm.getHeight()) / 2) + fm.getAscent() + 2;
                g2d.drawString(text, textX, textY);
                
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, textX - 2, textY - 2);

                g2d.dispose();
            }
        };

        try {
            InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (is != null) {
                Font retroFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f);
                button.setFont(retroFont);
            } else {
                button.setFont(new Font("Monospaced", Font.BOLD, 18));
            }
        } catch (Exception e) {
            button.setFont(new Font("Monospaced", Font.BOLD, 18));
        }

        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
