package top.xiaoxuan010.learn.game.element.utils;

import java.util.List;

import javax.swing.ImageIcon;

import lombok.Getter;

@Getter
public class FrameAnimation {
    private final List<ImageIcon> frames;
    private int currentFrame = 0;
    private int frameTick = 0;
    private boolean isFinished = false;
    private final int frameInterval;
    private final boolean isLoop;

    public FrameAnimation(List<ImageIcon> frames, int frameInterval, boolean loop) {
        this.frames = frames;
        this.frameInterval = frameInterval;
        this.isLoop = loop;
    }

    public ImageIcon getCurrentFrame() {
        return frames.get(currentFrame);
    }

    public void update() {
        if (frames.size() <= 1)
            return;
        frameTick++;
        if (frameTick >= frameInterval) {
            frameTick = 0;
            if (isLoop) {
                currentFrame = (currentFrame + 1) % frames.size();
            } else {
                if (currentFrame < frames.size() - 1) {
                    currentFrame++;
                } else {
                    isFinished = true;
                }
            }
        }
    }
}