package top.xiaoxuan010.learn.game.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 游戏关卡配置类
 * 根据gamelevel-config.plist配置文件中的信息管理鱼类生成
 */
public class GameLevelConfig {
    
    // Part 1 配置信息
    private static final String[] FISH_TYPES = {
        "fish01", "fish02", "fish03", "fish04", "fish05", "fish06", "fish07", "fish08",
        "fish09", "fish10", "fish11", "fish12", "fish13", "fish14", "fish15", "fish16"
    };
    
    private static final int[] SHOW_PROBABILITIES = {
        10, 10, 10, 10, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5
    };
    
    private static final int SHOAL_SUM_IN_SCREEN = 30; // 屏幕上同时显示的鱼的总数
    private static final int LEVEL_TIME = 300; // 关卡时间（秒）
    
    private static final Random random = new Random();
    
    /**
     * 根据配置生成鱼类信息
     * @return 鱼类信息列表，包含鱼的类型和位置
     */
    public static List<FishSpawnInfo> generateFishSpawnInfo() {
        List<FishSpawnInfo> fishList = new ArrayList<>();
        
        // 计算总概率
        int totalProbability = 0;
        for (int prob : SHOW_PROBABILITIES) {
            totalProbability += prob;
        }
        
        // 根据配置生成指定数量的鱼
        for (int i = 0; i < SHOAL_SUM_IN_SCREEN; i++) {
            // 根据概率选择鱼的类型
            int randomValue = random.nextInt(totalProbability);
            int currentSum = 0;
            int selectedFishIndex = 0;
            
            for (int j = 0; j < SHOW_PROBABILITIES.length; j++) {
                currentSum += SHOW_PROBABILITIES[j];
                if (randomValue < currentSum) {
                    selectedFishIndex = j;
                    break;
                }
            }
            
            // 生成随机位置
            int x = random.nextInt(600) + 50; // 50-650范围内
            int y = random.nextInt(300) + 100; // 100-400范围内，避免与UI重叠
            
            // 根据鱼的类型设置大小
            int width, height;
            if (selectedFishIndex < 4) { // fish01-fish04 小鱼
                width = 60;
                height = 40;
            } else if (selectedFishIndex < 8) { // fish05-fish08 中等鱼
                width = 80;
                height = 60;
            } else { // fish09-fish16 大鱼
                width = 100;
                height = 80;
            }
            
            FishSpawnInfo fishInfo = new FishSpawnInfo(
                FISH_TYPES[selectedFishIndex], 
                selectedFishIndex + 1, // 级别从1开始
                x, y, width, height
            );
            
            fishList.add(fishInfo);
        }
        
        return fishList;
    }
    
    /**
     * 鱼类生成信息
     */
    public static class FishSpawnInfo {
        public final String fishType;
        public final int level;
        public final int x, y, width, height;
        
        public FishSpawnInfo(String fishType, int level, int x, int y, int width, int height) {
            this.fishType = fishType;
            this.level = level;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
    
    public static int getShoalSumInScreen() {
        return SHOAL_SUM_IN_SCREEN;
    }
    
    public static int getLevelTime() {
        return LEVEL_TIME;
    }
}
