package top.xiaoxuan010.learn.game.show;

import java.awt.Graphics;

import javax.swing.JPanel;

import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;

public class GameMainPanel extends JPanel implements Runnable {
    private ElementManager elementManager;

    public GameMainPanel() {
        init();
    }

    public void init() {
        elementManager = ElementManager.getInstance();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (GameElementType elementType : GameElementType.values()) {
            elementManager.getElementsByType(elementType).forEach(element -> {
                if (element != null) {
                    element.showElement(g);
                }
            });
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }
}
