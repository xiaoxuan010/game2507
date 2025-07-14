package top.xiaoxuan010.learn.game.element;

import top.xiaoxuan010.learn.game.element.components.RotatableElement;
import top.xiaoxuan010.learn.game.manager.GameLoader;

public class FishNet extends RotatableElement {
    private long createTime = System.currentTimeMillis();
    private int netLevel; // 渔网等级
    private int netPower; // 渔网威力（能捕获的最大鱼类等级）

    public FishNet(float centerX, float centerY, Integer netLevel, float direction) {
        this.netLevel = netLevel;
        this.netPower = calculateNetPower(netLevel);
        
        this.setDirection(direction);
        this.setCenterPosition(centerX, centerY);
        
        // 根据等级设置渔网大小
        setNetSizeByLevel(netLevel);
        
        this.setIcon(GameLoader.imgMap.get("fishnet.lv" + netLevel));
        
        System.out.println("创建渔网 - 等级: " + netLevel + ", 威力: " + netPower + ", 大小: " + getWidth() + "x" + getHeight() + 
                          ", 能捕获1-" + netPower + "级鱼");
    }
    
    /**
     * 根据渔网等级设置大小
     */
    private void setNetSizeByLevel(int level) {
        int baseSize = 80; // 基础大小
        int sizeIncrement = 15; // 每级增加的大小
        
        int netSize = baseSize + (level - 1) * sizeIncrement;
        
        // 限制最大和最小尺寸
        netSize = Math.max(80, Math.min(150, netSize));
        
        this.setWidth(netSize);
        this.setHeight(netSize);
    }
    
    /**
     * 计算渔网威力（能捕获的最大鱼类等级）
     */
    private int calculateNetPower(int netLevel) {
        // 渔网威力计算规则：
        // 1级网：能捕1-2级鱼
        // 2级网：能捕1-4级鱼
        // 3级网：能捕1-6级鱼
        // 4级网：能捕1-8级鱼
        // 5级网：能捕1-10级鱼
        // 6级网：能捕1-12级鱼
        // 7级网：能捕1-14级鱼
        // 8级网及以上：能捕所有鱼
        
        if (netLevel <= 0) return 1;
        if (netLevel >= 8) return 16; // 最高级网能捕获所有鱼
        
        return Math.min(16, netLevel * 2);
    }
    
    /**
     * 检查是否能捕获指定等级的鱼
     */
    public boolean canCatchFish(int fishLevel) {
        return fishLevel <= netPower;
    }
    
    /**
     * 获取捕获成功率（百分比）
     */
    public int getCatchSuccessRate(int fishLevel) {
        if (fishLevel > netPower) {
            return 0; // 完全无法捕获
        }
        
        if (fishLevel <= netPower / 2) {
            return 100; // 轻松捕获
        }
        
        // 线性递减的成功率
        // 当鱼等级接近网的威力上限时，成功率降低
        int successRate = 100 - ((fishLevel - netPower / 2) * 60 / (netPower / 2));
        return Math.max(30, successRate); // 最低30%成功率
    }
    
    public int getNetLevel() {
        return netLevel;
    }
    
    public int getNetPower() {
        return netPower;
    }

    @Override
    public void update(long time) {
        if (time - createTime > 500) {
            this.setAlive(false);
        }
    }
}
