import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import ball.Ball;
import ball.Velocity;
import collidable.Collidable;
import collidable.CollisionInfo;
import game.GameEnvironment;
import game.Sprite;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class BallTest {

    private static final double DELTA = 0.001;

    private MockGameEnvironment mockEnv;
    private Ball ball;

    static class TestVelocity extends Velocity {
        public TestVelocity(double dx, double dy) {
            super(dx, dy);
        }

        @Override
        public Point applyToPoint(Point p) {
            return new Point(p.getX() + this.getDx(), p.getY() + this.getDy());
        }
    }

    static class MockGameEnvironment extends GameEnvironment {
        private CollisionInfo collisionToReturn = null;

        public void setCollisionToReturn(CollisionInfo info) {
            this.collisionToReturn = info;
        }

        @Override
        public CollisionInfo getClosetCollision(Line trajectory) {
            return this.collisionToReturn;
        }
    }

    static class MockCollidable implements Collidable {
        public Velocity velocityToReturnOnHit = null;

        @Override
        public Rectangle getCollisionRectangle() {
            return null; 
        }

        @Override
        public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
            if (velocityToReturnOnHit != null) {
                return velocityToReturnOnHit;
            }
            return new TestVelocity(-currentVelocity.getDx(), -currentVelocity.getDy());
        }
    }

    @Before
    public void setUp() {
        mockEnv = new MockGameEnvironment();
        
        Point startCenter = new Point(100, 100);
        Velocity startVelocity = new TestVelocity(10, 20); 
        ball = new Ball(startCenter, 5, Color.RED, startVelocity, mockEnv);
    }

    @Test
    public void testConstructorAndSetters() {
        assertNotNull(ball.getVelocity());
        assertEquals(10, ball.getVelocity().getDx(), DELTA);
        assertEquals(20, ball.getVelocity().getDy(), DELTA);

        Velocity v2 = new TestVelocity(50, 60);
        ball.setVelocity(v2);
        assertSame(v2, ball.getVelocity());

        ball.setVelocity(70, 80);
        assertEquals(70, ball.getVelocity().getDx(), DELTA);
        assertEquals(80, ball.getVelocity().getDy(), DELTA);
    }


    @Test
    public void testChangeDirectionIfMoveNear() {
        ball.setVelocity(10, 10);
        Point p1 = ball.changeDirectionIfMoveNear();
        assertEquals(99, p1.getX(), DELTA); 
        assertEquals(99, p1.getY(), DELTA); 

        ball.setVelocity(-10, -10);
        Point p2 = ball.changeDirectionIfMoveNear();
        assertEquals(101, p2.getX(), DELTA);
        assertEquals(101, p2.getY(), DELTA); 

        ball.setVelocity(10, -10);
        Point p3 = ball.changeDirectionIfMoveNear();
        assertEquals(99, p3.getX(), DELTA); 
        assertEquals(101, p3.getY(), DELTA);
    }

    @Test
    public void testUpdateNoCollision() {
        mockEnv.setCollisionToReturn(null);

    
        ball.update(1.0);
        assertEquals(110, ball.getCenter().getX(), DELTA);
        assertEquals(120, ball.getCenter().getY(), DELTA);
        ball.update(0.5);
        assertEquals(115, ball.getCenter().getX(), DELTA);
        assertEquals(130, ball.getCenter().getY(), DELTA);
    }

    @Test
    public void testUpdateWithCollision() {
        MockCollidable mockWall = new MockCollidable();
        Velocity bouncedVelocity = new TestVelocity(-10, -20); 
        mockWall.velocityToReturnOnHit = bouncedVelocity;

        Point collisionPoint = new Point(105, 110); 
        
        CollisionInfo info = new CollisionInfo(collisionPoint, mockWall);
        mockEnv.setCollisionToReturn(info);

        ball.update(1.0);

        assertEquals(-10, ball.getVelocity().getDx(), DELTA);
        assertEquals(-20, ball.getVelocity().getDy(), DELTA);
        assertEquals(89, ball.getCenter().getX(), DELTA);
        assertEquals(79, ball.getCenter().getY(), DELTA);
    }
}
