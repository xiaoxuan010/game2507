package top.xiaoxuan010.learn.game.controller;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.state.GameStateManager;
import top.xiaoxuan010.learn.game.view.GameMainPanel;

@Slf4j
@Setter
public class GameMainThread extends Thread {
    private final GameStateManager stateManager = GameStateManager.getInstance();
    private volatile boolean running = true;

    private GameMainPanel gamePanel;

    @Override
    public void run() {
        while (running) {
            // 更新当前游戏状态
            stateManager.update(System.currentTimeMillis());

            if (gamePanel != null) {
                gamePanel.repaint();
            } else {
                log.warn("Game main panel is not initialized yet.");
            }

            try {
                sleep(16);
            } catch (InterruptedException e) {
                log.error("Game main thread interrupted", e);
            }
        }
        log.info("Game main thread stopped.");
    }

    public void stopGame() {
        running = false;
    }
}
