package game;

import animation.Animation;
import animation.AnimationRunner;
import animation.CurtainTransition;
import animation.GameOverAnimation;
import input.Keyboard;
import input.PlayerInput;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
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
        soundManager.stopAllMusic();
        soundManager.preloadAll();
        soundManager.playMusicAsync(AudioResource.BACKGROUND_MUSIC.name());
        System.out.println("Am thanh da duoc khoi tao");

        // Chạy level đầu tiên
        runLevel(0, levels, null);
    }

    //  đệ quy để chạy từng level
    private void runLevel(int levelIndex, List<LevelInformation> levels, Animation previousAnimation) {
        if (levelIndex >= levels.size()) {
            // hoàn thành tất cả các level, hiện gameOver
            showVictory();
            return;
        }

        LevelInformation lv = levels.get(levelIndex);

        GameLevel level = new GameLevel(lv, input, runner, gameController);

        level.setHighScoreTable(highScoreTable);

        level.setOnExitToHome(() -> {
            Platform.runLater(() -> {
                try {
                    SoundManager.getInstance().stopAllMusic();
                } catch (Exception e) {

                }

                new menu.MainMenuScene(input, stage, () -> {}, highScoreTable).show();
            });
        });
        // sound
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.stopAllMusic();
        soundManager.preloadAll();
        soundManager.playMusicAsync(lv.getBackgroundMusic());

        gameController.setCurrentMusic(lv.getBackgroundMusic());


        // khi hoàn thành level, callback gọi level tiếp theo
        level.setOnLevelComplete(() -> {
            System.out.println("Level " + (levelIndex + 1) + " hoàn thành!");

            gameController.onLevelComplete();

            globalScore.increase(level.getScore().getValue());

            // chay level tiep theo
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> runLevel(levelIndex + 1, levels, level));
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
                    javafx.application.Platform.runLater(() -> showGameOver(level));
                }
            }, 2000);
        });

        if (previousAnimation != null) {
            CurtainTransition transition = new CurtainTransition(previousAnimation, level, 1.2);

            Animation transitionWrapper = new Animation() {
                @Override
                public void update(double dt) {
                    transition.update(dt);
                }

                @Override
                public void render(GraphicsContext gc) {
                    transition.render(gc);
                }

                @Override
                public boolean isFinished() {
                    if (transition.isFinished()) {
                        level.run();
                        return true;
                    }
                    return false;
                }
            };

            runner.run(transitionWrapper);
        } else {
            level.run();
        }
    }

    // hiển thị gameover
    private void showGameOver(Animation from) {
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
                    SoundManager.getInstance().stopAllMusic();

                    SoundManager.getInstance().playMusicAsync(AudioResource.BACKGROUND_MUSIC.name());

                    new menu.MainMenuScene(input, stage, () -> {}, highScoreTable).show();
                }
        );

        CurtainTransition transition = new CurtainTransition(from, gameOver, 1.0);
        runner.run(gameOver);
    }

    private void showVictory() {
        // to do
    }

    public GameController getGameController() {
        return gameController;
    }
}