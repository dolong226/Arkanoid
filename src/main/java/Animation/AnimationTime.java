package animation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.animation.AnimationTimer;

public class AnimationTime {
    private Canvas canvas;

    public AnimationTime(Canvas canvas) {
        this.canvas = canvas;
    }

    public void run(Animation animation) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        AnimationTimer timer  = new AnimationTimer() {
            private long lastTime = -1;
            @Override
            public void handle(long now) {
                if ( lastTime < 0 ) {
                    lastTime = now;
                    return;
                }
                double deltaTime = (now - lastTime)/1e9;
                lastTime = now;
                animation.update(deltaTime);
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                animation.render(gc);

                if (animation.isFinished()) {
                    this.stop();
                }

            }
        };
        timer.start();
    }
}

