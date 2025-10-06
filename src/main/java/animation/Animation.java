package animation;

import javafx.scene.canvas.GraphicsContext;

/**
 * Định nghĩa cho các đối tượng hiển thị hoạt animtion.
 * Điều khiển vẽ từng khung hình và khi nào dừng.
 */
public interface Animation {

    /**
     * Cập nhật logic của animation.
     * @param dt delta time.
     */
    void update(double dt);
    /**
     * Vẽ một khung hình lên.
     */
    void render(GraphicsContext gc);

    boolean isFinished();
}
