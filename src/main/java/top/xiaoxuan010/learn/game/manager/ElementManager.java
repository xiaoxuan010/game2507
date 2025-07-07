package top.xiaoxuan010.learn.game.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.Getter;
import top.xiaoxuan010.learn.game.element.components.GameElement;

@Getter
public class ElementManager {
    private static ElementManager instance = null;

    // 待添加元素队列
    private final ConcurrentLinkedQueue<ElementToAdd> elementsToAdd = new ConcurrentLinkedQueue<>();
    // 待删除元素队列
    private final ConcurrentLinkedQueue<GameElement> elementsToRemove = new ConcurrentLinkedQueue<>();

    // 内部类用于存储待添加的元素及其类型
    private static class ElementToAdd {
        final GameElement element;
        final GameElementType type;

        ElementToAdd(GameElement element, GameElementType type) {
            this.element = element;
            this.type = type;
        }
    }

    public static synchronized ElementManager getInstance() {
        if (instance == null) {
            instance = new ElementManager();
        }
        return instance;
    }

    private ElementManager() {
        init();
    }

    private Map<GameElementType, List<GameElement>> gameElements;

    public void addElement(GameElement element, GameElementType elementType) {
        elementsToAdd.offer(new ElementToAdd(element, elementType));
    }

    public void removeElement(GameElement element) {
        elementsToRemove.offer(element);
    }

    public List<GameElement> getElementsByType(GameElementType elementType) {
        if (gameElements.containsKey(elementType)) {
            return gameElements.get(elementType);
        } else {
            throw new IllegalArgumentException("Invalid game element type: " + elementType);
        }
    }

    public void init() {
        gameElements = new HashMap<GameElementType, List<GameElement>>();
        for (GameElementType type : GameElementType.values()) {
            gameElements.put(type, new ArrayList<GameElement>());
        }
    }

    public void clear() {
        gameElements.values().forEach(List::clear);
    }

    public void clearType(GameElementType type) {
        List<GameElement> elements = gameElements.get(type);
        if (elements != null) {
            elements.clear();
        }
    }

    /**
     * 刷新元素列表，统一处理待添加和待删除的元素
     */
    public synchronized void refresh() {
        // 处理待添加的元素
        ElementToAdd elementToAdd;
        while ((elementToAdd = elementsToAdd.poll()) != null) {
            if (gameElements.containsKey(elementToAdd.type)) {
                gameElements.get(elementToAdd.type).add(elementToAdd.element);
            }
        }

        // 处理待删除的元素
        GameElement elementToRemove;
        while ((elementToRemove = elementsToRemove.poll()) != null) {
            for (List<GameElement> elements : gameElements.values()) {
                elements.remove(elementToRemove);
            }
        }

        // 清理已死亡的元素
        for (List<GameElement> elements : gameElements.values()) {
            elements.removeIf(element -> !element.isAlive());
        }
    }
}
