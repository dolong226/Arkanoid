package game;

import animation.AnimationRunner;
import game.Counter;
import game.GameFlow;
import input.GameKeyboard;
import input.GameMouse;
import input.PlayerInput;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import level.LevelInformation;
import level.LevelTest;
import menu.MainMenuScene;

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
        GameKeyboard keyboard = new GameKeyboard();
        GameMouse mouse = new GameMouse();
        PlayerInput input = new PlayerInput(keyboard, mouse);

        MainMenuScene mainMenu = new MainMenuScene(input, primaryStage, () -> {}); // Runnable rỗng
        mainMenu.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}