package game;

import animation.AnimationRunner;
import animation.PauseAnimation;
import geometry.Point;
import input.GameKeyboard;
import input.Keyboard;
import level.LevelInformation;

import java.sql.SQLOutput;
import java.util.List;

import static java.lang.Thread.sleep;

public class GameFlow {
    private final AnimationRunner runner;
    private final Keyboard keyboard;
    private final Counter globalScore;

    public GameFlow(Keyboard keyboard, AnimationRunner runner) {
        this.keyboard = keyboard;
        this.runner = runner;
        globalScore = new Counter(0);
    }

    public GameFlow(AnimationRunner runner, GameKeyboard keyboard, Counter score) {
        this.runner = runner;
        this.keyboard = keyboard;
        this.globalScore = score;
    }

    public void runLevels(List<LevelInformation> levels) {
        boolean hasWonAll = true;

        for (int i = 0; i < levels.size(); i++) {
            LevelInformation levelInfo = levels.get(i);
            GameLevel level = new GameLevel(levelInfo, keyboard, runner);

            System.out.println("Bắt đầu level " + (i + 1));
            level.initialize();

            while (level.getRemainingBlocks() > 0  && level.getRemainingBalls() > 0) {
                level.playOneTurn();
                level.run();
            }

            // Cộng điểm
            int levelScore = level.getScore().getValue();
            globalScore.increase(levelScore);
            System.out.println("Temp");

            // Kiểm tra thua
            if (level.getRemainingBalls() <= 0) {
                hasWonAll = false;
                System.out.println("Kiểm tra thua");
                break;
            } else {
                System.out.println("Win");
            }
        }

        // KQ cuối
        if (hasWonAll) {
            System.out.println("Thắng toàn bộ game");
        } else {
            System.out.println("Game over");
        }
        System.out.println("Tong diem");

        // đóng cửa số sau 30s
        runner.run(new PauseAnimation(30000));
    }
}
