package game;

import animation.AnimationRunner;
import collidable.Paddle;
import input.GameKeyboard;
import input.GameMouse;
import input.PlayerInput;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import level.Level_1;
import data.HighScoreTable;
import level.Level_2;
import level.Level_3;
import menu.MainMenuScene;
import sound.SoundManager;
import thread.ResourceLoaderThread;
import ui.ImageLoad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main class để khởi động game Arkanoid.
 */
public class MainGame extends Application {

    public static final int SCREEN_WIDTH = 980;
    public static final int SCREEN_HEIGHT = 600;
    public static final double FPS = 60.0;

    @Override
    public void start(Stage primaryStage) {

        System.out.println(" Initializing threads");
        initializeThreads();

        System.out.println("Preloading resources");
        preloadResources();

        System.out.println("Setting up game components");
        GameKeyboard keyboard = new GameKeyboard();
        GameMouse mouse = new GameMouse();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        PlayerInput input = new PlayerInput(keyboard, mouse);
        HighScoreTable highScoreTable = new HighScoreTable();

        System.out.println("Setting up game flow");
        Runnable startGame = () -> {
            Counter score = new Counter();
            AnimationRunner runner = new AnimationRunner(canvas.getGraphicsContext2D(), FPS);
            GameFlow gameFlow = new GameFlow(runner, input, score, highScoreTable, primaryStage, canvas);
            gameFlow.runLevels(Arrays.asList(new Level_1(), new Level_2(), new Level_3()));
        };

        // show main menu
        MainMenuScene mainMenu = new MainMenuScene(input, primaryStage, startGame, highScoreTable);
        mainMenu.show();

        // Setup cleanup handler
        setupCleanupHandler(primaryStage);
        System.out.println("Game started");
    }

    /**
     * Khởi tạo tất cả các thread
     * SoundManager (bao gồm AudiioThread)
     * ResourceLoaderThread
     * ImageLoad (wrapper của RLD)
     */
    private void initializeThreads() {
        try {
            // SoundManager
            System.out.println("Initializing SoundManager");
            SoundManager soundManager = SoundManager.getInstance();
            soundManager.preloadAll();

            // ResourceLoaderThread
            System.out.println("Initializing ResourceLoaderThread");
            ResourceLoaderThread.getInstance();

            // ImageLoad
            System.out.println("Initializing ImageLoad");
            ImageLoad.initialize();

            System.out.println("All threads initialized");
        } catch (Exception e) {
            System.err.println("Error initializing thread:");
            e.printStackTrace();
        }
    }

    /**
     * Preload resources
     * Load tất cả resources cần thiết trước khi game bắt đầu
     */
    private void preloadResources() {
        try {
            // sound đã được load trong SoundManager.preloadAll()
            System.out.println("Preloading images");
            preloadImages();

            System.out.println("All resources preloaded");
        } catch (Exception e) {
            System.err.println("error preloading");
            e.printStackTrace();
        }
    }

    /**
     * Preload danh sách image quan trọng
     */
    private void preloadImages() {
        List<String> imagesToPreLoad = Arrays.asList(
                "/Default/gameover_bg.jpg",
                "/Default/highscore_Bg.png",
                "/Default/level1_bg.jpg",
                "/Default/level2_bg.jpg",
                "/Default/level3_bg.jpg",
                "/Default/menu_bg.jpg",
                "/Default/quit1.png",
                "/Default/quit2.png",
                "/Default/score1.png",
                "/Default/score2.png",
                "/Default/setting1.png",
                "/Default/setting2.png",
                "/Default/start1.png",
                "/Default/start2.png",
                "/Sprite/02-Breakout-Tiles.png",
                "/Sprite/04-Breakout-Tiles.png",
                "/Sprite/06-Breakout-Tiles.png",
                "/Sprite/08-Breakout-Tiles.png",
                "/Sprite/10-Breakout-Tiles.png",
                "/Sprite/12-Breakout-Tiles.png",
                "/Sprite/14-Breakout-Tiles.png",
                "/Sprite/16-Breakout-Tiles.png",
                "/Sprite/17-Breakout-Tiles.png",
                "/Sprite/18-Breakout-Tiles.png",
                "/Sprite/20-Breakout-Tiles.png",
                "/Sprite/54-Breakout-Tiles.png",
                "/Sprite/58-Breakout-Tiles.png"

        );

        try {
            ImageLoad.preload(imagesToPreLoad);
        } catch (Exception e) {
            System.err.println("Warning: Preload image");
        }
    }
    /**
     * setup cleanup handler
     * đảm bảo tất cả các threads được shut down khi đóng game
     */
    private void setupCleanupHandler(Stage primaryStage) {
        // cleanup khi đóng window
        primaryStage.setOnCloseRequest(event -> {
            cleanupThreads();
        });
    }

    @Override
    public void stop() {
        cleanupThreads();
    }

    /**
     * Clean up ttất cả thread theo thứ tự
     * game loop
     * audio therad
     * resource loader thread
     * image load
     * sound manager
     */
    private void cleanupThreads() {
        try {
            ImageLoad.shutdown();

            SoundManager soundManager = SoundManager.getInstance();
            if (soundManager != null) {
                soundManager.dispose(); // Tự động shutdown
            }
        } catch (Exception e) {
            System.err.println("Error during clean up");
            e.printStackTrace();
        }

        System.gc();
    }
    public static void main(String[] args) {
        launch(args);
    }
}