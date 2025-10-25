import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PauseScreen implements Animation {
    private boolean stop = false;

    @Override
    public void doOneFrame(GraphicsContext gc, double deltaTime) {
        gc.setFill(Color.rgb(0, 0, 0, 0.6)); // nền đen mờ
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(72));
        gc.fillText("PAUSED", gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2 - 30);

        gc.setFont(Font.font(24));
        gc.fillText("Nhấn ENTER để tiếp tục", gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2 + 40 );
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
