package powerup;

import ball.Ball;
import ball.BallType;
import ball.Velocity;
import collidable.Collidable;
import collidable.Paddle;
import game.Counter;
import game.GameEnvironment;
import game.GameLevel;
import game.Sprite;
import geometry.Point;
import geometry.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PowerUp implements Sprite, Collidable {
    private PowerUpType type;
    private Point position; // Vị trí hiện tại
    private Velocity velocity; // Vận tốc rơi xuống
    private double size = 25;
    private boolean collected = false; // đã được thu thập chưa
    private GameEnvironment environment;

    public PowerUp(PowerUpType type, Point position, GameEnvironment environment) {
        this.type = type;
        this.position = position;
        this.velocity = new Velocity(0, 80); // Rơi xuống với tốc độ 80 pixels/s
        this.environment = environment;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.save();

        // Vẽ hình tròn chính
        gc.setFill(type.getColor());
        gc.fillOval(position.getX() - size / 2, position.getY() - size / 2, size, size);

        // Vẽ viền trắng
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeOval(position.getX() - size / 2, position.getY() - size / 2, size, size);

        // Vẽ icon
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 14));
        String icon = type.getIcon();

        // Tính toán để căn giữa text
        double textWidth = icon.length() * 7; // Ước lượng
        gc.fillText(icon, position.getX() - textWidth / 2, position.getY() + 5);

        gc.restore();
    }

    @Override
    public void update(double dt) {
        // Di chuyển xuống dưới
        position = velocity.applyToPoint(position, dt);
    }

    @Override
    public Rectangle getCollisionRectangle() {
        // Tạo hình chữ nhật bao quanh power-up
        Point upperLeft = new Point(position.getX() - size / 2, position.getY() - size / 2);
        return new Rectangle(upperLeft, size, size);
    }

    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        // Power-up không ảnh hưởng đến bóng
        return currentVelocity;
    }

    /**
     * Kiểm tra xem power-up có rơi ra ngoài màn hình không
     */
    public boolean isOutOfBounds() {
        return position.getY() > GameLevel.SCREEN_HEIGHT + size;
    }

    /**
     * Áp dụng hiệu ứng của power-up lên game
     */
    public void applyEffect(GameLevel game) {
        Paddle paddle = game.getPaddle();
        Counter remainingBalls = game.getRemainingBalls();

        GameEnvironment gameEnv = game.getEnvironment();

        switch (type) {
            case EXPAND_PADDLE:
                // Mở rộng paddle 1.5 lần
                paddle.expandWidth(1.5);
                // Tự động reset sau thời gian duration
                if (!type.isPermanent()) {
                    scheduleReset(paddle);
                }
                System.out.println("Expand Paddle");
                break;

            case EXTRA_BALL:
                // Tạo thêm 1 bóng
                if (!game.getBalls().isEmpty()) {
                    Ball existingBall = game.getBalls().get(0);
                    Point center = new Point(
                            existingBall.getCenter().getX(),
                            existingBall.getCenter().getY()
                    );
                    Velocity vel = new Velocity(
                            -existingBall.getVelocity().getDx(),
                            existingBall.getVelocity().getDy()
                    );
                    Ball newBall = new Ball(
                            center,
                            GameLevel.BALL_RADIUS,
                            Color.WHITE,
                            vel,
                            gameEnv
                    );
                    game.addSprite(newBall);
                    game.getBalls().add(newBall);
                    remainingBalls.increase(1);
                    System.out.println("Extra Ball");
                }
                break;

            case SLOW_BALL:
            // Giảm tốc độ bóng
                for (Ball ball: game.getBalls()) {
                    Velocity v = ball.getVelocity();
                    ball.setVelocity(v.getDx() * 0.7, v.getDy() * 0.7);
                }
                System.out.println("SNOW BALL");
                break;

            case MULTI_BALL:
                // Tạo thêm 2 bóng
                if (!game.getBalls().isEmpty()) {
                    Ball original = game.getBalls().get(0);
                    double speed = Math.sqrt(
                            original.getVelocity().getDx() * original.getVelocity().getDx() +
                                    original.getVelocity().getDy() * original.getVelocity().getDy()
                    );

                    // Bóng 1: góc -30 độ
                    Point center1 = new Point(
                            original.getCenter().getX(),
                            original.getCenter().getY()
                    );
                    Velocity vel1 = Velocity.fromAngleAndSpeed(-30, speed);
                    Ball ball1 = new Ball(
                            center1,
                            GameLevel.BALL_RADIUS,
                            Color.WHITE,
                            vel1,
                            gameEnv
                    );

                    // Bóng 2: góc 30 độ
                    Point center2 = new Point(
                            original.getCenter().getX(),
                            original.getCenter().getY()
                    );
                    Velocity vel2 = Velocity.fromAngleAndSpeed(30, speed);
                    Ball ball2 = new Ball(
                            center2,
                            GameLevel.BALL_RADIUS,
                            Color.WHITE,
                            vel2,
                            environment
                    );

                    game.addSprite(ball1);
                    game.addSprite(ball2);
                    game.getBalls().add(ball1);
                    game.getBalls().add(ball2);
                    remainingBalls.increase(2);
                    System.out.println("Multi ball");
                }
                break;

            case FIRE_BALL:
                for (Ball ball: game.getBalls()) {
                    ball.setType(BallType.FIRE);
                }

                scheduleResetBallType(game.getBalls(), type.getDuration());
                break;

            case BIG_BALL:
                for (Ball ball : game.getBalls()) {
                    ball.setType(BallType.BIG);
                }
                scheduleResetBallType(game.getBalls(), type.getDuration());
                break;
        }
    }

    /**
     * Reset paddle về kích thước ban đầu
     */
    private void scheduleReset(Paddle paddle) {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        paddle.resetWidth();
                        System.out.println("Reset paddle");
                    }
                },
                (long)(type.getDuration() * 1000)
        );
    }

    private void scheduleResetBallType(List<Ball> balls, double seconds) {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        for (Ball ball: balls) {
                            ball.setType(BallType.NORMAL);
                        }
                    }
                },
                (long) (seconds * 1000)
        );
    }
    public boolean isCollected() {
        return collected;
    }

    public PowerUpType getType() {
        return type;
    }
}