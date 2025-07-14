package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.manager.GameLoader;
import top.xiaoxuan010.learn.game.manager.utils.GameStateDataManager;

public class Fish extends GameElement {
    private int level;
    private String fishType;
    private int animationFrame;
    private int maxAnimationFrames;
    private long lastFrameTime;
    private static final int ANIMATION_SPEED = 200; // 毫秒

    // 游动相关属性
    private float speedX;
    private float speedY;
    private float currentX;
    private float currentY;
    private boolean movingRight;
    private long spawnTime;
    private boolean active;

    // 移动模式相关属性
    private String movementType;
    @SuppressWarnings("unused")
    private float originalSpeedX;
    private float originalSpeedY;
    private long movementStartTime;
    private float waveAmplitude;
    private float waveFrequency;
    private float circleRadius;
    private float circleAngle;
    private float zigzagCounter;
    private float zigzagDirection;

    // 捕获相关属性
    private boolean isCaught;
    private int catchAnimationFrame;
    private int maxCatchAnimationFrames;
    private long catchStartTime;
    private static final int CATCH_ANIMATION_SPEED = 300; // 捕获动画速度（毫秒）
    private static final long CATCH_ANIMATION_DURATION = 1000; // 捕获动画持续时间（毫秒）

    public Fish(int x, int y, int width, int height, int level, String fishType) {
        super(x, y, width, height, null);
        this.level = level;
        this.fishType = fishType;
        this.animationFrame = 0;
        this.maxAnimationFrames = 10; // 大多数鱼有10帧动画
        this.lastFrameTime = System.currentTimeMillis();

        // 初始化游动相关属性
        this.currentX = x;
        this.currentY = y;
        this.speedX = (float) (Math.random() * 1.0 + 0.8); // 0.8-1.8像素/帧的速度，适中速度
        this.speedY = (float) (Math.random() * 0.3 - 0.15); // 轻微的垂直游动
        this.movingRight = true; // 默认向右，稍后会根据生成方向调整
        this.spawnTime = System.currentTimeMillis();
        this.active = true;

        // 初始化移动模式
        initializeMovementType();
        this.originalSpeedX = this.speedX;
        this.originalSpeedY = this.speedY;
        this.movementStartTime = System.currentTimeMillis();

        updateIcon();
    }

    public Fish(int x, int y, int width, int height, int level) {
        this(x, y, width, height, level, "fish" + String.format("%02d", level));
    }

    private void updateIcon() {
        String iconKey;
        if (isCaught) {
            // 捕获动画
            iconKey = "fish.lv" + level + "_catch[" + catchAnimationFrame + "]";
        } else {
            // 正常游动动画
            iconKey = "fish.lv" + level + "[" + animationFrame + "]";
        }

        ImageIcon icon = GameLoader.imgMap.get(iconKey);
        if (icon != null) {
            this.setIcon(icon);
        } else {
            // 如果找不到动画帧，尝试使用静态图片
            if (isCaught) {
                iconKey = "fish.lv" + level + "_catch[0]";
            } else {
                iconKey = "fish.lv" + level + "[0]";
            }
            icon = GameLoader.imgMap.get(iconKey);
            if (icon != null) {
                this.setIcon(icon);
            }
        }
    }

    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();

        if (isCaught) {
            // 捕获动画处理
            updateCatchAnimation(currentTime);
        } else {
            // 正常游动动画
            if (currentTime - lastFrameTime > ANIMATION_SPEED) {
                animationFrame = (animationFrame + 1) % maxAnimationFrames;
                updateIcon();
                lastFrameTime = currentTime;
            }
        }
    }

    private void updateCatchAnimation(long currentTime) {
        if (currentTime - catchStartTime > CATCH_ANIMATION_DURATION) {
            // 捕获动画播放完毕，标记为非活动状态
            active = false;
            return;
        }

        if (currentTime - lastFrameTime > CATCH_ANIMATION_SPEED) {
            catchAnimationFrame = (catchAnimationFrame + 1) % maxCatchAnimationFrames;
            updateIcon();
            lastFrameTime = currentTime;
        }
    }

    public void updateMovement() {
        if (!active || isCaught)
            return; // 如果被捕获，停止移动

        // 根据移动类型执行不同的移动逻辑
        switch (movementType) {
            case "straight":
                updateStraightMovement();
                break;
            case "wave":
                updateWaveMovement();
                break;
            case "zigzag":
                updateZigzagMovement();
                break;
            case "circle":
                updateCircleMovement();
                break;
            case "dash":
                updateDashMovement();
                break;
            case "slow_sway":
                updateSlowSwayMovement();
                break;
            default:
                updateStraightMovement();
                break;
        }

        // 边界检查 - 适配800x500屏幕的边界处理
        if (movingRight && currentX > 850) {
            // 从左往右游，超出右边界
            active = false;
            return;
        } else if (!movingRight && currentX < -100) {
            // 从右往左游，超出左边界
            active = false;
            return;
        }

        // 垂直边界检查 - 限制鱼在屏幕中间偏上区域游动
        // 屏幕高度500，设置鱼的活动范围在中间偏上区域（Y: 50-300）
        if (currentY < 50) {
            currentY = 50;
            // 柔和地调整垂直速度
            if (speedY < 0) {
                speedY = Math.abs(speedY) * 0.5f + 0.1f; // 轻微向下
            }
        }
        if (currentY > 300) {
            currentY = 300;
            // 柔和地调整垂直速度
            if (speedY > 0) {
                speedY = -Math.abs(speedY) * 0.5f - 0.1f; // 轻微向上
            }
        }

        // 更新实际位置
        setX((int) currentX);
        setY((int) currentY);
    }

    @Override
    public void draw(Graphics g) {
        if (!active)
            return;

        updateAnimation();
        updateMovement();

        if (getIcon() != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            boolean shouldFlip = shouldFlipImage();

            if (shouldFlip) {
                // 翻转图片
                g2d.translate(getX() + getWidth(), getY());
                g2d.scale(-1, 1);
                g2d.drawImage(getIcon().getImage(), 0, 0, getWidth(), getHeight(), null);
            } else {
                // 正常绘制
                g2d.drawImage(getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
            }

            g2d.dispose();
        }
    }

    // 如果需要切换翻转逻辑，可以修改这个方法
    private boolean shouldFlipImage() {
        // 原始图片朝左，向右游动时翻转
        return movingRight;

    }

    public int getLevel() {
        return level;
    }

    /**
     * 获取捕获此鱼的金币奖励
     * 现在从FishInfo.plist配置文件中读取鱼的worth值
     * 
     * @return 金币数量
     */
    public int getCoinReward() {
        // 从配置中获取鱼的基础分值（worth）
        int baseWorth = getFishWorthFromConfig();
        
        // 根据渔网等级添加少量奖励
        top.xiaoxuan010.learn.game.manager.PlayerEquipmentManager equipmentManager = 
            top.xiaoxuan010.learn.game.manager.PlayerEquipmentManager.getInstance();
        int netLevelBonus = Math.max(1, equipmentManager.getCurrentNetLevel() / 3); // 渔网等级奖励
        
        return baseWorth + netLevelBonus;
    }
    
    /**
     * 从FishInfo.plist配置文件中获取鱼的分值
     * 
     * @return 鱼的基础分值
     */
    private int getFishWorthFromConfig() {
        // 根据鱼类型返回对应的分值（参考FishInfo.plist中的worth值）
        switch (fishType) {
            case "fish01": return 5;   // 小光鱼
            case "fish02": return 8;   // 小黄鱼  
            case "fish03": return 12;  // 扁鱼
            case "fish04": return 18;  // 神仙鱼
            case "fish05": return 25;  // 小丑鱼
            case "fish06": return 30;  // 绿色河豚
            case "fish07": return 40;  // 小鳊鱼
            case "fish08": return 60;  // 黑鲈鱼
            case "fish09": return 80;  // 红杉鱼
            case "fish10": return 120; // 海龟
            case "fish11": return 180; // 灯笼鱼1
            case "fish12": return 200; // 灯笼鱼2
            case "fish13": return 250; // 海马
            case "fish14": return 300; // 蝠鲼
            case "fish15": return 400; // 鲨鱼
            case "fish16": return 500; // 座头鲸
            default: 
                return level * 5; // 默认分值
        }
    }
    
    /**
     * 创建金币动画显示
     * 
     * @param coinValue 金币数量
     */
    private void createCoinAnimation(int coinValue) {
        // 在鱼的位置创建金币动画
        float animationX = getX() + getWidth() / 2.0f;
        float animationY = getY() + getHeight() / 2.0f;
        
        CoinAnimation coinAnimation = new CoinAnimation(animationX, animationY, coinValue);
        
        // 添加到游戏元素管理器中
        top.xiaoxuan010.learn.game.manager.ElementManager elementManager = 
            top.xiaoxuan010.learn.game.manager.ElementManager.getInstance();
        elementManager.addElement(coinAnimation, 
            top.xiaoxuan010.learn.game.manager.GameElementType.UI);
        
        System.out.println("创建金币动画 - 位置: (" + animationX + ", " + animationY + "), 金币: " + coinValue);
    }

    public String getFishType() {
        return fishType;
    }

    public void setLevel(int level) {
        this.level = level;
        updateIcon();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMovingDirection(boolean fromLeft) {
        this.movingRight = fromLeft; // 如果从左边生成，那么向右游动
        if (fromLeft) {
            // 从左向右游动，速度为正
            this.speedX = Math.abs(this.speedX);
            System.out.println("鱼设置为向右游动: " + this.fishType);
        } else {
            // 从右向左游动，速度为负
            this.speedX = -Math.abs(this.speedX);
            System.out.println("鱼设置为向左游动: " + this.fishType);
        }
    }
    
    /**
     * 设置自定义速度（用于FishManager调用）
     */
    public void setCustomSpeed(float speedX, float speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.originalSpeedX = speedX;
        this.originalSpeedY = speedY;
        
        // 根据速度方向更新movingRight标志
        this.movingRight = speedX > 0;
        
        // 重新设置移动时间，确保动画同步
        this.movementStartTime = System.currentTimeMillis();
        
        System.out.println("鱼 " + fishType + " 设置自定义速度: X=" + speedX + ", Y=" + speedY + " 朝向: " + (movingRight ? "右" : "左"));
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    // 捕获相关方法
    public void setCaught() {
        if (!isCaught) {
            this.isCaught = true;
            this.catchAnimationFrame = 0;
            this.maxCatchAnimationFrames = 5; // 捕获动画通常5帧
            this.catchStartTime = System.currentTimeMillis();
            this.lastFrameTime = this.catchStartTime;
            System.out.println("鱼 " + fishType + " 被捕获！开始播放捕获动画");
        }
    }

    public boolean isCaught() {
        return isCaught;
    }

    @Override
    public void onCollision(GameElement other) {
        if (other instanceof FishNet && !isCaught) {
            FishNet fishNet = (FishNet) other;
            
            // 检查渔网是否能捕获这条鱼
            if (!fishNet.canCatchFish(level)) {
                System.out.println("渔网等级不足！鱼等级: " + level + ", 渔网威力: " + fishNet.getNetPower() + " - 捕获失败");
                return; // 渔网等级不足，无法捕获
            }
            
            // 计算捕获成功率
            int successRate = fishNet.getCatchSuccessRate(level);
            boolean catchSuccess = Math.random() * 100 < successRate;
            
            if (!catchSuccess) {
                System.out.println("捕获失败！鱼等级: " + level + ", 成功率: " + successRate + "% - 鱼逃脱了");
                return; // 捕获失败，鱼逃脱
            }
              // 成功捕获
            setCaught();

            // 根据渔网等级和鱼的等级计算金币奖励
            int totalReward = getCoinReward();
            
            // 添加金币到游戏状态
            GameStateDataManager.getInstance().addCoins(totalReward);
            
            // 创建金币动画显示
            createCoinAnimation(totalReward);
            
            System.out.println("成功捕获！鱼: " + fishType + " (等级" + level + "), 渔网等级: " + fishNet.getNetLevel() + 
                             ", 成功率: " + successRate + "%, 获得金币: " + totalReward);
        }
    }

    /**
     * 根据鱼的类型初始化移动模式 - 捕鱼达人风格
     * 16种鱼分为6组，每组有不同的游动特色
     */
    private void initializeMovementType() {
        if (fishType.equals("fish01") || fishType.equals("fish02") || fishType.equals("fish03")) {
            // 第1组：小光鱼、小黄鱼 - 成群结队，快速灵活，轻快摆尾
            movementType = "straight";
            speedX *= 1.1f; // 小鱼游得稍快
        } else if (fishType.equals("fish04") || fishType.equals("fish05") || fishType.equals("fish06")) {
            // 第2组：神仙鱼、河豚鱼、小丑鱼 - 优雅波浪游动，悠闲自得
            movementType = "wave";
            waveAmplitude = 20 + (float) (Math.random() * 15); // 20-35像素优雅摆动
            waveFrequency = 0.03f + (float) (Math.random() * 0.02f); // 悠闲频率
            speedX *= 0.95f; // 稍慢的优雅游动
        } else if (fishType.equals("fish07") || fishType.equals("fish08") || fishType.equals("fish09")) {
            // 第3组：小鳊鱼、红杉鱼 - 敏捷Z字形游动，灵活难捕
            movementType = "zigzag";
            zigzagCounter = 0;
            zigzagDirection = 1;
            speedX *= 1.15f; // 敏捷快速
        } else if (fishType.equals("fish10") || fishType.equals("fish11") || fishType.equals("fish12")) {
            // 第4组：海龟、灯笼鱼 - 稳重的弧形游动，体型较大
            movementType = "circle";
            circleRadius = 25 + (float) (Math.random() * 20); // 25-45像素弧度
            circleAngle = 0;
            speedX *= 0.85f; // 稳重缓慢
        } else if (fishType.equals("fish13") || fishType.equals("fish14")) {
            // 第5组：魔鬼鱼 - 滑翔式冲刺，偶尔加速
            movementType = "dash";
            speedX *= 1.3f; // 冲刺速度
        } else {
            // 第6组：鲨鱼 - 威猛的缓慢摆动，霸主级别
            movementType = "slow_sway";
            speedX *= 1.2f; // 威猛但不过快
            waveAmplitude = 25; // 威猛的摆动幅度
            waveFrequency = 0.025f; // 威猛的频率
        }
    }

    /**
     * 直线游动 - 小鱼的灵活游动，带有轻微摆尾效果
     */
    private void updateStraightMovement() {
        currentX += speedX;
        
        // 捕鱼达人风格的轻微摆尾效果
        long currentTime = System.currentTimeMillis();
        float timeDiff = (currentTime - movementStartTime) * 0.001f;
        float tailSway = 3f * (float) Math.sin(timeDiff * 4.0f); // 轻微的摆尾
        
        currentY += speedY + tailSway * 0.3f;
        
        // 偶尔的小幅垂直调整，模拟鱼的自然游动
        if (Math.random() < 0.008) {
            speedY += (float) (Math.random() * 0.3 - 0.15);
            speedY = Math.max(-0.4f, Math.min(0.4f, speedY)); // 限制垂直速度
        }
    }

    /**
     * 波浪游动 - 神仙鱼等的优雅波浪游动
     */
    private void updateWaveMovement() {
        long currentTime = System.currentTimeMillis();
        float timeDiff = (currentTime - movementStartTime) * 0.001f;

        currentX += speedX;
        
        // 优雅的波浪游动，加上基础垂直移动
        float waveY = waveAmplitude * (float) Math.sin(timeDiff * waveFrequency * 2 * Math.PI);
        currentY += originalSpeedY + waveY * 0.02f; // 降低波浪影响，使其更平滑
        
        // 河豚鱼的特殊效果：周期性的速度变化（鼓肚子效果）
        if (fishType.equals("fish05")) {
            float puffEffect = 0.9f + 0.2f * (float) Math.sin(timeDiff * 0.8f);
            currentX += speedX * (puffEffect - 1) * 0.1f; // 轻微的节奏变化
        }
    }

    /**
     * 之字形游动 - 敏捷鱼类的灵活游动
     */
    private void updateZigzagMovement() {
        currentX += speedX;

        // 更自然的Z字形游动
        zigzagCounter += Math.abs(speedX);
        if (zigzagCounter > 40 + Math.random() * 20) { // 随机化转向距离：40-60像素
            zigzagDirection *= -1;
            zigzagCounter = 0;
            
            // 转向时稍微减速，然后恢复，模拟真实鱼类行为
            speedX *= 0.9f;
        }

        // 平滑的Z字游动，不是生硬的直角
        float zigzagSpeed = zigzagDirection * 1.5f;
        currentY += speedY + zigzagSpeed;
        
        // 逐渐恢复水平速度
        if (Math.abs(speedX) < Math.abs(originalSpeedX) * 0.9f) {
            speedX += (originalSpeedX > 0 ? 0.02f : -0.02f);
        }
    }

    /**
     * 弧形游动 - 海龟等大型鱼类的稳重游动
     */
    private void updateCircleMovement() {
        currentX += speedX;
        
        // 稳重的弧形游动，不是完整的圆圈
        circleAngle += 0.03f + (float)(Math.random() * 0.02f); // 0.03-0.05弧度/帧
        
        // 创造大弧度的游动路径，而不是小圆圈
        float arcY = circleRadius * (float) Math.sin(circleAngle) * 0.3f;
        currentY += speedY + arcY * 0.02f; // 很轻微的弧形影响
        
        // 海龟的特殊效果：偶尔的"呼吸"暂停
        if (fishType.equals("fish10") && Math.random() < 0.003f) {
            speedX *= 0.8f; // 短暂减速
            if (Math.abs(speedX) < Math.abs(originalSpeedX) * 0.7f) {
                speedX = originalSpeedX * (speedX > 0 ? 1 : -1); // 恢复速度
            }
        }
    }

    /**
     * 冲刺游动 - 魔鬼鱼的滑翔冲刺
     */
    private void updateDashMovement() {
        currentX += speedX;
        currentY += speedY;

        // 滑翔效果：偶尔的加速冲刺，然后滑行
        if (Math.random() < 0.006f) {
            speedX *= 1.4f; // 冲刺加速
            speedY *= 1.2f; // 垂直方向也有推进力
        }
        
        // 滑翔阻力：逐渐减速到正常速度
        if (Math.abs(speedX) > Math.abs(originalSpeedX) * 1.1f) {
            speedX *= 0.98f; // 逐渐减速
        }
        if (Math.abs(speedY) > 0.5f) {
            speedY *= 0.95f; // 垂直速度衰减
        }
        
        // 魔鬼鱼的翅膀扇动效果
        long currentTime = System.currentTimeMillis();
        float timeDiff = (currentTime - movementStartTime) * 0.001f;
        float wingFlap = 2f * (float) Math.sin(timeDiff * 3.0f); // 翅膀扇动
        currentY += wingFlap * 0.1f;
    }

    /**
     * 威猛摆动 - 鲨鱼的霸主级游动
     */
    private void updateSlowSwayMovement() {
        long currentTime = System.currentTimeMillis();
        float timeDiff = (currentTime - movementStartTime) * 0.001f;

        currentX += speedX;
        
        // 鲨鱼的威猛摆动：强有力但不过于频繁
        float sharkSway = waveAmplitude * (float) Math.sin(timeDiff * waveFrequency * 2 * Math.PI);
        currentY += originalSpeedY + sharkSway * 0.025f; // 威猛但平滑的摆动
        
        // 掠食者的突然转向：偶尔的方向调整
        if (Math.random() < 0.004f) {
            speedY += (Math.random() - 0.5f) * 0.6f; // 更大的转向幅度
            speedY = Math.max(-0.8f, Math.min(0.8f, speedY)); // 限制范围
        }
        
        // 鲨鱼的威猛感：速度有轻微波动
        if (Math.random() < 0.002f) {
            speedX *= (0.95f + (float)(Math.random() * 0.1f)); // 0.95-1.05倍速度变化
        }
    }
}