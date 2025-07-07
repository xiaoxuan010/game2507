package top.xiaoxuan010.learn.game.manager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 游戏关卡配置加载器
 * 用于读取和解析gamelevel-config.plist文件
 */
public class GameLevelConfigLoader {
    
    public static class LevelConfig {
        public int part;
        public String[] fishTypes;
        public int[] showProbabilities;
        public int shoalSumInScreen;
        public int time;
        public int nextPart;
        public String bgMusic;
        public String background;
        
        @Override
        public String toString() {
            return "LevelConfig{" +
                    "part=" + part +
                    ", fishTypes=" + java.util.Arrays.toString(fishTypes) +
                    ", showProbabilities=" + java.util.Arrays.toString(showProbabilities) +
                    ", shoalSumInScreen=" + shoalSumInScreen +
                    ", time=" + time +
                    ", nextPart=" + nextPart +
                    ", bgMusic='" + bgMusic + '\'' +
                    ", background='" + background + '\'' +
                    '}';
        }
    }
    
    /**
     * 加载游戏关卡配置
     * @return 关卡配置列表
     */
    public static List<LevelConfig> loadLevelConfigs() {
        List<LevelConfig> configs = new ArrayList<>();
        
        try {
            // 从resources目录加载配置文件
            InputStream inputStream = GameLevelConfigLoader.class.getClassLoader()
                    .getResourceAsStream("config/gamelevel-config.plist");
            
            if (inputStream == null) {
                System.err.println("配置文件未找到: config/gamelevel-config.plist");
                return getDefaultConfigs();
            }
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            
            // 获取根元素
            Element root = document.getDocumentElement();
            NodeList arrayNodes = root.getElementsByTagName("array");
            
            if (arrayNodes.getLength() > 0) {
                Element arrayElement = (Element) arrayNodes.item(0);
                NodeList dictNodes = arrayElement.getElementsByTagName("dict");
                
                for (int i = 0; i < dictNodes.getLength(); i++) {
                    Element dictElement = (Element) dictNodes.item(i);
                    LevelConfig config = parseLevelConfig(dictElement);
                    if (config != null) {
                        configs.add(config);
                    }
                }
            }
            
            inputStream.close();
            
        } catch (Exception e) {
            System.err.println("解析配置文件时出错: " + e.getMessage());
            e.printStackTrace();
            return getDefaultConfigs();
        }
        
        return configs.isEmpty() ? getDefaultConfigs() : configs;
    }
    
    /**
     * 解析单个关卡配置
     */
    private static LevelConfig parseLevelConfig(Element dictElement) {
        try {
            LevelConfig config = new LevelConfig();
            Map<String, String> keyValueMap = parseDict(dictElement);
            
            config.part = Integer.parseInt(keyValueMap.getOrDefault("part", "1"));
            
            String fishString = keyValueMap.getOrDefault("fish", "");
            config.fishTypes = fishString.split(";");
            
            String probString = keyValueMap.getOrDefault("showProbability", "");
            String[] probStrings = probString.split(";");
            config.showProbabilities = new int[probStrings.length];
            for (int i = 0; i < probStrings.length; i++) {
                config.showProbabilities[i] = Integer.parseInt(probStrings[i].trim());
            }
            
            config.shoalSumInScreen = Integer.parseInt(keyValueMap.getOrDefault("shoalSumInScreen", "30"));
            config.time = Integer.parseInt(keyValueMap.getOrDefault("time", "300"));
            config.nextPart = Integer.parseInt(keyValueMap.getOrDefault("nextpart", "1"));
            config.bgMusic = keyValueMap.getOrDefault("bgMusic", "bg.ogg");
            config.background = keyValueMap.getOrDefault("background", "bg/fishlightbg_3.jpg");
            
            return config;
            
        } catch (Exception e) {
            System.err.println("解析关卡配置时出错: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 解析dict元素
     */
    private static Map<String, String> parseDict(Element dictElement) {
        Map<String, String> map = new HashMap<>();
        NodeList children = dictElement.getChildNodes();
        
        String currentKey = null;
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String tagName = element.getTagName();
                
                if ("key".equals(tagName)) {
                    currentKey = element.getTextContent().trim();
                } else if (currentKey != null) {
                    String value = element.getTextContent().trim();
                    map.put(currentKey, value);
                    currentKey = null;
                }
            }
        }
        
        return map;
    }
    
    /**
     * 获取默认配置（当文件读取失败时使用）
     */
    private static List<LevelConfig> getDefaultConfigs() {
        List<LevelConfig> configs = new ArrayList<>();
        
        // Part 1 默认配置
        LevelConfig part1 = new LevelConfig();
        part1.part = 1;
        part1.fishTypes = new String[]{
            "fish01", "fish02", "fish03", "fish04", "fish05", "fish06", "fish07", "fish08",
            "fish09", "fish10", "fish11", "fish12", "fish13", "fish14", "fish15", "fish16"
        };
        part1.showProbabilities = new int[]{10, 10, 10, 10, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
        part1.shoalSumInScreen = 30;
        part1.time = 300;
        part1.nextPart = 2;
        part1.bgMusic = "bg.ogg";
        part1.background = "bg/fishlightbg_3.jpg";
        
        configs.add(part1);
        
        // Part 2 默认配置
        LevelConfig part2 = new LevelConfig();
        part2.part = 2;
        part2.fishTypes = new String[]{"fish01", "fish02", "fish05", "fish07", "fish09"};
        part2.showProbabilities = new int[]{35, 35, 10, 10, 10};
        part2.shoalSumInScreen = 7;
        part2.time = 300;
        part2.nextPart = 1;
        part2.bgMusic = "bg";
        part2.background = "bg/fishlightbg_1.jpg";
        
        configs.add(part2);
        
        return configs;
    }
    
    /**
     * 根据part获取特定的关卡配置
     */
    public static LevelConfig getLevelConfig(int part) {
        List<LevelConfig> configs = loadLevelConfigs();
        for (LevelConfig config : configs) {
            if (config.part == part) {
                return config;
            }
        }
        return configs.isEmpty() ? null : configs.get(0);
    }
}
