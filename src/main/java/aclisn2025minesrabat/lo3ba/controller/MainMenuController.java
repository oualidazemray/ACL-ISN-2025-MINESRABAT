package aclisn2025minesrabat.lo3ba.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {
    
    @FXML
    private Button startButton;
    @FXML
    private void handleExit() {
    javafx.application.Platform.exit();
    }

    
    @FXML
    private void handleStartGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aclisn2025minesrabat/lo3ba/game-screen.fxml"));
            Parent gameRoot = loader.load();
            
            GameController gameController = loader.getController();
            
            Stage stage = (Stage) startButton.getScene().getWindow();
            Scene scene = new Scene(gameRoot, 1200, 700);
            stage.setScene(scene);
            
            gameController.requestFocus();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}