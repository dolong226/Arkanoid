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

    private boolean waitingForEnter = true;
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

    public Counter getScore() { return score; }
    public int getRemainingBlocks() { return remainingBlocks.getValue(); }
    public int getRemainingBalls() { return remainingBalls.getValue(); }
    public List<Ball> getBalls() { return balls; }
    public Paddle getPaddle() { return paddle; }

    public void initialize() {
        sprites = new SpriteCollection();
        environment = new GameEnvironment();
        score = new Counter(0);
        remainingBalls = new Counter(levelInfo.numberOfBalls());
        remainingBlocks = new Counter(levelInfo.numberOfBlocksToRemove());

        // Background
        Sprite background = levelInfo.getBackground();
        if (background != null) {
            sprites.addSprite(background);
        }

        // Paddle
        double paddleWidth = levelInfo.paddleWidth();
        double paddleSpeed = levelInfo.paddleSpeed();
        double paddleX = (SCREEN_WIDTH - paddleWidth)/2;
        double paddleY = SCREEN_HEIGHT - PADDLE_HEIGHT - 10;
        Rectangle paddleRect = new Rectangle(new Point(paddleX, paddleY), paddleWidth, PADDLE_HEIGHT);
        paddle = new Paddle((int) paddleSpeed, Color.YELLOW, paddleRect, 40, SCREEN_WIDTH - 40);

        sprites.addSprite(paddle);
        environment.addCollidable(paddle);

        // Balls (ban đầu vận tốc = 0, chờ Enter)
        List<Velocity> ballVelocities = levelInfo.initialBallVelocities();
        for (int i = 0; i < levelInfo.numberOfBalls(); i++) {
            double ballX = paddleX + paddleWidth / 2;
            double ballY = paddleY - BALL_RADIUS - 1;
            Point ballCenter = new Point(ballX, ballY);

            Ball ball = new Ball(ballCenter, BALL_RADIUS, Color.WHITE, ballVelocities.get(i), environment);

            balls.add(ball);
            sprites.addSprite(ball);
        }

        // Blocks
        List<Block> blocks = levelInfo.blocks();
        for (Block block: blocks) {
            sprites.addSprite(block);
            environment.addCollidable(block);
            block.addHitListener(new ScoreTrackingListener(score));
            block.addHitListener(new BlockRemove(this, remainingBlocks));
        }

        // Death Region
        Point deathUpperLeft = new Point(0, SCREEN_HEIGHT);
        Rectangle deathRect = new Rectangle(deathUpperLeft, SCREEN_WIDTH, DEATH_REGION_HEIGHT);
        Block deathBlock = new Block(deathRect, null, 1, true);
        sprites.addSprite(deathBlock);
        environment.addCollidable(deathBlock);
        deathBlock.addHitListener(new BallRemove(this, remainingBalls));

        running = true;
        waitingForEnter = false;
        ballsLaunched = true;
    }

    public void run() {
        initialize();
        animationRunner.run(this);
    }

    public void playOneTurn() {
        // Reset logic
        for (Ball ball: new ArrayList<>(balls)) {
            removeSprite(ball);
        }
        balls.clear();

        double centerX = (SCREEN_WIDTH - paddle.getCollisionRectangle().getWidth()) / 2;
        paddle.setX(centerX);

        List<Velocity> ballVelocities = levelInfo.initialBallVelocities();
        for (int i = 0; i < levelInfo.numberOfBalls(); i++) {
            double ballX = paddle.getX() + paddle.getWidth() / 2;
            double ballY = paddle.getY() - BALL_RADIUS - 1;
            Point center = new Point(ballX, ballY);

            Ball ball = new Ball(center, BALL_RADIUS, Color.WHITE, ballVelocities.get(i), environment);
            balls.add(ball);
            sprites.addSprite(ball);
        }

        running = true;
    }

    @Override
    public void update(double dt) {
        keyboard.update();

        if (!running) return;

        sprites.update(dt);

        // Paddle movement
        if(keyboard.isPressed(Key.LEFT)) paddle.moveLeft(dt);
        if(keyboard.isPressed(Key.RIGHT)) paddle.moveRight(dt);

        // Kiểm tra điều kiện kết thúc
        if (remainingBalls.getValue() <= 0) {
            System.out.println("Hết bóng!");
            running = false;
            return;
        }

        if (remainingBlocks.getValue() <= 0) {
            System.out.println("Phá hết block!");
            score.increase(100);
            running = false;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        sprites.render(gc);

        // Debug info
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score.getValue(), 10, 20);
        gc.fillText("Balls: " + remainingBalls.getValue(), 10, 40);
        gc.fillText("Blocks: " + remainingBlocks.getValue(), 10, 60);
    }

    @Override
    public boolean isFinished() {
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