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
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import ui.ImageLoad;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PowerUp implements Sprite, Collidable {
    private PowerUpType type;
    private Point position; // Vị trí hiện tại
    private Velocity velocity; // Vận tốc rơi xuống
    private double size = 50;
    private boolean collected = false; // đã được thu thập chưa
    private GameEnvironment environment;
    private Image cachedImage;

    public PowerUp(PowerUpType type, Point position, GameEnvironment environment) {
        this.type = type;
        this.position = position;
        this.velocity = new Velocity(0, 50); // Rơi xuống với tốc độ 80 pixels/s
        this.environment = environment;
        loadImage();
    }

    /**
     * Load ảnh từ file, còn nếu không có thì dùng fallback là vẽ hình tròn
     */
    private void loadImage() {
        try {
            cachedImage = ImageLoad.load(type.getImagePath());
        } catch (Exception e) {
            System.err.println("Không thể load ảnh power-up: " + type.getImagePath());
            cachedImage = null; // Sẽ dùng fallback rendering
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.save();

        if(cachedImage != null) {
            gc.drawImage(cachedImage, position.getX() - size/2, position.getY() - size/2, size, size);
        } else {
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
        }
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
                    Velocity oriVel =  original.getVelocity();
                    double dx = oriVel.getDx();
                    double dy = oriVel.getDy();
                    double speed = Math.hypot(dx, dy);
                    if (speed == 0) {
                        speed = 200;
                        dx = speed;
                        dy = 0;
                    }

                    double  norm = Math.hypot(dx, dy);
                    double px = -dy / norm;
                    double py = dx/norm;
                    double offset = GameLevel.BALL_RADIUS * 2.0;

                    Point center1 = new Point(original.getCenter().getX() + px * offset,
                            original.getCenter().getY() + py * offset);

                    Point center2 = new Point( original.getCenter().getX()- px * offset,
                            original.getCenter().getY() - py * offset);

                    // tránh spawn trong death region
                    double safeY = GameLevel.SCREEN_HEIGHT - GameLevel.DEATH_REGION_HEIGHT - GameLevel.BALL_RADIUS - 1;
                    if (center1.getY() > safeY) {
                        center1 = new Point(original.getCenter().getX() - px * offset,
                                original.getCenter().getY() - py * offset);
                        if (center1.getY() > safeY) center1.setY(safeY);
                    }
                    if (center2.getY() > safeY) {
                        center2 = new Point(original.getCenter().getX() + px * offset,
                                original.getCenter().getY() + py * offset);
                        if (center2.getY() > safeY) center2.setY(safeY);
                    }

                    double baseAngle = Math.toDegrees(Math.atan2(dy, dx));

                    // Tạo hai vận tốc lệch 30/-30 độ so với góc gốc
                    Velocity vel1 = Velocity.fromAngleAndSpeed(baseAngle, speed);
                    Velocity vel2 = Velocity.fromAngleAndSpeed(baseAngle, speed);

                    Ball ball1 = new Ball (center1, GameLevel.BALL_RADIUS, Color.WHITE, vel1, gameEnv);
                    Ball ball2 = new Ball (center2, GameLevel.BALL_RADIUS, Color.WHITE, vel2, gameEnv);

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