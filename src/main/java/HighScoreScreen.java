import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.List;

public class HighScoreScreen implements Animation {
    private final List<Integer> scores;
    private boolean stop = false;

    public HighScoreScreen(List<Integer> scores) {
        this.scores = scores;
    }

    @Override
    public void doOneFrame(GraphicsContext gc, double deltaTime) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 0, w, h);

        String title = "HIGH SCORES";
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 64));

        Text titleText = new Text(title);
        titleText.setFont(gc.getFont());
        double titleWidth = titleText.getLayoutBounds().getWidth();
        gc.fillText(title, (w - titleWidth) / 2, 120);

        gc.setFont(Font.font("Consolas", 36));
        int y = 200;
        for (int i = 0; i < scores.size(); i++) {
            String line = String.format("%d. %d", i + 1, scores.get(i));
            Text lineText = new Text(line);
            lineText.setFont(gc.getFont());
            double lineWidth = lineText.getLayoutBounds().getWidth();
            gc.fillText(line, (w - lineWidth) / 2, y);
            y += 50;
        }
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }

    public void stop() {
        this.stop = true;
    }
}
