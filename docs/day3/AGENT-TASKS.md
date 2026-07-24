# Day 3 - Prioritized Implementation Plan

## Objective

Close the highest-value gaps left by Day 2 by upgrading the document API to support controlled search and list behavior, then pay down the technical debt introduced by the initial implementation.

---

## Planning Principles

1. Ship the highest business value first.
2. Remove the broadest technical risk first.
3. Keep the scope aligned to the Day 2 architecture, not a new redesign.
4. Prefer small, verifiable changes with tests at each step.

---

## Highest-Value Feature To Implement Next

### Search API v2

Implement pageable, sortable list retrieval for knowledge documents with explicit filter semantics.

Why this comes first:

- The current list endpoint returns an unbounded full payload.
- Filtering is precedence-based instead of composable.
- Day 2 already committed to pagination and sort governance, but the implementation did not deliver it.
- This change reduces scalability risk and gives the API a production-ready list contract.

Expected behavior:

- Support `page`, `size`, and `sort` on `GET /api/documents`.
- Reject unsupported sort fields.
- Support combining filters instead of silently ignoring later criteria.
- Return a stable paged response shape with metadata.

---

## Day 2 Technical Debt To Pay Down

1. Missing mapper abstraction.
2. Controller-owned filtering precedence.
3. Unbounded list responses.
4. Custom error envelope instead of a clearly governed contract.
5. Incomplete tag normalization.

---

## Risks In The Current Implementation

1. Large response payloads may grow quickly as documents accumulate.
2. Filter precedence can hide user intent when multiple query params are supplied.
3. Sort behavior is not yet constrained, so future changes can become inconsistent.
4. Mapping logic is split across service and DTO factory methods.
5. Legacy note APIs still exist alongside the new document APIs, which increases surface area and confusion.

---

## Recommended Day 3 Scope

### P0

1. Add pageable document list support.
2. Enforce sort-field whitelist.
3. Define composable filter semantics for category, tag, and title.
4. Add controller and service tests for paging, sorting, and filter combinations.

### P1

5. Introduce a dedicated mapper for entity-to-DTO and DTO-to-entity conversion.
6. Normalize tag values consistently during write operations.
7. Tighten exception handling for invalid query contract inputs.

### P2

8. Decide whether the legacy note API should be deprecated, retained, or migrated.
9. Update documentation to explain the relationship between notes and documents.

---

## Execution Plan

### Phase 1 - Search API Foundation

1. Extend the document list contract to accept pagination and sort parameters.
2. Move query decision logic out of the controller and into a dedicated service path.
3. Add validation for unsupported sort fields and malformed pagination inputs.
4. Preserve deterministic ordering in list responses.

### Phase 2 - Mapper And Normalization

5. Create a mapper abstraction for document conversion.
6. Centralize tag cleanup rules so write operations behave consistently.
7. Remove direct mapping duplication from the service layer.

### Phase 3 - Test Coverage

8. Add service tests for paging, sorting, and combined filter behavior.
9. Add controller tests for request validation and response metadata.
10. Verify error handling for unsupported sort fields and invalid request payloads.

### Phase 4 - Surface Consolidation

11. Review the remaining note API for overlap and lifecycle strategy.
12. Capture the outcome in the Day 3 docs so the next iteration has a clear direction.

---

## Acceptance Criteria

1. `GET /api/documents` supports pagination and sorting.
2. Unsupported sort fields are rejected with a clear client error.
3. Multiple filters no longer rely on hidden precedence rules.
4. Mapping logic is isolated from business logic.
5. Tests cover the new contract and the failure paths introduced by the change.

---

## Out Of Scope For Day 3

1. Major redesign of the API surface.
2. Separate list and detail DTOs.
3. Optimistic locking workflows beyond the existing conflict handling.
4. ETag or If-Match support.
5. Broader refactor of the legacy note module unless it blocks the document roadmap.

---

## Recommended Order Of Work

1. Search API v2.
2. Mapper extraction.
3. Tag normalization.
4. Validation and exception polish.
5. Tests.
6. Legacy note API decision.
