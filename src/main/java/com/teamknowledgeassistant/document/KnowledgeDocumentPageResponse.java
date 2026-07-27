package com.teamknowledgeassistant.document;

import java.util.List;

/**
 * Page envelope for document list and search responses.
 */
public record KnowledgeDocumentPageResponse(
        List<KnowledgeDocumentSummaryResponse> items,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
}
