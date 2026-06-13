# Development Instructions & Code Standards

This section documents the principles, patterns, and rules that guide feature development across the **data** project. These instructions target both human developers and GitHub Copilot to ensure consistent, high-quality implementations.

## Quick Navigation

| Section | Purpose |
|---------|---------|
| [Introduction](#general-principles) | Core principles & objectives |
| [00 - Architecture & Structure](./00-architecture.md) | How the codebase is organized |
| [01 - Frontend Patterns](./01-frontend.md) | React, MUI, state management conventions |
| [02 - Backend Patterns](./02-backend.md) | Spring Boot, services, repositories patterns |
| [03 - Testing Conventions](./03-testing.md) | Test organization, naming, strategies |
| [04 - Documentation Standards](./04-documentation.md) | Javadoc, comments, OpenAPI, user docs |
| [05 - Security Practices](./05-security.md) | Validation, authentication, authorization |
| [06 - Improvement Proposals](./06-improvements.md) | Future enhancements & evolution opportunities |
| [07 - Mobile Flutter](./07-mobile-flutter.md) | Android architecture, local persistence, server sync |
| [P2 Migration History (CSV)](./06-improvements.md#migration-history) | Consolidation status for shared CsvSupport migration |

## General Principles

### 1. **Responsive by Default**
All web UI features must be responsive. Mobile-first layout with content stacking vertically; desktop views use tables for dense data; mobile replaces tables with card/list views maintaining full feature parity. Data list pages must also support printing for both current filtered results and full dataset.

### 2. **API-Centric Design**
Backend provides comprehensive OpenAPI documentation. Frontend consumes generated RTK Query APIs (not manual HTTP calls). API contracts are the source of truth.

### 3. **Separation of Concerns**
- **Backend:** Controllers → Services → Repositories (clear layering with validation in Services)
- **Frontend:** Pages → Components → Hooks (state management via Redux Toolkit + RTK Query)
- **Features:** Self-contained modules organized by domain

### 4. **Type Safety**
- TypeScript for frontend: strict mode, explicit types
- Java: generics, Java 25 features, no raw types
- OpenAPI schemas drive both client and server type generation

### 5. **Testing as First-Class**
Test code is as important as production code. New features require:
- Unit tests for business logic
- Integration tests for API endpoints
- Test naming reflects intent (e.g., `shouldCreateUpdateAndDeleteBrick`)

### 6. **Documentation Everywhere**
- API endpoints documented via OpenAPI annotations (`@Operation`, `@Schema`)
- Complex logic documented with inline comments (in French, matching project locale)
- User-facing features documented in MkDocs

---

## How to Implement a New Feature

### Phase 1: Backend
1. Define domain entities (JPA `@Entity`)
2. Create repositories (Spring Data `JpaRepository`)
3. Implement service classes (business logic + validation)
4. Create controllers (REST endpoints with OpenAPI annotations)
5. Write integration tests
6. For list screens, support print use cases in API contracts (filtered print and full print)

### Phase 2: API Generation
1. Run `pnpm -C data-web run openapi:pull` to fetch API spec
2. Run `pnpm -C data-web run rtk:codegen` to regenerate client APIs
3. Verify generated services in `data-web/src/services/`
4. Ensure every domain API expected to be regenerated is declared in `data-web/openapi-config.ts` under `outputFiles` before running codegen
5. If generation output is wrong, fix backend OpenAPI source and/or `data-web/openapi-config.ts`, then rerun generation (never patch generated files directly)

### Phase 3: Frontend
1. Create pages/forms consuming generated APIs
2. Use Redux Toolkit for complex state
3. Apply MUI components with responsive patterns
4. Write component tests
5. Add print action on list pages with two modes: print filtered results and print all

### Phase 4: Documentation & Validation
1. Ensure OpenAPI spec is complete
2. Add user-facing documentation in MkDocs
3. Run full test suite
4. Create PR with comprehensive description

### Phase 5: Mobile (if feature is also exposed in Android app)
1. Add/extend endpoints needed for mobile sync and conflict handling
2. Implement SQLite mapping and sync queue in Flutter app
3. Validate offline -> online reconciliation flows
4. Update mobile docs in `docs/1-features/1.3-mobile/`

---

## Language Convention

- **Code:** English (class names, method names, variable names)
- **Comments/Javadoc:** French (matches project documentation locale)
- **OpenAPI descriptions:** French (user-facing)
- **Commit messages:** English or French (flexible)

---

## References

- [Project README](https://github.com/tiogars/data#readme)
- [Main Architecture Overview](../../3-system/index.md)
- [MkDocs Documentation](../../settings/mkdocs.yml)
