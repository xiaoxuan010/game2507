package top.xiaoxuan010.learn.game.manager;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.swing.ImageIcon;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.manager.utils.ImageResourceLoader;

@Slf4j
public class GameLoader {
    // 图片资源映射
    public static Map<String, ImageIcon> imgMap = new HashMap<>();

    // 字体缓存
    public static Map<String, Font> fontMap = new HashMap<>();

    public static boolean isLoaded = false;

    // 线程池大小可以根据实际情况调整
    private static final int THREAD_POOL_SIZE = 8;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public static void preloadEssentialResources() {
        log.info("Start preloading essential resources...");
        long startTime = System.currentTimeMillis();

        try {
            loadEssentialImages();
            loadFonts();

            long endTime = System.currentTimeMillis();
            log.info("Essential resources preloaded, cost {} ms", (endTime - startTime));

        } catch (Exception e) {
            log.error("Failed to preload essential resources", e);
            throw new RuntimeException("Failed to load essential resources", e);
        }
    }

    private static void loadEssentialImages() {
        try {
            imgMap.put("background.menu", new ImageIcon(ImageResourceLoader.load("images/maps/start.jpg")));
            log.debug("Menu background image loaded");

        } catch (IOException e) {
            log.error("Failed to load essential image resources", e);
            throw new RuntimeException("Failed to load essential image resources", e);
        }
    }

    private static void loadFonts() {
        try {
            // 加载自定义字体
            InputStream fontStream = GameLoader.class.getClassLoader()
                    .getResourceAsStream("font/hk4e_zh-cn.ttf");

            if (fontStream != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                fontMap.put("default", customFont);
                log.debug("Custom font loaded");
                fontStream.close();
            } else {
                // 如果加载字体失败，使用系统默认字体
                Font defaultFont = new Font("微软雅黑", Font.PLAIN, 12);
                fontMap.put("default", defaultFont);
                log.warn("Custom font file not found, using system default font");
            }

        } catch (IOException | FontFormatException e) {
            Font defaultFont = new Font("微软雅黑", Font.PLAIN, 12);
            fontMap.put("default", defaultFont);
            log.warn("Font loading failed, using system default font", e);
        }
    }

    public static void loadImages(Consumer<Float> progressCallback) {
        long startTime = System.currentTimeMillis();
        log.trace("Start loading image resources...");
        try (InputStream inputStream = GameLoader.class.getClassLoader()
                .getResourceAsStream("images/images.properties")) {
            if (inputStream == null) {
                throw new IOException("Cannot find images.properties file");
            }
            Properties properties = new Properties();
            properties.load(inputStream);

            // 初始化图片资源映射
            int totalImages = properties.size();
            java.util.concurrent.atomic.AtomicInteger loadedImages = new java.util.concurrent.atomic.AtomicInteger(0);

            // 使用 CompletableFuture 异步加载图片资源
            CompletableFuture<?>[] futures = properties.stringPropertyNames().stream()
                    .map(key -> CompletableFuture.runAsync(() -> {
                        String value = properties.getProperty(key);
                        String imagePath = "images/" + value.trim();
                        if (value != null) {
                            try {
                                ImageIcon icon = new ImageIcon(ImageResourceLoader.load(imagePath));
                                synchronized (imgMap) {
                                    imgMap.put(key, icon);
                                }
                                log.debug("Loaded image: key={}, path={}", key, imagePath);
                            } catch (IOException e) {
                                log.error("Failed to load image: key={}, path={}", key, imagePath, e);
                            } finally {
                                // Update progress after loading
                                synchronized (GameLoader.class) {
                                    float progress = ((float) loadedImages.incrementAndGet() / totalImages
                                            * 100);
                                    progressCallback.accept(progress);
                                }
                            }
                        }
                    }, executorService))
                    .toArray(CompletableFuture[]::new);

            // 等待所有异步任务完成
            CompletableFuture.allOf(futures).join();

            long endTime = System.currentTimeMillis();
            isLoaded = true; // 标记资源加载完成
            log.info("Image resources loaded successfully, total {} images loaded", imgMap.size());
            log.debug("Finished loading images in {} ms", (endTime - startTime));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            // Close thread pool
            executorService.shutdown();
        }
    }

}
