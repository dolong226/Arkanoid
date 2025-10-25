import ball.Ball;
import ball.Velocity;
import static org.junit.Assert.assertEquals;
import geometry.Point; 
import org.junit.Test;

public class VelocityTest {

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
}
