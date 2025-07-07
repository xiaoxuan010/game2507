package top.xiaoxuan010.learn.game.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.element.CannonDowngradeBtn;
import top.xiaoxuan010.learn.game.element.CannonTower;
import top.xiaoxuan010.learn.game.element.CannonUpgradeBtn;
import top.xiaoxuan010.learn.game.element.Fish;
import top.xiaoxuan010.learn.game.element.GameBackground;
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
        // 使用配置文件信息生成鱼类
        List<GameLevelConfig.FishSpawnInfo> fishSpawnInfos = GameLevelConfig.generateFishSpawnInfo();
        
        log.info("Loading {} fish according to level configuration", fishSpawnInfos.size());
        
        for (GameLevelConfig.FishSpawnInfo fishInfo : fishSpawnInfos) {
            Fish fish = new Fish(
                fishInfo.x, 
                fishInfo.y, 
                fishInfo.width, 
                fishInfo.height, 
                fishInfo.level,
                fishInfo.fishType
            );
            ELEMENT_MANAGER.addElement(fish, GameElementType.ENEMY);
        }
        
        log.info("Successfully loaded {} fish to the game", fishSpawnInfos.size());
    }

    public static void loadUI() {
        CannonUpgradeBtn cannonUpgradeBtn = new CannonUpgradeBtn();
        ELEMENT_MANAGER.addElement(cannonUpgradeBtn, GameElementType.UI);
        CannonDowngradeBtn cannonDowngradeBtn = new CannonDowngradeBtn();
        ELEMENT_MANAGER.addElement(cannonDowngradeBtn, GameElementType.UI);
    }
}
