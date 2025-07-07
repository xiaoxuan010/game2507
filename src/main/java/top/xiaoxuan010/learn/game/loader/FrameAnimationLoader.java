package top.xiaoxuan010.learn.game.loader;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import top.xiaoxuan010.learn.game.element.utils.FrameAnimation;
import top.xiaoxuan010.learn.game.manager.GameLoader;

public class FrameAnimationLoader {
    private static List<ImageIcon> loadFrames(String prefix) {
        List<ImageIcon> frames = new ArrayList<>();
        int idx = 0;
        while (true) {
            ImageIcon icon = GameLoader.imgMap.get(prefix + "[" + idx + "]");
            if (icon == null)
                break;
            frames.add(icon);
            idx++;
        }
        if (frames.isEmpty()) {
            ImageIcon icon = GameLoader.imgMap.get(prefix);
            if (icon != null)
                frames.add(icon);
        }
        return frames;
    }

    public static FrameAnimation load(String prefix, boolean loop) {
        return new FrameAnimation(loadFrames(prefix), 5, loop);
    }

    public static FrameAnimation load(String prefix, int frameInterval, boolean loop) {
        return new FrameAnimation(loadFrames(prefix), frameInterval, loop);
    }
}
