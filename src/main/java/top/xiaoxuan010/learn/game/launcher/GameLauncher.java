package top.xiaoxuan010.learn.game.launcher;

import top.xiaoxuan010.learn.game.controller.GameListener;
import top.xiaoxuan010.learn.game.controller.GameMainThread;
import top.xiaoxuan010.learn.game.view.GameFrame;
import top.xiaoxuan010.learn.game.view.GameMainPanel;

public class GameLauncher {
    public static void main(String[] args) {
        GameFrame gameJFrame = new GameFrame();

        GameMainPanel gameMainPanel = new GameMainPanel();

        gameJFrame.setGamePanel(gameMainPanel);

        GameListener gameListener = new GameListener();

        gameJFrame.setKeyListener(gameListener);

        GameMainThread gameMainThread = new GameMainThread();

        gameJFrame.setGameThread(gameMainThread);

        gameJFrame.start();
    }
}
