package thread;

import javafx.scene.image.Image;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


public class ResourceLoaderThread {

    private static ResourceLoaderThread instance;

    public static synchronized ResourceLoaderThread getInstance() {
        if (instance == null) {
            instance = new ResourceLoaderThread();
        }
        return instance;
    }

    private final ExecutorService executor;

    private final Map<String, Image> imageCache;

    private final Map<String, Future<Image>> loadingFutures;


    private volatile boolean isShutdown;

    private ResourceLoaderThread() {
        this.isShutdown = false;


        this.executor = Executors.newFixedThreadPool(2, r -> {
            Thread thread = new Thread(r);
            thread.setName("ResourceLoader-Worker");
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY); // Priority thấp nhất
            return thread;
        });

        this.imageCache = new ConcurrentHashMap<>();
        this.loadingFutures = new ConcurrentHashMap<>();
    }

    public Future<Image> loadImageAsync(String imagePath) {
        // Kiểm tra shutdown
        if (isShutdown) {
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

        Future<Image> future = executor.submit(() -> {
            try {
                Image image = new Image(imagePath);

                if (image.isError()) {
                    Exception error = image.getException();
                    if (error != null) {
                        error.printStackTrace();
                    }
                    return null;
                }

                imageCache.put(imagePath, image);

                return image;

            } catch (Exception e) {
                e.printStackTrace();
                return null;

            } finally {
                loadingFutures.remove(imagePath);
            }
        });

        loadingFutures.put(imagePath, future);

        return future;
    }

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
                        System.err.println("ResourceLoaderThread Error in preload");
                        e.printStackTrace();
                    }
                }

                System.out.println("ResourceLoaderThread Preload complete: "
                        + successCount + "/" + imagePaths.size() + " images loaded");

                return null;

            } catch (Exception e) {
                System.err.println("[ResourceLoaderThread] Preload failed");
                e.printStackTrace();
                return null;
            }
        });
    }

    public Image loadImageSync(String imagePath) {
        // Kiểm tra cache trước
        if (imageCache.containsKey(imagePath)) {
            return imageCache.get(imagePath);
        }

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

    public Image getFromCache(String imagePath) {
        return imageCache.get(imagePath);
    }

    public boolean isLoaded(String imagePath) {
        return imageCache.containsKey(imagePath);
    }

    public void clearCache() {
        System.out.println("[ResourceLoaderThread] Clearing cache (" + imageCache.size() + " images)");
        imageCache.clear();
    }

    public int getCacheSize() {
        return imageCache.size();
    }

    public void shutdown() {
        if (isShutdown) {
            return;
        }

        System.out.println("ResourceLoaderThread Shutting down");
        isShutdown = true;

        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();

                if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                    System.err.println("ResourceLoaderThread Thread pool did not terminate");
                }
            }
        } catch (InterruptedException e) {
            System.err.println("ResourceLoaderThread Shutdown interrupted");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Clear cache khi shutdown
        clearCache();
    }

    /**
     * Kiểm tra đã shutdown chưa
     */
    public boolean isShutdown() {
        return isShutdown;
    }
}