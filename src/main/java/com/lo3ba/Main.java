package com.lo3ba;

import com.lo3ba.core.GameLoop;
import com.lo3ba.ui.LevelSelectScreen;
import javax.swing.*;
import java.awt.*;

public class Main {
    private static JFrame frame;
    private static LevelSelectScreen levelSelectScreen;
    private static GameLoop gameLoop;
    private static int maxUnlockedLevel = 1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Lo3ba Game - Devil Evil 2 Style");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setMinimumSize(new Dimension(800, 600));
            
            showLevelSelect();
            
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void showLevelSelect() {
        if (levelSelectScreen == null) {
            levelSelectScreen = new LevelSelectScreen(level -> {
                startGame(level);
            });
        }
        
        levelSelectScreen.setUnlockedLevels(maxUnlockedLevel);
        frame.getContentPane().removeAll();
        frame.add(levelSelectScreen);
        frame.pack();
        frame.setSize(1000, 600);
        frame.revalidate();
        frame.repaint();
    }

    private static void startGame(int startLevel) {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        
        gameLoop = new GameLoop(startLevel, () -> {
            // Callback when level is completed
            int completedLevel = gameLoop.getCurrentLevel();
            if (completedLevel >= maxUnlockedLevel) {
                maxUnlockedLevel = completedLevel + 1;
            }
        }, () -> {
            // Callback to return to level select
            gameLoop.stop();
            showLevelSelect();
        });
        
        frame.getContentPane().removeAll();
        frame.add(gameLoop);
        frame.pack();
        frame.revalidate();
        frame.repaint();
        
        gameLoop.requestFocusInWindow();
        gameLoop.start();
    }
}