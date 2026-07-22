package com.teamknowledgeassistant.document;

import com.teamknowledgeassistant.common.AuditableEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "knowledge_documents",
        indexes = {
                @Index(name = "idx_kd_category", columnList = "category"),
                @Index(name = "idx_kd_created_at", columnList = "created_at"),
                @Index(name = "idx_kd_updated_at", columnList = "updated_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(exclude = "tags")
public class KnowledgeDocument extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "UUID")
    @EqualsAndHashCode.Include
    private UUID id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 20000, message = "Content must be between 10 and 20000 characters")
    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "CLOB")
    private String content;

    @NotBlank(message = "Category is required")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_-]*$", message = "Category must start with a letter and contain only letters, digits, underscore, or hyphen")
    @Size(max = 100, message = "Category must be at most 100 characters")
    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @NotNull(message = "Tags are required")
    @NotEmpty(message = "At least one tag is required")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "knowledge_document_tags",
            joinColumns = @JoinColumn(name = "document_id"),
            uniqueConstraints = @UniqueConstraint(name = "uk_kd_tag_per_doc", columnNames = {"document_id", "tag"}),
            indexes = @Index(name = "idx_kd_tag", columnList = "tag")
    )
    @Column(name = "tag", nullable = false, length = 50)
    private Set<@NotBlank(message = "Tag must not be blank") @Size(max = 50, message = "Tag must be at most 50 characters") String> tags = new LinkedHashSet<>();

    public void addTag(String tag) {
        if (tag != null && !tag.isBlank()) {
            this.tags.add(tag.trim());
        }
    }

    public void removeTag(String tag) {
        if (tag != null) {
            this.tags.remove(tag.trim());
        }
    }
}
