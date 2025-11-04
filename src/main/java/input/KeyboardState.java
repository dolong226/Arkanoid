package input;

import java.util.HashSet;
import java.util.Set;

public class KeyboardState{
    private Set<Key> pressedKeys;
    private Set<Key> justPressedKeys; // Tập các phím vừa được nhấn trong frame này.
    private Set<Key> previousPressedKeys; // Tập các phím được nhấn trong frame trước.

    public KeyboardState() {
        this.pressedKeys = new HashSet<>();
        this.justPressedKeys = new HashSet<>();
        this.previousPressedKeys = new HashSet<>();
    }

    public KeyboardState(Set<Key> justPressedKeys, Set<Key> pressedKeys, Set<Key> previousPressedKeys) {
        this.justPressedKeys = justPressedKeys;
        this.pressedKeys = pressedKeys;
        this.previousPressedKeys = previousPressedKeys;
    }

    // thêm key vào pressedKey.
    public void pressedKey(Key key) {
        pressedKeys.add(key);
        if (!previousPressedKeys.contains(key)) {
            justPressedKeys.add(key);
        }
    }


    public void releaseKey(Key key) {
        pressedKeys.remove(key);
    }

    public boolean isPressed(Key key) {
        return pressedKeys.contains(key);
    }

    public boolean wasJustPressed(Key key) {
        return justPressedKeys.contains(key);
    }

    public void update() {
        previousPressedKeys.clear();
        previousPressedKeys.addAll(pressedKeys);
        justPressedKeys.clear();
    }
}
