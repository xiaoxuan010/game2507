package top.xiaoxuan010.learn.game.element;

import top.xiaoxuan010.learn.game.manager.GameLoader;

public class FishNet extends RotatableElement {
    private long createTime = System.currentTimeMillis();

    public FishNet(float centerX, float centerY, Integer netLevel, float direction) {
        this.setDirection(direction);
        this.setCenterPosition(centerX, centerY);
        this.setWidth(100);
        this.setHeight(100);
        this.setIcon(GameLoader.imgMap.get("fishnet.lv" + netLevel));
    }

    @Override
    public void update(long time) {
        if (time - createTime > 500) {
            this.setAlive(false);
        }
    }

}
