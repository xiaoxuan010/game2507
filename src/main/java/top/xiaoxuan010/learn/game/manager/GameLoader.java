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
import top.xiaoxuan010.learn.game.element.CannonBg;
import top.xiaoxuan010.learn.game.element.CannonDowngradeBtn;
import top.xiaoxuan010.learn.game.element.CannonTower;
import top.xiaoxuan010.learn.game.element.CannonUpgradeBtn;
import top.xiaoxuan010.learn.game.element.CoinsBg;
import top.xiaoxuan010.learn.game.element.CountdownBg;
import top.xiaoxuan010.learn.game.element.GameBackground;
import top.xiaoxuan010.learn.game.element.components.Digit;
import top.xiaoxuan010.learn.game.element.utils.GameStateDataManager;
import top.xiaoxuan010.learn.game.manager.utils.ImageResourceLoader;

@Slf4j
public class GameLoader {
    private final static ElementManager ELEMENT_MANAGER = ElementManager.getInstance();

    // 图片资源映射
    public static Map<String, ImageIcon> imgMap = new HashMap<>();

    // 字体缓存
    public static Map<String, Font> fontMap = new HashMap<>();

    // 线程池大小可以根据实际情况调整
    private static final int THREAD_POOL_SIZE = 8;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    /**
     * Preload essential resources (resources required for the menu)
     */
    public static void preloadEssentialResources() {
        log.info("Start preloading essential resources...");
        long startTime = System.currentTimeMillis();

        try {
            // Load menu background images
            loadEssentialImages();

            // Load fonts
            loadFonts();

            long endTime = System.currentTimeMillis();
            log.info("Essential resources preloaded, cost {} ms", (endTime - startTime));

        } catch (Exception e) {
            log.error("Failed to preload essential resources", e);
            throw new RuntimeException("Failed to load essential resources", e);
        }
    }

    /**
     * Load essential image resources
     */
    private static void loadEssentialImages() {
        try {
            // Load menu background
            imgMap.put("background.menu", new ImageIcon(ImageResourceLoader.load("images/maps/start.jpg")));
            log.debug("Menu background image loaded");

        } catch (IOException e) {
            log.error("Failed to load essential image resources", e);
            throw new RuntimeException("Failed to load essential image resources", e);
        }
    }

    /**
     * Load font resources
     */
    private static void loadFonts() {
        try {
            // Load font files from resources/font directory
            InputStream fontStream = GameLoader.class.getClassLoader()
                    .getResourceAsStream("font/hk4e_zh-cn.ttf");

            if (fontStream != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                fontMap.put("default", customFont);
                log.debug("Custom font loaded");
                fontStream.close();
            } else {
                // If font file is not found, use system default font
                Font defaultFont = new Font("微软雅黑", Font.PLAIN, 12);
                fontMap.put("default", defaultFont);
                log.warn("Custom font file not found, using system default font");
            }

        } catch (IOException | FontFormatException e) {
            // If font loading fails, use system default font
            Font defaultFont = new Font("微软雅黑", Font.PLAIN, 12);
            fontMap.put("default", defaultFont);
            log.warn("Font loading failed, using system default font", e);
        }
    }

    public static void loadImages(Consumer<Integer> progressCallback) {
        long startTime = System.currentTimeMillis();
        log.trace("Start loading image resources...");
        try (InputStream inputStream = GameLoader.class.getClassLoader()
                .getResourceAsStream("images/images.properties")) {
            if (inputStream == null) {
                throw new IOException("Cannot find images.properties file");
            }
            Properties properties = new Properties();
            properties.load(inputStream);

            // Get total number of images for progress calculation
            int totalImages = properties.size();
            java.util.concurrent.atomic.AtomicInteger loadedImages = new java.util.concurrent.atomic.AtomicInteger(0);

            // Use CompletableFuture to collect all loading tasks
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
                                    int progress = (int) ((double) loadedImages.incrementAndGet() / totalImages * 100);
                                    progressCallback.accept(progress);
                                }
                            }
                        }
                    }, executorService))
                    .toArray(CompletableFuture[]::new);

            // Wait for all tasks to complete
            CompletableFuture.allOf(futures).join();

            long endTime = System.currentTimeMillis();
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

    public static void loadBackground(int mapId) {
        GameBackground gameBackground = new GameBackground(2);
        ELEMENT_MANAGER.addElement(gameBackground, GameElementType.MAP);
    }

    public static void loadPlayer() {
        CannonTower cannonTower = new CannonTower();
        cannonTower.setLevel(1);
        ELEMENT_MANAGER.addElement(cannonTower, GameElementType.PLAYER);
    }


    public static void loadUI() {
        GameStateDataManager gameStateManager = GameStateDataManager.getInstance();
        gameStateManager.reset();

        CannonUpgradeBtn cannonUpgradeBtn = new CannonUpgradeBtn();
        ELEMENT_MANAGER.addElement(cannonUpgradeBtn, GameElementType.UI);
        CannonDowngradeBtn cannonDowngradeBtn = new CannonDowngradeBtn();
        ELEMENT_MANAGER.addElement(cannonDowngradeBtn, GameElementType.UI);
        
        // Add countdown display
        CountdownBg countdownBg = new CountdownBg();
        ELEMENT_MANAGER.addElement(countdownBg, GameElementType.UI);
        
        // Add coins display
        CoinsBg coinsBg = new CoinsBg();
        ELEMENT_MANAGER.addElement(coinsBg, GameElementType.UI);

        CannonBg cannonBg = new CannonBg();
        ELEMENT_MANAGER.addElement(cannonBg, GameElementType.MAP);

        // Load coins background and digit display
        CoinsBg coinsBackground = new CoinsBg();
        ELEMENT_MANAGER.addElement(coinsBackground, GameElementType.MAP);
        Digit digit1 = new Digit(590, 435, () -> (GameStateDataManager.getInstance().getCoins() / 100) % 10); // Hundreds
                                                                                                              // place
        ELEMENT_MANAGER.addElement(digit1, GameElementType.UI);
        Digit digit2 = new Digit(605, 435, () -> (GameStateDataManager.getInstance().getCoins() / 10) % 10); // Tens
                                                                                                             // place
        ELEMENT_MANAGER.addElement(digit2, GameElementType.UI);
        Digit digit3 = new Digit(620, 435, () -> GameStateDataManager.getInstance().getCoins() % 10); // Units place
        ELEMENT_MANAGER.addElement(digit3, GameElementType.UI);

        // Load time background and digit display
        ELEMENT_MANAGER.addElement(countdownBg, GameElementType.MAP);
        Digit timeDigit1 = new Digit(165, 435,
                () -> (GameStateDataManager.getInstance().getGameCountdown() / 10));
        ELEMENT_MANAGER.addElement(timeDigit1, GameElementType.UI);
        Digit timeDigit2 = new Digit(180, 435,
                () -> (GameStateDataManager.getInstance().getGameCountdown() % 10));
        ELEMENT_MANAGER.addElement(timeDigit2, GameElementType.UI);

    }
}
