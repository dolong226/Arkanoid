package collidable;

import geometry.Point;

/**
 * Class này định nghĩa về 1 thông tin va chạm
 */
public class CollisionInfo {
    /**
     * Điểm va chạm (gần nhất)
     */
    Point closetPoint;
    /**
     * Các object có thể tham gia va chạm
     */
    Collidable closetCollidable;

    /**
     * Khởi tạo thông tin va chạm rỗng
     */
    public CollisionInfo() {
    }
    
    /**
     * Hàm này khởi tạo 1 thông tin va chạm
     * @param closetPoint Điểm va chạm gần nhất
     * @param closetCollidable Object tham gia va chạm
     */
    public CollisionInfo(Point closetPoint, Collidable closetCollidable) {
        this.closetCollidable = closetCollidable;
        this.closetPoint = closetPoint;
    }

    /**
     * Trả về điểm va chạm gần nhất
     * @return điểm va chạm 
     */
    public Point getClosetPoint(){
        return this.closetPoint;
    }

    /**
     * Trả về object tham gia va chạm
     * @return object tham gia va chạm
     */
    public Collidable getCollidable(){
        return this.closetCollidable;
    }


}
