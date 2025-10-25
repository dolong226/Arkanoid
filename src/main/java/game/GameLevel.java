package game;

import animation.Animation;
import animation.AnimationRunner;
import ball.Ball;
import collidable.Collidable;
import collidable.Paddle;
import geometry.Point;
import input.Keyboard;
import javafx.scene.canvas.GraphicsContext;
import level.LevelInformation;
import listener.BallRemove;
import listener.BlockRemove;
import listener.ScoreTrackingListener;

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
    private List<Ball> balls;
    private Counter remFainingBlocks;
    private Counter remainingBalls;
    private Keyboard keyboard;
    private boolean running;

    public GameLevel(LevelInformation levelInfo, Keyboard keyboard, AnimationRunner animationRunner) {
        this.levelInfo = levelInfo;
        this.keyboard = keyboard;
        this.animationRunner = animationRunner;
    }

    /**
     * Khởi tạo mọi thứ trước khi level bắt đầu.
     */
    public void initialize() {

    }

    public void run() {

    }

    public void playOneTurn() {

    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void render(GraphicsContext gc) {}

    @Override
    public boolean isFinished() {
        return false;
    }

    void addSprite(Sprite s) {

    }

    void addCollidable(Collidable c) {

    }
}