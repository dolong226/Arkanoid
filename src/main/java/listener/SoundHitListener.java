package listener;

import collidable.Block;
import collidable.Paddle;
import game.GameController;

public class SoundHitListener implements HitListener {
    private final GameController controller;

    public SoundHitListener(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void hitEvent(HitEvent event) {
        if (event.getHitObject() instanceof Paddle) {
            controller.onBallHitBrick();
        } else if (event.getHitObject() instanceof Block) {
            Block b = (Block) event.getHitObject();
            if (!b.isDeathRegion()) {
                controller.onBallHitBrick();
                if (b.getHitPoints() <= 0) {
                    controller.onBrickDestroy();
                }
            }
        }
    }
}
