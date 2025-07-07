package top.xiaoxuan010.learn.game.manager.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.PropertyListParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlistImageUtil {
    /**
     * 加载 plist 切片内的子图片
     * 
     * @param pImgPath plist 图片路径（注意不是 plist 文件路径）
     * @param cImgName 子图片名称（如 "fire.png"）
     * @return 裁剪后的 BufferedImage
     * @throws IOException 如果加载图片失败
     */
    public static BufferedImage loadPlistImage(String pImgPath, String cImgName) throws IOException {
        BufferedImage parentImg;

        // 读取 plist 图片
        parentImg = ImageResourceLoader.load(pImgPath);
        if (parentImg == null) {
            throw new RuntimeException("父图片 " + pImgPath + " 未找到");
        }

        // 构建 plist 文件路径
        String plistPath = pImgPath.replace(".png", ".plist").replace(".jpg", ".plist");

        try {
            // 解析 plist 文件
            NSDictionary plist = (NSDictionary) PropertyListParser.parse(
                    PlistImageUtil.class.getClassLoader().getResourceAsStream(
                            plistPath));
            NSDictionary frames = (NSDictionary) plist.get("frames");

            if (frames == null || !frames.containsKey(cImgName)) {
                throw new IOException(
                        "子图片 " + cImgName + " 在 plist " + plistPath + " 中未找到");
            }

            // 获取指定图像的帧信息
            NSDictionary frameInfo = (NSDictionary) frames.get(cImgName);

            // 解析新格式的坐标和尺寸
            int x = ((NSNumber) frameInfo.get("x")).intValue();
            int y = ((NSNumber) frameInfo.get("y")).intValue();
            int width = ((NSNumber) frameInfo.get("width")).intValue();
            int height = ((NSNumber) frameInfo.get("height")).intValue();

            int originalWidth = ((NSNumber) frameInfo.get("originalWidth")).intValue();
            int originalHeight = ((NSNumber) frameInfo.get("originalHeight")).intValue();

            double offsetX = ((NSNumber) frameInfo.get("offsetX")).doubleValue();
            double offsetY = ((NSNumber) frameInfo.get("offsetY")).doubleValue();

            // 从原图中裁剪子图像
            BufferedImage croppedImage = parentImg.getSubimage(x, y, width, height);

            // 创建最终图像
            BufferedImage finalImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_ARGB);

            // 计算绘制位置
            int drawX = (int) ((originalWidth - width) / 2 + offsetX);
            int drawY = (int) ((originalHeight - height) / 2 - offsetY);

            finalImage.getGraphics().drawImage(croppedImage, drawX, drawY, null);

            return finalImage;
        } catch (Exception e) {
            throw new IOException("解析 plist 文件或处理图片时出错: " + e.getMessage(), e);
        }
    }
}
