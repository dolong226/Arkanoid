package game;

import animation.Animation;
import animation.AnimationRunner;
import ball.Ball;
import ball.Velocity;
import collidable.Block;
import collidable.Collidable;
import collidable.Paddle;
import geometry.Point;
import geometry.Rectangle;
import input.GameKeyboard;
import input.Key;
import input.Keyboard;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import level.LevelInformation;
import listener.BallRemove;
import listener.BlockRemove;
import listener.ScoreTrackingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này sẽ trình bày các đặc tính của một level, quản lý sprite, collildables, paddle, balls, background, là logic vòng lặp.
 */
public class GameLevel implements Animation {
    private LevelInformation levelInfo;
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private AnimationRunner animationRunner;
    private Paddle paddle;
    private Counter score;
    private List<Ball> balls = new ArrayList<>();
    private Counter remainingBlocks;
    private Counter remainingBalls;
    private Keyboard keyboard;
    private boolean running;
    private boolean needRestart = false;

    // SỬA: Thêm trạng thái chờ bấm Enter và đã bắn bóng chưa
    private boolean waitingForEnter = false;
    private boolean ballsLaunched = false;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int PADDLE_HEIGHT = 20;
    public static final int BALL_RADIUS = 6;
    public static final int DEATH_REGION_HEIGHT = 50;

    public GameLevel(LevelInformation levelInfo, Keyboard keyboard, AnimationRunner animationRunner) {
        this.levelInfo = levelInfo;
        this.keyboard = keyboard;
        this.animationRunner = animationRunner;
    }

    public GameLevel(LevelInformation level, AnimationRunner runner, GameKeyboard keyboard, Counter score) {
        this.levelInfo = level;
        this.animationRunner = runner;
        this.keyboard = keyboard;
        this.score = score;
    }

    // getter
    public Counter getScore() { return score; }
    public int getRemainingBlocks() { return remainingBlocks.getValue(); }
    public int getRemainingBalls() { return remainingBalls.getValue(); }
    public List<Ball> getBalls() { return balls; }
    public Paddle getPaddle() { return paddle; }

    /**
     * Khởi tạo mọi thứ trước khi level bắt đầu.
     */
    public void initialize() {
        // Các thành phần quản lý
        sprites = new SpriteCollection();
        environment = new GameEnvironment();
        score = new Counter(0);
        remainingBalls = new Counter(levelInfo.numberOfBalls());
        remainingBlocks = new Counter(levelInfo.numberOfBlocksToRemove());

        // Thêm background
        Sprite background = levelInfo.getBackground();
        if (background != null) {
            sprites.addSprite(background);
        }

        // tạo và thêm paddle
        double paddleWidth = levelInfo.paddleWidth();
        double paddleSpeed = levelInfo.paddleSpeed();
        // chọn vị trí paddle
        double paddleX = (SCREEN_WIDTH - paddleWidth)/2;
        double paddleY = SCREEN_HEIGHT - PADDLE_HEIGHT - 10;
        Rectangle paddleRect = new Rectangle(new Point(paddleX, paddleY), paddleWidth, PADDLE_HEIGHT);
        paddle = new Paddle((int) paddleSpeed, Color.YELLOW, paddleRect, 40, SCREEN_WIDTH - 40);

        sprites.addSprite(paddle); // vẽ + update
        environment.addCollidable(paddle); // va chạm

        // Tạo và thêm bóng
        List<Velocity> ballVelocities = levelInfo.initialBallVelocities();

        for (int i = 0; i < levelInfo.numberOfBalls(); i++) {
            double ballX = paddleX + paddleWidth / 2;
            double ballY = paddleY - BALL_RADIUS - 1;
            Point ballCenter = new Point(ballX, ballY);

            Velocity velocity = ballVelocities.get(i);

            Ball ball = new Ball(ballCenter, BALL_RADIUS, Color.WHITE, velocity, environment);

            balls.add(ball);

            sprites.addSprite(ball);
        }

        // Tạo và thêm Block
        List<Block> blocks = levelInfo.blocks();

        for (Block block: blocks) {
            sprites.addSprite(block);
            environment.addCollidable(block);

            // tăng điểm khi va chạm
            block.addHitListener(new ScoreTrackingListener(score));

            // Xóa block
            block.addHitListener(new BlockRemove(this, remainingBlocks));
        }

        // Tạo Death Region
        Point deathUpperLeft = new Point(0, SCREEN_HEIGHT);
        Rectangle deathRect = new Rectangle(deathUpperLeft, SCREEN_WIDTH, DEATH_REGION_HEIGHT);

        Block deathBlock = new Block(deathRect, null, 1, true);

        sprites.addSprite(deathBlock);
        environment.addCollidable(deathBlock);

        deathBlock.addHitListener(new BallRemove(this, remainingBalls));

        // Thêm score/ level name indicator
    }

    // Chạy level (vòng lặp chính)
    public void run() {
        initialize(); // Khởi tạo level 1 lần duy nhất

        do {
            playOneTurn();

            if (remainingBalls.getValue() > 0) {
                running = true;
                animationRunner.run(this); // chạy 1 turn
            } else {
                running = false; // Hết bóng thì dừng
            }

            if (remainingBlocks.getValue() <= 0) {
                score.increase(100);
                break;
            }
        } while (needRestart && remainingBalls.getValue() > 0);
    }

    public void playOneTurn() {
        // Xóa bóng cũ
        for (Ball ball: new ArrayList<>(balls)) {
            removeSprite(ball);
        }
        balls.clear();

        // Đặt paddle về giữa
        double centerX = (SCREEN_WIDTH - paddle.getCollisionRectangle().getWidth()) / 2;
        paddle.setX(centerX);

        // Tạo bóng mới với vận tốc 0
        List<Velocity> ballVelocities = levelInfo.initialBallVelocities();
        for (int i = 0; i < levelInfo.numberOfBalls(); i++) {
            double ballX = paddle.getX() + paddle.getWidth() / 2;
            double ballY = paddle.getY() - BALL_RADIUS - 1;
            Point center = new Point(ballX, ballY);

            Ball ball = new Ball(center, BALL_RADIUS, Color.WHITE, new Velocity(0, 0), environment);
            balls.add(ball);
            sprites.addSprite(ball);
        }

        // Reset các cờ trạng thái
        waitingForEnter = true;
        ballsLaunched = false;
        running = true; //  Set running ở đây thay vì trong run()
        needRestart = false; //  Reset needRestart
    }

    @Override
    public void update(double dt) {
        keyboard.update();

        if (!running) return;

        // Xử lý trạng thái chờ bấm Enter
        if (waitingForEnter) {
            if (keyboard.isPressed(Key.ENTER)) {
                waitingForEnter = false;
                ballsLaunched = true;
                // Bắn bóng
                List<Velocity> velocities = levelInfo.initialBallVelocities();
                for (int i = 0; i < balls.size(); i++) {
                    balls.get(i).setVelocity(velocities.get(i));
                }
            }
            return; // Không update sprite khi đang chờ
        }

        if (!ballsLaunched) return;

        sprites.update(dt);

        // paddle movement
        if(keyboard.isPressed(Key.LEFT)) paddle.moveLeft(dt);
        if(keyboard.isPressed(Key.RIGHT)) paddle.moveRight(dt);

        if (remainingBalls.getValue() <= 0) {
            needRestart = true;
            running = false; // dừng animation hiện tại
        }

        if (remainingBlocks.getValue() <= 0) {
            running = false;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        sprites.render(gc);

        //Hiển thị thông báo khi chờ bấm Enter
        if (waitingForEnter) {
            gc.setFill(Color.WHITE);
            gc.fillText("Press ENTER to launch balls", 250, 300);
        }

        // Vẽ các thứ khác
    }

    @Override
    public boolean isFinished() {
        System.out.println("isFinished? running=" + running);
        return !running;
    }

    public void addSprite(Sprite s) {
        if(s != null) sprites.addSprite(s);
    }

    public void addCollidable(Collidable c) {
        if (c != null) environment.addCollidable(c);
    }

    public void removeSprite(Sprite sprite) {
        sprites.removeSprite(sprite);
    }

    public void removeCollidable(Collidable c) {
        environment.removeCollidable(c);
    }
}