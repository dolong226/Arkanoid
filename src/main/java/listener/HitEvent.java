package listener;

import ball.Ball;
import collidable.Block;
import geometry.Point;
import javafx.geometry.Point2D;

/**
 * Chứa thông tin về một va chạm giữa bóng và block.
 */
public class HitEvent {
    private final Ball hitter;
    private final Block target;
    private final Point collisionPoint;

    public HitEvent(Ball hitter, Block target, Point collisionPoint) {
        this.hitter = hitter;
        this.target = target;
        this.collisionPoint = collisionPoint;
    }

    public Ball getHitter() {
        return hitter;
    }

    public Block getTarget() {
        return target;
    }
    public Point getCollisionPoint() {
        return collisionPoint;
    }
}
