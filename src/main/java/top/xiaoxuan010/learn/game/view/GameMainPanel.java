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
                
                // 更新鱼类管理器
                fishManager.update();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }
}
