package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;
import java.util.Random;

import javax.swing.ImageIcon;

public class Enemy extends GameElement {
    public Enemy() {
        super();
        this.setWidth(50);
        this.setHeight(50);
        Random random = new Random();
        this.setX(random.nextInt(720 - this.getWidth()));
        this.setY(random.nextInt(480 - this.getHeight()));
        this.setIcon(new ImageIcon("src/main/resources/images/tank/bot/bot_up.png"));
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }

}
