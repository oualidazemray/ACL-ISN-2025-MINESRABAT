package aclisn2025minesrabat.lo3ba;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class Lo3baController {

    @FXML
    private Canvas gameCanvas;

    private int playerX = 1, playerY = 1;
    private final int cellSize = 40;
    private final int[][] maze = {
        {1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,1,1,1,1,0,1},
        {1,0,1,0,0,0,0,1,0,1},
        {1,0,1,0,1,1,0,1,0,1},
        {1,0,1,0,1,1,0,1,0,1},
        {1,0,1,0,0,0,0,1,0,1},
        {1,0,1,1,1,1,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1},
    };

    @FXML
    public void initialize() {
        drawMaze();
        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(this::handleKey);
    }

    private void handleKey(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> movePlayer(0, -1);
            case DOWN -> movePlayer(0, 1);
            case LEFT -> movePlayer(-1, 0);
            case RIGHT -> movePlayer(1, 0);
        }
    }

    private void movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;
        if (maze[newY][newX] == 0) { // 0 = free space
            playerX = newX;
            playerY = newY;
            drawMaze();
        }
    }

    private void drawMaze() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                if (maze[y][x] == 1) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }

        // Draw player
        gc.setFill(Color.RED);
        gc.fillOval(playerX * cellSize, playerY * cellSize, cellSize, cellSize);
    }
}
