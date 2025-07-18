package top.xiaoxuan010.learn.game.element;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.element.components.RotatableElement;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;
import top.xiaoxuan010.learn.game.manager.GameLoader;
import top.xiaoxuan010.learn.game.manager.utils.GameStateDataManager;

@Slf4j
@Setter
@Getter
public class CannonTower extends RotatableElement {
    public static final int MAX_LEVEL = 11;

    private final ElementManager elementManager = ElementManager.getInstance();

    private int level = 1;
    private boolean isFiring = false;
    private long lastFireTime = 0;
    private final long fireDuration = 100; // 动画持续时间，单位：毫秒

    public CannonTower() {
        this.setWidth(80);
        this.setHeight(80);

        this.setCenterPosition(385, 400);

        this.setDirectionBias((float) Math.PI / 2);

        this.updateIcon();
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();
        if (isFiring && (currentTime - lastFireTime) >= fireDuration) {
            setFiring(false);
        }
        updateIcon();
    }

    private void updateIcon() {
        this.setIcon(GameLoader.imgMap.get("cannon.tower.lv" + level + (isFiring ? ".fire" : ".normal")));
    }

    @Override
    public void mouseMotionUpdated(int x, int y) {
        float newDirection = (float) Math.atan2(y - getRotationCenterY(), x - getRotationCenterX());
        this.setDirection(newDirection);

        log.trace("Cannon direction updated to {} radians, mouse at ({}, {})", newDirection, x, y);
    }

    @Override
    public boolean mouseClicked(int x, int y) {
        mouseMotionUpdated(x, y);
        fire(x, y);
        return true;
    }

    private void fire(int x, int y) {
        // 检查金币是否足够（按炮台等级消耗金币）
        GameStateDataManager gameState = GameStateDataManager.getInstance();
        int cost = level; // 炮台等级即为消耗的金币数量
        
        if (!gameState.spendCoins(cost)) {
            log.info("Insufficient coins! Need {} coins, current coins: {}", cost, gameState.getCoins());
            return; // 金币不足，无法发射
        }
        
        // 计算炮口位置
        setFiring(true); // 开始射击动画
        lastFireTime = System.currentTimeMillis();
        MuzzlePosition muzzlePosition = calculateMuzzlePosition();
        float muzzleX = getRotationCenterX() + muzzlePosition.x;
        float muzzleY = getRotationCenterY() + muzzlePosition.y;

        // 创建子弹
        float speed = 10f;
        Bullet bullet = Bullet.createCenteredBullet(muzzleX, muzzleY, x, y, speed, level);
        elementManager.addElement(bullet, GameElementType.BULLET);

        // 创建涟漪
        Ripple ripple = new Ripple(x, y);
        elementManager.addElement(ripple, GameElementType.UI);

        log.debug("Fire bullet from ({}, {}) to ({}, {}) with speed {}, cost {} coins", 
                 muzzleX, muzzleY, x, y, speed, cost);
    }

    private MuzzlePosition calculateMuzzlePosition() {
        // 炮管长度（从中心到炮口的距离）
        float barrelLength = getHeight() * 0.4f;

        // 计算炮口位置
        float actualDirection = direction;
        float offsetX = barrelLength * (float) Math.cos(actualDirection);
        float offsetY = barrelLength * (float) Math.sin(actualDirection);

        return new MuzzlePosition(offsetX, offsetY);
    }

    @Data
    private static class MuzzlePosition {
        final float x, y;
    }

    @Override
    protected float getRotationCenterY() {
        return centerY + getHeight() / 2.0f;
    }

}
