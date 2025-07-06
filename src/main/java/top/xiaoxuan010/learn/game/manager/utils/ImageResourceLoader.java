package top.xiaoxuan010.learn.game.manager.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageResourceLoader {
    private static Map<String, BufferedImage> cache = new HashMap<>();

    /**
     * 加载图片资源
     * 
     * @param resourcePath 图片资源路径，可以是普通图片路径或 plist 切片格式（如 "path/to/image.png" 或
     *                     "path/to/plist:subImageName"）
     * @return 加载后的 BufferedImage 对象
     * @throws IOException 如果加载图片失败
     */
    public static BufferedImage load(String resourcePath) throws IOException {
        try {
            if (cache.containsKey(resourcePath))
            return cache.get(resourcePath);

        BufferedImage img;
        if (resourcePath.contains(":")) {
            // plist切片
            String[] parts = resourcePath.split(":");
            img = PlistImageUtil.loadPlistImage(parts[0], parts[1]);
        } else {
            // 普通图片
            img = ImageIO.read(ImageResourceLoader.class.getClassLoader().getResourceAsStream((resourcePath)));
        }
        cache.put(resourcePath, img);
        return img;
    } catch (Exception e) {
        throw new IOException("加载图片资源失败: " + resourcePath, e);
    }

    }
}