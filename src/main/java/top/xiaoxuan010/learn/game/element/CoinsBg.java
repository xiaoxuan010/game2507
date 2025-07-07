package top.xiaoxuan010.learn.game.element;

import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.manager.GameLoader;

public class CoinsBg extends GameElement {
    public CoinsBg() {
        super(550, 430, GameLoader.imgMap.get("coins.background"));
    }

}
