package thread;

import sound.SoundManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class AudioThread {


    /** Instance duy nhất của AudioThread */
    private static AudioThread instance;

    public static synchronized AudioThread getInstance(SoundManager soundManager) {
        if (instance == null) {
            instance = new AudioThread(soundManager);
        }
        return instance;
    }

    public static AudioThread getInstance() {
        return instance;
    }

    private final ExecutorService executor;
    private final SoundManager soundManager;
    private volatile boolean isShutdown;

    private AudioThread(SoundManager soundManager) {
        this.soundManager = soundManager;
        this.isShutdown = false;

        this.executor = Executors.newFixedThreadPool(4, r -> {
            Thread thread = new Thread(r);
            thread.setName("AudioThread-Worker");
            thread.setDaemon(true);
            thread.setPriority(Thread.NORM_PRIORITY - 1); // Priority thấp hơn game loop một chút
            return thread;
        });
    }


    public void playSFXAsync(String soundName) {
        if (isShutdown) {
            return;
        }

        // Gọi phương thức đồng bộ của SoundManager bên trong task để tránh recursion
        executor.submit(() -> {
            try {
                soundManager.playSFX(soundName);

            } catch (Exception e) {
                System.err.println("AudioThread Error playing SFX: " + soundName);
                e.printStackTrace();
            }
        });
    }

    public void playSFXAsync(String soundName, double volume) {
        if (isShutdown) {
            System.err.println("AudioThread Cannot play SFX ");
            return;
        }

        executor.submit(() -> {
            try {
                soundManager.playSFX(soundName, volume);
            } catch (Exception e) {
                System.err.println("AudioThread Error playing SFX: " + soundName);
                e.printStackTrace();
            }
        });
    }

    public void playMusicAsync(String musicName, boolean loop) {
        if (isShutdown) {
            return;
        }

        executor.submit(() -> {
            try {
                soundManager.playMusic(musicName, loop);
            } catch (Exception e) {
                System.err.println("AudioThread Error playing music: " + musicName);
                e.printStackTrace();
            }
        });
    }

    public void stopMusicAsync(String musicName) {
        if (isShutdown) {
            return;
        }

        executor.submit(() -> {
            try {
                // Theo UML SoundManager không có stopMusic(key), nên nếu track đó đang phát thì dừng tất cả
                if (soundManager.isMusicPlaying(musicName)) {
                    soundManager.stopAllMusic();
                }
            } catch (Exception e) {
                System.err.println("AudioThread Error stopping music: " + musicName);
                e.printStackTrace();
            }
        });
    }

    public void stopAllMusicAsync() {
        if (isShutdown) {
            return;
        }

        executor.submit(() -> {
            try {
                soundManager.stopAllMusic();
            } catch (Exception e) {
                System.err.println("AudioThread Error stopping all music");
                e.printStackTrace();
            }
        });
    }

    public void shutdown() {
        if (isShutdown) {
            return;
        }

        System.out.println("AudioThread Shutting down");
        isShutdown = true;


        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {

                executor.shutdownNow();


                if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                    System.err.println("AudioThread Thread pool did not terminate");
                }
            }
        } catch (InterruptedException e) {
            System.err.println("AudioThread Shutdown interrupted");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("AudioThread Shutdown complete");
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    public int getPendingTaskCount() {

        return 0;
    }
}