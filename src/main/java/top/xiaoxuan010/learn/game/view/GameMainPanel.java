package top.xiaoxuan010.learn.game.view;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;

import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.FishManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;
import top.xiaoxuan010.learn.game.state.GameStateManager;

public class GameMainPanel extends JPanel implements Runnable {
    private final ElementManager elementManager = ElementManager.getInstance();

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
        if (stateManager.getCurrentStateHandler() != null) {
            stateManager.getCurrentStateHandler().handleInput(x, y);
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

                long currentTime = System.currentTimeMillis();

                // 更新鱼类管理器
                fishManager.update();

                // 更新游戏状态管理器
                GameStateManager.getInstance().update(currentTime);

                // 更新所有游戏元素
                for (GameElementType elementType : GameElementType.values()) {
                    List<GameElement> elements = elementManager.getElementsByType(elementType);
                    for (int i = elements.size() - 1; i >= 0; i--) {
                        GameElement element = elements.get(i);
                        if (element != null) {
                            if (element.isAlive()) {
                                element.update(currentTime);
                            } else {
                                elements.remove(i);
                            }
                        }
                    }
                }

                // 碰撞检测 - 鱼网与鱼
                collisionDetection(elementManager.getElementsByType(GameElementType.BULLET),
                        elementManager.getElementsByType(GameElementType.FISH));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }

    private void collisionDetection(List<GameElement> elements1, List<GameElement> elements2) {
        for (int i = 0; i < elements1.size(); i++) {
            GameElement elem1 = elements1.get(i);
            for (int j = 0; j < elements2.size(); j++) {
                GameElement elem2 = elements2.get(j);

                if (elem1.isAlive() && elem2.isAlive() && elem1.isCollided(elem2)) {
                    elem1.onCollision(elem2);
                    elem2.onCollision(elem1);
                    break;
                }
            }
        }
    }
}
