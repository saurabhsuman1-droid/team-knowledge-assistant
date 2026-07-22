package com.teamknowledgeassistant.note;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class KnowledgeNoteController {

    private final KnowledgeNoteService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KnowledgeNoteResponse create(@Valid @RequestBody CreateKnowledgeNoteRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<KnowledgeNoteResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public KnowledgeNoteResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
