package top.xiaoxuan010.learn.game.state.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.manager.GameLoader;
import top.xiaoxuan010.learn.game.manager.utils.GameStateDataManager;
import top.xiaoxuan010.learn.game.state.GameState;
import top.xiaoxuan010.learn.game.state.GameStateManager;
import top.xiaoxuan010.learn.game.utils.GraphicsUtils;

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
        this.finalScore = GameStateDataManager.getInstance().getCoins();
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
        // 绘制背景
        ImageIcon bg = GameLoader.imgMap.get("background.menu");
        if (bg != null) {
            g.drawImage(bg.getImage(), 0, 0, 800, 600, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, 800, 600);
        }
        
        // 绘制游戏结束文本
        g.setColor(Color.WHITE);
        Font menuFont = GameLoader.fontMap.get("default");
        g.setFont(menuFont != null ? menuFont.deriveFont(72f) : new Font("微软雅黑", Font.BOLD, 72));
        FontMetrics titleFm = g.getFontMetrics();
        String gameOverText = "游戏结束";
        int titleX = (800 - titleFm.stringWidth(gameOverText)) / 2;
        GraphicsUtils.drawStringWithOutline(g, gameOverText, titleX, 200, Color.WHITE, Color.BLACK);
        
        // 绘制分数
        g.setFont(menuFont != null ? menuFont.deriveFont(24f) : new Font("微软雅黑", Font.BOLD, 24));
        FontMetrics scoreFm = g.getFontMetrics();
        String scoreText = "最终得分：" + finalScore;
        int scoreX = (800 - scoreFm.stringWidth(scoreText)) / 2;
        GraphicsUtils.drawStringWithOutline(g, scoreText, scoreX, 280, Color.WHITE, Color.BLACK);
        
        // 绘制重新开始按钮
        g.setColor(new Color(0, 200, 83));
        g.fillRect(200, 350, 150, 50);
        g.setColor(Color.WHITE);
        String restartText = "重新开始";
        int restartX = 200 + (150 - scoreFm.stringWidth(restartText)) / 2;
        g.drawString(restartText, restartX, 382);
        
        // 绘制返回主菜单按钮
        g.setColor(Color.GRAY);
        g.fillRect(450, 350, 150, 50);
        g.setColor(Color.WHITE);
        String menuText = "返回主菜单";
        int menuX = 450 + (150 - scoreFm.stringWidth(menuText)) / 2;
        g.drawString(menuText, menuX, 382);
    }
    
    @Override
    public void handleInput(int x, int y) {
        // 检查是否点击了重新开始按钮 (200, 350, 150, 50)
        if (x >= 200 && x <= 350 && y >= 350 && y <= 400) {
            restartGame();
        }
        // 检查是否点击了返回主菜单按钮 (450, 350, 150, 50)
        else if (x >= 450 && x <= 600 && y >= 350 && y <= 400) {
            returnToMenu();
        }
    }
    
    public void restartGame() {
        stateManager.setState(GameState.GAME_PLAYING);
    }
    
    public void returnToMenu() {
        stateManager.setState(GameState.MAIN_MENU);
    }
}