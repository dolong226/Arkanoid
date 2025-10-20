package collidable;

import geometry.Point;

public class CollisionInfo {
    Point closetPoint;
    Collidable closetCollidable;

    public CollisionInfo() {
    }

    public CollisionInfo(Point closetPoint, Collidable closetCollidable) {
        this.closetCollidable = closetCollidable;
        this.closetPoint = closetPoint;
    }
}
