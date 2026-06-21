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
- Data list pages must provide print support for both filtered results and full results
- Data entry pages and list pages must provide blank form printing for paper-based pre-entry data collection

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
- Always run `openapi:pull` before `rtk:codegen` so generated services match the latest backend contract.
- Ensure each managed domain API file is declared in `data-web/openapi-config.ts` under `outputFiles` (for example `androidApi.ts`), otherwise it will not be regenerated.
- If a generated endpoint is incorrect, fix the source of truth first (backend OpenAPI annotations/spec or `openapi-config.ts`), then regenerate. Do not patch generated files manually.
- Always use generated hooks

---

## Quick Reference: Mobile (Flutter Android)

### Architecture & State
- Structure mobile features by domain in `flutter_application/lib/features/`
- Prefer layered architecture: Presentation -> Domain -> Data
- Keep shared cross-feature concerns in `flutter_application/lib/core/` (API, SQLite, sync)

### Local Storage & Sync
- Use SQLite for local persistence
- Implement offline-first writes via a local sync queue
- Apply conflict resolution policy consistently (server wins by default)
- Sync domains targeted in mobile scope: GTIN, Car, CarMileage, Android apps

### API Integration
- Mobile app consumes gateway endpoints exposed in `data-gateway`
- Reuse backend domain contracts (`/gtin`, `/car`, `/car-mileage`, `/android`)
- Keep authentication aligned with gateway JWT security

### Mobile Testing
- Unit tests for repositories/use cases
- Widget tests for critical screens
- Integration tests for offline -> online synchronization flows

### Detail-First Navigation (Required)
- List row taps must navigate to a read-only detail page, not directly to edit/form.
- Each detail page displays all key attributes of the record.
- The detail page exposes explicit mutation actions: **Modifier** (opens form) and **Supprimer** (confirmation dialog then offline delete).
- Domain-specific secondary actions (e.g., viewing mileage entries from a car detail) belong on the detail page.
- After a mutation action completes and pops the detail page, the list reloads.
- List pages use `trailing: const Icon(Icons.chevron_right)` to signal navigability.

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

### List Printing Support (Required)

- For each domain with list pages, implement backend print support for both `filtered` and `all` modes.
- Print API must accept list filters/sort parameters to keep frontend and backend results aligned.
- Print response should include metadata (`generatedAt`, `total`) for print headers/footers.

### Form Printing Support (Required)

- For each domain with data entry, provide blank paper forms for pre-entry data collection (no API required).
- Two form types must be supported:
  - **Unitaire**: a single-record blank form listing all fields with write lines
  - **Listing**: a blank table with column headers and empty rows for batch entry
- Both are generated client-side as HTML and opened in a new tab for browser printing.
- Forms must include a title, domain name, and print date in the header.
- Default listing form row count: 20 rows.

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
5. For list pages, add print API support (`filtered` and `all`) and validate filter inputs

### Phase 2: API Generation
pnpm -C data-web run openapi:pull
pnpm -C data-web run rtk:codegen

### Phase 3: Frontend
1. Create pages/components using generated APIs
2. Ensure responsive design (mobile-first)
3. Write component tests
4. Add list print UX with two modes: print filtered results and print all
5. Add form print UX with two modes: unit (single-record blank form) and listing (blank batch-entry table)

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
- For list pages, include a print action that supports both filtered results and full results.

### Implementation Notes
- Prefer explicit breakpoint-driven rendering (for example: desktop component + mobile component).
- Maintain feature parity between desktop table view and mobile list view.
- Preserve accessibility (readable labels, clear hierarchy, touch-friendly targets).

### API Generation Rules

For data-web API services generated from OpenAPI:

- Always regenerate API services from the OpenAPI spec instead of manually editing generated service files.
- Generated files must be treated as read-only.
- Primary generated target in this repository: data-web/src/services/sectionApi.ts.
- Standard sequence is mandatory: first `pnpm -C data-web run openapi:pull`, then `pnpm -C data-web run rtk:codegen`.
- Every domain service expected to be generated must be listed in `data-web/openapi-config.ts` (`outputFiles`). If a domain is missing from that list, add it before running codegen.
- If API contracts change on the server, run generation scripts from data-web:
    - pnpm run openapi:pull
    - pnpm run rtk:codegen:section
    - or pnpm run generate:section-api
- If a change is needed in generated output, update the source of truth first (backend OpenAPI contract and/or codegen config), then regenerate.
