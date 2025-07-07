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

    public abstract void handleInput();
}