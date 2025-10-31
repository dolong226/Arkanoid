package game;

import animation.AnimationRunner;
import game.Counter;
import game.GameFlow;
import input.GameKeyboard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import level.LevelInformation;
import level.LevelTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class để khởi động game Arkanoid.
 */
public class MainGame extends Application {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final double FPS = 60.0;

    @Override
    public void start(Stage primaryStage) {
        // Tạo Canvas và Pane
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);

        Pane root = new Pane(canvas);
        root.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Arkanoid - Test Level");
        primaryStage.setResizable(true);
        primaryStage.show();

        // Tạo GraphicsContext
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Tạo AnimationRunner
        AnimationRunner runner = new AnimationRunner(gc, FPS);

        // Làm canvas tự động resize
        AnimationRunner.makeCanvasResizable(canvas, root);

        // Tạo input
        GameKeyboard keyboard = new GameKeyboard();
        scene.setOnKeyPressed(keyboard::onKeyPressed);
        scene.setOnKeyReleased(keyboard::onKeyReleased);

        // Tạo GameFlow
        Counter globalScore = new Counter(0);
        GameFlow gameFlow = new GameFlow(runner, keyboard, globalScore);

        // Tạo danh sách level
        List<LevelInformation> levels = new ArrayList<>();
        levels.add(new LevelTest());

        // Chạy game
        gameFlow.runLevels(levels);
    }

    public static void main(String[] args) {
        launch(args);
    }
}