package top.xiaoxuan010.learn.game.manager;

/**
 * 玩家装备管理器
 * 管理玩家的渔网等级、升级等功能
 */
public class PlayerEquipmentManager {
    private static PlayerEquipmentManager instance;
    
    private int currentNetLevel = 1; // 当前渔网等级，默认1级
    private int maxNetLevel = 8; // 最大渔网等级
    
    // 渔网升级所需金币
    private static final int[] UPGRADE_COSTS = {
        0,    // 1级网（默认）
        100,  // 升级到2级
        250,  // 升级到3级
        500,  // 升级到4级
        1000, // 升级到5级
        2000, // 升级到6级
        4000, // 升级到7级
        8000  // 升级到8级
    };
    
    private PlayerEquipmentManager() {
        // 私有构造函数
    }
    
    public static synchronized PlayerEquipmentManager getInstance() {
        if (instance == null) {
            instance = new PlayerEquipmentManager();
        }
        return instance;
    }
    
    /**
     * 获取当前渔网等级
     */
    public int getCurrentNetLevel() {
        return currentNetLevel;
    }
    
    /**
     * 设置当前渔网等级（用于与大炮等级同步）
     */
    public void setCurrentNetLevel(int level) {
        if (level >= 1 && level <= maxNetLevel) {
            this.currentNetLevel = level;
            System.out.println("渔网等级设置为: " + currentNetLevel);
        } else {
            System.out.println("无效的渔网等级: " + level + "，必须在1-" + maxNetLevel + "之间");
        }
    }
    
    /**
     * 获取最大渔网等级
     */
    public int getMaxNetLevel() {
        return maxNetLevel;
    }
    
    /**
     * 获取升级到下一级所需的金币
     */
    public int getUpgradeCost() {
        if (currentNetLevel >= maxNetLevel) {
            return -1; // 已达到最大等级
        }
        return UPGRADE_COSTS[currentNetLevel];
    }
    
    /**
     * 检查是否可以升级渔网
     */
    public boolean canUpgradeNet() {
        if (currentNetLevel >= maxNetLevel) {
            return false; // 已达到最大等级
        }
        
        int currentCoins = top.xiaoxuan010.learn.game.manager.utils.GameStateDataManager.getInstance().getCoins();
        return currentCoins >= getUpgradeCost();
    }
      /**
     * 升级渔网
     */
    public boolean upgradeNet() {
        if (!canUpgradeNet()) {
            return false;
        }

        int upgradeCost = getUpgradeCost();

        // 扣除金币
        top.xiaoxuan010.learn.game.manager.utils.GameStateDataManager.getInstance().addCoins(-upgradeCost);

        // 升级
        currentNetLevel++;

        System.out.println("渔网升级成功！新等级: " + currentNetLevel + ", 花费金币: " + upgradeCost);
        return true;
    }

    /**
     * 降级渔网（通常用于调试或特殊情况）
     */
    public boolean downgradeNet() {
        if (currentNetLevel <= 1) {
            System.out.println("渔网已经是最低等级，无法降级");
            return false;
        }

        currentNetLevel--;
        System.out.println("渔网降级到等级: " + currentNetLevel);
        return true;
    }
    
    /**
     * 获取渔网威力描述
     */
    public String getNetPowerDescription() {
        int netPower = calculateNetPower(currentNetLevel);
        return "能捕获 1-" + netPower + " 级鱼类";
    }
    
    /**
     * 计算渔网威力（与FishNet中的逻辑保持一致）
     */
    private int calculateNetPower(int netLevel) {
        if (netLevel <= 0) return 1;
        if (netLevel >= 8) return 16;
        return Math.min(16, netLevel * 2);
    }
    
    /**
     * 重置装备（用于游戏重置）
     */
    public void reset() {
        currentNetLevel = 1;
    }
    
    /**
     * 获取渔网等级信息
     */
    public String getNetLevelInfo() {
        StringBuilder info = new StringBuilder();
        info.append("当前渔网等级: ").append(currentNetLevel).append("\n");
        info.append("威力: ").append(getNetPowerDescription()).append("\n");
        
        if (currentNetLevel < maxNetLevel) {
            info.append("升级费用: ").append(getUpgradeCost()).append(" 金币\n");
            info.append("升级后威力: ");
            int nextPower = calculateNetPower(currentNetLevel + 1);
            info.append("能捕获 1-").append(nextPower).append(" 级鱼类");
        } else {
            info.append("已达到最大等级！");
        }
        
        return info.toString();
    }
}
