package com.lo3ba;

import com.lo3ba.core.GameLoop;
import com.lo3ba.ui.MainMenuScreen;
import com.lo3ba.ui.LevelSelectScreen;
import javax.swing.*;
import java.awt.*;

public class Main {
    private static JFrame frame;
    private static MainMenuScreen mainMenuScreen;
    private static LevelSelectScreen levelSelectScreen;
    private static GameLoop gameLoop;
    private static int currentLevel = 1;
    private static int maxUnlockedLevel = 10;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Lo3ba Game - Devil Evil 2 Style");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);

            showMainMenu();

            frame.setVisible(true);
        });
    }

    private static void showMainMenu() {
        // Hide previous screens to stop their timers
        if (levelSelectScreen != null) {
            levelSelectScreen.hide();
        }
        if (gameLoop != null) {
            gameLoop.stop();
        }

        if (mainMenuScreen == null) {
            mainMenuScreen = new MainMenuScreen(new MainMenuScreen.OnButtonClickListener() {
                @Override
                public void onPlay() {
                    SwingUtilities.invokeLater(() -> startGame(currentLevel));
                }

                @Override
                public void onRepeat() {
                    SwingUtilities.invokeLater(() -> startGame(currentLevel));
                }

                @Override
                public void onQuit() {
                    System.exit(0);
                }

                @Override
                public void onLevels() {
                    SwingUtilities.invokeLater(() -> showLevelSelect());
                }
            });
        }

        frame.getContentPane().removeAll();
        frame.add(mainMenuScreen);
        frame.revalidate();
        frame.repaint();
        frame.requestFocusInWindow();
    }

    private static void startGame(int startLevel) {
        System.out.println("Starting game for level " + startLevel);
        // Hide previous screens to stop their timers
        if (mainMenuScreen != null) {
            // MainMenuScreen doesn't have a hide method, but we can assume it's not running timers when not visible
        }
        if (levelSelectScreen != null) {
            levelSelectScreen.hide();
        }

        if (gameLoop != null) {
            gameLoop.stop();
        }

        gameLoop = new GameLoop(startLevel, () -> {
            // Callback when level is completed
            int completedLevel = gameLoop.getCurrentLevel();
            maxUnlockedLevel = Math.max(maxUnlockedLevel, completedLevel + 1);
            currentLevel = completedLevel + 1;
            if (currentLevel > 10) {
                currentLevel = 1; // Loop back to level 1
            }
        }, () -> {
            // Callback to return to main menu
            SwingUtilities.invokeLater(() -> {
                gameLoop.stop();
                showMainMenu();
            });
        });


        frame.getContentPane().removeAll();
        frame.add(gameLoop);
    
        frame.revalidate();

        frame.repaint();
  
        frame.requestFocusInWindow();

        gameLoop.requestFocusInWindow();
       
        gameLoop.start();
      
    }

    private static void showLevelSelect() {
        // Hide previous screens to stop their timers
        if (mainMenuScreen != null) {
            // MainMenuScreen doesn't have a hide method, but we can assume it's not running timers when not visible
        }
        if (gameLoop != null) {
            gameLoop.stop();
        }

        if (levelSelectScreen == null) {
            levelSelectScreen = new LevelSelectScreen(maxUnlockedLevel, new LevelSelectScreen.OnLevelSelectedListener() {
                @Override
                public void onLevelSelected(int level) {
                    SwingUtilities.invokeLater(() -> {
                        currentLevel = level;
                        startGame(level);
                    });
                }

                @Override
                public void onBack() {
                    SwingUtilities.invokeLater(() -> showMainMenu());
                }
            });
        } else {
            levelSelectScreen.updateUnlockedLevels(maxUnlockedLevel);
            levelSelectScreen.show(); // Restart timers when showing again
        }

        frame.getContentPane().removeAll();
        frame.add(levelSelectScreen);
        frame.revalidate();
        frame.repaint();
        frame.requestFocusInWindow();
    }
}
