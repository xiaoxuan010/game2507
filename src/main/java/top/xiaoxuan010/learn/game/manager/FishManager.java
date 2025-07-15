package top.xiaoxuan010.learn.game.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import top.xiaoxuan010.learn.game.element.Fish;

/**
 * 鱼类管理器
 * 负责根据GameLevelConfig配置生成和管理鱼类
 */
public class FishManager {
    private static FishManager instance;
    private List<Fish> activeFishes;
    private long lastBatchSpawnTime;
    private Random random;
    private ElementManager elementManager;
    private GameLevelConfig levelConfig;
    
    private static final long BATCH_SPAWN_INTERVAL = 2000;
    private static final int MIN_BATCH_SIZE = 4;
    private static final int MAX_BATCH_SIZE = 6;
    
    private FishManager() {
        this.activeFishes = new ArrayList<>();
        this.random = new Random();
        this.elementManager = ElementManager.getInstance();
        this.lastBatchSpawnTime = 0;
    }
    
    public static synchronized FishManager getInstance() {
        if (instance == null) {
            instance = new FishManager();
        }
        return instance;
    }
    
    /**
     * 设置关卡配置
     * 
     * @param config 关卡配置
     */
    public void setLevelConfig(GameLevelConfig config) {
        this.levelConfig = config;
        System.out.println("设置关卡配置: Level " + config.part +
                ", 鱼类数量: " + config.fishTypes.size() +
                ", 屏幕鱼数: " + config.shoalSumInScreen);
    }

    /**
     * 更新鱼类管理器
     */
    public void update() {
        long currentTime = System.currentTimeMillis();
        
        // 清理非活动的鱼
        cleanupInactiveFishes();
        
        // 检查是否需要批次生成鱼
        if (currentTime - lastBatchSpawnTime > BATCH_SPAWN_INTERVAL) {
            spawnBatchOfFish();
            lastBatchSpawnTime = currentTime;
        }
    }
    
    /**
     * 批次生成鱼
     */
    private void spawnBatchOfFish() {
        if (levelConfig == null) {
            System.out.println("警告: 没有关卡配置，跳过鱼类生成");
            return;
        }

        // 检查当前屏幕上的鱼数量是否已达到上限
        if (activeFishes.size() >= levelConfig.shoalSumInScreen) {
            return;
        }

        int remainingSlots = levelConfig.shoalSumInScreen - activeFishes.size();
        int batchSize = Math.min(
                random.nextInt(MAX_BATCH_SIZE - MIN_BATCH_SIZE + 1) + MIN_BATCH_SIZE,
                remainingSlots);
        
        // 随机选择从左边还是右边生成
        boolean fromLeft = random.nextBoolean();
        
        System.out.println("生成一批鱼，数量: " + batchSize + "，方向: " + (fromLeft ? "从左到右" : "从右到左"));
        
        for (int i = 0; i < batchSize; i++) {
            spawnSingleFish(fromLeft, i * 80); // 增加鱼之间的间隔到80像素
        }
    }
    
    /**
     * 生成单条鱼
     */
    private void spawnSingleFish(boolean fromLeft, int offset) {
        if (levelConfig == null || levelConfig.fishTypes.isEmpty()) {
            return;
        }

        // 计算总概率
        int totalProbability = levelConfig.showProbabilities.stream().mapToInt(Integer::intValue).sum();
        
        // 根据概率选择鱼的类型
        int randomValue = random.nextInt(totalProbability);
        int currentSum = 0;
        int selectedFishIndex = 0;
        
        for (int j = 0; j < levelConfig.showProbabilities.size(); j++) {
            currentSum += levelConfig.showProbabilities.get(j);
            if (randomValue < currentSum) {
                selectedFishIndex = j;
                break;
            }
        }
        
        // 确保索引在有效范围内
        if (selectedFishIndex >= levelConfig.fishTypes.size()) {
            selectedFishIndex = levelConfig.fishTypes.size() - 1;
        }

        String selectedFishType = levelConfig.fishTypes.get(selectedFishIndex);

        // 设置位置 - 适配800x500屏幕尺寸
        int x, y;
        if (fromLeft) {
            x = -120 - offset; // 从左边生成，位置稍微调整
        } else {
            x = 850 + offset; // 从右边生成，适配800像素宽度
        }

        // Y坐标设置 - 鱼在屏幕中间偏上区域生成和游动
        y = random.nextInt(200) + 75; // 75-275范围，确保在中间偏上区域
        
        // 根据鱼的类型设置大小
        int width, height;
        int fishNumber = Integer.parseInt(selectedFishType.replaceAll("\\D", ""));
        if (fishNumber <= 4) { // fish01-fish04 小鱼
            width = 40;
            height = 20;
        } else if (fishNumber <= 8) { // fish05-fish08 中等鱼
            width = 60;
            height = 40;
        } else { // fish09-fish16 大鱼
            width = 70;
            height = 55;
        }
        
        // 创建鱼
        Fish fish = new Fish(
            x, y, width, height, 
                fishNumber, // 级别
                selectedFishType
        );
        
        // 设置游动方向
        fish.setMovingDirection(fromLeft);
        
        activeFishes.add(fish);
        elementManager.addElement(fish, GameElementType.FISH);
        
        System.out.println("生成了一条鱼: " + selectedFishType + " 在位置 (" + x + ", " + y + ")");
    }
    
    /**
     * 清理非活动的鱼
     */
    private void cleanupInactiveFishes() {
        List<Fish> toRemove = new ArrayList<>();
        List<top.xiaoxuan010.learn.game.element.components.GameElement> fishElements = 
            elementManager.getElementsByType(GameElementType.FISH);
        
        for (int i = activeFishes.size() - 1; i >= 0; i--) {
            Fish fish = activeFishes.get(i);
            if (!fish.isActive()) {
                toRemove.add(fish);
                activeFishes.remove(i);
                fishElements.remove(fish);
                System.out.println("清理了一条鱼: " + fish.getFishType());
            }
        }
    }
    
    /**
     * 获取当前活动的鱼的数量
     */
    public int getActiveFishCount() {
        return activeFishes.size();
    }
    
    /**
     * 重置管理器
     */
    public void reset() {
        activeFishes.clear();
        elementManager.getElementsByType(GameElementType.FISH).clear();
        lastBatchSpawnTime = 0;
    }
    
    /**
     * 立即生成初始鱼群
     */
    public void spawnInitialFishes() {
        if (levelConfig != null) {
            // 根据关卡配置生成初始鱼群，数量为配置的一半
            int initialCount = Math.max(1, levelConfig.shoalSumInScreen / 2);
            for (int i = 0; i < initialCount; i++) {
                spawnSingleFish(random.nextBoolean(), i * 100);
            }
        } else {
            // 立即生成一批鱼
            spawnBatchOfFish();
        }
    }
}
