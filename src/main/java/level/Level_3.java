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

public class Level_3 implements LevelInformation {

    @Override
    public int numberOfBalls() {
        return 1;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return 56; // 5x10
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> velocities = new ArrayList<>();
        int numberOfBalls = this.numberOfBalls();
        double speed = 320;

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
        return 170;
    }

    @Override
    public String levelName() {
        return "EERIE";
    }

    @Override
    public Sprite getBackground() {
        return new ImageBackground("/Default/level3_bg.jpg");
    }

    private final double[][][] BLOCK_POSITIONS = {
        {{280,45}, {360,45}, {440,45}, {280,72}, {360,72}, {440,72}, {280,99}, {360,99}, {440,99}},
        {{280,180}, {360,180}, {440,180}, {280,207}, {360,207}, {440,207}, {280,234}, {360,234}, {440,234}},
        {{40,72}, {120,72}, {200,72}, {520,72}, {600,72}, {680,72}},
        {{40,180}, {120,180}, {200,180}, {40,207}, {120,207}, {200,207}, {520,180}, {600,180}, {680,180}, {520,207}, {600,207}, {680,207}},
        {{40,99}, {120,99}, {40,126}, {120,126}, {40,153}, {120,153}, {200,126}, {280,126}, {360,126}, {440,126}, {520,126}, {600,99}, {680,99}, {600,126}, {680,126}, {600,153}, {680,153}}
    };

    private final double[][] PERMANENT_BLOCK_POSITIONS =
        {{40,234}, {120,234}, {280,153}, {360,153},
        {440,153}, {600,234}, {680,234}};
    
    
    private final String PERMANENT_BLOCK_IMAGE = "/Sprite/17-Breakout-Tiles.png";

    private final String[] LAYER_IMAGES = {
            "/Sprite/02-Breakout-Tiles.png",
            "/Sprite/04-Breakout-Tiles.png",
            "/Sprite/06-Breakout-Tiles.png",
            "/Sprite/08-Breakout-Tiles.png",
            "/Sprite/10-Breakout-Tiles.png"
    };

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

                if (Math.random() < 0.1)  {
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
                PowerUpType.EXTRA_BALL,
                PowerUpType.FIRE_BALL,
                PowerUpType.BIG_BALL
        };
        return types[(int)(Math.random() * types.length)];
    }
}