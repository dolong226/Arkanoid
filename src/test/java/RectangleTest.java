import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.util.ArrayList;

public class RectangleTest {
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
}
