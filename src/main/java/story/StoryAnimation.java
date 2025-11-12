package story;
import animation.Animation;
import input.Key;
import input.PlayerInput;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import ui.ImageLoad;

import java.util.List;

public class StoryAnimation implements Animation {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final PlayerInput input;
    private final List<String> imagePaths; // Danh sách đường dẫn ảnh cốt truyện
    private final Runnable onComplete;
    
    private int currentImageIndex = 0;
    private double elapsedTime = 0;
    private boolean isFinished = false;
    private Image currentImage;
    private boolean canAdvance = false;
    private double fadeAlpha = 0; // Hiệu ứng fade in
    private static final double FADE_SPEED = 2.5;

    public StoryAnimation(Canvas canvas, PlayerInput input, List<String> imagePaths, Runnable onComplete) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.input = input;
        this.imagePaths = imagePaths;
        this.onComplete = onComplete;
        
        if (!imagePaths.isEmpty()) {
            loadImage(0);
        }
    }

    private void loadImage(int index) {
        if (index < imagePaths.size()) {
            try {
                currentImage = ImageLoad.load(imagePaths.get(index));
                System.out.println("Loaded story image: " + imagePaths.get(index));
            } catch (Exception e) {
                System.err.println("Không thể load ảnh story: " + imagePaths.get(index));
                currentImage = null;
            }
        }
    }

    @Override
    public void update(double dt) {
        if (imagePaths.isEmpty() || isFinished) {
            return;
        }

        elapsedTime += dt;

        // Fade in effect
        if (fadeAlpha < 1.0) {
            fadeAlpha += FADE_SPEED * dt;
            if (fadeAlpha > 1.0) fadeAlpha = 1.0;
        }

        // Cho phép next sau 0.3 giây (tránh skip ngay lập tức)
        if (elapsedTime > 0.3) {
            canAdvance = true;
        }

        // Kiểm tra input để chuyển ảnh
        boolean shouldNext = false;

        if (canAdvance) {
            // Click chuột TRÁI hoặc nhấn ENTER để next
            if (input.isClickLeft() || input.getKeyboard().wasJustPressed(Key.ENTER)) {
                shouldNext = true;
            }
        }

        // Nhấn ESC để skip toàn bộ cốt truyện
        if (input.getKeyboard().wasJustPressed(Key.ESC)) {
            isFinished = true;
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        if (shouldNext) {
            currentImageIndex++;
            elapsedTime = 0;
            canAdvance = false;
            fadeAlpha = 0;

            if (currentImageIndex >= imagePaths.size()) {
                // Hết ảnh, chuyển sang level
                isFinished = true;
                if (onComplete != null) {
                    onComplete.run();
                }
            } else {
                // Load ảnh tiếp theo
                loadImage(currentImageIndex);
            }
        }

        // Reset input state
        input.getMouse().update();
        input.getKeyboard().update();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (imagePaths.isEmpty() || currentImageIndex >= imagePaths.size()) {
            return;
        }

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        // Vẽ background đen
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        // Vẽ ảnh cốt truyện (với fade effect)
        if (currentImage != null) {
            gc.setGlobalAlpha(fadeAlpha);
            gc.drawImage(currentImage, 0, 0, width, height);
            gc.setGlobalAlpha(1.0);
        }

        // Hint text (chỉ hiện khi đã qua thời gian chờ)
        if (canAdvance) {
            gc.setGlobalAlpha(0.8);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", 14));
            gc.setTextAlign(TextAlignment.CENTER);
            
            // Hiển thị số trang
            String pageInfo = String.format("%d / %d", currentImageIndex + 1, imagePaths.size());
            gc.fillText(pageInfo, width / 2, height - 60);
            
            // Hướng dẫn
            gc.fillText("Nhấn ENTER hoặc CLICK để tiếp tục", width / 2, height - 35);
            gc.fillText("Nhấn ESC để bỏ qua", width / 2, height - 15);
            
            gc.setGlobalAlpha(1.0);
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
