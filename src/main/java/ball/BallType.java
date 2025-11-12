package ball;
import javafx.scene.paint.Color;

public enum BallType {
    NORMAL(1.0, Color.WHITE, false),
    FIRE(1.0, Color.ORANGE, true),
    BIG(1.25, Color.GREENYELLOW, false);

    private double sizeBallMultiplier;
    private Color defaultColor;
    private final boolean canPenetrate;

    BallType(double sizeBallMultiplier, Color defaulColor, boolean canPenetrate){
        this.sizeBallMultiplier = sizeBallMultiplier;
        this.defaultColor = defaulColor;
        this.canPenetrate = canPenetrate;
    }

    public double getSizeBallMultiplier(){
        return this.sizeBallMultiplier;
    }

    public Color getColor(){
        return this.defaultColor;
    }

    public boolean getPenetration(){
        return this.canPenetrate;
    }
}
