import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

public class LineTest1 {
    @Test
    public void testConstructor1(){
        Point a = new Point(3.0, 5.0);
        Point b = new Point(4.0, 7.0);
        Line newLine = new Line(a,b);
        double expectedStartX = a.getX();
        double expectedStartY = a.getY();
        double expectedEndX = b.getX();
        double expectedEndY = b.getY();
        Assert.assertEquals(expectedStartX, newLine.getStart().getX(), 1e-9);
        Assert.assertEquals(expectedStartY, newLine.getStart().getY(), 1e-9);
        Assert.assertEquals(expectedEndX, newLine.getEnd().getX(), 1e-9);
        Assert.assertEquals(expectedEndY, newLine.getEnd().getY(), 1e-9);
    }

    @Test
    public void isIntersectingTest1(){
        Point start1 = new Point(3.0, 5.0);
        Point end1 = new Point(3.0, -7.0);
        Point start2 = new Point(2.0, 10.0);
        Point end2 = new Point(2.0, -5.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = false;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest2(){
        Point start1 = new Point(3.0, 5.0);
        Point end1 = new Point(3.0, -7.0);
        Point start2 = new Point(3.0, 10.0);
        Point end2 = new Point(3.0, -5.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = true;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest3(){
        Point start1 = new Point(3.0, 5.0);
        Point end1 = new Point(3.0, -7.0);
        Point start2 = new Point(3.0, 10.0);
        Point end2 = new Point(3.0, 15.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = false;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest4(){
        Point start1 = new Point(3.0, 5.0);
        Point end1 = new Point(3.0, 17.0);
        Point start2 = new Point(3.0, 10.0);
        Point end2 = new Point(3.0, 15.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = true;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest5(){
        Point start1 = new Point(3.0, 5.0);
        Point end1 = new Point(3.0, 17.0);
        Point start2 = new Point(5.0, 10.0);
        Point end2 = new Point(-5.0, 15.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = true;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest6(){
        Point start1 = new Point(3.0, 5.0);
        Point end1 = new Point(3.0, 17.0);
        Point start2 = new Point(5.0, 10.0);
        Point end2 = new Point(15.0, 5.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = false;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest7(){
        Point start1 = new Point(10.0, 10.0);
        Point end1 = new Point(-10.0, -10.0);
        Point start2 = new Point(3.0, 10.0);
        Point end2 = new Point(3.0, -17.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = true;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest8(){
        Point start1 = new Point(10.0, 10.0);
        Point end1 = new Point(5.0, 5.0);
        Point start2 = new Point(3.0, 10.0);
        Point end2 = new Point(3.0, -17.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = false;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest9(){
        Point start1 = new Point(0.0, 4.0);
        Point end1 = new Point(2.0, 10.0);
        Point start2 = new Point(3.0, 13.0);
        Point end2 = new Point(4.0, 16.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = false;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest10(){
        Point start1 = new Point(0.0, 4.0);
        Point end1 = new Point(2.0, 10.0);
        Point start2 = new Point(3.0, 13.0);
        Point end2 = new Point(1.0, 7.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = true;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest11(){
        Point start1 = new Point(0.0, 4.0);
        Point end1 = new Point(2.0, 10.0);
        Point start2 = new Point(0.0, 7.0);
        Point end2 = new Point(1.0, 10.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = false;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest12(){
        Point start1 = new Point(0.0, 4.0);
        Point end1 = new Point(2.0, 10.0);
        Point start2 = new Point(0.0, 7.0);
        Point end2 = new Point(2.0, 11.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = false;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest13(){
        Point start1 = new Point(0.0, 4.0);
        Point end1 = new Point(2.0, 10.0);
        Point start2 = new Point(0.0, 7.0);
        Point end2 = new Point(4.0, 15.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = false;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest14(){
        Point start1 = new Point(0.0, 4.0);
        Point end1 = new Point(4.0, 16.0);
        Point start2 = new Point(0.0, 7.0);
        Point end2 = new Point(4.0, 15.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = true;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest15() {
        Point start1 = new Point(0.0, 0.0);
        Point end1 = new Point(5.0, 5.0);
        Point start2 = new Point(5.0, 0.0);
        Point end2 = new Point(0.0, 5.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = true;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest16() {
        Point start1 = new Point(0.0, 0.0);
        Point end1 = new Point(5.0, 5.0);
        Point start2 = new Point(6.0, 6.0);
        Point end2 = new Point(10.0, 10.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = false;
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void isIntersectingTest17() {
        Point start1 = new Point(1.0, 1.0);
        Point end1 = new Point(4.0, 4.0);
        Point start2 = new Point(2.0, 2.0);
        Point end2 = new Point(3.0, 3.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        boolean expectedCheck = true; // chồng lên nhau một phần
        boolean check = thisLine.isIntersecting(otherLine);
        Assert.assertEquals(expectedCheck, check);
    }

    @Test
    public void intersectionPointTest1(){
        Point start1 = new Point(0.0, 4.0);
        Point end1 = new Point(2.0, 10.0);
        Point start2 = new Point(0.0, 7.0);
        Point end2 = new Point(4.0, 15.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        Point expectedResult = null;
        Point check = thisLine.intersectionPoint(otherLine);
        Assert.assertEquals(expectedResult, check);
    }

    @Test
    public void intersectionPointTest2(){
        Point start1 = new Point(3.0, 5.0);
        Point end1 = new Point(3.0, -7.0);
        Point start2 = new Point(3.0, 10.0);
        Point end2 = new Point(3.0, 15.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        Point expectedResult = null;
        Point check = thisLine.intersectionPoint(otherLine);
        Assert.assertEquals(expectedResult, check);
    }

    @Test
    public void intersectionPointTest3(){
        Point start1 = new Point(3.0, 5.0);
        Point end1 = new Point(3.0, 15.0);
        Point start2 = new Point(3.0, 10.0);
        Point end2 = new Point(3.0, -7.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        Point expectedResult = new Point(3.0, 10.0);
        Point check = thisLine.intersectionPoint(otherLine);
        Assert.assertEquals(expectedResult.getX(), check.getX(), 1e-9);
        Assert.assertEquals(expectedResult.getY(), check.getY(), 1e-9);
    }

    @Test
    public void intersectionPointTest4(){
        Point start1 = new Point(3.0, 5.0);
        Point end1 = new Point(3.0, -7.0);
        Point start2 = new Point(9.0, 10.0);
        Point end2 = new Point(-6.0, -5.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        Point expectedResult = new Point(3.0,4.0);
        Point check = thisLine.intersectionPoint(otherLine);
        Assert.assertEquals(expectedResult.getX(), check.getX(), 1e-9);
        Assert.assertEquals(expectedResult.getY(), check.getY(), 1e-9);
    }

    @Test
    public void intersectionPointTest5(){
        Point start1 = new Point(3.0, 5.0);
        Point end1 = new Point(3.0, -7.0);
        Point start2 = new Point(6.0, 10.0);
        Point end2 = new Point(-4.0, 0.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        Point expectedResult = null;
        Point check = thisLine.intersectionPoint(otherLine);
        Assert.assertEquals(expectedResult, check);
    }

    @Test
    public void intersectionPointTest6(){
        Point start1 = new Point(0.0, 3.0);
        Point end1 = new Point(6.0, 15.0);
        Point start2 = new Point(1.5, 0.0);
        Point end2 = new Point(2.5, 2.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        Point expectedResult = null;
        Point check = thisLine.intersectionPoint(otherLine);
        Assert.assertEquals(expectedResult, check);
    }

    @Test
    public void intersectionPointTest7(){
        Point start1 = new Point(0.0, 3.0);
        Point end1 = new Point(6.0, 15.0);
        Point start2 = new Point(-6.0, -5.0);
        Point end2 = new Point(-4.0, -3.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        Point expectedResult = null;
        Point check = thisLine.intersectionPoint(otherLine);
        Assert.assertEquals(expectedResult, check);
    }

    @Test
    public void intersectionPointTest8(){
        Point start1 = new Point(0.0, 3.0);
        Point end1 = new Point(-6.5, -10.0);
        Point start2 = new Point(4.0, 5.0);
        Point end2 = new Point(-6.0, -5.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        Point expectedResult = new Point(-2.0,-1.0);
        Point check = thisLine.intersectionPoint(otherLine);
        Assert.assertEquals(expectedResult.getX(), check.getX(), 1e-9);
        Assert.assertEquals(expectedResult.getY(), check.getY(), 1e-9);
    }

    @Test
    public void intersectionPointTest9() {
        Point start1 = new Point(0.0, 0.0);
        Point end1 = new Point(4.0, 4.0);
        Point start2 = new Point(0.0, 4.0);
        Point end2 = new Point(4.0, 0.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        Point expectedResult = new Point(2.0, 2.0);
        Point check = thisLine.intersectionPoint(otherLine);
        Assert.assertEquals(expectedResult.getX(), check.getX(), 1e-9);
        Assert.assertEquals(expectedResult.getY(), check.getY(), 1e-9);
    }


    @Test
    public void intersectionPointTest10() {
        Point start1 = new Point(0.0, 0.0);
        Point end1 = new Point(5.0, 0.0);
        Point start2 = new Point(2.5, -1.0);
        Point end2 = new Point(2.5, 1.0);
        Line thisLine = new Line(start1, end1);
        Line otherLine = new Line(start2, end2);
        Point expectedResult = new Point(2.5, 0.0);
        Point check = thisLine.intersectionPoint(otherLine);
        Assert.assertEquals(expectedResult.getX(), check.getX(), 1e-9);
        Assert.assertEquals(expectedResult.getY(), check.getY(), 1e-9);
    }

    @Test
    public void closestIntersectionToStartOfLine1(){
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
        Point intersectionPoint2 = new Point(6.0, 6.028328611898017);
        expectedIntersectionPointList.add(intersectionPoint2);
        Point checkedClosestIntersectionToStartOfLine = newLine.closestIntersectionToStartOfLine(newRec);
        Point expectedClosestIntersectionToStartOfLine = intersectionPoint2;
        Assert.assertEquals(expectedIntersectionPointList.get(0).getX(), checkedIntersectionPointList.get(0).getX(), 0.00001);
        Assert.assertEquals(expectedIntersectionPointList.get(0).getY(), checkedIntersectionPointList.get(0).getY(), 0.00001);
        Assert.assertEquals(expectedIntersectionPointList.get(1).getX(), checkedIntersectionPointList.get(1).getX(), 0.00001);
        Assert.assertEquals(expectedIntersectionPointList.get(1).getY(), checkedIntersectionPointList.get(1).getY(), 0.00001);
        Assert.assertEquals(expectedClosestIntersectionToStartOfLine.getX(), checkedClosestIntersectionToStartOfLine.getX(),1e-9);
        Assert.assertEquals(expectedClosestIntersectionToStartOfLine.getY(), checkedClosestIntersectionToStartOfLine.getY(),1e-9);
    }

    @Test
    public void closestIntersectionToStartOfLine2() {
        Rectangle newRec = new Rectangle(new Point(1.0, 1.0), 3.0, 3.0);
        Line newLine = new Line(new Point(0.0, 0.0), new Point(5.0, 5.0));
        Point expectedClosestIntersection = new Point(1.0, 1.0);
        Point checkedClosestIntersection = newLine.closestIntersectionToStartOfLine(newRec);
        Assert.assertEquals(expectedClosestIntersection.getX(), checkedClosestIntersection.getX(), 1e-9);
        Assert.assertEquals(expectedClosestIntersection.getY(), checkedClosestIntersection.getY(), 1e-9);
    }

    @Test
    public void closestIntersectionSimpleTest3() {
        Rectangle rect = new Rectangle(new Point(1,1), 2, 2);
        Line line = new Line(new Point(0,0), new Point(5,5));
        Point closest = line.closestIntersectionToStartOfLine(rect);
        Assert.assertEquals(1.0, closest.getX(), 1e-9);
        Assert.assertEquals(1.0, closest.getY(), 1e-9);
    }
}
