package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;

import lombok.Getter;
import lombok.Setter;
import top.xiaoxuan010.learn.game.manager.GameLoader;

@Getter
@Setter
public class GameBackground extends GameElement {

    public GameBackground(String bgName) {
        this.setX(0);
        this.setY(0);

        this.setIcon(GameLoader.imgMap.get(bgName));

        this.setWidth(this.getIcon().getIconWidth());
        this.setHeight(this.getIcon().getIconHeight());
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }

}
