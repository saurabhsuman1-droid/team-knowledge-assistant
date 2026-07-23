# Day 2 - Implementation Plan

## Objective

Build a production-oriented DTO-based CRUD API for KnowledgeDocument while applying Context Engineering principles and project-wide coding standards defined in `.github/copilot-instructions.md`.

---

## Scope

Implement:

- DTO-based API contracts
- Service layer
- Controller layer
- Global exception handling
- Validation handling
- Automated tests
- OpenAPI documentation

---

## Files To Create

### Document Module

1. CreateKnowledgeDocumentRequest.java
2. UpdateKnowledgeDocumentRequest.java
3. KnowledgeDocumentResponse.java
4. KnowledgeDocumentMapper.java
5. KnowledgeDocumentService.java
6. KnowledgeDocumentController.java

### Common Module

1. ResourceNotFoundException.java
2. GlobalExceptionHandler.java

### Tests

1. KnowledgeDocumentServiceTest.java
2. KnowledgeDocumentControllerTest.java

---

## Architecture Decisions

### API Boundary

- Controllers must never expose entities.
- DTOs are used for all request and response contracts.

### Layered Architecture

Controller
↓
Service
↓
Repository

Responsibilities:

- Controller → Request handling and validation
- Service → Business logic
- Repository → Persistence operations

### Update Strategy

- PUT only
- Full replacement of mutable fields
- No PATCH support in MVP

### Exception Strategy

- Global exception handling
- Consistent API error contract
- Resource not found mapped to HTTP 404
- Validation failures mapped to HTTP 400

### Sorting

Allowed sortable fields:

- title
- category
- createdAt
- updatedAt

Any unsupported field should be rejected.

---

## Accepted Recommendations

### Included

- ResourceNotFoundException
- Global Exception Handler
- Pagination and sorting
- Minimal automated tests
- Sort field validation

### Deferred

- Separate List and Detail response DTOs
- Optimistic locking conflict handling
- Version-aware updates
- ETag / If-Match support

These are considered future enhancements.

---

## Risks

### Tag Normalization

Potential risk of inconsistent tag storage.

Mitigation:

- Normalize tags during DTO-to-Entity mapping.
- Remove duplicates.
- Trim values.

### API Contract Consistency

Global exception handling may affect future modules.

Mitigation:

- Keep error structure consistent across all APIs.

---

## Implementation Sequence

### Phase 1

Create DTOs

- CreateKnowledgeDocumentRequest
- UpdateKnowledgeDocumentRequest
- KnowledgeDocumentResponse

### Phase 2

Create business layer

- Service
- Mapper
- ResourceNotFoundException

### Phase 3

Create REST API

- POST /api/documents
- GET /api/documents
- GET /api/documents/{id}
- PUT /api/documents/{id}
- DELETE /api/documents/{id}

### Phase 4

Add exception handling

- Validation
- Not found
- Generic failures

### Phase 5

Testing

- Service tests
- Controller tests
- Validation tests

### Phase 6

Manual verification

- Swagger UI
- CRUD operations
- Error handling

---

## Expected Learning Outcome

Learn how:

1. AI uses project context
2. Architecture standards influence generated code
3. Planning reduces implementation errors
4. Human review improves AI generated solutions