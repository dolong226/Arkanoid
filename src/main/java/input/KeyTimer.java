package input;

import java.util.HashMap;
import java.util.Map;

public class KeyTimer {
    private Map<Key, Long> lastPressedTime; // lưu trữ thời gian gần nhất của mỗi phím.
    private long debounceDelay; // Thời gian tối thiểu giữa 2 làn nhấn.
    public static final long DEFAULT_DEBOUNCE_DELAY = 20;

    public KeyTimer(long debounceDelay) {
        if (debounceDelay <= 0) this.debounceDelay = DEFAULT_DEBOUNCE_DELAY;
        else this.debounceDelay = debounceDelay;
        this.lastPressedTime = new HashMap<>();
    }

    public long getDebounceDelay() {
        return debounceDelay;
    }

    public void setDebounceDelay(long debounceDelay) {
        if (debounceDelay < 0) {
            this.debounceDelay = DEFAULT_DEBOUNCE_DELAY;
            System.out.println("Thời gian delay không hợp lệ");
        } else this.debounceDelay = debounceDelay;
    }

    // Kiểm tra xem key có được kích hoạt tại thời điểm hiện tại hay không.
    public boolean canTrigger(Key key) {
        if (!lastPressedTime.containsKey(key)) return true;
        if (System.currentTimeMillis() - lastPressedTime.get(key) < debounceDelay) return false;
        return true;
    }

    public void updatePressTime(Key key) {
        lastPressedTime.put(key, System.currentTimeMillis());
    }
}