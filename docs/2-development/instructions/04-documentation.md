# 04 - Documentation Standards

## Code Documentation

### Backend: Javadoc & Comments

#### Javadoc on Public Classes & Methods

All public classes and methods must have Javadoc:

```java
/**
 * Service responsible for creating new bricks.
 * 
 * Validates uniqueness of brick numbers before creation.
 */
@Service
public class BrickCreationService {

    private final BrickRepository brickRepository;

    /**
     * Creates a new brick from the provided form.
     * 
     * @param form the brick creation form containing number, title, and tags
     * @return the created brick with generated ID and timestamps
     * @throws IllegalArgumentException if number already exists or required fields are empty
     */
    @Transactional
    public Brick createBrick(BrickCreationForm form) {
        // Implementation
    }
}
```

**Rules:**
- Public classes: brief description + purpose
- Public methods: what, parameters, return value, exceptions
- Use `@param`, `@return`, `@throws` tags
- Keep descriptions concise (one sentence when possible)

#### Inline Comments

Use inline comments to explain **why**, not **what** (code shows the what):

```java
// Validate uniqueness: only one brick per number
Optional<BrickEntity> existing = brickRepository.findByNumber(number);
if (existing.isPresent() && !existing.get().getId().equals(excludeId)) {
    throw new IllegalArgumentException(\"Le numero de brique est deja utilise: \" + number);
}

// Store tags as CSV to avoid normalization on every fetch
entity.setTags(BrickModelMapper.tagsToCsv(normalizeTags(tags)));
```

**Avoid:**
```java
// BAD: Restates obvious code
// Check if number is not empty
if (number != null && !number.trim().isEmpty())
```

#### Comments in French

Comments and Javadoc descriptions are in **French** (matching project locale), except for code examples:

```java
/**
 * Valide l'unicité du numero de brique dans la base.
 * 
 * @param number le numero a valider
 * @param excludeId l'ID a exclure de la verification (pour les updates)
 * @throws IllegalArgumentException si le numero est deja utilise
 */
private void validateUniqueNumber(String number, String excludeId) {
    // Implementation
}
```

---

### Frontend: TSDoc & Comments

#### TSDoc on Exported Types & Functions

```typescript
/**
 * Represents a brick entity with metadata.
 */
export type Brick = {
  /** Unique identifier (UUID) */
  id: string;
  /** Reference number (e.g., \"60284\") */
  number: string;
  /** Display title */
  title: string;
  /** Classification tags */
  tags: string[];
  /** Base64-encoded image data or URL */
  imageBase64?: string;
  /** ISO timestamp of creation */
  createdAt: Instant;
  /** ISO timestamp of last modification */
  updatedAt: Instant;
};

/**
 * Fetches a list of all bricks from the server.
 * 
 * @returns A promise resolving to the brick list response or rejecting on error
 * 
 * @example
 * const { data, loading } = useListBricksQuery();
 */
export const useListBricksQuery = () => {
  // Implementation
};
```

**Rules:**
- Export types: brief description
- Exported functions: purpose + usage example
- Use `@param`, `@returns` tags
- Use `@example` for complex hooks

#### Inline Comments in TypeScript

```typescript
// Memoize computed tags to prevent unnecessary re-renders
const computedTags = useMemo(() => {
  return brick.tags.map(tag => tag.toLowerCase()).sort();
}, [brick.tags]);

// Mobile-first: stack vertically on small screens
const [isDesktop, setIsDesktop] = useState(false);
useEffect(() => {
  const handleResize = () => setIsDesktop(window.innerWidth >= 960);
  window.addEventListener('resize', handleResize);
  return () => window.removeEventListener('resize', handleResize);
}, []);
```

---

## OpenAPI Documentation

### Schema Annotations

All API models must have `@Schema` annotations with descriptions and examples:

```java
public class Brick {

    @Schema(
        description = \"Identifiant unique de la brique (UUID v4).\",
        example = \"550e8400-e29b-41d4-a716-446655440000\",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String id;

    @Schema(
        description = \"Numero de reference unique de la brique.\",
        example = \"60284\",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String number;

    @Schema(
        description = \"Titre descriptif de la brique.\",
        example = \"Le camion de chantier\",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String title;

    @Schema(
        description = \"Tags de classification pour recherche et filtrage.\",
        example = \"[\\\"city\\\", \\\"truck\\\", \\\"construction\\\"]\",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<String> tags;
}
```

### Endpoint Documentation

All REST endpoints must have `@Tag` and `@Operation`:

```java
@RestController
@Tag(name = \"brick\", description = \"Gestion de la collection de briques.\")
public class BrickController {

    @GetMapping(\"/brick\")
    @Operation(
        summary = \"Lister toutes les briques\",
        description = \"Retourne la collection complete de briques avec pagination optionnelle.\"
    )
    public ResponseEntity<BrickListResponse> listBricks() {
        // Implementation
    }

    @PostMapping(\"/brick\")
    @Operation(
        summary = \"Creer une nouvelle brique\",
        description = \"Cree une brique avec numero unique, titre, tags et image optionnelle.\"
    )
    public ResponseEntity<Brick> createBrick(@RequestBody BrickCreationForm form) {
        // Implementation
    }

    @DeleteMapping(\"/brick/{id}\")
    @Operation(
        summary = \"Supprimer une brique\",
        description = \"Supprime definitivement une brique et tous ses liens externes associes.\"
    )
    public ResponseEntity<Void> deleteBrick(@PathVariable String id) {
        // Implementation
    }
}
```

**Rules:**
- Descriptions in French
- `summary`: one-liner
- `description`: detailed behavior
- Include important side effects (e.g., cascading deletes)

### Generating API Specs

API specs are auto-generated and served at `/v3/api-docs`:

```bash
# Frontend: Fetch and regenerate client code
pnpm -C data-web run openapi:pull
pnpm -C data-web run rtk:codegen
```

The OpenAPI spec is the source of truth for the API contract.

---

## User-Facing Documentation (MkDocs)

### Documentation Structure

Features should be documented in `docs/1-features/`:

```
docs/1-features/
├── 1.1-management/
│   ├── index.md          # Overview
│   ├── creation.md       # How to create items
│   ├── editing.md        # How to edit items
│   ├── deletion.md       # How to delete items
│   ├── search.md         # Search & filtering
│   ├── import-export.md  # Import/export formats
│   └── screenshots/      # UI screenshots
├── 1.2-business/
│   └── ...
```

### Documentation Template

```markdown
# Feature Name

## Overview
Brief description of what this feature does and its purpose.

## Common Tasks

### Task 1: Create an Item
Step-by-step instructions with screenshots.

1. Navigate to the Create page
2. Fill in required fields
3. Click \"Save\"
4. Confirmation message appears

### Task 2: Edit an Existing Item
Instructions...

### Task 3: Delete an Item
Instructions...

## Keyboard Shortcuts
- `Ctrl+S` — Save
- `Ctrl+N` — New
- `Ctrl+F` — Find

## Troubleshooting
Common issues and solutions.

## See Also
- [Related Feature](../1.2-business/index.md)
```

**Rules:**
- Clear, non-technical language
- Step-by-step instructions
- Screenshots for UI features
- Include error messages and solutions
- Reference related features

### Expected Sections for Feature Pages

For user-facing feature pages in `docs/1-features/`, use the template as a guide but adapt it to the actual feature. The expected sections form a checklist, not a rigid skeleton.

**Expected sections when applicable:**
- **Objectif et périmètre**: what the feature is for, who uses it, and what is in or out of scope
- **Parcours utilisateur / cas d'usage**: main user flows or common operations, described step by step
- **Règles métier / validations**: important constraints, required fields, uniqueness rules, accepted formats, side effects
- **Responsive / mobile / desktop**: differences in behavior or layout between devices when they matter to the user
- **Imports / exports / formats**: supported file types, expected columns or JSON fields, replacement or merge behavior
- **Dépannage / erreurs fréquentes**: common failure cases, error messages, and corrective actions
- **Glossaire / concepts clés**: domain vocabulary needed to use the feature correctly
- **API / aspects techniques**: optional, only when helpful to explain integration points, automation, or known limits
- **Captures d'écran / exemples visuels**: recommended when they make workflows easier to understand

**Writing rules:**
- Do not keep empty headings for non-applicable topics
- Group closely related topics when that improves readability
- Keep the language user-oriented; move implementation detail to development docs unless it clarifies a real user need
- Prefer concrete examples over abstract descriptions for formats, validations, and troubleshooting

---

## Configuration Documentation

### README Files

Each module should have a `README.md`:

```markdown
# data-web

Frontend application for the Data platform.

## Quick Start

```bash
pnpm install
pnpm dev
```

## Project Structure

- `src/pages/` — Page components
- `src/components/` — Reusable components
- `src/features/` — Redux slices and domain logic
- `src/services/` — Generated RTK Query APIs

## Development

See [Development Instructions](../instructions/index.md) for patterns and conventions.

## Testing

```bash
pnpm test
pnpm test:coverage
```
```

### Configuration Files

Document non-obvious configuration in comments:

```yaml
# mkdocs.yml
site_name: Data Platform
site_url: https://data.tiogars.fr
docs_dir: docs
site_dir: site_output

# Theme and plugins
theme:
  name: material
  features:
    - navigation.tabs
    - toc.follow

plugins:
  - search
  - pdf:
      enabled: !ENV [ENABLE_PDF_EXPORT, false]
```

---

## Documentation Maintenance

### Keep Docs in Sync

When updating features:
1. Update code comments/Javadoc
2. Update OpenAPI annotations
3. Update user-facing MkDocs
4. Update architecture/pattern docs if design changed

### Review Checklist

Before committing:
- [ ] All public APIs have Javadoc/TSDoc
- [ ] OpenAPI annotations are complete
- [ ] User-facing features documented in MkDocs
- [ ] Complex logic has inline comments explaining \"why\"
- [ ] All links and references are valid

---

## Generating Documentation

### Backend: OpenAPI Spec

```bash
# Automatically generated on startup
# Access at: http://localhost:8080/v3/api-docs
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### Frontend: API Documentation

```bash
# Generated from OpenAPI spec
pnpm -C data-web run generate:apis
```

### MkDocs: User Documentation

```bash
# Build static HTML
mkdocs build -f docs/settings/mkdocs.yml -d ../site_output

# Serve locally
mkdocs serve -f docs/settings/mkdocs.yml
```

---

## References

- [Javadoc Style Guide](https://docs.oracle.com/en/java/javase/25/docs/specs/javadoc/doc-comment-spec.html)
- [OpenAPI 3 Specification](https://spec.openapis.org/oas/v3.1.0)
- [MkDocs Documentation](https://www.mkdocs.org/)
- [RFC 7231: HTTP Semantics](https://tools.ietf.org/html/rfc7231)
