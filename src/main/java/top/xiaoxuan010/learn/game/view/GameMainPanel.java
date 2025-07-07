package top.xiaoxuan010.learn.game.view;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import top.xiaoxuan010.learn.game.element.components.GameElement;
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
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }
}
