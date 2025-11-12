import powerup.PowerUp;
import powerup.PowerUpType;
import ball.*;
import collidable.Paddle;
import game.*;
import geometry.Point;
import geometry.Rectangle;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class PowerUpTest {

    @Test
    public void testConstructor1() {
        GameEnvironment env = new GameEnvironment();
        Point position = new Point(100, 200);
        PowerUp p = new PowerUp(PowerUpType.EXPAND_PADDLE, position, env);

        Assert.assertEquals(PowerUpType.EXPAND_PADDLE, p.getType());
        Assert.assertFalse(p.isCollected());
        Assert.assertNotNull(p);
    }

    @Test
    public void testUpdate1() {
        GameEnvironment env = new GameEnvironment();
        PowerUp p = new PowerUp(PowerUpType.SLOW_BALL, new Point(0, 0), env);

        p.update(1.0); // sau 1 giây, rơi xuống 80px
        Rectangle rect = p.getCollisionRectangle();
        Assert.assertEquals(37.5 - 12.5, rect.getUpperLeft().getY(), 1e-9);
    }

    @Test
    public void testIsOutOfBounds1() {
        GameEnvironment env = new GameEnvironment();
        PowerUp p = new PowerUp(PowerUpType.MULTI_BALL, new Point(100, GameLevel.SCREEN_HEIGHT + 50), env);

        Assert.assertFalse(p.isOutOfBounds());
    }

    @Test
    public void testIsOutOfBounds2() {
        GameEnvironment env = new GameEnvironment();
        PowerUp p = new PowerUp(PowerUpType.MULTI_BALL, new Point(100, 10), env);

        Assert.assertFalse(p.isOutOfBounds());
    }
}