package ball;
import geometry.Point;
/**
 * Class này định nghĩa về vận tốc của bóng khi di chuyển, tính toán vị trí tiếp theo của bóng khi di chuyển
 */
public class Velocity {
    /**
     * Vận tốc tính theo chiều trục x
     */
    private double Dx;
    /**
     * Vận tốc tính theo chiều trục y
     */
    private double Dy;

    /**
     * Khởi tạo vận tốc
     * @param dx vận tốc theo chiều x
     * @param dy vận tốc theo chiều y
     */
    public Velocity(double dx, double dy){
        this.Dx = dx;
        this.Dy = dy;
    }

    /**
     * Hàm này tính toán và trả về vận tốc theo chiều x, y tương ứng với hướng bóng đang di chuyển, dựa trên góc của đường bóng đi tạo với trục y hướng lên  
     * @param angle Góc của đường bóng đi so với trục y hướng lên
     * @param speed Tốc độ của bóng
     * @return
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed){
        /**
         * Nếu góc nhỏ hơn không (âm)
         */
        while(angle < 0){
            angle += 360;
        }
        /**
         * Nếu góc quá 360 độ
         */
        while(angle > 360){
            angle -= 360;
        }

        /**
         * Nếu góc từ khoảng 0 đến 90 độ --> bóng đang đi theo chiều giảm của y và chiều tăng x (lên trên sang phải)
         */
        if(angle >=0  && angle <= 90){
            angle = Math.toRadians(angle);
            double dx = speed * Math.sin(angle);
            double dy = -speed * Math.cos(angle);
            return new Velocity(dx, dy);
        }

        /**
         * Nếu góc từ 90 đến 180 độ --> bóng đang đi theo chiều tăng của y và chiều tăng của x (xuống dưới sang phải)
         */
        else if(angle > 90 && angle <= 180){
            angle = angle - 90;
            angle = Math.toRadians(angle);
            double dx = speed * Math.cos(angle);
            double dy = speed * Math.sin(angle);
            return new Velocity(dx, dy);
        }

        /**
         * Nếu góc từ 180 đến 270 độ --> bóng đang đi theo chiều tăng của y và chiều giảm của x (xuống dưới sang trái)
         */
        else if(angle > 180 && angle <= 270){
             angle = angle - 180;
             angle = Math.toRadians(angle);
             double dx = -speed * Math.sin(angle);
             double dy = speed * Math.cos(angle);
             return new Velocity(dx, dy); 
        }

        /**
         * Nếu góc từ 270 đến 360 độ --> bóng đang đi theo chiều giảm của y và chiều giảm của x (lên trên sang trái)
         */
        else{
            angle = angle - 270;
            angle = Math.toRadians(angle);
            double dx = -speed * Math.cos(angle);
            double dy = -speed * Math.sin(angle);
            return new Velocity(dx, dy);
        }
    }

    /**
     * Hàm này đưa ra vị trí bóng tiếp theo sau khi cập nhật vận tốc
     * @param onePoint điểm hiện tại
     * @return điểm mới ở vị trí mới
     */
    public Point applyToPoint(Point onePoint){
        return new Point(onePoint.getX() + Dx, onePoint.getY() + Dy);
    }

    /**
     * Trả về giá trị vận tốc theo x
     * @return vận tốc theo x
     */
    public double getDx(){
        return this.Dx;
    }

    /**
     * Trả về giá trị vận tốc theo y
     * @return vận tốc theo y
     */
    public double getDy(){
        return this.Dy;
    }

    /**
     * Set giá trị vận tốc theo x
     * @param dx vận tốc theo x
     */
    public void setDx(double dx){
        this.Dx = dx;
    }

    /**
     * Set giá trị vận tốc theo y
     * @param dy vận tốc theo y
     */
    public void setDy(double dy){
        this.Dy = dy;
    }
}
