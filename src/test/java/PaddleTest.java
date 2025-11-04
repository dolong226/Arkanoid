
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import collidable.*;
import ball.Ball;
import ball.Velocity;
import geometry.Point;
import geometry.Rectangle;
import javafx.scene.paint.Color; 

public class PaddleTest {

    private static final double DELTA = 0.001; 

    private Rectangle defaultRect;
    private Paddle paddle;
    private int defaultStep = 10;
    private double minBoundary = 0;
    private double maxBoundary = 800; 

    @Before
    public void setUp() {
        defaultRect = new Rectangle(new Point(350, 580), 20, 100);
        paddle = new Paddle(defaultStep, Color.ORANGE, defaultRect, minBoundary, maxBoundary);
    }

    @Test
    public void testConstructorAndGetter() {
        assertSame(defaultRect, paddle.getCollisionRectangle());
    }


    @Test
    public void testMoveLeftNormal() {
        double initialX = paddle.getCollisionRectangle().getUpperLeft().getX(); // 350
        double dt = 0.5; 
        paddle.moveLeft(dt);

        double expectedX = initialX - defaultStep * dt;
        assertEquals(expectedX, paddle.getCollisionRectangle().getUpperLeft().getX(), DELTA);
    }

    @Test
    public void testMoveLeftHitBoundary() {
        paddle.getCollisionRectangle().getUpperLeft().setX(5);
        double dt = 1.0;
        
        paddle.moveLeft(dt);

        assertEquals(minBoundary, paddle.getCollisionRectangle().getUpperLeft().getX(), DELTA);
    }

    @Test
    public void testMoveRightNormal() {
        double initialX = paddle.getCollisionRectangle().getUpperLeft().getX(); 
        double dt = 0.5; 
        paddle.moveRight(dt);

        double expectedX = initialX + defaultStep * dt;
        assertEquals(expectedX, paddle.getCollisionRectangle().getUpperLeft().getX(), DELTA);
    }

    @Test
    public void testMoveRightHitBoundary() {
        paddle.getCollisionRectangle().getUpperLeft().setX(maxBoundary - paddle.getCollisionRectangle().getLength() - 5); 
        double dt = 1.0;
        paddle.moveRight(dt);

        double expectedX = maxBoundary - paddle.getCollisionRectangle().getLength();
        assertEquals(expectedX, paddle.getCollisionRectangle().getUpperLeft().getX(), DELTA);
    }
    @Test
    public void testCheckRegionCalculationAndLogic() {
        assertEquals(1, paddle.checkRegion(new Point(350, 580))); 
        assertEquals(1, paddle.checkRegion(new Point(360, 580)));
        assertEquals(1, paddle.checkRegion(new Point(369.999, 580)));

        assertEquals(2, paddle.checkRegion(new Point(370, 580))); 
        assertEquals(2, paddle.checkRegion(new Point(380, 580)));
        assertEquals(2, paddle.checkRegion(new Point(389.999, 580)));

        assertEquals(3, paddle.checkRegion(new Point(390, 580))); 
        assertEquals(3, paddle.checkRegion(new Point(400, 580)));
        assertEquals(3, paddle.checkRegion(new Point(409.999, 580)));

        assertEquals(4, paddle.checkRegion(new Point(410, 580)));
        assertEquals(4, paddle.checkRegion(new Point(420, 580)));
        assertEquals(4, paddle.checkRegion(new Point(429.999, 580)));

        assertEquals(5, paddle.checkRegion(new Point(430, 580))); 
        assertEquals(5, paddle.checkRegion(new Point(440, 580)));
        assertEquals(5, paddle.checkRegion(new Point(450, 580))); 

        assertEquals(0, paddle.checkRegion(new Point(349.999, 580))); 
        assertEquals(0, paddle.checkRegion(new Point(450.001, 580))); 
    }
    
    @Test
    public void testHitRegion3_Center() {
        Point collisionP = new Point(400, 580);
        Velocity incomingVel = new Velocity(5, -10);

        Velocity resultVel = paddle.hit(null, collisionP, incomingVel);

        assertEquals(5, resultVel.getDx(), DELTA);
        assertEquals(10, resultVel.getDy(), DELTA);
    }

    @Test
    public void testHitRegion1_LeftEdge() {
        Point collisionP = new Point(360, 580);
        Velocity incomingVel = new Velocity(5, -10); 
        double speed = Math.sqrt(5*5 + (-10)*(-10)); 

        Velocity resultVel = paddle.hit(null, collisionP, incomingVel);

        Velocity expectedVel = Velocity.fromAngleAndSpeed(-60, speed);
        assertEquals(expectedVel.getDx(), resultVel.getDx(), DELTA);
        assertEquals(expectedVel.getDy(), resultVel.getDy(), DELTA);
    }

    @Test
    public void testHitRegion5_RightEdge() {
        Point collisionP = new Point(440, 580); 
        Velocity incomingVel = new Velocity(-5, -10);
        double speed = Math.sqrt((-5)*(-5) + (-10)*(-10)); 

        Velocity resultVel = paddle.hit(null, collisionP, incomingVel);

        Velocity expectedVel = Velocity.fromAngleAndSpeed(60, speed);
        assertEquals(expectedVel.getDx(), resultVel.getDx(), DELTA);
        assertEquals(expectedVel.getDy(), resultVel.getDy(), DELTA);
    }

    @Test
    public void testHitRegion2() {
        Point collisionP = new Point(380, 580);
        Velocity incomingVel = new Velocity(5, -10); 
        double speed = Math.sqrt(5*5 + (-10)*(-10));

        Velocity resultVel = paddle.hit(null, collisionP, incomingVel);

        Velocity expectedVel = Velocity.fromAngleAndSpeed(-30, speed);
        assertEquals(expectedVel.getDx(), resultVel.getDx(), DELTA);
        assertEquals(expectedVel.getDy(), resultVel.getDy(), DELTA);
    }

    @Test
    public void testHitRegion4() {
        Point collisionP = new Point(420, 580); 
        Velocity incomingVel = new Velocity(-5, -10); 
        double speed = Math.sqrt((-5)*(-5) + (-10)*(-10));

        Velocity resultVel = paddle.hit(null, collisionP, incomingVel);

        Velocity expectedVel = Velocity.fromAngleAndSpeed(30, speed);
        assertEquals(expectedVel.getDx(), resultVel.getDx(), DELTA);
        assertEquals(expectedVel.getDy(), resultVel.getDy(), DELTA);
    }

    @Test
    public void testHitSideEdge() {
        Point collisionP = new Point(350, 590); 
        Velocity incomingVel = new Velocity(-5, 5); 

        Velocity resultVel = paddle.hit(null, collisionP, incomingVel);

        assertEquals(5, resultVel.getDx(), DELTA); 
        assertEquals(5, resultVel.getDy(), DELTA); 
    }
}
