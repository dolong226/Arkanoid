package level;

import collidable.Block;

/**
 * Định nghĩa cho các lớp tạo ra block với các chi tiết khác nhau.
 */
public interface BlockCreator {

    /**
     * Tạo một block.
     */
    Block createBlock(double xpos, double ypos);

    /**
     * Trả về chiều rộng của block.
     * Để tính toán va chạm.
     */
    double getBlockWidth();

    double getBlockHeight();
}
