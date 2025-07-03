package top.xiaoxuan010.learn.game.launcher;

import top.xiaoxuan010.learn.game.show.GameFrame;
import top.xiaoxuan010.learn.game.show.GameMainPanel;

public class GameStart {
    public static void main(String[] args) {
        GameFrame gameJFrame = new GameFrame();

        GameMainPanel gameMainPanel = new GameMainPanel();

        gameJFrame.setGamePanel(gameMainPanel);

        gameJFrame.start();
    }
}
