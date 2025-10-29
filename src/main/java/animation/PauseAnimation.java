package animation;

import javafx.scene.canvas.GraphicsContext;

public class PauseAnimation implements Animation {
    private long durationMs;
    private long startTime;

    public PauseAnimation(long durationMs) {
        this.durationMs = durationMs;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void render(GraphicsContext gc) {
        // todo
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() - startTime >= durationMs;
    }
}
