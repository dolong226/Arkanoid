package listener;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Hiển thị tên màn chơi hiện tại (ví dụ: Level 1, Level 2,...).
 */
public class LevelNameIndicator {
    private final String levelName;

    public LevelNameIndicator(String levelName) {
        this.levelName = levelName;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Level: " + levelName, 400, 25); // vị trí có thể điều chỉnh
    }
}
