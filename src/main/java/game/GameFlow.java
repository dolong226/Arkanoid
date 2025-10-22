package game;

import animation.AnimationRunner;
import level.LevelInformation;

import java.util.List;

public class GameFlow {
    private Counter score; // đếm tổng điểm
    private AnimationRunner runner; // Điều khiển vòng lặp khung hình.
    private List<LevelInformation> levels; // Danh sách thông tin các level cần chạy.

    public GameFlow(AnimationRunner runner) {
        this.runner = runner;
        this.score = new Counter(0);

    }

    public void runLevels(List<LevelInformation> levels) {
        for (LevelInformation level: levels) {
            GameLevel _level = new GameLevel(level);
            _level.initialize();

        }
    }

}
