package com.teamknowledgeassistant.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocument, UUID> {

    List<KnowledgeDocument> findByTitleContainingIgnoreCase(String title);

    List<KnowledgeDocument> findByCategoryIgnoreCase(String category);

    @Query("select distinct kd from KnowledgeDocument kd join kd.tags t where lower(t) = lower(:tag)")
    List<KnowledgeDocument> findByTag(@Param("tag") String tag);
}
