package top.xiaoxuan010.learn.game.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;

import top.xiaoxuan010.learn.game.element.Enemy;
import top.xiaoxuan010.learn.game.element.GameElement;
import top.xiaoxuan010.learn.game.element.GameElementDirection;
import top.xiaoxuan010.learn.game.element.GameMapBlock;
import top.xiaoxuan010.learn.game.element.Player;

public class GameLoader {
    private final static ElementManager ELEMENT_MANAGER = ElementManager.getInstance();

    private static Properties imageProps = new Properties();

    // 图片资源映射
    public static Map<String, ImageIcon> imgMap = new HashMap<>();

    private static Properties properties = new Properties();

    public static void loadMap(int mapId) {
        String mapPath = "maps/data/" + mapId + ".map";
        ClassLoader classLoader = GameLoader.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(mapPath);
        if (inputStream == null) {
            throw new RuntimeException("Map file not found: " + mapPath);
        }
        try {
            properties.load(inputStream);
            Enumeration<?> propertyNames = properties.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String blockName = (String) propertyNames.nextElement();
                String propertyValue = properties.getProperty(blockName);
                String[] coordinates = propertyValue.split(";");
                for (String coordinate : coordinates) {
                    String[] xy = coordinate.split(",");
                    int x = Integer.parseInt(xy[0].trim());
                    int y = Integer.parseInt(xy[1].trim());

                    GameElement element = new GameMapBlock(x, y, blockName);
                    ELEMENT_MANAGER.addElement(element, GameElementType.MAP);

                    // System.out.println("Loaded map block: " + blockName + " at (" + x + ", " + y
                    // + ")");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load map: " + mapPath, e);
        }
    }

    public static void loadImages() {
        String propertiesPath = "images.properties";
        ClassLoader classLoader = GameLoader.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(propertiesPath);
        if (inputStream == null) {
            throw new RuntimeException("Image properties file not found: " + propertiesPath);
        }
        imageProps.clear();
        try {
            imageProps.load(inputStream);
        } catch (IOException e) {
            System.err.println("Failed to load image properties file: " + propertiesPath);
            e.printStackTrace();
        }
        Enumeration<?> propertyNames = imageProps.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String key = (String) propertyNames.nextElement();
            String value = imageProps.getProperty(key);
            try {
                ImageIcon icon = new ImageIcon(classLoader.getResource(value));
                imgMap.put(key, icon);
            } catch (Exception e) {
                System.err.println("Failed to load image: " + value);
                e.printStackTrace();
                continue;
            }
            // System.out.println("Loaded image: " + key + " -> " + value);
        }

    }

    public static void loadPlayers() {
        GameElement player1 = new Player(100, 100, GameElementDirection.UP);
        ELEMENT_MANAGER.addElement(player1, GameElementType.PLAYER);
    }

    public static void loadEnemies() {
        for (int i = 0; i < 5; i++) {
            Enemy enemy = new Enemy();
            ELEMENT_MANAGER.addElement(enemy, GameElementType.ENEMY);
        }
    }
}
