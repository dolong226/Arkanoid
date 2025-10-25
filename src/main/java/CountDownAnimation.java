import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

public class CountDownAnimation implements Animation {
    private final double numOfSeconds;
    private final int countFrom;
    private boolean stop;
    private double elapsedTime = 0;

    public CountDownAnimation(double numOfSeconds, int countFrom) {
        this.numOfSeconds = numOfSeconds;
        this.countFrom = countFrom;
        this.stop = false;
    }

    @Override
    public void doOneFrame(GraphicsContext gc, double deltaTime) {
        elapsedTime += deltaTime;

        double timePerNumber = numOfSeconds / countFrom;
        int current = countFrom - (int)(elapsedTime / timePerNumber);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 72));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        String text = current > 0 ? String.valueOf(current) : "GO!";

        gc.fillText(text,
                gc.getCanvas().getWidth() / 2,
                gc.getCanvas().getHeight() / 2);

        if (elapsedTime >= numOfSeconds) {
            stop = true;
        }
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }
}
