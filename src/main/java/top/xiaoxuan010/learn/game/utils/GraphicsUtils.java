package top.xiaoxuan010.learn.game.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GraphicsUtils {
    public static void drawStringWithOutline(Graphics g, String text, int x, int y, Color textColor,
            Color outlineColor) {
        Graphics2D g2d = (Graphics2D) g;
        // 开启抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 绘制描边
        g2d.setColor(outlineColor);
        g2d.drawString(text, x - 1, y - 1);
        g2d.drawString(text, x - 1, y + 1);
        g2d.drawString(text, x + 1, y - 1);
        g2d.drawString(text, x + 1, y + 1);

        // 绘制文本
        g2d.setColor(textColor);
        g2d.drawString(text, x, y);
    }
}
