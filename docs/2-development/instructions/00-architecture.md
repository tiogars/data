# 00 - Architecture & Project Structure

## Monorepo Organization

The **data** project is organized as a monorepo containing five main modules:

```
data/
├── data-web/              # React frontend
├── flutter_application/   # Flutter Android app
├── data-server/           # Spring Boot API server
├── data-gateway/          # Spring Cloud Gateway (routing, rate-limiting, auth)
├── docs/                  # MkDocs documentation
├── docker-compose.yml     # Local development orchestration
└── .github/               # CI/CD and configuration
```

### Module Responsibilities

| Module | Role | Technology |
|--------|------|-----------|
| `data-web` | User interface & client | React 19, TypeScript, Vite, MUI v9, Redux Toolkit |
| `flutter_application` | Mobile user interface | Flutter, Dart, SQLite |
| `data-server` | Business logic & data access | Spring Boot 4, Spring Data JPA, PostgreSQL, OpenAPI |
| `data-gateway` | API gateway & security | Spring Cloud Gateway MVC, OAuth2, Bucket4j (rate-limiting) |
| `docs` | Product & technical docs | MkDocs, Markdown |

---

## Feature Organization Pattern

Features are organized by **domain** with consistent structure across frontend and backend.

### Backend Feature Structure
```
data-server/src/main/java/fr/tiogars/data/dev/docs/<domain>/
├── controllers/           # @RestController classes
│   ├── <Entity>Controller.java
│   ├── <Entity>ByIdController.java
│   └── <Entity>PdfController.java (if applicable)
├── services/              # @Service classes (one per operation)
│   ├── <Entity>ListService.java
│   ├── <Entity>CreationService.java
│   ├── <Entity>DeletionService.java
│   ├── <Entity>UpdateService.java
│   └── <Entity>ModelMapper.java
├── repositories/          # Spring Data JpaRepository interfaces
│   └── <Entity>Repository.java
├── entities/              # JPA @Entity classes
│   └── <Entity>Entity.java
├── models/                # DTOs (response models)
│   └── <Entity>.java
├── forms/                 # Request DTOs (form inputs)
│   ├── <Entity>CreationForm.java
│   ├── <Entity>UpdateForm.java
│   └── <Entity>SearchForm.java (if applicable)
└── [exception/]           # Domain-specific exceptions (optional)
```

**Pattern:** Each service class handles a single responsibility (e.g., `BrickCreationService` only creates bricks). This makes testing and maintenance easier.

### Frontend Feature Structure
```
data-web/src/
├── pages/<domain>/                # Page components
│   ├── <Entity>Page/
│   │   ├── index.tsx
│   │   ├── <Entity>Form.tsx
│   │   └── <Entity>List.tsx
├── components/<domain>/           # Reusable components
│   ├── <Entity>Form.tsx
│   ├── <Entity>Card.tsx
│   └── ...
├── features/<domain>/             # Redux slices & hooks
│   ├── types.ts                   # Domain types
│   └── iconRegistry.tsx (if needed)
└── services/                      # Generated RTK Query APIs
    ├── <domain>Api.ts             # Generated from OpenAPI
    └── emptyApi.ts                # Base split API
```

### Mobile Feature Structure (Flutter)
```
flutter_application/lib/
├── app/                             # app bootstrap & routing
├── core/                            # api client, auth, db, sync engine
│   ├── api/
│   ├── database/
│   └── sync/
├── features/
│   ├── gtin/
│   ├── vehicles/                    # car + car mileage
│   └── android_apps/
└── shared/                          # common models, widgets, utils
```

---

## Layering & Dependency Flow

### Backend Layering (Spring)
```
REST Controllers
    ↓ (handles HTTP)
Service Layer
    ↓ (business logic)
Repository Layer
    ↓ (data access)
Database (PostgreSQL)
```

**Rule:** Controllers call Services, Services call Repositories. No skipping layers.

**Validation:** Performed in Service layer, not in controllers or entities.

### Frontend Layering
```
Pages
    ↓ (compose & orchestrate)
Components + Hooks
    ↓ (UI & local logic)
Redux Toolkit Store + RTK Query
    ↓ (state & API calls)
Generated API Services
    ↓ (HTTP)
Gateway (localhost:8081)
    ↓
Backend (localhost:8080)
```

**Rule:** Pages orchestrate components. Components don't know about routing. Hooks encapsulate reusable logic.

### Mobile Layering (Flutter)
```
Presentation (screens, state notifiers)
    ↓
Domain (use cases, business rules)
    ↓
Data (repositories, API client, SQLite)
    ↓
Gateway API (localhost:8081)
```

**Rule:** Offline-first by default for Android app. Writes are persisted locally and synchronized through a queue.

---

## Key Conventions

### Naming

| Item | Convention | Example |
|------|-----------|---------|
| Java classes | PascalCase | `BrickCreationService`, `BrickRepository` |
| Java packages | lowercase, domain-based | `fr.tiogars.data.dev.docs.brick.services` |
| React components | PascalCase with `.tsx` | `BrickForm.tsx`, `BrickCard.tsx` |
| React hooks | camelCase, prefix `use` | `useBrickForm()`, `useBrickList()` |
| Dart classes | PascalCase | `GtinSyncService`, `CarMileageRepository` |
| Dart files | snake_case | `gtin_sync_service.dart` |
| Redux slices | camelCase | `apiErrorSnackbar` |
| API services (generated) | `<domain>Api.ts` | `sectionApi.ts`, `footerLinkApi.ts` |
| Database tables | snake_case | `brick`, `external_link` |

### Directories

- **Plural for collections:** `controllers/`, `services/`, `entities/`, `forms/`, `pages/`, `components/`, `features/`
- **Singular for modules:** `data-web/`, `data-server/`, `data-gateway/` (one per responsibility)
- **Mobile domain folders:** `features/gtin`, `features/vehicles`, `features/android_apps`

---

## Local Development Structure

### Environment Setup
```powershell
# Launch entire stack
docker compose up --build --watch

# Or run modules separately:
pnpm -C data-web install && pnpm -C data-web dev
.\data-server\mvnw.cmd -f data-server\pom.xml spring-boot:run
.\data-gateway\mvnw.cmd -f data-gateway\pom.xml spring-boot:run
cd flutter_application
flutter pub get
flutter run -d emulator-5554
```

### External Networks (Docker)
The project requires pre-existing Docker networks:
- `data_network` — internal communication
- `npm_network` — shared npm cache

Create once:
```bash
docker network create data_network
docker network create npm_network
```

### Port Configuration
| Service | Port | URL |
|---------|------|-----|
| Frontend (Vite) | 5173 | http://localhost:5173 |
| Gateway | 8081 | http://localhost:8081 |
| API Server | 8080 | http://localhost:8080 |
| Flutter Android (debug) | n/a | Android emulator/device |
| PostgreSQL | 5432 | (internal) |
| MkDocs | 8000 | http://localhost:8000/data/ |
| Grafana (observability) | 3000 | http://localhost:3000/ |

---

## Adding a New Feature

### Step 1: Identify the Domain
- Choose or create a domain folder (e.g., `brick`, `section`, `footerLink`)
- Follow naming: singular lowercase (e.g., `brick` not `bricks`)

### Step 2: Backend
- Create entities, repositories, services following the pattern above
- Add OpenAPI annotations to controllers
- Write integration tests
- Run `mvn clean test` to verify

### Step 3: API Generation
- Run `pnpm -C data-web run openapi:pull`
- Run `pnpm -C data-web run rtk:codegen`
- Verify generated services exist in `data-web/src/services/`

### Step 4: Frontend
- Create pages using generated API services
- Use responsive patterns (MUI Grid, mobile-first)
- Write component tests

### Step 5: Documentation
- Add OpenAPI descriptions (in French) to API operations
- Update MkDocs if user-facing behavior changed
- Link new feature in sidebar if applicable

---

## Cross-Cutting Concerns

### Error Handling
**Backend:** Use `@RestControllerAdvice` with `@ExceptionHandler` (see `GlobalControllerExceptionHandler`)

**Frontend:** RTK Query errors captured by Redux middleware (`rtkQueryErrorSnackbarMiddleware`), displayed as snackbar

**Mobile:** Offline saves enqueue operations in local SQLite queue; sync failures are retried via `SyncEngine` and attempts counter.

### Logging & Observability
- **Backend:** Use Spring Actuator (`/actuator/health`, `/actuator/metrics`)
- **Frontend:** Browser DevTools + network inspector
- **Monitoring:** Grafana (localhost:3000) reads LGTM stack (Loki, Grafana, Tempo, Mimir)

### Security
- **Gateway:** OAuth2 Resource Server, rate-limiting via Bucket4j
- **Validation:** Input validation in service layer (not controller)
- **Database:** Parameterized queries (Spring Data JPA handles this)

See [Security Practices](./05-security.md) for details.

---

## References

- [Frontend Patterns](./01-frontend.md)
- [Backend Patterns](./02-backend.md)
- [Project README](https://github.com/tiogars/data#readme)
