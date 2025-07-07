package top.xiaoxuan010.learn.game.element;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.element.components.RotatableElement;
import top.xiaoxuan010.learn.game.element.utils.FrameAnimation;
import top.xiaoxuan010.learn.game.loader.FrameAnimationLoader;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;

@Slf4j
@Getter
public class Bullet extends RotatableElement {
    private final ElementManager elementManager = ElementManager.getInstance();

    private float targetX, targetY;
    private float speed;
    private boolean arrived = false;
    private FrameAnimation animation;
    private int bulletLevel;

    public Bullet(float x, float y, float targetX, float targetY, float speed, int bulletLevel) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.speed = speed;
        this.bulletLevel = bulletLevel;

        this.directionBias = (float) Math.PI / 2;
        this.setDirection((float) Math.atan2(targetY - y, targetX - x));

        // 加载帧动画
        animation = FrameAnimationLoader.load("bullet.lv" + bulletLevel, true);

        if (!animation.getFrames().isEmpty()) {
            this.setIcon(animation.getCurrentFrame());
        }

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
    public void update() {
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
            setAlive(false);
            onArrive();
            arrived = true;
            log.debug("Bullet arrived at target ({}, {})", targetX, targetY);
        } else {
            // 继续移动
            float moveX = (dx / distance) * speed;
            float moveY = (dy / distance) * speed;
            setCenterPosition(centerX + moveX, centerY + moveY);
        }

        // 更新动画帧
        if (animation != null) {
            animation.update();
            setIcon(animation.getCurrentFrame());
        }
    }

    private void onArrive() {
        FishNet fishNet = new FishNet(centerX, centerY, getBulletLevel(), getDirection());
        elementManager.addElement(fishNet, GameElementType.BULLET);
    }
}
