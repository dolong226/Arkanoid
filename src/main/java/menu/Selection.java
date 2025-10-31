package menu;

import geometry.Rectangle;
import input.Key;
import input.Mouse;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import geometry.Point;
import javafx.scene.text.Font;

public class Selection<T> {
    private Rectangle rectangle;
    private Color color;
    private String message;
    private T returnVal;

    public Selection(String message, T returnVal, Rectangle rectangle,Color color) {
        this.color = color;
        this.message = message;
        this.rectangle = rectangle;
        this.returnVal = returnVal;
    }

    public boolean isClicked(Mouse mouse) {
        Point mousePosition = mouse.getMousePosition();
        return mouse.wasJustPressed(Key.MOUSE_LEFT) && rectangle.contains(mousePosition);
    }

    public T getReturnVal() {
        return returnVal;
    }

    public String getMessage() {
        return message;
    }

    public void render(GraphicsContext gc) {
        // Lưu trạng thái hiện tại của GC.
        gc.save();

        // Tô màu cho hcn.
        gc.setFill(color);
        gc.fillRect(
                rectangle.getUpperLeft().getX(),
                rectangle.getUpperLeft().getY(),
                rectangle.getWidth(),
                rectangle.getLength()
        );

        // Màu và phông chữ
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 20));

        // Căn giữa
        double textX = rectangle.getUpperLeft().getX() + (rectangle.getWidth() - gc.getFont().getSize() * message.length() / 2) / 2;
        double textY = rectangle.getUpperLeft().getY() + (rectangle.getLength() + gc.getFont().getSize()) / 2;

        // Vẽ string
        gc.fillText(message, textX, textY);

        // Khôi phục trạng thái GraphicsContext
        gc.restore();
    }
}
