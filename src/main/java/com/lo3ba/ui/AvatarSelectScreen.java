package com.lo3ba.ui;

import com.lo3ba.gameobjects.Avatar;
import com.lo3ba.util.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class AvatarSelectScreen extends JPanel {
    private Avatar[] avatars;
    private int selectedIndex = 0;
    private OnAvatarSelectedListener listener;
    private Timer animationTimer;
    private float titleGlow = 0f;
    private boolean glowUp = true;


    public interface OnAvatarSelectedListener {
        void onAvatarSelected(Avatar avatar);
        void onBack();
    }

    public AvatarSelectScreen(OnAvatarSelectedListener listener) {
        this.listener = listener;
        this.avatars = Avatar.getAvailableAvatars();
        
        setLayout(null); // Absolute positioning for custom layout
        setOpaque(true);
        setPreferredSize(new Dimension(1000, 600));
        
        initUI();
        startAnimations();
        
        // Add component listener to handle resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                repositionElements();
            }
        });
    }

    private void initUI() {
        // Title label
        JLabel titleLabel = new JLabel("SELECT YOUR HERO", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                String text = getText();
                Font font = new Font("Impact", Font.BOLD, 48);
                g2d.setFont(font);
                
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                
                // Glow effect
                for (int i = 6; i > 0; i--) {
                    int alpha = (int) (20 * titleGlow * (i / 6.0f));
                    g2d.setColor(new Color(255, 215, 0, alpha));
                    g2d.drawString(text, x, y);
                }
                
                // Main text
                GradientPaint gradient = new GradientPaint(
                    x, y - fm.getAscent(), new Color(255, 255, 150),
                    x, y, new Color(255, 200, 50)
                );
                g2d.setPaint(gradient);
                g2d.drawString(text, x, y);
                
                g2d.dispose();
            }
        };
        titleLabel.setBounds(0, 30, 1000, 80);
        titleLabel.setForeground(new Color(255, 215, 0));
        add(titleLabel);

        // Avatar grid (4 avatars in a row)
        int startX = 100;
        int startY = 150;
        int avatarWidth = 180;
        int avatarHeight = 300;
        int spacing = 20;

        for (int i = 0; i < avatars.length; i++) {
            final int index = i;
            AvatarCard card = new AvatarCard(avatars[i], i == selectedIndex);
            card.setBounds(
                startX + i * (avatarWidth + spacing),
                startY,
                avatarWidth,
                avatarHeight
            );
            
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectAvatar(index);
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setHovered(true);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    card.setHovered(false);
                }
            });
            
            add(card);
        }

        // Buttons
        JButton selectButton = createButton("SELECT", new Color(76, 175, 80), 350, 500);
        selectButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                if (listener != null) {
                    listener.onAvatarSelected(avatars[selectedIndex]);
                }
            });
        });
        add(selectButton);

        JButton backButton = createButton("BACK", new Color(158, 158, 158), 550, 500);
        backButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                if (listener != null) {
                    listener.onBack();
                }
            });
        });
        add(backButton);
    }

    private void selectAvatar(int index) {
        selectedIndex = index;
        
        // Update all cards
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof AvatarCard) {
                AvatarCard card = (AvatarCard) comp;
                card.setSelected(false);
            }
        }
        
        // Select new card
        if (components[index + 1] instanceof AvatarCard) {
            ((AvatarCard) components[index + 1]).setSelected(true);
        }
        
        repaint();
    }

    private JButton createButton(String text, Color baseColor, int x, int y) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.fillRoundRect(4, 4, w - 8, h - 8, 20, 20);
                
                // Background
                GradientPaint gradient = new GradientPaint(0, 0, baseColor.brighter(), 0, h, baseColor.darker());
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, w, h, 20, 20);
                
                // Border
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, w - 2, h - 2, 20, 20);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBounds(x, y, 180, 60);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void repositionElements() {
        Component[] components = getComponents();
        int titleIndex = 0;
        int avatarStartIndex = 1;
        int selectButtonIndex = avatars.length + 1;
        int backButtonIndex = avatars.length + 2;
        
        // Reposition title
        if (components.length > titleIndex && components[titleIndex] instanceof JLabel) {
            components[titleIndex].setBounds(0, 30, getWidth(), 80);
        }
        
        // Reposition avatar cards
        int avatarWidth = 180;
        int avatarHeight = 300;
        int spacing = 20;
        int totalWidth = (avatarWidth * avatars.length) + (spacing * (avatars.length - 1));
        int startX = (getWidth() - totalWidth) / 2;
        int startY = 150;
        
        for (int i = 0; i < avatars.length; i++) {
            int componentIndex = avatarStartIndex + i;
            if (componentIndex < components.length && components[componentIndex] instanceof AvatarCard) {
                components[componentIndex].setBounds(
                    startX + i * (avatarWidth + spacing),
                    startY,
                    avatarWidth,
                    avatarHeight
                );
            }
        }
        
        // Reposition buttons
        int buttonY = 500;
        int buttonWidth = 180;
        int centerX = getWidth() / 2;
        
        if (components.length > selectButtonIndex && components[selectButtonIndex] instanceof JButton) {
            components[selectButtonIndex].setBounds(centerX - buttonWidth - 10, buttonY, buttonWidth, 60);
        }
        if (components.length > backButtonIndex && components[backButtonIndex] instanceof JButton) {
            components[backButtonIndex].setBounds(centerX + 10, buttonY, buttonWidth, 60);
        }
    }

    private void startAnimations() {
        animationTimer = new Timer(30, e -> {
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
        animationTimer.start();
    }

    private boolean isHiding = false;

    public void show() {
        setVisible(true);
        if (animationTimer != null && !animationTimer.isRunning()) {
            animationTimer.start();
        }
    }

public void hide() {
    if (isHiding) return; // Prevent re-entrance
    isHiding = true;
    try {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        setVisible(false);
    } finally {
        isHiding = false;
    }
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(20, 20, 40),
            0, getHeight(), new Color(40, 20, 60)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.dispose();
    }

    // Inner class for avatar cards
    private class AvatarCard extends JPanel {
        private Avatar avatar;
        private boolean selected;
        private boolean hovered;
        private BufferedImage avatarSprite;

        public AvatarCard(Avatar avatar, boolean selected) {
            this.avatar = avatar;
            this.selected = selected;
            this.hovered = false;
            
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Load avatar sprite
            avatarSprite = ResourceManager.loadTexture(avatar.getSpriteFile());
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        public void setHovered(boolean hovered) {
            this.hovered = hovered;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Card background
            Color bgColor = selected ? new Color(100, 150, 255, 200) : new Color(50, 50, 70, 180);
            if (hovered && !selected) {
                bgColor = new Color(80, 80, 100, 200);
            }
            
            g2d.setColor(bgColor);
            g2d.fillRoundRect(5, 5, w - 10, h - 10, 20, 20);

            // Border
            if (selected) {
                g2d.setColor(new Color(255, 215, 0));
                g2d.setStroke(new BasicStroke(3));
            } else {
                g2d.setColor(new Color(200, 200, 200, 150));
                g2d.setStroke(new BasicStroke(2));
            }
            g2d.drawRoundRect(5, 5, w - 10, h - 10, 20, 20);

            // Draw avatar sprite (scaled)
            if (avatarSprite != null) {
                int spriteSize = 100;
                int spriteX = (w - spriteSize) / 2;
                int spriteY = 40;
                g2d.drawImage(avatarSprite, spriteX, spriteY, spriteSize, spriteSize, null);
            }

            // Avatar name
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.setColor(Color.WHITE);
            FontMetrics fm = g2d.getFontMetrics();
            String name = avatar.getName();
            int nameX = (w - fm.stringWidth(name)) / 2;
            g2d.drawString(name, nameX, 170);

            // Ability
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.setColor(new Color(255, 215, 0));
            String ability = avatar.getAbility().getDisplayName();
            int abilityX = (w - fm.stringWidth(ability)) / 2;
            g2d.drawString(ability, abilityX, 195);

            // Description (wrapped)
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            g2d.setColor(new Color(200, 200, 200));
            String desc = avatar.getDescription();
            drawCenteredText(g2d, desc, 220, w - 20);

            g2d.dispose();
        }

        private void drawCenteredText(Graphics2D g2d, String text, int y, int maxWidth) {
            FontMetrics fm = g2d.getFontMetrics();
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();
            int lineY = y;

            for (String word : words) {
                String testLine = line.length() == 0 ? word : line + " " + word;
                if (fm.stringWidth(testLine) > maxWidth) {
                    if (line.length() > 0) {
                        int x = (getWidth() - fm.stringWidth(line.toString())) / 2;
                        g2d.drawString(line.toString(), x, lineY);
                        lineY += fm.getHeight();
                        line = new StringBuilder(word);
                    }
                } else {
                    line = new StringBuilder(testLine);
                }
            }

            if (line.length() > 0) {
                int x = (getWidth() - fm.stringWidth(line.toString())) / 2;
                g2d.drawString(line.toString(), x, lineY);
            }
        }
    }
}
