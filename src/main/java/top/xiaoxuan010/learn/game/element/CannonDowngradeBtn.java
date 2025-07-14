package top.xiaoxuan010.learn.game.element;

import java.awt.event.KeyEvent;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.element.components.GameElement;
import top.xiaoxuan010.learn.game.element.components.ImageButton;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;
import top.xiaoxuan010.learn.game.manager.GameLoader;

@Slf4j
public class CannonDowngradeBtn extends ImageButton {
    private final ElementManager elementManager = ElementManager.getInstance();

    public CannonDowngradeBtn() {
        this.setIcon(GameLoader.imgMap.get("cannon.downgrade"));

        this.setX(265);
        this.setY(415);
    }

    @Override
    public void onClick() {
        for (GameElement cannonTower : elementManager.getElementsByType(GameElementType.PLAYER)) {
            if (cannonTower instanceof CannonTower) {
                CannonTower cannonElement = (CannonTower) cannonTower;
                if (cannonElement.getLevel() > 1) {
                    // 降级大炮等级
                    cannonElement.setLevel(cannonElement.getLevel() - 1);
                    
                    // 同时同步降级渔网等级
                    top.xiaoxuan010.learn.game.manager.PlayerEquipmentManager equipmentManager = 
                        top.xiaoxuan010.learn.game.manager.PlayerEquipmentManager.getInstance();
                    equipmentManager.setCurrentNetLevel(cannonElement.getLevel());
                    
                    log.debug("Cannon and Net downgraded to level {}", cannonElement.getLevel());
                } else {
                    log.debug("Cannon is already at minimum level.");
                }
            }
        }
    }

    @Override
    public void keyUpdated(Set<Integer> keySet) {
        if (keySet.contains(KeyEvent.VK_DOWN) || keySet.contains(KeyEvent.VK_S)) {
            onClick();
        }
    }

}
