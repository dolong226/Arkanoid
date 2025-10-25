package ball;

import game.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import collidable.CollisionInfo;
import game.GameEnvironment;
import geometry.*;

/**
 * File này định nghĩa về bóng, làm thế nào để tạo ra bóng, thay đổi vận tốc bóng trước khi có va chạm và update trạng thái bóng sau 1 khoảng thời gian
 */
public class Ball implements Sprite {
    /**
     * Bán kính bóng
     */
    private double radius;
    /**
     * Tâm bóng
     */
    private Point center;
    /**
     * Màu bóng
     */
    private Color color;
    /**
     * Vận tốc bóng
     */
    private Velocity velocity;
    /**
     * Quản lý môi trường va chạm
     */
    private GameEnvironment gameEnvironment;

    /**
     * Khởi tạo bóng
     * @param center tâm bóng
     * @param radius bán kính
     * @param color màu bóng
     * @param velocity vận tốc
     * @param gameEnvironment môi trường quản lý va chạm
     */
    public Ball(Point center, double radius, Color color, Velocity velocity, GameEnvironment gameEnvironment){
        this.radius = radius;
        this.center = center;
        this.velocity = velocity;
        this.color = color;
        this.gameEnvironment = gameEnvironment;
    }

    @Override
    /**
     * Vẽ hình ảnh bóng
     */
    public void render(GraphicsContext gc) {
        gc.setFill(color);
        double upperLeftX = center.getX() - radius;
        double upperLeftY = center.getY() - radius;
        double length = radius*2;
        double width = radius*2;
        gc.fillOval(upperLeftX, upperLeftY, width, length);
    }

    @Override
    /**
     * Update lại trạng thái bóng sau các sự kiện xảy ra
     */
    public void update(double dt) {
        Velocity currentVelocity = this.getVelocity();
        double x = this.center.getX();
        double y = this.center.getY();
        Line pathBall = new Line(new Point(x,y), new Point(x + currentVelocity.getDx()*dt, y + currentVelocity.getDy()*dt));
        CollisionInfo collisionInfo = this.gameEnvironment.getClosetCollision(pathBall);

        /**
         * Trường hợp 1: nếu có va chạm thì sẽ đặt lại vị trí bóng, cập nhật vận tốc mới cho bóng
         */
        if(collisionInfo != null){
            this.center = changeDirectionIfMoveNear();
            currentVelocity = collisionInfo.getCollidable().hit(this, collisionInfo.getClosetPoint() , currentVelocity);
            this.velocity = currentVelocity;
            this.center = this.velocity.applyToPoint(this.center);
        }

        /**
         * Trường hợp 2: không có va chạm thì tiếp tục di chuyển quãng đường bằng vận tốc hiện tại nhân với delta thời gian
         */
        else{
            this.center = new Point(x + currentVelocity.getDx()*dt, y + currentVelocity.getDy()*dt);
        }
    }
    
    /**
     * Hàm set giá trị vận tốc theo 2 hướng dx, dy
     * @param dx vận tốc theo hướng dx
     * @param dy vận tốc theo hướng dy
     */
    public void setVelocity(double dx, double dy){
        this.velocity = new Velocity(dx, dy);
    }

    /**
     * Hàm set giá trị vận tốc bằng 1 vận tốc cho trước
     * @param v vận tốc
     */
    public void setVelocity(Velocity v){
        this.velocity = v;
    }

    /**
     * Hàm trả về vận tốc
     * @return vận tốc
     */
    public Velocity getVelocity(){
        return this.velocity;
    }

    /**
     * Hàm trả về tâm bóng
     * @return tâm bóng
     */
    public Point getCenter(){
        return this.center;
    }

    /**
     * Hàm thay đổi vị trí bóng trước khi xảy ra va chạm bằng cách đổi hướng, lùi bóng lại 1 đơn vị
     * @return vị trí mới của bóng
     */
    public Point changeDirectionIfMoveNear(){
        double dxCenter = this.center.getX();
        double dyCenter = this.center.getY();
        if(this.getVelocity().getDx() > 0){
            dxCenter = dxCenter - 1;
        }
        if(this.getVelocity().getDx() < 0){
            dxCenter = dxCenter + 1;
        }
        if(this.getVelocity().getDy() > 0){
            dyCenter = dyCenter - 1;
        }
        if(this.getVelocity().getDy() < 0){
            dyCenter = dyCenter + 1;
        }
        return new Point(dxCenter, dyCenter);
    }
}
