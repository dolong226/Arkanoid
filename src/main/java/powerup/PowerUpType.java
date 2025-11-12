package powerup;

import javafx.scene.paint.Color;

public enum PowerUpType {
    EXPAND_PADDLE(Color.GREEN, "EP", 10.0, "Expand Paddle", "/Sprite/EXPAND_PADDLE.png"),
    SHRINK_PADDLE(Color.RED, "SP", 10.0, "Shrink Paddle", null),

    MULTI_BALL(Color.MAGENTA, "×3", -1, "Multi Ball", "/Sprite/MULTI_BALL.png"),


    FIRE_BALL(Color.ORANGE, "F", 15.0, "Fire Ball", "/Sprite/FIRE_BALL.png"),
    BIG_BALL(Color.GREENYELLOW, "B", 12.0, "Big Ball", "/Sprite/BIG_BALL.png"),

    SLOW_BALL(Color.BLUE, "S", 15.0, "Slow Motion", "/Sprite/SLOW_BALL.png");
    

    private final Color color;
    private final String icon;
    private final double duration;
    private final String displayName;
    private final String imagePath;

    /**
     * Constructor cho PowerUpType
     * @param color Màu sắc của power-up
     * @param icon Ký tự hiển thị trên power-up
     * @param duration Thời gian hiệu lực (giây), -1 nếu vĩnh viễn
     * @param displayName Tên hiển thị
     */
    PowerUpType(Color color, String icon, double duration, String displayName, String imagePath) {
        this.color = color;
        this.icon = icon;
        this.duration = duration;
        this.displayName = displayName;
        this.imagePath = imagePath;
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

    public String getImagePath(){
        return imagePath;
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
