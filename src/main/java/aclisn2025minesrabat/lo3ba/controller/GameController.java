package aclisn2025minesrabat.lo3ba.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.util.HashSet;
import java.util.Set;

public class GameController {
    
    @FXML
    private Pane gamePane;
    
    private Rectangle player;
    private Circle playerEye1, playerEye2;
    private Polygon playerTail;
    private Rectangle ground;
    private Rectangle door;
    private AnimationTimer gameLoop;
    private Set<KeyCode> pressedKeys;
    
    private double playerVelocityX = 0;
    private double playerVelocityY = 0;
    private double gravity = 0.5;
    private double jumpStrength = -12;
    private double moveSpeed = 3;
    private boolean isOnGround = false;
    
    @FXML
    public void initialize() {
        pressedKeys = new HashSet<>();
        
        gamePane.setFocusTraversable(true);
        
        // Key listeners
        gamePane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
                newScene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
            }
        });
        
        // Initialize game after pane is sized
        javafx.application.Platform.runLater(() -> {
            initializeGame();
            startGameLoop();
            requestFocus();
        });
    }
    
    private void initializeGame() {
        double width = gamePane.getWidth() > 0 ? gamePane.getWidth() : 1200;
        double height = gamePane.getHeight() > 0 ? gamePane.getHeight() : 700;
        
        // Create ground
        ground = new Rectangle(0, height - 100, width, 100);
        ground.setFill(Color.SADDLEBROWN);
        ground.setStroke(Color.BLACK);
        ground.setStrokeWidth(3);
        gamePane.getChildren().add(ground);
        
        // Create player (dinosaur)
        double playerX = 100;
        double playerY = height - 140; // 100 (ground) + 40 (player height)
        
        player = new Rectangle(playerX, playerY, 30, 40);
        player.setFill(Color.GREEN);
        player.setStroke(Color.DARKGREEN);
        player.setStrokeWidth(2);
        
        playerEye1 = new Circle(playerX + 10, playerY + 12, 4, Color.BLACK);
        playerEye2 = new Circle(playerX + 20, playerY + 12, 4, Color.BLACK);
        
        playerTail = new Polygon();
        playerTail.getPoints().addAll(
            playerX + 5, playerY + 40,
            playerX - 5, playerY + 50,
            playerX + 10, playerY + 35
        );
        playerTail.setFill(Color.DARKGREEN);
        
        gamePane.getChildren().addAll(player, playerEye1, playerEye2, playerTail);
        
        // Create door (exit)
        door = new Rectangle(width - 100, height - 160, 40, 60);
        door.setFill(Color.GOLD);
        door.setStroke(Color.ORANGE);
        door.setStrokeWidth(3);
        
        Rectangle doorKnob = new Rectangle(width - 70, height - 130, 5, 5);
        doorKnob.setFill(Color.BROWN);
        
        gamePane.getChildren().addAll(door, doorKnob);
    }
    
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
    }
    
    private void update() {
        handleInput();
        applyGravity();
        movePlayer();
        checkGroundCollision();
        checkDoorCollision();
        updatePlayerVisuals();
    }
    
    private void handleInput() {
        playerVelocityX = 0;
        
        if (pressedKeys.contains(KeyCode.LEFT)) {
            playerVelocityX = -moveSpeed;
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            playerVelocityX = moveSpeed;
        }
        if (pressedKeys.contains(KeyCode.SPACE) && isOnGround) {
            playerVelocityY = jumpStrength;
            isOnGround = false;
        }
    }
    
    private void applyGravity() {
        playerVelocityY += gravity;
        if (playerVelocityY > 20) {
            playerVelocityY = 20;
        }
    }
    
    private void movePlayer() {
        player.setX(player.getX() + playerVelocityX);
        player.setY(player.getY() + playerVelocityY);
        
        // Keep player on screen
        double width = gamePane.getWidth() > 0 ? gamePane.getWidth() : 1200;
        if (player.getX() < 0) {
            player.setX(0);
        }
        if (player.getX() + player.getWidth() > width) {
            player.setX(width - player.getWidth());
        }
    }
    
    private void checkGroundCollision() {
        isOnGround = false;
        
        if (player.getBoundsInParent().intersects(ground.getBoundsInParent())) {
            if (playerVelocityY > 0 && 
                player.getY() + player.getHeight() - playerVelocityY <= ground.getY()) {
                player.setY(ground.getY() - player.getHeight());
                playerVelocityY = 0;
                isOnGround = true;
            }
        }
    }
    
    private void checkDoorCollision() {
        if (player.getBoundsInParent().intersects(door.getBoundsInParent())) {
            showVictoryScreen();
        }
    }
    
    private void updatePlayerVisuals() {
        playerEye1.setCenterX(player.getX() + 10);
        playerEye1.setCenterY(player.getY() + 12);
        playerEye2.setCenterX(player.getX() + 20);
        playerEye2.setCenterY(player.getY() + 12);
        
        playerTail.getPoints().setAll(
            player.getX() + 5, player.getY() + 40,
            player.getX() - 5, player.getY() + 50,
            player.getX() + 10, player.getY() + 35
        );
    }
    
    private void showVictoryScreen() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        
        gamePane.getChildren().clear();
        
        VBox victoryBox = new VBox(20);
        victoryBox.setAlignment(Pos.CENTER);
        
        Label victoryLabel = new Label("🎉 BRAVO ! 🎉");
        victoryLabel.setStyle("-fx-font-size: 56px; -fx-text-fill: gold; -fx-font-weight: bold;");
        
        Label congratsLabel = new Label("Tu as atteint la porte !");
        congratsLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white;");
        
        javafx.scene.control.Button menuButton = new javafx.scene.control.Button("Retour au Menu");
        menuButton.setStyle("-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 30; -fx-cursor: hand;");
        menuButton.setOnAction(e -> returnToMenu());
        
        victoryBox.getChildren().addAll(victoryLabel, congratsLabel, menuButton);
        victoryBox.setLayoutX(gamePane.getWidth() / 2 - 150);
        victoryBox.setLayoutY(gamePane.getHeight() / 2 - 100);
        
        gamePane.getChildren().add(victoryBox);
    }
    
    @FXML
    private void returnToMenu() {
        try {
            if (gameLoop != null) {
                gameLoop.stop();
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aclisn2025minesrabat/lo3ba/main-menu.fxml"));
            Parent menuRoot = loader.load();
            
            Stage stage = (Stage) gamePane.getScene().getWindow();
            Scene scene = new Scene(menuRoot, 800, 600);
            stage.setScene(scene);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void requestFocus() {
        javafx.application.Platform.runLater(() -> {
            if (gamePane.getScene() != null) {
                gamePane.requestFocus();
            }
        });
    }
}