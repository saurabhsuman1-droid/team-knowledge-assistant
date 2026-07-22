# Day 1 Learnings

## Best Prompt

Refactor the KnowledgeDocument entity to follow enterprise standards.

Requirements:
- UUID primary key
- Audit timestamps
- Validation constraints
- Meaningful column definitions
- Extensible design
- Production-ready implementation

Compare both outputs.

Record:
Which prompt generated better code?
What changed?

You can record in PROMPT LEARNING as new section or create a new file in day1

## Prompt -> Output Change Flow

Prompt 1 (basic entity prompt):
- Output had the required fields and basic JPA mapping.
- Good for initial scaffolding and quick iteration.

Prompt 2 (enterprise standards prompt):
- Output became production-oriented: auditing base class, stronger column definitions, indexing, safer entity design, and better extensibility.
- Output quality improved because constraints were explicit and architecture intent was clear.

## What Worked Well

- Writing explicit non-functional expectations (production-ready, extensible) improved architecture decisions.
- Asking for comparison forced a measurable before/after quality review.
- Domain constraints in prompts produced better validation and persistence mappings.

## What Did Not Work

- Vague phrasing like "clean code" alone was not enough to guarantee enterprise-grade implementation.
- Local run check failed on default port due to an existing process on port 8080, which looked like a config issue but was an environment conflict.

## Surprising Observation

- A single line such as "compare both outputs" significantly improved learning value by making quality differences explicit.

## Improvements

- Add explicit acceptance criteria in every prompt:
  - startup success
  - schema quality checks
  - repository/query expectations
- Include operational constraints in prompt (port strategy, local run command, expected endpoints).
- Ask for tradeoff notes (performance, maintainability, extensibility) in output.

## Copilot Rating

8.5/10

GOAL - Prompt engineering

Today's success is:
I learned how prompt quality directly impacts code quality.
