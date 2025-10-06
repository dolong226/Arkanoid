package game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;


public interface Sprite {

    /**
     * Vẽ đối tượng lên màn hình.
     */
    void render(GraphicsContext gc);

    /**
     * Cập nhật mỗi frame để cập nhật logic, chuyển động, hiệu ứng của đối tượng.
     * @param dt: delta time là số giây đã trôi qua kể từ frame trước.
     */
    void update(double dt);
}
