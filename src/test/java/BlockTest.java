import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import collidable.Block;
import geometry.Rectangle;
import geometry.Point;
import ball.Velocity;
import javafx.scene.paint.Color;

public class BlockTest {

    private Block block;
    private Rectangle rect;
    private final double DELTA = 0.0001; // Delta cho so sánh số double

    // Tọa độ block: x=100, y=100, width=50, height=20
    // Cạnh trên: y=100
    // Cạnh dưới: y=120
    // Cạnh trái: x=100
    // Cạnh phải: x=150
    @Before // Thay vì @BeforeEach
    public void setUp() {
        rect = new Rectangle(new Point(100, 100), 50, 20);
        // Khởi tạo block với 3 hit points
        block = new Block(rect, Color.RED, 3, false, null);
    }

    /**
     * Test logic khởi tạo và các hàm getter cơ bản.
     */
    @Test
    public void testConstructorAndGetters() {
        assertEquals(rect, block.getCollisionRectangle());
        assertEquals(Color.RED, block.getColor());
        assertEquals(3, block.getHitPoints());
        assertFalse(block.isDeathRegion());
        assertNull(block.getPowerUp());
        assertFalse(block.hasPowerUp());
    }

    /**
     * Test logic giảm điểm va chạm (hit points).
     */
    @Test
    public void testDecreaseHitPoints() {
        block.decreaseHitPoints();
        assertEquals(2, block.getHitPoints());
        block.decreaseHitPoints();
        assertEquals(1, block.getHitPoints());
        block.decreaseHitPoints();
        assertEquals(0, block.getHitPoints());
        block.decreaseHitPoints();
        assertEquals(0, block.getHitPoints());
    }

    /**
     * Test va chạm từ cạnh trên.
     */
    @Test
    public void testHitFromTop() {
        Point collisionPoint = new Point(125, 100); // Giữa cạnh trên
        Velocity initialVelocity = new Velocity(5, -5); // Đang đi lên (dy âm)

        Velocity newVelocity = block.hit(null, collisionPoint, initialVelocity);

        assertEquals(5, newVelocity.getDx(), DELTA); // "dx không đổi"
        assertEquals(5, newVelocity.getDy(), DELTA); // "dy bị đảo ngược"
        assertEquals(2, block.getHitPoints()); // "Hit points giảm đi 1"
    }

    /**
     * Test va chạm từ cạnh dưới.
     */
    @Test
    public void testHitFromBottom() {
        Point collisionPoint = new Point(125, 120); // Giữa cạnh dưới
        Velocity initialVelocity = new Velocity(5, 5); // Đang đi xuống (dy dương)

        Velocity newVelocity = block.hit(null, collisionPoint, initialVelocity);

        assertEquals(5, newVelocity.getDx(), DELTA); // "dx không đổi"
        assertEquals(-5, newVelocity.getDy(), DELTA); // "dy bị đảo ngược"
        assertEquals(2, block.getHitPoints()); // "Hit points giảm đi 1"
    }

    /**
     * Test va chạm từ cạnh trái.
     */
    @Test
    public void testHitFromLeft() {
        Point collisionPoint = new Point(100, 110); // Giữa cạnh trái
        Velocity initialVelocity = new Velocity(-5, 5); // Đang đi sang trái (dx âm)

        Velocity newVelocity = block.hit(null, collisionPoint, initialVelocity);

        assertEquals(5, newVelocity.getDx(), DELTA); // "dx bị đảo ngược"
        assertEquals(5, newVelocity.getDy(), DELTA); // "dy không đổi"
        assertEquals(2, block.getHitPoints()); // "Hit points giảm đi 1"
    }

    /**
     * Test va chạm từ cạnh phải.
     */
    @Test
    public void testHitFromRight() {
        Point collisionPoint = new Point(150, 110); // Giữa cạnh phải
        Velocity initialVelocity = new Velocity(5, 5); // Đang đi sang phải (dx dương)

        Velocity newVelocity = block.hit(null, collisionPoint, initialVelocity);

        assertEquals(-5, newVelocity.getDx(), DELTA); // "dx bị đảo ngược"
        assertEquals(5, newVelocity.getDy(), DELTA); // "dy không đổi"
        assertEquals(2, block.getHitPoints()); // "Hit points giảm đi 1"
    }

    /**
     * Test trường hợp block có 0 hit points (bất tử).
     */
    @Test
    public void testHitOnIndestructibleBlock() {
        Block indestructibleBlock = new Block(rect, Color.GRAY, 0, false);
        assertEquals(0, indestructibleBlock.getHitPoints());

        Velocity newVelocity = indestructibleBlock.hit(null, new Point(125, 100), new Velocity(5, -5));

        assertEquals(0, indestructibleBlock.getHitPoints());
        assertEquals(5, newVelocity.getDy(), DELTA);
    }

    /**
     * Test trường hợp block có 1 hit point và bị va chạm 2 lần.
     */
    @Test
    public void testHitOnWeakBlockTwice() {
        Block weakBlock = new Block(rect, Color.CYAN, 1, false);
        assertEquals(1, weakBlock.getHitPoints());

        weakBlock.hit(null, new Point(125, 100), new Velocity(5, -5));
        assertEquals(0, weakBlock.getHitPoints());

        weakBlock.hit(null, new Point(125, 100), new Velocity(5, -5));
        assertEquals( 0, weakBlock.getHitPoints());
    }
}