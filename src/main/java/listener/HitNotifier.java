package listener;

/**
 * Thông báo cho HitListener khi xảy ra sự kiện va chạm.
 */
public interface HitNotifier {
    /**
     * Thêm một hitListener.
     */
    void addHitListener(HitListener hitListener);

    /**
     * Xóa một hitListener.
     */
    void removeHitListener(HitListener hitListener);

}
