package game;

import animation.AnimationRunner;
import animation.GameOverAnimation;
import input.Keyboard;
import input.PlayerInput;
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

    public GameFlow(AnimationRunner runner, PlayerInput input, Counter globalScore,
                    HighScoreTable highScoreTable, Stage stage, Canvas canvas) {
        this.runner = runner;
        this.input = input;
        this.keyboard = input.getKeyboard();
        this.globalScore = globalScore;
        this.highScoreTable = highScoreTable;
        this.stage = stage;
        this.canvas = canvas;
    }

    public void runLevels(List<LevelInformation> levels) {

        if (levels.isEmpty()) {
            System.out.println("Không có level nào!");
            return;
        }

        // Chạy level đầu tiên
        runLevel(0, levels);
    }

    //  đệ quy để chạy từng level
    private void runLevel(int levelIndex, List<LevelInformation> levels) {
        if (levelIndex >= levels.size()) {
            // hoàn thành tất cả các level, hiện gameOver
            showGameOver();
            return;
        }

        LevelInformation lv = levels.get(levelIndex);

        GameLevel level = new GameLevel(lv, input, runner);

        // khi hoàn thành level, callback gọi level tiếp theo
        level.setOnLevelComplete(() -> {
            System.out.println("Level " + (levelIndex + 1) + " hoàn thành!");
            globalScore.increase(level.getScore().getValue());
            // Chạy level tiếp theo
//            runLevel(levelIndex + 1, levels);
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> runLevel(levelIndex + 1, levels));
                }
            }, 200);
        });

        // set call back khi kết thúc
        level.setOnGameOver(() -> {
            System.out.println("Game Over!");
            globalScore.increase(level.getScore().getValue());
            showGameOver();
        });

        level.run();
    }

    // hiển thị gameover
    private void showGameOver() {
        GameOverAnimation gameOver = new GameOverAnimation(
                canvas,
                input,
                globalScore.getValue(),
                highScoreTable,
                () -> {
                    // Lưu điểm
                    if (globalScore.getValue() > 0) {
                        highScoreTable.addScore("Player", globalScore.getValue());
                    }
                    // reset điểm và quay về menu
                    globalScore.reset();
                    new menu.MainMenuScene(input, stage, () -> {}, highScoreTable).show();
                }
        );
        runner.run(gameOver);
    }
}