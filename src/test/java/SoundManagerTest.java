import sound.SoundManager;
import org.junit.*;
import java.lang.reflect.Field;
import java.util.Map;
import static org.junit.Assert.*;

public class SoundManagerTest {

    private SoundManager soundManager;

    @Before
    public void setUp() {
        soundManager = SoundManager.getInstance();
    }

    @After
    public void tearDown() {
        soundManager.dispose();
    }

    /**
     * Kiểm tra Singleton có được triển khai đúng hay không.
     */
    @Test
    public void testSingletonInstance() {
        SoundManager other = SoundManager.getInstance();
        assertSame( soundManager, other);
    }

    /**
     * Kiểm tra giá trị âm lượng SFX.
     */
    @Test
    public void testSetSFXVolume() {
        soundManager.setSFXVolume(0.5);
        assertEquals(0.5, soundManager.getSFXVolume(), 1e-6);
    }

    /**
     * Kiểm tra khoảng âm lượng
     */
    @Test
    public void testVolume() {
        soundManager.setMusicVolume(-1.0);
        assertEquals(0.0, soundManager.getMusicVolume(), 1e-6);
        soundManager.setMusicVolume(2.0);
        assertEquals(1.0, soundManager.getMusicVolume(), 1e-6);
    }

    /**
     * Kiểm tra bật/tắt.
     */
    @Test
    public void testToggleMuted() {
        assertFalse(soundManager.isMuted());
        soundManager.setMuted(true);
        assertTrue(soundManager.isMuted());
        soundManager.setMuted(false);
        assertFalse(soundManager.isMuted());
    }

    /**
     * Kiểm tra trạng thái sau khi chuyển đổi.
     */
    @Test
    public void testToggleMutedReturnValue() {
        boolean current = soundManager.isMuted();
        boolean newState = soundManager.toggleMuted();
        assertNotEquals(current, newState);
    }

    /**
     * Kiểm tra reset lại Singleton.
     */
    @Test
    public void testDisposeClears() throws Exception {
        Field sfxMapField = SoundManager.class.getDeclaredField("sfxMap");
        sfxMapField.setAccessible(true);
        Map<?, ?> sfxMap = (Map<?, ?>) sfxMapField.get(soundManager);
        soundManager.dispose();
        SoundManager newInstance = SoundManager.getInstance();
        assertNotSame(soundManager, newInstance);
    }

    /**
     * Kiểm tra MasterVolume.
     */
    @Test
    public void testSetMasterVolume() {
        soundManager.setMasterVolume(0.3);
        assertEquals(0.3, soundManager.getMusicVolume(), 1e-6);
        assertEquals(0.3, soundManager.getSFXVolume(), 1e-6);
    }
}