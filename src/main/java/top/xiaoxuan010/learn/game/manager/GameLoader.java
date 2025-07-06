package top.xiaoxuan010.learn.game.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;

import top.xiaoxuan010.learn.game.element.CannonTower;
import top.xiaoxuan010.learn.game.element.GameBackground;
import top.xiaoxuan010.learn.game.manager.utils.ImageResourceLoader;

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
                System.out.println("加载图片资源: " + key);
                String value = properties.getProperty(key);
                String imagePath = "images/" + value.trim();
                if (value != null) {
                    ImageIcon icon = new ImageIcon(ImageResourceLoader.load(imagePath));
                    imgMap.put(key, icon);
                }
            }
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
        ELEMENT_MANAGER.addElement(cannonTower, GameElementType.PLAYER);
    }

    public static void loadEnemies() {

    }
}
