# Day 1 Prompt Learnings (Better prompts → Better code)

## Observations
- Prompt clarity matters: explicitly listing Java version, build tool, and exact Spring starters made scaffold generation deterministic.
- Asking for standard Maven structure reduced ambiguity and resulted in conventional Spring Boot layout under src/main and src/test.
- Including documentation as a requirement ensured project setup and process learning are captured together.
- Springdoc OpenAPI should be called out explicitly in prompts because it is not part of the default Spring Boot starter set.

## What Was Created
- Spring Boot 3.5.x Maven project baseline.
- Java 21 configuration in Maven properties.
- Dependencies: Spring Web, Spring Data JPA, H2, Lombok, Validation, Springdoc OpenAPI.
- Basic CRUD API slice for knowledge notes to prove JPA + validation wiring.

## Next Prompt Improvements
- Mention preferred package name explicitly when naming conventions matter.
- Specify whether you want only scaffolding or sample domain endpoints as part of bootstrap.
- Add required quality gates in prompt, for example: build success, smoke endpoint check, and OpenAPI URL verification.

## Additional Observations (KnowledgeDocument Prompt)
- A prompt that specifies field types clearly (for example UUID and Set<String>) prevents common ORM mapping ambiguity.
- For collection fields in JPA entities, Set<String> requires explicit @ElementCollection mapping to remain portable and clean.
- Timestamp fields are most maintainable when lifecycle hooks (@PrePersist and @PreUpdate) are defined at entity level.
- Requesting clean code with validation is most effective when validation is declared near field definitions so data rules are self-documenting.

## Prompt Comparison: Initial vs Enterprise Refactor

### Which Prompt Generated Better Code?
- The enterprise refactor prompt generated better code for production use.

### What Changed?
- Primary key generation moved from provider-specific UUID generation to JPA UUID strategy with explicit UUID column definition.
- Audit timestamps were extracted into a reusable base class and wired via Spring Data JPA auditing.
- Added optimistic locking with a version field in the shared auditable base for safer concurrent updates.
- Replaced broad Lombok @Data with targeted annotations to avoid risky entity equality/toString side effects.
- Content column was upgraded to LOB/CLOB for larger payload support and more realistic persistence behavior.
- Tags collection changed to LAZY loading and now enforces uniqueness per document through table constraint.
- Added table indexes for category and audit timestamps to support query scalability.
- Added stricter validation constraints (minimum/maximum lengths and category pattern) aligned to API/data quality standards.
- Added small domain methods (addTag/removeTag) to keep tag mutation behavior encapsulated and extensible.
