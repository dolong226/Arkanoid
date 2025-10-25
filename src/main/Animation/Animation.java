import javafx.scene.canvas.GraphicsContext;

public interface Animation {
    void upDate(double deltaTime );
    void render(GraphicsContext gc);
    boolean isFinished();

}