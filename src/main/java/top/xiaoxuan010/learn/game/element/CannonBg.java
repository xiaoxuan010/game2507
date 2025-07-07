package top.xiaoxuan010.learn.game.element;

import top.xiaoxuan010.learn.game.manager.GameLoader;

public class CannonBg extends GameElement {
    public CannonBg() {
        this.setX(354);
        this.setY(415);
        this.setIcon(GameLoader.imgMap.get("cannon.background"));
    }
}
