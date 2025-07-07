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
        this.speedX = (float) (Math.random() * 1.5 + 1); // 1-2.5像素/帧的速度，方向稍后由setMovingDirection设置
        this.speedY = (float) (Math.random() * 0.3 - 0.15); // 轻微的垂直游动
        this.movingRight = true; // 默认向右，稍后会根据生成方向调整
        this.spawnTime = System.currentTimeMillis();
        this.active = true;
        
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
        
        // 更新位置
        currentX += speedX;
        currentY += speedY;
        
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
        if (currentY < 80 || currentY > 400) {
            // 反转垂直方向
            speedY = -speedY;
            // 调整位置确保在边界内
            if (currentY < 80) currentY = 80;
            if (currentY > 400) currentY = 400;
        }
        
        // 更新实际位置
        setX((int) currentX);
        setY((int) currentY);
        
        // 轻微的垂直波动 - 减少频率，使游动更自然
        if (Math.random() < 0.005) { // 0.5%的概率改变垂直方向
            speedY = (float) (Math.random() * 0.4 - 0.2);
        }
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
    
    public int getGoldValue() {
        // 根据鱼的等级返回金币价值
        switch (level) {
            case 1: return 2;   
            case 2: return 3;  
            case 3: return 5;   
            case 4: return 8;  
            case 5: return 12;  
            case 6: return 18;  
            case 7: return 25;   
            case 8: return 35;   
            case 9: return 50;  
            case 10: return 60; 
            case 11: return 70; 
            case 12: return 80; 
            case 13: return 100; 
            case 14: return 120; 
            case 15: return 150; 
            case 16: return 180; 
            case 17: return 210;
            default: return 1; // 默认值
        }
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
}