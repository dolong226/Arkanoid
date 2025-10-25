import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class EndScreenLose implements Animation {
    private boolean stop = false;

    @Override
    public void doOneFrame(GraphicsContext gc, double deltaTime) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.RED);
        gc.setFont(Font.font(48));
        gc.fillText(" GAME OVER ", 200, 300);
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }
}
