package com.teamknowledgeassistant.note;

import java.time.LocalDateTime;

public record KnowledgeNoteResponse(
        Long id,
        String title,
        String content,
        String author,
        LocalDateTime createdAt
) {
    public static KnowledgeNoteResponse from(KnowledgeNote note) {
        return new KnowledgeNoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getAuthor(),
                note.getCreatedAt()
        );
    }
}
