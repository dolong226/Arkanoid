package menu;

import animation.Animation;
import geometry.Rectangle;
import javafx.scene.paint.Color;

/**
 * Định nghĩa cho các đối tượng quản lí menu trong game.
 * Menu được kế thừa từ Animation.
 */
public interface Menu<T> {

    /**
     * Thêm một lựa chọn vào menu.
     * @param key là phím người chơi nhấn để chọn.
     * @param message Văn bản hiển thị của nút bấm.
     * @param returnVal Giá trị kiểu trả về khi ấn chọn (Task, PlayGame, QuitGame).
     */
    void addSelection(String message, T returnVal,Rectangle rectangle, Color color);

    /**
     * Trả về kiểu giá trị T đại diện vho
     */
    T getStatus();

    /**
     * Reset trạng thái của menu.
     */
    void reset();


}
