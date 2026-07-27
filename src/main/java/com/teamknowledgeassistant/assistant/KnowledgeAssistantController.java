package com.teamknowledgeassistant.assistant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
@Tag(name = "Knowledge Assistant", description = "Ask grounded questions against team knowledge")
public class KnowledgeAssistantController {

    private final KnowledgeAssistantService service;

    @PostMapping("/ask")
    @Operation(summary = "Answer a question using stored team knowledge and citations")
    public KnowledgeAssistantResponse ask(@Valid @RequestBody AskKnowledgeRequest request) {
        return service.answer(request.question());
    }
}
