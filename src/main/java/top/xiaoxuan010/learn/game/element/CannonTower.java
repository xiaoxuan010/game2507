package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;

import top.xiaoxuan010.learn.game.manager.GameLoader;

public class CannonTower extends GameElement {

    private int level = 1;
    private boolean isFiring = false;

    public CannonTower() {
        this.setX(360);
        this.setY(415);
        this.setWidth(50);
        this.setHeight(50);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }

    @Override
    protected void refreshIcon() {
        this.setIcon(GameLoader.imgMap.get("cannon.tower.lv" + level + (isFiring ? ".fire" : ".normal")));
    }

}
