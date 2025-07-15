package top.xiaoxuan010.learn.game.state.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import lombok.extern.slf4j.Slf4j;
import top.xiaoxuan010.learn.game.manager.GameLoader;
import top.xiaoxuan010.learn.game.state.GameState;
import top.xiaoxuan010.learn.game.state.GameStateManager;
import top.xiaoxuan010.learn.game.utils.GraphicsUtils;

@Slf4j
public class MainMenuState extends BaseGameState {

    public MainMenuState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void enter() {
        log.info("Entering main menu state");
    }

    @Override
    public void exit() {
        log.info("Exiting main menu state");
    }

    @Override
    public void update(long deltaTime) {

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

        // 使用预加载的字体
        Font menuFont = GameLoader.fontMap.get("default");
        if (menuFont != null) {
            g.setFont(menuFont.deriveFont(72f));
        } else {
            g.setFont(new Font("微软雅黑", Font.BOLD, 36));
        }

        // 绘制游戏标题
        g.setColor(Color.WHITE);
        String title = "捕鱼达人";
        FontMetrics titleFm = g.getFontMetrics();
        int titleX = (800 - titleFm.stringWidth(title)) / 2;
        GraphicsUtils.drawStringWithOutline(g, title, titleX, 150, Color.WHITE, Color.BLACK);

        // 绘制关卡选择按钮
        drawLevelButtons(g);

        // 绘制退出按钮
        g.setColor(Color.GRAY);
        g.fillRect(300, 350, 200, 60);
        g.setColor(Color.WHITE);
        g.drawRect(300, 350, 200, 60);

        if (menuFont != null) {
            g.setFont(menuFont.deriveFont(20f));
        } else {
            g.setFont(new Font("微软雅黑", Font.BOLD, 20));
        }

        String exitText = "退出游戏";
        FontMetrics exitFm = g.getFontMetrics();
        int exitX = 300 + (200 - exitFm.stringWidth(exitText)) / 2;
        int exitY = 350 + (60 + exitFm.getHeight()) / 2 - exitFm.getDescent();
        g.drawString(exitText, exitX, exitY);
    }

    private void drawLevelButtons(Graphics g) {
        Font menuFont = GameLoader.fontMap.get("default");
        if (menuFont != null) {
            g.setFont(menuFont.deriveFont(18f));
        } else {
            g.setFont(new Font("微软雅黑", Font.BOLD, 18));
        }

        int buttonWidth = 120;
        int buttonHeight = 50;
        int buttonSpacing = 20;
        int totalWidth = 5 * buttonWidth + 4 * buttonSpacing;
        int startX = (800 - totalWidth) / 2;
        int buttonY = 250;

        for (int i = 0; i < 5; i++) {
            int buttonX = startX + i * (buttonWidth + buttonSpacing);

            // 绘制按钮背景
            g.setColor(new Color(0, 200, 83)); // 深绿色
            g.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
            g.setColor(Color.WHITE);
            g.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);

            // 绘制按钮文字
            String levelText = "Level" + (i + 1);
            FontMetrics fm = g.getFontMetrics();
            int textX = buttonX + (buttonWidth - fm.stringWidth(levelText)) / 2;
            int textY = buttonY + (buttonHeight + fm.getHeight()) / 2 - fm.getDescent();
            g.drawString(levelText, textX, textY);
        }
    }

    @Override
    public void handleInput() {
        // 输入处理在GameMainPanel中完成
    }

    @Override
    public void handleInput(int x, int y) {
        // 检查关卡按钮点击
        int buttonWidth = 120;
        int buttonHeight = 50;
        int buttonSpacing = 20;
        int totalWidth = 5 * buttonWidth + 4 * buttonSpacing;
        int startX = (800 - totalWidth) / 2;
        int buttonY = 250;

        for (int i = 0; i < 5; i++) {
            int buttonX = startX + i * (buttonWidth + buttonSpacing);
            if (x >= buttonX && x <= buttonX + buttonWidth && y >= buttonY && y <= buttonY + buttonHeight) {
                int level = i + 1;
                if (level <= 2) {
                    stateManager.setSelectedLevel(level);
                    log.info("Selected level: {}", level);
                    // 进入加载状态，传递关卡信息
                    if (GameLoader.isLoaded) {
                        stateManager.setState(GameState.GAME_PLAYING);
                    } else {
                        stateManager.setState(GameState.LOADING);
                    }
                } else {
                    log.info("Level {} is not implemented yet", level);
                }
                return;
            }
        }

        // 检查是否点击了退出按钮 (300, 350, 200, 60)
        if (x >= 300 && x <= 500 && y >= 350 && y <= 410) {
            System.exit(0);
        }
    }

    public int getSelectedLevel() {
        return stateManager.getSelectedLevel();
    }

    public void startGame() {
        // 当点击关卡按钮时，进入加载状态
        stateManager.setState(GameState.LOADING);
    }
}