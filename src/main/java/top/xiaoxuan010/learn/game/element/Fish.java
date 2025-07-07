package top.xiaoxuan010.learn.game.element;

import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.manager.GameLoader;

import javax.swing.*;
import java.awt.Graphics;

public class Fish extends GameElement {
    private int level;
    private String fishType;
    private int animationFrame;
    private int maxAnimationFrames;
    private long lastFrameTime;
    private static final int ANIMATION_SPEED = 200; // 毫秒
    
    public Fish(int x, int y, int width, int height, int level, String fishType) {
        super(x, y, width, height, null);
        this.level = level;
        this.fishType = fishType;
        this.animationFrame = 0;
        this.maxAnimationFrames = 10; // 大多数鱼有10帧动画
        this.lastFrameTime = System.currentTimeMillis();
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

    @Override
    public void draw(Graphics g) {
        updateAnimation();
        if (getIcon() != null) {
            g.drawImage(getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
        }
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
}