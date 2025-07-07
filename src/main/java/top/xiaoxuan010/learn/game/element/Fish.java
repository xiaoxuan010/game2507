package top.xiaoxuan010.learn.game.element;

import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.manager.GameLoader;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
        this.speedX = (float) (Math.random() * 0.8 + 0.5); // 0.5-1.3像素/帧的速度，减慢速度
        this.speedY = (float) (Math.random() * 0.2 - 0.1); // 更轻微的垂直游动
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
        if (!active || isCaught) return; // 如果被捕获，停止移动
        
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
        
        // 边界检查 - 给更大的边界范围，确保鱼能完全游过屏幕
        if (movingRight && currentX > 850) {
            // 从左往右游，超出右边界就停止
            active = false;
            return;
        } else if (!movingRight && currentX < -150) {
            // 从右往左游，超出左边界就停止
            active = false;
            return;
        }
        
        // 垂直边界检查，防止鱼游出可见区域
        if (currentY < 160 || currentY > 330) {
            // 对于某些移动模式，需要特殊处理
            if (movementType.equals("wave") || movementType.equals("slow_sway")) {
                // 波浪模式时反转垂直方向
                speedY = -speedY;
            }
            // 调整位置确保在边界内
            if (currentY < 160) currentY = 160;
            if (currentY > 330) currentY = 330;
        }
        
        // 更新实际位置
        setX((int) currentX);
        setY((int) currentY);
    }

    @Override
    public void draw(Graphics g) {
        if (!active) return;
        
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
     * @return 金币数量
     */
    public int getCoinReward() {
        // 根据鱼的等级返回不同的金币奖励
        if (level <= 4) {
            return level * 2; // 1-4级鱼：2, 4, 6, 8金币
        } else if (level <= 8) {
            return level * 3; // 5-8级鱼：15, 18, 21, 24金币
        } else {
            return level * 5; // 9-16级鱼：45, 50, 55, 60, 65, 70, 75, 80金币
        }
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
            // 鱼被渔网捕获
            setCaught();
            
            // 增加金币
            int coinReward = getCoinReward();
            top.xiaoxuan010.learn.game.element.utils.GameStateManager.getInstance().addCoins(coinReward);
            
            System.out.println("鱼 " + fishType + " (等级" + level + ") 被捕获！获得 " + coinReward + " 金币");
        }
    }
    
    /**
     * 根据鱼的类型初始化移动模式
     * 17种鱼分为6组，每组有不同的移动模式
     */
    private void initializeMovementType() {
        if (fishType.equals("fish01") || fishType.equals("fish02") || fishType.equals("fish03")) {
            // 第1组：直线游动（默认模式）
            movementType = "straight";
        } else if (fishType.equals("fish04") || fishType.equals("fish05") || fishType.equals("fish06")) {
            // 第2组：波浪游动
            movementType = "wave";
            waveAmplitude = 15 + (float)(Math.random() * 20); // 15-35像素振幅，减小振幅
            waveFrequency = 0.015f + (float)(Math.random() * 0.02f); // 降低频率
        } else if (fishType.equals("fish07") || fishType.equals("fish08") || fishType.equals("fish09")) {
            // 第3组：之字形游动
            movementType = "zigzag";
            zigzagCounter = 0;
            zigzagDirection = 1;
        } else if (fishType.equals("fish10") || fishType.equals("fish11") || fishType.equals("fish12")) {
            // 第4组：圆形游动
            movementType = "circle";
            circleRadius = 20 + (float)(Math.random() * 25); // 20-45像素半径，减小半径
            circleAngle = 0;
        } else if (fishType.equals("fish13") || fishType.equals("fish14")) {
            // 第5组：急速冲刺
            movementType = "dash";
            speedX *= 1.8f; // 冲刺速度降低到1.8倍
        } else {
            // 第6组：fish15, fish16 - 缓慢摆动
            movementType = "slow_sway";
            speedX *= 0.4f; // 进一步减慢速度
            waveAmplitude = 10; // 减小摆动幅度
            waveFrequency = 0.008f; // 更低频率
        }
    }
    
    /**
     * 直线游动 - 基本的直线移动模式
     */
    private void updateStraightMovement() {
        currentX += speedX;
        currentY += speedY;
        
        // 轻微的垂直波动
        if (Math.random() < 0.005) {
            speedY = (float) (Math.random() * 0.4 - 0.2);
        }
    }
    
    /**
     * 波浪游动 - 呈正弦波形游动
     */
    private void updateWaveMovement() {
        long currentTime = System.currentTimeMillis();
        float timeDiff = (currentTime - movementStartTime) * 0.001f;
        
        currentX += speedX;
        currentY += originalSpeedY + waveAmplitude * (float)Math.sin(timeDiff * waveFrequency * 2 * Math.PI);
    }
    
    /**
     * 之字形游动 - 呈锯齿状游动
     */
    private void updateZigzagMovement() {
        currentX += speedX;
        
        zigzagCounter += speedX;
        if (zigzagCounter > 50) { // 每50像素改变一次方向
            zigzagDirection *= -1;
            zigzagCounter = 0;
        }
        
        currentY += zigzagDirection * 2;
    }
    
    /**
     * 圆形游动 - 呈小圆圈游动
     */
    private void updateCircleMovement() {
        float centerX = currentX + speedX;
        
        circleAngle += 0.1f;
        currentX = centerX + circleRadius * (float)Math.cos(circleAngle);
        currentY += speedY + circleRadius * (float)Math.sin(circleAngle) * 0.5f;
    }
    
    /**
     * 急速冲刺 - 快速直线游动
     */
    private void updateDashMovement() {
        currentX += speedX;
        currentY += speedY;
        
        // 偶尔加速
        if (Math.random() < 0.01) {
            speedX *= 1.1f; // 减少加速幅度
        }
    }
    
    /**
     * 缓慢摆动 - 慢速波浪游动
     */
    private void updateSlowSwayMovement() {
        long currentTime = System.currentTimeMillis();
        float timeDiff = (currentTime - movementStartTime) * 0.001f;
        
        currentX += speedX;
        currentY += originalSpeedY + waveAmplitude * (float)Math.sin(timeDiff * waveFrequency * 2 * Math.PI);
    }
}