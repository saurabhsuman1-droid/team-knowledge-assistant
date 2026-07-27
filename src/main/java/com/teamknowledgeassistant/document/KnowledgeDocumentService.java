package com.teamknowledgeassistant.document;

import com.teamknowledgeassistant.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Business logic for managing {@link KnowledgeDocument} resources.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class KnowledgeDocumentService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("title", "category", "createdAt", "updatedAt");

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
        public KnowledgeDocumentPageResponse search(String title,
                            String category,
                            String tag,
                            Integer page,
                            Integer size,
                            List<String> sort) {
        int resolvedPage = page == null ? DEFAULT_PAGE : page;
        int resolvedSize = size == null ? DEFAULT_SIZE : size;
        validatePaging(resolvedPage, resolvedSize);

        Pageable pageable = PageRequest.of(resolvedPage, resolvedSize, buildSort(sort));
        Specification<KnowledgeDocument> spec = Specification.where(titleContains(title))
            .and(categoryEquals(category))
            .and(hasTag(tag));

        Page<KnowledgeDocument> result = repository.findAll(spec, pageable);
        List<KnowledgeDocumentSummaryResponse> items = result.getContent().stream()
            .map(KnowledgeDocumentSummaryResponse::from)
            .toList();

        return new KnowledgeDocumentPageResponse(
            items,
            result.getNumber(),
            result.getSize(),
            result.getTotalElements(),
            result.getTotalPages(),
            result.hasNext(),
            result.hasPrevious()
        );
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

    private void validatePaging(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be greater than or equal to 0");
        }
        if (size <= 0 || size > MAX_SIZE) {
            throw new IllegalArgumentException("Size must be between 1 and " + MAX_SIZE);
        }
    }

    private Sort buildSort(List<String> sortParams) {
        if (sortParams == null || sortParams.isEmpty()) {
            return Sort.by(Sort.Order.desc("updatedAt"));
        }

        List<Sort.Order> orders = sortParams.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .map(this::toSortOrder)
                .toList();

        return orders.isEmpty() ? Sort.by(Sort.Order.desc("updatedAt")) : Sort.by(orders);
    }

    private Sort.Order toSortOrder(String sortParam) {
        String[] parts = sortParam.split(",", 2);
        String property = parts[0].trim();

        if (!ALLOWED_SORT_FIELDS.contains(property)) {
            throw new IllegalArgumentException("Unsupported sort field: " + property);
        }

        Sort.Direction direction = Sort.Direction.DESC;
        if (parts.length == 2 && StringUtils.hasText(parts[1])) {
            direction = Sort.Direction.fromString(parts[1].trim().toUpperCase(Locale.ROOT));
        }

        return new Sort.Order(direction, property);
    }

    private Specification<KnowledgeDocument> titleContains(String title) {
        if (!StringUtils.hasText(title)) {
            return null;
        }

        String normalized = title.trim().toLowerCase(Locale.ROOT);
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + normalized + "%");
    }

    private Specification<KnowledgeDocument> categoryEquals(String category) {
        if (!StringUtils.hasText(category)) {
            return null;
        }

        String normalized = category.trim().toLowerCase(Locale.ROOT);
        return (root, query, cb) -> cb.equal(cb.lower(root.get("category")), normalized);
    }

    private Specification<KnowledgeDocument> hasTag(String tag) {
        if (!StringUtils.hasText(tag)) {
            return null;
        }

        String normalized = tag.trim().toLowerCase(Locale.ROOT);
        return (root, query, cb) -> {
            query.distinct(true);
            Join<KnowledgeDocument, String> tags = root.join("tags", JoinType.INNER);
            return cb.equal(cb.lower(tags), normalized);
        };
    }
}
