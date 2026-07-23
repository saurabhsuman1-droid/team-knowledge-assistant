package com.teamknowledgeassistant.document;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Response payload representing a {@link KnowledgeDocument}.
 */
public record KnowledgeDocumentResponse(
        UUID id,
        String title,
        String content,
        String category,
        Set<String> tags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {

    public static KnowledgeDocumentResponse from(KnowledgeDocument document) {
        return new KnowledgeDocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getContent(),
                document.getCategory(),
                Set.copyOf(document.getTags()),
                document.getCreatedAt(),
                document.getUpdatedAt(),
                document.getVersion()
        );
    }
}
