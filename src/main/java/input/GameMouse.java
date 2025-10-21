package input;

import java.awt.Point;
import java.awt.event.MouseEvent;

public class GameMouse implements Mouse {
    private MouseState state;
    private KeyTimer timer;

    public GameMouse() {
        this.state = new MouseState();
        this.timer = new KeyTimer(KeyTimer.DEFAULT_DEBOUNCE_DELAY);
    }

    private Key mapButtonToKey(int button) {
        switch (button) {
            case MouseEvent.BUTTON1:
                return Key.MOUSE_LEFT;
            case MouseEvent.BUTTON2:
                return Key.MOUSE_MIDDLE;
            case MouseEvent.BUTTON3:
                return Key.MOUSE_RIGHT;
            default:
                return null;
        }
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
        state.setMousePosition(event.getX(), event.getY());
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