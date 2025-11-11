import ball.Ball;
import ball.Velocity;
import static org.junit.Assert.assertEquals;
import geometry.Point;
import org.junit.Test;

public class VelocityTest1 {

    private static final double DELTA = 0.001;

    @Test
    public void testConstructorAndGettersSetters() {
        Velocity v = new Velocity(3.5, -4.2);
        assertEquals(3.5, v.getDx(), DELTA);
        assertEquals(-4.2, v.getDy(), DELTA);

        v.setDx(10.0);
        v.setDy(-20.0);
        assertEquals(10.0, v.getDx(), DELTA);
        assertEquals(-20.0, v.getDy(), DELTA);
    }

    @Test
    public void testApplyToPoint() {
        Point startPoint = new Point(100, 200);
        Velocity v = new Velocity(5, -10);

        Point endPoint = v.applyToPoint(startPoint);

        assertEquals(105, endPoint.getX(), DELTA);
        assertEquals(190, endPoint.getY(), DELTA);

        Point startPoint2 = new Point(-50, 0);
        Velocity v2 = new Velocity(-2, 3);
        Point endPoint2 = v2.applyToPoint(startPoint2);
        assertEquals(-52, endPoint2.getX(), DELTA);
        assertEquals(3, endPoint2.getY(), DELTA);
    }

    @Test
    public void testFromAngleAndSpeedStandardAngles() {
        double speed = 10.0;

        Velocity v0 = Velocity.fromAngleAndSpeed(0, speed);
        assertEquals(0, v0.getDx(), DELTA);
        assertEquals(-speed, v0.getDy(), DELTA);

        Velocity v90 = Velocity.fromAngleAndSpeed(90, speed);
        assertEquals(speed, v90.getDx(), DELTA);
        assertEquals(0, v90.getDy(), DELTA);

        Velocity v180 = Velocity.fromAngleAndSpeed(180, speed);
        assertEquals(0, v180.getDx(), DELTA);
        assertEquals(speed, v180.getDy(), DELTA);

        Velocity v270 = Velocity.fromAngleAndSpeed(270, speed);
        assertEquals(-speed, v270.getDx(), DELTA);
        assertEquals(0, v270.getDy(), DELTA);

        Velocity v360 = Velocity.fromAngleAndSpeed(360, speed);
        assertEquals(0, v360.getDx(), DELTA);
        assertEquals(-speed, v360.getDy(), DELTA);
    }

    @Test
    public void testFromAngleAndSpeedQuadrants() {
        double speed = 10.0;
        double component = speed * Math.sin(Math.toRadians(45));

        Velocity v45 = Velocity.fromAngleAndSpeed(45, speed);
        assertEquals(component, v45.getDx(), DELTA);
        assertEquals(-component, v45.getDy(), DELTA);


        Velocity v135 = Velocity.fromAngleAndSpeed(135, speed);
        assertEquals(component, v135.getDx(), DELTA);
        assertEquals(component, v135.getDy(), DELTA);


        Velocity v225 = Velocity.fromAngleAndSpeed(225, speed);
        assertEquals(-component, v225.getDx(), DELTA);
        assertEquals(component, v225.getDy(), DELTA);


        Velocity v315 = Velocity.fromAngleAndSpeed(315, speed);
        assertEquals(-component, v315.getDx(), DELTA);
        assertEquals(-component, v315.getDy(), DELTA);
    }

    @Test
    public void testFromAngleAndSpeedBoundaries() {
        double speed = 10.0;


        Velocity v90_1 = Velocity.fromAngleAndSpeed(90.0001, speed);

        assertEquals(speed * Math.cos(Math.toRadians(90.0001-90)), v90_1.getDx(), DELTA);
        assertEquals(speed * Math.sin(Math.toRadians(90.0001-90)), v90_1.getDy(), DELTA);

        Velocity v180_1 = Velocity.fromAngleAndSpeed(180.0001, speed);

        assertEquals(-speed * Math.sin(Math.toRadians(180.0001-180)), v180_1.getDx(), DELTA);
        assertEquals(speed * Math.cos(Math.toRadians(180.0001-180)), v180_1.getDy(), DELTA);


        Velocity v270_1 = Velocity.fromAngleAndSpeed(270.0001, speed);

        assertEquals(-speed * Math.cos(Math.toRadians(270.0001-270)), v270_1.getDx(), DELTA);
        assertEquals(-speed * Math.sin(Math.toRadians(270.0001-270)), v270_1.getDy(), DELTA);
    }


    @Test
    public void testFromAngleAndSpeedGreaterThan360() {
        double speed = 10.0;

        Velocity v405 = Velocity.fromAngleAndSpeed(405, speed);
        Velocity v45 = Velocity.fromAngleAndSpeed(45, speed);
        assertEquals(v45.getDx(), v405.getDx(), DELTA);
        assertEquals(v45.getDy(), v405.getDy(), DELTA);
    }


    @Test
    public void testFromAngleAndSpeedNegativeAngles() {
        double speed = 10.0;

        Velocity v_45 = Velocity.fromAngleAndSpeed(-45, speed);
        Velocity v315 = Velocity.fromAngleAndSpeed(315, speed);
        assertEquals(v315.getDx(), v_45.getDx(), DELTA);
        assertEquals(v315.getDy(), v_45.getDy(), DELTA);

        Velocity v_90 = Velocity.fromAngleAndSpeed(-90, speed);
        Velocity v270 = Velocity.fromAngleAndSpeed(270, speed);
        assertEquals(v270.getDx(), v_90.getDx(), DELTA);
        assertEquals(v270.getDy(), v_90.getDy(), DELTA);

        Velocity v_180 = Velocity.fromAngleAndSpeed(-180, speed);
        Velocity v180 = Velocity.fromAngleAndSpeed(180, speed);
        assertEquals(v180.getDx(), v_180.getDx(), DELTA);
        assertEquals(v180.getDy(), v_180.getDy(), DELTA);
    }

    @Test
    public void testZeroVelocity() {
        Velocity v = new Velocity(0, 0);
        Point p = new Point(10, 10);
        Point result = v.applyToPoint(p);

        assertEquals(10, result.getX(), DELTA);
        assertEquals(10, result.getY(), DELTA);
    }

    @Test
    public void testNegativeVelocity() {
        Velocity v = new Velocity(-5, -5);
        Point p = new Point(0, 0);
        Point result = v.applyToPoint(p);

        assertEquals(-5, result.getX(), DELTA);
        assertEquals(-5, result.getY(), DELTA);
    }

    @Test
    public void testSmallDecimalVelocity() {
        Velocity v = new Velocity(0.1, -0.2);
        Point p = new Point(1.0, 1.0);
        Point result = v.applyToPoint(p);

        assertEquals(1.1, result.getX(), DELTA);
        assertEquals(0.8, result.getY(), DELTA);
    }

    @Test
    public void testSpeedComputationConsistency() {
        // Kiểm tra rằng tốc độ tính từ dx, dy tương đương với tốc độ ban đầu
        double speed = 8.0;
        Velocity v = Velocity.fromAngleAndSpeed(60, speed);
        double computedSpeed = Math.sqrt(v.getDx() * v.getDx() + v.getDy() * v.getDy());
        assertEquals(speed, computedSpeed, DELTA);
    }

    @Test
    public void testAngleNormalizationOverMultipleRotations() {
        // 720 + 45 = 2 vòng + 45 độ
        double speed = 5.0;
        Velocity v1 = Velocity.fromAngleAndSpeed(45, speed);
        Velocity v2 = Velocity.fromAngleAndSpeed(765, speed); // 720 + 45

        assertEquals(v1.getDx(), v2.getDx(), DELTA);
        assertEquals(v1.getDy(), v2.getDy(), DELTA);
    }

    @Test
    public void testFromAngleAndSpeedAtRandomAngles() {
        double speed = 10.0;
        Velocity v123 = Velocity.fromAngleAndSpeed(123, speed);
        double dx = v123.getDx();
        double dy = v123.getDy();

        // Kiểm tra giá trị dx, dy có độ dài đúng bằng speed
        double computedSpeed = Math.sqrt(dx * dx + dy * dy);
        assertEquals(speed, computedSpeed, DELTA);
    }

    @Test
    public void testInverseVelocity() {
        Velocity v = new Velocity(4, -3);
        Velocity inverse = new Velocity(-v.getDx(), -v.getDy());
        Point p = new Point(100, 100);

        Point p1 = v.applyToPoint(p);
        Point p2 = inverse.applyToPoint(p1);

        // Sau khi di chuyển bằng v rồi inverse(v), ta phải quay lại vị trí ban đầu
        assertEquals(p.getX(), p2.getX(), DELTA);
        assertEquals(p.getY(), p2.getY(), DELTA);
    }

    @Test
    public void testExtremeAngles() {
        double speed = 10.0;

        Velocity v89_999 = Velocity.fromAngleAndSpeed(89.999, speed);
        Velocity v90_001 = Velocity.fromAngleAndSpeed(90.001, speed);

        // Hai góc rất gần nhau => dx, dy cũng phải gần nhau
        assertEquals(v89_999.getDx(), v90_001.getDx(), 0.01);
        assertEquals(v89_999.getDy(), v90_001.getDy(), 0.01);
    }

}
