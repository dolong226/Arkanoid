
package menu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import geometry.Point;
import geometry.Rectangle;

public class MenuImageButton {
    public String text; // để nhận diện hành động (tùy chọn)
    private Image normalImage;
    private Image hoverImage;
    private Image currentImage;
    private Rectangle rect;
    private boolean isHovered;

    public MenuImageButton(String text, Image normal, Image hover, double x, double y, double width, double height) {
        this.text = text;
        this.normalImage = normal;
        this.hoverImage = hover;
        this.currentImage = normal;
        this.rect = new Rectangle(new Point(x,y), width, height);
        this.isHovered = false;
    }

    public MenuImageButton(String key, Image normal, double x, double y, double width, double height) {
        this(key, normal, normal, x, y, width, height);
    }

    public boolean isClicked(Point mouse) {
        return rect.contains(mouse);
    }

    public void updateHover(Point mouse) {
        boolean wasHovered = isHovered;
        isHovered = rect.contains(mouse);
        if (isHovered != wasHovered) {
            currentImage = isHovered ? hoverImage : normalImage;
        }
    }

    public void render(GraphicsContext gc) {
        if (currentImage != null) {
            gc.drawImage(currentImage, rect.getUpperLeft().getX(), rect.getUpperLeft().getY(), rect.getWidth(), rect.getHeight());
        }
    }

    public String getText() {
        return text;
    }
}