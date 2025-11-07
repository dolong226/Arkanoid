package collidable;

import java.util.List;


import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import game.Sprite;
import javafx.scene.canvas.GraphicsContext;
import listener.HitNotifier;
import ball.*;
import geometry.*;
import listener.HitEvent;
import listener.HitListener;
import powerup.PowerUp;
import ui.ImageLoader;

/**
 * Hàm này định nghĩa về block (khối hình) trong trò chơi, cập nhật khối hình sau sự kiện, tính toán vận tốc của bóng sau khi va chạm và thông báo sự kiện va chạm
 */

public class Block implements Sprite, Collidable, HitNotifier {
    /**
     * Danh sách thông báo sự kiện va chạm
     */
    private List<HitListener> hitListeners = new ArrayList<>();
    /**
     * Khối block
     */
    private Rectangle rectangle;
    /**
     * Màu khối
     */
    private Color color;

    private int hitPoints = 1; // Độ cứng của block, có thể điều chỉnh trong level
    private int maxHitPoints = 1;

    private boolean isDeathRegion = false;  // Vùng bên dưới màn hình, bóng ra ngoài sẽ biến mất

    private PowerUp containedPowerUp = null; // chứ powerup

    /**
     * Khởi tạo block
     * @param rectangle Khối block
     * @param color Màu
     */
    public Block(Rectangle rectangle, Color color) {
        this.color = color;
        this.rectangle = rectangle;
        hitListeners = new ArrayList<>();
    }

    public Block(int hitPoints, Color color, Rectangle rectangle) {
        this.hitPoints = hitPoints;
        this.color = color;
        this.rectangle = rectangle;
    }

    public Block(Rectangle rectangle, Color color, int hitPoints, boolean isDeathRegion) {
        this.color = color;
        this.hitPoints = hitPoints;
        this.rectangle = rectangle;
        this.isDeathRegion = isDeathRegion;
    }

    /**
     * Khởi tạo khối Block
     * @param leftUpperCorner Điểm góc trên trái của khối block
     * @param width Chiều dài
     * @param height Chiều rộng
     */
    public Block(Point leftUpperCorner, int width, int height) {
        this.rectangle = new Rectangle(leftUpperCorner, width, height);
        hitListeners = new ArrayList<>();
    }

    public boolean isDeathRegion() {
        return isDeathRegion;
    }

    // trả về độ cứng của Block
    public int getHitPoints() {
        return hitPoints;
    }

    public void decreaseHitPoints() {
        if (hitPoints > 0) {
            hitPoints--;
        }
    }

    // thêm power up
    public void setPowerUp(PowerUp powerUp) {
        this.containedPowerUp = powerUp;
    }

    public boolean hasPowerUp() {
        return containedPowerUp != null;
    }

    public PowerUp getPowerUp() {
        return containedPowerUp;
    }

    /**
     * Vẽ lại khối block
     */
    @Override
    public void render(GraphicsContext gc) {
        double x = rectangle.getUpperLeft().getX();
        double y = rectangle.getUpperLeft().getY();
        double w = rectangle.getWidth();
        double h = rectangle.getHeight();

        String imgPath = "/png/buttonSelected.png";

        Image img = ImageLoader.load(imgPath);
        gc.drawImage(img, x, y, w, h);

    }

    /**
     * Update lại hình ảnh block sau thời gian dt
     */
    @Override
    public void update(double dt) {
    }

    /**
     * Thay đổi vận tốc bóng sau khi va chạm với block dựa trên hướng bóng va chạm
     * @param hitter Bóng va chạm
     * @param collisionPoint Điểm va chạm
     * @param currentVelocity Vận tốc bóng hiện tại
     * @return updateVelocity Vận tốc mới 
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity){

        double upperLeftX = this.rectangle.getUpperLeft().getX();
        double upperLeftY = this.rectangle.getUpperLeft().getY();
        double width = this.rectangle.getWidth();
        double height = this.rectangle.getHeight();


        double x = collisionPoint.getX();
        double y = collisionPoint.getY();
        double epsilon = 0.0001;

        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();

        double distTop = Math.abs(y - upperLeftY);
        double distBottom = Math.abs(y - (upperLeftY + height));
        double distLeft = Math.abs(x - upperLeftX);
        double distRight = Math.abs(x - (upperLeftX + width));

        double minDist = Math.min(Math.min(distTop, distBottom), Math.min(distLeft, distRight));

        boolean hitTopOrBottom = false;
        boolean hitLeftOrRight = false;

        // Va chạm cạnh trên hoặc dưới
        if (minDist == distTop || minDist == distBottom) {
            // Kiểm tra điểm va chạm có nằm trong phạm vi chiều rộng không
            if (x >= upperLeftX - epsilon && x <= upperLeftX + width + epsilon) {
                dy = -dy;
                hitTopOrBottom = true;
            }
        }

        // Va chạm cạnh trái hoặc phải
        if (minDist == distLeft || minDist == distRight) {
            // Kiểm tra điểm va chạm có nằm trong phạm vi chiều cao không
            if (y >= upperLeftY - epsilon && y <= upperLeftY + height + epsilon) {
                dx = -dx;
                hitLeftOrRight = true;
            }
        }
        Velocity updateVelocity = new Velocity(dx, dy);
        // Giảm hit points và thông báo
        if(this.getHitPoints() > 0){
            this.decreaseHitPoints();
        }
        this.notifyHit(hitter, collisionPoint);

        return updateVelocity;
    }


    @Override
    /**
     * Thêm block là 1 vật thông báo va chạm 
     */
    public void addHitListener(HitListener h){
        this.hitListeners.add(h);
    }

    @Override
    /**
     * Xóa block khỏi danh sách thông báo va chạm
     */
    public void removeHitListener(HitListener h){
        this.hitListeners.remove(h);
    }

    /**
     * Hàm được gọi mỗi khi có va chạm Hit() và thông báo cho toàn bộ các đối tượng quan tâm đến va chạm
     * @param hitter Bóng
     * @param collisionPoint Điểm va chạm
     */
    public void notifyHit(Ball hitter, Point collisionPoint){
        List<HitListener> copyHitListeners = new ArrayList<HitListener>(this.hitListeners);
        for(HitListener hl : copyHitListeners){
            hl.hitEvent(new HitEvent(hitter, this, collisionPoint));
        }
    }

    /**
     * hàm trả về block
     * @return rectangle Block
     */
    public Rectangle getCollisionRectangle(){
        return this.rectangle;
    }

    /**
     * Hàm trả về màu 
     * @return color Màu
     */
    public Color getColor(){
        return this.color;
    }
}
