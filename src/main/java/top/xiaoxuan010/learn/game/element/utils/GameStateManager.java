package top.xiaoxuan010.learn.game.element.utils;

import lombok.Getter;
import lombok.Setter;
import top.xiaoxuan010.learn.game.element.components.GameElement;

@Getter
@Setter
public class GameStateManager extends GameElement {
    private static GameStateManager instance = null;

    private long gameStartTime; // 游戏开始时间
    private long currentTime; // 当前时间
    private int coins; // 金币数量
    private int score; // 分数
    private int level; // 当前关卡

    private GameStateManager() {
        reset();
    }

    public static synchronized GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    public void reset() {
        gameStartTime = System.currentTimeMillis();
        coins = 0;
        score = 0;
        level = 1;
    }

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    @Override
    public void update(long time) {
        setCurrentTime(time);
    }

    public int getGameCountdown() {
        // 假设游戏时间为30秒
        return (int) (((gameStartTime + 30000) - System.currentTimeMillis()) / 1000);
    }

}