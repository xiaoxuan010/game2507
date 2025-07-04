package top.xiaoxuan010.learn.game.controller;

import javax.swing.ImageIcon;

import top.xiaoxuan010.learn.game.element.GameElement;
import top.xiaoxuan010.learn.game.element.Player;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;

public class GameMainThread extends Thread {
    private final ElementManager elementManager = ElementManager.getInstance();

    @Override
    public void run() {
        while (true) {
            gameLoad();
            gameRun();
            gameOver();
        }
    }

    private void gameLoad() {
        ImageIcon icon = new ImageIcon("src/main/resources/images/tank/player1/player1_up.png");
        GameElement player1 = new Player(100, 100, 50, 50, icon);
        elementManager.addElement(player1, GameElementType.PLAYER);
    }

    private void gameRun() {
        while (true) {
            elementManager.getGameElements().forEach((_, elements) -> {
                for (int i = elements.size() - 1; i >= 0; i--) {
                    GameElement element = elements.get(i);
                    if (element != null) {
                        if (element.isAlive()) {
                            element.model();
                        } else {
                            elements.remove(i);
                        }
                    }
                }
            });

            try {
                sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void gameOver() {
    }
}
