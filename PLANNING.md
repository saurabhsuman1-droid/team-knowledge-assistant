Microsoft 365 copilot role will be:

🎯 Program Mentor
🏗️ Solution Architect
🤖 GitHub Copilot Coach
👨‍🏫 Daily Reviewer
📝 Jury Preparation Partner

GOAL:

Become an engineer who can lead AI-native software development teams.

# AEP Java AI Specialist Capstone
## Team Knowledge Assistant (AI-Powered Engineering Knowledge Platform)

Author: Saurabh Suman
Role: Lead Software Engineer

---

# Project Vision

Engineering teams generate large amounts of knowledge:

- Architecture decisions
- Coding guidelines
- Production runbooks
- Deployment instructions
- Incident SOPs
- Release processes
- Team documentation

Most of this information becomes scattered across:
- Teams chats
- Wikis
- Emails
- PDFs
- Repositories

As a result:
- Knowledge is difficult to discover
- New team members onboard slowly
- Teams repeatedly ask the same questions
- Critical information gets lost

This project creates an AI-powered Team Knowledge Assistant that centralizes engineering knowledge and enables:

- Knowledge Management
- Intelligent Search
- AI-powered Summaries
- Related Content Discovery
- Conversational Team Knowledge Chat

---

# Business Problem

How can engineering teams quickly find accurate information without searching through multiple tools and documents?

---

# Solution

A Spring Boot-based knowledge platform that allows:

1. Storing team knowledge documents
2. Categorizing and tagging content
3. Searching relevant information
4. Discovering related content
5. Generating AI summaries
6. Chatting with stored knowledge

---

# Learning Objectives

This project is designed to demonstrate the complete AEP Java AI Specialist journey:

## Module 1
AI-Assisted Coding & Prompt Engineering

Learn:
- Prompt design
- Code generation
- Iterative prompting

Deliverables:
- Entity Layer
- Repository Layer
- Project Scaffolding

---

## Module 2
Context Engineering

Learn:
- Copilot instructions
- Coding conventions
- Architectural guidance

Deliverables:
- DTOs
- Services
- Controllers
- Exception Handling

---

## Module 3
Agentic Development

Learn:
- Agent Mode
- Multi-step development
- Human review workflows

Deliverables:
- Pagination
- Filtering
- Validation
- Unit Tests

---

## Module 4
MCP Integration

Learn:
- GitHub MCP
- Issue-driven development
- External context integration

Deliverables:
- Feature implementation through GitHub Issues

---

## Module 5
Multi-Agent Workflows

Learn:
- Planner Agent
- Implementer Agent
- Reviewer Agent

Deliverables:
- AI-reviewed feature implementation

---

# High-Level Architecture

User
 |
 v
Frontend (Optional)
 |
 v
Spring Boot REST API
 |
 +---- Search Service
 |
 +---- Knowledge Service
 |
 +---- AI Service
 |
 v
Database

Optional:
Azure OpenAI / Ollama

---

# MVP Features

## Document Management

Create Document

Update Document

Delete Document

View Document

---

## Search

Keyword Search

Category Search

Tag Search

Pagination

---

## Related Content

Recommend similar documents

---

## AI Summary

Generate concise summaries

---

## AI Chat

Ask questions based on available knowledge

---

# Technology Stack

Java 21

Spring Boot 3

Spring Data JPA

PostgreSQL (or H2 initially)

Lombok

Maven

JUnit 5

GitHub Copilot

GitHub MCP

Azure OpenAI (Optional)

Swagger/OpenAPI

---

# Success Criteria

A user can:

- Create knowledge documents
- Search documents
- Retrieve related information
- Generate summaries
- Ask questions via AI chat

The application demonstrates all required AEP Java AI Specialist competencies.

---

# Capstone Outcome

Build and demonstrate a working AI-native engineering productivity solution while showcasing:

- Prompt Engineering
- Context Engineering
- Agentic Development
- MCP Integration
- Multi-Agent Orchestration

Badge Target:
AI Specialist

Learning Plan (Optimized for Maximum Growth)
Week 1 Focus

Don't think:

"How do I build an application?"

Think:

"How do I make Copilot build with me?"

Day 0 (Today)
Goal

Environment + Mindset

Install
Java 21
IntelliJ Ultimate / VS Code
GitHub Copilot
GitHub Copilot Chat
GitHub MCP
Learn

Watch:

GitHub Copilot Fundamentals:

https://learn.microsoft.com/training/modules/introduction-to-github-copilot/

GitHub Copilot Chat:

https://learn.microsoft.com/training/modules/generate-documentation-using-github-copilot-tools/

Outcome

Repository created.

Day 1
Topic

Prompt Engineering

Learn

Study:

https://docs.github.com/en/copilot/using-github-copilot/prompt-engineering-for-github-copilot  
https://github.blog/developer-skills/github/how-to-write-better-prompts-for-github-copilot/?ref_product=copilot&ref_type=engagement&ref_style=text   
https://github.blog/engineering/prompt-engineering-guide-generative-ai-llms/  
https://github.blog/developer-skills/github/prompting-github-copilot-chat-to-become-your-personal-ai-assistant-for-accessibility/?ref_product=copilot&ref_type=engagement&ref_style=text

Practice

Generate:

Spring Boot Project
Entity
Repository
Skills

Learn difference between:

Bad Prompt

Create a document entity.


Good Prompt

Create a Spring Boot JPA entity named KnowledgeDocument.

Requirements:
- UUID id
- title
- content
- category
- tags
- createdAt

Use:
- Lombok
- JPA annotations
- validation annotations

Follow clean code principles.

Deliverable

First commit.

Day 2
Topic

Context Engineering

Learn

Understand:

"What does AI see?"

Create
.github/copilot-instructions.md


This may become the MOST important artifact in the program.

Deliverable

Service Layer

Controller Layer

DTO Layer

Day 3
Topic

Agent Mode

Goal

Let Copilot perform a complete feature.

Example:

Add pagination, sorting,
validation and tests for
the search API.


Observe:

Good decisions
Bad decisions
Missing decisions
Deliverable

Feature implemented through Agent Mode.

Day 4
Topic

MCP

Learn

Issue Driven Development

Create issues.

Example:

Add document tags

Add related content search

Add search by category


Allow MCP to work from issue context.

Deliverable

Issue-linked commits.

Day 5
Topic

Multi-Agent

Flow

Planner

↓

Implementer

↓

Reviewer

Deliverable

Reviewed feature with findings.

Week 2 (Capstone Differentiator)

Now we add AI.

Feature 1

AI Summary

Summarize document

Feature 2

Related Knowledge

Show related documents

Feature 3

Knowledge Chat

How do we deploy a hotfix?


AI answers using stored documents.

Capstone Demo Story

Do NOT start with technology.

Start with pain.

Problem
Knowledge is scattered and difficult to find.

Solution
AI-Powered Team Knowledge Assistant

Demo
Create document
Search document
Related documents
AI summary
Ask AI question
Learning Journey

Explain:

Prompt Engineering
Context Engineering
Agent Mode
MCP
Multi-Agent
Mentor Rules (How We'll Work)

Every day, send me:

Day X Progress

Completed:
...

Issues:
...

Questions:
...

GitHub Link / Screenshots


I'll review as:

Architect
Reviewer
Jury Member
AI Specialist Mentor

and guide the next day's work.

Your First Assignment (Today)

Complete these before writing any code:

1. Create GitHub Repository
team-knowledge-assistant

2. Create README.md

Use the template above.

3. Install and verify
GitHub Copilot
Copilot Chat
Agent Mode
4. Create a note called
AI-Learning-Journal.md


Every day capture:

What prompt worked?

What failed?

What surprised me?

What would I do differently?