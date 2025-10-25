import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class EndScreenWin implements Animation {
    private boolean stop = false;

    @Override
    public void doOneFrame(GraphicsContext gc, double deltaTime) {
        // Lấy kích thước canvas
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();


        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        gc.setFill(Color.LIMEGREEN);
        gc.setFont(Font.font("Arial", 80));

        String text = "YOU WIN";

        Text t = new Text(text);
        t.setFont(gc.getFont());
        double textWidth = t.getLayoutBounds().getWidth();
        double textHeight = t.getLayoutBounds().getHeight();

        // Căn giữa cả hai chiều
        double x = (w - textWidth) / 2;
        double y = (h + textHeight) / 2;

        // Vẽ text
        gc.fillText(text, x, y);
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }

}
