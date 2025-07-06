package top.xiaoxuan010.learn.game.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;

@Slf4j
public class GameMouseListener implements MouseListener {
    private final ElementManager elementManager = ElementManager.getInstance();

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        AtomicBoolean isHandled = new AtomicBoolean(false);
        elementManager.getElementsByType(GameElementType.UI).forEach(uiElement -> {
            isHandled.set(isHandled.get() | uiElement.mouseClicked(e.getX(), e.getY()));
        });
        if (isHandled.get()) {
            return; // 如果UI元素处理了点击事件，则不继续处理
        }
        elementManager.getElementsByType(GameElementType.PLAYER).forEach(player -> {
            isHandled.set(isHandled.get() | player.mouseClicked(e.getX(), e.getY()));
        });
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
