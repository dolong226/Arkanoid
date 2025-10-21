package input;

import java.awt.Point;

public interface Mouse {
    boolean isPressed(Key key);

    boolean wasJustPressed(Key key);

    Point getMousePosition();

    void update();
}
