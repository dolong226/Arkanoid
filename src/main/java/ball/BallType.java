package ball;
import javafx.scene.paint.Color;

/**
 * File này định nghĩa về các loại bóng trong game
 */
public enum BallType {
    /**
     * Bóng thường
     */
    NORMAL(1.0, Color.WHITE, false),
    /**
     * Bóng lửa
     */
    FIRE(1.0, Color.ORANGE, true),
    /**
     * Bóng lớn
     */
    BIG(1.25, Color.GREENYELLOW, false);

    /**
     * Hệ số nhân kích cỡ bóng
     */
    private double sizeBallMultiplier;
    /**
     * Màu mặc định bóng
     */
    private Color defaultColor;
    /**
     * Bóng có thể xuyên qua các block được không
     */
    private final boolean canPenetrate;

    /**
     * Hàm khởi tạo 1 loại bóng
     * @param sizeBallMultiplier Hệ số nhân kích cỡ bóng
     * @param defaulColor Màu mặc định
     * @param canPenetrate Khả năng xuyên qua block của bóng
     */
    BallType(double sizeBallMultiplier, Color defaulColor, boolean canPenetrate){
        this.sizeBallMultiplier = sizeBallMultiplier;
        this.defaultColor = defaulColor;
        this.canPenetrate = canPenetrate;
    }

    /**
     * Trả về hệ số nhân kích cỡ bóng
     * @return hệ số nhân kích cỡ bóng
     */
    public double getSizeBallMultiplier(){
        return this.sizeBallMultiplier;
    }

    /**
     * Trả về màu bóng
     * @return màu bóng
     */
    public Color getColor(){
        return this.defaultColor;
    }

    /**
     * Check xem bóng có thể xuyên qua block không
     * @return khả năng xuyên qua block
     */
    public boolean getPenetration(){
        return this.canPenetrate;
    }
}
