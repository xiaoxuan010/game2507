package top.xiaoxuan010.learn.game.controller;

import java.util.List;
import java.util.Map;

import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.FishManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;
import top.xiaoxuan010.learn.game.manager.GameLoader;

public class GameMainThread extends Thread {
    private final ElementManager elementManager = ElementManager.getInstance();
    private final FishManager fishManager = FishManager.getInstance();

    @Override
    public void run() {
        // while (true) {
        gameLoad();
        gameRun();
        gameOver();
        // }
    }

    private void gameLoad() {
        GameLoader.loadImages();
        GameLoader.loadBackground(9);
        GameLoader.loadPlayer();
        GameLoader.loadEnemies();
        GameLoader.loadUI();
    }

    private void gameRun() {
        while (true) {
            long time = System.currentTimeMillis();

            // 更新鱼类管理器
            fishManager.update();

            Map<GameElementType, List<GameElement>> gameElements = elementManager.getGameElements();
            gameElements.forEach((_, elements) -> {
                elementUpdate(elements, time);
            });

            collisionDetection(gameElements.get(GameElementType.BULLET), gameElements.get(GameElementType.ENEMY));
            collisionDetection(gameElements.get(GameElementType.BULLET), gameElements.get(GameElementType.MAP));
            collisionDetection(gameElements.get(GameElementType.BULLET), gameElements.get(GameElementType.FISH));

            try {
                sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void elementUpdate(List<GameElement> elements, long time) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            GameElement element = elements.get(i);
            if (element != null) {
                if (element.isAlive()) {
                    element.update(time);
                } else {
                    elements.remove(i);
                }
            }
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

    private void gameOver() {
    }
}
