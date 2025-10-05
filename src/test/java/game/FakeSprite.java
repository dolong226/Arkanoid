package game;

import javafx.scene.canvas.GraphicsContext;

public class FakeSprite implements Sprite {
    boolean timePassedCalled = false;
    boolean drawOnCalled = false;

    @Override
    public void drawOn(GraphicsContext gc) {
        drawOnCalled = true;
    }

    public void timePassed() {
        timePassedCalled = true;
    }
}
