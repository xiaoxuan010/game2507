package top.xiaoxuan010.learn.game.test;

import top.xiaoxuan010.learn.game.element.Fish;

/**
 * 测试鱼类游动方向
 */
public class FishDirectionTest {
    public static void main(String[] args) {
        // 测试从左边生成的鱼
        Fish leftFish = new Fish(-100, 200, 80, 60, 1, "fish01");
        leftFish.setMovingDirection(true); // 从左边生成
        
        System.out.println("从左边生成的鱼:");
        System.out.println("初始位置: (" + leftFish.getX() + ", " + leftFish.getY() + ")");
        
        // 模拟几次移动
        for (int i = 0; i < 5; i++) {
            leftFish.updateMovement();
            System.out.println("移动后位置: (" + leftFish.getX() + ", " + leftFish.getY() + ")");
        }
        
        System.out.println();
        
        // 测试从右边生成的鱼
        Fish rightFish = new Fish(900, 200, 80, 60, 1, "fish01");
        rightFish.setMovingDirection(false); // 从右边生成
        
        System.out.println("从右边生成的鱼:");
        System.out.println("初始位置: (" + rightFish.getX() + ", " + rightFish.getY() + ")");
        
        // 模拟几次移动
        for (int i = 0; i < 5; i++) {
            rightFish.updateMovement();
            System.out.println("移动后位置: (" + rightFish.getX() + ", " + rightFish.getY() + ")");
        }
    }
}
