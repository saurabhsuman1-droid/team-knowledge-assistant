# Day 3 - Feature Comparison

Date: 2026-07-23

## Objective

Compare expected feature scope against the implemented behavior.

## Comparison Table

| Feature | Expected | Actual | Gap | Notes |
| --- | --- | --- | --- | --- |
| Search by title | Search by title on document list endpoint | Implemented via title filter | Partial | Works, but remains unpaged and returns full document payload in lists |
| Search by category | Search by category filter | Implemented via category filter | Partial | Current semantics are not composable when multiple filters are supplied |
| Search by tags | Search by tag filter | Implemented via tag query | Partial | Works for single filter usage; composition behavior is not explicit in API contract |
| Combination of filters | Allow title + category + tag combinations | Not implemented as composable AND logic | High | Controller applies precedence logic and short-circuits at first non-blank filter |
| Pagination | Page, size, and metadata for list/search | Not implemented | High | No page envelope, no total count metadata, no bounded default size |
| Sort governance | Allowlist for sort fields and clear validation errors | Not implemented | High | No explicit sort contract or invalid-sort rejection path |
| Mapper abstraction | Dedicated mapper to isolate conversion logic | Not implemented | Medium | Mapping remains coupled to DTO static factory and service flow |
| Validation and error consistency | Structured validation and error responses | Implemented | Low | Global exception handling is present and reusable |
| Test coverage for Day 3 scope | Tests for search combinations, paging, sorting, and note consistency | Partial | High | Document unit/web tests exist; note module and search v2 behavior coverage missing |
| Day 3 artifact closure | Completed Day 3 planning/review/debt documentation | Partial | Medium | AGENT-REVIEW is now filled; FEATURE-COMPARISON and TECHNICAL-DEBT needed completion |

## Summary

Day 3 intent and execution are directionally aligned, but not complete against the planned search contract.

What is aligned:
- Document APIs include basic filter capabilities.
- Core architecture patterns remain layered and maintainable.
- Exception handling and validation patterns are in place.

Where alignment is incomplete:
- Search API v2 contract (pagination, composable filters, sort governance) was planned but not delivered.
- Mapping and normalization paydown items were identified but not implemented.
- Test coverage has not expanded to validate the Day 3 search contract or note module parity.

## Recommended Actions

1. Implement Search API v2 on GET /api/documents with page, size, sort, and metadata response envelope.
2. Replace filter precedence with composable AND semantics for title, category, and tag.
3. Add sort-field allowlist validation with explicit 400 error behavior for unsupported fields.
4. Add controller and service tests for paging, sorting, and filter combinations.
5. Introduce mapper abstraction and central tag normalization as immediate cleanup after search contract delivery.
