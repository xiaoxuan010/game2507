package top.xiaoxuan010.learn.game.state.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.manager.GameLoader;
import top.xiaoxuan010.learn.game.state.GameState;
import top.xiaoxuan010.learn.game.state.GameStateManager;

@Slf4j
public class LoadingState extends BaseGameState {
    private boolean loadingComplete = false;
    private String loadingText = "正在游戏资源中";
    private int loadingProgress = 0;

    public LoadingState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void enter() {
        log.info("Entering loading state");
        loadingComplete = false;
        loadingProgress = 0;

        // 异步加载所有游戏资源
        new Thread(() -> {
            GameLoader.loadImages(progress -> {
                this.loadingProgress = progress;
            });
            loadingComplete = true;
        }).start();
    }

    @Override
    public void exit() {
        log.info("Exiting loading state");
    }

    @Override
    public void update(long deltaTime) {
        if (loadingComplete) {
            // 加载完成后进入游戏状态
            stateManager.setState(GameState.GAME_PLAYING);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 600);

        ImageIcon bg = GameLoader.imgMap.get("background.menu");
        // 绘制加载背景
        if (bg != null) {
            g.drawImage(bg.getImage(), 0, 0, null);
        }

        // 使用预加载的字体
        Font loadingFont = GameLoader.fontMap.get("default");
        if (loadingFont != null) {
            g.setFont(loadingFont.deriveFont(Font.BOLD, 24f));
        } else {
            g.setFont(new Font("微软雅黑", Font.BOLD, 24));
        }

        // 绘制加载文本
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int x = (800 - fm.stringWidth(loadingText)) / 2;
        int y = 300;
        g.drawString(loadingText, x, y);

        // 绘制进度条
        g.setColor(Color.GRAY);
        g.fillRect(250, 350, 300, 20);
        g.setColor(Color.GREEN);
        g.fillRect(250, 350, (int) (300 * loadingProgress / 100.0), 20);

        // 绘制进度百分比
        String progressText = loadingProgress + "%";
        int progressX = (800 - fm.stringWidth(progressText)) / 2;
        g.drawString(progressText, progressX, 400);
    }

    @Override
    public void handleInput() {
        // 加载状态不处理输入
    }
}