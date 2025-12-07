package com.lo3ba.core;

import com.lo3ba.ui.GameUI;
import com.lo3ba.ui.PauseMenu;
import com.lo3ba.ui.LevelVictoryScreen;
import com.lo3ba.ui.FinalVictoryScreen;
import com.lo3ba.util.ScaleManager;

import javax.swing.*;
import java.awt.*;

/**
 * Manages all UI screens and components for the game.
 * Extracted from GameLoop to follow Single Responsibility Principle.
 * 
 * Responsibilities:
 * - Create and configure all UI screens (death, victory, pause)
 * - Create and manage pause button
 * - Handle UI visibility state
 * - Update UI bounds on window resize
 * - Route UI callbacks to game logic
 */
public class GameUIManager {
    private final JPanel parentPanel;
    private final UICallbacks callbacks;
    
    // UI Screens
    private GameUI gameUI;
    private LevelVictoryScreen levelVictoryScreen;
    private FinalVictoryScreen finalVictoryScreen;
    private PauseMenu pauseMenu;
    
    // UI Components
    private JButton pauseButton;
    
    // UI State
    private boolean victoryScreenShown = false;
    private boolean gameUIShown = false;
    private boolean isPaused = false;
    
    /**
     * Creates a new GameUIManager.
     * 
     * @param parentPanel Panel to add UI components to
     * @param callbacks Callbacks for UI events
     */
    public GameUIManager(JPanel parentPanel, UICallbacks callbacks) {
        this.parentPanel = parentPanel;
        this.callbacks = callbacks;
    }
    
    /**
     * Initialize all UI screens and components.
     * Must be called after construction.
     */
    public void setupUI() {
        createGameUI();
        createLevelVictoryScreen();
        createFinalVictoryScreen();
        createPauseMenu();
        createPauseButton();
        
        addComponentsToPanel();
        updateBounds();
        hideAllScreens();
    }
    
    /**
     * Create the death screen UI.
     */
    private void createGameUI() {
        gameUI = new GameUI(GameLoop.BASE_WIDTH, GameLoop.BASE_HEIGHT, new GameUI.OnButtonClickListener() {
            @Override
            public void onRepeat() {
                callbacks.onRepeatLevel();
                hideGameUI();
                parentPanel.requestFocusInWindow();
            }

            @Override
            public void onQuit() {
                callbacks.onStopGame();
                callbacks.onExit();
            }

            @Override
            public void onHome() {
                callbacks.onStopGame();
                callbacks.onReturnToMenu();
            }
        });
    }
    
    /**
     * Create the level victory screen.
     */
    private void createLevelVictoryScreen() {
        levelVictoryScreen = new LevelVictoryScreen(new LevelVictoryScreen.OnVictoryActionListener() {
            @Override
            public void onNextLevel() {
                callbacks.onNextLevel();
                hideVictoryScreen();
                parentPanel.requestFocusInWindow();
                callbacks.onLevelComplete();
            }

            @Override
            public void onRetry() {
                callbacks.onRepeatLevel();
                hideVictoryScreen();
                parentPanel.requestFocusInWindow();
            }

            @Override
            public void onMainMenu() {
                callbacks.onStopGame();
                callbacks.onReturnToMenu();
            }
        });
    }
    
    /**
     * Create the final victory screen (after level 10).
     */
    private void createFinalVictoryScreen() {
        finalVictoryScreen = new FinalVictoryScreen(new FinalVictoryScreen.OnFinalVictoryActionListener() {
            @Override
            public void onPlayAgain() {
                callbacks.onStopGame();
                callbacks.onReturnToMenu();
            }

            @Override
            public void onMainMenu() {
                callbacks.onStopGame();
                callbacks.onReturnToMenu();
            }

            @Override
            public void onExit() {
                callbacks.onExit();
            }
        });
    }
    
    /**
     * Create the pause menu.
     */
    private void createPauseMenu() {
        pauseMenu = new PauseMenu(new PauseMenu.OnMenuActionListener() {
            @Override
            public void onResume() {
                togglePause();
            }
            
            @Override
            public void onMainMenu() {
                isPaused = false;
                pauseMenu.setVisible(false);
                callbacks.onStopGame();
                callbacks.onReturnToMenu();
            }
            
            @Override
            public void onLevels() {
                isPaused = false;
                pauseMenu.setVisible(false);
                callbacks.onStopGame();
                callbacks.onReturnToMenu();
            }
            
            @Override
            public void onExit() {
                callbacks.onExit();
            }
        });
    }
    
    /**
     * Create the pause button (hamburger menu in top-right).
     */
    private void createPauseButton() {
        pauseButton = new JButton("☰") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                int w = getWidth();
                int h = getHeight();

                Boolean hovered = (Boolean) getClientProperty("isHovered");
                boolean isHovered = hovered != null && hovered;
                
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRect(3, 3, w - 2, h - 2);

                // Button background
                Color bgColor = isHovered ? new Color(255, 215, 0) : new Color(255, 152, 0);
                g2d.setColor(bgColor);
                g2d.fillRect(0, 0, w - 3, h - 3);
                
                // 3D border
                g2d.setColor(bgColor.brighter());
                g2d.fillRect(0, 0, w - 3, 2);
                g2d.fillRect(0, 0, 2, h - 3);
                
                g2d.setColor(bgColor.darker().darker());
                g2d.fillRect(0, h - 5, w - 3, 2);
                g2d.fillRect(w - 5, 0, 2, h - 3);

                // Draw hamburger icon (☰)
                g2d.setFont(new Font("Monospaced", Font.BOLD, 28));
                FontMetrics fm = g2d.getFontMetrics();
                
                // Icon shadow
                g2d.setColor(new Color(0, 0, 0, 150));
                int textX = (w - fm.stringWidth("☰")) / 2 + 1;
                int textY = ((h - fm.getHeight()) / 2) + fm.getAscent() + 1;
                g2d.drawString("☰", textX, textY);
                
                // Icon
                g2d.setColor(Color.WHITE);
                g2d.drawString("☰", textX - 1, textY - 1);

                g2d.dispose();
            }
        };
        
        pauseButton.setFocusPainted(false);
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setOpaque(false);
        pauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pauseButton.setBounds(0, 0, 50, 50); // Will be updated in updateBounds
        pauseButton.setLayout(null);
        
        // Click action
        pauseButton.addActionListener(e -> togglePause());
        
        // Hover effect
        pauseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                pauseButton.putClientProperty("isHovered", true);
                pauseButton.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                pauseButton.putClientProperty("isHovered", false);
                pauseButton.repaint();
            }
        });
    }
    
    /**
     * Add all UI components to the parent panel.
     */
    private void addComponentsToPanel() {
        parentPanel.add(gameUI);
        parentPanel.add(levelVictoryScreen);
        parentPanel.add(finalVictoryScreen);
        parentPanel.add(pauseMenu);
        parentPanel.add(pauseButton);
    }
    
    /**
     * Hide all UI screens initially.
     */
    private void hideAllScreens() {
        gameUI.setVisible(false);
        levelVictoryScreen.setVisible(false);
        finalVictoryScreen.setVisible(false);
        pauseMenu.setVisible(false);
    }
    
    /**
     * Update bounds of all UI components based on current window size.
     */
    public void updateBounds() {
        ScaleManager sm = ScaleManager.getInstance();
        int width = sm.getCurrentWidth();
        int height = sm.getCurrentHeight();
        
        if (gameUI != null) {
            gameUI.setBounds(0, 0, width, height);
            gameUI.updatePositions(width, height);
        }
        if (levelVictoryScreen != null) {
            levelVictoryScreen.setBounds(0, 0, width, height);
        }
        if (finalVictoryScreen != null) {
            finalVictoryScreen.setBounds(0, 0, width, height);
        }
        if (pauseMenu != null) {
            pauseMenu.setBounds(0, 0, width, height);
        }
        if (pauseButton != null) {
            // Position in top-right corner
            int buttonSize = 50;
            int margin = 20;
            pauseButton.setBounds(width - buttonSize - margin, margin, buttonSize, buttonSize);
        }
    }
    
    /**
     * Show the victory screen (either level or final victory).
     * 
     * @param isFinalLevel Whether this is the final level (level 10)
     * @param data Victory statistics to display
     */
    public void showVictoryScreen(boolean isFinalLevel, VictoryData data) {
        victoryScreenShown = true;
        
        if (isFinalLevel) {
            parentPanel.setComponentZOrder(finalVictoryScreen, 0);
            finalVictoryScreen.setVisible(true);
        } else {
            levelVictoryScreen.setStats(data.getCollectedStars(), data.getTotalStars(), 
                                       data.getDeathCount(), null);
            levelVictoryScreen.setVisible(true);
            parentPanel.setComponentZOrder(levelVictoryScreen, 0);
        }
        parentPanel.revalidate();
        parentPanel.repaint();
    }
    
    /**
     * Hide the victory screen.
     */
    public void hideVictoryScreen() {
        if (victoryScreenShown) {
            levelVictoryScreen.setVisible(false);
            finalVictoryScreen.setVisible(false);
            victoryScreenShown = false;
            parentPanel.revalidate();
            parentPanel.repaint();
        }
    }
    
    /**
     * Show the game UI (death screen).
     */
    public void showGameUI() {
        if (!gameUIShown) {
            gameUIShown = true;
            gameUI.setVisible(true);
        }
    }
    
    /**
     * Hide the game UI (death screen).
     */
    public void hideGameUI() {
        if (gameUIShown) {
            gameUI.setVisible(false);
            gameUIShown = false;
        }
    }
    
    /**
     * Toggle the pause menu visibility.
     */
    public void togglePause() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);
        if (isPaused) {
            parentPanel.setComponentZOrder(pauseMenu, 0);
        }
        parentPanel.requestFocusInWindow();
        callbacks.onTogglePause();
    }
    
    /**
     * Check if the game is currently paused.
     */
    public boolean isPaused() {
        return isPaused;
    }
    
    /**
     * Get the pause button component.
     */
    public JButton getPauseButton() {
        return pauseButton;
    }
    
    /**
     * Check if victory screen is currently shown.
     */
    public boolean isVictoryScreenShown() {
        return victoryScreenShown;
    }
}
