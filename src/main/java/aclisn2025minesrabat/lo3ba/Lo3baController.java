package aclisn2025minesrabat.lo3ba;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Lo3baController {

    @FXML
    private Canvas gameCanvas;

    @FXML
    public void initialize() {
        drawHelloWorld();
    }

    private void drawHelloWorld() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        // Fond blanc
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // Texte noir en grand
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 40)); // taille 40px
        gc.fillText("HELLO WORLD", 80, 200); // position X=80, Y=200
    }
}
