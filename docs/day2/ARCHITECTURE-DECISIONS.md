# Day 2 - Architecture Decisions

## Purpose

This document captures key architectural decisions made during Day 2 and the rationale behind them.

---

# ADR-001: Use DTOs at API Boundary

## Decision

Controllers will use DTOs for all request and response models.

## Reason

- Prevents exposing JPA entities directly.
- Decouples persistence model from API contract.
- Allows future API evolution without entity changes.

## Benefits

- Better maintainability
- Better security
- Clear separation of concerns

---

# ADR-002: Layered Architecture

## Decision

Use the following structure:

Controller
↓
Service
↓
Repository

## Reason

Keep responsibilities separate.

### Controller

- Request validation
- HTTP concerns
- Response generation

### Service

- Business logic
- Orchestration
- Domain rules

### Repository

- Persistence only

## Benefits

- Easier testing
- Better maintainability
- Aligns with enterprise Spring Boot practices

---

# ADR-003: Constructor Injection Only

## Decision

Use constructor injection for all dependencies.

## Reason

- Encourages immutability
- Improves testability
- Makes dependencies explicit

## Benefits

- Cleaner design
- Easier unit testing

---

# ADR-004: PUT Only Updates

## Decision

Support PUT for updates.

PATCH is deferred.

## Reason

- Simpler API surface
- Faster MVP delivery
- Lower implementation complexity

## Trade-Off

Partial updates are not supported.

## Future Consideration

Introduce PATCH API if business requirements demand partial updates.

---

# ADR-005: Global Exception Handling

## Decision

Centralize exception handling.

## Reason

Avoid duplicate error handling logic.

## Benefits

- Consistent API responses
- Reduced boilerplate
- Easier maintenance

---

# ADR-006: Pagination and Sorting

## Decision

Support pagination and sorting for list APIs.

## Reason

Production applications should not return unbounded data sets.

## Benefits

- Better performance
- Scalable API design
- Improved user experience

---

# ADR-007: Sort Field Governance

## Decision

Allow sorting only on approved fields.

Approved fields:

- title
- category
- createdAt
- updatedAt

## Reason

Prevent invalid queries and expensive database operations.

## Benefits

- Predictable behavior
- Better performance
- Easier support

---

# ADR-008: ResourceNotFoundException

## Decision

Introduce a dedicated exception for missing resources.

## Reason

Clearly separate business errors from technical failures.

## Benefits

- Cleaner service code
- Clear HTTP 404 mapping
- Consistent error handling

---

# ADR-009: Auditing Support

## Decision

Use JPA auditing.

## Reason

Track lifecycle information automatically.

Tracked fields:

- createdAt
- updatedAt

## Benefits

- Operational visibility
- Improved troubleshooting
- Better data governance

---

# Deferred Decisions

These were intentionally postponed to keep the MVP focused.

## D-001: Separate List and Detail Response Models

Reason:

Added complexity without immediate business value.

Future enhancement.

---

## D-002: Optimistic Locking Strategy

Options considered:

- Version-based updates
- ETag / If-Match

Reason for deferral:

Not required for MVP.

Future enhancement.

---

## D-003: Advanced Search API

Options considered:

- Full text search
- Combined filtering
- Relevance ranking

Reason for deferral:

Will be considered during AI Search implementation.

---

# Key Learning

The most valuable outcome of Day 2 was not code generation.

It was learning to:

1. Capture architectural decisions.
2. Challenge AI-generated designs.
3. Review assumptions.
4. Maintain a clear record of technical trade-offs.

This transformed AI from a code generator into an architecture collaborator.