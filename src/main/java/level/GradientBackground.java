package level;

import game.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

/**
 * Background với gradient
 * Tạm thời chưa có background nên đùng tạm cho levelTest
 */
public class GradientBackground implements Sprite {
    private LinearGradient gradient;

    public GradientBackground() {
        // Gradient từ trên xuống: xanh đen -> đen
        gradient = new LinearGradient(
                0, 0, 0, 600,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(10, 10, 40)),    // Xanh đen
                new Stop(1, Color.rgb(0, 100, 100))        // Đen
        );
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.save();
        gc.setFill(gradient);
        gc.fillRect(0, 0, 800, 600);
        gc.restore();
    }

    @Override
    public void update(double dt) {
    }
}