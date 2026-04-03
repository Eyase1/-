package com.graduatedesign.knowledgeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduatedesign.commonmodule.dto.ApiResponse;
import com.graduatedesign.knowledgeservice.client.CozeServiceClient;
import com.graduatedesign.knowledgeservice.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.graduatedesign.knowledgeservice.dto.DocumentQaRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/knowledge/file")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private CozeServiceClient cozeServiceClient;

    /**
     * 上传文件
     * @param file 上传的文件
     * @return 文件信息
     */
    @PostMapping("/upload")
    public ApiResponse<Map<String, Object>> uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // 检查文件参数是否存在
            if (file == null) {
                log.warn("文件上传请求中未找到file参数");
                return ApiResponse.badRequest("请选择要上传的文件，参数名必须为'file'");
            }

            log.info("收到文件上传请求: 文件名={}, 大小={} bytes, ContentType={}",
                    file.getOriginalFilename(), file.getSize(), file.getContentType());

            // 验证文件
            if (file.isEmpty()) {
                log.warn("上传的文件为空");
                return ApiResponse.badRequest("文件不能为空，请选择一个有效的文件");
            }

            // 验证文件类型
            String originalFilename = file.getOriginalFilename();
            if (!fileUploadService.isAllowedFileType(originalFilename, file.getContentType())) {
                log.warn("不支持的文件类型: 文件名={}, ContentType={}", originalFilename, file.getContentType());
                return ApiResponse.badRequest("不支持的文件类型。支持的文件类型：文本文件(.txt, .md, .json等)、PDF文件(.pdf)、Word文件(.doc, .docx)");
            }

            // 上传文件
            Map<String, Object> fileInfo = fileUploadService.uploadFile(file);

            log.info("文件上传成功: {}", fileInfo);
            return ApiResponse.success(fileInfo);

        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ApiResponse.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件列表
     * @return 文件列表
     */
    @GetMapping("/list")
    public ApiResponse<Map<String, Object>> getFileList() {
        try {
            Map<String, Object> fileList = fileUploadService.getFileList();
            return ApiResponse.success(fileList);
        } catch (Exception e) {
            log.error("获取文件列表失败", e);
            return ApiResponse.error("获取文件列表失败: " + e.getMessage());
        }
    }

    /**
     * 下载文件
     * @param fileName 文件名
     * @return 文件
     */
    @GetMapping("/download/{fileName:.+}")
    public org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> downloadFile(
            @PathVariable String fileName) {
        try {
            org.springframework.core.io.Resource resource = fileUploadService.getFileAsResource(fileName);
            if (resource.exists() && resource.isReadable()) {
                return org.springframework.http.ResponseEntity.ok()
                        .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return org.springframework.http.ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("下载文件失败: {}", fileName, e);
            return org.springframework.http.ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取文件内容（支持文本、PDF、Word文件）
     * @param fileName 文件名
     * @return 文件内容
     */
    @GetMapping("/content/{fileName:.+}")
    public ApiResponse<Map<String, Object>> getFileContent(@PathVariable String fileName) {
        try {
            // URL解码文件名（处理中文和特殊字符）
            String decodedFileName = java.net.URLDecoder.decode(fileName, "UTF-8");
            log.info("获取文件内容: 原始文件名={}, 解码后文件名={}", fileName, decodedFileName);

            // 检查文件是否存在
            if (!fileUploadService.fileExists(decodedFileName)) {
                log.warn("文件不存在: {}", decodedFileName);
                return ApiResponse.notFound("文件不存在: " + decodedFileName);
            }

            // 读取文件内容
            String content = fileUploadService.readFileContent(decodedFileName);

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("fileName", decodedFileName);
            result.put("content", content);
            result.put("contentLength", content.length());

            log.info("成功获取文件内容，长度: {} 字符", content.length());
            return ApiResponse.success(result);

        } catch (Exception e) {
            log.error("获取文件内容失败: {}", fileName, e);
            return ApiResponse.error("获取文件内容失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param fileName 文件名
     * @return 操作结果
     */
    @DeleteMapping("/{fileName:.+}")
    public ApiResponse<String> deleteFile(@PathVariable String fileName) {
        try {
            boolean deleted = fileUploadService.deleteFile(fileName);
            if (deleted) {
                return ApiResponse.success("文件删除成功");
            } else {
                return ApiResponse.notFound("文件不存在");
            }
        } catch (Exception e) {
            log.error("删除文件失败: {}", fileName, e);
            return ApiResponse.error("删除文件失败: " + e.getMessage());
        }
    }

    /**
     * 文档问答接口
     */
    @PostMapping("/qa")
    public ApiResponse<String> documentQa(@RequestBody DocumentQaRequest request) {
        try {
            log.info("收到文档问答请求: 文件名={}, 问题={}", request.getFileName(), request.getQuestion());

            // 根据文件名获取文件内容
            String fileContent = fileUploadService.readFileContent(request.getFileName());
            log.info("文件内容长度: {} 字符", fileContent.length());

            // 限制文件内容长度，避免超过AI的token限制
            String truncatedContent = fileContent;
            if (fileContent.length() > 8000) {
                truncatedContent = fileContent.substring(0, 8000) + "...\n\n【注意：文档内容过长，已截断前8000字符】";
                log.info("文件内容过长，已截断为8000字符");
            }

            // 构建包含实际文件内容的提示词
            String prompt = String.format(
                    "请根据以下文档内容回答问题：\n\n" +
                            "文档名称：%s\n" +
                            "文档内容：\n%s\n\n" +
                            "问题：%s\n\n" +
                            "请基于上述文档内容准确回答，如果文档中没有相关信息，请说明无法从文档中找到答案。",
                    request.getFileName(), truncatedContent, request.getQuestion()
            );

            log.info("构建的提示词长度: {} 字符", prompt.length());

            // 使用 documentQa 方法，但传递包含文件内容的提示词
            // 注意：这里我们使用文件名作为docId，但实际上docId不会被使用
            Map<String, Object> cozeRequest = new HashMap<>();
            cozeRequest.put("docId", request.getFileName());
            cozeRequest.put("message", prompt);

            String qaRequest = new ObjectMapper().writeValueAsString(cozeRequest);
            log.info("发送到智能体的请求长度: {} 字符", qaRequest.length());

            // 使用 documentQa 方法
            String answer = cozeServiceClient.documentQa(qaRequest);
            log.info("智能体返回的答案: {}", answer);

            return ApiResponse.success(answer);
        } catch (Exception e) {
            log.error("文档问答失败: {}", request.getFileName(), e);
            return ApiResponse.error("文档问答失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有文件名列表（用于前端选择框）
     */
    @GetMapping("/names")
    public ApiResponse<Map<String, Object>> getAllFileNames() {
        try {
            Map<String, Object> fileList = fileUploadService.getFileList();
            // 提取文件名列表
            List<String> fileNames = new ArrayList<>();
            List<Map<String, Object>> files = (List<Map<String, Object>>) fileList.get("files");

            for (Map<String, Object> file : files) {
                fileNames.add((String) file.get("fileName"));
            }

            Map<String, Object> result = new HashMap<>();
            result.put("fileNames", fileNames);
            result.put("total", fileNames.size());

            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("获取文件名列表失败", e);
            return ApiResponse.error("获取文件名列表失败: " + e.getMessage());
        }
    }

    /**
     * 构建问答请求
     */
    private String buildQaRequest(String fileContent, String question) {
        // 根据智能体服务的实际要求构建请求格式
        Map<String, Object> request = new HashMap<>();
        request.put("content", fileContent);
        request.put("question", question);
        request.put("timestamp", LocalDateTime.now().toString());

        try {
            return new ObjectMapper().writeValueAsString(request);
        } catch (Exception e) {
            log.error("构建问答请求失败", e);
            // 返回更简单的JSON格式
            return String.format("{\"content\":\"%s\",\"question\":\"%s\"}",
                    escapeJson(fileContent), escapeJson(question));
        }
    }

    /**
     * 转义JSON字符串中的特殊字符
     */
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}