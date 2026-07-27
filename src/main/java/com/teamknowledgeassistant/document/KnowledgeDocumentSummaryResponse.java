package com.teamknowledgeassistant.document;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Lightweight response payload for list/search views.
 */
public record KnowledgeDocumentSummaryResponse(
        UUID id,
        String title,
        String category,
        Set<String> tags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {

    public static KnowledgeDocumentSummaryResponse from(KnowledgeDocument document) {
        return new KnowledgeDocumentSummaryResponse(
                document.getId(),
                document.getTitle(),
                document.getCategory(),
                Set.copyOf(document.getTags()),
                document.getCreatedAt(),
                document.getUpdatedAt(),
                document.getVersion()
        );
    }
}
