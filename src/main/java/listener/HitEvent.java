package listener;

import ball.Ball;
import collidable.Block;
import collidable.Collidable;
import geometry.Point;
import javafx.geometry.Point2D;

/**
 * Chứa thông tin về một va chạm giữa bóng và block.
 */
public class HitEvent {
    private Ball hitter;
    private Collidable hitObject;
    private Point collisionPoint;

    public HitEvent(Ball hitter, Collidable hitObject, Point collisionPoint) {
        this.hitter = hitter;
        this.hitObject = hitObject;
        this.collisionPoint = collisionPoint;
    }

    public Ball getHitter() {
        return hitter;
    }

    public Collidable getHitObject() {
        return hitObject;
    }

    public Point getCollisionPoint() {
        return collisionPoint;
    }
}
