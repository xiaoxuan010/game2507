package top.xiaoxuan010.learn.game.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
import top.xiaoxuan010.learn.game.element.utils.GameStateManager;
import top.xiaoxuan010.learn.game.manager.utils.ImageResourceLoader;

@Slf4j
public class GameLoader {
    private final static ElementManager ELEMENT_MANAGER = ElementManager.getInstance();

    // 图片资源映射
    public static Map<String, ImageIcon> imgMap = new HashMap<>();

    public static void loadImages() {
        try (InputStream inputStream = GameLoader.class.getClassLoader()
                .getResourceAsStream("images/images.properties")) {
            if (inputStream == null) {
                throw new IOException("无法找到 images.properties 文件");
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                String imagePath = "images/" + value.trim();
                if (value != null) {
                    ImageIcon icon = new ImageIcon(ImageResourceLoader.load(imagePath));
                    imgMap.put(key, icon);
                }
            }
            log.info("Image resources loaded successfully, total {} images loaded", imgMap.size());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
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

    }

    public static void loadUI() {
        GameStateManager gameStateManager = GameStateManager.getInstance();
        gameStateManager.reset();

        CannonUpgradeBtn cannonUpgradeBtn = new CannonUpgradeBtn();
        ELEMENT_MANAGER.addElement(cannonUpgradeBtn, GameElementType.UI);
        CannonDowngradeBtn cannonDowngradeBtn = new CannonDowngradeBtn();
        ELEMENT_MANAGER.addElement(cannonDowngradeBtn, GameElementType.UI);
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
        CountdownBg countdownBg = new CountdownBg();
        ELEMENT_MANAGER.addElement(countdownBg, GameElementType.MAP);
        Digit timeDigit1 = new Digit(165, 435,
                () -> (GameStateManager.getInstance().getGameCountdown() / 10));
        ELEMENT_MANAGER.addElement(timeDigit1, GameElementType.UI);
        Digit timeDigit2 = new Digit(180, 435,
                () -> (GameStateManager.getInstance().getGameCountdown() % 10));
        ELEMENT_MANAGER.addElement(timeDigit2, GameElementType.UI);

    }
}
