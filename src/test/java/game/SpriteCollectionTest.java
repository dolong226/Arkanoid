package game;

import com.sun.webkit.graphics.WCGraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class SpriteCollectionTest {

    @Test
    public void testAddSprite() {
        SpriteCollection sc = new SpriteCollection();
        FakeSprite s = new FakeSprite();
        sc.addSprite(s);

        Assert.assertTrue(sc.getSprites().contains(s));

    }

    @Test
    public void testRemoveSprite() {
        FakeSprite s = new FakeSprite();
        List list = new ArrayList<>();
        list.add(s);
        SpriteCollection sc = new SpriteCollection(list);

        Assert.assertTrue(sc.getSprites().contains(s));
    }

    @Test
    public void testNotifyAllTimePassed() {
        FakeSprite s = new FakeSprite();
        SpriteCollection sc = new SpriteCollection(new ArrayList<>());
        sc.addSprite(s);
        sc.notifyAllTimePassed();

        Assert.assertTrue(s.timePassedCalled);
    }
}
