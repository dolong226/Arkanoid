package game;

import sound.SoundManager;
import sound.AudioResource;

public class GameController {
    private final SoundManager soundManager = SoundManager.getInstance();

    public void initialize() {
        soundManager.preloadAll();
        soundManager.playMusic(AudioResource.BACKGROUND_MUSIC.name());
    }

    public void onBallHitPaddle() {
        soundManager.playSFX(AudioResource.BALL_HIT_PADDLE.name());
    }

    public void onBallHitBrick() {
        soundManager.playSFX(AudioResource.BALL_HIT_BRICK.name());
    }

    public void onBrickDestroy() {
        soundManager.playSFX(AudioResource.BRICK_DESTROY.name());
    }

    public void onPowerUpCollected() {
        soundManager.playSFX(AudioResource.POWERUP_COLLECT.name());
    }

    public void onGameOver() {
        soundManager.stopAllMusic();
        soundManager.playSFX(AudioResource.GAME_OVER.name());
    }

    public void onLevelComplete() {
        soundManager.stopAllMusic();
        soundManager.playSFX(AudioResource.LEVEL_COMPLETE.name());
    }

    public void toggleSound() {
        boolean isMuted = soundManager.toggleMuted();
        if (isMuted) {
            System.out.println("MUTED");
        } else {
            System.out.println("UNMUTED");
            String key = AudioResource.LEVEL_COMPLETE.name();
            if (!soundManager.isMusicPlaying(key)) {
                soundManager.playMusic(key, true);
            }
        }
    }
}
