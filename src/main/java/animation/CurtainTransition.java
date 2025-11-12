// File: animation/CurtainTransition.java
package animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import ui.ImageLoad;

public class CurtainTransition implements Animation {
    private final Animation fromAnimation;  // Màn hình cũ
    private final Animation toAnimation;    // Màn hình mới
    private final double duration;          // Thời gian (giây)
    private double elapsed = 0;             // Thời gian đã trôi

    private enum State { FROM_FADE, CURTAIN_DROP, TO_FADE_IN }
    private State state = State.FROM_FADE;

    public CurtainTransition(Animation from, Animation to, double duration) {
        this.fromAnimation = from;
        this.toAnimation = to;
        this.duration = duration;
    }

    @Override
    public void update(double dt) {
        elapsed += dt;


        if (elapsed >= duration) {
            // Kết thúc transition
            shouldStop = true;
            return;
        }

        // Chuyển state theo thời gian
        double progress = elapsed / duration;
        if (progress < 0.3) {
            state = State.FROM_FADE;      // Fade out màn cũ (0-30%)
        } else if (progress < 0.7) {
            state = State.CURTAIN_DROP;   // Màn kéo xuống (30-70%)
        } else {
            state = State.TO_FADE_IN;     // Fade in màn mới (70-100%)
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        double canvasW = gc.getCanvas().getWidth();
        double canvasH = gc.getCanvas().getHeight();
        double progress = elapsed / duration;

        //  Vẽ màn hình cũ (fade out)
        gc.save();
        if (state == State.FROM_FADE || state == State.CURTAIN_DROP) {
            double fadeOutAlpha = Math.max(0, 1 - (progress * 3.33)); // 0-30% → alpha 1→0
            gc.setGlobalAlpha(fadeOutAlpha);
            fromAnimation.render(gc);
        }
        gc.restore();

        // Vẽ màn hình mới (fade in)
        gc.save();
        if (state == State.TO_FADE_IN) {
            double fadeInAlpha = Math.max(0, (progress - 0.7) * 3.33);
            gc.setGlobalAlpha(fadeInAlpha);
            toAnimation.render(gc);
        }
        gc.restore();

        // Màn che
        if (state == State.CURTAIN_DROP) {
            double curtainHeight = (progress - 0.3) * 2.33 * canvasH;
            Image curtainTexture = ImageLoad.load("/Default/background_blue.png");
            gc.drawImage(curtainTexture, 0, 0, canvasW, curtainHeight);
        }
    }

    private boolean shouldStop = false;
    @Override
    public boolean isFinished() { return shouldStop; }
}