package thread;

import javafx.scene.image.Image;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * ResourceLoaderThread - Thread pool để load images bất đồng bộ
 *
 * Nhiệm vụ:
 * - Load images trong background, không block UI thread
 * - Cache images đã load để tránh load lại
 * - Preload danh sách images lúc game start
 * - Hỗ trợ cả sync và async loading
 *
 * Design Pattern: Singleton
 * - Chỉ có 1 instance duy nhất
 * - Share cache giữa các nơi sử dụng
 *
 * Lợi ích:
 * - UI không bị freeze khi load image lớn
 * - Game start nhanh hơn (load resources song song)
 * - Giảm loading time khi chuyển level
 */
public class ResourceLoaderThread {

    // ==================== SINGLETON ====================

    /** Instance duy nhất của ResourceLoaderThread */
    private static ResourceLoaderThread instance;

    /**
     * Lấy instance của ResourceLoaderThread (Singleton pattern)
     * Thread-safe với synchronized
     *
     * @return Instance duy nhất
     */
    public static synchronized ResourceLoaderThread getInstance() {
        if (instance == null) {
            instance = new ResourceLoaderThread();
        }
        return instance;
    }


    // ==================== FIELDS ====================

    /**
     * Thread pool để load resources
     * Fixed thread pool với 2 threads:
     * - Đủ để load song song nhiều resources
     * - Không quá nhiều để tránh I/O bottleneck
     */
    private final ExecutorService executor;

    /**
     * Cache để lưu images đã load
     * Key: đường dẫn của image
     * Value: Image object đã load
     *
     * ConcurrentHashMap: thread-safe, cho phép multiple threads
     * đọc/ghi đồng thời mà không cần synchronized
     */
    private final Map<String, Image> imageCache;

    /**
     * Cache cho các Future đang load
     * Tránh load cùng 1 image nhiều lần đồng thời
     * Key: đường dẫn
     * Value: Future đang load image đó
     */
    private final Map<String, Future<Image>> loadingFutures;

    /**
     * Flag đánh dấu đã shutdown chưa
     */
    private volatile boolean isShutdown;


    // ==================== CONSTRUCTOR ====================

    /**
     * Private constructor (Singleton pattern)
     */
    private ResourceLoaderThread() {
        this.isShutdown = false;

        // Tạo fixed thread pool với 2 threads
        // Số thread ít vì I/O bound, không cần nhiều threads
        this.executor = Executors.newFixedThreadPool(2, r -> {
            Thread thread = new Thread(r);
            thread.setName("ResourceLoader-Worker");
            thread.setDaemon(true); // Daemon: tự động dừng khi main dừng
            thread.setPriority(Thread.MIN_PRIORITY); // Priority thấp nhất
            return thread;
        });

        // Thread-safe maps
        this.imageCache = new ConcurrentHashMap<>();
        this.loadingFutures = new ConcurrentHashMap<>();

        System.out.println("[ResourceLoaderThread] Initialized with 2 worker threads");
    }


    // ==================== PUBLIC METHODS - ASYNC LOADING ====================

    /**
     * Load image bất đồng bộ
     * Method return ngay lập tức với Future
     * Caller có thể:
     * - Tiếp tục làm việc khác
     * - Gọi future.get() khi cần image (sẽ block nếu chưa load xong)
     *
     * @param imagePath Đường dẫn đến image file
     * @return Future<Image> để lấy kết quả sau
     */
    public Future<Image> loadImageAsync(String imagePath) {
        // Kiểm tra shutdown
        if (isShutdown) {
            System.err.println("[ResourceLoaderThread] Cannot load - thread pool is shutdown");
            return CompletableFuture.completedFuture(null);
        }

        // Kiểm tra cache trước
        // Nếu đã load rồi, return ngay không cần load lại
        if (imageCache.containsKey(imagePath)) {
            Image cachedImage = imageCache.get(imagePath);
            return CompletableFuture.completedFuture(cachedImage);
        }

        // Kiểm tra có đang load cùng image này không
        // Tránh load trùng lặp
        if (loadingFutures.containsKey(imagePath)) {
            return loadingFutures.get(imagePath);
        }

        // Submit task load image vào thread pool
        Future<Image> future = executor.submit(() -> {
            try {
                System.out.println("[ResourceLoaderThread] Loading: " + imagePath);

                // Load image từ file
                // Constructor của Image tự động load từ đường dẫn
                Image image = new Image(imagePath);

                // Kiểm tra load thành công không
                if (image.isError()) {
                    System.err.println("[ResourceLoaderThread] Error loading: " + imagePath);
                    Exception error = image.getException();
                    if (error != null) {
                        error.printStackTrace();
                    }
                    return null;
                }

                // Load thành công - lưu vào cache
                imageCache.put(imagePath, image);
                System.out.println("[ResourceLoaderThread] Loaded successfully: " + imagePath);

                return image;

            } catch (Exception e) {
                System.err.println("[ResourceLoaderThread] Exception loading: " + imagePath);
                e.printStackTrace();
                return null;

            } finally {
                // Remove khỏi loadingFutures khi xong
                loadingFutures.remove(imagePath);
            }
        });

        // Lưu future để track
        loadingFutures.put(imagePath, future);

        return future;
    }

    /**
     * Preload danh sách images
     * Load tất cả images trong list song song
     * Useful để preload resources lúc game start
     *
     * @param imagePaths Danh sách đường dẫn images cần load
     * @return Future<Void> - complete khi tất cả images đã load xong
     */
    public Future<Void> preloadImages(List<String> imagePaths) {
        if (isShutdown) {
            System.err.println("[ResourceLoaderThread] Cannot preload - thread pool is shutdown");
            return CompletableFuture.completedFuture(null);
        }

        System.out.println("[ResourceLoaderThread] Preloading " + imagePaths.size() + " images...");

        return executor.submit(() -> {
            try {
                // Tạo list các futures
                List<Future<Image>> futures = new java.util.ArrayList<>();

                // Start load tất cả images
                for (String imagePath : imagePaths) {
                    Future<Image> future = loadImageAsync(imagePath);
                    futures.add(future);
                }

                // Đợi tất cả load xong
                int successCount = 0;
                for (Future<Image> future : futures) {
                    try {
                        Image image = future.get(); // Block đợi load xong
                        if (image != null) {
                            successCount++;
                        }
                    } catch (Exception e) {
                        System.err.println("[ResourceLoaderThread] Error in preload");
                        e.printStackTrace();
                    }
                }

                System.out.println("[ResourceLoaderThread] Preload complete: "
                        + successCount + "/" + imagePaths.size() + " images loaded");

                return null;

            } catch (Exception e) {
                System.err.println("[ResourceLoaderThread] Preload failed");
                e.printStackTrace();
                return null;
            }
        });
    }


    // ==================== PUBLIC METHODS - SYNC LOADING ====================

    /**
     * Load image đồng bộ (blocking)
     * Method này sẽ block cho đến khi image load xong
     *
     * Use case:
     * - Khi PHẢI có image ngay lập tức
     * - Khi đã biết chắc image đã được preload
     *
     * @param imagePath Đường dẫn đến image
     * @return Image object hoặc null nếu load fail
     */
    public Image loadImageSync(String imagePath) {
        // Kiểm tra cache trước
        if (imageCache.containsKey(imagePath)) {
            return imageCache.get(imagePath);
        }

        // Nếu chưa có trong cache, load async rồi đợi
        try {
            Future<Image> future = loadImageAsync(imagePath);
            return future.get(); // Block đợi load xong

        } catch (InterruptedException e) {
            System.err.println("[ResourceLoaderThread] Load interrupted: " + imagePath);
            Thread.currentThread().interrupt();
            return null;

        } catch (ExecutionException e) {
            System.err.println("[ResourceLoaderThread] Load failed: " + imagePath);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lấy image từ cache (không load nếu chưa có)
     * Return ngay lập tức
     *
     * @param imagePath Đường dẫn đến image
     * @return Image nếu đã có trong cache, null nếu chưa load
     */
    public Image getFromCache(String imagePath) {
        return imageCache.get(imagePath);
    }

    /**
     * Kiểm tra image đã load chưa
     *
     * @param imagePath Đường dẫn đến image
     * @return true nếu image đã có trong cache
     */
    public boolean isLoaded(String imagePath) {
        return imageCache.containsKey(imagePath);
    }

    /**
     * Clear cache để giải phóng memory
     * Gọi khi chuyển level hoặc khi cần free memory
     */
    public void clearCache() {
        System.out.println("[ResourceLoaderThread] Clearing cache (" + imageCache.size() + " images)");
        imageCache.clear();
    }

    /**
     * Lấy số lượng images trong cache
     *
     * @return Số lượng images đã load
     */
    public int getCacheSize() {
        return imageCache.size();
    }


    // ==================== SHUTDOWN ====================

    /**
     * Shutdown thread pool một cách graceful
     * Tương tự AudioThread.shutdown()
     */
    public void shutdown() {
        if (isShutdown) {
            return;
        }

        System.out.println("[ResourceLoaderThread] Shutting down...");
        isShutdown = true;

        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("[ResourceLoaderThread] Force shutdown - timeout reached");
                executor.shutdownNow();

                if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                    System.err.println("[ResourceLoaderThread] Thread pool did not terminate");
                }
            }
        } catch (InterruptedException e) {
            System.err.println("[ResourceLoaderThread] Shutdown interrupted");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Clear cache khi shutdown
        clearCache();

        System.out.println("[ResourceLoaderThread] Shutdown complete");
    }

    /**
     * Kiểm tra đã shutdown chưa
     */
    public boolean isShutdown() {
        return isShutdown;
    }
}