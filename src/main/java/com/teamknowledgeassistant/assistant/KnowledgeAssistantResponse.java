package com.teamknowledgeassistant.assistant;

import java.util.List;
import java.util.UUID;

public record KnowledgeAssistantResponse(
        String answer,
        String confidence,
        String reason,
        List<Citation> citations
) {

    public record Citation(UUID documentId, String title, String category) {
    }
}
