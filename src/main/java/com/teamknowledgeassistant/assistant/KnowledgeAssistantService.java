package com.teamknowledgeassistant.assistant;

import com.teamknowledgeassistant.document.KnowledgeDocument;
import com.teamknowledgeassistant.document.KnowledgeDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KnowledgeAssistantService {

    private static final int MAX_CANDIDATES = 100;
    private static final int MAX_CITATIONS = 3;
    private static final int MAX_ANSWER_POINTS = 3;

    private final KnowledgeDocumentRepository repository;

    public KnowledgeAssistantResponse answer(String question) {
        String normalizedQuestion = normalize(question);
        if (!StringUtils.hasText(normalizedQuestion)) {
            return new KnowledgeAssistantResponse(
                    "I need a non-empty question to help you.",
                    "LOW",
                    "INVALID_QUESTION",
                    List.of()
            );
        }

        List<String> terms = extractTerms(normalizedQuestion);
        if (terms.isEmpty()) {
            return new KnowledgeAssistantResponse(
                    "I could not extract meaningful keywords from your question. Please be more specific.",
                    "LOW",
                    "NO_MEANINGFUL_TERMS",
                    List.of()
            );
        }

        List<KnowledgeDocument> candidates = repository.findAll(
                PageRequest.of(0, MAX_CANDIDATES, Sort.by(Sort.Order.desc("updatedAt")))
        ).getContent();

        List<ScoredDocument> ranked = candidates.stream()
                .map(document -> new ScoredDocument(document, score(document, terms)))
                .filter(scored -> scored.score > 0)
                .sorted(Comparator.comparingInt(ScoredDocument::score).reversed())
                .toList();

        if (ranked.isEmpty()) {
            return new KnowledgeAssistantResponse(
                    "I could not find enough relevant team knowledge for this question.",
                    "LOW",
                    "NO_CONTEXT",
                    List.of()
            );
        }

        List<ScoredDocument> top = ranked.stream().limit(MAX_CITATIONS).toList();
        String answer = buildAnswer(top, terms);
        String confidence = toConfidence(top.get(0).score);
        List<KnowledgeAssistantResponse.Citation> citations = top.stream()
                .map(item -> new KnowledgeAssistantResponse.Citation(
                        item.document.getId(),
                        item.document.getTitle(),
                        item.document.getCategory()
                ))
                .toList();

        return new KnowledgeAssistantResponse(answer, confidence, "GROUNDED", citations);
    }

    private String normalize(String question) {
        return question == null ? "" : question.trim().replaceAll("\\s+", " ");
    }

    private List<String> extractTerms(String question) {
        return Arrays.stream(question.toLowerCase(Locale.ROOT).split("[^a-z0-9]+"))
                .filter(token -> token.length() >= 3)
                .filter(token -> !STOP_WORDS.contains(token))
                .distinct()
                .toList();
    }

    private int score(KnowledgeDocument document, List<String> terms) {
        String title = lower(document.getTitle());
        String category = lower(document.getCategory());
        String content = lower(document.getContent());
        String tags = lower(String.join(" ", document.getTags()));

        int total = 0;
        for (String term : terms) {
            if (title.contains(term)) {
                total += 4;
            }
            if (category.contains(term)) {
                total += 2;
            }
            if (tags.contains(term)) {
                total += 3;
            }
            if (content.contains(term)) {
                total += 1;
            }
        }
        return total;
    }

    private String buildAnswer(List<ScoredDocument> top, List<String> terms) {
        List<String> points = new ArrayList<>();
        for (ScoredDocument scoredDocument : top) {
            String snippet = bestSnippet(scoredDocument.document, terms);
            points.add(scoredDocument.document.getTitle() + ": " + snippet);
            if (points.size() == MAX_ANSWER_POINTS) {
                break;
            }
        }

        return "Based on the available team knowledge: " + String.join(" ", points);
    }

    private String bestSnippet(KnowledgeDocument document, List<String> terms) {
        String[] sentences = document.getContent().split("(?<=[.!?])\\s+");
        for (String sentence : sentences) {
            String lower = sentence.toLowerCase(Locale.ROOT);
            boolean matched = terms.stream().anyMatch(lower::contains);
            if (matched) {
                return trimToLength(sentence, 220);
            }
        }
        return trimToLength(document.getContent(), 220);
    }

    private String trimToLength(String value, int maxLength) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.length() <= maxLength) {
            return normalized;
        }
        return normalized.substring(0, maxLength - 3) + "...";
    }

    private String lower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private String toConfidence(int score) {
        if (score >= 10) {
            return "HIGH";
        }
        if (score >= 5) {
            return "MEDIUM";
        }
        return "LOW";
    }

    private static final Set<String> STOP_WORDS = Set.of(
            "the", "and", "for", "with", "that", "this", "from", "have", "are", "how", "what", "when", "where", "why", "who", "into", "about", "your"
    );

    private record ScoredDocument(KnowledgeDocument document, int score) {
    }
}
