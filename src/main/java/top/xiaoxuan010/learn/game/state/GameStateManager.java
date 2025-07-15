package top.xiaoxuan010.learn.game.state;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.state.states.BaseGameState;
import top.xiaoxuan010.learn.game.state.states.GameOverState;
import top.xiaoxuan010.learn.game.state.states.GamePlayingState;
import top.xiaoxuan010.learn.game.state.states.LoadingState;
import top.xiaoxuan010.learn.game.state.states.MainMenuState;

@Slf4j
@Getter
@Setter
public class GameStateManager {
    private static GameStateManager instance;
    private GameState currentState;
    private BaseGameState currentStateHandler;
    private int selectedLevel = 1; // 当前选择的关卡，默认为1

    private LoadingState loadingState;
    private MainMenuState mainMenuState;
    private GamePlayingState gamePlayingState;
    private GameOverState gameOverState;

    private GameStateManager() {
        initializeStates();
        setState(GameState.MAIN_MENU);
    }

    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    private void initializeStates() {
        loadingState = new LoadingState(this);
        mainMenuState = new MainMenuState(this);
        gamePlayingState = new GamePlayingState(this);
        gameOverState = new GameOverState(this);
    }

    public void setState(GameState newState) {
        if (currentStateHandler != null) {
            currentStateHandler.exit();
        }

        this.currentState = newState;

        switch (newState) {
            case LOADING:
                currentStateHandler = loadingState;
                break;
            case MAIN_MENU:
                currentStateHandler = mainMenuState;
                break;
            case GAME_PLAYING:
                currentStateHandler = gamePlayingState;
                break;
            case GAME_OVER:
                currentStateHandler = gameOverState;
                break;
        }

        if (currentStateHandler != null) {
            currentStateHandler.enter();
        }

        log.info("Game state changed to: {}", newState);
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public BaseGameState getCurrentStateHandler() {
        return currentStateHandler;
    }

    public void update(long time) {
        if (currentStateHandler != null) {
            currentStateHandler.update(time);
        }
        else {
            log.warn("Current state handler is not initialized. Cannot update game state.");
        }
    }
}