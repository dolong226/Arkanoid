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
        CollisionInfo closest = null;
        double minDistance = Double.MAX_VALUE;
        final double EPSILON = 1e-5;

        List<Collidable> collidablesCopy = new ArrayList<>(this.collisionList);

        for (Collidable collidable : collisionList) {
            Rectangle rect = collidable.getCollisionRectangle();


            Point intersection = trajectory.closestIntersectionToStartOfLine(rect);

            if (intersection != null) {
                double distance = start.distance(intersection);
                if (distance > EPSILON && distance < minDistance) {
                    minDistance = distance;
                    closest = new CollisionInfo(intersection, collidable);
                }
            }
        }

        return closest;
    }
    public List<Collidable> getCollisionList() {
        return collisionList;
    }
}
