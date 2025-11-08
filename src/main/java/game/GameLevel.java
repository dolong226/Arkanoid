
package game;

import animation.Animation;
import animation.AnimationRunner;
import ball.Ball;
import ball.Velocity;
import collidable.Block;
import collidable.Collidable;
import collidable.Paddle;
import data.HighScoreTable;
import geometry.Point;
import geometry.Rectangle;
import input.GameMouse;
import input.Key;
import input.Keyboard;
import input.PlayerInput;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import level.LevelInformation;
import listener.BallRemove;
import listener.BlockRemove;
import listener.ScoreTrackingListener;
import listener.SoundHitListener;
import powerup.PowerUp;
import  ui.GameInfoPanel;

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
    private Runnable onLevelComplete;
    private Runnable onGameOver;
    private Runnable pendingCallback = null;

    private boolean waitingForEnter = true;
    private boolean ballsLaunched = false;

    private List<Velocity> initialBallVelocities;
    private List<PowerUp> activePowerUps = new ArrayList<>();

    private GameInfoPanel infoPanel;
    private HighScoreTable highScoreTable;
    private GameController gameController;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int PANEL_WIDTH = 180;
    public static final int TOTAL_WIDTH = SCREEN_WIDTH + PANEL_WIDTH;
    public static final int PADDLE_HEIGHT = 17;
    public static final int BALL_RADIUS = 7;
    public static final int DEATH_REGION_HEIGHT = 50;

    public GameLevel(LevelInformation levelInfo, PlayerInput input, AnimationRunner animationRunner, GameController gameController) {
        this.levelInfo = levelInfo;
        this.input = input;
        this.keyboard = input.getKeyboard();
        this.animationRunner = animationRunner;
        this.gameController = gameController;
    }

    public void setHighScoreTable(HighScoreTable highScoreTable) {
        this.highScoreTable = highScoreTable;
    }

    public GameEnvironment getEnvironment() {
        return environment;
    }

    public void setOnLevelComplete(Runnable callback) {
        this.onLevelComplete = callback;
    }
    public void setOnGameOver(Runnable callback) {
        this.onGameOver = callback;
    }
    public Counter getScore() { return score; }
    public int getRemainingBlocks() { return remainingBlocks.getValue(); }
    public Counter getRemainingBalls() { return remainingBalls; }
    public List<Ball> getBalls() { return balls; }
    public Paddle getPaddle() { return paddle; }


    public void initialize() {
        sprites = new SpriteCollection();
        environment = new GameEnvironment();
        score = new Counter(0);
        remainingBalls = new Counter(levelInfo.numberOfBalls());
        remainingBlocks = new Counter(levelInfo.numberOfBlocksToRemove());
        activePowerUps.clear();
        final int WALL_THICKNESS = 20;

        // Khởi tạo Info Panel
        infoPanel = new GameInfoPanel(SCREEN_WIDTH + 10, 10, PANEL_WIDTH - 20, SCREEN_HEIGHT - 20);
        infoPanel.setLevelName(levelInfo.levelName());
        infoPanel.setCurrentScore(score);
        infoPanel.setHighScoreTable(highScoreTable);
        infoPanel.setRemainingBalls(remainingBalls);
        infoPanel.setRemainingBlocks(remainingBlocks);

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
        // boundary cho paddle sát tường
        paddle = new Paddle((int) paddleSpeed, Color.YELLOW, paddleRect, WALL_THICKNESS , SCREEN_WIDTH - WALL_THICKNESS);
        SoundHitListener soundListener = new SoundHitListener(gameController);
        paddle.addHitListener(soundListener);

        sprites.addSprite(paddle);
        environment.addCollidable(paddle);

        // Tạo với vận tốc 0, lưu vận tốc thật vào biến
        this.initialBallVelocities = levelInfo.initialBallVelocities();
        for (int i = 0; i < levelInfo.numberOfBalls(); i++) {
            double ballX = paddleX + paddleWidth / 2;
            double ballY = paddleY - BALL_RADIUS - 1;
            Point ballCenter = new Point(ballX, ballY);

            // tạo bóng với v = 0
            Ball ball = new Ball(ballCenter, BALL_RADIUS, Color.WHITE, new Velocity(0, 0), environment);

            balls.add(ball);
            sprites.addSprite(ball);
        }

        // Blocks
        List<Block> blocks = levelInfo.blocks();

        for (Block block: blocks) {
            if (!block.isDeathRegion()) {
                sprites.addSprite(block);
                environment.addCollidable(block);
                block.addHitListener(new ScoreTrackingListener(score));
                block.addHitListener(new BlockRemove(this, remainingBlocks));
                block.addHitListener(soundListener);
            }
        }

        // Death Region
        Point deathUpperLeft = new Point(0, SCREEN_HEIGHT);
        Rectangle deathRect = new Rectangle(deathUpperLeft, SCREEN_WIDTH, DEATH_REGION_HEIGHT);
        Block deathBlock = new Block(deathRect, null, 1, true);
        sprites.addSprite(deathBlock);
        environment.addCollidable(deathBlock);
        deathBlock.addHitListener(new BallRemove(this, remainingBalls));
        deathBlock.addHitListener(soundListener);

        // Tường trên: cao 20px, rộng SCREEN_WIDTH
        Point topLeft = new Point(0, 0);
        Rectangle topRect = new Rectangle(topLeft, SCREEN_WIDTH, WALL_THICKNESS + 10) ;
        Block topWall = new Block(topRect, null, Integer.MAX_VALUE, true);
        sprites.addSprite(topWall);
        environment.addCollidable(topWall);
        topWall.addHitListener(soundListener);

        // Tường trái: rộng 20px, cao SCREEN_HEIGHT
        Point leftTop = new Point(0, 0);
        Rectangle leftRect = new Rectangle(leftTop, WALL_THICKNESS + 10, SCREEN_HEIGHT);
        Block leftWall = new Block(leftRect, null, Integer.MAX_VALUE, true);
        sprites.addSprite(leftWall);
        environment.addCollidable(leftWall);
        leftWall.addHitListener(soundListener);

        // Tường phải: rộng 20px, cao SCREEN_HEIGHT
        Point rightTop = new Point(SCREEN_WIDTH - WALL_THICKNESS - 10, 0);
        Rectangle rightRect = new Rectangle(rightTop, WALL_THICKNESS, SCREEN_HEIGHT);
        Block rightWall = new Block(rightRect, null, Integer.MAX_VALUE, true);
        sprites.addSprite(rightWall);
        environment.addCollidable(rightWall);
        rightWall.addHitListener(soundListener);

        running = true;
        // chờ enter
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

            return;
        }

        sprites.update(dt);

        // chuyển động paddle
        if(keyboard.isPressed(Key.LEFT)) paddle.moveLeft(dt);
        if(keyboard.isPressed(Key.RIGHT)) paddle.moveRight(dt);

        // Thêm xử lí power up
        List<PowerUp> toRemove = new ArrayList<>();
        Rectangle paddleRect = paddle.getCollisionRectangle();

        for (PowerUp powerUp: activePowerUps) {
            Rectangle powerUpRect = powerUp.getCollisionRectangle();

            // Kiểm tra va chạm với paddle
            if (paddleRect.intersects(powerUpRect)) {
                powerUp.applyEffect(this);
                gameController.onPowerUpCollected();
                toRemove.add(powerUp);
                removeSprite(powerUp);
                removeCollidable(powerUp);
            }

            // Kiểm tra rơi ra ngoài
            else if (powerUp.isOutOfBounds()) {
                toRemove.add(powerUp);
                removeSprite(powerUp);
                removeCollidable(powerUp);
            }
        }
        activePowerUps.removeAll(toRemove);

        // Kiểm tra điều kiện kết thúc
        if (remainingBalls.getValue() <= 0) {
            System.out.println("Hết bóng!");
            running = false;
            pendingCallback = onGameOver;
            return;
        }

        if (remainingBlocks.getValue() <= 0) {
            System.out.println("Phá hết block!");
            score.increase(100);
            running = false;
            pendingCallback = onLevelComplete;
            return;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // Vẽ background toàn màn hình (bao gồm cả panel)
        gc.setFill(Color.rgb(20, 20, 30));
        gc.fillRect(0, 0, TOTAL_WIDTH, SCREEN_HEIGHT);

        if (sprites != null) {
            sprites.render(gc);
        }

        // Vẽ Info Panel bên phải
        if (infoPanel != null) {
            infoPanel.render(gc);
        }

        //  Hiển thị thông báo chờ Enter
        if (waitingForEnter && sprites != null) {
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            gc.setFill(Color.rgb(147, 88, 94));
            gc.fillText("PRESS ENTER TO LAUNCH!", SCREEN_WIDTH / 2 - 195,
                    SCREEN_HEIGHT / 2 + 100);
        }
    }

    @Override
    public boolean isFinished() {
        if (!running && pendingCallback != null) {
            Runnable callback = pendingCallback;
            pendingCallback = null;

            callback.run();
        }
        return !running;
    }

    public void addSprite(Sprite s) {
        if(s != null) sprites.addSprite(s);
    }

    public void addCollidable(Collidable c) {
        if (c != null) {
            environment.addCollidable(c);

            if (c instanceof PowerUp) {
                activePowerUps.add((PowerUp) c);
            }
        }
    }

    public void removeSprite(Sprite sprite) {
        sprites.removeSprite(sprite);
    }

    public void removeCollidable(Collidable c) {
        environment.removeCollidable(c);
    }
}