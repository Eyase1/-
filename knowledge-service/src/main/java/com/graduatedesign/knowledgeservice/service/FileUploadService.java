package com.graduatedesign.knowledgeservice.service;

import com.graduatedesign.knowledgeservice.entity.Document;
import com.graduatedesign.knowledgeservice.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// PDF解析库
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

// Word解析库
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

// Apache Tika - 通用文档解析
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

/**
 * 文件上传服务（数据库存储版本）
 */
@Slf4j
@Service
public class FileUploadService {

    @Autowired
    private DocumentRepository documentRepository;

    // Apache Tika实例，用于解析各种文档格式
    private final Tika tika = new Tika();

    /**
     * 上传文件到数据库
     * @param file 上传的文件
     * @return 文件信息
     */
    public Map<String, Object> uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();

        // 生成唯一文件名（时间戳 + 原始文件名）
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uniqueFilename = timestamp + "_" + (originalFilename != null ? originalFilename : "file");

        log.info("开始上传文件到数据库: 文件名={}, 大小={} bytes", originalFilename, file.getSize());

        // 创建文档实体
        Document document = new Document();
        document.setFileName(uniqueFilename);
        document.setOriginalFileName(originalFilename);
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setDescription("上传文件: " + originalFilename);
        document.setUploadUserId("system"); // 这里可以根据实际需求设置上传用户ID
        document.setUploadTime(LocalDateTime.now());
        document.setFileContent(file.getBytes());

        // 保存到数据库
        Document savedDocument = documentRepository.save(document);
        log.info("文件保存到数据库成功: ID={}, 文件名={}", savedDocument.getId(), uniqueFilename);

        // 构建文件信息
        Map<String, Object> fileInfo = new HashMap<>();
        fileInfo.put("id", savedDocument.getId());
        fileInfo.put("fileName", uniqueFilename);
        fileInfo.put("originalFileName", originalFilename);
        fileInfo.put("fileSize", file.getSize());
        fileInfo.put("fileType", file.getContentType());
        fileInfo.put("uploadTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        fileInfo.put("fileUrl", "/api/knowledge/file/download/" + uniqueFilename);
        fileInfo.put("contentUrl", "/api/knowledge/file/content/" + uniqueFilename);

        // 如果是文本文件，尝试读取并返回内容预览（前500字符）
        try {
            if (isTextFile(originalFilename, file.getContentType())) {
                String content = new String(file.getBytes());
                if (content.length() > 500) {
                    fileInfo.put("contentPreview", content.substring(0, 500) + "...");
                    fileInfo.put("contentLength", content.length());
                    fileInfo.put("hasMoreContent", true);
                } else {
                    fileInfo.put("contentPreview", content);
                    fileInfo.put("contentLength", content.length());
                    fileInfo.put("hasMoreContent", false);
                }
            }
        } catch (Exception e) {
            log.warn("读取文件内容预览失败: {}", uniqueFilename, e);
        }

        return fileInfo;
    }

    /**
     * 获取文件列表
     * @return 文件列表
     */
    public Map<String, Object> getFileList() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> files = new ArrayList<>();

        try {
            List<Document> documents = documentRepository.findAllByOrderByUploadTimeDesc();

            for (Document doc : documents) {
                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("id", doc.getId());
                fileInfo.put("fileName", doc.getFileName());
                fileInfo.put("originalFileName", doc.getOriginalFileName());
                fileInfo.put("fileSize", doc.getFileSize());
                fileInfo.put("fileType", doc.getFileType());
                fileInfo.put("uploadTime", doc.getUploadTime());
                fileInfo.put("fileUrl", "/api/knowledge/file/download/" + doc.getFileName());
                files.add(fileInfo);
            }
        } catch (Exception e) {
            log.error("获取文件列表失败", e);
            throw new RuntimeException("获取文件列表失败: " + e.getMessage(), e);
        }

        result.put("files", files);
        result.put("total", files.size());
        return result;
    }

    /**
     * 删除文件
     * @param fileName 文件名
     * @return 是否删除成功
     */
    public boolean deleteFile(String fileName) {
        try {
            Optional<Document> document = documentRepository.findByFileName(fileName);
            if (document.isPresent()) {
                documentRepository.delete(document.get());
                log.info("文件从数据库删除成功: {}", fileName);
                return true;
            } else {
                log.warn("文件不存在: {}", fileName);
                return false;
            }
        } catch (Exception e) {
            log.error("删除文件失败: {}", fileName, e);
            return false;
        }
    }

    /**
     * 获取文件资源
     * @param fileName 文件名
     * @return 文件资源
     */
    public Resource getFileAsResource(String fileName) throws IOException {
        Optional<Document> document = documentRepository.findByFileName(fileName);
        if (document.isPresent()) {
            byte[] fileContent = document.get().getFileContent();
            return new ByteArrayResource(fileContent) {
                @Override
                public String getFilename() {
                    return document.get().getOriginalFileName();
                }
            };
        } else {
            throw new IOException("文件不存在: " + fileName);
        }
    }

    /**
     * 读取文件内容（支持文本、PDF、Word文件）
     * @param fileName 文件名
     * @return 文件内容
     */
    public String readFileContent(String fileName) throws IOException {
        Optional<Document> document = documentRepository.findByFileName(fileName);
        if (!document.isPresent()) {
            throw new IOException("文件不存在: " + fileName);
        }

        byte[] fileContent = document.get().getFileContent();
        String originalFileName = document.get().getOriginalFileName();

        // 根据文件类型选择不同的解析方式
        String lowerFileName = originalFileName.toLowerCase();

        if (lowerFileName.endsWith(".pdf")) {
            // PDF文件
            return readPdfContent(fileContent, originalFileName);
        } else if (lowerFileName.endsWith(".docx")) {
            // Word 2007+ 文件 (.docx)
            return readDocxContent(fileContent, originalFileName);
        } else if (lowerFileName.endsWith(".doc")) {
            // Word 97-2003 文件 (.doc)
            return readDocContent(fileContent, originalFileName);
        } else {
            // 文本文件
            String content = new String(fileContent);
            log.info("成功读取文件内容: 文件名={}, 内容长度={}", fileName, content.length());
            return content;
        }
    }

    /**
     * 读取PDF文件内容
     * @param fileContent 文件内容字节数组
     * @param fileName 文件名
     * @return 文件内容
     */
    private String readPdfContent(byte[] fileContent, String fileName) throws IOException {
        try (PDDocument document = PDDocument.load(fileContent)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String content = stripper.getText(document);
            log.info("成功读取PDF文件内容: 文件名={}, 内容长度={}", fileName, content.length());
            return content;
        } catch (Exception e) {
            log.error("读取PDF文件失败: {}", fileName, e);
            throw new IOException("读取PDF文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 读取Word 2007+文件内容 (.docx)
     * @param fileContent 文件内容字节数组
     * @param fileName 文件名
     * @return 文件内容
     */
    private String readDocxContent(byte[] fileContent, String fileName) throws IOException {
        try (java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(fileContent);
             XWPFDocument document = new XWPFDocument(bis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            String content = extractor.getText();
            log.info("成功读取Word文件内容: 文件名={}, 内容长度={}", fileName, content.length());
            return content;
        } catch (Exception e) {
            log.error("读取Word文件失败: {}", fileName, e);
            throw new IOException("读取Word文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 读取Word 97-2003文件内容 (.doc)
     * 使用Apache Tika库来解析.doc文件
     * @param fileContent 文件内容字节数组
     * @param fileName 文件名
     * @return 文件内容
     */
    private String readDocContent(byte[] fileContent, String fileName) throws IOException {
        try (java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(fileContent)) {
            // 使用Apache Tika来解析.doc文件
            String content = tika.parseToString(bis);
            log.info("成功读取Word文件内容: 文件名={}, 内容长度={}", fileName, content.length());
            return content;
        } catch (TikaException e) {
            log.error("使用Tika读取Word文件失败: {}", fileName, e);
            throw new IOException("读取Word文件失败: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("读取Word文件失败: {}", fileName, e);
            throw new IOException("读取Word文件失败: " + e.getMessage() + "。建议将文件转换为.docx格式。", e);
        }
    }

    /**
     * 检查文件是否存在
     * @param fileName 文件名
     * @return 是否存在
     */
    public boolean fileExists(String fileName) {
        try {
            return documentRepository.existsByFileName(fileName);
        } catch (Exception e) {
            log.error("检查文件是否存在时出错: {}", fileName, e);
            return false;
        }
    }

    // 以下方法保持不变...
    /**
     * 判断是否为允许的文件类型
     * @param fileName 文件名
     * @param contentType 内容类型
     * @return 是否为允许的文件类型
     */
    public boolean isAllowedFileType(String fileName, String contentType) {
        if (fileName == null) {
            return false;
        }

        String lowerFileName = fileName.toLowerCase();

        // 文本文件
        if (isTextFile(fileName, contentType)) {
            return true;
        }

        // PDF文件
        if (lowerFileName.endsWith(".pdf") ||
                (contentType != null && contentType.equals("application/pdf"))) {
            return true;
        }

        // Word文件
        if (lowerFileName.endsWith(".doc") ||
                lowerFileName.endsWith(".docx") ||
                (contentType != null && (contentType.equals("application/msword") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")))) {
            return true;
        }

        return false;
    }

    /**
     * 判断是否为文本文件
     * @param fileName 文件名
     * @param contentType 内容类型
     * @return 是否为文本文件
     */
    private boolean isTextFile(String fileName, String contentType) {
        if (contentType != null && contentType.startsWith("text/")) {
            return true;
        }

        if (fileName == null) {
            return false;
        }

        String lowerFileName = fileName.toLowerCase();
        return lowerFileName.endsWith(".txt") ||
                lowerFileName.endsWith(".md") ||
                lowerFileName.endsWith(".json") ||
                lowerFileName.endsWith(".xml") ||
                lowerFileName.endsWith(".html") ||
                lowerFileName.endsWith(".css") ||
                lowerFileName.endsWith(".js") ||
                lowerFileName.endsWith(".java") ||
                lowerFileName.endsWith(".py") ||
                lowerFileName.endsWith(".yaml") ||
                lowerFileName.endsWith(".yml") ||
                lowerFileName.endsWith(".properties") ||
                lowerFileName.endsWith(".log") ||
                lowerFileName.endsWith(".csv");
    }

    /**
     * 判断是否为PDF文件
     * @param fileName 文件名
     * @param contentType 内容类型
     * @return 是否为PDF文件
     */
    public boolean isPdfFile(String fileName, String contentType) {
        if (fileName == null) {
            return false;
        }
        String lowerFileName = fileName.toLowerCase();
        return lowerFileName.endsWith(".pdf") ||
                (contentType != null && contentType.equals("application/pdf"));
    }

    /**
     * 判断是否为Word文件
     * @param fileName 文件名
     * @param contentType 内容类型
     * @return 是否为Word文件
     */
    public boolean isWordFile(String fileName, String contentType) {
        if (fileName == null) {
            return false;
        }
        String lowerFileName = fileName.toLowerCase();
        return lowerFileName.endsWith(".doc") ||
                lowerFileName.endsWith(".docx") ||
                (contentType != null && (contentType.equals("application/msword") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")));
    }
}