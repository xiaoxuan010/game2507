package top.xiaoxuan010.learn.game.state.states;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.element.CannonBg;
import top.xiaoxuan010.learn.game.element.CannonDowngradeBtn;
import top.xiaoxuan010.learn.game.element.CannonTower;
import top.xiaoxuan010.learn.game.element.CannonUpgradeBtn;
import top.xiaoxuan010.learn.game.element.CoinsBg;
import top.xiaoxuan010.learn.game.element.CountdownBg;
import top.xiaoxuan010.learn.game.element.GameBackground;
import top.xiaoxuan010.learn.game.element.components.Digit;
import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.FishManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;
import top.xiaoxuan010.learn.game.manager.utils.GameStateDataManager;
import top.xiaoxuan010.learn.game.state.GameState;
import top.xiaoxuan010.learn.game.state.GameStateManager;

@Slf4j
public class GamePlayingState extends BaseGameState {
    private final ElementManager elementManager = ElementManager.getInstance();
    private final FishManager fishManager = FishManager.getInstance();
    private final GameStateDataManager gameStateDataManager = GameStateDataManager.getInstance();

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
        this.loadBackground(2);
        this.loadPlayer();
        this.loadUI();
    }

    private void loadBackground(int mapId) {
        GameBackground gameBackground = new GameBackground(2);
        elementManager.addElement(gameBackground, GameElementType.MAP);
    }

    private void loadPlayer() {
        CannonTower cannonTower = new CannonTower();
        cannonTower.setLevel(1);
        elementManager.addElement(cannonTower, GameElementType.PLAYER);
    }

    public void loadUI() {
        GameStateDataManager gameStateManager = GameStateDataManager.getInstance();
        gameStateManager.reset();

        CannonUpgradeBtn cannonUpgradeBtn = new CannonUpgradeBtn();
        elementManager.addElement(cannonUpgradeBtn, GameElementType.UI);
        CannonDowngradeBtn cannonDowngradeBtn = new CannonDowngradeBtn();
        elementManager.addElement(cannonDowngradeBtn, GameElementType.UI);

        CountdownBg countdownBg = new CountdownBg();
        elementManager.addElement(countdownBg, GameElementType.UI);

        CoinsBg coinsBg = new CoinsBg();
        elementManager.addElement(coinsBg, GameElementType.UI);
        CannonBg cannonBg = new CannonBg();
        elementManager.addElement(cannonBg, GameElementType.MAP);

        Digit digit1 = new Digit(590, 435, () -> (GameStateDataManager.getInstance().getCoins() / 1000) % 10);
        elementManager.addElement(digit1, GameElementType.UI);
        Digit digit2 = new Digit(605, 435, () -> (GameStateDataManager.getInstance().getCoins() / 100) % 10);
        elementManager.addElement(digit2, GameElementType.UI);
        Digit digit3 = new Digit(620, 435, () -> (GameStateDataManager.getInstance().getCoins() / 10) % 10);
        elementManager.addElement(digit3, GameElementType.UI);
        Digit digit4 = new Digit(635, 435, () -> GameStateDataManager.getInstance().getCoins() % 10);
        elementManager.addElement(digit4, GameElementType.UI);

        elementManager.addElement(countdownBg, GameElementType.MAP);
        Digit timeDigit1 = new Digit(165, 435,
                () -> (GameStateDataManager.getInstance().getGameCountdown() / 10));
        elementManager.addElement(timeDigit1, GameElementType.UI);
        Digit timeDigit2 = new Digit(180, 435,
                () -> (GameStateDataManager.getInstance().getGameCountdown() % 10));
        elementManager.addElement(timeDigit2, GameElementType.UI);

    }

    private void cleanupGameContent() {
        elementManager.clear();
    }

    @Override
    public void update(long time) {
        // 更新鱼类管理器
        fishManager.update();

        // 更新游戏状态数据管理器
        gameStateDataManager.update(time);

        // 更新所有游戏元素
        Map<GameElementType, List<GameElement>> gameElements = elementManager.getGameElements();
        gameElements.forEach((_, elements) -> {
            updateElements(elements, time);
        });

        // 刷新元素列表（统一处理添加和删除）
        elementManager.refresh();

        // 碰撞检测
        performCollisionDetection(elementManager.getGameElements());

        // 检查游戏结束条件
        checkGameOverConditions();
    }

    private void updateElements(List<GameElement> elements, long time) {
        // 使用传统 for 循环避免并发修改异常
        for (int i = 0; i < elements.size(); i++) {
            GameElement element = elements.get(i);
            if (element != null && element.isAlive()) {
                element.update(time);
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