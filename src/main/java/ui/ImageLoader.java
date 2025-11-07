package ui;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private static final Map<String, Image> cache = new HashMap<>();


    public static Image load(String path) {
        return cache.computeIfAbsent(path, p -> {
            InputStream is = ImageLoader.class.getResourceAsStream(p);
            if (is == null) {
                System.err.println("Khong tim thay anh " + p);
            }
            try {
                return new Image(is);
            } catch (Exception e) {
                System.err.println("Loi load anh " + p + " - " + e.getMessage());
                return null;
            }
        });
    }
}