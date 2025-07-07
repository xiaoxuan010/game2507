package top.xiaoxuan010.learn.game.element;

import top.xiaoxuan010.learn.game.element.components.RotatableElement;
import top.xiaoxuan010.learn.game.element.utils.FrameAnimation;
import top.xiaoxuan010.learn.game.loader.FrameAnimationLoader;

public class Ripple extends RotatableElement {
    private FrameAnimation animation;

    public Ripple(int x, int y) {
        setCenterPosition(x, y);

        animation = FrameAnimationLoader.load("ripple", 2, false);
        if (!animation.getFrames().isEmpty()) {
            this.setIcon(animation.getCurrentFrame());
        }
    }

    @Override
    public void update() {
        animation.update();

        if (animation.isFinished()) {
            this.setAlive(false);
        }
        setIcon(animation.getCurrentFrame());
    }

}
