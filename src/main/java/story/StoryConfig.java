package story;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cấu hình đường dẫn ảnh cốt truyện
 */
public class StoryConfig {

    /**
     * Lấy danh sách ảnh cốt truyện mở đầu (trước Level 1)
     */
    public static List<String> getOpeningStory() {
        return Arrays.asList(
                "/story/1.png",  
                "/story/2.png",  
                "/story/3.png",
                "/story/5.png",
                "/story/4.png"
        );
    }

    /**
     * Lấy cốt truyện giữa các level
     * @param completedLevel Level vừa hoàn thành
     */
    public static List<String> getInterLevelStory(int completedLevel) {
        List<String> images = new ArrayList<>();
        
        switch (completedLevel) {
            case 1:
                // Cốt truyện giữa Level 1 và Level 2
                images.add("/story/6.png");
                break;
                
            case 2:
                // Cốt truyện giữa Level 2 và Level 3
                images.add("/story/7.png");
                break;
        }
        
        return images;
    }
}