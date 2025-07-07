package top.xiaoxuan010.learn.game.view;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import top.xiaoxuan010.learn.game.manager.FishManager;
import top.xiaoxuan010.learn.game.state.GameState;
import top.xiaoxuan010.learn.game.state.GameStateManager;

public class GameMainPanel extends JPanel implements Runnable {
    private FishManager fishManager;

    private GameStateManager stateManager;

    public GameMainPanel() {
        init();
    }

    public void init() {
        fishManager = FishManager.getInstance();
        stateManager = GameStateManager.getInstance();

        // 添加鼠标监听器
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    private void handleMouseClick(int x, int y) {
        GameState currentState = stateManager.getCurrentState();

        switch (currentState) {
            case MAIN_MENU:
                // 检查是否点击了开始按钮 (300, 250, 200, 60)
                if (x >= 300 && x <= 500 && y >= 250 && y <= 310) {
                    // 进入加载状态，而不是直接开始游戏
                    stateManager.setState(GameState.LOADING);
                }
                // 检查是否点击了退出按钮 (300, 350, 200, 60)
                else if (x >= 300 && x <= 500 && y >= 350 && y <= 410) {
                    System.exit(0);
                }
                break;
            case GAME_PLAYING:
                // 处理游戏中的点击事件
                break;
            case GAME_OVER:
                // 处理游戏结束界面的点击事件
                break;
            case LOADING:
                break;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 根据当前状态渲染不同内容
        if (stateManager.getCurrentStateHandler() != null) {
            stateManager.getCurrentStateHandler().render(g);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(16); // 约60FPS

                // 更新鱼类管理器
                fishManager.update();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }
}
