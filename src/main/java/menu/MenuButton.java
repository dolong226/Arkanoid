package menu;

import geometry.Point;
import geometry.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuButton {
    public final String text;
    private final Rectangle rect;
    private final Color color;
    private final Color hoverColor; // Màu khi di chuột lên
    private boolean isHovered = false;


    // Khởi tọa button với text, tọa độ kích thước màu sắc
    public MenuButton(String text, double x, double y, double w, double h, Color color) {
        this.text = text;
        this.rect = new Rectangle(new Point(x,y), w, h);
        this.color = color;
        this.hoverColor = color.brighter();
    }

    public boolean isClicked(Point p) {
        return rect.contains(p);
    }

    public void render(GraphicsContext gc) {
        // Hover effect
        gc.setFill(isHovered ? hoverColor : color);
        gc.fillRoundRect(rect.getUpperLeft().getX(), rect.getUpperLeft().getY(),
                rect.getWidth(), rect.getHeight(), 20, 20);

        // Viền
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.strokeRoundRect(rect.getUpperLeft().getX(), rect.getUpperLeft().getY(),
                rect.getWidth(), rect.getHeight(), 20, 20);

        // Chữ
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 24));
        double textX = rect.getUpperLeft().getX() + 60;
        double textY = rect.getUpperLeft().getY() + 40;
        gc.fillText(text, textX, textY);
    }

    public void updateHover(Point mouse) {
        isHovered = rect.contains(mouse);
    }
}
