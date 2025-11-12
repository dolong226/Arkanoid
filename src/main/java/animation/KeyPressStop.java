package animation;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class KeyPressStop implements Animation {
    private final Scene scene;
    private final Animation animation;
    private final KeyCode stopKey;
    private boolean stop;

    public KeyPressStop(Scene scene, Animation animation, KeyCode stopKey) {
        this.scene = scene;
        this.animation = animation;
        this.stopKey = stopKey;
        this.stop = false;

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == stopKey) {
                stop = true;
            }
        });
    }

    @Override
    public void update(double deltaTime) {
        animation.update(deltaTime);
    }

    @Override
    public void render(GraphicsContext gc) {
        animation.render(gc);
    }

    @Override
    public boolean isFinished() {
        return stop || animation.isFinished();
    }
}
