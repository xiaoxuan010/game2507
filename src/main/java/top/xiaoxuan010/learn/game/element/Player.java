package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Set;

import javax.swing.ImageIcon;

import lombok.Getter;
import lombok.Setter;
import top.xiaoxuan010.learn.game.manager.GameLoad;

@Getter
@Setter
public class Player extends GameElement {
    // 状态：移动速度
    private float speedX = 0;
    private float speedY = 0;

    // 状态：方向
    private GameElementDirection direction = GameElementDirection.UP;

    public Player(int x, int y, int width, int height, ImageIcon icon) {
        super(x, y, width, height, icon);

    }

    @Override
    public void showElement(Graphics g) {
        g.drawImage(getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }

    @Override
    public void keyClicked(Set<Integer> keySet) {
        int speedX = 0;
        int speedY = 0;
        direction = this.getDirection();

        for (Integer keyCode : keySet) {
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    speedX = -6;
                    direction = GameElementDirection.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    speedX = 6;
                    direction = GameElementDirection.RIGHT;
                    break;
                case KeyEvent.VK_UP:
                    speedY = -6;
                    direction = GameElementDirection.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    speedY = 6;
                    direction = GameElementDirection.DOWN;
                    break;
                default:
                    break;
            }
        }

        this.setSpeedX(speedX);
        this.setSpeedY(speedY);
        this.setDirection(direction);

        System.out.println("Player Status - X: " + this.getSpeedX() + ", Y: " + this.getSpeedY() + ", Direction: "
                + this.getDirection());
    }

    @Override
    public void move() {
        this.setX(Math.min(Math.max(0, this.getX() + (int) this.getSpeedX()), 720 - this.getWidth()));
        this.setY(Math.min(Math.max(0, this.getY() + (int) this.getSpeedY()), 480 - this.getHeight()));
    }

    @Override
    protected void updateImage() {
        this.setIcon(GameLoad.imgMap.get(direction));
    }

}
