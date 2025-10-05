package game;

import javafx.scene.canvas.GraphicsContext;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SpriteCollection {

    private List<Sprite> sprites;

    public SpriteCollection() {
        this.sprites = new ArrayList<>();
    }

    public SpriteCollection(List<Sprite> sprites) {
        this.sprites = sprites;
    }

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
     */
    public void notifyAllTimePassed() {
        for (Sprite sprite: sprites) {
            sprite.timePassed();
        }
    }

    /**
     * Vẽ tất cả ra màn hình với thứ tự hiện có.
     */
    public void drawAllOn(GraphicsContext gc) {
        for (Sprite sprite: sprites) {
            sprite.drawOn(gc);
        }
    }
}
