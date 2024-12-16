package com.zht.testjavafx2.openapi;

import com.zht.testjavafx2.func.OpenApiFuncProcessor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtil {
    /**
     * 获取指定文件的 InputStream。
     *
     * @param filePath 文件的完整路径
     * @return 文件的 InputStream，如果文件不存在或无法打开，则返回 null。
     */
    public static InputStream getInputStreamForFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            return Files.newInputStream(path, StandardOpenOption.READ);
        } catch (IOException e) {
            System.err.println("Error opening InputStream for file: " + e.getMessage());
            return null;
        }
    }

    public static File copyResourceToFileIfNotExists(String targetFilePath) throws IOException {
        // 加载资源为输入流
        InputStream resource = OpenApiFuncProcessor.class.getResourceAsStream("/com/zht/testjavafx2/appregister.xml");
        try (resource) {
            // 创建目标文件路径
            Path targetPath = Paths.get(targetFilePath);
            Files.createDirectories(targetPath.getParent()); // 确保父目录存在

            // 如果目标文件不存在，则复制资源文件到目标位置
            if (!Files.exists(targetPath)) {
                try (OutputStream outputStream = new FileOutputStream(targetPath.toFile())) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = resource.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
            return targetPath.toFile();
        }
    }
}
