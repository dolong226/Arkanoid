package input;

import java.awt.event.KeyEvent;

public class GameKeyboard implements Keyboard {
    private KeyboardState state; // trạng thái phím vừa nhấn hoặc đang nhấn.
    private KeyTimer timer; // quản lý thời gian nhấn và debounce.

    public GameKeyboard() {
        this.state = new KeyboardState();
        this.timer = new KeyTimer(KeyTimer.DEFAULT_DEBOUNCE_DELAY);
    }

    private Key mapKeyCodeToKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                return Key.LEFT;
            case KeyEvent.VK_RIGHT:
                return Key.RIGHT;
            case KeyEvent.VK_ENTER:
                return Key.ENTER;
            case KeyEvent.VK_ESCAPE:
                return Key.ESC;
            case KeyEvent.VK_P:
                return Key.PAUSE;
            case KeyEvent.VK_R:
                return Key.RESUME;
            case KeyEvent.VK_C:
                return Key.CONFIRM;
            default:
                return null;
        }
    }

    // Xử lý sự kiện phím được nhấn.
    public void onKeyPressed (KeyEvent event) {
        Key key = mapKeyCodeToKey(event.getKeyCode());
        if (key != null && timer.canTrigger(key)) {
            state.pressedKey(key);
            timer.updatePressTime(key);
        }
    }

    // Xử lý sự kiện phím được thả.
    public void onKeyReleased (KeyEvent event) {
        Key key = mapKeyCodeToKey(event.getKeyCode());
        if(key != null) {
            state.releaseKey(key);
        }
    }

    @Override
    public boolean isPressed (Key key) {
        return state.isPressed(key);
    }

    @Override
    public boolean wasJustPressed (Key key) {
        return state.wasJustPressed(key);
    }

    @Override
    public void update() {
        state.update();
    }
}
