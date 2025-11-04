package geometry;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này định nghĩa các thuộc tính và phương thức liên quan tới hình chữ nhật (đóng vai trò là blocks sau này).
 */
public class Rectangle {
    /**
     * Các thuộc tính của 1 hình chữ nhật gồm điểm trên cùng bên trái, chiều dài, chiều rộng.
     */
    private double width;
    private double height;
    private Point upperLeft;

    /**
     * Hàm khởi tạo 1 hình chữ nhật.
     * @param upperLeft Điểm trên cùng bên trái.
     * @param width Chiều rộng.
     * @param height Chiều dài.
     */
    public Rectangle(Point upperLeft, double width, double height){
        this.width = width;
        this.height = height;
        this.upperLeft = upperLeft;
    }

    /**
     * Lấy ra điểm trên cùng bên trái.
     * @return điểm trên cùng bên trái.
     */
    public Point getUpperLeft(){
        return this.upperLeft;
    }

    /**
     * Lấy ra chiều rộng.
     * @return chiều rộng.
     */
    public double getWidth(){
        return this.width;
    }

    /**
     * Lấy ra chiều dài.
     * @return chiều dài.
     */
    public double getHeight(){
        return this.height;
    }
    
    /**
     * Tính toán tọa độ của 4 góc hình chữ nhật.
     * @param corners Mảng gồm 4 góc hình chữ nhật.
     */
    public void rectangle4Corners(Point[] corners){
        double x_upperLeft = this.upperLeft.getX();
        double y_upperLeft = this.upperLeft.getY();
        double theWidth = this.width;
        double theHeight = this.height;
        corners[0] = this.getUpperLeft();

        Point upperRight = new Point();
        upperRight.setX(x_upperLeft + theHeight);
        upperRight.setY(y_upperLeft);
        corners[1] = upperRight;

        Point downLeft = new Point();
        downLeft.setX(x_upperLeft);
        downLeft.setY(y_upperLeft + theWidth);
        corners[2] = downLeft;

        Point downRight = new Point();
        downRight.setX(x_upperLeft + theHeight);
        downRight.setY(y_upperLeft + theWidth);
        corners[3] = downRight;
    }
    
    /**
     * Khởi tạo 4 cạnh hình chữ nhật từ 4 đỉnh của hình đó.
     * @param sides Mảng chứa 4 cạnh bắt đầu từ cạnh dài trên cùng, cạnh dài dưới cùng, cạnh rộng bên trái, cạnh rộng bên phải.
     * @param corners Mảng chứa tọa độ 4 góc.
     */
    public void rectangle4Sides(Line[] sides, Point[] corners){
        sides[0] = new Line(corners[0], corners[1]);
        sides[1] = new Line(corners[2], corners[3]);
        sides[2] = new Line(corners[0], corners[2]);
        sides[3] = new Line(corners[1], corners[3]);
    }

    /**
     * Hàm để tìm các giao điểm của 1 đường thẳng với 1 hình chữ nhật.
     * @param line đường thẳng.
     * @return Danh sach gồm các giao điểm.
     */
    public java.util.List<Point> intersectionPoints(Line line){
        List<Point> intersectionPointList = new ArrayList<Point>();
        Point[] corners = new Point[4];
        Line[] sides = new Line[4];
        rectangle4Corners(corners);
        rectangle4Sides(sides, corners);
        for(int i = 0; i <= 3; i++){
            Point intersectionPoint = line.intersectionPoint(sides[i]);
            if(intersectionPoint != null){
                intersectionPointList.add(intersectionPoint);
            }
        }
        return intersectionPointList;
    }

    // Kiểm tra xem một điểm có nằm trong hình chữ nhật hay không.
    public boolean contains(Point point) {
        double x = point.getX();
        double y = point.getY();

        double xLeft = upperLeft.getX();
        double yTop = upperLeft.getY();
        double xRight = xLeft + width;
        double yBottom = yTop + height;

        return x >= xLeft && x <= xRight && y >= yTop && y <= yBottom;
    }

    /**
     * Kiểm tra xem hình chữ nhật này có giao với hình chữ nhật khác không
     */
    public boolean intersects(Rectangle other) {
        double thisX = upperLeft.getX();
        double thisY = upperLeft.getY();
        double thisRight = thisX + height;
        double thisBottom = thisY + width;

        double otherX = other.getUpperLeft().getX();
        double otherY = other.getUpperLeft().getY();
        double otherRight = otherX + other.getHeight();
        double otherBottom = otherY + other.getWidth();

        return !(thisRight < otherX || otherRight < thisX ||
                thisBottom < otherY || otherBottom < thisY);
    }

}
