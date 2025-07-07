package top.xiaoxuan010.learn.game.state.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.state.GameState;
import top.xiaoxuan010.learn.game.state.GameStateManager;

@Slf4j
public class GameOverState extends BaseGameState {
    private int finalScore = 0;
    
    public GameOverState(GameStateManager stateManager) {
        super(stateManager);
    }
    
    @Override
    public void enter() {
        log.info("Entering game over state");
        // 获取最终分数等信息
    }
    
    @Override
    public void exit() {
        log.info("Exiting game over state");
    }
    
    @Override
    public void update(long deltaTime) {
        // 游戏结束状态的更新逻辑
    }
    
    @Override
    public void render(Graphics g) {
        // 绘制游戏结束背景
        g.setColor(new Color(0, 0, 0, 150)); // 半透明黑色
        g.fillRect(0, 0, 800, 600);
        
        // 绘制游戏结束文本
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics titleFm = g.getFontMetrics();
        String gameOverText = "Game Over";
        int titleX = (800 - titleFm.stringWidth(gameOverText)) / 2;
        g.drawString(gameOverText, titleX, 200);
        
        // 绘制分数
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics scoreFm = g.getFontMetrics();
        String scoreText = "Final Score: " + finalScore;
        int scoreX = (800 - scoreFm.stringWidth(scoreText)) / 2;
        g.drawString(scoreText, scoreX, 280);
        
        // 绘制重新开始按钮
        g.setColor(Color.GREEN);
        g.fillRect(200, 350, 150, 50);
        g.setColor(Color.WHITE);
        String restartText = "Restart";
        int restartX = 200 + (150 - scoreFm.stringWidth(restartText)) / 2;
        g.drawString(restartText, restartX, 380);
        
        // 绘制返回主菜单按钮
        g.setColor(Color.BLUE);
        g.fillRect(450, 350, 150, 50);
        g.setColor(Color.WHITE);
        String menuText = "Main Menu";
        int menuX = 450 + (150 - scoreFm.stringWidth(menuText)) / 2;
        g.drawString(menuText, menuX, 380);
    }
    
    @Override
    public void handleInput() {
        // 处理重新开始和返回主菜单的输入
    }
    
    public void restartGame() {
        stateManager.setState(GameState.GAME_PLAYING);
    }
    
    public void returnToMenu() {
        stateManager.setState(GameState.MAIN_MENU);
    }
}