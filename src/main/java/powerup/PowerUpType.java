package powerup;

import javafx.scene.paint.Color;

public enum PowerUpType {
    EXPAND_PADDLE(Color.GREEN, "↔", 10.0, "Expand Paddle"),
    SHRINK_PADDLE(Color.RED, "←→", 10.0, "Shrink Paddle"),

    EXTRA_BALL(Color.CYAN, "+", -1, "Extra Ball"),
    MULTI_BALL(Color.MAGENTA, "×3", -1, "Multi Ball"),


    FIRE_BALL(Color.ORANGE, "🔥", 15.0, "Fire Ball"),
    BIG_BALL(Color.GREENYELLOW, "●", 12.0, "Big Ball"),

    SLOW_BALL(Color.BLUE, "S", 15.0, "Slow Motion"),
    FAST_BALL(Color.YELLOW, "F", 10.0, "Fast Ball"),

    EXTRA_LIFE(Color.LIGHTGREEN, "♥", -1, "Extra Life");

    private final Color color;
    private final String icon;
    private final double duration;
    private final String displayName;

    /**
     * Constructor cho PowerUpType
     * @param color Màu sắc của power-up
     * @param icon Ký tự hiển thị trên power-up
     * @param duration Thời gian hiệu lực (giây), -1 nếu vĩnh viễn
     * @param displayName Tên hiển thị
     */
    PowerUpType(Color color, String icon, double duration, String displayName) {
        this.color = color;
        this.icon = icon;
        this.duration = duration;
        this.displayName = displayName;
    }

    public Color getColor() {
        return color;
    }

    public double getDuration() {
        return duration;
    }

    public String getIcon() {
        return icon;
    }

    /**
     * Kiểm tra power-up có hiệu lực vĩnh viễn không
     */
    public boolean isPermanent() {
        return this.duration < 0;
    }

    /**
     * Lấy tên hiển thị
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Lấy một PowerUpType ngẫu nhiên
     */
    public static PowerUpType random() {
        PowerUpType[] types = values();
        return types[(int) (Math.random() * types.length)];
    }
}
