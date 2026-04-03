package com.ttt.util.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageUtil {
    public static String encodeImageToBase64(String imagePath) {
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            System.err.println("图片转换失败: " + e.getMessage());
            return "";
        }
    }
    public static String decodeImageFromBase64(String imagePath) {
        String mimeType=getMimeTypeFromExtension(imagePath);
        if (mimeType==null) {
            return null;
        }
        String image="data:"+mimeType+";base64,"+encodeImageToBase64(imagePath);
        return image;
    }
    //通过图片地址生成对应base64然后生成对应content
    public static String getImageContentByPathBase(String imagePath) {

        String image=decodeImageFromBase64(imagePath);
        String result= """
                {
                "name":"用户上传的菜品图片",
                "image":"%s"
                }
                """.formatted(image);
        return result;
    }
    //通过图片链接生成对应content
    public static String getImageContentByPath(String imagePath) {
        String result= """
                 {
                "name":"用户上传的菜品图片",
                "image":"%s"
                }
                """.formatted(imagePath);
        return result;
    }

    // 根据文件扩展名确定MIME类型
    private static String getMimeTypeFromExtension(String filePath) {
        // 获取文件扩展名（小写处理）
        int lastIndex = filePath.lastIndexOf('.');
        if (lastIndex == -1) return null;

        String extension = filePath.substring(lastIndex + 1).toLowerCase();

        // 根据扩展名映射MIME类型
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            case "bmp" -> "image/bmp";
            default -> null; // 未知格式
        };
    }
}
