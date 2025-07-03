package top.xiaoxuan010.learn.game.show;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import top.xiaoxuan010.learn.game.element.ElementObj;
import top.xiaoxuan010.learn.game.element.Player;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;

public class GameMainPanel extends JPanel {
    private ElementManager elementManager;

    public GameMainPanel() {
        init();
        load();
    }

    public void load() {
        ImageIcon icon = new ImageIcon("src/main/resources/images/tank/player1//player1_up.png");
        ElementObj player1 = new Player(100, 100, 50, 50, icon);
        elementManager.addElement(player1, GameElementType.PLAYER);
        ElementObj player2 = new Player(200, 200, 50, 50, icon);
        elementManager.addElement(player2, GameElementType.ENEMY);
        ElementObj player3 = new Player(300, 300, 50, 50, icon);
        elementManager.addElement(player3, GameElementType.BOSS);
    }

    public void init() {
        elementManager = ElementManager.getInstance();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        elementManager.getGameElements().forEach((_, elements) -> {
            for (ElementObj element : elements) {
                element.showElement(g);
            }
        });

    }
}
