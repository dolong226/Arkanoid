package input;

import geometry.Point;

public interface Mouse {
    boolean isPressed(Key key);

    boolean wasJustPressed(Key key);

    Point getMousePosition();

    void update();
}
