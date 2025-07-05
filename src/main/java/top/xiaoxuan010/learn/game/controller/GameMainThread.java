package top.xiaoxuan010.learn.game.controller;

import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import top.xiaoxuan010.learn.game.element.Enemy;
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

        for (int i = 0; i < 10; i++) {
            Enemy enemy = new Enemy();
            elementManager.addElement(enemy, GameElementType.ENEMY);
        }
    }

    private void gameRun() {
        while (true) {
            Map<GameElementType, List<GameElement>> gameElements = elementManager.getGameElements();
            gameElements.forEach((_, elements) -> {
                elementUpdate(elements);
            });

            collisionDetection(gameElements);

            try {
                sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void elementUpdate(List<GameElement> elements) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            GameElement element = elements.get(i);
            if (element != null) {
                if (element.isAlive()) {
                    element.updateStatus();
                } else {
                    elements.remove(i);
                }
            }
        }
    }

    private void collisionDetection(Map<GameElementType, List<GameElement>> gameElements) {
        List<GameElement> enemyList = gameElements.get(GameElementType.ENEMY);
        List<GameElement> bulletList = gameElements.get(GameElementType.BULLET);

        for (int i = 0; i < bulletList.size(); i++) {
            GameElement bullet = bulletList.get(i);
            for (int j = 0; j < enemyList.size(); j++) {
                GameElement enemy = enemyList.get(j);

                if (bullet.isAlive() && enemy.isAlive() && bullet.isCollided(enemy)) {
                    bullet.setAlive(false);
                    enemy.setAlive(false);
                    break;
                }
            }
        }
    }

    private void gameOver() {
    }
}
