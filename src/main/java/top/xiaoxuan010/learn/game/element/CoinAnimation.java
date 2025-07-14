package top.xiaoxuan010.learn.game.element;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;

import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.element.utils.FrameAnimation;
import top.xiaoxuan010.learn.game.loader.FrameAnimationLoader;
import top.xiaoxuan010.learn.game.manager.GameLoader;
import top.xiaoxuan010.learn.game.utils.GraphicsUtils;

/**
 * 金币动画显示类
 * 在捕鱼成功时显示获得的金币数量，带有动画效果
 */
public class CoinAnimation extends GameElement {
    private int coinValue;
    private long startTime;
    private long duration = 2000; // 动画持续2秒
    private FrameAnimation goldAnimation;
    private float currentY;
    private float targetY;
    private float alpha = 1.0f;
    
    // 动画类型
    private AnimationType animationType;
    
    public enum AnimationType {
        GOLD_SMALL,    // 小金币 (1-10分)
        GOLD_MEDIUM,   // 中等金币 (11-50分)
        GOLD_LARGE,    // 大金币 (51-100分)
        HUNDRED_BONUS  // 百分奖励 (100+分)
    }
    
    public CoinAnimation(float x, float y, int coinValue) {
        super((int)x, (int)y, 0, 0, null);
        this.coinValue = coinValue;
        this.startTime = System.currentTimeMillis();
        this.currentY = y;
        this.targetY = y - 60; // 向上飘移60像素
        
        // 根据金币数量选择动画类型
        determineAnimationType();
        
        // 加载对应的动画
        loadAnimation();
        
        System.out.println("创建金币动画 - 位置: (" + x + ", " + y + "), 金币数: " + coinValue + ", 类型: " + animationType);
    }
    
    /**
     * 根据金币数量确定动画类型
     */
    private void determineAnimationType() {
        if (coinValue >= 150) {
            animationType = AnimationType.HUNDRED_BONUS;  // 150+分
            duration = 3000;
        } else if (coinValue >= 100) {
            animationType = AnimationType.HUNDRED_BONUS;  // 100+分
            duration = 3000;
        } else if (coinValue >= 40) {
            animationType = AnimationType.GOLD_LARGE;     // 40+分
            duration = 2500;
        } else if (coinValue >= 15) {
            animationType = AnimationType.GOLD_MEDIUM;    // 15+分
            duration = 2000;
        } else {
            animationType = AnimationType.GOLD_SMALL;     // 1-14分
            duration = 1500;
        }
    }
    
    /**
     * 加载对应的动画资源
     */
    private void loadAnimation() {
        try {
            String animationKey = getAnimationKey();
            
            // 根据确切分值加载对应动画
            if (animationKey != null) {
                goldAnimation = FrameAnimationLoader.load(animationKey, 4, true);
            }
            
            // 如果没有找到确切分值的动画，使用通用动画
            if (goldAnimation == null || goldAnimation.getFrames().isEmpty()) {
                switch (animationType) {
                    case GOLD_SMALL:
                    case GOLD_MEDIUM:
                        goldAnimation = FrameAnimationLoader.load("goldItem", 3, true);
                        break;
                    case GOLD_LARGE:
                        goldAnimation = FrameAnimationLoader.load("point40", 4, true);
                        break;
                    case HUNDRED_BONUS:
                        goldAnimation = FrameAnimationLoader.load("point100", 5, true);
                        break;
                }
            }
            
            if (goldAnimation != null && !goldAnimation.getFrames().isEmpty()) {
                setIcon(goldAnimation.getCurrentFrame());
                // 根据图片大小设置元素尺寸
                setWidth(getIcon().getIconWidth());
                setHeight(getIcon().getIconHeight());
            } else {
                // 动画加载失败，使用静态fallback图标
                useFallbackIcon();
            }
        } catch (Exception e) {
            System.err.println("加载金币动画失败: " + e.getMessage());
            e.printStackTrace();
            // fallback：使用默认图标
            useFallbackIcon();
        }
    }
    
    /**
     * 根据确切的金币数量获取动画键名
     */
    private String getAnimationKey() {
        switch (coinValue) {
            case 40: return "point40";
            case 50: return "point50";
            case 60: return "point60";
            case 70: return "point70";
            case 80: return "point80";
            case 90: return "point90";
            case 100: return "point100";
            case 120: return "point120";
            case 150: return "point150";
            default: return null; // 使用通用动画
        }
    }
    
    /**
     * 使用备用图标
     */
    private void useFallbackIcon() {
        goldAnimation = null; // 清除无效动画
        
        // 尝试使用静态图标
        String[] fallbackIcons = {
            "goldItem.1", "goldNum.1", "highPoint.1", "hundred.1",
            "goldItem", "goldNum", "highPoint", "hundred"
        };
        
        for (String iconKey : fallbackIcons) {
            ImageIcon fallbackIcon = GameLoader.imgMap.get(iconKey);
            if (fallbackIcon != null) {
                setIcon(fallbackIcon);
                setWidth(fallbackIcon.getIconWidth());
                setHeight(fallbackIcon.getIconHeight());
                System.out.println("使用备用图标: " + iconKey);
                return;
            }
        }
        
        // 如果所有图标都不存在，创建一个简单的默认图标
        setWidth(32);
        setHeight(32);
        System.out.println("所有图标都无法加载，使用默认尺寸");
    }
    
    @Override
    public void update(long time) {
        long elapsed = time - startTime;
        
        if (elapsed >= duration) {
            setAlive(false);
            return;
        }
        
        // 更新动画帧 - 只有在动画有效时才更新
        if (goldAnimation != null && !goldAnimation.getFrames().isEmpty()) {
            goldAnimation.update();
            setIcon(goldAnimation.getCurrentFrame());
        }
        
        // 计算动画进度 (0.0 到 1.0)
        float progress = (float) elapsed / duration;
        
        // 缓动函数：快速开始，慢慢结束
        float easeProgress = 1 - (float) Math.pow(1 - progress, 3);
        
        // 位置动画：向上飘移
        currentY = getY() + (targetY - getY()) * easeProgress;
        setY((int) currentY);
        
        // 透明度动画：后半段逐渐消失
        if (progress > 0.6f) {
            alpha = 1.0f - ((progress - 0.6f) / 0.4f);
        }
    }
    
    @Override
    public void draw(Graphics g) {
        if (!isAlive()) {
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // 设置透明度
        g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha));
        
        // 绘制金币图标（如果存在）
        if (getIcon() != null) {
            g2d.drawImage(getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
        }
        
        // 绘制金币数量文字
        drawCoinText(g2d);
        
        g2d.dispose();
    }
    
    /**
     * 绘制金币数量文字
     */
    private void drawCoinText(Graphics2D g2d) {
        // 选择字体大小
        int fontSize = getFontSize();
        Font font = GameLoader.fontMap.get("default");
        if (font != null) {
            g2d.setFont(font.deriveFont(Font.BOLD, fontSize));
        } else {
            g2d.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        }
        
        String text = "+" + coinValue;
        
        // 计算文字位置（在图标右侧）
        int textX = getX() + getWidth() + 5;
        int textY = getY() + getHeight() / 2 + fontSize / 3;
        
        // 根据金币数量选择颜色
        Color textColor = getTextColor();
        Color outlineColor = Color.BLACK;
        
        // 绘制文字
        GraphicsUtils.drawStringWithOutline(g2d, text, textX, textY, textColor, outlineColor);
    }
    
    /**
     * 根据动画类型获取字体大小
     */
    private int getFontSize() {
        switch (animationType) {
            case HUNDRED_BONUS:
                return 24;
            case GOLD_LARGE:
                return 20;
            case GOLD_MEDIUM:
                return 16;
            case GOLD_SMALL:
            default:
                return 14;
        }
    }
    
    /**
     * 根据动画类型获取文字颜色
     */
    private Color getTextColor() {
        switch (animationType) {
            case HUNDRED_BONUS:
                return new Color(255, 215, 0); // 金色
            case GOLD_LARGE:
                return new Color(255, 165, 0); // 橙金色
            case GOLD_MEDIUM:
                return new Color(255, 255, 0); // 黄色
            case GOLD_SMALL:
            default:
                return new Color(255, 255, 255); // 白色
        }
    }
    
    public int getCoinValue() {
        return coinValue;
    }
    
    public AnimationType getAnimationType() {
        return animationType;
    }
}
