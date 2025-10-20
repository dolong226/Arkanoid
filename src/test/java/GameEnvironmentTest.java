import ball.Ball;
import ball.Velocity;
import collidable.Collidable;
import game.GameEnvironment;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import org.junit.Assert;
import org.junit.Test;

public class GameEnvironmentTest {

    @Test
    public void testNoCollision() {
        GameEnvironment env = new GameEnvironment();
        Line line = new Line(new Point(0, 0), new Point(10, 10));

        Assert.assertNull(env.getClosetCollision(line));
    }

    @Test
    public void testSingleCollision() {
        GameEnvironment env = new GameEnvironment();

        // fake collidable
        Collidable fake = new Collidable() {
            @Override
            public Rectangle getCollisionRectangle() {
                // rectangle tại (5,5) rộng 10, cao 10
                return new Rectangle(new Point(5, 5), 10, 10);
            }

            @Override
            public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
                return null;
            }

        };

        env.addCollidable(fake);
        Line line = new Line(new Point(0, 0), new Point(20, 20));

        Assert.assertNotNull(env.getClosetCollision(line));
    }
}
