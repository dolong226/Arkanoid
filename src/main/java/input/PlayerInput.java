package input;

import geometry.Point;

public class PlayerInput {
    private Keyboard keyboard;
    private Mouse mouse;

    public PlayerInput(Keyboard keyboard, Mouse mouse) {
        this.keyboard = keyboard;
        this.mouse = mouse;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public boolean isMoveLeft() {
        return keyboard.isPressed(Key.LEFT);
    }

    public boolean isMoveRight() {
        return keyboard.isPressed(Key.RIGHT);
    }

    public boolean isActivatePower() {
        return keyboard.wasJustPressed(Key.ENTER);
    }

    public boolean isPause() {
        return keyboard.wasJustPressed(Key.PAUSE);
    }

    public boolean isExit() {
        return keyboard.wasJustPressed(Key.ESC);
    }

    public boolean isConfirm() {
        return keyboard.wasJustPressed(Key.CONFIRM);
    }

    public char readChar() {
        // bổ sung sau
        // Trả về '\0' nếu chưa có ký tự nhập
        return '\0';
    }

    public boolean isClickLeft() {
        return mouse.wasJustPressed(Key.MOUSE_LEFT);
    }

    public Point getMousePosition() {
        return mouse.getMousePosition();
    }

}
