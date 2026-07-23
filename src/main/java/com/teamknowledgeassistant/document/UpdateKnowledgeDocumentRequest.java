package com.teamknowledgeassistant.document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Request payload for updating an existing {@link KnowledgeDocument}.
 */
public record UpdateKnowledgeDocumentRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
        String title,

        @NotBlank(message = "Content is required")
        @Size(min = 10, max = 20000, message = "Content must be between 10 and 20000 characters")
        String content,

        @NotBlank(message = "Category is required")
        @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_-]*$", message = "Category must start with a letter and contain only letters, digits, underscore, or hyphen")
        @Size(max = 100, message = "Category must be at most 100 characters")
        String category,

        @NotNull(message = "Tags are required")
        @NotEmpty(message = "At least one tag is required")
        Set<@NotBlank(message = "Tag must not be blank") @Size(max = 50, message = "Tag must be at most 50 characters") String> tags
) {
}
