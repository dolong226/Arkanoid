import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import collidable.Paddle;
import geometry.Rectangle;
import geometry.Point;
import javafx.scene.paint.Color;

public class PaddleTest {

    private Paddle paddle;
    private final double MIN_BOUNDARY = 0;
    private final double MAX_BOUNDARY = 400;
    // Paddle: x=150, y=500, width=100, height=20
    private final double PADDLE_X = 150;
    private final double PADDLE_WIDTH = 100;
    private final int STEP = 50;
    private final double DELTA = 0.0001;

    @Before
    public void setUp() {
        Rectangle rect = new Rectangle(new Point(PADDLE_X, 500), PADDLE_WIDTH, 20);
        paddle = new Paddle(STEP, Color.BLUE, rect, MIN_BOUNDARY, MAX_BOUNDARY);
    }

    /**
     * Test hàm khởi tạo và các hàm getter cơ bản.
     */
    @Test
    public void testConstructorAndGetters() {
        assertEquals(PADDLE_X, paddle.getX(), DELTA);
        assertEquals(500, paddle.getY(), DELTA);
        assertEquals(PADDLE_WIDTH, paddle.getWidth(), DELTA);
        assertEquals(PADDLE_WIDTH, paddle.getOriginalWidth(), DELTA);
        assertNotNull(paddle.getCollisionRectangle());
    }

    /**
     * Test di chuyển sang trái và phải.
     */
    @Test
    public void testMoveLeftAndRight() {
        double dt = 1.0;
        paddle.moveRight(dt);
        // 150 + 50*1.0 = 200
        assertEquals(200, paddle.getX(), DELTA);
        paddle.moveLeft(dt);
        // 200 - 50*1.0 = 150
        assertEquals(150, paddle.getX(), DELTA);
    }

    /**
     * Test giới hạn di chuyển (boundaries).
     */
    @Test
    public void testMovementBoundaries() {
        double dt = 1.0;
        // trái
        paddle.moveLeft(10.0);
        assertEquals(MIN_BOUNDARY, paddle.getX(), DELTA);
        // phải
        paddle.moveRight(10.0);
        assertEquals(300, paddle.getX(), DELTA);
    }

    /**
     * Test logic thay đổi chiều rộng (setWidth) và resetWidth.
     */
    @Test
    public void testSetWidthAndReset() {
        // Tâm hiện tại: x + width/2 = 150 + 100/2 = 200

        // Thu nhỏ
        paddle.setWidth(50);
        assertEquals(50, paddle.getWidth(), DELTA);
        assertEquals(175, paddle.getX(), DELTA);

        // Mở rộng
        paddle.setWidth(120);
        assertEquals(120, paddle.getWidth(), DELTA);
        assertEquals(140, paddle.getX(), DELTA);

        // Reset về ban đầu
        paddle.resetWidth();
        assertEquals(100, paddle.getWidth(), DELTA);
        assertEquals(150, paddle.getX(), DELTA);
    }

}