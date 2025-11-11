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
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        });

        this.imageCache = new ConcurrentHashMap<>();
        this.loadingFutures = new ConcurrentHashMap<>();
    }

    public Future<Image> loadImageAsync(String imagePath) {
        if (isShutdown) {
            return CompletableFuture.completedFuture(null);
        }

        if (imageCache.containsKey(imagePath)) {
            Image cachedImage = imageCache.get(imagePath);
            return CompletableFuture.completedFuture(cachedImage);
        }

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
            return CompletableFuture.completedFuture(null);
        }

        return executor.submit(() -> {
            try {
                List<Future<Image>> futures = new java.util.ArrayList<>();

                for (String imagePath : imagePaths) {
                    Future<Image> future = loadImageAsync(imagePath);
                    futures.add(future);
                }

                for (Future<Image> future : futures) {
                    try {
                        Image image = future.get();
                        if (image != null) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public Image loadImageSync(String imagePath) {
        if (imageCache.containsKey(imagePath)) {
            return imageCache.get(imagePath);
        }

        try {
            Future<Image> future = loadImageAsync(imagePath);
            return future.get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;

        } catch (ExecutionException e) {
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
        imageCache.clear();
    }

    public int getCacheSize() {
        return imageCache.size();
    }

    public void shutdown() {
        if (isShutdown) {
            return;
        }

        isShutdown = true;

        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();

                if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        clearCache();
    }

    public boolean isShutdown() {
        return isShutdown;
    }

}
