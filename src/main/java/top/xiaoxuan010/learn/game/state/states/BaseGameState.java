package top.xiaoxuan010.learn.game.state.states;

import java.awt.Graphics;

import top.xiaoxuan010.learn.game.state.GameStateManager;

public abstract class BaseGameState {
    protected GameStateManager stateManager;

    public BaseGameState(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public abstract void enter();

    public abstract void exit();

    public abstract void update(long deltaTime);

    public abstract void render(Graphics g);

    public void handleInput() {

    }

    // 新增带坐标参数的handleInput方法
    public void handleInput(int x, int y) {
        // 默认实现，子类可以重写此方法来处理带坐标的输入
        handleInput();
    }
}