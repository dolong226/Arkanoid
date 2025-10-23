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
            int frame = 0;
            while (!animation.shouldStop()) {
                ++frame;
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
                if (accumulator >= timePerFrame) {
                    double canvasWidth = gc.getCanvas().getWidth();
                    double canvasHeight = gc.getCanvas().getHeight();
                    animation.doOneFrame(gc, accumulator);
                    accumulator = 0;
                }
                if (animation.shouldStop()) {
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
