// animation/GameOverAnimation.java
package animation;

import data.HighScoreTable;
import input.PlayerInput;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import geometry.Point;

public class GameOverAnimation implements Animation {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final PlayerInput input;
    private final int finalScore;
    private final HighScoreTable highScoreTable;
    private final Runnable onBackToMenu;

    // Nút HOME
    private double homeX = 300, homeY = 350, homeW = 200, homeH = 60;
    private boolean homeHovered = false;

    // Nút EXIT
    private double exitX = 300, exitY = 430, exitW = 200, exitH = 60;
    private boolean exitHovered = false;

    private boolean shouldStop = false;

    public GameOverAnimation(Canvas canvas, PlayerInput input, int finalScore,
                             HighScoreTable highScoreTable, Runnable onBackToMenu) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.input = input;
        this.finalScore = finalScore;
        this.highScoreTable = highScoreTable;
        this.onBackToMenu = onBackToMenu;
    }

    @Override
    public void update(double dt) {
        Point mouse = input.getMousePosition();

        // Cập nhật hover
        homeHovered = isInside(mouse, homeX, homeY, homeW, homeH);
        exitHovered = isInside(mouse, exitX, exitY, exitW, exitH);

        // Xử lý click
        if (input.isClickLeft()) {
            if (homeHovered) {
                if (finalScore > 0) {
                    highScoreTable.addScore("Player", finalScore);
                }
                shouldStop = true;
                onBackToMenu.run();
            } else if (exitHovered) {
                System.exit(0);
            }
        }

        input.getMouse().update();
    }

    @Override
    public void render(GraphicsContext gc) {
        // Nền
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 800, 600);

        // Tiêu đề
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", 64));
        gc.fillText("GAME OVER", 180, 180);

        // Điểm
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", 36));
        gc.fillText("Score: " + finalScore, 300, 250);

        // Vẽ nút HOME
        drawButton(gc, homeX, homeY, homeW, homeH, "HOME", Color.GREEN.darker(), Color.GREEN, homeHovered);

        // Vẽ nút EXIT
        drawButton(gc, exitX, exitY, exitW, exitH, "EXIT", Color.RED.darker(), Color.RED, exitHovered);
    }

    private void drawButton(GraphicsContext gc, double x, double y, double w, double h,
                            String text, Color base, Color hover, boolean isHovered) {
        gc.setFill(isHovered ? hover : base);
        gc.fillRoundRect(x, y, w, h, 20, 20);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 28));
        double textWidth = gc.getFont().getSize() * text.length() / 3.0;
        double textX = x + (w - textWidth) / 2;
        double textY = y + h / 2 + 10;
        gc.fillText(text, textX, textY);
    }

    private boolean isInside(Point p, double x, double y, double w, double h) {
        return p.getX() >= x && p.getX() <= x + w && p.getY() >= y && p.getY() <= y + h;
    }

    @Override
    public boolean isFinished() {
        return shouldStop;
    }
}