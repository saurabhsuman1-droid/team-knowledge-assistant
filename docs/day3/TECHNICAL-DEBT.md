# Day 3 - Technical Debt

Date: 2026-07-23

## Objective

Classify current technical debt by urgency and define concrete paydown actions.

## Immediate

### 1. Unbounded list and search responses

- Description:
  - List APIs currently return unbounded result sets.
  - Document list responses include full content payloads.
- Risk:
  - Increased memory pressure and response latency as data grows.
  - Higher risk of timeout and poor user experience under load.
- Recommended action:
  - Implement pagination with sensible defaults and max-size cap.
  - Introduce paged response metadata.
  - Plan list-vs-detail payload separation in next iteration.

### 2. Filter precedence instead of composable semantics

- Description:
  - Controller applies first-match precedence across category, tag, and title filters.
- Risk:
  - Hidden behavior for clients when multiple filters are provided.
  - Functional mismatch with Day 3 search contract expectations.
- Recommended action:
  - Move filter composition into service/repository query layer.
  - Implement explicit AND semantics for all provided filters.

### 3. Missing search contract governance

- Description:
  - No sort allowlist and no explicit invalid-sort error path.
- Risk:
  - Inconsistent behavior and potential query performance issues.
- Recommended action:
  - Add approved sort-field allowlist.
  - Reject unsupported sort fields with clear 400 response.

## Near Term

### 1. Mapper abstraction not implemented

- Description:
  - Mapping remains coupled to DTO static factory methods and service logic.
- Risk:
  - Duplication and reduced maintainability as models evolve.
- Recommended action:
  - Introduce dedicated mapper component and centralize conversion logic.

### 2. Incomplete test pyramid for current scope

- Description:
  - Document tests exist, but note-module tests and search-v2 behavior tests are missing.
- Risk:
  - Higher regression probability during API evolution.
- Recommended action:
  - Add note controller/service tests.
  - Add search contract tests for combinations, paging, sorting, and error paths.

### 3. Environment profile hardening not separated

- Description:
  - Development-oriented defaults are still in baseline configuration.
- Risk:
  - Operational and security risks if defaults leak into higher environments.
- Recommended action:
  - Split local/dev/test/prod profiles.
  - Disable risky dev-only capabilities outside local profile.

## Future

### 1. Search and indexing strategy for larger datasets

- Description:
  - Case-insensitive contains search can degrade at scale without stronger strategy.
- Risk:
  - Query latency and database cost increase with dataset size.
- Recommended action:
  - Evaluate full-text indexing or dedicated search backend based on growth targets.

### 2. API surface consolidation (notes vs documents)

- Description:
  - Legacy note endpoints coexist with document endpoints without clear lifecycle strategy.
- Risk:
  - Product confusion, duplicated maintenance, and inconsistent standards.
- Recommended action:
  - Decide retain/deprecate/migrate strategy and publish migration timeline.

### 3. Read-model optimization

- Description:
  - Single response shape serves both list and detail use cases.
- Risk:
  - Over-fetching and unnecessary payload transfer at scale.
- Recommended action:
  - Introduce separate summary DTO for list views and detail DTO for full content views.

## Prioritized Paydown Sequence

1. Search API v2 contract completion (pagination, composable filters, sort governance).
2. Test coverage expansion for search behavior and note module parity.
3. Mapper and normalization cleanup.
4. Environment profile hardening.
5. Search/indexing roadmap and API surface consolidation.

## Exit Criteria for Day 3 Debt Closure

1. GET /api/documents supports page, size, sort, and metadata.
2. Multiple filters are composable and deterministic.
3. Unsupported sort fields return explicit client errors.
4. Tests cover happy path and failure path for the new contract.
5. Day 3 documentation set is complete and internally consistent.
