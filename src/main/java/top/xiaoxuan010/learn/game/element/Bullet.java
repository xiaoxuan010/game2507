package top.xiaoxuan010.learn.game.element;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.manager.GameLoader;

@Slf4j
public class Bullet extends RotatableElement {
    private float targetX, targetY;
    private float speed;
    private boolean arrived = false;

    public Bullet(float x, float y, float targetX, float targetY, float speed, int bulletLevel) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.speed = speed;

        this.directionBias = (float) Math.PI / 2;
        this.setDirection((float) Math.atan2(targetY - y, targetX - x));

        this.setIcon(GameLoader.imgMap.get("bullet.lv" + bulletLevel));
        this.setWidth(getIcon().getIconWidth());
        this.setHeight(getIcon().getIconHeight());

        // 设置中心点坐标
        this.setCenterPosition(x, y);
    }

    /**
     * 创建一个居中于指定坐标的子弹对象
     */
    public static Bullet createCenteredBullet(float x, float y, float targetX, float targetY, float speed,
            int bulletLevel) {
        return new Bullet(x, y, targetX, targetY, speed, bulletLevel);
    }

    @Override
    protected void updatePosition() {
        if (arrived) {
            return;
        }

        // 计算到目标的距离向量
        float dx = targetX - centerX;
        float dy = targetY - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance <= speed) {
            // 到达目标
            setCenterPosition(targetX, targetY);
            arrived = true;
            log.debug("Bullet arrived at target ({}, {})", targetX, targetY);
        } else {
            // 继续移动
            float moveX = (dx / distance) * speed;
            float moveY = (dy / distance) * speed;
            setCenterPosition(centerX + moveX, centerY + moveY);
        }
    }

    public boolean isArrived() {
        return arrived;
    }
}
