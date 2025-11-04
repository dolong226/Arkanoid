package sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.LineUnavailableException;



public class SoundManager {
    private static SoundManager instance;
    private Map<String, Clip> sfxMap = new HashMap<>();
    private Map<String, Clip> musicMap = new HashMap<>();
    private double sfxVolume = 1.0;
    private double musicVolume = 0.7;
    private double masterVolume = 1.0;
    private boolean muted = false;
    private boolean isInitialized = false;

    private SoundManager() {}

    public static synchronized SoundManager getInstance() {
        if (instance == null ) {
            instance = new SoundManager();
            instance.initialize();
        }
        return instance;
    }

    private void initialize() {
        if (!isInitialized) {
            loadSounds();
            isInitialized = true;
        }
    }

    private float linearToDecibel(double linearVolume) {
        if (linearVolume <= 0.000001) {
            return -80.0f;
        }
        return (float) (20.0 * Math.log10(linearVolume));
    }

    private void setClipVolume(Clip clip, double linearVolume) {
        if (clip == null || !clip.isOpen()) return;
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = linearToDecibel(linearVolume);
            dB = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB));
            gainControl.setValue(dB);
        }
    }

    private double getFinalVolume(double baseVolume) {
        if (muted) {
            return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, baseVolume * masterVolume));
    }

    private void loadSounds() {
        for ( AudioResource resource : AudioResource.values()) {
            String key = resource.name();
            String path = null;
            boolean isMusic = false;
            switch ( resource ) {
                case BACKGROUND_MUSIC:
                    path = "/resources/sound/sound-background.mp3";
                    isMusic = true;
                    break;
                case BRICK_DESTROY:
                    path = "/resources/sound/sound-brick-destroy.mp3";
                    break;
                case LEVEL_COMPLETE:
                    path = "/resources/sound/sound-level.mp3";
                    break;
                case GAME_OVER:
                    path = "/resources/sound/sound-game-over.mp3";
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
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            if(isMusic) {
                musicMap.put(key, clip);
            } else {
                sfxMap.put(key, clip);
            }
            audioStream.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading" + key + ":" + e.getMessage());
        }
    }

    public void playSFX(String key) {
        playSFX(key, 1.0);
    }

    public void playSFX(String key, double volume) {
        Clip clip = sfxMap.get(key);
        if (clip != null) {
            if(!clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            double finalVolume = getFinalVolume(volume * sfxVolume);
            setClipVolume(clip, finalVolume);
            clip.start();
        } else {
            System.out.println("SFX not found: " + key);
        }
    }

    public void playMusic(String key) {
        playMusic(key, true);
    }

    public void playMusic(String key, boolean loop) {
        stopAllMusic();
        Clip clip = musicMap.get(key);
        if (clip != null) {
            clip.setFramePosition(0);
            double finalVolume = getFinalVolume(musicVolume);
            setClipVolume(clip, finalVolume);
            if(loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.loop(0);
            }
            clip.start();
        } else {
            System.out.println("Music not found: " + key);
        }
    }

    public void stopAllMusic() {
        for (Clip clip : musicMap.values()) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }

    public void stopSFX (String key) {
        Clip clip = sfxMap.get(key);
        if (clip != null) {
            clip.stop();
        }
    }

    public void setSFXVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
        System.out.println("SFX volume set to: " + this.sfxVolume);
        double finalVolume = getFinalVolume(this.sfxVolume);
        for (Clip clip : sfxMap.values()) {
            if (clip.isRunning()) {
                setClipVolume(clip, finalVolume);
            }
        }
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        System.out.println("Music volume set to: " + this.musicVolume);
        double finalVolume = getFinalVolume(this.musicVolume);
        for (Clip clip : musicMap.values()) {
            if (clip.isRunning()) {
                setClipVolume(clip, finalVolume);
            }
        }
    }

    public void setMasterVolume(double volume) {
        this.masterVolume = Math.max(0.0, Math.min(1.0, volume));
        System.out.println("Master volume set to: " + this.masterVolume);
        setMusicVolume(this.musicVolume);
        setSFXVolume(this.sfxVolume);
    }

    public void setMuted(boolean muted) {
        if (this.muted != muted ) {
            this.muted = muted;
            System.out.println("Muted set to: " + this.muted);
            setMasterVolume(this.musicVolume);
        }
    }

    public boolean toggleMuted() {
        setMuted(!this.muted);
        return this.muted;
    }

    public boolean isMuted() {
        return this.muted;
    }

    public boolean isMusicPlaying(String key) {
        Clip clip = musicMap.get(key);
        return clip != null && clip.isRunning();
    }

    public double getSFXVolume() {
        return this.sfxVolume;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void preloadAll() {
        getInstance();
    }

    public void cleanup() {
        stopAllMusic();
        for (Clip clip : sfxMap.values()) {
            clip.close();
        }
        for (Clip clip : musicMap.values()) {
            clip.close();
        }
        sfxMap.clear();
        musicMap.clear();
    }

    public final void dispose() {
        cleanup();
    }
}
