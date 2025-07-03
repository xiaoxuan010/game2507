package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class ElementObj {
    private int x;
    private int y;
    private int width;
    private int height;
    private ImageIcon icon;

    public ElementObj(int x, int y, int width, int height, ImageIcon icon) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.icon = icon;
    }

    public abstract void showElement(Graphics g);

}
