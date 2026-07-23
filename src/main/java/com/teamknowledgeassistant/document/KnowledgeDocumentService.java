package com.teamknowledgeassistant.document;

import com.teamknowledgeassistant.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

/**
 * Business logic for managing {@link KnowledgeDocument} resources.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class KnowledgeDocumentService {

    private final KnowledgeDocumentRepository repository;

    public KnowledgeDocumentResponse create(CreateKnowledgeDocumentRequest request) {
        KnowledgeDocument document = new KnowledgeDocument();
        document.setTitle(request.title());
        document.setContent(request.content());
        document.setCategory(request.category());
        document.setTags(new LinkedHashSet<>(request.tags()));

        return KnowledgeDocumentResponse.from(repository.save(document));
    }

    @Transactional(readOnly = true)
    public KnowledgeDocumentResponse findById(UUID id) {
        return KnowledgeDocumentResponse.from(getOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<KnowledgeDocumentResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(KnowledgeDocumentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<KnowledgeDocumentResponse> findByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category)
                .stream()
                .map(KnowledgeDocumentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<KnowledgeDocumentResponse> findByTag(String tag) {
        return repository.findByTag(tag)
                .stream()
                .map(KnowledgeDocumentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<KnowledgeDocumentResponse> searchByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(KnowledgeDocumentResponse::from)
                .toList();
    }

    public KnowledgeDocumentResponse update(UUID id, UpdateKnowledgeDocumentRequest request) {
        KnowledgeDocument document = getOrThrow(id);
        document.setTitle(request.title());
        document.setContent(request.content());
        document.setCategory(request.category());
        document.setTags(new LinkedHashSet<>(request.tags()));

        return KnowledgeDocumentResponse.from(repository.save(document));
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Knowledge document not found for id: " + id);
        }
        repository.deleteById(id);
    }

    private KnowledgeDocument getOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Knowledge document not found for id: " + id));
    }
}
