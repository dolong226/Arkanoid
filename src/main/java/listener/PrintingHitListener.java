package listener;

public class PrintingHitListener implements HitListener {

    @Override
    public void hitEvent(HitEvent event) {
        System.out.println("[HitEvent] Bóng va chạm với gạch là: " + event.getHitter());
    }
}
