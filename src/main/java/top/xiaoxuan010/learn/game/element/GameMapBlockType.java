package top.xiaoxuan010.learn.game.element;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.xiaoxuan010.learn.game.manager.GameLoader;

@Getter
@AllArgsConstructor
public enum GameMapBlockType {
    GRASS("GRASS", GameLoader.imgMap.get("resource.images.mapblock.grass"), true, false, 0),
    BRICK("BRICK", GameLoader.imgMap.get("resource.images.mapblock.brick"), false, true, 2),
    RIVER("RIVER", GameLoader.imgMap.get("resource.images.mapblock.river"), false, false, 0),
    IRON("IRON", GameLoader.imgMap.get("resource.images.mapblock.iron"), false, false, 3);

    private final String blockName;
    private final ImageIcon imageIcon;
    private final boolean passable;
    private final boolean destructible;
    private final int HP;

    private static final Map<String, GameMapBlockType> NAME_MAP = new HashMap<>();

    static {
        for (GameMapBlockType type : values()) {
            NAME_MAP.put(type.blockName, type);
        }
    }
}
