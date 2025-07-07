package top.xiaoxuan010.learn.game.element;

import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.manager.GameLoader;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

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
        String iconKey = "fish.lv" + level + "[" + animationFrame + "]";
        ImageIcon icon = GameLoader.imgMap.get(iconKey);
        if (icon != null) {
            this.setIcon(icon);
        } else {
            // 如果找不到动画帧，尝试使用静态图片
            iconKey = "fish.lv" + level + "[0]";
            icon = GameLoader.imgMap.get(iconKey);
            if (icon != null) {
                this.setIcon(icon);
            }
        }
    }
    
    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime > ANIMATION_SPEED) {
            animationFrame = (animationFrame + 1) % maxAnimationFrames;
            updateIcon();
            lastFrameTime = currentTime;
        }
    }
    
    public void updateMovement() {
        if (!active) return;
        
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
        if (currentY < 50 || currentY > 550) {
            // 反转垂直方向
            speedY = -speedY;
            // 调整位置确保在边界内
            if (currentY < 50) currentY = 50;
            if (currentY > 550) currentY = 550;
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
}