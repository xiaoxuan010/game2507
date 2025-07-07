package top.xiaoxuan010.learn.game.view;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.FishManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;

public class GameMainPanel extends JPanel implements Runnable {
    private ElementManager elementManager;
    private FishManager fishManager;

    public GameMainPanel() {
        init();
    }

    public void init() {
        elementManager = ElementManager.getInstance();
        fishManager = FishManager.getInstance();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (GameElementType elementType : GameElementType.values()) {
            List<GameElement> elements = elementManager.getElementsByType(elementType);
            for (int i = 0; i < elements.size(); i++) {
                elements.get(i).draw(g);
            }
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
