
package ball;

import collidable.Block;
import collidable.Collidable;
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
     * Bán kính gốc
     */
    private double baseRadius;
    /**
     * Màu bóng
     */
    private Color color;
    /**
     * Vận tốc bóng
     */
    private Velocity velocity;
    /**
     * Loại bóng.
     */
    private BallType type;
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
        this.type = BallType.NORMAL;
    }

    /**
     * Khởi tạo bóng cụ thể
     * @param center tâm bóng
     * @param radius bán kính
     * @param color màu bóng
     * @param velocity vận tốc
     * @param gameEnvironment môi trường quản lý va chạm
     * @param type loại bóng
     */
    public Ball(Point center, double radius, Color color, Velocity velocity, GameEnvironment gameEnvironment, BallType type){
        this.baseRadius = radius;
        this.radius = radius;
        this.center = center;
        this.velocity = velocity;
        this.color = color;
        this.gameEnvironment = gameEnvironment;
        this.type = type;
        updateBallType();
    }

    @Override
    /**
     * Vẽ hình ảnh bóng
     */
    public void render(GraphicsContext gc) {
        gc.save();
        gc.setFill(color);
        double upperLeftX = center.getX() - radius;
        double upperLeftY = center.getY() - radius;
        double length = radius*2;
        double width = radius*2;
        gc.fillOval(upperLeftX, upperLeftY, width, length);
        gc.restore();
    }

    @Override
    /**
     * Update lại trạng thái bóng sau các sự kiện xảy ra
     */
    public void update(double dt) {
        Point intended = new Point(
                center.getX() + velocity.getDx() * dt,
                center.getY() + velocity.getDy() * dt
        );

        Line path = new Line(center, intended);
        CollisionInfo collision = gameEnvironment.getClosetCollision(path);

        if (collision != null) {
            Point p = collision.getClosetPoint();

            Collidable obj = collision.getCollidable();
            boolean shouldPenetrate = canPenetrate() && obj instanceof Block && !((Block) obj).isDeathRegion();

            if(shouldPenetrate){
                obj.hit(this, p, velocity);
                this.center = intended;
            }
            else{
                this.velocity = obj.hit(this, p, velocity);
                double speed = Math.hypot(velocity.getDx(), velocity.getDy());
                if (speed > 0) {
                    double offset = 1.0;
                    this.center = new Point(
                        p.getX() + (velocity.getDx() / speed) * offset,
                        p.getY() + (velocity.getDy() / speed) * offset
                    );
                } else {
                    this.center = p;
                }
            }
        } else {
            this.center = velocity.applyToPoint(center, dt);
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
     * Trả về loại bóng
     * @return Loại bóng
     */
    public BallType getType(){
        return this.type;
    }

    public void setType(BallType newType) {
        this.type = newType;
        updateBallType();
    }

    public void updateBallType(){
        this.radius = baseRadius * this.type.getSizeBallMultiplier();
        this.color = this.type.getColor();
    }

    public boolean canPenetrate(){
        return this.type.getPenetration();
    }

}