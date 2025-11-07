package sound;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.AudioClip;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, AudioClip> sfxMap = new HashMap<>();
    private Map<String, AudioClip> musicMap = new HashMap<>();
    private double sfxVolume = 1.0;
    private double musicVolume = 0.7;
    private boolean muted = false;
    private boolean isInitialized = false;

    private SoundManager() {
        this.sfxMap = new HashMap<>();
        this.musicMap = new HashMap<>();
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null ) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadSounds() {
        for ( AudioResource resource : AudioResource.values()) {
            String key = resource.name();
            String path = null;
            boolean isMusic = false;
            switch ( resource ) {
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
                System.out.println("Warning: AudioResource " + key + " not found.");
            }
        }
    }

    private void loadAudioClip (String key, String path, boolean isMusic) {
        try {
            URL url = SoundManager.class.getResource(path);
            if (url == null) {
                System.out.println("Resource not found in classpath: " + path);
                return;
            }
            AudioClip clip = new AudioClip(url.toExternalForm());
            if(isMusic) {
                musicMap.put(key, clip);
            } else {
                sfxMap.put(key, clip);
            }
        } catch (Exception e) {
            System.out.println("Error loading" + key + ":" + e.getMessage());
        }
    }

    public void playSFX(String key) {
        playSFX(key, sfxVolume);
    }

    public void playSFX(String key, double volume) {
        if ( muted || !isInitialized ) {
            return;
        }
        AudioClip clip = sfxMap.get(key);
        if (clip != null) {
            clip.setVolume(volume * sfxVolume);
            clip.play();
        } else {
            System.out.println("SFX not found: " + key);
        }
    }

    public void playMusic(String key) {
        playMusic(key, true);
    }

    public void playMusic(String key, boolean loop) {
        if ( muted || !isInitialized ) {
            return;
        }
        stopAllMusic();
        AudioClip newClip = musicMap.get(key);
        if (newClip != null) {
            newClip.setCycleCount(loop ? AudioClip.INDEFINITE : 1);
            newClip.setVolume(musicVolume);
            newClip.play();
        }
    }

    public void stopAllMusic() {
        for (AudioClip clip : musicMap.values()) {
            if (clip.isPlaying()) {
                clip.stop();
            }
        }
    }

    public void stopSFX (String key) {
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
        loadSounds();
        this.isInitialized = true;
    }

    public void cleanup() {

    }

    public final void dispose() {
        stopAllMusic();
        sfxMap.values().forEach(AudioClip::stop);
        sfxMap.clear();
        musicMap.clear();
        instance = null;
    }
}
