package geometry;
/**
 * Lớp này định nghĩa và khởi tạo 1 đường thẳng và những phương thức liên quan đến đường thẳng như giao điểm.
 */
public class Line {
    /**
     * Điểm đầu. 
     */
    private Point start;
    /**
     * Điểm cuối.
     */
    private Point end;
    /**
     * Hằng số để so sánh xem hai giá trị double có bằng nhau không.
     */
    private static final double EPSILON = 1E-5;
    
    /**
     * Khởi tạo 1 đường thẳng qua 2 điểm đầu cuối.
     * @param start Điểm đầu.
     * @param end Điểm cuối.
     */
    public Line(Point start, Point end){
        this.start = start;
        this.end = end;
    }

    /**
     * Khởi tạo 1 đường thẳng thông qua hai cặp tọa độ x,y.
     * @param x1 Tọa độ x của điểm đầu.
     * @param y1 Tọa độ y của điểm đầu.
     * @param x2 Tọa độ x của điểm cuối.
     * @param y2 Tọa độ y của điểm cuối.
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
    }

    /**
     * Trả về điểm đầu của đường thẳng.
     * @return Điểm đầu.
     */
    public Point getStart(){
        return this.start;
    }

    /**
     * Trả về điểm cuối của đường thẳng.
     * @return Điểm cuối.
     */
    public Point getEnd(){
        return this.end;
    }

    /**
     * Hàm kiểm tra sự giao nhau của 2 đường thẳng.
     * @param other Đường thẳng khác.
     * @return true hoặc false - liệu rằng hai đường thẳng có giao nhau hay không.
     */
    public boolean isIntersecting(Line other){
        double x1 = start.getX();
        double x2 = end.getX();
        double x3 = other.start.getX();
        double x4 = other.end.getX();
        double y1 = start.getY();
        double y2 = end.getY();
        double y3 = other.start.getY();
        double y4 = other.end.getY();
        //Trường hợp 1: Hai đường thẳng đều là đường thẳng đứng x = ...
        if(Math.abs(x1 - x2) < EPSILON && Math.abs(x3 - x4) < EPSILON){
            if(x1 != x3){
                return false;
            }
            if(Math.min(y1, y2) < Math.min(y3, y4)){
                if(Math.max(y1, y2) > Math.min(y3, y4)){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                if(Math.max(y3, y4) > Math.min(y1, y2)){
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        // Trường hợp 2: Một trong 2 đường là đường thẳng đứng.
        if(Math.abs(x1 - x2) < EPSILON){
            double slope2 = (y3 - y4)/(x3 - x4);
            double y_intercept_2 = y3 - slope2*x3;
            double y_result = slope2*x1 + y_intercept_2;
            if(y_result >= Math.min(y1, y2) && y_result <= Math.max(y1, y2) && x1 >= Math.min(x3, x4) && x1 <= Math.max(x3, x4)){
                return true;
            }
            else{
                return false;
            }
        }
        if(Math.abs(x3 - x4) < EPSILON){
            double slope1 = (y1 - y2)/(x1 - x2);
            double y_intercept_1 = y1 -slope1*x1;
            double y_result = slope1*x3 + y_intercept_1;
            if(y_result >= Math.min(y3, y4) && y_result <= Math.max(y3, y4) && x3 >= Math.min(x1, x2) && x3 <= Math.max(x1, x2)){
                return true;
            }
            else{
                return false;
            }
        }
        // Trường hợp 3: Tình huống còn lại trừ 2 trường hợp trên.
        double slope1 = (y1 - y2)/(x1 - x2);
        double slope2 = (y3 - y4)/(x3 - x4);
        double y_intercept_1 = y1 -slope1*x1;
        double y_intercept_2 = y3 - slope2*x3;
        if(Math.abs(slope1 - slope2) < EPSILON){
            if(Math.abs(y_intercept_1 - y_intercept_2) < EPSILON){
                double min1 = Math.min(x1, x2);
                double max1 = Math.max(x1, x2);
                double min2 = Math.min(x3, x4);
                double max2 = Math.max(x3, x4);
                if(max1 >= min2 && max2 >= min1){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        double intersectionPoint = (y_intercept_1 - y_intercept_2)/(slope2 - slope1);
        if(Math.min(x1, x2) <= intersectionPoint && Math.max(x1, x2) >= intersectionPoint && Math.min(x3, x4) <= intersectionPoint && Math.max(x3, x4) >= intersectionPoint){
            return true;
        }
        return false;
    }

    /**
     * Hàm tìm giao điểm của hai đường thẳng nếu chúng giao nhau, trả về null nếu chúng không giao nhau.
     * @param other Đường thẳng khác.
     * @return Giao điểm của 2 đường thẳng hoặc null nếu không giao nhau.
     */
    public Point intersectionPoint(Line other){
        // Trường hợp 1: không giao nhau --> return null.
        if(isIntersecting(other) != true){
            return null;
        }
        // Trường hợp 2: có thể giao nhau .
        else{
            double x1 = start.getX();
            double x2 = end.getX();
            double x3 = other.start.getX();
            double x4 = other.end.getX();
            double y1 = start.getY();
            double y2 = end.getY();
            double y3 = other.start.getY();
            double y4 = other.end.getY();
            // Hai đường đều là đường thẳng đứng nên không thể có 1 giao điểm được (vì có thể không có giao điểm nào hoặc có vô số giao điểm).
            if(Math.abs(x1 - x2) < EPSILON && Math.abs(x3 - x4) < EPSILON){
                return null;
            }
            //Một trong hai đường thẳng là đường thẳng đứng.
            else if(Math.abs(x1 - x2) < EPSILON || Math.abs(x3 - x4) < EPSILON){
                 if(Math.abs(x1 - x2) < EPSILON){ // Đường thẳng 1 là đường thẳng đứng.
                    double slope2 = (y3 - y4)/(x3 - x4);
                    double y_intercept_2 = y3 - slope2*x3;
                    double y_result = slope2*x1 + y_intercept_2;
                    if(y_result >= Math.min(y1, y2) && y_result <= Math.max(y1, y2) && x1 >= Math.min(x3, x4) && x1 <= Math.max(x3, x4)){
                        Point intersectionPoint = new Point();
                        intersectionPoint.setX(x1);
                        intersectionPoint.setY(y_result);
                        return intersectionPoint;
                    }
                    else{
                        return null;
                    }
                 }
                 else{ // Đường thẳng 2 là đường thẳng đứng.
                    double slope1 = (y1 - y2)/(x1 - x2);
                    double y_intercept_1 =  y1 -slope1*x1;
                    double y_result = slope1*x3 + y_intercept_1;
                    if(y_result >= Math.min(y3, y4) && y_result <= Math.max(y3, y4) && x3 >= Math.min(x1, x2) && x3 <= Math.max(x1, x2)){
                        Point intersectionPoint = new Point();
                        intersectionPoint.setX(x3);
                        intersectionPoint.setY(y_result);
                        return intersectionPoint;
                    }
                 } 
            }
            // Cả 2 đều là các trường hợp ngoài các trường hợp trên.
            else{
                double slope1 = (y1 - y2)/(x1 - x2);
                double slope2 = (y3 - y4)/(x3 - x4);
                if(Math.abs(slope1 - slope2) < EPSILON){
                    return null;
                }
            }
        }
        double x1 = start.getX();
        double x2 = end.getX();
        double x3 = other.start.getX();
        double x4 = other.end.getX();
        double y1 = start.getY();
        double y2 = end.getY();
        double y3 = other.start.getY();
        double y4 = other.end.getY();
        double slope1 = (y1 - y2)/(x1 - x2);
        double slope2 = (y3 - y4)/(x3 - x4);
        double y_intercept_1 =  y1 -slope1*x1;
        double y_intercept_2 = y3 - slope2*x3;
        Point intersectionPoint = new Point();
        double x_value = (y_intercept_1 - y_intercept_2)/(slope2 - slope1);
        intersectionPoint.setX(x_value);
        intersectionPoint.setY(slope1*x_value + y_intercept_1);
        return intersectionPoint;
}

    /**
     * Nếu mà 1 đường thẳng không giao với bất kì cạnh nào hình chữ nhật thì sẽ return null, còn nếu có giao với ạnh hình chữ nhật thì sẽ return giao điểm cần với điểm đầu của đường thẳng nhất.
     * @param rectangle Hình chữ nhật.
     * @return null hoặc giao điểm gần với điểm đầu của đường thẳng.
     */
    public Point closestIntersectionToStartOfLine(Rectangle rectangle){
        Line[] sidesOfRectangle = new Line[4];
        Point[] intersectionPoints = new Point[4];
        double minDistance = 0;
        boolean isIntersecting = false;

        sidesOfRectangle[0] = new Line(rectangle.getUpperLeft().getX(), rectangle.getUpperLeft().getY(), rectangle.getUpperLeft().getX() + rectangle.getLength(), rectangle.getUpperLeft().getY());
        sidesOfRectangle[1] = new Line(rectangle.getUpperLeft().getX(), rectangle.getUpperLeft().getY() + rectangle.getWidth(), rectangle.getUpperLeft().getX() + rectangle.getLength(), rectangle.getUpperLeft().getY() + rectangle.getWidth());
        sidesOfRectangle[2] = new Line(rectangle.getUpperLeft().getX(), rectangle.getUpperLeft().getY(), rectangle.getUpperLeft().getX(), rectangle.getUpperLeft().getY() + rectangle.getWidth());
        sidesOfRectangle[3] = new Line(rectangle.getUpperLeft().getX() + rectangle.getLength(), rectangle.getUpperLeft().getY(), rectangle.getUpperLeft().getX() + rectangle.getLength(), rectangle.getUpperLeft().getY() + rectangle.getWidth());

        for(int i = 0; i <= 3; i++){
            if(isIntersecting(sidesOfRectangle[i])){
                intersectionPoints[i] = new Point();
                intersectionPoints[i] = intersectionPoint(sidesOfRectangle[i]);
                isIntersecting = true;
            }
        }
        Point pointClosestToStartOfLine = null;
        boolean check = true;
        if(isIntersecting == true){
            for(int i = 0; i <= 3; i++){
                if(intersectionPoints[i] == null){
                    continue;
                }
                else{
                    if(check == true){
                        minDistance = Math.sqrt((start.getX() - intersectionPoints[i].getX())*(start.getX() - intersectionPoints[i].getX()) + (start.getY() - intersectionPoints[i].getY())*(start.getY() - intersectionPoints[i].getY()));
                        pointClosestToStartOfLine = intersectionPoints[i];
                        check = false;
                    }
                    else{
                        if(Math.sqrt((start.getX() - intersectionPoints[i].getX())*(start.getX() - intersectionPoints[i].getX()) + (start.getY() - intersectionPoints[i].getY())*(start.getY() - intersectionPoints[i].getY())) < minDistance){
                            pointClosestToStartOfLine = intersectionPoints[i];
                        }
                    }
                }
            }
            return pointClosestToStartOfLine;
        }
        return null;
    }
}
