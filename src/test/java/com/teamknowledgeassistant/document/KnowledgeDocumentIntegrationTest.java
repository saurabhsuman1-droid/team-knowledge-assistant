package com.teamknowledgeassistant.document;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class KnowledgeDocumentIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KnowledgeDocumentRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void findAll_returnsRequestedPageWithExpectedMetadata() {
        for (int i = 1; i <= 10; i++) {
            CreateKnowledgeDocumentRequest request = new CreateKnowledgeDocumentRequest(
                    "Runbook " + i,
                    "Content for runbook " + i + " with enough length.",
                    "Runbook",
                    Set.of("ops", "day3")
            );

            ResponseEntity<KnowledgeDocumentResponse> createResponse =
                    restTemplate.postForEntity("/api/documents", request, KnowledgeDocumentResponse.class);
            assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }

        ResponseEntity<JsonNode> pageResponse = restTemplate.exchange(
                "/api/documents?page=1&size=5",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                JsonNode.class
        );

        assertThat(pageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(pageResponse.getBody()).isNotNull();

        JsonNode body = pageResponse.getBody();
        assertThat(body.get("items").size()).isEqualTo(5);
        assertThat(body.get("page").asInt()).isEqualTo(1);
        assertThat(body.get("size").asInt()).isEqualTo(5);
        assertThat(body.get("totalElements").asLong()).isEqualTo(10L);
        assertThat(body.get("totalPages").asInt()).isEqualTo(2);
        assertThat(body.get("hasPrevious").asBoolean()).isTrue();
    }

        @Test
        void create_whenDuplicateTitle_returnsConflict() {
        CreateKnowledgeDocumentRequest request = new CreateKnowledgeDocumentRequest(
            "Release Checklist",
            "Long enough content for the first checklist document.",
            "Runbook",
            Set.of("release")
        );

        ResponseEntity<KnowledgeDocumentResponse> firstCreate =
            restTemplate.postForEntity("/api/documents", request, KnowledgeDocumentResponse.class);
        assertThat(firstCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<JsonNode> duplicateCreate =
            restTemplate.postForEntity("/api/documents", request, JsonNode.class);

        assertThat(duplicateCreate.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(duplicateCreate.getBody()).isNotNull();
        assertThat(duplicateCreate.getBody().get("status").asInt()).isEqualTo(409);
        }

        @Test
        void create_withInvalidPayload_returnsBadRequestWithFieldErrors() {
        String invalidPayload = """
            {
              "title": "",
              "content": "short",
              "category": "",
              "tags": []
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(invalidPayload, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
            "/api/documents",
            HttpMethod.POST,
            request,
            JsonNode.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("fieldErrors").isArray()).isTrue();
        assertThat(response.getBody().get("fieldErrors").size()).isGreaterThan(0);
        }
}
