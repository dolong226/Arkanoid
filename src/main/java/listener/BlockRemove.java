package listener;

import ball.Ball;
import collidable.Block;
import game.Counter;
import game.GameLevel;
import powerup.PowerUp;
import sound.AudioResource;
import sound.SoundManager;

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
        Block beingHit = (Block) event.getHitObject();
        Ball hitter = event.getHitter();

        if (beingHit.getHitPoints() <= 0) {
            // kiểm tra power up và thả xuống
            if (beingHit.hasPowerUp()) {
                PowerUp powerUp = beingHit.getPowerUp();
                game.addSprite(powerUp);
                game.addCollidable(powerUp);
                SoundManager.getInstance().playSFX(AudioResource.POWERUP_SPAWN.name());
                System.out.println("power up");
            }
            game.removeSprite(beingHit);
            game.removeCollidable(beingHit);
            remainingBlocks.decrease(1);
        }
    }
}
