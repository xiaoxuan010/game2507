package top.xiaoxuan010.learn.game.element.utils;

import lombok.Getter;
import lombok.Setter;
import top.xiaoxuan010.learn.game.element.components.GameElement;

@Getter
@Setter
public class GameStateDataManager extends GameElement {
    private static GameStateDataManager instance = null;

    private long gameStartTime; // 游戏开始时间
    private long currentTime; // 当前时间
    private int coins; // 金币数量
    private int score; // 分数
    private int level; // 当前关卡

    private GameStateDataManager() {
        reset();
    }

    public static synchronized GameStateDataManager getInstance() {
        if (instance == null) {
            instance = new GameStateDataManager();
        }
        return instance;
    }

    public void reset() {
        gameStartTime = System.currentTimeMillis();
        coins = 100; // 初始金币设置为100
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
        return Math.max(0, (int) (((gameStartTime + 30000) - System.currentTimeMillis()) / 1000));
    }

}