
package animation;

import data.HighScoreTable;
import input.PlayerInput;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import geometry.Point;
import menu.MenuImageButton;

import java.util.ArrayList;
import java.util.List;

public class GameOverAnimation implements Animation {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final PlayerInput input;
    private final int finalScore;
    private final HighScoreTable highScoreTable;
    private final Runnable onBackToMenu;
    private final List<MenuImageButton> buttons;

    private Image gameOverBg;

    private Image quitNormal, quitHover;
    private Image homeNormal, homeHover;

    private boolean shouldStop = false;

    public GameOverAnimation(Canvas canvas, PlayerInput input, int finalScore,
                             HighScoreTable highScoreTable, Runnable onBackToMenu) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.input = input;
        this.buttons = new ArrayList<>();
        this.finalScore = finalScore;
        this.highScoreTable = highScoreTable;
        this.onBackToMenu = onBackToMenu;

        loadImage();
        createButtons();
    }

    public void loadImage() {
        Class<?> clazz = getClass();

        gameOverBg = new Image(clazz.getResourceAsStream("/Default/gameover_bg.png"));

        quitNormal = new Image(clazz.getResourceAsStream("/Default/quit1.png"));
        quitHover = new Image(clazz.getResourceAsStream("/Default/quit2.png"));
        homeNormal = new Image(clazz.getResourceAsStream("/Default/home1.png"));
        homeHover = new Image(clazz.getResourceAsStream("/Default/home2.png"));

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

    private void createButtons() {
        buttons.add(new MenuImageButton("QUIT", quitNormal, quitHover, 587, 430, 301, 70));
        buttons.add(new MenuImageButton("HOME", homeNormal, homeHover, 95, 430, 301, 70));
    }

    @Override
    public void render(GraphicsContext gc) {
        // Nền
        if (gameOverBg != null) {
            gc.drawImage(gameOverBg, 0, 0, 980, 600);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, 800, 600);
        }

        for (MenuImageButton btn : buttons) {
            btn.render(gc);
        };
    }



    private void handleClick(String text) {
        System.out.println("Clicked: " + text);
        if ("QUIT".equals(text)) {
            System.exit(0);
        } else if ("HOME".equals(text)) {
            shouldStop = true;
            onBackToMenu.run();
        }
    }

    @Override
    public boolean isFinished() {
        return shouldStop;
    }
}