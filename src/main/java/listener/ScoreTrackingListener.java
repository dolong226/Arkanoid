package listener;

import game.Counter;

/**
 * Cập nhật điểm khi có va chạm giữa bóng và block.
 */
public class ScoreTrackingListener implements HitListener {
    private final Counter score;

    public ScoreTrackingListener(Counter score) {
        this.score = score;
    }

    @Override
    public void hitEvent(HitEvent event) {
        score.increase(100);
    }
}
