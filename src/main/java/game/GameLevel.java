
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
import input.GameMouse;
import input.Key;
import input.Keyboard;
import input.PlayerInput;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private PlayerInput input;

    private boolean waitingForEnter = true;
    private boolean ballsLaunched = false;

    // THÊM BIẾN NÀY
    private List<Velocity> initialBallVelocities;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int PADDLE_HEIGHT = 20;
    public static final int BALL_RADIUS = 6;
    public static final int DEATH_REGION_HEIGHT = 50;

    public GameLevel(LevelInformation levelInfo, PlayerInput input, AnimationRunner animationRunner) {
        this.levelInfo = levelInfo;
        this.input = input;
        this.keyboard = input.getKeyboard();
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
        Rectangle paddleRect = new Rectangle(new Point(paddleX, paddleY), PADDLE_HEIGHT, paddleWidth);
        // boundary cho paddle sát tường
        paddle = new Paddle((int) paddleSpeed, Color.YELLOW, paddleRect, 20, SCREEN_WIDTH - 20);

        sprites.addSprite(paddle);
        environment.addCollidable(paddle);

        // Tạo với vận tốc 0, lưu vận tốc thật vào biến
        this.initialBallVelocities = levelInfo.initialBallVelocities();
        for (int i = 0; i < levelInfo.numberOfBalls(); i++) {
            double ballX = paddleX + paddleWidth / 2;
            double ballY = paddleY - BALL_RADIUS - 1;
            Point ballCenter = new Point(ballX, ballY);

            // TẠO BÓNG VỚI VẬN TỐC = 0
            Ball ball = new Ball(ballCenter, BALL_RADIUS, Color.WHITE, new Velocity(0, 0), environment);

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
        Rectangle deathRect = new Rectangle(deathUpperLeft, DEATH_REGION_HEIGHT, SCREEN_WIDTH);
        Block deathBlock = new Block(deathRect, null, 1, true);
        sprites.addSprite(deathBlock);
        environment.addCollidable(deathBlock);
        deathBlock.addHitListener(new BallRemove(this, remainingBalls));

        // Tường trên: cao 20px, rộng SCREEN_WIDTH
        Point topLeft = new Point(0, 0);
        Rectangle topRect = new Rectangle(topLeft, 20, SCREEN_WIDTH);
        Block topWall = new Block(topRect, Color.GRAY, Integer.MAX_VALUE, false);
        sprites.addSprite(topWall);
        environment.addCollidable(topWall);

        // Tường trái: rộng 20px, cao SCREEN_HEIGHT
        Point leftTop = new Point(0, 0);
        Rectangle leftRect = new Rectangle(leftTop, SCREEN_HEIGHT, 20);
        Block leftWall = new Block(leftRect, Color.GRAY, Integer.MAX_VALUE, false);
        sprites.addSprite(leftWall);
        environment.addCollidable(leftWall);

        // Tường phải: rộng 20px, cao SCREEN_HEIGHT
        Point rightTop = new Point(SCREEN_WIDTH - 20, 0);
        Rectangle rightRect = new Rectangle(rightTop, SCREEN_HEIGHT, 20);
        Block rightWall = new Block(rightRect, Color.GRAY, Integer.MAX_VALUE, false);
        sprites.addSprite(rightWall);
        environment.addCollidable(rightWall);

        running = true;
        // Đặt waitingForEnter = true
        waitingForEnter = true;
        ballsLaunched = false;
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

        ((GameMouse) input.getMouse()).update();
        if (!running) return;

        // Xử lý chờ Enter
        if (waitingForEnter) {

            if(keyboard.isPressed(Key.LEFT)) paddle.moveLeft(dt);
            if(keyboard.isPressed(Key.RIGHT)) paddle.moveRight(dt);

            // Cập nhật vị trí bóng theo paddle (dính vào paddle)
            double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
            for (Ball ball : balls) {
                ball.getCenter().setX(paddleCenterX);
                ball.getCenter().setY(paddle.getY() - BALL_RADIUS - 1);
            }

            // Kiểm tra nhấn Enter để bắn bóng
            if (keyboard.isPressed(Key.ENTER)) {
                for (int i = 0; i < balls.size(); i++) {
                    balls.get(i).setVelocity(initialBallVelocities.get(i));
                    System.out.println("Ball " + i + " velocity: " + initialBallVelocities.get(i).getDx() + ", " + initialBallVelocities.get(i).getDy());
                }
                waitingForEnter = false;
                ballsLaunched = true;
            }

            return; // Không update sprites khác
        }

        sprites.update(dt);

        // chuyển động paddle
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

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 15));
        gc.fillText("Score: " + score.getValue(), 10, 20);
        gc.fillText("Balls: " + remainingBalls.getValue(), 10, 40);
        gc.fillText("Blocks: " + remainingBlocks.getValue(), 10, 60);

        //  Hiển thị thông báo chờ Enter
        if (waitingForEnter) {
            gc.setFont(new javafx.scene.text.Font(30));
            gc.setFill(Color.YELLOW);
            gc.fillText("Press ENTER to Launch!", SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2);
        }
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