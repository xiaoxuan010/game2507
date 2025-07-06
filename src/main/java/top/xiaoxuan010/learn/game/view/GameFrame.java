package top.xiaoxuan010.learn.game.view;

import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class GameFrame extends JFrame {
    public static int GameX = 800;
    public static int GameY = 500;

    private JPanel gamePanel = null;
    private KeyListener keyListener = null;
    private Thread gameThread = null;

    public void setGamePanel(JPanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public GameFrame() {
        init();
    }

    public void init() {
        this.setTitle("捕鱼达人");

        this.setSize(GameX, GameY);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 将窗口居中显示
        this.setLocationRelativeTo(null);

        // 设置窗口不可调整大小
        this.setResizable(false);
    }

    public void start() {
        if (gamePanel != null) {
            this.add(gamePanel);
        }
        if (keyListener != null) {
            this.addKeyListener(keyListener);
        }
        if (gameThread != null) {
            gameThread.start();
        }
        this.setVisible(true);
        if (this.gamePanel instanceof Runnable) {
            gameThread = new Thread((Runnable) this.gamePanel);
            gameThread.start();
        } else {
            log.warn("GamePanel {} does not implement Runnable, cannot start game thread.",
                    gamePanel.getClass().getName());
        }
    }
}
