package listener;

import ball.Ball;
import collidable.Block;
import game.GameLevel;
import game.Counter;

/**
 * Listener loại bỏ bóng khi rơi ra khỏi vùng chơi hoặc chạm death region.
 */
public class BallRemove implements HitListener {
    private GameLevel game;
    private Counter remainingBalls;

    public BallRemove(GameLevel game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    @Override
    public void hitEvent(HitEvent event) {
        Ball hitter = event.getHitter();
        Block beingHit = event.getTarget();

        if (beingHit.isDeathRegion()) {
            game.removeSprite(hitter);
            remainingBalls.decrease(1);
        }
    }
}
