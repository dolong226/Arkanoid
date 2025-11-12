package level;

import ball.Velocity;
import collidable.Block;
import game.Sprite;
import geometry.Point;
import geometry.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import powerup.PowerUp;
import powerup.PowerUpType;
import sound.AudioResource;
import ui.ImageLoad;

import java.util.ArrayList;
import java.util.List;

public class Level_2 implements LevelInformation {

    @Override
    public int numberOfBalls() {
        return 1;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return 1;//50;
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> velocities = new ArrayList<>();
        int numberOfBalls = this.numberOfBalls();
        double speed = 200;

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
        return 200;
    }

    @Override
    public double paddleWidth() {
        return 170;
    }

    @Override
    public String levelName() {
        return "EERIE";
    }

    @Override
    public Sprite getBackground() {
        return new ImageBackground("/Default/level2_bg.jpg");
    }

    private final double[][][] BLOCK_POSITIONS = {
            {{40,45}, {40,72}, {40,99}, {40,126}, {40,153}, {40,180}, {40,207}, {40,234}},
            {{120,45}, {120,72}, {120,99}, {120,126}, {120,153}, {120,180}, {120,207}, {120,234}},
            {{280,45}, {280,72}, {280,99}, {280,126}, {280,153}, {280,180}},
            {{360,45}, {360,72}, {360,99}, {360,126}, {360,153}, {360,180}},
            {{440,45}, {440,72}, {440,99}, {440,126}, {440,153}, {440,180}},
            {{600,45}, {600,72}, {600,99}, {600,126}, {600,153}, {600,180}, {600,207}, {600,234}},
            {{680,45}, {680,72}, {680,99}, {680,126}, {680,153}, {680,180}, {680,207}, {680,234}}
    };

    private final double[][] PERMANENT_BLOCK_POSITIONS =
        {{200,207}, {280,207}, {360,207}, {440,207}, {520,207},
        {200,234}, {280,234}, {360,234}, {440,234}, {520,234}};

    private final String[] LAYER_IMAGES = {
            "/Sprite/02-Breakout-Tiles.png",
            "/Sprite/04-Breakout-Tiles.png",
            "/Sprite/06-Breakout-Tiles.png",
            "/Sprite/08-Breakout-Tiles.png",
            "/Sprite/10-Breakout-Tiles.png",
            "/Sprite/16-Breakout-Tiles.png",
            "/Sprite/18-Breakout-Tiles.png",
    };

    private final String PERMANENT_BLOCK_IMAGE = "/Sprite/17-Breakout-Tiles.png";

    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();
        final double WIDTH = 80;
        final double HEIGHT = 27;

         for (double[] pos : PERMANENT_BLOCK_POSITIONS) {
            double x = pos[0];
            double y = pos[1];
            Point upperLeft = new Point(x, y);
            Rectangle rect = new Rectangle(upperLeft, WIDTH, HEIGHT);
            Block permanentBlock = new Block(rect, Color.GRAY, 10000, false, PERMANENT_BLOCK_IMAGE);
            blocks.add(permanentBlock);
         }

         for (int layer = 0; layer < BLOCK_POSITIONS.length; layer++) {
            String imgPath = LAYER_IMAGES[layer];
            Image blockImage = ImageLoad.load(imgPath);

            for (double[] pos : BLOCK_POSITIONS[layer]) {
                double x = pos[0];
                double y = pos[1];
                Point upperLeft = new Point(x, y);
                Rectangle rect = new Rectangle(upperLeft, WIDTH, HEIGHT);

                Block block = new Block(rect, Color.RED, 1, false, imgPath);

                if (Math.random() < 0.15)  {
                    PowerUpType type = getRandomPowerUpType();
                    Point center = new Point(x + WIDTH/2, y + HEIGHT/2);
                    PowerUp powerUp = new PowerUp(type, center, null);
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
                PowerUpType.BIG_BALL,
                PowerUpType.MULTI_BALL,
                PowerUpType.SLOW_BALL
        };
        return types[(int)(Math.random() * types.length)];
    }
}