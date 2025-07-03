package top.xiaoxuan010.learn.game.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import top.xiaoxuan010.learn.game.element.ElementObj;

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

    private Map<GameElementType, List<ElementObj>> gameElements;

    public void addElement(ElementObj element, GameElementType elementType) {
        if (gameElements.containsKey(elementType)) {
            gameElements.get(elementType).add(element);
        } else {
            throw new IllegalArgumentException("Invalid game element type: " + elementType);
        }
    }

    public List<ElementObj> getElementsByType(GameElementType elementType) {
        if (gameElements.containsKey(elementType)) {
            return gameElements.get(elementType);
        } else {
            throw new IllegalArgumentException("Invalid game element type: " + elementType);
        }
    }

    public void init() {
        gameElements = new HashMap<GameElementType, List<ElementObj>>();
        gameElements.put(GameElementType.PLAYER, new ArrayList<ElementObj>());
        gameElements.put(GameElementType.MAP, new ArrayList<ElementObj>());
        gameElements.put(GameElementType.ENEMY, new ArrayList<ElementObj>());
        gameElements.put(GameElementType.BOSS, new ArrayList<ElementObj>());
    }
}
