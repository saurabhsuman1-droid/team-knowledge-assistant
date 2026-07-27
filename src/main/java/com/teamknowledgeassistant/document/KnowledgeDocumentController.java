package com.teamknowledgeassistant.document;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST API for managing knowledge documents.
 * Contains no business logic; all operations are delegated to {@link KnowledgeDocumentService}.
 */
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Tag(name = "Knowledge Documents", description = "Operations for managing team knowledge documents")
public class KnowledgeDocumentController {

    private final KnowledgeDocumentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new knowledge document")
    public KnowledgeDocumentResponse create(@Valid @RequestBody CreateKnowledgeDocumentRequest request) {
        return service.create(request);
    }

    @GetMapping
    @Operation(summary = "List knowledge documents with optional composable filters, pagination, and sorting")
    public KnowledgeDocumentPageResponse findAll(
            @RequestParam(required = false) @Parameter(description = "Filter by exact category") String category,
            @RequestParam(required = false) @Parameter(description = "Filter by tag") String tag,
            @RequestParam(required = false) @Parameter(description = "Filter by title (contains, case-insensitive)") String title,
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number (0-based)") Integer page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Page size (1-100)") Integer size,
            @RequestParam(required = false) @Parameter(description = "Sort fields, e.g. sort=updatedAt,desc") List<String> sort) {
        return service.search(title, category, tag, page, size, sort);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a knowledge document by id")
    public KnowledgeDocumentResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing knowledge document")
    public KnowledgeDocumentResponse update(@PathVariable UUID id,
                                             @Valid @RequestBody UpdateKnowledgeDocumentRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a knowledge document")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
