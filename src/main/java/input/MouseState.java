package input;


import geometry.Point;

import java.util.HashSet;
import java.util.Set;

public class MouseState {
    private Set<Key> pressedButtons;
    private Set<Key> justPressedButtons;
    private Point mousePosition;

    public MouseState() {
        this.pressedButtons = new HashSet<>();
        this.justPressedButtons = new HashSet<>();
        this.mousePosition = new Point(0, 0);
    }

    // Nhấn nút chuột (trái)
    public void pressButton(Key key) {
        if (key == Key.MOUSE_LEFT) {
            pressedButtons.add(key);
            justPressedButtons.add(key);
        }

        // todo
    }

    // Thả nút chuột (trái)
    public void releaseButton(Key key) {
        if (key == Key.MOUSE_LEFT) {
            pressedButtons.remove(key);
        }

        // todo
    }

    // Cập nhật vị trí chuột
    public void setMousePosition(int x, int y) {
        this.mousePosition.setLocation(x, y);
    }

    // Kiểm tra nút có đang được giữ không
    public boolean isPressed(Key key) {
        return pressedButtons.contains(key);
    }

    // Kiểm tra nút vừa được nhấn trong frame này.
    public boolean isJustPressed(Key key) {
        return justPressedButtons.contains(key);
    }

    // Lấy vị trí chuột hiện tại
    public Point getMousePosition() {
        return mousePosition;
    }

    // Reset trạng thái justPressed (gọi mỗi frame).
    public void resetJustPressed() {
        justPressedButtons.clear();
    }
}