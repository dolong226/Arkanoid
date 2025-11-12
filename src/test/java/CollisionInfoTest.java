import org.junit.Test;
import static org.junit.Assert.*;
import collidable.CollisionInfo;
import collidable.Collidable;
import geometry.Point;
import ball.Ball;
import ball.Velocity;
import geometry.Rectangle;

public class CollisionInfoTest {

    /**
     * class MockCollidable phục vụ việc test.
     */
    private class MockCollidable implements Collidable {
        @Override
        public Rectangle getCollisionRectangle() {
            return null;
        }
        @Override
        public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
            return null;
        }
    }

    /**
     * Test hàm khởi tạo rỗng.
     */
    @Test
    public void testEmptyConstructor() {
        CollisionInfo info = new CollisionInfo();
        assertNull(info.getClosetPoint());
        assertNull(info.getCollidable());
    }

    /**
     * Test hàm khởi tạo đầy đủ và các hàm getter.
     */
    @Test
    public void testFullConstructorAndGetters() {
        Point testPoint = new Point(100, 150);
        Collidable testCollidable = new MockCollidable();
        CollisionInfo info = new CollisionInfo(testPoint, testCollidable);
        assertSame(testPoint, info.getClosetPoint());
        assertSame(testCollidable, info.getCollidable());
    }
}