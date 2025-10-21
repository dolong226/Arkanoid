package input;

public interface Keyboard {

    // Xem phím nào đang được nhấn.
    boolean isPressed(Key key);

    // Xem phím đó giữ/nhấn một lần/nhấn nhiều lần.
    boolean wasJustPressed(Key key);

    // cập nhật trạng thái.
    void update();
}
