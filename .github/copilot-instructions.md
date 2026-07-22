# Team Knowledge Assistant - Copilot Instructions

## Tech Stack

- Java 21
- Spring Boot 3
- Maven
- Spring Data JPA
- H2/PostgreSQL

## Coding Standards

- Use constructor injection only
- Do not use field injection
- Follow layered architecture
- Controllers should contain no business logic
- Services should contain business logic
- Repositories should only contain persistence logic
- Use DTOs for API contracts
- Use Global Exception Handling
- Follow SOLID principles
- Prefer composition over inheritance

## API Standards

- Use RESTful naming
- Return appropriate HTTP status codes
- Validate all request payloads
- Include OpenAPI annotations where applicable

## Testing

- Write JUnit 5 tests
- Mock external dependencies
- Keep tests readable

## Documentation

- Add JavaDoc for public APIs
- Use meaningful method names