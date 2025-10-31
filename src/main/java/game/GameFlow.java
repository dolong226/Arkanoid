package game;

import animation.AnimationRunner;
import animation.PauseAnimation;
import input.GameKeyboard;
import input.Keyboard;
import level.LevelInformation;

import java.util.List;

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

    // chạy level
    public void runLevels(List<LevelInformation> levels) {
        if (levels.isEmpty()) {
            System.out.println("Không có level nào!");
            return;
        }

        for (LevelInformation lv: levels) {
            GameLevel level = new GameLevel(lv, keyboard,runner);
            int i = 1;
            System.out.println("Bắt đầu level " + i);
            i++;
            level.run();
        }
        // AnimationTimer sẽ tự động dừng khi isFinished() = true
    }
}