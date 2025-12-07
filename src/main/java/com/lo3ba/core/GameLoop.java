package com.lo3ba.core;

import com.lo3ba.levels.Level;
import com.lo3ba.ui.GameUI;
import com.lo3ba.ui.PauseMenu;
import com.lo3ba.ui.LevelVictoryScreen;
import com.lo3ba.ui.FinalVictoryScreen;
import com.lo3ba.util.ResourceManager;
import com.lo3ba.util.ScaleManager;
import com.lo3ba.effects.ParticleSystem;
import com.lo3ba.effects.CameraShake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Main game loop class responsible for:
 * - Thread management (start/stop game loop)
 * - Game state coordination
 * - Delegating rendering to GameRenderer
 * - Delegating UI management to GameUIManager
 * - Updating game logic
 */
public class GameLoop extends JPanel implements UICallbacks {
    public static final int BASE_WIDTH = 1000;
    public static final int BASE_HEIGHT = 600;
    private static final int FPS = 60;

    private Thread gameThread;
    private volatile boolean running = false;

    private Player player;
    private LevelManager levelManager;
    private InputHandler inputHandler;

    // Enhancement systems
    private ParticleSystem particleSystem;
    private CameraShake cameraShake;
    
    // Rendering
    private GameRenderer gameRenderer;
    
    // UI Management
    private GameUIManager uiManager;

    private BufferedImage backgroundImg;
    private Font retroFont;

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
        setLayout(null); // Use absolute layout for overlays

        // Initialize ScaleManager with base dimensions
        ScaleManager.getInstance().updateDimensions(BASE_WIDTH, BASE_HEIGHT);

        // Add component listener to handle window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = getWidth();
                int newHeight = getHeight();
                if (newWidth > 0 && newHeight > 0) {
                    ScaleManager.getInstance().updateDimensions(newWidth, newHeight);
                    updateUIBounds();
                    repaint();
                }
            }
        });

        loadFont();
        init(startLevel);
        setupUI();
        System.out.println("GameLoop constructor finished");
    }

    private void loadFont() {
        retroFont = ResourceManager.loadFont("PressStart2P-Regular.ttf", 16f);
    }

    private void init(int startLevel) {
        // Initialize enhancement systems
        particleSystem = new ParticleSystem();
        cameraShake = new CameraShake();
        
        // Initialize at default position first
        player = new Player(100, 400, this); // Pass GameLoop reference
        levelManager = new LevelManager(player, startLevel);
        inputHandler = new InputHandler(player, levelManager);

        // Key listener: send movement keys to inputHandler; F3 toggles debug; F/ESC toggles pause
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F3) {
                    debugOverlay = !debugOverlay;
                    repaint();
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_F || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
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
        
        // Initialize renderer
        gameRenderer = new GameRenderer(retroFont, backgroundImg);
    }

    /**
     * Set up all UI screens using GameUIManager.
     */
    private void setupUI() {
        uiManager = new GameUIManager(this, this);
        uiManager.setupUI();
    }
    
    /**
     * Update UI bounds when window is resized.
     */
    private void updateUIBounds() {
        if (uiManager != null) {
            uiManager.updateBounds();
        }
    }
    
    /**
     * Toggle pause menu.
     */
    private void togglePause() {
        if (uiManager != null) {
            uiManager.togglePause();
        }
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
        // Skip update if game is paused
        if (uiManager != null && uiManager.isPaused()) return;
        
        if (!player.isDead()) {
            // Apply input state each frame
            inputHandler.updateMovement();

            player.update();
            levelManager.update();
            
            // ENHANCEMENT: Update particles and camera shake
            particleSystem.update();
            cameraShake.update();
            
            // ENHANCEMENT: Check for landing effects after collision detection
            player.checkLandingEffects();

            Level currentLevel = levelManager.getCurrentLevel();
            if (currentLevel != null && currentLevel.isCompleted() && !uiManager.isVictoryScreenShown()) {
                // Use a brief delay before showing the victory screen on the EDT
                SwingUtilities.invokeLater(() -> {
                    Timer delayTimer = new Timer(500, e -> {
                        showVictoryScreen();
                        ((Timer)e.getSource()).stop();
                    });
                    delayTimer.setRepeats(false);
                    delayTimer.start();
                });
            }
        } else {
            // Player died -> show game UI on EDT
            SwingUtilities.invokeLater(this::showGameUI);
        }
    }

    private void showVictoryScreen() {
        boolean isFinalLevel = levelManager.getCurrentLevelNumber() == 10;
        Level current = levelManager.getCurrentLevel();
        VictoryData data = new VictoryData(
            current.getCollectedStars(),
            current.getTotalStars(),
            player.getDeathCount(),
            null
        );
        uiManager.showVictoryScreen(isFinalLevel, data);
    }

    private void hideVictoryScreen() {
        if (uiManager != null) {
            uiManager.hideVictoryScreen();
        }
    }

    private void showGameUI() {
        if (uiManager != null) {
            uiManager.showGameUI();
        }
    }

    private void hideGameUI() {
        if (uiManager != null) {
            uiManager.hideGameUI();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Create rendering context
        GameRenderContext context = new GameRenderContext(
            player,
            levelManager.getCurrentLevel(),
            levelManager,
            particleSystem,
            cameraShake,
            debugOverlay,
            BASE_WIDTH,
            BASE_HEIGHT
        );
        
        // Delegate rendering to GameRenderer
        gameRenderer.render(g, context);
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
    
    // ENHANCEMENT: Getters for particle system and camera shake
    public ParticleSystem getParticleSystem() {
        return particleSystem;
    }
    
    public CameraShake getCameraShake() {
        return cameraShake;
    }
    
    // ========== UICallbacks Implementation ==========
    
    @Override
    public void onLevelComplete() {
        if (onLevelComplete != null) {
            onLevelComplete.run();
        }
    }

    @Override
    public void onReturnToMenu() {
        if (onReturnToMenu != null) {
            onReturnToMenu.run();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void onStopGame() {
        stop();
    }

    @Override
    public void onRepeatLevel() {
        levelManager.resetCurrentLevel();
        player.reset(levelManager.getCurrentLevel().getSpawnX(),
                     levelManager.getCurrentLevel().getSpawnY());
    }

    @Override
    public void onNextLevel() {
        levelManager.nextLevel();
    }

    @Override
    public void onTogglePause() {
        // Pause state is managed by uiManager
        // This is just a notification callback
    }

    @Override
    public void onExit() {
        System.exit(0);
    }
}