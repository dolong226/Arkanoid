package collidable;

import ball.Ball;
import ball.Velocity;

import geometry.Point;
import geometry.Rectangle;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

/**
 * Interface này dùng để định nghĩa cho các Object có thể tham gia vào va chạm.
 */
public interface Collidable {

    /**
     * Trả về vùng bao quanh đối tượng, để tính toán va chạm.
     */
    Rectangle getCollisionRectangle();

    /**
     * Trả về vận tốc (hướng & tốc độ) sau va chạm của bóng.
     * @param hitter bóng sẽ va chạm.
     * @param collisionPoint điểm va chạm -> để xác định xem va chạm ở đâu trên Paddle -> có thể ảnh hưởng hướng va chạm
     * @param currentVelocity tốc độ hiện tại.
     * @return
     */
    Velocity hit (Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
