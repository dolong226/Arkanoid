package menu;

import animation.AnimationRunner;
import data.HighScoreTable;
import game.Counter;
import game.GameFlow;
import input.GameKeyboard;
import input.GameMouse;
import input.PlayerInput;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.canvas.Canvas;
import level.LevelInformation;
import level.LevelTest;

import java.util.ArrayList;
import java.util.List;

public class MainMenuScene {
    private final Stage stage;
    private final PlayerInput input;
    private Runnable onStartGame;
    private HighScoreTable highScoreTable;

    public MainMenuScene(PlayerInput input, Stage stage,  Runnable onStartGame, HighScoreTable highScoreTable) {
        this.input = input;
        this.stage = stage;
        this.onStartGame = onStartGame;
        this.highScoreTable = highScoreTable;
    }

    public void show() {
        Canvas canvas = new Canvas(800, 600);
        StackPane root = new StackPane(canvas);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 800, 600);


        // Gắn input vào Scene
        GameMouse mouse = new GameMouse(canvas);
        GameKeyboard keyboard = (GameKeyboard) input.getKeyboard();
        PlayerInput input = new PlayerInput(keyboard, mouse);

        scene.setOnKeyPressed(keyboard::onKeyPressed);
        scene.setOnKeyReleased(keyboard::onKeyReleased);
        canvas.setOnMousePressed(mouse::onMousePressed);
        canvas.setOnMouseReleased(mouse::onMouseReleased);
        canvas.setOnMouseMoved(mouse::onMouseMoved);

        stage.setScene(scene);
        stage.setTitle("MENU");
        stage.show();

        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        // Tạo GraphicsContext + AnimationRunner
        GraphicsContext gc = canvas.getGraphicsContext2D();
        AnimationRunner runner = new AnimationRunner(gc, 60.0);


        Counter globalScore = new Counter(0);

        GameFlow gameFlow = new GameFlow(runner, input, globalScore, highScoreTable, stage, canvas);

        List<LevelInformation> levels = new ArrayList<>();
        levels.add(new LevelTest());

        // Callback khi bấm START
        Runnable startGameAction = () -> {
            System.out.println("Starting game...");
            gameFlow.runLevels(levels);
        };
        this.onStartGame = startGameAction;

        // Tạo và chạy menu
        MainMenuAnimation menuAnim = new MainMenuAnimation(canvas, input, startGameAction, highScoreTable);
        runner.run(menuAnim);
    }

}
