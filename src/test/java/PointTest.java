import org.junit.Test;
import org.junit.Assert;

public class PointTest {
    @Test
    public void testConstructor1(){
        double expectedX = 3.0;
        double expectedY = 5.0;
        Point newPoint = new Point(3, 5);
        Assert.assertEquals(expectedX, newPoint.getX(), 1e-9);
        Assert.assertEquals(expectedY, newPoint.getY(), 1e-9);
    }

    @Test
    public void testConstructor2(){
        double expectedX = 0.0;
        double expectedY = 0.0;
        Point newPoint = new Point(0.0, 0.0);
        Assert.assertEquals(expectedX, newPoint.getX(), 1e-9);
        Assert.assertEquals(expectedY, newPoint.getY(), 1e-9);
    }

    @Test
    public void testConstructor3(){
        double expectedX = -3.5;
        double expectedY = 10.0;
        Point newPoint = new Point(-3.5, 10.0);
        Assert.assertEquals(expectedX, newPoint.getX(), 1e-9);
        Assert.assertEquals(expectedY, newPoint.getY(), 1e-9);
    }

    @Test 
    public void testSetXY1(){
        Point newPoint = new Point();
        double expectedX = -3.5;
        double expectedY = 10.0;
        newPoint.setX(-3.5);
        newPoint.setY(10.0);
        Assert.assertEquals(expectedX, newPoint.getX(), 1e-9);
        Assert.assertEquals(expectedY, newPoint.getY(), 1e-9);
    }

    @Test
    public void testSetXY2(){
        Point newPoint = new Point();
        double expectedX = 7.0;
        double expectedY = 10.0;
        newPoint.setX(7.0);
        newPoint.setY(10.0);
        Assert.assertEquals(expectedX, newPoint.getX(), 1e-9);
        Assert.assertEquals(expectedY, newPoint.getY(), 1e-9);
    }
    
    @Test
    public void testDistance1(){
        Point newPoint = new Point();
        newPoint.setX(5.0);
        newPoint.setY(3.0);
        Point otherPoint = new Point();
        otherPoint.setX(0.0);
        otherPoint.setY(0.0);
        double distance = newPoint.distance(otherPoint);
        double expectedResult = Math.sqrt(34);
        Assert.assertEquals(expectedResult, distance, 1e-9);
    }

    @Test
    public void testDistance2(){
      Point newPoint = new Point();
        newPoint.setX(5.0);
        newPoint.setY(3.0);
        Point otherPoint = new Point();
        otherPoint.setX(5.0);
        otherPoint.setY(3.0);
        double distance = newPoint.distance(otherPoint);
        double expectedResult = Math.sqrt(0);
        Assert.assertEquals(expectedResult, distance, 1e-9);  
    }

    @Test
    public void testDistance3(){
        Point newPoint = new Point();
        newPoint.setX(5.0);
        newPoint.setY(3.0);
        Point otherPoint = new Point();
        otherPoint.setX(5.0);
        otherPoint.setY(7.0);
        double distance = newPoint.distance(otherPoint);
        double expectedResult = Math.sqrt(16);
        Assert.assertEquals(expectedResult, distance, 1e-9);
    }

    @Test
    public void testDistance4(){
        Point newPoint = new Point();
        newPoint.setX(5.0);
        newPoint.setY(3.0);
        Point otherPoint = new Point();
        otherPoint.setX(2.0);
        otherPoint.setY(3.0);
        double distance = newPoint.distance(otherPoint);
        double expectedResult = Math.sqrt(9);
        Assert.assertEquals(expectedResult, distance, 1e-9);
    }

    @Test
    public void testDistance5(){
        Point newPoint = new Point();
        newPoint.setX(5.0);
        newPoint.setY(3.0);
        Point otherPoint = new Point();
        otherPoint.setX(-2.0);
        otherPoint.setY(6.0);
        double distance = newPoint.distance(otherPoint);
        double expectedResult = Math.sqrt(58);
        Assert.assertEquals(expectedResult, distance, 1e-9);
    }
}
