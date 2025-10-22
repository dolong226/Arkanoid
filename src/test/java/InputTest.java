import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import input.*;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class InputTest {
    private KeyTimer keyTimer;
    private KeyboardState keyboardState;
    private GameKeyboard gameKeyboard;
    private GameMouse gameMouse;
    private PlayerInput playerInput;
    private MultiInputHandler multiInputHandler;

    @Before
    public void setUp() {
        keyTimer = new KeyTimer(200);
        keyboardState = new KeyboardState();
        gameKeyboard = new GameKeyboard();
        gameMouse = new GameMouse();
        playerInput = new PlayerInput(gameKeyboard, gameMouse);
        multiInputHandler = new MultiInputHandler(gameKeyboard, gameMouse);
    }

    @Test
    public void testKeyTimerCanTriggerFirstPress() {
        assertTrue(keyTimer.canTrigger(Key.LEFT));
    }

    @Test
    public void testKeyTimerDebounce() throws InterruptedException {
        keyTimer.updatePressTime(Key.LEFT);
        assertFalse(keyTimer.canTrigger(Key.LEFT));
        Thread.sleep(250);
        assertTrue(keyTimer.canTrigger(Key.LEFT));
    }

    @Test
    public void testKeyTimerInvalidDebounceDelay() {
        keyTimer.setDebounceDelay(-1);
        assertEquals(20, keyTimer.getDebounceDelay());
    }

    @Test
    public void testKeyboardStatePressAndReleaseKey() {
        keyboardState.pressedKey(Key.LEFT);
        assertTrue(keyboardState.isPressed(Key.LEFT));
        assertTrue(keyboardState.wasJustPressed(Key.LEFT));
        keyboardState.releaseKey(Key.LEFT);
        assertFalse(keyboardState.isPressed(Key.LEFT));
        keyboardState.update();
        assertFalse(keyboardState.wasJustPressed(Key.LEFT));
    }

    @Test
    public void testKeyboardStateUpdateResetsJustPressed() {
        keyboardState.pressedKey(Key.RIGHT);
        assertTrue(keyboardState.wasJustPressed(Key.RIGHT));
        keyboardState.update();
        assertFalse(keyboardState.wasJustPressed(Key.RIGHT));
        assertTrue(keyboardState.isPressed(Key.RIGHT));
    }

    @Test
    public void testGameKeyboardKeyPressAndRelease() {
        gameKeyboard.onKeyPressed(new KeyEvent(new Frame(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, ' '));
        assertTrue(gameKeyboard.isPressed(Key.LEFT));
        assertTrue(gameKeyboard.wasJustPressed(Key.LEFT));
        gameKeyboard.onKeyReleased(new KeyEvent(new Frame(), KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, ' '));
        assertFalse(gameKeyboard.isPressed(Key.LEFT));
    }

    @Test
    public void testGameKeyboardUpdate() {
        gameKeyboard.onKeyPressed(new KeyEvent(new Frame(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, ' '));
        assertTrue(gameKeyboard.wasJustPressed(Key.RIGHT));
        gameKeyboard.update();
        assertFalse(gameKeyboard.wasJustPressed(Key.RIGHT));
        assertTrue(gameKeyboard.isPressed(Key.RIGHT));
    }

    @Test
    public void testGameKeyboardDebounce() throws InterruptedException {
        gameKeyboard.onKeyPressed(new KeyEvent(new Frame(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' '));
        assertTrue(gameKeyboard.isPressed(Key.ENTER));
        gameKeyboard.onKeyPressed(new KeyEvent(new Frame(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' '));
        assertTrue(gameKeyboard.isPressed(Key.ENTER));
        Thread.sleep(250);
        gameKeyboard.onKeyPressed(new KeyEvent(new Frame(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' '));
        assertTrue(gameKeyboard.isPressed(Key.ENTER));
    }

    @Test
    public void testPlayerInputMovement() {
        gameKeyboard.onKeyPressed(new KeyEvent(new Frame(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, ' '));
        assertTrue(playerInput.isMoveLeft());
        assertFalse(playerInput.isMoveRight());

        gameKeyboard.onKeyReleased(new KeyEvent(new Frame(), KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, ' '));
        gameKeyboard.onKeyPressed(new KeyEvent(new Frame(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, ' '));
        assertFalse(playerInput.isMoveLeft());
        assertTrue(playerInput.isMoveRight());
    }

    @Test
    public void testPlayerInputActions() {
        // Pause
        gameKeyboard.onKeyPressed(new KeyEvent(new Frame(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_P, ' '));
        assertTrue(playerInput.isPause());
        gameKeyboard.update();
        assertFalse(playerInput.isPause());

        // Exit
        gameKeyboard.onKeyPressed(new KeyEvent(new Frame(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_ESCAPE, ' '));
        assertTrue(playerInput.isExit());

        // Confirm + ActivatePower (Enter)
        gameKeyboard.onKeyPressed(new KeyEvent(new Frame(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n'));
        assertTrue(playerInput.isActivatePower());
    }

    @Test
    public void testMultiInputHandlerSinglePlayer() {
        PlayerInput player = multiInputHandler.getPlayerInput(0);
        assertNotNull(player);
        gameKeyboard.onKeyPressed(new KeyEvent(new Frame(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, ' '));
        gameKeyboard.update();
        assertTrue(player.isMoveLeft());
    }

    @Test
    public void testMultiInputHandlerInvalidId() {
        assertNull(multiInputHandler.getPlayerInput(-1));
        assertNull(multiInputHandler.getPlayerInput(1));
    }

    @Test
    public void testMouseClickAndPosition() {
        // Click left
        MouseEvent press = new MouseEvent(new Frame(),
                MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(),
                0, 100, 200, 1, false,
                MouseEvent.BUTTON1);
        gameMouse.onMousePressed(press);
        assertTrue(playerInput.isClickLeft());

        // Release left
        MouseEvent release = new MouseEvent(new Frame(),
                MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(),
                0, 100, 200, 1, false,
                MouseEvent.BUTTON1);
        gameMouse.onMouseReleased(release);
        assertFalse(gameMouse.isPressed(Key.MOUSE_LEFT));

        // Move mouse
        MouseEvent move = new MouseEvent(new Frame(),
                MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(),
                0, 150, 250, 0, false);
        gameMouse.onMouseMoved(move);
        assertEquals(new Point(150, 250), playerInput.getMousePosition());
    }
}