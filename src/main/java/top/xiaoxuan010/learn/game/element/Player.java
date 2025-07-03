package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Player extends ElementObj {
    public Player(int x, int y, int width, int height, ImageIcon icon) {
        super(x, y, width, height, icon);
    }

    @Override
    public void showElement(Graphics g) {
        g.drawImage(getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }
}
