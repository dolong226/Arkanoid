package animation;
import javafx.scene.canvas.GraphicsContext;

public interface Animation {
    void update(double deltaTime );
    void render(GraphicsContext gc);
    boolean isFinished();
}