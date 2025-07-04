package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Set;

import javax.swing.ImageIcon;

import lombok.Getter;
import lombok.Setter;
import top.xiaoxuan010.learn.game.manager.ElementManager;
import top.xiaoxuan010.learn.game.manager.GameElementType;
import top.xiaoxuan010.learn.game.manager.GameLoad;

@Getter
@Setter
public class Player extends GameElement {
    // 状态：移动速度
    private float speedX = 0;
    private float speedY = 0;

    // 状态：方向
    private GameElementDirection direction = GameElementDirection.UP;

    // 状态：是否开火
    private boolean isFiring = false;

    public Player(int x, int y, int width, int height, ImageIcon icon) {
        super(x, y, width, height, icon);

    }

    @Override
    public void showElement(Graphics g) {
        g.drawImage(getIcon().getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }

    @Override
    public void keyUpdated(Set<Integer> keySet) {
        this.setSpeedX(0);
        this.setSpeedY(0);
        this.setFiring(false);

        for (Integer keyCode : keySet) {
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    this.setSpeedX(-6);
                    direction = GameElementDirection.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    this.setSpeedX(6);
                    direction = GameElementDirection.RIGHT;
                    break;
                case KeyEvent.VK_UP:
                    this.setSpeedY(-6);
                    direction = GameElementDirection.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    this.setSpeedY(6);
                    direction = GameElementDirection.DOWN;
                    break;
                case KeyEvent.VK_SPACE:
                    this.setFiring(true);
                    break;
            }
        }

        System.out.println("Player Status - X: " + this.getSpeedX() + ", Y: " + this.getSpeedY() + ", Direction: "
                + this.getDirection() + ", Firing: " + this.isFiring());
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

    @Override
    protected void add() {
        if (!this.isFiring()) {
            return;
        } else {
            this.setFiring(false);
        }

        int bulletX, bulletY;
        switch (direction) {
            case GameElementDirection.LEFT:
                bulletX = getX();
                bulletY = getY() + getHeight() / 2;
                break;
            case GameElementDirection.RIGHT:
                bulletX = getX() + getWidth();
                bulletY = getY() + getHeight() / 2;
                break;
            case GameElementDirection.UP:
                bulletX = getX() + getWidth() / 2;
                bulletY = getY();
                break;
            case GameElementDirection.DOWN:
                bulletX = getX() + getWidth() / 2;
                bulletY = getY() + getHeight();
                break;
            default:
                throw new IllegalStateException("Unexpected direction: " + direction);
        }

        GameElement element = Bullet.createCenteredBullet(bulletX, bulletY, direction);

        ElementManager.getInstance().addElement(element, GameElementType.BULLET);
        System.out.println("Player fired a bullet at (" + bulletX + ", " + bulletY + ") in direction " + direction);

    }

}
