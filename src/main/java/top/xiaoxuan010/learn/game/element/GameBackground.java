package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;

import lombok.Getter;
import lombok.Setter;
import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.manager.GameLoader;

@Getter
@Setter
public class GameBackground extends GameElement {

    /**
     * 游戏背景类，用于加载和显示游戏背景图片
     * 
     * @param gameIndex 关卡编号（从1开始）
     */
    public GameBackground(Integer gameIndex) {
        this.setX(0);
        this.setY(0);

        gameIndex--;

        this.setIcon(GameLoader.imgMap.get("background.game." + gameIndex));
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }

}
