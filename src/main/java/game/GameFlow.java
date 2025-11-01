package game;

import animation.AnimationRunner;
import animation.GameOverAnimation;
import input.Keyboard;
import input.PlayerInput;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import level.LevelInformation;
import data.HighScoreTable;

import java.util.List;

public class GameFlow {
    private final AnimationRunner runner;
    private final Keyboard keyboard;
    private final Counter globalScore;
    private final PlayerInput input;
    private final HighScoreTable highScoreTable;
    private final Stage stage;
    private final Canvas canvas;

    public GameFlow(AnimationRunner runner, PlayerInput input, Counter globalScore, HighScoreTable highScoreTable, Stage stage, Canvas canvas) {
        this.runner = runner;
        this.input = input;
        this.keyboard = input.getKeyboard();
        this.globalScore = globalScore;
        this.highScoreTable = highScoreTable;
        this.stage = stage;
        this.canvas = canvas;
    }


    // chạy level
    public void runLevels(List<LevelInformation> levels) {
        if (levels.isEmpty()) {
            System.out.println("Không có level nào!");
            return;
        }

        for (LevelInformation lv : levels) {
            GameLevel level = new GameLevel(lv, input, runner);
            int i = 1;
            System.out.println("Bắt đầu level " + i);
            i++;
            level.run();

            if (level.getRemainingBalls() <= 0) {
                GameOverAnimation gameOver = new GameOverAnimation(
                        canvas, input, globalScore.getValue(), highScoreTable,
                        () -> {
                            if (globalScore.getValue() > 0) {
                                highScoreTable.addScore("Player", globalScore.getValue());
                            }
                            new menu.MainMenuScene(input, stage, () -> {}, highScoreTable).show();
                        }
                );
                runner.run(gameOver);
                return;
            }
        }

        if (globalScore.getValue() > 0) { // Có điểm
            highScoreTable.addScore("Player", globalScore.getValue()); // Tên tạm
        }
    }
}