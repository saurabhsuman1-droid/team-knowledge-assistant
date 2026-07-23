# Day 2 - Context Engineering Learnings

## Goal

Understand how providing project context, coding standards, and architectural expectations improves AI-generated outcomes.

---

## Biggest Observation

Day 1 focused on improving prompts.

Day 2 revealed that context has an even bigger impact than prompt wording.

When Copilot was provided with:

- project structure
- coding standards
- architecture guidelines
- implementation patterns

it generated significantly more consistent and maintainable solutions.

---

## Context Engineering vs Prompt Engineering

### Prompt Engineering

Focuses on:

"What should AI do?"

Example:

Create a CRUD API for KnowledgeDocument.

### Context Engineering

Focuses on:

"What should AI know before doing it?"

Example:

- Use constructor injection
- Follow layered architecture
- Use DTOs
- Add global exception handling
- Follow SOLID principles

The latter produced noticeably better architectural decisions.

---

## What Worked Well

### Planning Before Coding

Requesting an implementation plan before code generation:

- Increased visibility
- Exposed assumptions
- Reduced surprises

### Architecture Review

Having AI review its own proposal as a Principal Engineer surfaced:

- scalability concerns
- testing gaps
- API design issues
- future maintenance risks

### Explicit Decision Making

The implementation improved whenever decisions were made explicitly.

Examples:

- PUT only
- Include exception handling
- Add pagination
- Define sorting strategy

---

## What Did Not Work

### Direct Code Generation

Generating code without reviewing the plan first made it difficult to understand:

- what changed
- why it changed
- whether architectural standards were followed

### Implicit Assumptions

When assumptions were not discussed, AI introduced its own interpretations.

Examples:

- Custom error contract
- Search behavior
- Mapping strategy

---

## Most Valuable Workflow Learned

Old workflow:

Prompt
↓
Code

New workflow:

Prompt
↓
Context
↓
Plan
↓
Review
↓
Implementation
↓
Diff Analysis
↓
Learning

This workflow provides much greater control over AI-generated development.

---

## Architect-Level Insights

### AI Should Be Reviewed Like a Developer

Generated code should be treated similarly to a pull request.

Questions to ask:

- Why this design?
- What alternatives were considered?
- What assumptions were made?
- What future risks exist?

### Deviations Matter More Than Generated Code

Reviewing:

Plan vs Implementation

provided more learning than reading the generated files individually.

---

## Biggest Insight

The highest value did not come from code generation.

The highest value came from:

- reviewing plans
- identifying assumptions
- challenging design decisions
- comparing implementation against intended architecture

This transformed Copilot from a code generator into an engineering collaborator.

---

## Copilot Rating

### Planning Capability

9/10

### Architecture Assistance

9/10

### Code Generation

8.5/10

### Review Capability

9.5/10

---

## Day 2 Success Statement

I learned that Context Engineering is the practice of shaping the information and standards available to AI before implementation begins.

Effective AI-assisted development is:

Plan → Review → Implement → Validate

rather than

Prompt → Generate Code.