package top.xiaoxuan010.learn.game.state.states;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.FishManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;
import top.xiaoxuan010.learn.game.manager.GameLoader;
import top.xiaoxuan010.learn.game.state.GameState;
import top.xiaoxuan010.learn.game.state.GameStateManager;

@Slf4j
public class GamePlayingState extends BaseGameState {
    private final ElementManager elementManager = ElementManager.getInstance();
    private final FishManager fishManager = FishManager.getInstance();

    public GamePlayingState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void enter() {
        log.info("Entering game playing state");
        loadGameContent();
    }

    @Override
    public void exit() {
        log.info("Exiting game playing state");
        cleanupGameContent();
    }

    private void loadGameContent() {
        elementManager.clear();
        GameLoader.loadBackground(2);
        GameLoader.loadPlayer();
        GameLoader.loadUI();
    }

    private void cleanupGameContent() {
        elementManager.clear();
    }

    @Override
    public void update(long deltaTime) {
        // 更新鱼类管理器
        fishManager.update();

        // 更新所有游戏元素
        Map<GameElementType, List<GameElement>> gameElements = elementManager.getGameElements();
        gameElements.forEach((_, elements) -> {
            updateElements(elements, deltaTime);
        });

        // 碰撞检测
        performCollisionDetection(gameElements);

        // 检查游戏结束条件
        checkGameOverConditions();
    }

    private void updateElements(List<GameElement> elements, long deltaTime) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            GameElement element = elements.get(i);
            if (element != null) {
                if (element.isAlive()) {
                    element.update(deltaTime);
                } else {
                    elements.remove(i);
                }
            }
        }
    }

    private void performCollisionDetection(Map<GameElementType, List<GameElement>> gameElements) {
        collisionDetection(gameElements.get(GameElementType.BULLET), gameElements.get(GameElementType.FISH));
    }

    private void collisionDetection(List<GameElement> elements1, List<GameElement> elements2) {
        if (elements1 == null || elements2 == null)
            return;

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

    private void checkGameOverConditions() {
        // 检查游戏结束条件，比如时间到了或者完成目标
        // 这里需要根据您的具体游戏逻辑来实现
    }

    @Override
    public void render(Graphics g) {
        // 渲染所有游戏元素
        for (GameElementType elementType : GameElementType.values()) {
            List<GameElement> elements = elementManager.getElementsByType(elementType);
            for (int i = 0; i < elements.size(); i++) {
                elements.get(i).draw(g);
            }
        }
    }

    @Override
    public void handleInput() {
        // 处理游戏中的输入
    }

    public void endGame() {
        stateManager.setState(GameState.GAME_OVER);
    }
}