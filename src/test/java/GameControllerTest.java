import org.junit.*;
import sound.AudioResource;
import sound.SoundManager;
import game.GameController;
import static org.junit.Assert.*;

public class GameControllerTest {

    private GameController gameController;
    private SoundManager soundManager;

    @Before
    public void setUp() {
        System.setProperty("skipAudioLoad", "true");
        soundManager = SoundManager.getInstance();
        gameController = new GameController();
    }

    @After
    public void tearDown() {
        soundManager.dispose();
        System.clearProperty("skipAudioLoad");
    }

    /**
     * Kiểm tra phương thức setCurrentMusic.
     */
    @Test
    public void testSetCurrentMusic() throws Exception {
        gameController.setCurrentMusic("TEST_MUSIC");
        java.lang.reflect.Field field = GameController.class.getDeclaredField("currentMusicKey");
        field.setAccessible(true);
        String key = (String) field.get(gameController);
        assertEquals("TEST_MUSIC", key);
    }

    /**
     * Kiểm tra Pause và Resume.
     */
    @Test
    public void testOnGamePauseAndResume() throws Exception {
        gameController.setCurrentMusic(AudioResource.LEVEL_COMPLETE.name());
        java.lang.reflect.Field field = GameController.class.getDeclaredField("currentMusicKey");
        field.setAccessible(true);
        assertEquals(AudioResource.LEVEL_COMPLETE.name(), field.get(gameController));
        gameController.onGamePause();
        assertEquals(AudioResource.LEVEL_COMPLETE.name(), field.get(gameController));
        gameController.onGameResume();
        assertEquals(AudioResource.LEVEL_COMPLETE.name(), field.get(gameController));
    }

    /**
     * Kiểm tra việc gọi trên GameController.
     */
    @Test
    public void testToggleSoundChangesMuteState() throws Exception {
        java.lang.reflect.Field mutedField = SoundManager.class.getDeclaredField("muted");
        mutedField.setAccessible(true);
        assertFalse((boolean) mutedField.get(soundManager));
        gameController.toggleSound();
        assertTrue((boolean) mutedField.get(soundManager));
        gameController.toggleSound();
        assertFalse((boolean) mutedField.get(soundManager));
    }
}
