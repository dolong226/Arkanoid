package input;

import geometry.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

public class GameMouse implements Mouse {
    private MouseState state;
    private KeyTimer timer;
    private Canvas canvas;

    public GameMouse() {
        this.state = new MouseState();
        this.timer = new KeyTimer(KeyTimer.DEFAULT_DEBOUNCE_DELAY);
    }

    public GameMouse(Canvas canvas) {
        this.canvas = canvas;
        this.state = new MouseState();
        this.timer = new KeyTimer(KeyTimer.DEFAULT_DEBOUNCE_DELAY);
    }

    private Key mapButtonToKey(MouseButton button) {
        if (button == MouseButton.PRIMARY) {
            return Key.MOUSE_LEFT;
        } else if (button == MouseButton.MIDDLE) {
            return Key.MOUSE_MIDDLE;
        } else if (button == MouseButton.SECONDARY) {
            return Key.MOUSE_RIGHT;
        }
        return null;
    }

    public void onMousePressed(MouseEvent event) {
        Key key = mapButtonToKey(event.getButton());
        if (key != null && timer.canTrigger(key)) {
            state.pressButton(key);
            timer.updatePressTime(key);
        }
    }

    public void onMouseReleased(MouseEvent event) {
        Key key = mapButtonToKey(event.getButton());
        if (key != null) {
            state.releaseButton(key);
        }
    }

    public void onMouseMoved(MouseEvent event) {
        double sceneX = event.getSceneX();
        double sceneY = event.getSceneY();

        double canvasX = sceneX - canvas.getLayoutX();
        double canvasY = sceneY - canvas.getLayoutY();
        state.setMousePosition(canvasX, canvasY);
    }

    @Override
    public boolean isPressed(Key key) {
        return state.isPressed(key);
    }

    @Override
    public boolean wasJustPressed(Key key) {
        return state.isJustPressed(key);
    }

    @Override
    public Point getMousePosition() {
        return state.getMousePosition();
    }

    @Override
    public void update() {
        state.resetJustPressed();
    }
}