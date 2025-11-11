package ui;

import data.HighScoreTable;
import game.Counter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class GameInfoPanel {
    private  final double x;
    private final double y;
    private final double width;
    private final double height;

    private String levelName;
    private Counter currentScore;
    private HighScoreTable highScoreTable;
    private Counter remainingBalls;
    private Counter remainingBlocks;

    private Image backgroundImage;
    private boolean imageLoaded = false;

    private static final Color PANEL_BG = Color.rgb(50, 50, 80, 0.85);
    private static final Color BORDER_COLOR = Color.rgb(150, 150, 200);
    private static final Color TITLE_COLOR = Color.rgb(255, 220, 100);
    private static final Color TEXT_COLOR = Color.rgb(255, 255, 255);
    private static final Color VALUE_COLOR = Color.rgb(100, 200, 255);

    public GameInfoPanel(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        loadBackground();
    }

    private void loadBackground() {
        try {
            backgroundImage = new Image("/Default/panel_bg.png");
            imageLoaded = true;
        } catch (Exception e) {
            System.out.println("Khong thay anh");
            imageLoaded = false;
        }
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public void setRemainingBalls(Counter remainingBalls) {
        this.remainingBalls = remainingBalls;
    }

    public void setRemainingBlocks(Counter remainingBlocks) {
        this.remainingBlocks = remainingBlocks;
    }

    public void setCurrentScore(Counter currentScore) {
        this.currentScore = currentScore;
    }

    public void setHighScoreTable(HighScoreTable highScoreTable) {
        this.highScoreTable = highScoreTable;
    }

    public void render(GraphicsContext gc) {
        if (imageLoaded && backgroundImage != null) {
            gc.drawImage(backgroundImage, x,y, width, height);
        } else {
            gc.setFill(PANEL_BG);
            gc.fillRoundRect(x, y, width, height, 20, 20);

            gc.setStroke(BORDER_COLOR);
            gc.setLineWidth(3);
            gc.strokeRoundRect(x, y, width, height, 20, 20);
        }

        // Vẽ nội dung
        double contentX = x + 10;
        double startY = 100;
        double lineHeight = 60;

        gc.setFill(TITLE_COLOR);
        gc.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("GAME INFO", contentX, startY);

        // Level Name
        gc.setFill(TEXT_COLOR);
        gc.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        gc.fillText("Level:", contentX, startY + lineHeight);

        gc.setFill(VALUE_COLOR);
        gc.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 20));
        gc.fillText(levelName != null ? levelName : "???", contentX + 10, startY + lineHeight + 25);

        // Current Score
        gc.setFill(TEXT_COLOR);
        gc.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        gc.fillText("Score:", contentX, startY + lineHeight * 2);

        gc.setFill(VALUE_COLOR);
        gc.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
        String scoreText = currentScore != null ? String.valueOf(currentScore.getValue()) : "0";
        gc.fillText(scoreText, contentX + 10, startY + lineHeight * 2 + 25);

        // High Score
        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        gc.fillText("★ High Score:", contentX, startY + lineHeight * 3);

        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 22));
        int highScore = getHighScore();
        gc.fillText(String.valueOf(highScore), contentX + 10, startY + lineHeight * 3 + 25);

        // Căn trái
        gc.setTextAlign(TextAlignment.LEFT);
    }

    private int getHighScore() {
        if (highScoreTable == null) return 0;

        List<HighScoreTable.ScoreEntry> topScores = highScoreTable.getTopScores();
        if (topScores.isEmpty()) {
            return 0;
        }
        return topScores.get(0).score;
    }
}