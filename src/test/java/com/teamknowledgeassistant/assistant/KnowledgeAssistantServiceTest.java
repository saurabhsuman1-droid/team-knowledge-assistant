package com.teamknowledgeassistant.assistant;

import com.teamknowledgeassistant.document.KnowledgeDocument;
import com.teamknowledgeassistant.document.KnowledgeDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KnowledgeAssistantServiceTest {

    @Mock
    private KnowledgeDocumentRepository repository;

    @InjectMocks
    private KnowledgeAssistantService service;

    private KnowledgeDocument deploymentDoc;

    @BeforeEach
    void setUp() {
        deploymentDoc = new KnowledgeDocument();
        deploymentDoc.setId(UUID.randomUUID());
        deploymentDoc.setTitle("Deployment Runbook");
        deploymentDoc.setCategory("Runbook");
        deploymentDoc.setContent("Deploy using blue green strategy. Verify health endpoint before switching traffic.");
        deploymentDoc.setTags(new LinkedHashSet<>(Set.of("deployment", "release")));
        deploymentDoc.setCreatedAt(LocalDateTime.now());
        deploymentDoc.setUpdatedAt(LocalDateTime.now());
        deploymentDoc.setVersion(1L);
    }

    @Test
    void answer_withRelevantDocuments_returnsGroundedResponseWithCitations() {
        when(repository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(deploymentDoc)));

        KnowledgeAssistantResponse response = service.answer("How do we deploy safely?");

        assertThat(response.reason()).isEqualTo("GROUNDED");
        assertThat(response.answer()).contains("Deployment Runbook");
        assertThat(response.citations()).hasSize(1);
    }

    @Test
    void answer_withNoRelevantDocuments_returnsNoContextResponse() {
        when(repository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        KnowledgeAssistantResponse response = service.answer("How do we deploy safely?");

        assertThat(response.reason()).isEqualTo("NO_CONTEXT");
        assertThat(response.confidence()).isEqualTo("LOW");
        assertThat(response.citations()).isEmpty();
    }
}
