package animation;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class AnimationRunner {
    private final double fps;
    private final GraphicsContext gc;
    private AnimationTimer currentTimer;

    public AnimationRunner(GraphicsContext gc, double fps) {
        this.gc = gc;
        this.fps = fps;
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public void run(Animation animation) {
        if (currentTimer != null) {
            currentTimer.stop();
        }

        currentTimer = new AnimationTimer() {
            private long lastTime = -1;
            private double accumulator = 0;
            private final double timePerFrame = 1.0 / fps;

            @Override
            public void handle(long now) {
                if (lastTime < 0) {
                    lastTime = now;
                    return;
                }
                double deltaTime = (now - lastTime) / 1e9;
                lastTime = now;
                accumulator += deltaTime;

                while (accumulator >= timePerFrame) {
                    animation.update(timePerFrame);
                    accumulator -= timePerFrame;
                }

                animation.render(gc);

                if (animation.isFinished()) {
                    stop();
                    currentTimer = null; // xóa tham chiếu
                }
            }
        };
        currentTimer.start();
    }

    public static void makeCanvasResizable(Canvas canvas, Pane parent) {
        canvas.widthProperty().bind(parent.widthProperty());
        canvas.heightProperty().bind(parent.heightProperty());
    }

}