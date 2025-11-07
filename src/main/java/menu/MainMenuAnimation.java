package menu;

import animation.Animation;

import animation.AnimationRunner;
import animation.HighScoreAnimation;
import data.HighScoreTable;
import geometry.Point;
import input.PlayerInput;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class MainMenuAnimation implements Animation {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private PlayerInput input;
    private final List<MenuImageButton> buttons;
    private boolean shouldStop = false;
    private Runnable onStartGame;
    private final HighScoreTable highScoreTable;
    private final Runnable onSettings;
    private boolean isSettingsOpen = false;

    private static final String IMG_BUTTON_PATH = "/Default/";
    private static final String IMG_BACKGROUND_PATH = "/image/";

    private Image backgroundMenu;

    private Image startNormal, startHover;
    private Image highScoreNormal, highScoreHover;
    private Image settingsNormal, settingsHover;
    private Image quitNormal, quitHover;


    public MainMenuAnimation(Canvas canvas, PlayerInput input, Runnable onStartGame, HighScoreTable highScoreTable, Runnable onSettings) {
        this.canvas = canvas;
        this.input = input;
        this.gc = canvas.getGraphicsContext2D();
        this.buttons = new ArrayList<>();
        this.onStartGame = onStartGame;
        this.highScoreTable = highScoreTable;
        this.onSettings = onSettings;

        loadImages();
        createButtons();

    }

    private void loadImages() {
        Class<?> clazz = getClass();

        backgroundMenu = new Image(clazz.getResourceAsStream(IMG_BACKGROUND_PATH + "background2.jpg"));

        startNormal = new Image(clazz.getResourceAsStream(IMG_BUTTON_PATH + "button_blue.png"));
        startHover = new Image(clazz.getResourceAsStream(IMG_BUTTON_PATH + "button_grey.png"));
        highScoreNormal = new Image(clazz.getResourceAsStream(IMG_BUTTON_PATH + "button_blue.png"));
        highScoreHover = new Image(clazz.getResourceAsStream(IMG_BUTTON_PATH + "button_grey.png"));
        settingsNormal = new Image(clazz.getResourceAsStream(IMG_BUTTON_PATH + "button_grey.png"));
        settingsHover = new Image(clazz.getResourceAsStream(IMG_BUTTON_PATH + "button_blue.png"));
        quitNormal = new Image(clazz.getResourceAsStream(IMG_BUTTON_PATH + "button_grey.png"));
        quitHover = new Image(clazz.getResourceAsStream(IMG_BUTTON_PATH + "button_blue.png"));
    }

    private void createButtons() {
        buttons.add(new MenuImageButton("START", startNormal, startHover, 400, 200, 200, 60));
        buttons.add(new MenuImageButton("HIGH SCORE", highScoreNormal, highScoreHover, 400, 280, 200, 60));
        buttons.add(new MenuImageButton("SETTINGS", settingsNormal, settingsHover, 400, 360, 200, 60));
        buttons.add(new MenuImageButton("QUIT", quitNormal, quitHover, 400, 440, 200, 60));
    }


    @Override
    public void update(double dt) {
        Point mouse = input.getMousePosition();

        //  Hover
        for (MenuImageButton btn : buttons) {
            btn.updateHover(mouse);
        }

        //  Click (trước khi reset)
        if (input.isClickLeft()) {
            for (MenuImageButton btn : buttons) {
                if (btn.isClicked(mouse)) {
                    handleClick(btn.text);
                    return;
                }
            }
        }

        //  Reset justPressed
        input.getMouse().update();
    }

    @Override
    public void render(GraphicsContext gc) {
        // Nền
        if (backgroundMenu != null) {
            gc.drawImage(backgroundMenu,0,0, 800, 600);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0,0,800,600);
        }

        // Vẽ các nút
        for (MenuImageButton btn : buttons) {
            btn.render(gc);
        }
    }

    private void handleClick(String text) {
        System.out.println("Clicked: " + text);
        if("QUIT".equals(text)) {
            System.exit(0);
        } else if ("START".equals(text)) {
            shouldStop = true;
            if (onStartGame != null) {
                onStartGame.run();
            }
        } else if ("HIGH SCORE".equals(text)) {
            HighScoreAnimation highScoreAnim = new HighScoreAnimation(canvas, input, highScoreTable);
            AnimationRunner tempRunner = new AnimationRunner(gc, 60);
            tempRunner.run(highScoreAnim);
        } else if ("SETTINGS".equals(text)) {
            if (onSettings != null && !isSettingsOpen) {
                isSettingsOpen = true;
                onSettings.run();
                input.getMouse().reset();
                isSettingsOpen = false;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return shouldStop;
    }
}
