package com.ttt.util.user;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TemplateUtils {
    public static String readHtmlTemplate(String templatePath) {
        StringBuilder content = new StringBuilder();
        try {
            // 使用类路径资源加载
            ClassPathResource resource = new ClassPathResource(templatePath);
            InputStream inputStream = resource.getInputStream();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
