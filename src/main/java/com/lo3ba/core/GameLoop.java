package com.lo3ba.core;

import com.lo3ba.levels.Level;
import com.lo3ba.ui.GameUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException; // Added for ImageIO error handling
import java.io.InputStream;
import javax.imageio.ImageIO; // Added for direct image loading

public class GameLoop extends JPanel implements Runnable {
    // Base game resolution (internal rendering resolution)
    public static final int BASE_WIDTH = 800;
    public static final int BASE_HEIGHT = 600;
    private static final int FPS = 60;

    private Thread gameThread;
    private boolean running = false;

    private Player player;
    private LevelManager levelManager;
    private InputHandler inputHandler;
    private GameUI gameUI;

    private BufferedImage backgroundImg;
    private Font retroFont;

    // Scaling factors (for rendering only)
    private double scaleX = 1.0;
    private double scaleY = 1.0;
    
    // Callbacks
    private Runnable onLevelComplete;
    private Runnable onReturnToMenu;

    // For backwards compatibility
    public GameLoop() {
        this(1, null, null);
    }

    public GameLoop(int startLevel, Runnable onLevelComplete, Runnable onReturnToMenu) {
        this.onLevelComplete = onLevelComplete;
        this.onReturnToMenu = onReturnToMenu;
        
        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null);

        loadFont();
        init(startLevel);
        setupUI();
        setupResizeListener();
    }

    private void setupResizeListener() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateScale();
                updateButtonPositions();
            }
        });
    }

    private void updateScale() {
        int width = getWidth();
        int height = getHeight();
        if (width > 0 && height > 0) {
            scaleX = (double) width / BASE_WIDTH;
            scaleY = (double) height / BASE_HEIGHT;
        }
    }

    private void updateButtonPositions() {
        if (gameUI != null) {
            gameUI.updatePositions(getWidth(), getHeight());
        }
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

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                inputHandler.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                inputHandler.keyReleased(e);
            }
        });

        // FIX: Directly load background image using the full resource path
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
            public void onReplay() {
                levelManager.resetCurrentLevel();
                player.reset(levelManager.getCurrentLevel().getSpawnX(),
                             levelManager.getCurrentLevel().getSpawnY());
                gameUI.hide();
                requestFocusInWindow();
            }

            @Override
            public void onQuit() {
                if (onReturnToMenu != null) {
                    onReturnToMenu.run();
                } else {
                    System.exit(0);
                }
            }
        });
    }

    public void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stop() {
        running = false;
        try {
            if (gameThread != null) {
                gameThread.join(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / FPS;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                update();
                delta--;
            }

            repaint();

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        if (!player.isDead()) {
            player.update();
            levelManager.update();
            
            // Check if level completed
            Level currentLevel = levelManager.getCurrentLevel();
            if (currentLevel != null && currentLevel.isCompleted()) {
                if (onLevelComplete != null) {
                    onLevelComplete.run();
                }
                // Small delay before next level
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            gameUI.show();
        }
    }

    public int getCurrentLevel() {
        return levelManager.getCurrentLevelNumber();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Update scale based on current size
        updateScale();

        // Apply scaling transformation (ONLY for rendering)
        g2d.scale(scaleX, scaleY);

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

        // Draw UI with retro font (This is the official UI drawing location)
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

        g2d.dispose();
    }

    public double getScaleX() { return scaleX; }
    public double getScaleY() { return scaleY; }
}