package top.xiaoxuan010.learn.game.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.element.CannonBg;
import top.xiaoxuan010.learn.game.element.CannonDowngradeBtn;
import top.xiaoxuan010.learn.game.element.CannonTower;
import top.xiaoxuan010.learn.game.element.CannonUpgradeBtn;
import top.xiaoxuan010.learn.game.element.CoinsBg;
import top.xiaoxuan010.learn.game.element.CountdownBg;
import top.xiaoxuan010.learn.game.element.Fish;
import top.xiaoxuan010.learn.game.element.GameBackground;
import top.xiaoxuan010.learn.game.element.components.Digit;
import top.xiaoxuan010.learn.game.element.utils.GameStateManager;
import top.xiaoxuan010.learn.game.manager.utils.ImageResourceLoader;

@Slf4j
public class GameLoader {
    private final static ElementManager ELEMENT_MANAGER = ElementManager.getInstance();

    // 图片资源映射
    public static Map<String, ImageIcon> imgMap = new HashMap<>();

    // 线程池大小可以根据实际情况调整
    private static final int THREAD_POOL_SIZE = 8;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public static void loadImages() {
        long startTime = System.currentTimeMillis();
        log.trace("Start loading image resources...");
        try (InputStream inputStream = GameLoader.class.getClassLoader()
                .getResourceAsStream("images/images.properties")) {
            if (inputStream == null) {
                throw new IOException("无法找到 images.properties 文件");
            }
            Properties properties = new Properties();
            properties.load(inputStream);

            // 使用 CompletableFuture 收集所有加载任务
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
                            }
                        }
                    }, executorService))
                    .toArray(CompletableFuture[]::new);

            // 等待所有任务完成
            CompletableFuture.allOf(futures).join();

            long endTime = System.currentTimeMillis();
            log.info("Image resources loaded successfully, total {} images loaded", imgMap.size());
            log.debug("Finished loading images in {} ms", (endTime - startTime));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭线程池
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

    public static void loadEnemies() {
        // 初始化鱼类管理器并生成初始鱼群
        FishManager fishManager = FishManager.getInstance();
        fishManager.spawnInitialFishes();
        // 创建一些1级鱼
        for (int i = 0; i < 3; i++) {
            Fish fish = new Fish(100 + i * 120, 100, 80, 60, 1);
            ELEMENT_MANAGER.addElement(fish, GameElementType.ENEMY);
        }

        // 创建一些2级鱼 (fish.lv2系列)
        for (int i = 0; i < 4; i++) {
            Fish fish = new Fish(150 + i * 120, 200, 100, 80, 2);
            ELEMENT_MANAGER.addElement(fish, GameElementType.ENEMY);
        }

        // 创建更多不同位置的2级鱼
        for (int i = 0; i < 3; i++) {
            Fish fish = new Fish(80 + i * 150, 350, 100, 80, 2);
            ELEMENT_MANAGER.addElement(fish, GameElementType.ENEMY);
        }

        // 创建一些移动的2级鱼
        for (int i = 0; i < 2; i++) {
            Fish fish = new Fish(50 + i * 200, 450, 100, 80, 2);
            ELEMENT_MANAGER.addElement(fish, GameElementType.ENEMY);
        }
    }

    public static void loadUI() {
        GameStateManager gameStateManager = GameStateManager.getInstance();
        gameStateManager.reset();

        CannonUpgradeBtn cannonUpgradeBtn = new CannonUpgradeBtn();
        ELEMENT_MANAGER.addElement(cannonUpgradeBtn, GameElementType.UI);
        CannonDowngradeBtn cannonDowngradeBtn = new CannonDowngradeBtn();
        ELEMENT_MANAGER.addElement(cannonDowngradeBtn, GameElementType.UI);
        
        // 添加倒计时显示
        CountdownBg countdownBg = new CountdownBg();
        ELEMENT_MANAGER.addElement(countdownBg, GameElementType.UI);
        
        // 添加金币显示
        CoinsBg coinsBg = new CoinsBg();
        ELEMENT_MANAGER.addElement(coinsBg, GameElementType.UI);

        CannonBg cannonBg = new CannonBg();
        ELEMENT_MANAGER.addElement(cannonBg, GameElementType.MAP);

        // 加载金币背景和数字显示
        CoinsBg coinsBackground = new CoinsBg();
        ELEMENT_MANAGER.addElement(coinsBackground, GameElementType.MAP);
        Digit digit1 = new Digit(590, 435, () -> (GameStateManager.getInstance().getCoins() / 100) % 10); // 百位
        ELEMENT_MANAGER.addElement(digit1, GameElementType.UI);
        Digit digit2 = new Digit(605, 435, () -> (GameStateManager.getInstance().getCoins() / 10) % 10); // 十位
        ELEMENT_MANAGER.addElement(digit2, GameElementType.UI);
        Digit digit3 = new Digit(620, 435, () -> GameStateManager.getInstance().getCoins() % 10); // 个位
        ELEMENT_MANAGER.addElement(digit3, GameElementType.UI);

        // 加载时间背景和数字显示
        ELEMENT_MANAGER.addElement(countdownBg, GameElementType.MAP);
        Digit timeDigit1 = new Digit(165, 435,
                () -> (GameStateManager.getInstance().getGameCountdown() / 10));
        ELEMENT_MANAGER.addElement(timeDigit1, GameElementType.UI);
        Digit timeDigit2 = new Digit(180, 435,
                () -> (GameStateManager.getInstance().getGameCountdown() % 10));
        ELEMENT_MANAGER.addElement(timeDigit2, GameElementType.UI);

    }
}
