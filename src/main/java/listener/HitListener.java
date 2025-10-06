package listener;

import ball.Ball;
import collidable.Block;

/**
 * Định nghĩa cho các lớp xử lí sự kiện và va chạm. Cho phép phản ứng với va chạm.
 */
public interface HitListener {

    /**
     * Xử lí va chạm. Sẽ được triển khai riêng.
     */
    void hitEvent(HitEvent event);
}
