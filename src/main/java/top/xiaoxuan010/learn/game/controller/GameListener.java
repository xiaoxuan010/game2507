package top.xiaoxuan010.learn.game.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedHashSet;
import java.util.Set;

import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;

public class GameListener implements KeyListener {
    private final ElementManager elementManager = ElementManager.getInstance();

    private Set<Integer> keySet = new LinkedHashSet<Integer>();

    @Override
    public void keyTyped(KeyEvent e) {
        // 不处理 keyTyped 事件
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println("Key Pressed: " + KeyEvent.getKeyText(keyCode));
        if (keySet.contains(keyCode)) {
            // 如果按键已经被按下，则不处理重复按下的事件
            return;
        } else {
            keySet.add(keyCode);
            elementManager.getElementsByType(GameElementType.PLAYER).forEach(player -> {
                player.keyClicked(keySet);
            });
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println("Key Released: " + KeyEvent.getKeyText(keyCode));
        if (keySet.contains(keyCode)) {
            keySet.remove(keyCode);
            elementManager.getElementsByType(GameElementType.PLAYER).forEach(player -> {
                player.keyClicked(keySet);
            });
        } else {
            // 如果按键没有被按下，则不处理松开的事件
            return;
        }
    }

}
