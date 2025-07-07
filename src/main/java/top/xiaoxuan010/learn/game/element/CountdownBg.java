package top.xiaoxuan010.learn.game.element;

import top.xiaoxuan010.learn.game.manager.GameLoader;

public class CountdownBg extends GameElement {
    public CountdownBg() {
        super(120, 430, GameLoader.imgMap.get("countdown.background"));
    }
}
