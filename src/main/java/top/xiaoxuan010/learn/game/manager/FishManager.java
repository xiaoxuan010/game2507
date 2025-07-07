package top.xiaoxuan010.learn.game.manager;

import top.xiaoxuan010.learn.game.element.Fish;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 鱼类管理器
 * 负责根据gamelevel-config.plist第一部分配置生成和管理鱼类
 */
public class FishManager {
    private static FishManager instance;
    private List<Fish> activeFishes;
    private long gameStartTime;
    private long lastSpawnTime;
    private long lastBatchSpawnTime;
    private Random random;
    private ElementManager elementManager;
    
    // Part 1 配置信息 (从gamelevel-config.plist读取)
    private static final String[] FISH_TYPES = {
        "fish01", "fish02", "fish03", "fish04", "fish05", "fish06", "fish07", "fish08",
        "fish09", "fish10", "fish11", "fish12", "fish13", "fish14", "fish15", "fish16"
    };
    
    private static final int[] SHOW_PROBABILITIES = {
        10, 10, 10, 10, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5
    };
    
    private static final long BATCH_SPAWN_INTERVAL = 8000; // 批次生成间隔增加到8秒
    private static final int MIN_BATCH_SIZE = 1; // 最小批次大小减少到1
    private static final int MAX_BATCH_SIZE = 3; // 最大批次大小减少到3
    
    private FishManager() {
        this.activeFishes = new ArrayList<>();
        this.random = new Random();
        this.elementManager = ElementManager.getInstance();
        this.gameStartTime = System.currentTimeMillis();
        this.lastSpawnTime = 0;
        this.lastBatchSpawnTime = 0;
    }
    
    public static synchronized FishManager getInstance() {
        if (instance == null) {
            instance = new FishManager();
        }
        return instance;
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
        int batchSize = random.nextInt(MAX_BATCH_SIZE - MIN_BATCH_SIZE + 1) + MIN_BATCH_SIZE;
        
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
        // 计算总概率
        int totalProbability = 0;
        for (int prob : SHOW_PROBABILITIES) {
            totalProbability += prob;
        }
        
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
        
        // 设置位置 - 确保鱼能够游过整个屏幕
        int x, y;
        if (fromLeft) {
            x = -150 - offset; // 从左边更远的地方开始，加上偏移
        } else {
            x = 950 + offset; // 从右边更远的地方开始，加上偏移
        }
        
        
        // Y坐标设置 - 确保鱼出现在屏幕中上部分，避免与底部UI重叠
        // 假设屏幕高度600像素，底部UI占用约100像素，所以有效游戏区域是0-500
        // 让鱼出现在上方60%的区域，现在范围向上移动80像素
        y = random.nextInt(170) + 160; // 160-330范围，向上移动80像素
        
        // 根据鱼的类型设置大小
        int width, height;
        if (selectedFishIndex < 4) { // fish01-fish04 小鱼
            width = 40;
            height = 20;
        } else if (selectedFishIndex < 8) { // fish05-fish08 中等鱼
            width = 60;
            height = 40;
        } else { // fish09-fish16 大鱼
            width = 70;
            height = 55;
        }
        
        // 创建鱼
        Fish fish = new Fish(
            x, y, width, height, 
            selectedFishIndex + 1, // 级别从1开始
            FISH_TYPES[selectedFishIndex]
        );
        
        // 设置游动方向
        fish.setMovingDirection(fromLeft);
        
        activeFishes.add(fish);
        elementManager.addElement(fish, GameElementType.FISH);
        
        System.out.println("生成了一条鱼: " + FISH_TYPES[selectedFishIndex] + " 在位置 (" + x + ", " + y + ")");
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
        gameStartTime = System.currentTimeMillis();
        lastSpawnTime = 0;
        lastBatchSpawnTime = 0;
    }
    
    /**
     * 立即生成初始鱼群
     */
    public void spawnInitialFishes() {
        // 立即生成一批鱼
        spawnBatchOfFish();
    }
}
