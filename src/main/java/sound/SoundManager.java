package sound;

import thread.AudioThread;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.AudioClip;

/**
 * SoundManager - Quản lý âm thanh trong game
 */
public class SoundManager {

    private static SoundManager instance;

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }


    private Map<String, AudioClip> sfxMap = new HashMap<>();
    private Map<String, AudioClip> musicMap = new HashMap<>();
    private double sfxVolume = 1.0;
    private double musicVolume = 0.7;
    private boolean muted = false;
    private boolean isInitialized = false;

    /**
     * AudioThread để play sound bất đồng bộ
     * Chỉ khởi tạo khi preloadAll() được gọi
     */
    private AudioThread audioThread;

    private SoundManager() {
        this.sfxMap = new HashMap<>();
        this.musicMap = new HashMap<>();
    }

    /**
     * load sound
     */
    private void loadSounds() {
        for (AudioResource resource : AudioResource.values()) {
            String key = resource.name();
            String path = null;
            boolean isMusic = false;

            switch (resource) {
                case BACKGROUND_MUSIC:
                    path = "/sound/sound-background.mp3";
                    isMusic = true;
                    break;
                case BALL_HIT_BRICK:
                    path = "/sound/ball-hit-brick.mp3";
                    break;
                case BALL_HIT_PADDLE:
                    path = "/sound/ball-hit-paddle.mp3";
                    break;
                case BRICK_DESTROY:
                    path = "/sound/sound-brick-destroy.mp3";
                    break;
                case POWERUP_COLLECT:
                    path = "/sound/powerup-collect.mp3";
                    break;
                case POWERUP_SPAWN:
                    path = "/sound/powerup-spawn.mp3";
                    break;
                case WALL_HIT:
                    path = "/sound/wall-hit.mp3";
                    break;
                case LEVEL_COMPLETE:
                    path = "/sound/sound-level.mp3";
                    break;
                case GAME_OVER:
                    path = "/sound/sound-game-over.mp3";
                    break;
            }

            if (path != null) {
                loadAudioClip(key, path, isMusic);
            } else {
                System.out.println("SoundManager Warning: AudioResource " + key + " not found.");
            }
        }
    }

    private void loadAudioClip(String key, String path, boolean isMusic) {
        try {
            URL url = SoundManager.class.getResource(path);
            if (url == null) {
                System.out.println("SoundManager Resource not found: " + path);
                return;
            }
            AudioClip clip = new AudioClip(url.toExternalForm());
            if (isMusic) {
                musicMap.put(key, clip);
            } else {
                sfxMap.put(key, clip);
            }
        } catch (Exception e) {
            System.out.println("SoundManager Error loading " + key + ": " + e.getMessage());
        }
    }


    /**
     * Play sound effect
     */
    public void playSFX(String key) {
        playSFX(key, sfxVolume);
    }

    public void playSFX(String key, double volume) {
        if (muted || !isInitialized) {
            return;
        }

        AudioClip clip = sfxMap.get(key);
        if (clip != null) {
            clip.setVolume(volume * sfxVolume);
            clip.play();
        } else {
            System.out.println("SoundManager SFX not found: " + key);
        }
    }

    public void playMusic(String key) {
        playMusic(key, true);
    }


    public void playMusic(String key, boolean loop) {
        if (muted || !isInitialized) {
            return;
        }

        // Stop all music trước (đồng bộ)
        stopAllMusic();

        AudioClip newClip = musicMap.get(key);
        if (newClip != null) {
            newClip.setCycleCount(loop ? AudioClip.INDEFINITE : 1);
            newClip.setVolume(musicVolume);
            newClip.play();
        } else {
            System.out.println("[SoundManager] Music not found: " + key);
        }
    }


    public void playSFXAsync(String key) {
        if (audioThread != null && !audioThread.isShutdown()) {
            audioThread.playSFXAsync(key);
        } else {
            playSFX(key);
        }
    }

    public void playSFXAsync(String key, double volume) {
        if (audioThread != null && !audioThread.isShutdown()) {
            audioThread.playSFXAsync(key, volume);
        } else {
            playSFX(key, volume);
        }
    }

    public void playMusicAsync(String key) {
        playMusicAsync(key, true);
    }

    public void playMusicAsync(String key, boolean loop) {
        if (audioThread != null && !audioThread.isShutdown()) {
            audioThread.playMusicAsync(key, loop);
        } else {
            playMusic(key, loop);
        }
    }

    public void stopAllMusic() {
        for (AudioClip clip : musicMap.values()) {
            if (clip.isPlaying()) {
                clip.stop();
            }
        }
    }

    public void stopSFX(String key) {
        AudioClip clip = sfxMap.get(key);
        if (clip != null) {
            clip.stop();
        }
    }

    public void setSFXVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
        for (AudioClip clip : sfxMap.values()) {
            clip.setVolume(this.sfxVolume);
        }
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        for (AudioClip clip : musicMap.values()) {
            clip.setVolume(this.musicVolume);
        }
    }

    public void setMasterVolume(double volume) {
        double masterVolume = Math.max(0.0, Math.min(1.0, volume));
        setSFXVolume(masterVolume);
        setMusicVolume(masterVolume);
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        if (muted) {
            stopAllMusic();
        }
    }

    public boolean toggleMuted() {
        setMuted(!this.muted);
        return this.muted;
    }

    public boolean isMuted() {
        return muted;
    }


    public boolean isMusicPlaying(String key) {
        AudioClip clip = musicMap.get(key);
        return clip != null && clip.isPlaying();
    }

    public double getSFXVolume() {
        return sfxVolume;
    }

    public double getMusicVolume() {
        return musicVolume;
    }


    public void preloadAll() {
        System.out.println("SoundManager Preloading sounds");

        // Load tất cả audio clips
        loadSounds();

        // Khởi tạo AudioThread
        this.audioThread = AudioThread.getInstance(this);

        this.isInitialized = true;
        System.out.println("[SoundManager] Preload complete - " +
                sfxMap.size() + " SFX, " +
                musicMap.size() + " music tracks loaded");
    }


    /**
     * Có thể dùng để clear cache nếu cần
     */
    public void cleanup() {
        // todo
    }

    /**
     * Dispose - dọn dẹp hoàn toàn khi thoát game
     * Stop tất cả sounds, shutdown AudioThread, clear maps
     */
    public final void dispose() {
        System.out.println("SoundManager Disposing");

        // Stop tất cả sounds
        stopAllMusic();
        sfxMap.values().forEach(AudioClip::stop);

        // Shutdown AudioThread
        if (audioThread != null) {
            audioThread.shutdown();
            audioThread = null;
        }

        // Clear maps
        sfxMap.clear();
        musicMap.clear();

        // Reset singleton
        instance = null;
    }
}