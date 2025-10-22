package input;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class KeyTimerTest {

    private KeyTimer keyTimer;

    @Before
    public void setUp() {
        keyTimer = new KeyTimer(50); // debounceDelay = 50ms
    }

    @Test
    public void testCanTrigger_FirstTime_ShouldReturnTrue() {
        assertTrue(keyTimer.canTrigger(Key.LEFT));
    }

    @Test
    public void testCanTrigger_ImmediatelyAfterUpdate_ShouldReturnFalse() {
        keyTimer.updatePressTime(Key.LEFT);
        assertFalse(keyTimer.canTrigger(Key.LEFT));
    }

    @Test
    public void testCanTrigger_AfterDelay_ShouldReturnTrue() throws InterruptedException {
        keyTimer.updatePressTime(Key.LEFT);
        Thread.sleep(60); // chờ hơn debounceDelay
        assertTrue(keyTimer.canTrigger(Key.LEFT));
    }

    @Test
    public void testSetDebounceDelay_InvalidValue_ShouldResetToDefault() {
        keyTimer.setDebounceDelay(-10);
        assertEquals(KeyTimer.DEFAULT_DEBOUNCE_DELAY, keyTimer.getDebounceDelay());
    }

    @Test
    public void testSetDebounceDelay_ValidValue_ShouldUpdate() {
        keyTimer.setDebounceDelay(100);
        assertEquals(100, keyTimer.getDebounceDelay());
    }
}