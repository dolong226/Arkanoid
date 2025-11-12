package animation;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class AnimationRunner {
    private final double fps;
    private final GraphicsContext gc;

    public AnimationRunner(GraphicsContext gc, double fps) {
        this.gc = gc;
        this.fps = fps;
    }

    public void run(Animation animation) {
        if ( gc == null ) {
            while (!animation.isFinished()) {
                animation.update(1.0/fps);
                try {
                    Thread.sleep((long) (1000 / fps));
                } catch (InterruptedException e ) {
                    e.printStackTrace();
                    break;
                }
            }
            return;
        }

        new AnimationTimer() {
            private long lastTime = -1;
            private double accumulator = 0;
            private final double timePerFrame = 1.0 / fps;

            public void handle (long now) {
                if (lastTime < 0 ) {
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
                double canvasWidth = gc.getCanvas().getWidth();
                double canvasHeight = gc.getCanvas().getHeight();
                animation.render(gc);

                if (animation.isFinished()) {
                    stop();
                }
            }
        }.start();
    }

    public static void makeCanvasResizable(Canvas canvas, Pane parent) {
        canvas.widthProperty().bind(parent.widthProperty());
        canvas.heightProperty().bind(parent.heightProperty());
    }
}