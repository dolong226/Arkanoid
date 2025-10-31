package collidable;
import java.util.ArrayList;
import java.util.List;

import ball.Ball;
import ball.Velocity;
import com.sun.org.apache.xerces.internal.impl.dv.xs.BooleanDV;
import geometry.*;
import game.Sprite;
import listener.HitListener;

import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

/**
 * Class này định nghĩa về thanh paddle (người chơi thao tác với game), thao tác di chuyển thanh paddle, tính toán vận tốc khi bóng đập vào thanh 
 */
public class Paddle implements Sprite, Collidable {
    /**
     * Vận tốc thanh
     */
    private int step;
    /**
     * Màu thanh
     */
    private Color color;
    /**
     * Hình chữ nhật khung của thanh
     */
    private Rectangle paddle;
    /**
     * Giới hạn min màn hình game
     */
    private double minBoundary;
    /**
     * Giới hạn max màn hình game
     */
    private double maxBoundary;
    /**
     * Các vị trí đập bóng trên thanh paddle
     */
    private double[] regionBorders;

    public Paddle() {
        this.step = 1;
        this.color = new Color(100, 100, 100, 50);
        this.paddle = new Rectangle(new Point(200, 400), 100, 100);
    }

    /**
     * Khởi tạo paddle
     * @param step vận tốc
     * @param color màu
     * @param paddle khung 
     * @param minBound giới hạn min cửa sổ
     * @param maxBound giới hạn max cửa sổ
     */


    public Paddle(int step, Color color, Rectangle paddle, double minBound, double maxBound){
        this.step = step;
        this.color = color;
        this.paddle = paddle;
        this.minBoundary = minBound;
        this.maxBoundary = maxBound;
        this.regionBorders = new double[4];
    }

    public double getX() {
        return paddle.getUpperLeft().getX();
    }

    public double getY() {
        return paddle.getUpperLeft().getY();
    }

    public double getWidth() {
        return paddle.getWidth();
    }
    // Dùng để đặt lại vị trí paddle
    public void setX(double x) {
        Point oldUpperLeft = paddle.getUpperLeft();
        paddle = new Rectangle(new Point(x, oldUpperLeft.getY()), paddle.getWidth(), paddle.getLength());
    }

    /** 
     * Di chuyển paddle sang trái
     * @param dt thời gian
     */
    public void moveLeft(double dt){
        double newX = this.paddle.getUpperLeft().getX() - this.step*dt;
        if(newX <= this.minBoundary){
            newX = this.minBoundary;
        }
        this.paddle = new Rectangle(new Point(newX, this.paddle.getUpperLeft().getY()), this.paddle.getWidth(), this.paddle.getLength());
    }

    /** 
     * Di chuyển paddle sang phải
     * @param dt thời gian
     */
    public void moveRight(double dt){
        double newX = this.paddle.getUpperLeft().getX() + this.step*dt;
        if(newX + this.paddle.getLength() >= this.maxBoundary){
            newX = this.maxBoundary - this.paddle.getLength();
        }
        this.paddle = new Rectangle(new Point(newX, this.paddle.getUpperLeft().getY()), this.paddle.getWidth(), this.paddle.getLength());
    }

    /** 
     * Trả về thanh paddle
     * @return Rectangle
     */
    public Rectangle getCollisionRectangle(){
        return this.paddle;
    }

    /** 
     * Vẽ lại thanh paddle 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        gc.setFill(color);
        double upperLeftX = this.paddle.getUpperLeft().getX();
        double upperLeftY = this.paddle.getUpperLeft().getY();
        double length = this.paddle.getLength();
        double width = this.paddle.getWidth();
        gc.fillRect(upperLeftX, upperLeftY, length,width);
    }

    /**
     * Cập nhật trạng thái thanh paddle sau các sự kiện
     */
    public void update(double dt){}

    /** 
     * Tính vận tốc bóng sau khi va chạm với paddle
     * @param hitter Bóng
     * @param collisionPoint Điểm va chạm
     * @param currentVelocity Vận tốc hiện tại của bóng
     * @return Velocity Vận tốc mới bóng
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity){
        Velocity upDateVelocity = new Velocity(currentVelocity.getDx(), currentVelocity.getDy());
        double x = collisionPoint.getX();
        double y = collisionPoint.getY();

        double epsilon = 0.0001;

        double upperLeftX = this.paddle.getUpperLeft().getX();
        double upperLeftY = this.paddle.getUpperLeft().getY();

        if(Math.abs(y - upperLeftY) < epsilon && collisionPoint.getX() >= upperLeftX && collisionPoint.getX() <= upperLeftX + this.paddle.getLength()){
            int region = this.checkRegion(collisionPoint);
            upDateVelocity = this.changeVelocity(region, upDateVelocity);
            return upDateVelocity;
        }
        if(Math.abs(x - upperLeftX) < epsilon || Math.abs(x - (upperLeftX + this.paddle.getLength())) < epsilon){
            if(y > upperLeftY && y < upperLeftY + this.paddle.getWidth()){
                upDateVelocity.setDx(upDateVelocity.getDx() *(-1));
            }
            else{
                upDateVelocity.setDx(upDateVelocity.getDx() * (-1));
                upDateVelocity.setDy(upDateVelocity.getDy() * (-1));
            }
        }
        return upDateVelocity;
    }

    /** 
     * Kiểm tra xem vị trí va chạm nằm trên khoảng nào của paddle
     * @param collisionPoint Điểm va chạm
     * @return int Vị trí
     */
    public int checkRegion(Point collisionPoint){
        double UpperLeftX = this.paddle.getUpperLeft().getX();
        double eachRegionSize = this.paddle.getLength()/5;
        regionBorders[0] = UpperLeftX + eachRegionSize;
        regionBorders[1] = UpperLeftX + eachRegionSize * 2;
        regionBorders[2] = UpperLeftX + eachRegionSize * 3;
        regionBorders[3] = UpperLeftX + eachRegionSize * 4;
        if(collisionPoint.getX() >= UpperLeftX && collisionPoint.getX() < this.regionBorders[0]){
            return 1;
        }
        if(collisionPoint.getX() >= this.regionBorders[0] && collisionPoint.getX() < this.regionBorders[1]){
            return 2;
        }
        if(collisionPoint.getX() >= this.regionBorders[1] && collisionPoint.getX() < this.regionBorders[2]){
            return 3;
        }
        if(collisionPoint.getX() >= this.regionBorders[2] && collisionPoint.getX() < this.regionBorders[3]){
            return 4;
        }
        if(collisionPoint.getX() >= this.regionBorders[3] && collisionPoint.getX() <= UpperLeftX + this.paddle.getLength()){
            return 5;
        }
        return 0;
    }
    
    /** 
     * Tính vận tốc tương ứng trả về cho bóng với mỗi vị trí va chạm thuộc 1 khoảng trên paddle
     * @param currenVelocity vận tốc hiện tại
     * @param velo Mảng chứa các vận tốc mới tương ứng với các vị trí trên paddle
     */
    public void differentVelocity(Velocity currenVelocity, Velocity[] velo){
        double speed = Math.sqrt((currenVelocity.getDx()*currenVelocity.getDx()) + currenVelocity.getDy()*currenVelocity.getDy());
        double dx = currenVelocity.getDx();
        double dy = currenVelocity.getDy();
        velo[0] = new Velocity(dx, dy);
        velo[3] = new Velocity(dx, -dy);
        velo[1] = Velocity.fromAngleAndSpeed(-60.0, speed);
        velo[5] = Velocity.fromAngleAndSpeed(60.0, speed);
        velo[4] = Velocity.fromAngleAndSpeed(30.0, speed);
        velo[2] = Velocity.fromAngleAndSpeed(-30.0, speed);
    }

    /** 
     * Thay đổi vận tốc bóng tương ứng với các vùng trên paddle
     * @param region Vùng
     * @param currenVelocity Vận tốc hiện tại
     * @return Velocity Vận tốc mới
     */
    public Velocity changeVelocity(int region, Velocity currenVelocity){
        Velocity[] differentVelo = new Velocity[6];
        differentVelocity(currenVelocity, differentVelo);
        Velocity newVelo = differentVelo[region];
        return newVelo;
    }
}
