package top.xiaoxuan010.learn.game.element.components;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Set;

import javax.swing.ImageIcon;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class GameElement {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected ImageIcon icon;

    protected boolean isAlive = true;

    public GameElement(int x, int y, int width, int height, ImageIcon icon) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.icon = icon;
    }

    public GameElement(int x, int y, ImageIcon icon) {
        this(x, y, -1, -1, icon);
        if (icon != null) {
            this.width = icon.getIconWidth();
            this.height = icon.getIconHeight();
        }
    }

    public void draw(Graphics g) {
        if (g == null || this.getIcon() == null) {
            return;
        }
        g.drawImage(this.getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }

    /**
     * 当按键被点击时调用的方法。
     * 子类可以重写此方法以实现自定义的按键响应逻辑。
     * 
     * @param keyCode   键盘按键代码
     * @param isKeyDown 键盘按键动作，true表示按下，false表示松开
     */
    public void keyClicked(int keyCode, boolean isKeyDown) {
        // 无默认实现
    }

    /**
     * 当按键被点击时调用的方法。
     * 子类可以重写此方法以实现自定义的按键响应逻辑。
     *
     * @param keySet 当前被点击的按键集合，包含所有被按下的键的键码。
     */
    public void keyUpdated(Set<Integer> keySet) {
        // 无默认实现
    }

    public void mouseMotionUpdated(int x, int y) {
        // 无默认实现
    }

    public void update(long time) {
        update();
    }

    public void update() {
        // 无默认实现
    }

    /*
     * 获取对象的碰撞箱
     * 
     * @return 碰撞箱矩形
     */
    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * 碰撞检测
     * 
     * @param other 另一个游戏元素
     * @return 如果与另一个游戏元素发生碰撞，则返回true，否则返回false
     */
    public boolean isCollided(GameElement other) {
        return this.getRectangle().intersects(other.getRectangle());
    }

    public void onCollision(GameElement other) {
        // 无默认实现
    }

    /**
     * 鼠标点击事件处理方法。
     * 子类可以重写此方法以实现自定义的鼠标点击响应逻辑。
     * 
     * @param x 鼠标点击的x坐标
     * @param y 鼠标点击的y坐标
     * @return 事件是否已被处理。如果返回true，表示事件已被处理，其他元素不需要再处理此事件。
     */
    public boolean mouseClicked(int x, int y) {
        // 无默认实现
        return false;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
        if (icon != null) {
            this.width = icon.getIconWidth();
            this.height = icon.getIconHeight();
        }
    }
}
