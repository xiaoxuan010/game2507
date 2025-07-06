package top.xiaoxuan010.learn.game.element;

import java.awt.event.KeyEvent;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;
import top.xiaoxuan010.learn.game.manager.GameLoader;

@Slf4j
public class CannonUpgradeBtn extends ImageButton {
    private final ElementManager elementManager = ElementManager.getInstance();

    public CannonUpgradeBtn() {
        this.setIcon(GameLoader.imgMap.get("cannon.upgrade"));
        this.setWidth(getIcon().getIconWidth());
        this.setHeight(getIcon().getIconHeight());

        this.setX(425);
        this.setY(415);
    }

    @Override
    public void onClick() {
        for (GameElement cannonTower : elementManager.getElementsByType(GameElementType.PLAYER)) {
            if (cannonTower instanceof CannonTower) {
                CannonTower cannonElement = (CannonTower) cannonTower;
                if (cannonElement.getLevel() < CannonTower.MAX_LEVEL) {
                    cannonElement.setLevel(cannonElement.getLevel() + 1);
                    log.debug("Cannon upgraded to level {}", cannonElement.getLevel());
                } else {
                    log.debug("Cannon is already at maximum level.");
                }
            }
        }
    }

    @Override
    public void keyUpdated(Set<Integer> keySet) {
        if (keySet.contains(KeyEvent.VK_UP) || keySet.contains(KeyEvent.VK_W)) {
            onClick();
        }
    }

}
