package level;

import ball.Velocity;
import collidable.Block;
import game.Sprite;
import geometry.Point;
import geometry.Rectangle;
import javafx.scene.paint.Color;
import powerup.PowerUp;
import powerup.PowerUpType;

import java.util.ArrayList;
import java.util.List;

public class LevelTest implements LevelInformation {

    @Override
    public int numberOfBalls() {
        return 5;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return 50; // 5x10
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> velocities = new ArrayList<>();
        int numberOfBalls = this.numberOfBalls();
        double speed = 350;

        // Góc bắt đầu từ -60 đến +60, chia đều cho 20 bóng
        double startAngle = -60;
        double endAngle = 60;
        double angleStep = (endAngle - startAngle) / (numberOfBalls - 1);

        for (int i = 0; i < numberOfBalls; i++) {
            double angle = startAngle + i * angleStep;
            velocities.add(Velocity.fromAngleAndSpeed(angle, speed));
        }
        return velocities;
    }

    @Override
    public double paddleSpeed() {
        return 300;
    }

    @Override
    public double paddleWidth() {
        return 300;
    }

    @Override
    public String levelName() {
        return "Test Level";
    }

    @Override
    public Sprite getBackground() {
        return new GradientBackground();
    }

    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();

        // Tạo 5 hàng x 10 cột blocks
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                double x = 50 + col * 70;
                double y = 100 + row * 30;
                Point upperLeft = new Point(x, y);
                Rectangle rect = new Rectangle(upperLeft, 70, 30);

                // Random màu
                Color color = getRandomColor();
                Block block = new Block(rect, color, 1, false);

                if (Math.random() < 0.3) {
                    PowerUpType type = getRandomPowerUpType();
                    Point powerUpPos = new Point(x + 35, y + 15); // Giữa block
                    PowerUp powerUp = new PowerUp(type, powerUpPos, null);
                    block.setPowerUp(powerUp);
                }

                blocks.add(block);
            }
        }

        return blocks;
    }


    private Color getRandomColor() {
        Color[] colors = {
                Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
                Color.ORANGE, Color.PURPLE, Color.PINK, Color.CYAN
        };
        return colors[(int)(Math.random() * colors.length)];
    }

    private PowerUpType getRandomPowerUpType() {
        PowerUpType[] types = {
                PowerUpType.EXPAND_PADDLE,
                PowerUpType.EXTRA_BALL,
                PowerUpType.FIRE_BALL,
                PowerUpType.BIG_BALL
        };
        return types[(int)(Math.random() * types.length)];
    }
}