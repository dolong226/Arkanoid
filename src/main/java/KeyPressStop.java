import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class KeyPressStop implements Animation {
    private Scene scenes;
    private Animation animation;
    private boolean stop = false;

    public KeyPressStop(Scene scenes, Animation animation) {
        this.scenes = scenes;
        this.animation = animation;

        scenes.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                stop = true;
            }
        });
    }

    @Override
    public void doOneFrame(GraphicsContext gc, double deltaTime) {
        animation.doOneFrame(gc, deltaTime);
    }

    public boolean shouldStop() {
        return stop || animation.shouldStop();
    }
}
