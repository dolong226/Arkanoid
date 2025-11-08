// File: ImageBackground.java
package level;

import game.GameLevel;
import game.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.ImageLoad;

public class ImageBackground implements Sprite {
    private final Image image;

    public ImageBackground(String imagePath) {
        this.image = ImageLoad.load(imagePath);
        if (this.image == null) {
            throw new RuntimeException("Không thể load ảnh background: " + imagePath);
        }
    }

    @Override
    public void render(GraphicsContext gc) {

        // Vẽ ảnh nền, co giãn full màn hình
        gc.drawImage(image, 0, 0, GameLevel.SCREEN_WIDTH, GameLevel.SCREEN_HEIGHT);
    }

    @Override
    public void update(double dt) {
    }
}