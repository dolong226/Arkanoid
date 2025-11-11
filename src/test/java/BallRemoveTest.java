import ball.Ball;
import ball.Velocity;
import collidable.Block;
import geometry.Point;
import geometry.Rectangle;
import game.Counter;
import game.GameEnvironment;
import listener.BallRemove;
import listener.HitEvent;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BallRemoveTest {

    private static class MiniGameLevel {
        private List<Ball> sprites = new ArrayList<>();

        public void addSprite(Ball b) { sprites.add(b); }
        public void removeSprite(Ball b) { sprites.remove(b); }
        public List<Ball> getSprites() { return sprites; }
    }

    @Test
    public void testBallHitDeathRegion() {
        MiniGameLevel game = new MiniGameLevel();
        Counter remainingBalls = new Counter(1);

        BallRemove listener = new BallRemove(null, remainingBalls) {
            @Override
            public void hitEvent(HitEvent event) {
                if (((Block) event.getHitObject()).isDeathRegion()) {
                    game.removeSprite(event.getHitter());
                    remainingBalls.decrease(1);
                }
            }
        };

        Ball ball = new Ball(new Point(0,0), 5, null, new Velocity(0,0), new GameEnvironment());
        game.addSprite(ball);

        Block deathBlock = new Block(
                new Rectangle(new Point(0,0), 10, 10),
                Color.BLACK,
                1,
                true,   // death region
                "death"
        );

        HitEvent event = new HitEvent(ball, deathBlock, new Point(5, 5));
        listener.hitEvent(event);

        Assert.assertEquals("Counter should decrease", 0, remainingBalls.getValue());
        Assert.assertFalse("Ball should be removed from sprites", game.getSprites().contains(ball));
    }

    @Test
    public void testBallHitNormalBlock() {
        MiniGameLevel game = new MiniGameLevel();
        Counter remainingBalls = new Counter(1);

        BallRemove listener = new BallRemove(null, remainingBalls) {
            @Override
            public void hitEvent(HitEvent event) {
                if (((Block) event.getHitObject()).isDeathRegion()) {
                    game.removeSprite(event.getHitter());
                    remainingBalls.decrease(1);
                }
            }
        };

        Ball ball = new Ball(new Point(0,0), 5, null, new Velocity(0,0), new GameEnvironment());
        game.addSprite(ball);

        Block normalBlock = new Block(
                new Rectangle(new Point(0,0), 10, 10),
                Color.BLACK,
                1,
                false,  // không phải death region
                "normal"
        );

        HitEvent event = new HitEvent(ball, normalBlock, new Point(5, 5));
        listener.hitEvent(event);

        Assert.assertEquals("Counter should not change", 1, remainingBalls.getValue());
        Assert.assertTrue("Ball should still be in sprites", game.getSprites().contains(ball));
    }
}
