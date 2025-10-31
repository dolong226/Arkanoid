package collidable;

import java.util.List;


import java.util.ArrayList;
import javafx.scene.paint.Color;

import game.Sprite;
import javafx.scene.canvas.GraphicsContext;
import listener.HitNotifier;
import ball.*;
import geometry.*;
import listener.HitEvent;
import listener.HitListener;

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

    private boolean isDeathRegion = false;  // Vùng bên dưới màn hình, bóng ra ngoài sẽ biến mất

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

    /**
     * Vẽ lại khối block
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.save();
        if (color != null) {
            gc.setFill(color);
            gc.fillRect(rectangle.getUpperLeft().getX(),
                    rectangle.getUpperLeft().getY(),
                    rectangle.getHeight(),
                    rectangle.getWidth());
        }
        gc.restore();
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
        Velocity upDateVelocity = new Velocity(currentVelocity.getDx(), currentVelocity.getDy());

        double upperLeftX = this.rectangle.getUpperLeft().getX();
        double upperLeftY = this.rectangle.getUpperLeft().getY();

        double x = collisionPoint.getX();
        double y = collisionPoint.getY();
        double epsilon = 0.0001;
        boolean dxChanged = false;
        boolean dyChanged = false;

        /**
         * Nếu bóng va vào cạnh trên và dưới của block
         */
        if(Math.abs(y - upperLeftY) < epsilon || Math.abs(y - (upperLeftY + this.rectangle.getWidth())) < epsilon){
            /**
             * Bóng va vào giữa cạnh (không tính hai đỉnh biên)
             */
            if(x > upperLeftX && x < upperLeftX + this.rectangle.getHeight()){
            upDateVelocity.setDy(currentVelocity.getDy() * (-1));
            dyChanged = true;
        }
    }

        /**
         * Nếu bóng va vào cạnh bên trái và phải của block
         */
        if(Math.abs(x - upperLeftX) < epsilon || Math.abs(x - (upperLeftX + this.rectangle.getHeight())) < epsilon){
            /**
             * Bóng va vào giữa cạnh (Không tính hai đỉnh biên)
             */
            if(y > upperLeftY && y < upperLeftY + this.rectangle.getWidth()){
            upDateVelocity.setDx(currentVelocity.getDx() * (-1));
            dxChanged = true;
        }
    }
        /**
         * Nếu bóng va chạm vào góc
         */
        if(!dyChanged && !dxChanged){
            upDateVelocity.setDx(currentVelocity.getDx() * (-1));
            upDateVelocity.setDy(currentVelocity.getDy() * (-1));
        }
        /**
         * Nếu bóng va chạm rất gần góc (để loại trừ sai số của epsilon)
         */
        else if((Math.abs(y - upperLeftY) < epsilon || Math.abs(y - (upperLeftY + this.rectangle.getWidth())) < epsilon) && (Math.abs(x - upperLeftX) < epsilon || Math.abs(x - (upperLeftX + this.rectangle.getHeight())) < epsilon)){
            if(!dxChanged){
                upDateVelocity.setDx(currentVelocity.getDx() * (-1));
            }
            if(!dyChanged){
                upDateVelocity.setDy(currentVelocity.getDy() * (-1));
        }
    }
        this.decreaseHitPoints();
        this.notifyHit(hitter, collisionPoint);
        return upDateVelocity;
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
