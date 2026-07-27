Day 3 – Agent Mode & AI-Led Development
🎯 Day 3 Goal

So far:

Day 1 → How to ask AI
Day 2 → How to govern AI
Day 3 → How to delegate work to AI


The key question today is:

"What work should I delegate to AI, and what work should remain with me as the architect?"

What We Will Build

Instead of adding random features, let's move the product toward its final vision.

Current State

✅ CRUD APIs

✅ DTOs

✅ Service Layer

✅ Controller Layer

✅ Exception Handling

✅ Tests

Day 3 Scope

Build features that users will actually use:

1. Search & Filtering
2. API Review & Optimization
3. Technical Debt Review
4. Agent Self-Review

Create Day 3 Folder
docs/day3/

├── AGENT-TASKS.md
├── AGENT-REVIEW.md
├── TECHNICAL-DEBT.md
├── IMPLEMENTATION-SUMMARY.md
└── AGENT-LEARNINGS.md

Task 1 – Agent as Principal Engineer

Use Agent in Plan Mode.

Prompt:

Act as a Principal Engineer.

Review the current Team Knowledge Assistant codebase.

Identify:

1. Highest value feature to implement next.
2. Technical debt introduced in Day 2.
3. Risks in the current implementation.
4. Recommended Day 3 scope.

Provide a prioritized implementation plan before making changes.

What You Are Learning

Not coding.

You're learning:

AI can act as:

Architect
Reviewer
Planner


before acting as:

Developer


---

# Task 2 – Search Feature

This is our first meaningful user feature.

Prompt:

```text
Design and implement document search.

Requirements:

- Search by title
- Search by category
- Search by tags
- Support combinations of filters
- Support pagination
- Follow existing architecture

Before implementation:
Provide design and API contract.

Architect Review

Before implementation ask:

What are the pros and cons of:

Option A:
Separate search endpoint

GET /documents/search

Option B:
Filter on existing endpoint

GET /documents?category=&tag=&title=

Recommend one approach and justify.

My Recommendation

Use:

GET /api/documents
    ?title=
    &category=
    &tag=


Cleaner MVP.

Task 3 – Agent Self Review

After implementation ask:

Review your implementation as a Staff Engineer.

Provide:

1. Strengths
2. Weaknesses
3. Scalability concerns
4. Future enhancements
5. Technical debt introduced


Store in:

docs/day3/AGENT-REVIEW.md

Task 4 – Technical Debt Analysis

Prompt:

Analyze the entire codebase.

Classify technical debt into:

1. Immediate
2. Near Term
3. Future

For each:

- Description
- Risk
- Recommended Action


Store in:

docs/day3/TECHNICAL-DEBT.md

Most Important Learning Today

Day 3 is NOT:

Agent generates code


Day 3 IS:

Agent proposes
↓
Agent implements
↓
Agent reviews
↓
Human approves


This is how senior engineers use AI.

Success Criteria

By end of Day 3 you should be able to explain:

Prompt Engineering
How to ask AI

Context Engineering
How to prepare AI

Agentic Development
How to delegate tasks and review outcomes

Presentation-Friendly Learning Statement

Add this to your journal:

## Day 3 Biggest Learning

Agent Mode is not about letting AI code autonomously.

The real value comes from using AI in multiple roles:

- Planner
- Architect
- Implementer
- Reviewer

while keeping humans responsible for final technical decisions.

Your first Day 3 action

Run the Principal Engineer Review prompt above and share the output. That review will determine the exact implementation scope for Day 3 and give you strong material for your Day 4 presentation as well.


## Review of endpoints

I’m tightening the search design into a concrete contract now so the implementation can move against a stable API, not another ambiguous list behavior.

Read [](file:///c%3A/Users/SaurabhSuman1/codebase/team-knowledge-assistant/docs/day3/PROMPT-HISTORY.md)

Read [](file:///c%3A/Users/SaurabhSuman1/codebase/team-knowledge-assistant/src/main/resources/application.yml)

Read [](file:///c%3A/Users/SaurabhSuman1/codebase/team-knowledge-assistant/src/test/java/com/teamknowledgeassistant/TeamKnowledgeAssistantApplicationTests.java)

I have the current prompt history and app config. Next I’m recording the concrete search contract in the session plan so the implementation can follow one agreed shape.

Read memory [](file:///c%3A/Users/SaurabhSuman1/AppData/Roaming/Code/User/workspaceStorage/ea33cd4642737ea05600dcca475a7dae/GitHub.copilot-chat/memory-tool/memories/NmE1OWE0MzctMzVmYi00MDRhLWE3N2MtNjRhZWJlMWM5YmQ2/plan.md)

I’m adding the contract details now. The goal is to freeze the list/search semantics, the pagination envelope, and the invalid-sort behavior before any code moves.

Inserted into memory file [](file:///c%3A/Users/SaurabhSuman1/AppData/Roaming/Code/User/workspaceStorage/ea33cd4642737ea05600dcca475a7dae/GitHub.copilot-chat/memory-tool/memories/NmE1OWE0MzctMzVmYi00MDRhLWE3N2MtNjRhZWJlMWM5YmQ2/plan.md)

Here is the recommended design and API contract before implementation.

**Design**
- Keep `GET /api/documents` as the single search/list endpoint.
- Let the controller stay thin: it only accepts query params, validates them, and delegates.
- Move query composition into the service so filter logic is not split between controller and repository.
- Use Spring Data JPA pagination with a composable query mechanism, preferably `JpaSpecificationExecutor`, so `title`, `category`, and `tag` can be combined in one request.
- Keep the existing item DTO, `KnowledgeDocumentResponse`, for now. Add a paged wrapper DTO for the list response instead of introducing a separate summary DTO.
- Normalize sort handling in one place and reject any field outside the approved whitelist.
- Leave the legacy note module out of this contract for now; it is a separate follow-up decision.

**API Contract**
- Endpoint: `GET /api/documents`
- Query params:
  - `title` optional, case-insensitive contains match
  - `category` optional, case-insensitive exact match
  - `tag` optional, case-insensitive exact match against the tag set
  - `page` optional, default 0
  - `size` optional, default 20, with a max cap of 100
  - `sort` optional, repeatable, for example `sort=updatedAt,desc`
- Filter semantics:
  - If more than one filter is present, all supplied filters must match.
  - No hidden precedence rules.
  - Blank values are treated as absent.
- Sort whitelist:
  - `title`
  - `category`
  - `createdAt`
  - `updatedAt`
- Unsupported sort fields:
  - Return HTTP 400 with the existing API error shape.
- Success response:
  - HTTP 200
  - Body should be a page envelope with:
    - `items`: list of `KnowledgeDocumentResponse`
    - `page`
    - `size`
    - `totalElements`
    - `totalPages`
    - `hasNext`
    - `hasPrevious`

**Recommended examples**
- `GET /api/documents?title=runbook&page=0&size=20&sort=updatedAt,desc`
- `GET /api/documents?category=Runbook&tag=deployment`
- `GET /api/documents?title=api&category=Runbook&page=1&size=10`

**Related files**
- KnowledgeDocumentController
- KnowledgeDocumentService
- KnowledgeDocumentRepository
- KnowledgeDocumentResponse
- GlobalExceptionHandler

I also recorded this contract in the session plan at `/memories/session/plan.md`. If you want, I can turn this into the exact implementation steps next.

