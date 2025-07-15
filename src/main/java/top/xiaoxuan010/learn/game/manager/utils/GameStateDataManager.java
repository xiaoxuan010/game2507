package top.xiaoxuan010.learn.game.manager.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class GameStateDataManager {
    private static GameStateDataManager instance = null;

    private long gameStartTime; // 游戏开始时间
    private long currentTime; // 当前时间
    private int gameLevel; // 当前游戏关卡
    private int coins; // 金币数量
    private int score; // 分数
    private int level; // 当前关卡
    private int gameDuration; // 游戏时长（秒）

    private GameStateDataManager() {
        log.info("Initializing GameStateDataManager");
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

    public void update(long time) {
        setCurrentTime(time);
    }

    public int getGameCountdown() {
        return Math.max(0, (int) (((gameStartTime + gameDuration * 1000) - System.currentTimeMillis()) / 1000));
    }

}