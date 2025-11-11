import geometry.Point;
import org.junit.Test;
import org.junit.Assert;

public class PointTest1 {
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
    public void testConstructor4() {
        double expectedX = 10.0;
        double expectedY = -5.0;
        Point newPoint = new Point(-3.5, 10.0);
        Assert.assertEquals(expectedX, newPoint.getX(), 1e-9);
        Assert.assertEquals(expectedY, newPoint.getY(), 1e-9);
    }

    @Test
    public void testConstructor5() {
        Point p = new Point(-7.5, 12.3);
        Assert.assertEquals(-7.5, p.getX(), 1e-9);
        Assert.assertEquals(12.3, p.getY(), 1e-9);
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
    public void testSetXY3() {
        Point p = new Point();
        p.setX(8.8);
        p.setY(-2.2);
        Assert.assertEquals(8.8, p.getX(), 1e-9);
        Assert.assertEquals(-2.2, p.getY(), 1e-9);
    }

    @Test
    public void testSetXY4() {
        Point p = new Point();
        p.setX(-11.0);
        p.setY(0.5);
        Assert.assertEquals(-11.0, p.getX(), 1e-9);
        Assert.assertEquals(0.5, p.getY(), 1e-9);
    }


    @Test
    public void testSetLocation1() {
        Point p = new Point(1.0, 1.0);
        p.setLocation(5.0, 7.0);
        Assert.assertEquals(5.0, p.getX(), 1e-9);
        Assert.assertEquals(7.0, p.getY(), 1e-9);
    }

    @Test
    public void testSetLocation2() {
        Point p = new Point(-2.0, 3.0);
        p.setLocation(0.0, 0.0);
        Assert.assertEquals(0.0, p.getX(), 1e-9);
        Assert.assertEquals(0.0, p.getY(), 1e-9);
    }

    @Test
    public void testSetLocation3() {
        Point p = new Point(1.0, 1.0);
        p.setLocation(3.3, 4.4);
        Assert.assertEquals(3.3, p.getX(), 1e-9);
        Assert.assertEquals(4.4, p.getY(), 1e-9);
    }

    @Test
    public void testSetLocation4() {
        Point p = new Point(-5.5, -6.6);
        p.setLocation(0.0, -3.3);
        Assert.assertEquals(0.0, p.getX(), 1e-9);
        Assert.assertEquals(-3.3, p.getY(), 1e-9);
    }


    @Test
    public void testGetLocation1() {
        Point p = new Point(4.0, 5.0);
        Point copy = p.getLocation();
        Assert.assertEquals(4.0, copy.getX(), 1e-9);
        Assert.assertEquals(5.0, copy.getY(), 1e-9);
        Assert.assertNotSame(p, copy);
    }

    @Test
    public void testGetLocation2() {
        Point p = new Point(7.7, -8.8);
        Point copy = p.getLocation();
        Assert.assertEquals(7.7, copy.getX(), 1e-9);
        Assert.assertEquals(-8.8, copy.getY(), 1e-9);
        Assert.assertNotSame(p, copy);
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

    @Test
    public void testDistance6() {
        Point p1 = new Point(-1.0, -1.0);
        Point p2 = new Point(2.0, 3.0);
        double distance = p1.distance(p2);
        double expected = Math.sqrt(25); // (2+1)^2 + (3+1)^2 = 9 + 16 = 25
        Assert.assertEquals(expected, distance, 1e-9);
    }

    @Test
    public void testDistance7() {
        Point p1 = new Point(0.0, 0.0);
        Point p2 = new Point(0.0, 5.0);
        double distance = p1.distance(p2);
        double expected = 5.0;
        Assert.assertEquals(expected, distance, 1e-9);
    }

    @Test
    public void testDistance8() {
        Point p1 = new Point(3.0, 4.0);
        Point p2 = new Point(0.0, 0.0);
        double distance = p1.distance(p2);
        double expected = 5.0; // 3-4-5 triangle
        Assert.assertEquals(expected, distance, 1e-9);
    }
}
