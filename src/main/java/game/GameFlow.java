package game;

import animation.AnimationRunner;
import animation.GameOverAnimation;
import input.Keyboard;
import input.PlayerInput;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import level.LevelInformation;
import data.HighScoreTable;
import sound.AudioResource;
import sound.SoundManager;
import sun.security.provider.ConfigFile;

import java.util.List;

public class GameFlow {
    private final AnimationRunner runner;
    private final Keyboard keyboard;
    private final Counter globalScore;
    private final PlayerInput input;
    private final HighScoreTable highScoreTable;
    private final Stage stage;
    private final Canvas canvas;
    private final GameController gameController;

    public GameFlow(AnimationRunner runner, PlayerInput input, Counter globalScore,
                    HighScoreTable highScoreTable, Stage stage, Canvas canvas) {
        this.runner = runner;
        this.input = input;
        this.keyboard = input.getKeyboard();
        this.globalScore = globalScore;
        this.highScoreTable = highScoreTable;
        this.stage = stage;
        this.canvas = canvas;
        this.gameController = new GameController();
    }

    public void runLevels(List<LevelInformation> levels) {

        if (levels.isEmpty()) {
            System.out.println("Không có level nào!");
            return;
        }

        // Khởi tạo âm thanh khi bắt đầu game
        System.out.println("Đang load âm thanh");
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.preloadAll();
        soundManager.playMusic(AudioResource.BACKGROUND_MUSIC.name());
        System.out.println("Am thanh da duoc khoi tao");

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

        level.setHighScoreTable(highScoreTable);

        // sound
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.stopAllMusic();
        soundManager.preloadAll();
        soundManager.playMusic(lv.getBackgroundMusic());

        // khi hoàn thành level, callback gọi level tiếp theo
        level.setOnLevelComplete(() -> {
            System.out.println("Level " + (levelIndex + 1) + " hoàn thành!");

            gameController.onLevelComplete();

            globalScore.increase(level.getScore().getValue());

            // chay level tiep theo
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> runLevel(levelIndex + 1, levels));
                }
            }, 2000);
        });

        // set call back khi kết thúc
        level.setOnGameOver(() -> {
            System.out.println("Game Over!");

            gameController.onGameOver();

            globalScore.increase(level.getScore().getValue());
            // Delay trước khi show game over screen
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> showGameOver());
                }
            }, 2000);
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

                    SoundManager.getInstance().playMusic(AudioResource.BACKGROUND_MUSIC.name());

                    new menu.MainMenuScene(input, stage, () -> {}, highScoreTable).show();
                }
        );
        runner.run(gameOver);
    }

    public GameController getGameController() {
        return gameController;
    }
}