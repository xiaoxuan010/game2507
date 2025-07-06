package top.xiaoxuan010.learn.game.element;

import java.util.List;

import javax.swing.ImageIcon;

import lombok.Getter;

@Getter
public class FrameAnimation {
    private final List<ImageIcon> frames;
    private int currentFrame = 0;
    private int frameTick = 0;
    private final int frameInterval;

    public FrameAnimation(List<ImageIcon> frames, int frameInterval) {
        this.frames = frames;
        this.frameInterval = frameInterval;
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
            currentFrame = (currentFrame + 1) % frames.size();
        }
    }
}