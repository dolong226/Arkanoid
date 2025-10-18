import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.util.ArrayList;

public class RectangleTest {
    @Test
    public void rectangle4CornersTest(){
        Point newUpperLeft = new Point(4.0, 5.0);
        Rectangle newRec = new Rectangle(newUpperLeft, 1, 2);
        Point[] CornersOf4 = new Point[4];
        newRec.rectangle4Corners(CornersOf4);
        Point thisUpperLeft = new Point(4.0, 5.0);
        Point thisUpperRight = new Point(6.0, 5.0);
        Point thisDownLeft = new Point(4.0, 6.0);
        Point thisDownRight = new Point(6.0, 6.0);
        Assert.assertEquals(thisUpperLeft.getX(), CornersOf4[0].getX(), 1e-9);
        Assert.assertEquals(thisUpperLeft.getY(), CornersOf4[0].getY(), 1e-9);
        Assert.assertEquals(thisUpperRight.getX(), CornersOf4[1].getX(), 1e-9);
        Assert.assertEquals(thisUpperRight.getY(), CornersOf4[1].getY(), 1e-9);
        Assert.assertEquals(thisDownLeft.getX(), CornersOf4[2].getX(), 1e-9);
        Assert.assertEquals(thisDownLeft.getY(), CornersOf4[2].getY(), 1e-9);
        Assert.assertEquals(thisDownRight.getX(), CornersOf4[3].getX(), 1e-9);
        Assert.assertEquals(thisDownRight.getY(), CornersOf4[3].getY(), 1e-9);
    }
    
    @Test 
    public void intersectionPointsTest1(){
        Point newUpperLeft = new Point(4.0, 5.0);
        Rectangle newRec = new Rectangle(newUpperLeft, 2.0, 6.0);
        List<Point> checkedIntersectionPointList = new ArrayList<Point>();
        Point start = new Point(5.93, 6.03);
        Point end = new Point(0.94, 1.04);
        Line newLine = new Line(start, end);
        checkedIntersectionPointList = newRec.intersectionPoints(newLine);
        List<Point> expectedIntersectionPointList = new ArrayList<Point>();
        Point intersectionPoint1 = new Point(4.9, 5.0);
        expectedIntersectionPointList.add(intersectionPoint1);
        Assert.assertEquals(expectedIntersectionPointList.get(0).getX(), checkedIntersectionPointList.get(0).getX(), 1e-9);
        Assert.assertEquals(expectedIntersectionPointList.get(0).getY(), checkedIntersectionPointList.get(0).getY(), 1e-9);
    }

    @Test
    public void intersectionPointsTest2(){
        Point newUpperLeft = new Point(4.0, 5.0);
        Rectangle newRec = new Rectangle(newUpperLeft, 2.0, 6.0);
        List<Point> checkedIntersectionPointList = new ArrayList<Point>();
        Point start = new Point(8.0, 8.0);
        Point end = new Point(0.94, 1.04);
        Line newLine = new Line(start, end);
        checkedIntersectionPointList = newRec.intersectionPoints(newLine);
        List<Point> expectedIntersectionPointList = new ArrayList<Point>();
        Point intersectionPoint1 = new Point(575.0/116.0, 5.0);
        expectedIntersectionPointList.add(intersectionPoint1);
        Point intersectionPoint2 = new Point(2431.0/348.0, 7.0);
        expectedIntersectionPointList.add(intersectionPoint2);
        Assert.assertEquals(expectedIntersectionPointList.get(0).getX(), checkedIntersectionPointList.get(0).getX(), 0.00001);
        Assert.assertEquals(expectedIntersectionPointList.get(0).getY(), checkedIntersectionPointList.get(0).getY(), 0.00001);
        Assert.assertEquals(expectedIntersectionPointList.get(1).getX(), checkedIntersectionPointList.get(1).getX(), 0.00001);
        Assert.assertEquals(expectedIntersectionPointList.get(1).getY(), checkedIntersectionPointList.get(1).getY(), 0.00001);
    }

}
