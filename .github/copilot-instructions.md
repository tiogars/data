# GitHub Copilot Instructions

**For comprehensive development guidelines, see:** [Development Instructions](../docs/2-development/instructions/)

This file provides quick reference rules. For detailed guidance by domain:

- [00 - Architecture & Structure](../docs/2-development/instructions/00-architecture.md)
- [01 - Frontend Patterns](../docs/2-development/instructions/01-frontend.md)
- [02 - Backend Patterns](../docs/2-development/instructions/02-backend.md)
- [03 - Testing Conventions](../docs/2-development/instructions/03-testing.md)
- [04 - Documentation Standards](../docs/2-development/instructions/04-documentation.md)
- [05 - Security Practices](../docs/2-development/instructions/05-security.md)
- [06 - Improvement Proposals](../docs/2-development/instructions/06-improvements.md)

---

## Quick Reference: Frontend

### Responsive UI (Mobile-First)
- Content stacks vertically on mobile (xs/sm breakpoints)
- Desktop (md+) uses tables for tabular data
- Mobile uses card/list views with same information
- No horizontal scrolling for core data
- Touch-friendly buttons (greater than 48px)

### Components & State
- Functional components with FC type
- Redux Toolkit for app state + RTK Query for server state
- React Hook Form + MUI for forms
- Props in separate type definition
- MUI sx prop for styling

### Generated APIs & RTK Query
- Generated files in data-web/src/services/\*Api.ts are read-only
- Regenerate after backend API changes:
  pnpm -C data-web run openapi:pull
  pnpm -C data-web run rtk:codegen
- Always use generated hooks

---

## Quick Reference: Backend

### Layering
Controllers (HTTP) -> Services (business logic) -> Repositories (data access)

### Services & Validation
- One service per operation (BrickCreationService, BrickListService, etc.)
- All validation in service layer
- Fail early: throw IllegalArgumentException for validation errors
- Constructor dependency injection required

### Data Access
- Spring Data JPA repositories only
- Entity mapping via ModelMapper (static methods)
- Separate response models and request forms
- All models/forms have @Schema annotations with French descriptions

### OpenAPI Documentation
- @Tag on controllers
- @Operation on endpoints
- @Schema on all model fields with examples

### Gateway Routing (Required)

- For each new backend domain/endpoint (for example `brand`), always verify route exposure in `data-gateway/src/main/java/fr/tiogars/data/gateway/routes/GatewayRoutesConfiguration.java`.
- If missing, add both routes: `/domain` and `/domain/**`.
- During feature implementation, proactively identify and perform this gateway routing update instead of leaving it as a follow-up.

---

## Quick Reference: Testing

### Naming Convention
should\<ExpectedBehavior\>When\<Condition\> or shouldThrow\<Exception\>When\<Condition\>

### Backend Tests
- @SpringBootTest with full context
- MockMvc for API testing
- Mock repositories for unit tests
- Clean database in @BeforeEach

### Frontend Tests
- @testing-library/react
- Query by role/label (not implementation)
- Use userEvent for interactions
- Test accessibility

### Coverage Targets
- >= 80% for services
- >= 70% overall

---

## Quick Reference: Documentation

### Code Documentation
- Javadoc on all public classes/methods (French)
- Comments explain why, not what
- Inline comments for complex business logic

### OpenAPI
- All endpoints documented via annotations
- All models have @Schema with description + examples
- Descriptions in French (user-facing)

### User Documentation
- MkDocs in docs/1-features/
- Step-by-step instructions with screenshots
- Troubleshooting section

---

## Quick Reference: Security

- Input Validation: Service layer, fail fast
- Parameterized Queries: Spring Data JPA (never string concatenation)
- XSS Prevention: React escapes by default, avoid dangerouslySetInnerHTML
- Authentication: OAuth2 at gateway (JWT tokens)
- Rate-Limiting: Bucket4j on gateway
- Logging: Never log passwords, tokens, sensitive data

---

## Development Workflow: Adding a Feature

### Phase 1: Backend
1. Create entities, repositories, services
2. Add OpenAPI annotations
3. Write integration tests
4. Verify and update GatewayRoutesConfiguration for the new domain routes (`/domain` and `/domain/**`)

### Phase 2: API Generation
pnpm -C data-web run openapi:pull
pnpm -C data-web run rtk:codegen

### Phase 3: Frontend
1. Create pages/components using generated APIs
2. Ensure responsive design (mobile-first)
3. Write component tests

### Phase 4: Documentation & Validation
1. Update MkDocs if user-facing
2. Run full test suite
3. Create PR

---

## Environment & Commands

- docker compose up --build --watch (Start entire stack)
- mvn clean test (Run backend tests)
- pnpm -C data-web test (Run frontend tests)

### Ports
- Frontend: http://localhost:5173
- Gateway: http://localhost:8081
- API: http://localhost:8080
- MkDocs: http://localhost:8000/data/

---

## Original Rules

### Responsive UI Default Principle

For all web UI features, responsive behavior is required by default.

- Mobile-first layout: content should stack vertically and remain easy to scan and use.
- Desktop/tablet: use tables for dense tabular data.
- Mobile: replace tables with list/card views containing the same essential information.
- Never rely on horizontal scrolling as the primary way to access core data on mobile.
- Keep actions and key metadata available in both desktop and mobile representations.

### Implementation Notes
- Prefer explicit breakpoint-driven rendering (for example: desktop component + mobile component).
- Maintain feature parity between desktop table view and mobile list view.
- Preserve accessibility (readable labels, clear hierarchy, touch-friendly targets).

### API Generation Rules

For data-web API services generated from OpenAPI:

- Always regenerate API services from the OpenAPI spec instead of manually editing generated service files.
- Generated files must be treated as read-only.
- Primary generated target in this repository: data-web/src/services/sectionApi.ts.
- If API contracts change on the server, run generation scripts from data-web:
    - pnpm run openapi:pull
    - pnpm run rtk:codegen:section
    - or pnpm run generate:section-api
- If a change is needed in generated output, update the source of truth first (backend OpenAPI contract and/or codegen config), then regenerate.
