import collidable.Block;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import ball.Ball; 
import ball.Velocity; 
import geometry.Point;
import geometry.Rectangle;
import javafx.scene.paint.Color; 
import listener.HitEvent; 
import listener.HitListener; 

import java.util.ArrayList;
import java.util.List;


public class BlockTest {

    private static final double DELTA = 0.001; 

    private Rectangle defaultRect;
    private Block block;
    private Ball mockBall; 
    static class MockHitListener implements HitListener {
        public boolean hitEventCalled = false;
        public Block blockHit = null;
        public Ball hitterBall = null;
        public Point collisionPoint = null;

        @Override
        public void hitEvent(HitEvent e) {
            this.hitEventCalled = true;
            this.blockHit = e.getTarget();
            this.hitterBall = e.getHitter();
            this.collisionPoint = e.getCollisionPoint();
        }

        public void reset() {
            this.hitEventCalled = false;
            this.blockHit = null;
            this.hitterBall = null;
            this.collisionPoint = null;
        }
    }

    @Before
    public void setUp() {
        defaultRect = new Rectangle(new Point(100, 200), 20, 50);
        block = new Block(defaultRect, Color.BLUE);
        
        mockBall = new Ball(new Point(0, 0), 1, Color.RED, new Velocity(0, 0), null); 
    }

    @Test
    public void testConstructorsAndGetters() {
        assertSame(defaultRect, block.getCollisionRectangle());
        assertEquals(Color.BLUE, block.getColor()); 
        Point p = new Point(10, 20);
        Block block2 = new Block(p, 30, 40);
        assertEquals(p.getX(), block2.getCollisionRectangle().getUpperLeft().getX(), DELTA);
        assertEquals(p.getY(), block2.getCollisionRectangle().getUpperLeft().getY(), DELTA);
        assertEquals(40, block2.getCollisionRectangle().getLength(), DELTA); 
        assertEquals(30, block2.getCollisionRectangle().getWidth(), DELTA);  
    }

    @Test
    public void testHitTopEdge() {
        Point collisionP = new Point(125, 200);
        Velocity incomingVel = new Velocity(10, -10); 
        
        Velocity resultVel = block.hit(mockBall, collisionP, incomingVel);

        assertEquals(10, resultVel.getDx(), DELTA);  
        assertEquals(10, resultVel.getDy(), DELTA);  
    }

    @Test
    public void testHitBottomEdge() {
        Point collisionP = new Point(125, 220); 
        Velocity incomingVel = new Velocity(10, 10); 

        Velocity resultVel = block.hit(mockBall, collisionP, incomingVel);

        assertEquals(10, resultVel.getDx(), DELTA);  
        assertEquals(-10, resultVel.getDy(), DELTA); 
    }


    @Test
    public void testHitLeftEdge() {
        Point collisionP = new Point(100, 210); 
        Velocity incomingVel = new Velocity(-10, 10); 

        Velocity resultVel = block.hit(mockBall, collisionP, incomingVel);

        assertEquals(10, resultVel.getDx(), DELTA); 
        assertEquals(10, resultVel.getDy(), DELTA); 
    }

    @Test
    public void testHitRightEdge() {
        Point collisionP = new Point(150, 210); 
        Velocity incomingVel = new Velocity(10, 10); 

        Velocity resultVel = block.hit(mockBall, collisionP, incomingVel);

        assertEquals(-10, resultVel.getDx(), DELTA);
        assertEquals(10, resultVel.getDy(), DELTA);  
    }

    @Test
    public void testHitTopLeftCorner() {
        Point collisionP = new Point(100, 200);
        Velocity incomingVel = new Velocity(-10, -10);

        Velocity resultVel = block.hit(mockBall, collisionP, incomingVel);

        assertEquals(10, resultVel.getDx(), DELTA);
        assertEquals(10, resultVel.getDy(), DELTA);
    }
    
    
    @Test
    public void testHitBottomRightCorner() {
        Point collisionP = new Point(150, 220); 
        Velocity incomingVel = new Velocity(10, 10);

        Velocity resultVel = block.hit(mockBall, collisionP, incomingVel);

        assertEquals(-10, resultVel.getDx(), DELTA);
        assertEquals(-10, resultVel.getDy(), DELTA); 
    }

    @Test
    public void testHitVelocityReference() {
        Point collisionP = new Point(125, 200); 
        Velocity originalVel = new Velocity(5, -5);
        
        Velocity returnedVel = block.hit(mockBall, collisionP, originalVel);

        assertEquals(5, returnedVel.getDx(), DELTA);
        assertEquals(5, returnedVel.getDy(), DELTA); 

        assertEquals(5, originalVel.getDx(), DELTA); 
        assertEquals(-5, originalVel.getDy(), DELTA);
    }

    @Test
    public void testAddAndNotifyHitListener() {
        MockHitListener listener = new MockHitListener();
        block.addHitListener(listener);

        assertFalse("Listener không nên được gọi trước khi hit", listener.hitEventCalled);

        Point collisionP = new Point(130, 200);
        Velocity incomingVel = new Velocity(0, -10);
        block.hit(mockBall, collisionP, incomingVel);

        assertTrue("Listener đáng lẽ phải được gọi sau khi hit", listener.hitEventCalled);
        assertSame("Listener phải nhận đúng block bị hit", block, listener.blockHit);
        assertSame("Listener phải nhận đúng ball hitter", mockBall, listener.hitterBall);
        assertEquals(collisionP.getX(), listener.collisionPoint.getX(), DELTA);
        assertEquals(collisionP.getY(), listener.collisionPoint.getY(), DELTA);
    }

    @Test
    public void testRemoveHitListener() {
        MockHitListener listener = new MockHitListener();
        block.addHitListener(listener);
        block.removeHitListener(listener);

        Point collisionP = new Point(130, 200);
        Velocity incomingVel = new Velocity(0, -10);
        block.hit(mockBall, collisionP, incomingVel);

        assertFalse("Listener đã bị xóa không nên được gọi", listener.hitEventCalled);
    }

    @Test
    public void testNotifyMultipleListeners() {
        MockHitListener listener1 = new MockHitListener();
        MockHitListener listener2 = new MockHitListener();
        block.addHitListener(listener1);
        block.addHitListener(listener2);

        Point collisionP = new Point(130, 200);
        Velocity incomingVel = new Velocity(0, -10);
        block.hit(mockBall, collisionP, incomingVel);

        assertTrue("Listener 1 phải được gọi", listener1.hitEventCalled);
        assertTrue("Listener 2 phải được gọi", listener2.hitEventCalled);
    }
}
