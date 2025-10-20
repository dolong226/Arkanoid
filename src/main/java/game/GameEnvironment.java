package game;

import collidable.Collidable;
import collidable.CollisionInfo;
import geometry.Rectangle;
import geometry.Line;
import geometry.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý môi trường vật lí, nơi tập hợp tất cả các vật thể có thể va chạm.
 */
public class GameEnvironment {
    private List<Collidable> collisionList = new ArrayList<>();

    /**
     * thêm một vật thể có thể va chạm vào môi trường.
     **/
    public void addCollidable(Collidable collidable) {
        collisionList.add(collidable);
    }

    /**
     * xóa một vật thể có thể va chạm trong môi trường.
     */
    public void removeCollidable(Collidable collidable) {
        collisionList.remove(collidable);
    }

    /**
     * Trả về thông tin va chạm gần nhất hoặc null nếu không va chạm.
     */
    public CollisionInfo getClosetCollision(Line trajectory) {
        Point start = trajectory.getStart();
        Point closetPoint = null;
        Collidable closetCollidable = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Collidable collidable: collisionList) {
            Rectangle rect = collidable.getCollisionRectangle();

            // Tìm giao điểm của đường trajectory với rectangle này.
            Point intersection = trajectory.closestIntersectionToStartOfLine(rect);

            if (intersection != null) {
                double distance = start.distance(intersection);
                if (distance < minDistance) {
                    minDistance = distance;
                    closetPoint = intersection;
                    closetCollidable = collidable;
                }
            }
        }

        if (closetPoint == null) {
            return null;
        }

        return new CollisionInfo(closetPoint, closetCollidable);
    }
}
