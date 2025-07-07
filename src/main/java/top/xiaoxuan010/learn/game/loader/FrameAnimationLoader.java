package top.xiaoxuan010.learn.game.loader;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import top.xiaoxuan010.learn.game.element.utils.FrameAnimation;
import top.xiaoxuan010.learn.game.manager.GameLoader;

public class FrameAnimationLoader {
    public static FrameAnimation load(String prefix) {
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
        return new FrameAnimation(frames, 5);
    }
}
