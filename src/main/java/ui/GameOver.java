
package ui;

import data.HighScoreTable;
import game.MainGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameOver {
    private final Stage stage;
    private final int finalScore;
    private final HighScoreTable highScoreTable;
    private final Runnable onBackToMenu;

    public GameOver(Stage stage, int finalScore, HighScoreTable highScoreTable, Runnable onBackToMenu) {
        this.stage = stage;
        this.finalScore = finalScore;
        this.highScoreTable = highScoreTable;
        this.onBackToMenu = onBackToMenu;
    }

    public void show() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        root.setStyle("-fx-background-color: #0f0f0f;");

        // GAME OVER
        Label title = new Label("GAME OVER");
        title.setFont(Font.font("Arial", 64));
        title.setTextFill(Color.CRIMSON);
        title.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, #ff0000, 15, 0.8, 0, 0);");

        // SCORE
        Label scoreLabel = new Label("Score: " + finalScore);
        scoreLabel.setFont(Font.font("Arial", 40));
        scoreLabel.setTextFill(Color.LIME);
        scoreLabel.setStyle("-fx-font-weight: bold;");

        // HOME
        Button homeButton = createButton("HOME", "#1a5f1a", "#2d8f2d", () -> {
            // Lưu điểm cao trước khi về menu
            highScoreTable.addScore("Player", finalScore);
            onBackToMenu.run();
        });

        // EXIT
        Button exitButton = createButton("EXIT", "#5f1a1a", "#8f2d2d", () -> System.exit(0));

        VBox buttons = new VBox(20, homeButton, exitButton);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, scoreLabel, buttons);

        Scene scene = new Scene(root, MainGame.SCREEN_WIDTH, MainGame.SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Arkanoid - Game Over");
        stage.show();
    }

    private Button createButton(String text, String baseColor, String hoverColor, Runnable action) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", 28));
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(200);
        btn.setStyle(
                "-fx-background-color: " + baseColor + "; " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 15 40; " +
                        "-fx-font-weight: bold; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 3);"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + hoverColor + "; " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 15 40; " +
                        "-fx-font-weight: bold;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + baseColor + "; " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 15 40; " +
                        "-fx-font-weight: bold;"
        ));

        btn.setOnAction(e -> action.run());
        return btn;
    }
}
