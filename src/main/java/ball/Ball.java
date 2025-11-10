
package ball;

import collidable.Block;
import collidable.Collidable;
import game.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import collidable.CollisionInfo;
import game.GameEnvironment;
import geometry.*;
import ui.ImageLoad;

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

    private Image ballImage;

    /**
     * Khởi tạo bóng
     * @param center tâm bóng
     * @param radius bán kính
     * @param color màu bóng
     * @param velocity vận tốc
     * @param gameEnvironment môi trường quản lý va chạm
     */
    public Ball(Point center, double radius, Color color, Velocity velocity, GameEnvironment gameEnvironment){
        this.baseRadius = radius;
        this.radius = radius;
        this.center = center;
        this.velocity = velocity;
        this.color = color;
        this.gameEnvironment = gameEnvironment;
        this.type = BallType.NORMAL;
        loadBallImage();
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
        loadBallImage();
    }

    @Override
    /**
     * Vẽ hình ảnh bóng
     */
    public void render(GraphicsContext gc) {
        gc.save();
        if (ballImage != null) {
            double size = radius * 2;
            gc.drawImage(ballImage, center.getX() - radius, center.getY() - radius, size, size);
        } else {
            // fallback: vẽ hình tròn nếu ảnh lỗi
            gc.setFill(color);
            gc.fillOval(center.getX() - radius, center.getY() - radius, radius * 2, radius * 2);
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeOval(center.getX() - radius, center.getY() - radius, radius * 2, radius * 2);
        }

        gc.restore();
    }
    private void loadBallImage() {
        String path;
        switch (type) {
            case NORMAL:
                path = "/Sprite/58-Breakout-Tiles.png";
                break;
            case FIRE:
                path = "/Default/ball_red_large.png";
                break;
            case BIG:
                path = "/Sprite/58-Breakout-Tiles.png";
                break;
            default:
                path = "/Sprite/58-Breakout-Tiles.png";
        }
        this.ballImage = ImageLoad.load(path);

        // debug
        if (this.ballImage == null) {
            System.err.println("Không load được ảnh bóng: " + path + " cho type: " + type);
        }
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
                Velocity newVelocity = obj.hit(this, p, velocity);
                this.velocity = newVelocity;

                double speed = Math.hypot(newVelocity.getDx(), newVelocity.getDy());
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
        loadBallImage();
    }

    public void updateBallType(){
        if (this.type == null) {
            this.type = BallType.NORMAL;
        }
        this.radius = baseRadius * this.type.getSizeBallMultiplier();
        this.color = this.type.getColor();
        loadBallImage();
    }

    public boolean canPenetrate(){
        return this.type.getPenetration();
    }

}