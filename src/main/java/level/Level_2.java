package level;

import ball.Velocity;
import collidable.Block;
import game.Sprite;
import geometry.Point;
import geometry.Rectangle;
import javafx.scene.paint.Color;
import powerup.PowerUp;
import powerup.PowerUpType;
import sound.AudioResource;

import java.util.ArrayList;
import java.util.List;

public class Level_2 implements LevelInformation {

    @Override
    public int numberOfBalls() {
        return 30;
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
        double angleStep = (endAngle - startAngle) / numberOfBalls;

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
        return 200;
    }

    @Override
    public String levelName() {
        return "Test Level";
    }

    @Override
    public Sprite getBackground() {
        return new ImageBackground("/Default/level2_bg.jpg");
    }

    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();

        // Chỉ dùng 2 ảnh duy nhất
        final String IMG_1 = "/png/buttonSelected.png";
        final String IMG_2 = "/png/element_blue_rectangle_glossy.png";

        // Quy tắc: xen kẽ theo hàng và cột
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                double x = 50 + col * 70;
                double y = 100 + row * 30;
                Point upperLeft = new Point(x, y);
                Rectangle rect = new Rectangle(upperLeft, 70, 30);

                // Gán ảnh xen kẽ: (row + col) chẵn -> IMG_1, lẻ -> IMG_2
                String imgPath = ((row + col) % 2 == 0) ? IMG_1 : IMG_2;

                // Tạo block với ảnh tùy chỉnh (color = null để không vẽ màu)
                Block block = new Block(rect, Color.BLACK, 1, false, imgPath);

                // Power-up ngẫu nhiên (30% cơ hội)
                if (Math.random() < 0.3) {
                    PowerUpType type = getRandomPowerUpType();
                    Point powerUpPos = new Point(x + 35, y + 15);
                    PowerUp powerUp = new PowerUp(type, powerUpPos, null);
                    block.setPowerUp(powerUp);
                }

                blocks.add(block);
            }
        }

        return blocks;
    }

    @Override
    public String getBackgroundMusic() {
        return AudioResource.BACKGROUND_MUSIC.name();
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