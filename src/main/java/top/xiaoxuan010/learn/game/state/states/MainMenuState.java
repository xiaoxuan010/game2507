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
import top.xiaoxuan010.learn.game.utils.GraphicsUtils;

@Slf4j
public class MainMenuState extends BaseGameState {

    public MainMenuState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void enter() {
        log.info("Entering main menu state");
    }

    @Override
    public void exit() {
        log.info("Exiting main menu state");
    }

    @Override
    public void update(long deltaTime) {

    }

    @Override
    public void render(Graphics g) {
        // 绘制背景
        ImageIcon bg = GameLoader.imgMap.get("background.menu");
        if (bg != null) {
            g.drawImage(bg.getImage(), 0, 0, 800, 600, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, 800, 600);
        }

        // 使用预加载的字体
        Font menuFont = GameLoader.fontMap.get("default");
        if (menuFont != null) {
            g.setFont(menuFont.deriveFont(72f));
        } else {
            g.setFont(new Font("微软雅黑", Font.BOLD, 36));
        }

        // 绘制游戏标题
        g.setColor(Color.WHITE);
        String title = "捕鱼达人";
        FontMetrics titleFm = g.getFontMetrics();
        int titleX = (800 - titleFm.stringWidth(title)) / 2;
        GraphicsUtils.drawStringWithOutline(g, title, titleX, 150, Color.WHITE, Color.BLACK);

        // 绘制开始按钮
        g.setColor(new Color(0, 200, 83)); // 深绿色
        g.fillRect(300, 250, 200, 60);
        g.setColor(Color.WHITE);
        g.drawRect(300, 250, 200, 60);

        if (menuFont != null) {
            g.setFont(menuFont.deriveFont(20f));
        } else {
            g.setFont(new Font("微软雅黑", Font.BOLD, 20));
        }

        String startText = "开始游戏";
        FontMetrics startFm = g.getFontMetrics();
        int startX = 300 + (200 - startFm.stringWidth(startText)) / 2;
        int startY = 250 + (60 + startFm.getHeight()) / 2 - startFm.getDescent();
        g.drawString(startText, startX, startY);

        // 绘制退出按钮
        g.setColor(Color.GRAY);
        g.fillRect(300, 350, 200, 60);
        g.setColor(Color.WHITE);
        g.drawRect(300, 350, 200, 60);

        String exitText = "退出游戏";
        FontMetrics exitFm = g.getFontMetrics();
        int exitX = 300 + (200 - exitFm.stringWidth(exitText)) / 2;
        int exitY = 350 + (60 + exitFm.getHeight()) / 2 - exitFm.getDescent();
        g.drawString(exitText, exitX, exitY);
    }

    @Override
    public void handleInput() {
        // 输入处理在GameMainPanel中完成
    }

    @Override
    public void handleInput(int x, int y) {
        // 检查是否点击了开始按钮 (300, 250, 200, 60)
        if (x >= 300 && x <= 500 && y >= 250 && y <= 310) {
            // 进入加载状态，而不是直接开始游戏
            if (GameLoader.isLoaded) {
                stateManager.setState(GameState.GAME_PLAYING);
            } else {
                stateManager.setState(GameState.LOADING);
            }
        }
        // 检查是否点击了退出按钮 (300, 350, 200, 60)
        else if (x >= 300 && x <= 500 && y >= 350 && y <= 410) {
            System.exit(0);
        }
    }

    public void startGame() {
        // 当点击开始游戏时，进入加载状态
        stateManager.setState(GameState.LOADING);
    }
}