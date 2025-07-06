package top.xiaoxuan010.learn.game.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;

public class GameMouseListener implements MouseListener {
    private final ElementManager elementManager = ElementManager.getInstance();

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        elementManager.getElementsByType(GameElementType.PLAYER).forEach(player -> {
            player.mouseClicked(e.getX(), e.getY());
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
