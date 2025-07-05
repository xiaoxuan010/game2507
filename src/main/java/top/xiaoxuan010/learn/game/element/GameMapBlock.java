package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameMapBlock extends GameElement {
    private int HP;

    public GameMapBlock(int x, int y, String blockName) {
        this.setX(x);
        this.setY(y);

        GameMapBlockType blockType = GameMapBlockType.valueOf(blockName.toUpperCase());

        this.setHP(blockType.getHP());
        this.setIcon(blockType.getImageIcon());
        this.setWidth(this.getIcon().getIconWidth());
        this.setHeight(this.getIcon().getIconHeight());
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }

    @Override
    public void onCollision(GameElement other) {
        if (other instanceof Bullet) {
            this.HP--;
            if (this.HP <= 0) {
                this.setAlive(false);
            }
        }
    }

}
