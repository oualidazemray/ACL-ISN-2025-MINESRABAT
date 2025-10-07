package aclisn2025minesrabat.lo3ba;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Lo3baMain extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/aclisn2025minesrabat/lo3ba/main-menu.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setTitle("LO3BA - Sprint 1");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}