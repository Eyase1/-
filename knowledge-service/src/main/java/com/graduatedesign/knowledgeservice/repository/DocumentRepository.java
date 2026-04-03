package com.graduatedesign.knowledgeservice.repository;

import com.graduatedesign.knowledgeservice.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 文档数据访问接口
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByFileName(String fileName);

    List<Document> findAllByOrderByUploadTimeDesc();

    boolean existsByFileName(String fileName);

    @Query("SELECT d.fileName FROM Document d")
    List<String> findAllFileNames();

    void deleteByFileName(String fileName);
}