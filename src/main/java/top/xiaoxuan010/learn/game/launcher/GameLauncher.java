package top.xiaoxuan010.learn.game.launcher;

import top.xiaoxuan010.learn.game.controller.GameKeyListener;
import top.xiaoxuan010.learn.game.controller.GameMainThread;
import top.xiaoxuan010.learn.game.controller.GameMouseListener;
import top.xiaoxuan010.learn.game.controller.GameMouseMotionListener;
import top.xiaoxuan010.learn.game.view.GameFrame;
import top.xiaoxuan010.learn.game.view.GameMainPanel;

public class GameLauncher {
    public static void main(String[] args) {
        GameFrame gameJFrame = new GameFrame();

        GameMainPanel gameMainPanel = new GameMainPanel();
        gameJFrame.setGamePanel(gameMainPanel);

        GameKeyListener gameKeyListener = new GameKeyListener();
        gameJFrame.setKeyListener(gameKeyListener);

        GameMouseMotionListener gameMouseMotionListener = new GameMouseMotionListener();
        gameMainPanel.addMouseMotionListener(gameMouseMotionListener);

        GameMouseListener gameMouseListener = new GameMouseListener();
        // gameJFrame.setMouseListener(gameMouseListener);
        gameMainPanel.addMouseListener(gameMouseListener);

        GameMainThread gameMainThread = new GameMainThread();
        gameJFrame.setGameThread(gameMainThread);

        gameJFrame.start();
    }
}
