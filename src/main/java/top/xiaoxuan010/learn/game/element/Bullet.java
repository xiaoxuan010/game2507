package top.xiaoxuan010.learn.game.element;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Bullet extends GameElement {
    @SuppressWarnings("unused")
    private int ATK;
    private int speed;
    private GameElementDirection direction;

    public Bullet(int x, int y, int width, int height, ImageIcon icon, int ATK, int speed,
            GameElementDirection direction) {
        super(x, y, width, height, icon);
        this.ATK = ATK;
        this.speed = speed;
        this.direction = direction;
    }

    public Bullet(int x, int y, int width, int height, ImageIcon icon, GameElementDirection direction) {
        this(x, y, width, height, icon, 1, 10, direction);
    }

    public Bullet(int x, int y, GameElementDirection direction) {
        this(x, y, 10, 10, null, direction);
    }

    /**
     * 创建一个居中于指定坐标的子弹对象。
     * 子弹的坐标将根据方向进行调整，使其在指定位置居中
     * 
     * @param x         中心点X坐标
     * @param y         中心点Y坐标
     * @param direction 子弹的方向
     * @return 子弹对象
     */
    public static Bullet createCenteredBullet(int x, int y, GameElementDirection direction) {
        Bullet bullet = new Bullet(x, y, direction);
        switch (direction) {
            case GameElementDirection.LEFT:
                bullet.setX(x - bullet.getWidth());
                bullet.setY(y - bullet.getHeight() / 2);
                break;
            case GameElementDirection.RIGHT:
                bullet.setY(y - bullet.getHeight() / 2);
                break;
            case GameElementDirection.UP:
                bullet.setX(x - bullet.getWidth() / 2);
                bullet.setY(y - bullet.getHeight());
                break;
            case GameElementDirection.DOWN:
                bullet.setX(x - bullet.getWidth() / 2);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
        return bullet;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(this.getX(), this.getY(), this.getWidth(), this.getWidth());
    }

    @Override
    protected void updatePosition() {
        if (this.getX() < 0 || this.getX() > 720 || this.getY() < 0 || this.getY() > 480) {
            this.setAlive(false);
            return;
        }
        switch (direction) {
            case GameElementDirection.LEFT:
                this.setX(this.getX() - speed);
                break;
            case GameElementDirection.RIGHT:
                this.setX(this.getX() + speed);
                break;
            case GameElementDirection.UP:
                this.setY(this.getY() - speed);
                break;
            case GameElementDirection.DOWN:
                this.setY(this.getY() + speed);
                break;
            default:
                throw new IllegalStateException("Unexpected direction: " + direction);
        }
    }

    @Override
    public void onCollision(GameElement other) {
        this.setAlive(false);
    }

}
