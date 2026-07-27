package com.teamknowledgeassistant.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KnowledgeAssistantController.class)
class KnowledgeAssistantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KnowledgeAssistantService service;

    @Test
    void ask_withValidRequest_returnsGroundedAnswer() throws Exception {
        KnowledgeAssistantResponse response = new KnowledgeAssistantResponse(
                "Based on the available team knowledge: ...",
                "MEDIUM",
                "GROUNDED",
                List.of(new KnowledgeAssistantResponse.Citation(UUID.randomUUID(), "Deployment Runbook", "Runbook"))
        );
        when(service.answer(anyString())).thenReturn(response);

        AskKnowledgeRequest request = new AskKnowledgeRequest("How do we deploy safely?");

        mockMvc.perform(post("/api/assistant/ask")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.confidence").value("MEDIUM"))
                .andExpect(jsonPath("$.reason").value("GROUNDED"))
                .andExpect(jsonPath("$.citations").isArray());
    }

    @Test
    void ask_withBlankQuestion_returns400() throws Exception {
        AskKnowledgeRequest request = new AskKnowledgeRequest(" ");

        mockMvc.perform(post("/api/assistant/ask")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }
}
