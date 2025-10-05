package game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;


public interface Sprite {

    /**
     * Vẽ đối tượng lên màn hình.
     */
    void drawOn(GraphicsContext gc);

    /**
     * Cập nhật mỗi frame để cập nhật logic, chuyển động, hiệu ứng của đối tượng.
     */
    void timnePassed();
}
