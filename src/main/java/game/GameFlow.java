package game;

import animation.AnimationRunner;
import animation.PauseAnimation;
import input.GameKeyboard;
import input.Keyboard;
import input.PlayerInput;
import level.LevelInformation;

import java.util.List;

public class GameFlow {
    private final AnimationRunner runner;
    private final Keyboard keyboard;
    private final Counter globalScore;
    private final PlayerInput input;

    public GameFlow(AnimationRunner runner, PlayerInput input, Counter globalScore) {
        this.runner = runner;
        this.input = input;
        this.keyboard = input.getKeyboard();
        this.globalScore = globalScore;
    }


    // chạy level
    public void runLevels(List<LevelInformation> levels) {
        if (levels.isEmpty()) {
            System.out.println("Không có level nào!");
            return;
        }

        for (LevelInformation lv: levels) {
            GameLevel level = new GameLevel(lv, input,runner);
            int i = 1;
            System.out.println("Bắt đầu level " + i);
            i++;
            level.run();
        }
    }
}