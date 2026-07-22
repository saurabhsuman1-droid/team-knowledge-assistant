package com.teamknowledgeassistant.note;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KnowledgeNoteService {

    private final KnowledgeNoteRepository repository;

    public KnowledgeNoteResponse create(CreateKnowledgeNoteRequest request) {
        KnowledgeNote saved = repository.save(
                KnowledgeNote.builder()
                        .title(request.title())
                        .content(request.content())
                        .author(request.author())
                        .build()
        );
        return KnowledgeNoteResponse.from(saved);
    }

    public List<KnowledgeNoteResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(KnowledgeNoteResponse::from)
                .toList();
    }

    public KnowledgeNoteResponse findById(Long id) {
        KnowledgeNote note = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Knowledge note not found for id: " + id));
        return KnowledgeNoteResponse.from(note);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Knowledge note not found for id: " + id);
        }
        repository.deleteById(id);
    }
}
