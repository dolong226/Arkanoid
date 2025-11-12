package ui;

import javafx.scene.image.Image;
import thread.ResourceLoaderThread;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 */
public class ImageLoad {

    /**
     * Cache local cho images đã load
     */
    private static final Map<String, Image> cache = new HashMap<>();

    /**
     * ResourceLoaderThread instance để load async
     */
    private static ResourceLoaderThread loaderThread;

    /**
     * Khởi tạo ResourceLoaderThread
     */
    public static void initialize() {
        if (loaderThread == null) {
            loaderThread = ResourceLoaderThread.getInstance();
            System.out.println("[ImageLoad] Initialized with ResourceLoaderThread");
        }
    }

    /**
     * Load image đồng bộ
     * @param path Đường dẫn đến image file
     */
    public static Image load(String path) {
        // Kiểm tra local cache trước
        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        // Nếu có ResourceLoaderThread, dùng nó (sync)
        if (loaderThread != null && !loaderThread.isShutdown()) {
            Image image = loaderThread.loadImageSync(path);
            if (image != null) {
                cache.put(path, image);
            }
            return image;
        }

        return loadFromInputStream(path);
    }

    /**
     * Load image từ InputStream (cách cũ)
     */
    private static Image loadFromInputStream(String path) {
        return cache.computeIfAbsent(path, p -> {
            InputStream is = ImageLoad.class.getResourceAsStream(p);
            if (is == null) {
                System.err.println("ImageLoad Không tìm thấy ảnh: " + p);
                return null;
            }
            try {
                Image image = new Image(is);
                System.out.println("ImageLoad Loaded: " + p);
                return image;
            } catch (Exception e) {
                System.err.println("ImageLoad Lỗi load ảnh " + p + " - " + e.getMessage());
                return null;
            }
        });
    }


    public static Future<Image> loadAsync(String path) {

        if (loaderThread == null || loaderThread.isShutdown()) {
            initialize();
        }

        if (cache.containsKey(path)) {
            Image cachedImage = cache.get(path);

            return java.util.concurrent.CompletableFuture.completedFuture(cachedImage);
        }

        Future<Image> future = loaderThread.loadImageAsync(path);

        return wrapFutureWithCache(path, future);
    }


    private static Future<Image> wrapFutureWithCache(String path, Future<Image> originalFuture) {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            try {
                Image image = originalFuture.get();
                if (image != null) {
                    cache.put(path, image);
                }
                return image;
            } catch (Exception e) {
                System.err.println("[ImageLoad] Error in async load: " + path);
                e.printStackTrace();
                return null;
            }
        });
    }

    public static Future<Void> preload(List<String> paths) {
        if (loaderThread == null || loaderThread.isShutdown()) {
            initialize();
        }

        System.out.println("ImageLoa Preloadin ");

        return loaderThread.preloadImages(paths);
    }

    public static boolean preloadSync(String path) {
        try {
            Future<Image> future = loadAsync(path);
            Image image = future.get(); // Block đợi load xong
            return image != null;
        } catch (Exception e) {
            System.err.println("ImageLoad Preload failed: " + path);
            e.printStackTrace();
            return false;
        }
    }

    public static Image getFromCache(String path) {
        Image localImage = cache.get(path);
        if (localImage != null) {
            return localImage;
        }

        if (loaderThread != null && !loaderThread.isShutdown()) {
            Image threadImage = loaderThread.getFromCache(path);
            if (threadImage != null) {
                cache.put(path, threadImage);
                return threadImage;
            }
        }

        return null;
    }

    public static boolean isLoaded(String path) {
        if (cache.containsKey(path)) {
            return true;
        }

        if (loaderThread != null && !loaderThread.isShutdown()) {
            return loaderThread.isLoaded(path);
        }

        return false;
    }


    public static void clearCache() {
        System.out.println("ImageLoad] Clearing local cache (" + cache.size() + " images)");
        cache.clear();

        if (loaderThread != null && !loaderThread.isShutdown()) {
            loaderThread.clearCache();
        }
    }

    public static int getCacheSize() {
        int localSize = cache.size();
        int threadSize = (loaderThread != null && !loaderThread.isShutdown())
                ? loaderThread.getCacheSize()
                : 0;
        return Math.max(localSize, threadSize);
    }


    public static void shutdown() {

        clearCache();

        if (loaderThread != null) {
            loaderThread.shutdown();
            loaderThread = null;
        }

    }
}