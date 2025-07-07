package top.xiaoxuan010.learn.game.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import top.xiaoxuan010.learn.game.element.components.GameElement;

@Getter
public class ElementManager {
    private static ElementManager instance = null;

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
        if (gameElements.containsKey(elementType)) {
            gameElements.get(elementType).add(element);
        } else {
            throw new IllegalArgumentException("Invalid game element type: " + elementType);
        }
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
}
