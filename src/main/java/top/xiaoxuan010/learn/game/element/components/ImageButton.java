package top.xiaoxuan010.learn.game.element.components;

import java.awt.Graphics;

import top.xiaoxuan010.learn.game.element.utils.Clickable;

public abstract class ImageButton extends GameElement implements Clickable {
    @Override
    public void draw(Graphics g) {
        if (icon != null) {
            g.drawImage(icon.getImage(), x, y, width, height, null);
        }
    }

    @Override
    public boolean mouseClicked(int x, int y) {
        if (getRectangle().contains(x, y)) {
            onClick();
            return true; // 返回true表示点击事件已被处理
        } else {
            return false; // 返回false表示点击事件未被处理
        }
    }

}
