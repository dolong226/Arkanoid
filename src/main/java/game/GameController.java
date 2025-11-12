package game;

import sound.SoundManager;
import sound.AudioResource;

public class GameController {
    private final SoundManager soundManager = SoundManager.getInstance();
    private String currentMusicKey = null;

    public void initialize() {
        soundManager.preloadAll();
        currentMusicKey = AudioResource.BACKGROUND_MUSIC.name();
        soundManager.playMusicAsync(AudioResource.BACKGROUND_MUSIC.name());
    }

    public void onBallHitPaddle() {
        soundManager.playSFXAsync(AudioResource.BALL_HIT_PADDLE.name());
    }

    public void onBallHitBrick() {
        soundManager.playSFXAsync(AudioResource.BALL_HIT_BRICK.name());
    }

    public void onBrickDestroy() {
        soundManager.playSFXAsync(AudioResource.BRICK_DESTROY.name());
    }

    public void onPowerUpCollected() {
        soundManager.playSFXAsync(AudioResource.POWERUP_COLLECT.name());
    }

    public void onGameOver() {
        soundManager.stopAllMusic();
        soundManager.playSFXAsync(AudioResource.GAME_OVER.name());
        currentMusicKey = null;
    }

    public void onLevelComplete() {
        soundManager.stopAllMusic();
        soundManager.playSFXAsync(AudioResource.LEVEL_COMPLETE.name());
        currentMusicKey = null;
    }

    public void toggleSound() {
        boolean isMuted = soundManager.toggleMuted();
        if (isMuted) {
            System.out.println("MUTED");
        } else {
            System.out.println("UNMUTED");
            String key = AudioResource.LEVEL_COMPLETE.name();
            if (!soundManager.isMusicPlaying(key)) {
                soundManager.playMusicAsync(key, true);
            }
        }
    }

    public void setCurrentMusic(String musicKey) {
        this.currentMusicKey = musicKey;
    }

    public void onGamePause() {
        soundManager.stopAllMusic();
    }

    public void onGameResume() {
        if (currentMusicKey != null) {
            soundManager.playMusicAsync(currentMusicKey, true);
        }
    }

}
