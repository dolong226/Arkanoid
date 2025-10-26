package listener;

import ball.Ball;
import collidable.Block;
import game.Counter;
import game.GameLevel;

/**
 * Listener loại bỏ block khi bị đánh trúng đến khi hitpoints = 0,
 * đồng thời cập nhật bộ đếm remainingBlocks.
 */
public class BlockRemove implements HitListener {
    private final GameLevel game;
    private final Counter remainingBlocks;

    public BlockRemove(GameLevel game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    @Override
    public void hitEvent(HitEvent event) {
        Block beingHit = event.getTarget();
        Ball hitter = event.getHitter();

        if (beingHit.getHitPoints() <= 0) {
            game.removeSprite(beingHit);
            game.removeCollidable(beingHit);
            remainingBlocks.decrease(1);
        }
    }
}
