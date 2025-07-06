package top.xiaoxuan010.learn.game.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RotatableElement extends GameElement {
    protected float direction = 0;
    protected float directionBias = 0;

    // 逻辑中心点坐标
    protected float centerX, centerY;

    public void setCenterPosition(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        // 同步更新左上角坐标（用于兼容父类）
        this.x = (int) (centerX - width / 2.0f);
        this.y = (int) (centerY - height / 2.0f);
    }

    /**
     * 获取旋转中心X坐标，子类可以重写此方法来自定义旋转中心
     * 
     * @return 旋转中心X坐标
     */
    protected float getRotationCenterX() {
        return centerX;
    }

    /**
     * 获取旋转中心Y坐标，子类可以重写此方法来自定义旋转中心
     * 
     * @return 旋转中心Y坐标
     */
    protected float getRotationCenterY() {
        return centerY;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Image img = this.getIcon().getImage();

        // 使用仿射变换进行旋转绘制
        AffineTransform oldTransform = g2d.getTransform();

        // 获取旋转中心点
        float rotationCenterX = getRotationCenterX();
        float rotationCenterY = getRotationCenterY();

        // 先平移到旋转中心点，再旋转，最后平移回去
        g2d.translate(rotationCenterX, rotationCenterY);
        g2d.rotate(direction + directionBias);
        g2d.translate(-width / 2.0, -height / 2.0);

        g2d.drawImage(img, 0, 0, width, height, null);

        // 恢复变换
        g2d.setTransform(oldTransform);

        // 调试绘制
        // drawDebugInfo(g2d);

        g2d.dispose();
    }

    // private void drawDebugInfo(Graphics2D g2d) {
    // // 获取旋转中心点
    // float rotationCenterX = getRotationCenterX();
    // float rotationCenterY = getRotationCenterY();

    // // 绘制旋转后的边界框
    // g2d.setColor(Color.RED);
    // AffineTransform transform = new AffineTransform();
    // transform.translate(rotationCenterX, rotationCenterY);
    // transform.rotate(direction + directionBias);
    // transform.translate(-width / 2.0, -height / 2.0);

    // Rectangle2D rect = new Rectangle2D.Double(0, 0, width, height);
    // Shape rotatedRect = transform.createTransformedShape(rect);
    // g2d.draw(rotatedRect);

    // // 绘制旋转中心点
    // g2d.setColor(Color.BLUE);
    // g2d.fillOval((int) rotationCenterX - 2, (int) rotationCenterY - 2, 4, 4);

    // // 绘制方向线
    // g2d.setColor(Color.GREEN);
    // int lineLength = Math.max(width, height) / 2 + 10;
    // int endX = (int) (rotationCenterX + Math.cos(direction) * lineLength);
    // int endY = (int) (rotationCenterY + Math.sin(direction) * lineLength);
    // g2d.drawLine((int) rotationCenterX, (int) rotationCenterY, endX, endY);
    // }

    @Override
    public Rectangle getRectangle() {
        // 返回未旋转的边界框用于简单碰撞检测
        return new Rectangle(
                (int) (centerX - width / 2),
                (int) (centerY - height / 2),
                width,
                height);
    }

    /**
     * 获取旋转后的精确边界框（用于精确碰撞检测）
     */
    public Rectangle2D getRotatedBounds() {
        // 获取旋转中心点
        float rotationCenterX = getRotationCenterX();
        float rotationCenterY = getRotationCenterY();

        AffineTransform transform = new AffineTransform();
        transform.translate(rotationCenterX, rotationCenterY);
        transform.rotate(direction + directionBias);
        transform.translate(-width / 2.0, -height / 2.0);

        Rectangle2D rect = new Rectangle2D.Double(0, 0, width, height);
        return transform.createTransformedShape(rect).getBounds2D();
    }
}