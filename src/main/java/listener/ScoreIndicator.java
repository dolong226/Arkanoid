package listener;

import game.Counter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Hiển thị điểm số của người chơi lên màn hình.
 */
public class ScoreIndicator {
    private final Counter score;

    public ScoreIndicator(Counter score) {
        this.score = score;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Score: " + score.getValue(), 20, 25);
    }
}
