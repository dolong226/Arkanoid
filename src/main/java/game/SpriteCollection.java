package game;

import javafx.scene.canvas.GraphicsContext;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpriteCollection {

    // final ở đây chỉ ngăn việc gán lại tham chiếu.
    private final List<Sprite> sprites = new ArrayList<>();

    /**
     * Khi cần lấy danh sách các sprites.
     */
    public List<Sprite> getSprites() {
        return sprites;
    }

    /**
     * Thêm một sprite vào sprites.
     */
    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    /**
     * Xoá một sprite.
     */ 
    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    }

    /**
     * Cập nhật trạng thái cho tất cả các sprite.
     * Gọi trong hàm handle() của AnimationTimer.
     */
    public void update(double deltaTime) {
        // Dùng iteratr thay cho cfor each, để tránh lỗi sprite bị xóa ngay trong vòng lặp
        Iterator<Sprite> iterator = sprites.iterator();
        while(iterator.hasNext()) {
            Sprite s = iterator.next();
            s.update(deltaTime);
        }
    }
    /**
     * Vẽ tất cả ra màn hình với thứ tự hiện có.
     */
    public void render(GraphicsContext gc) {
        for (Sprite sprite: sprites) {
            if (sprite != null) {
                sprite.render(gc);
            }
        }
    }
}
