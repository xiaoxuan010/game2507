package top.xiaoxuan010.learn.game.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 游戏关卡配置类
 * 根据gamelevel-config.plist配置文件中的信息管理鱼类生成
 */
public class GameLevelConfig {
    public int part;
    public List<String> fishTypes;
    public List<Integer> showProbabilities;
    public int shoalSumInScreen;
    public int time;
    public int nextpart;
    public String bgMusic;
    public Integer background;

    private static final Random random = new Random();

    public GameLevelConfig(int part, String fishStr, String probabilityStr,
            int shoalSumInScreen, int time, int nextpart,
            String bgMusic, String background) {
        this.part = part;
        this.fishTypes = Arrays.asList(fishStr.split(";"));
        this.showProbabilities = Arrays.stream(probabilityStr.split(";"))
                .map(Integer::parseInt)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        this.shoalSumInScreen = shoalSumInScreen;
        this.time = time;
        this.nextpart = nextpart;
        this.bgMusic = bgMusic;
        this.background = Integer.valueOf(background);
    }
    
    /**
     * 根据配置生成鱼类信息
     * @return 鱼类信息列表，包含鱼的类型和位置
     */
    public List<FishSpawnInfo> generateFishSpawnInfo() {
        List<FishSpawnInfo> fishList = new ArrayList<>();
        
        // 计算总概率
        int totalProbability = showProbabilities.stream().mapToInt(Integer::intValue).sum();
        
        // 根据配置生成指定数量的鱼
        for (int i = 0; i < shoalSumInScreen; i++) {
            // 根据概率选择鱼的类型
            int randomValue = random.nextInt(totalProbability);
            int currentSum = 0;
            int selectedFishIndex = 0;
            
            for (int j = 0; j < showProbabilities.size(); j++) {
                currentSum += showProbabilities.get(j);
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
            int fishNumber = Integer.parseInt(fishTypes.get(selectedFishIndex).replaceAll("\\D", ""));
            if (fishNumber <= 4) { // fish01-fish04 小鱼
                width = 60;
                height = 40;
            } else if (fishNumber <= 8) { // fish05-fish08 中等鱼
                width = 80;
                height = 60;
            } else { // fish09-fish16 大鱼
                width = 100;
                height = 80;
            }
            
            FishSpawnInfo fishInfo = new FishSpawnInfo(
                    fishTypes.get(selectedFishIndex),
                    fishNumber,
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
}
