package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Set;

import javax.swing.ImageIcon;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class GameElement {
    private int x;
    private int y;
    private int width;
    private int height;
    private ImageIcon icon;

    private boolean isAlive = true;

    public GameElement(int x, int y, int width, int height, ImageIcon icon) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.icon = icon;
    }

    public abstract void draw(Graphics g);

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

    public final void updateStatus() {
        refreshIcon();
        updatePosition();
        fire();
    }

    protected void updatePosition() {
        // 无默认实现
    }

    protected void fire() {
        // 无默认实现
    }

    protected void refreshIcon() {
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

}
