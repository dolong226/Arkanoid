package thread;

import collidable.Paddle;
import game.GameLevel;

/**
 * Thread riêng cho game loop
 * Chạy game ở tốc độ 60 fps
 * Gọi gameLevel.update() dể cập nhật trạng thái game
 * Hỗ trợ pause/resume game.
 */
public class GameLoopThread extends Thread{
    /** Số fream mục tiêu mỗi giây */
    private static final double TARGET_FPS = 60.0;

    /** Thời gian mỗi frame */
    private static final long FRAME_TIME_MS = (long) (1000.0 / TARGET_FPS);

    /** Thời gian mỗi fream để truyền vào update() */
    private static final double DELTA_TIME = 1.0 / TARGET_FPS;

    /**
     * GameLevel instance để gọi update()
     */
    private final GameLevel gameLevel;

    /**
     * kiểm soát vòng lặp chính
     * volatile: đảm bảo thay đổi từ thread khác được thấy ngay lập tức.
     */
    private volatile boolean running;

    /**
     * Kiểm soát trạng thái pause game.
     */
    private volatile boolean paused;

    /**
     *
     */
    private final Object pauseLock;

    /**
     * @param gameLevel instance của GameLevel để update
     */
    public GameLoopThread(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
        this.running = false;
        this.paused = false;
        this.pauseLock = new Object();

        this.setName("GameLoopThread");

        // Đặt mức ưu tiên cao hơn một chút
        this.setPriority(Thread.NORM_PRIORITY + 1);
    }

    /**
     * Bắt đầu game loop thread.
     */
    public void startGameLoop() {
        if (!running) {
            running = true;
            // Gọi Thread.start() để bắt đầu run.
            this.start();
        }
    }

    /**
     * Dừng game loop thread.
     */
    public void stopGameLoop() {
        running = false;

        // Nếu game đang pause, cần resume đẻ thread có thể thoát vòng lặp.
        if (paused) {
            resumeGameLoop();
        }
        try {
            this.join(1000); // đợi 1 s
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Tạm dừng game loop.
     */
    public void pauseGameLoop() {
        paused = true;
    }

    /**
     * Tiếp tục game loop sau khi pause
     * Notify để thoát khỏi trạng thái wait().
     */
    public void resumeGameLoop() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notify();
        }
    }

    /**
     * Kiểm tra thread có đang chạy hay không.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Kiểm tra game loop có đang pause không.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * method chính của thread: chạy game loop
     * được gọi tự động khi start() thread
     */
    @Override
    public void run() {
        System.out.println("Game Loop Thread started");

        // Thời điểm bắt đầu mỗi frame.
        long frameStartTime;

        // Thời gian thực tế đã dùng để xử lí frame/
        long frameProcessingTime;

        // Thời gian cần sleep để đặt 60 fps
        long sleepTime;

        // main game loop
        while (running) {
            frameStartTime = System.currentTimeMillis();

            try {
                synchronized (pauseLock) {
                    while (paused && running) {
                        pauseLock.wait();
                    }
                }

                if (running && !paused) {
                    gameLevel.update(DELTA_TIME);
                }

                frameProcessingTime = System.currentTimeMillis() - frameStartTime;

                sleepTime = FRAME_TIME_MS - frameProcessingTime;

                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                } else {
                    if (frameProcessingTime > FRAME_TIME_MS * 2) {
                        System.out.println("GameLoopThread WARNING: Frame took "
                                + frameProcessingTime + "ms (target: " + FRAME_TIME_MS + "ms)");
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("GameLoopThread Interrupted");
                running = false;
                Thread.currentThread().interrupt();
            } catch (Exception e) {

                System.err.println("GameLoopThread Error in game loop:");
                e.printStackTrace();
            }
        }
        System.out.println("GameLoopThread Stopped");
    }

}
