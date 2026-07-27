package com.teamknowledgeassistant.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamknowledgeassistant.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KnowledgeDocumentController.class)
class KnowledgeDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KnowledgeDocumentService service;

    @Test
    void create_withValidRequest_returns201() throws Exception {
        UUID id = UUID.randomUUID();
        CreateKnowledgeDocumentRequest request = new CreateKnowledgeDocumentRequest(
                "Deployment Runbook",
                "Steps to deploy the service safely to production.",
                "Runbook",
                Set.of("deployment")
        );
        KnowledgeDocumentResponse response = new KnowledgeDocumentResponse(
                id, request.title(), request.content(), request.category(), request.tags(), null, null, 0L
        );
        when(service.create(any(CreateKnowledgeDocumentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/documents")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Deployment Runbook"));
    }

    @Test
    void create_withBlankTitle_returns400() throws Exception {
        CreateKnowledgeDocumentRequest request = new CreateKnowledgeDocumentRequest(
                "", "Steps to deploy the service safely to production.", "Runbook", Set.of("deployment")
        );

        mockMvc.perform(post("/api/documents")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isNotEmpty());
    }

    @Test
    void findById_whenExists_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        KnowledgeDocumentResponse response = new KnowledgeDocumentResponse(
                id, "Deployment Runbook", "content long enough", "Runbook", Set.of("deployment"), null, null, 0L
        );
        when(service.findById(id)).thenReturn(response);

        mockMvc.perform(get("/api/documents/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void findById_whenMissing_returns404() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("Knowledge document not found for id: " + id));

        mockMvc.perform(get("/api/documents/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void findAll_withCategoryFilter_delegatesToService() throws Exception {
        KnowledgeDocumentPageResponse response = new KnowledgeDocumentPageResponse(List.of(), 0, 20, 0, 0, false, false);
        when(service.search(any(), any(), any(), any(), any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/documents").param("category", "Runbook"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    void delete_whenExists_returns204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/documents/{id}", id))
                .andExpect(status().isNoContent());
    }
}
