package game;

import animation.AnimationRunner;
import input.GameKeyboard;
import input.GameMouse;
import input.PlayerInput;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import level.LevelTest;
import data.HighScoreTable;
import level.LevelTest3;
import menu.MainMenuScene;

import java.util.Arrays;

/**
 * Main class để khởi động game Arkanoid.
 */
public class MainGame extends Application {

    public static final int SCREEN_WIDTH = 980;
    public static final int SCREEN_HEIGHT = 600;
    public static final double FPS = 60.0;

    @Override
    public void start(Stage primaryStage) {
        GameKeyboard keyboard = new GameKeyboard();
        GameMouse mouse = new GameMouse();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        PlayerInput input = new PlayerInput(keyboard, mouse);
        HighScoreTable highScoreTable = new HighScoreTable();

        Runnable startGame = () -> {
            Counter score = new Counter();
            AnimationRunner runner = new AnimationRunner(canvas.getGraphicsContext2D(), FPS);
            GameFlow gameFlow = new GameFlow(runner, input, score, highScoreTable, primaryStage, canvas);
            gameFlow.runLevels(Arrays.asList(new LevelTest(), new LevelTest3()));
        };
        MainMenuScene mainMenu = new MainMenuScene(input, primaryStage, startGame, highScoreTable);
        mainMenu.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}