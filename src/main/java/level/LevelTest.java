package level;

import ball.Velocity;
import collidable.Block;
import collidable.Paddle;
import geometry.Point;
import geometry.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import level.LevelInformation;
import game.Sprite;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Level đơn giản để test game: 1 hàng 10 block, 1 bóng, paddle nhanh.
 */
public class LevelTest implements LevelInformation {

    @Override
    public int numberOfBalls() {
        return 1;
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> velocities = new ArrayList<>();
        velocities.add(Velocity.fromAngleAndSpeed(0, 500)); // bắn thẳng lên, tốc độ 300
        return velocities;
    }

    @Override
    public double paddleSpeed() {
        return 500; // paddle di chuyển nhanh
    }

    @Override
    public double paddleWidth() {
        return 180;
    }

    @Override
    public String levelName() {
        return "Test Level";
    }

    @Override
    public Sprite getBackground() {
        return new Sprite() {
            @Override
            public void render(GraphicsContext gc) {
                gc.save();
                gc.setFill(Color.rgb(0, 100, 200));
                gc.fillRect(0, 0, 800, 600);
                gc.restore();
            }

            @Override
            public void update(double dt) {
                // không cần cập nhật gì
            }
        };
    }

    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();
        int blockWidth = 50;
        int blockHeight = 20;
        int startX = 65;
        int startY = 150;

        for (int i = 0; i < 10; i++) {
            double x = startX + i * (blockWidth + 5);
            Rectangle rect = new Rectangle(new Point(x, startY), blockHeight, blockWidth);
            Block block = new Block(rect, Color.RED);
            blocks.add(block);
        }
        return blocks;
    }


    @Override
    public int numberOfBlocksToRemove() {
        return blocks().size();
    }
}