
package animation;

import data.HighScoreTable;
import geometry.Point;
import input.PlayerInput;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import level.ImageBackground;

import java.util.List;

public class HighScoreAnimation implements Animation {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final PlayerInput input;
    private final HighScoreTable highScoreTable;
    private boolean shouldStop = false;

    public HighScoreAnimation(Canvas canvas, PlayerInput input, HighScoreTable highScoreTable) {
        this.canvas = canvas;
        this.input = input;
        this.gc = canvas.getGraphicsContext2D();
        this.highScoreTable = highScoreTable;
    }

    @Override
    public void update(double dt) {
        Point mouse = input.getMousePosition();

        // Thoát bằng ESC
        if (input.isExit()) {
            shouldStop = true;
        }

        input.getMouse().update();
    }

    @Override
    public void render(GraphicsContext gc) {
        // background
        ImageBackground bg = new ImageBackground("/Default/background_blue.png");
        bg.render(gc);

        // Tiêu đề
        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", 48));
        gc.fillText("HIGH SCORES", 350, 100);

        // Bảng điểm
        List<HighScoreTable.ScoreEntry> topScores = highScoreTable.getTopScores();
        gc.setFont(Font.font("Arial", 24));
        gc.setFill(Color.WHITE);

        int y = 180;
        for (int i = 0; i < 5; i++) {
            String rank = "#" + (i + 1);
            String name = i < topScores.size() ? topScores.get(i).name : "---";
            String score = i < topScores.size() ? String.valueOf(topScores.get(i).score) : "0";

            // Rank (màu vàng cho top 3)
            gc.setFill(i < 3 ? Color.GOLD : Color.WHITE);
            gc.fillText(rank, 200, y);

            // Tên
            gc.setFill(Color.CYAN);
            gc.fillText(name, 350, y);

            // Điểm
            gc.setFill(Color.GREEN);
            gc.fillText(score, 550, y);

            y += 50;
        }

        // Hướng dẫn thoát
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Nhấn ESC để quay lại", 300, 520);
    }

    @Override
    public boolean isFinished() {
        return shouldStop;
    }
}