package aclisn2025minesrabat.lo3ba;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Lo3baMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/aclisn2025minesrabat/lo3ba/lo3ba.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setTitle("Lo3ba Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
