package listener;

import data.HighScoreTable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class HighScoreIndicator {
    private final HighScoreTable highScoreTable;

    public HighScoreIndicator(HighScoreTable highScoreTable) {
        this.highScoreTable = highScoreTable;
    }

    public int getHighScore() {
        List<HighScoreTable.ScoreEntry> topScores = highScoreTable.getTopScores();
        if (topScores.isEmpty()) {
            return 0;
        }
        return topScores.get(0).score;
    }

    public void draw (GraphicsContext gc, double x, double y) {
        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        gc.fillText("High: " + getHighScore(), x, y);
    }
}
