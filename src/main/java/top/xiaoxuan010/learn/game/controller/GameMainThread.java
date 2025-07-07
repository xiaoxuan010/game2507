package top.xiaoxuan010.learn.game.controller;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.state.GameStateManager;

@Slf4j
public class GameMainThread extends Thread {
    private final GameStateManager stateManager = GameStateManager.getInstance();
    private volatile boolean running = true;

    @Override
    public void run() {

        while (running) {
            // 更新当前游戏状态
            stateManager.update(System.currentTimeMillis());

            try {
                sleep(16);
            } catch (InterruptedException e) {
                log.error("Game main thread interrupted", e);
            }
        }
    }

    public void stopGame() {
        running = false;
    }
}
