package input;

import javafx.scene.input.KeyEvent;

public class GameKeyboard implements Keyboard {
    private KeyboardState state;
    private KeyTimer timer;

    public GameKeyboard() {
        this.state = new KeyboardState();
        this.timer = new KeyTimer(KeyTimer.DEFAULT_DEBOUNCE_DELAY);
    }

    public GameKeyboard(KeyboardState state, KeyTimer timer) {
        this.state = state;
        this.timer = timer;
    }

    private Key mapKeyCodeToKey(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                return Key.LEFT;
            case RIGHT:
                return Key.RIGHT;
            case ENTER:
                return Key.ENTER;
            case ESCAPE:
                return Key.ESC;
            case P:
                return Key.PAUSE;
            case R:
                return Key.RESUME;
            case C:
                return Key.CONFIRM;
            default:
                return null;
        }
    }

    //  xử lý phím nhấn
    public void onKeyPressed(KeyEvent event) {
        Key key = mapKeyCodeToKey(event);
        if (key != null && timer.canTrigger(key)) {
            state.pressedKey(key);
            timer.updatePressTime(key);
        }
    }

    //  xử lý phím thả
    public void onKeyReleased(KeyEvent event) {
        Key key = mapKeyCodeToKey(event);
        if (key != null) {
            state.releaseKey(key);
        }
    }

    @Override
    public boolean isPressed(Key key) {
        return state.isPressed(key);
    }

    @Override
    public boolean wasJustPressed(Key key) {
        return state.wasJustPressed(key);
    }

    @Override
    public void update() {
        state.update();
    }
}
