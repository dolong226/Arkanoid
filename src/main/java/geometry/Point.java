package geometry;

/**
 * Lớp này định nghĩa 1 điểm trong hệ tọa độ và tính khoảng cách giữa 2 điểm.
 */
public class Point {
    /**
     * Hai tọa độ x, y của 1 điểm.
     */
    private double x;
    private double y;

    /**
     * Khởi tạo 1 điểm với giá trị mặc định là 0.
     */
    public Point() {}
    
    /**
     * Khởi tạo 1 điểm với giá trị tọa độ x và y cho trước.
     * @param x tọa độ x.
     * @param y tọa độ y.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Trả về tọa độ X của 1 điểm.
     * @return tọa độ X.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Trả về tọa độ Y của 1 điểm.
     * @return tọa độ Y.
     */
    public double getY() {
        return this.y;
    }
    
    /**
     * Khởi tạo giá trị tọa độ X.
     * @param x giá trị tọa độ X.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Khởi tạo giá trị tọa độ Y.
     * @param y giá trị tọa độ Y.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Tính khoảng cách giữa 2 điểm.
     *
     * @param other Điểm còn lại.
     * @return khoảng cách.
     */
    public double distance(Point other) {
        double distance = Math.sqrt((x - other.getX()) * (x - other.getX()) + (y - other.getY()) * (y - other.getY()));
        return distance;
    }

    /**
     * Set vị trí tọa độ của điểm.
     * @param x Tọa độ x.
     * @param y Tọa độ y.
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Trả về vị trí điểm hiện tại.
     * @return Điểm hiện tại.
     */
    public Point getLocation() {
        return new Point(x,y);
    }
}
