package top.xiaoxuan010.learn.game.show;

import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import lombok.Setter;

@Setter
public class GameFrame extends JFrame {
    public static int GameX = 720;
    public static int GameY = 480;

    private JPanel gamePanel = null;
    private KeyListener keyListener = null;
    // private MouseMotionListener mouseMotionListener = null;
    // private MouseListener mouseListener = null;
    private Thread gameThread = null;

    public void setGamePanel(JPanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public GameFrame() {
        init();
    }

    public void init() {
        this.setTitle("Game Frame");

        this.setSize(GameX, GameY);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 将窗口居中显示
        this.setLocationRelativeTo(null);

        // 设置窗口不可调整大小
        this.setResizable(false);
    }

    public void addButton() {

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
    }
}
