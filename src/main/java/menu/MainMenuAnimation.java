package menu;

import animation.Animation;

import geometry.Point;
import input.PlayerInput;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class MainMenuAnimation implements Animation {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private PlayerInput input;
    private final List<MenuButton> buttons;
    private boolean shouldStop = false;
    private Runnable onStartGame;

    public MainMenuAnimation(Canvas canvas, PlayerInput input, Runnable onStartGame) {
        this.canvas = canvas;
        this.input = input;
        this.gc = canvas.getGraphicsContext2D();
        this.buttons = new ArrayList<>();
        this.onStartGame = onStartGame;

        buttons.add(new MenuButton("START", 300, 200, 200, 60, Color.GREEN));
        buttons.add(new MenuButton("HIGH SCORE", 300, 280, 200, 60, Color.YELLOW));
        buttons.add(new MenuButton("SETTINGS", 300, 360, 200, 60, Color.CYAN));
        buttons.add(new MenuButton("QUIT", 300, 440, 200, 60, Color.RED));
    }

    @Override
    public void update(double dt) {
        Point mouse = input.getMousePosition();
        System.out.println("MOUSE: " + mouse.getX() + ", " + mouse.getY());
        //  Hover
        for (MenuButton btn : buttons) {
            btn.updateHover(mouse);
        }

        //  Click (trước khi reset)
        if (input.isClickLeft()) {
            for (MenuButton btn : buttons) {
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
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 800, 600);

        // Tiêu đề
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 48));
        gc.fillText("ARKANOID", 220, 120);

        // Vẽ các nút
        for (MenuButton btn : buttons) {
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
        }
    }

    @Override
    public boolean isFinished() {
        return shouldStop;
    }
}
