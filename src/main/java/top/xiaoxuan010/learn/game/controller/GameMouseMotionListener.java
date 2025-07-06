package top.xiaoxuan010.learn.game.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;

@Slf4j
public class GameMouseMotionListener implements MouseMotionListener {
    private final ElementManager elementManager = ElementManager.getInstance();

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        elementManager.getElementsByType(GameElementType.PLAYER).forEach(player -> {
            player.mouseMotionUpdated(e.getX(), e.getY());
        });
    }

}
