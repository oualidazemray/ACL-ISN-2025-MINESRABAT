package com.lo3ba.core;

import com.lo3ba.levels.Level;
import com.lo3ba.ui.GameUI;
import com.lo3ba.ui.VictoryScreen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class GameLoop extends JPanel {
    public static final int BASE_WIDTH = 1000;
    public static final int BASE_HEIGHT = 600;
    private static final int FPS = 60;

    private Thread gameThread;
    private volatile boolean running = false;

    private Player player;
    private LevelManager levelManager;
    private InputHandler inputHandler;
    private GameUI gameUI;
    private VictoryScreen victoryScreen;

    private BufferedImage backgroundImg;
    private Font retroFont;

    private boolean victoryScreenShown = false;
    private boolean gameUIShown = false;

    private boolean debugOverlay = false; // Toggle with F3

    private Runnable onLevelComplete;
    private Runnable onReturnToMenu;

    public GameLoop() {
        this(1, null, null);
    }

    public GameLoop(int startLevel, Runnable onLevelComplete, Runnable onReturnToMenu) {
        System.out.println("GameLoop constructor called with startLevel: " + startLevel);
        this.onLevelComplete = onLevelComplete;
        this.onReturnToMenu = onReturnToMenu;

        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));
        setBackground(new Color(135, 206, 235)); // Sky blue
        setFocusable(true);
        setLayout(new BorderLayout());

        loadFont();
        init(startLevel);
        setupUI();
        System.out.println("GameLoop constructor finished");
    }

    private void loadFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (is != null) {
                retroFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(16f);
            } else {
                System.err.println("⚠ Font not found: /fonts/PressStart2P-Regular.ttf");
                retroFont = new Font("Monospaced", Font.BOLD, 16);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retroFont = new Font("Monospaced", Font.BOLD, 16);
        }
    }

    private void init(int startLevel) {
        // Initialize at default position first
        player = new Player(100, 400);
        levelManager = new LevelManager(player, startLevel);
        inputHandler = new InputHandler(player);

        // Key listener: send movement keys to inputHandler; F3 toggles debug overlay
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F3) {
                    debugOverlay = !debugOverlay;
                    repaint();
                    return;
                }
                inputHandler.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                inputHandler.keyReleased(e);
            }
        });

        // Load background image
        try {
            backgroundImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/textures/background.png"));
        } catch (IOException e) {
            System.err.println("⚠ Failed to load background image: assets/textures/background.png");
            e.printStackTrace();
        }
    }

    private void setupUI() {
        gameUI = new GameUI(this, BASE_WIDTH, BASE_HEIGHT, new GameUI.OnButtonClickListener() {
            @Override
            public void onRepeat() {
                levelManager.resetCurrentLevel();
                player.reset(levelManager.getCurrentLevel().getSpawnX(),
                             levelManager.getCurrentLevel().getSpawnY());
                hideGameUI();
                requestFocusInWindow();
            }

            @Override
            public void onQuit() {
                stop();
                if (onReturnToMenu != null) {
                    onReturnToMenu.run();
                } else {
                    System.exit(0);
                }
            }

            @Override
            public void onHome() {
                stop();
                if (onReturnToMenu != null) {
                    onReturnToMenu.run();
                } else {
                    System.exit(0);
                }
            }
        });

        victoryScreen = new VictoryScreen(new VictoryScreen.OnButtonClickListener() {
            @Override
            public void onNextLevel() {
                // Advance to next level
                levelManager.nextLevel();
                player.reset(levelManager.getCurrentLevel().getSpawnX(),
                             levelManager.getCurrentLevel().getSpawnY());
                hideVictoryScreen();
                requestFocusInWindow();
            }

            @Override
            public void onRepeat() {
                levelManager.resetCurrentLevel();
                player.reset(levelManager.getCurrentLevel().getSpawnX(),
                             levelManager.getCurrentLevel().getSpawnY());
                hideVictoryScreen();
                requestFocusInWindow();
            }

            @Override
            public void onQuit() {
                stop();
                if (onReturnToMenu != null) {
                    onReturnToMenu.run();
                } else {
                    System.exit(0);
                }
            }

            @Override
            public void onHome() {
                stop();
                if (onReturnToMenu != null) {
                    onReturnToMenu.run();
                } else {
                    System.exit(0);
                }
            }
        });
    }

    // Start the game loop on a dedicated thread
    public void start() {
        System.out.println("GameLoop.start() called");

        // Ensure UI screens are hidden
        SwingUtilities.invokeLater(this::hideVictoryScreen);
        SwingUtilities.invokeLater(this::hideGameUI);

        if (running) return;

        running = true;
        gameThread = new Thread(this::runLoop, "GameLoop-Thread");
        gameThread.start();

        // Request focus for keyboard input on EDT
        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
        });
    }

    // Stop the game loop
    public void stop() {
        System.out.println("GameLoop.stop() called");

        running = false;
        if (gameThread != null) {
            try {
                gameThread.join(500);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            gameThread = null;
        }

        // Hide UI screens on EDT
        SwingUtilities.invokeLater(() -> {
            hideVictoryScreen();
            hideGameUI();
        });
    }

    private void runLoop() {
        final double nsPerUpdate = 1_000_000_000.0 / FPS;
        long lastTime = System.nanoTime();
        double delta = 0.0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerUpdate;
            lastTime = now;

            while (delta >= 1.0) {
                updateGameLogic();
                delta -= 1.0;
            }

            // Request repaint (safe from background thread)
            repaint();

            // Sleep a little to avoid busy loop
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Game logic update kept off the EDT. UI changes are scheduled onto the EDT.
    private void updateGameLogic() {
        if (!player.isDead()) {
            // Apply input state each frame
            inputHandler.updateMovement();

            player.update();
            levelManager.update();

            Level currentLevel = levelManager.getCurrentLevel();
            if (currentLevel != null && currentLevel.isCompleted() && !victoryScreenShown) {
                // Call the callback to update unlocked levels on EDT
                SwingUtilities.invokeLater(() -> {
                    if (onLevelComplete != null) {
                        onLevelComplete.run();
                    }
                    // Use a brief delay before showing the victory screen on the EDT
                    Timer delayTimer = new Timer(500, e -> {
                        showVictoryScreen();
                        ((Timer)e.getSource()).stop();
                    });
                    delayTimer.setRepeats(false);
                    delayTimer.start();
                });

                // Set flag immediately to avoid multiple triggers
                victoryScreenShown = true;
            }
        } else {
            // Player died -> show game UI on EDT
            SwingUtilities.invokeLater(this::showGameUI);
        }
    }

    private void showVictoryScreen() {
        if (!victoryScreenShown) {
            victoryScreenShown = true;
            add(victoryScreen, BorderLayout.CENTER);
            victoryScreen.show();
            revalidate();
            repaint();
        }
    }

    private void hideVictoryScreen() {
        if (victoryScreenShown) {
            victoryScreen.hide();
            remove(victoryScreen);
            victoryScreenShown = false;
            revalidate();
            repaint();
        }
    }

    private void showGameUI() {
        if (!gameUIShown) {
            gameUIShown = true;
            gameUI.show();
        }
    }

    private void hideGameUI() {
        if (gameUIShown) {
            gameUI.hide();
            gameUIShown = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Disable anti-aliasing for pixel-perfect retro look
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // Draw background image
        if (backgroundImg != null) {
            g2d.drawImage(backgroundImg, 0, 0, BASE_WIDTH, BASE_HEIGHT, null);
        }

        Level currentLevel = levelManager.getCurrentLevel();
        if (currentLevel != null) {
            currentLevel.render(g2d);
        }

        player.render(g2d);

        // Draw UI with retro font
        g2d.setFont(retroFont);

        // Level indicator with shadow
        g2d.setColor(Color.BLACK);
        g2d.drawString("LEVEL: " + levelManager.getCurrentLevelNumber(), 12, 32);
        g2d.setColor(new Color(255, 200, 0));
        g2d.drawString("LEVEL: " + levelManager.getCurrentLevelNumber(), 10, 30);

        // Deaths counter with shadow
        g2d.setColor(Color.BLACK);
        g2d.drawString("DEATHS: " + player.getDeathCount(), 12, 62);
        g2d.setColor(new Color(255, 100, 100));
        g2d.drawString("DEATHS: " + player.getDeathCount(), 10, 60);

        // Stars counter with shadow
        g2d.setColor(Color.BLACK);
        g2d.drawString("STARS: " + levelManager.getCurrentLevel().getCollectedStars() + " / " + levelManager.getCurrentLevel().getTotalStars(), 12, 92);
        g2d.setColor(Color.YELLOW);
        g2d.drawString("STARS: " + levelManager.getCurrentLevel().getCollectedStars() + " / " + levelManager.getCurrentLevel().getTotalStars(), 10, 90);

        // Stuck timer with shadow (only show if stuck)
        int stuckTimer = levelManager.getCurrentLevel().getStuckTimer();
        if (stuckTimer > 0) {
            int remainingSeconds = (300 - stuckTimer) / 60 + 1; // Approximate remaining seconds
            g2d.setColor(Color.BLACK);
            g2d.drawString("STUCK: " + remainingSeconds + "s", 12, 122);
            g2d.setColor(Color.RED);
            g2d.drawString("STUCK: " + remainingSeconds + "s", 10, 120);
        }

        if (player.isDead()) {
            // Draw "YOU DIED!" with retro style
            Font bigFont = retroFont.deriveFont(32f);
            g2d.setFont(bigFont);
            String msg = "YOU DIED!";
            FontMetrics fm = g2d.getFontMetrics();
            int msgWidth = fm.stringWidth(msg);
            int x = BASE_WIDTH / 2 - msgWidth / 2;
            int y = BASE_HEIGHT / 2 - 20;

            // Shadow
            g2d.setColor(Color.BLACK);
            g2d.drawString(msg, x + 3, y + 3);

            // Main text with gradient effect
            GradientPaint gp = new GradientPaint(
                x, y - 20, new Color(255, 50, 0),
                x, y + 10, new Color(200, 0, 0)
            );
            g2d.setPaint(gp);
            g2d.drawString(msg, x, y);
        }

        // Debug overlay: draw player bounds and level collision visuals
        if (debugOverlay && currentLevel != null) {
            // Player bounds (magenta)
            Rectangle pb = player.getBounds();
            g2d.setColor(Color.MAGENTA);
            g2d.drawRect(pb.x, pb.y, pb.width, pb.height);

            // Ask level to draw its debug shapes
            currentLevel.debugRender(g2d);

            // small legend
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g2d.setColor(Color.WHITE);
            g2d.drawString("DEBUG: F3 toggles overlay", 12, BASE_HEIGHT - 10);
        }

        g2d.dispose();
    }

    // Public method to reset player position
    public void resetPlayer() {
        if (levelManager != null && levelManager.getCurrentLevel() != null) {
            player.reset(levelManager.getCurrentLevel().getSpawnX(),
                        levelManager.getCurrentLevel().getSpawnY());
        }
    }

    // Public method to get player
    public Player getPlayer() {
        return player;
    }

    // Public method to get level manager
    public LevelManager getLevelManager() {
        return levelManager;
    }

    // Restored accessor expected by Main.java
    public int getCurrentLevel() {
        if (levelManager != null) {
            return levelManager.getCurrentLevelNumber();
        }
        return 0;
    }
}