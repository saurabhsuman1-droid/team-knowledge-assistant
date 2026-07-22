package com.teamknowledgeassistant.note;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateKnowledgeNoteRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must be at most 200 characters")
        String title,

        @NotBlank(message = "Content is required")
        @Size(max = 4000, message = "Content must be at most 4000 characters")
        String content,

        @NotBlank(message = "Author is required")
        @Size(max = 120, message = "Author must be at most 120 characters")
        String author
) {
}
