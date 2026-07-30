package com.teamknowledgeassistant.document;

import com.teamknowledgeassistant.common.exception.ResourceAlreadyExistsException;
import com.teamknowledgeassistant.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KnowledgeDocumentServiceTest {

    @Mock
    private KnowledgeDocumentRepository repository;

    @InjectMocks
    private KnowledgeDocumentService service;

    private KnowledgeDocument document;
    private UUID documentId;

    @BeforeEach
    void setUp() {
        documentId = UUID.randomUUID();
        document = new KnowledgeDocument();
        document.setId(documentId);
        document.setTitle("Deployment Runbook");
        document.setContent("Steps to deploy the service safely to production.");
        document.setCategory("Runbook");
        document.setTags(new LinkedHashSet<>(Set.of("deployment", "ops")));
    }

    @Test
    void create_savesDocumentAndReturnsResponse() {
        CreateKnowledgeDocumentRequest request = new CreateKnowledgeDocumentRequest(
                "Deployment Runbook",
                "Steps to deploy the service safely to production.",
                "Runbook",
                Set.of("deployment", "ops")
        );
        when(repository.existsByTitleIgnoreCase("Deployment Runbook")).thenReturn(false);
        when(repository.save(any(KnowledgeDocument.class))).thenReturn(document);

        KnowledgeDocumentResponse response = service.create(request);

        assertThat(response.id()).isEqualTo(documentId);
        assertThat(response.title()).isEqualTo("Deployment Runbook");
        assertThat(response.tags()).containsExactlyInAnyOrder("deployment", "ops");
        verify(repository, times(1)).save(any(KnowledgeDocument.class));
    }

    @Test
    void create_whenTitleAlreadyExists_throwsResourceAlreadyExistsException() {
        CreateKnowledgeDocumentRequest request = new CreateKnowledgeDocumentRequest(
                "Deployment Runbook",
                "Steps to deploy the service safely to production.",
                "Runbook",
                Set.of("deployment", "ops")
        );
        when(repository.existsByTitleIgnoreCase("Deployment Runbook")).thenReturn(true);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage("A document with the same title already exists");
        verify(repository, never()).save(any(KnowledgeDocument.class));
    }

    @Test
    void findById_whenDocumentExists_returnsResponse() {
        when(repository.findById(documentId)).thenReturn(Optional.of(document));

        KnowledgeDocumentResponse response = service.findById(documentId);

        assertThat(response.id()).isEqualTo(documentId);
        assertThat(response.category()).isEqualTo("Runbook");
    }

    @Test
    void findById_whenDocumentMissing_throwsResourceNotFoundException() {
        when(repository.findById(documentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(documentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(documentId.toString());
    }

    @Test
    void findAll_returnsAllDocumentsMappedToResponses() {
        Page<KnowledgeDocument> page = new PageImpl<>(List.of(document));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        KnowledgeDocumentPageResponse responses = service.search(null, null, null, 0, 20, List.of("updatedAt,desc"));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAll(any(Specification.class), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();

        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(20);
        assertThat(pageable.getSort().getOrderFor("updatedAt")).isEqualTo(new Sort.Order(Sort.Direction.DESC, "updatedAt"));
        assertThat(responses.items()).hasSize(1);
        assertThat(responses.items().get(0).id()).isEqualTo(documentId);
    }

    @Test
    void search_withUnsupportedSortField_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> service.search(null, null, null, 0, 20, List.of("content,asc")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported sort field");
    }

    @Test
    void search_withInvalidPageSize_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> service.search(null, null, null, 0, 101, List.of("updatedAt,desc")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Size must be between 1 and 100");
    }

    @Test
    void search_withBlankSort_usesDefaultUpdatedAtDesc() {
        Page<KnowledgeDocument> page = new PageImpl<>(List.of(document));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        service.search(null, null, null, 0, 20, List.of(" "));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository, times(1)).findAll(any(Specification.class), pageableCaptor.capture());
        Sort.Order order = pageableCaptor.getValue().getSort().getOrderFor("updatedAt");
        assertThat(order).isEqualTo(new Sort.Order(Sort.Direction.DESC, "updatedAt"));
    }

    @Test
    void update_whenDocumentExists_updatesAndReturnsResponse() {
        UpdateKnowledgeDocumentRequest request = new UpdateKnowledgeDocumentRequest(
                "Updated Title",
                "Updated content that is long enough to satisfy validation.",
                "Runbook",
                Set.of("updated")
        );
        when(repository.findById(documentId)).thenReturn(Optional.of(document));
        when(repository.existsByTitleIgnoreCase("Updated Title")).thenReturn(false);
        when(repository.save(any(KnowledgeDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        KnowledgeDocumentResponse response = service.update(documentId, request);

        assertThat(response.title()).isEqualTo("Updated Title");
        assertThat(response.tags()).containsExactly("updated");
    }

    @Test
    void update_whenChangingTitleToExistingTitle_throwsResourceAlreadyExistsException() {
        UpdateKnowledgeDocumentRequest request = new UpdateKnowledgeDocumentRequest(
                "Existing Runbook",
                "Updated content that is long enough to satisfy validation.",
                "Runbook",
                Set.of("updated")
        );
        when(repository.findById(documentId)).thenReturn(Optional.of(document));
        when(repository.existsByTitleIgnoreCase("Existing Runbook")).thenReturn(true);

        assertThatThrownBy(() -> service.update(documentId, request))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage("A document with the same title already exists");
        verify(repository, never()).save(any(KnowledgeDocument.class));
    }

    @Test
    void update_whenDocumentMissing_throwsResourceNotFoundException() {
        UpdateKnowledgeDocumentRequest request = new UpdateKnowledgeDocumentRequest(
                "Updated Title",
                "Updated content that is long enough to satisfy validation.",
                "Runbook",
                Set.of("updated")
        );
        when(repository.findById(documentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(documentId, request))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).save(any(KnowledgeDocument.class));
    }

    @Test
    void delete_whenDocumentExists_deletesDocument() {
        when(repository.existsById(documentId)).thenReturn(true);

        service.delete(documentId);

        verify(repository, times(1)).deleteById(documentId);
    }

    @Test
    void delete_whenDocumentMissing_throwsResourceNotFoundException() {
        when(repository.existsById(documentId)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(documentId))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).deleteById(any(UUID.class));
    }
}
