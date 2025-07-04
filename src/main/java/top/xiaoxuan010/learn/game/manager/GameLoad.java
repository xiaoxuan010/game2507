package top.xiaoxuan010.learn.game.manager;

import java.util.Map;

import javax.swing.ImageIcon;

import top.xiaoxuan010.learn.game.element.GameElementDirection;

public class GameLoad {
    // 图片资源映射
    public static Map<GameElementDirection, ImageIcon> imgMap;

    static {
        imgMap = Map.of(
                GameElementDirection.LEFT, new ImageIcon("src/main/resources/images/tank/player1/player1_left.png"),
                GameElementDirection.RIGHT, new ImageIcon("src/main/resources/images/tank/player1/player1_right.png"),
                GameElementDirection.UP, new ImageIcon("src/main/resources/images/tank/player1/player1_up.png"),
                GameElementDirection.DOWN, new ImageIcon("src/main/resources/images/tank/player1/player1_down.png"));
    }

}
