package level;

import ball.Velocity;
import collidable.Block;
import game.Sprite;
import sound.AudioResource;

import java.util.*;

/**
 * Định nghĩa cho các lớp mô tả một level cụ thể trong game.
 */
public interface LevelInformation {

    /**
     * Trả về số lương bóng ban đầu của level.
     */
    int numberOfBalls();

    /**
     * Trả về danh sách vận tốc ban đầu của các bóng.
     */
    List<Velocity> initialBallVelocities();

    /**
     * Trả về vận tốc của paddle.
     */
    double paddleSpeed();

    /**
     * Trả về chiều rộng của paddle.
     */
    double paddleWidth();

    /**
     * Tên của Level.
     */
    String levelName();

    /**
     * Trả về Background.
     */
    Sprite getBackground();

    /**
     * Danh sách blocks.
     */
    List<Block> blocks();

    /**
     * Số block cần phá để win.
     */
    int numberOfBlocksToRemove();

    String getBackgroundMusic();
}
