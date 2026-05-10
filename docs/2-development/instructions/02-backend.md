# 02 - Backend Patterns (Spring Boot, Services, Repositories)

## Layered Architecture

Backend follows a strict **three-layer architecture**:

```
REST Controllers (HTTP handling)
         ↓
Service Layer (business logic)
         ↓
Repository Layer (data access)
         ↓
Database (PostgreSQL)
```

**Rule:** Each layer has a single responsibility. No skipping layers.

---

## Controllers (REST Endpoints)

### Controller Design

Controllers are thin HTTP adapters. They delegate to services:

```java
package fr.tiogars.data.dev.docs.brick.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick", description = "Gestion de la collection de briques et des liens externes associes.")
public class BrickController {

    private final BrickListService brickListService;
    private final BrickCreationService brickCreationService;
    private final BrickDeleteAllService brickDeleteAllService;

    public BrickController(
        BrickListService brickListService,
        BrickCreationService brickCreationService,
        BrickDeleteAllService brickDeleteAllService
    ) {
        this.brickListService = brickListService;
        this.brickCreationService = brickCreationService;
        this.brickDeleteAllService = brickDeleteAllService;
    }

    @GetMapping(\"/brick\")
    @Operation(summary = \"Lister les briques\", description = \"Retourne la collection complete de briques.\")
    public ResponseEntity<BrickListResponse> listBricks() {
        return ResponseEntity.ok(brickListService.listBricks());
    }

    @PostMapping(\"/brick\")
    @Operation(summary = \"Creer une brique\", description = \"Ajoute une nouvelle brique a la collection.\")
    public ResponseEntity<Brick> createBrick(@RequestBody BrickCreationForm form) {
        return ResponseEntity.ok(brickCreationService.createBrick(form));
    }

    @DeleteMapping(\"/brick\")
    @Operation(summary = \"Supprimer toutes les briques\", description = \"Supprime l'ensemble des briques de la collection.\")
    public ResponseEntity<Void> deleteAllBricks() {
        brickDeleteAllService.deleteAllBricks();
        return ResponseEntity.noContent().build();
    }
}
```

**Rules:**
- Inject services via constructor dependency injection
- One service per operation (keep controllers thin)
- Use OpenAPI annotations (`@Tag`, `@Operation`) on all endpoints
- Return appropriate HTTP status codes (200, 201, 204, 400, 404, 500)
- No business logic in controllers

### List Printing Support (Required)
For domains exposing list screens, backend must support printing both full and filtered data.

Contract and behavior rules:
- Reuse list filtering/sorting parameters for print requests
- Support two print modes: `filtered` and `all`
- Return stable ordering matching UI sort order
- Include print metadata (`generatedAt`, `total`) in the response
- Protect performance with bounded queries and validation on filter inputs

Suggested endpoint style:

```java
@PostMapping("/brick/print")
@Operation(summary = "Imprimer la liste des briques", description = "Retourne les donnees imprimees en mode filtre ou complet.")
public ResponseEntity<BrickPrintResponse> printBricks(@RequestBody BrickPrintRequest request) {
    return ResponseEntity.ok(brickPrintService.printBricks(request));
}
```

### Separation of Concerns: One Service Per Operation

Instead of a single `BrickService`:

```java
// GOOD: Separate services for each operation
- BrickListService
- BrickCreationService
- BrickUpdateService
- BrickDeleteOneService
- BrickDeleteAllService
- BrickGetOneService
```

Why? Single Responsibility Principle makes testing and maintenance easier.

---

## Service Layer (Business Logic)

Services contain all business logic, validation, and error handling.

### Service Structure

```java
package fr.tiogars.data.dev.docs.brick.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BrickCreationService {

    private final BrickRepository brickRepository;

    // Constructor injection (required for testing)
    public BrickCreationService(BrickRepository brickRepository) {
        this.brickRepository = brickRepository;
    }

    @Transactional
    public Brick createBrick(BrickCreationForm form) {
        // 1. Validate input
        validateUniqueNumber(form.getNumber(), null);

        // 2. Create entity
        BrickEntity entity = new BrickEntity();
        applyValues(entity, form.getNumber(), form.getTitle(), form.getTags(), form.getImageBase64());

        // 3. Persist
        return BrickModelMapper.toModel(brickRepository.save(entity));
    }

    private void validateUniqueNumber(String number, String excludeId) {
        Optional<BrickEntity> existing = brickRepository.findByNumber(number);
        if (existing.isPresent() && !existing.get().getId().equals(excludeId)) {
            throw new IllegalArgumentException(\"Le numero de brique est deja utilise: \" + number);
        }
    }

    static void applyValues(BrickEntity entity, String number, String title, List<String> tags, String imageBase64) {
        entity.setNumber(requireText(number, \"Le numero de brique est obligatoire.\"));
        entity.setTitle(requireText(title, \"Le titre de la brique est obligatoire.\"));
        entity.setTags(BrickModelMapper.tagsToCsv(normalizeTags(tags)));
        entity.setImageBase64(normalizeNullableText(imageBase64));
    }

    private static String requireText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
```

**Rules:**
- Use `@Transactional` for methods that modify data
- Validate inputs early (fail fast)
- Throw `IllegalArgumentException` for validation errors (caught by `@RestControllerAdvice`)
- Inject repositories, not entities
- Keep methods focused (do one thing)
- Use helper static methods for repeated logic

### Validation Pattern

Validation happens in the **Service layer**, not in controllers or entities:

```java
public void validateBrick(BrickCreationForm form) {
    if (form.getNumber() == null || form.getNumber().trim().isEmpty()) {
        throw new IllegalArgumentException(\"Le numero est obligatoire.\");
    }
    if (form.getTitle() == null || form.getTitle().trim().isEmpty()) {
        throw new IllegalArgumentException(\"Le titre est obligatoire.\");
    }
    // Check uniqueness
    brickRepository.findByNumber(form.getNumber()).ifPresent(existing -> {
        throw new IllegalArgumentException(\"Le numero est deja utilise.\");
    });
}
```

---

## Repository Layer (Data Access)

Repositories are Spring Data `JpaRepository` interfaces:

```java
package fr.tiogars.data.dev.docs.brick.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrickRepository extends JpaRepository<BrickEntity, String> {

    Optional<BrickEntity> findByNumber(String number);
    
    // Custom queries can be added, but keep them simple
    // List<BrickEntity> findByTagsContaining(String tag);
}
```

**Rules:**
- Extend `JpaRepository<Entity, IdType>`
- Declare as `@Repository`
- Use Spring Data query methods (avoid custom `@Query`)
- Let services handle complex query logic

---

## Entity & DTO Patterns

### Entities (JPA)

Entities represent database tables:

```java
package fr.tiogars.data.dev.docs.brick.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = \"brick\")
public class BrickEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = \"id\", nullable = false, updatable = false)
    private String id;

    @Column(name = \"number\", nullable = false, unique = true)
    private String number;

    @Column(name = \"title\", nullable = false)
    private String title;

    @Column(name = \"tags\", nullable = false, length = 4000)
    private String tags; // CSV format

    @Column(name = \"image_base64\", columnDefinition = \"TEXT\")
    private String imageBase64;

    @Column(name = \"created_at\", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = \"updated_at\", nullable = false)
    private Instant updatedAt;

    // Lifecycle hooks
    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    // Getters and setters (generated by IDE)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    // ... etc
}
```

**Rules:**
- Use `@Entity` and `@Table` annotations
- Use `@Id` with `@GeneratedValue(strategy = GenerationType.UUID)`
- Database column names are `snake_case`
- Use `@PrePersist` and `@PreUpdate` for timestamps
- No business logic in entities

### DTOs (Data Transfer Objects)

#### Response Models (exported to API)

```java
package fr.tiogars.data.dev.docs.brick.models;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

public class Brick {

    @Schema(description = \"Identifiant unique de la brique.\", example = \"123e4567-e89b-12d3-a456-426614174000\")
    private String id;

    @Schema(description = \"Numero de reference de la brique.\", example = \"60284\")
    private String number;

    @Schema(description = \"Titre de la brique.\", example = \"Le camion de chantier\")
    private String title;

    @Schema(description = \"Tags de classification de la brique.\", example = \"[\\\"city\\\",\\\"truck\\\"]\")
    private List<String> tags;

    @Schema(description = \"Image en data URL base64.\")
    private String imageBase64;

    @Schema(description = \"Date de creation.\", example = \"2025-12-22T12:14:59.569Z\")
    private Instant createdAt;

    @Schema(description = \"Date de derniere modification.\")
    private Instant updatedAt;

    // Getters and setters
}
```

#### Request Forms (input validation)

```java
package fr.tiogars.data.dev.docs.brick.forms;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class BrickCreationForm {

    @Schema(description = \"Numero de reference.\", example = \"60284\")
    private String number;

    @Schema(description = \"Titre.\", example = \"Le camion de chantier\")
    private String title;

    @Schema(description = \"Tags de classification.\")
    private List<String> tags;

    @Schema(description = \"Image en data URL base64 (optional).\")
    private String imageBase64;

    // Getters and setters
}
```

**Rules:**
- Response models: immutable from client perspective, all fields with `@Schema`
- Request forms: mutable, only fields needed for input
- All public fields have `@Schema` documentation (French)
- Use descriptive examples in schemas

### Model Mappers

Mappers convert entities ↔ DTOs:

```java
package fr.tiogars.data.dev.docs.brick.services;

import fr.tiogars.data.dev.docs.brick.entities.BrickEntity;
import fr.tiogars.data.dev.docs.brick.models.Brick;
import java.util.List;

final class BrickModelMapper {

    private BrickModelMapper() {
    }

    static Brick toModel(BrickEntity entity) {
        Brick model = new Brick();
        model.setId(entity.getId());
        model.setNumber(entity.getNumber());
        model.setTitle(entity.getTitle());
        model.setTags(csvToTags(entity.getTags()));
        model.setImageBase64(entity.getImageBase64());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    static BrickEntity toEntity(Brick model) {
        BrickEntity entity = new BrickEntity();
        entity.setNumber(model.getNumber());
        entity.setTitle(model.getTitle());
        entity.setTags(tagsToCsv(model.getTags()));
        entity.setImageBase64(model.getImageBase64());
        return entity;
    }

    private static List<String> csvToTags(String csv) {
        if (csv == null || csv.isEmpty()) return List.of();
        return Arrays.asList(csv.split(\",\"));
    }

    static String tagsToCsv(List<String> tags) {
        if (tags == null || tags.isEmpty()) return \"\";
        return String.join(\",\", tags);
    }
}
```

**Rules:**
- Keep mappers simple (no business logic)
- Use static methods
- Package-private (only used within domain)

---

## Error Handling (Global Exception Handler)

Exceptions are caught by a centralized `@RestControllerAdvice`:

```java
package fr.tiogars.data.common.controllers.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import fr.tiogars.data.common.exceptions.DataNotFoundException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleNotFound(DataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(\"Une erreur inattendue s'est produite.\");
    }
}
```

**Rules:**
- Throw `IllegalArgumentException` for validation errors → 400 Bad Request
- Throw `DataNotFoundException` for missing resources → 404 Not Found
- Centralize error responses in `@RestControllerAdvice`
- Log unexpected errors

---

## Transactions

Use `@Transactional` on service methods that modify data:

```java
@Service
public class BrickUpdateService {

    @Transactional
    public Brick updateBrick(String id, BrickUpdateForm form) {
        BrickEntity entity = brickRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException(\"Brick not found: \" + id));
        
        entity.setTitle(form.getTitle());
        entity.setTags(tagsTocsv(form.getTags()));
        
        return BrickModelMapper.toModel(brickRepository.save(entity));
    }
}
```

**Rules:**
- Use `@Transactional` on update/delete operations
- Omit on read-only queries (optional)
- Rollback automatically on exception

---

## Dependency Injection & Testing

Always use **constructor injection** for testability:

```java
// GOOD: Constructor injection
@Service
public class BrickCreationService {
    private final BrickRepository repo;
    
    public BrickCreationService(BrickRepository repo) {
        this.repo = repo;
    }
}

// AVOID: Field injection
@Service
public class BrickCreationService {
    @Autowired
    private BrickRepository repo;
}
```

Constructor injection makes mocking easier in tests.

---

## OpenAPI Documentation

All endpoints and models must have OpenAPI annotations:

```java
@RestController
@Tag(name = \"brick\", description = \"Gestion des briques.\")
public class BrickController {

    @GetMapping(\"/brick/{id}\")
    @Operation(summary = \"Obtenir une brique\", description = \"Retourne une brique par son identifiant.\")
    public ResponseEntity<Brick> getBrickById(@PathVariable String id) {
        // Implementation
    }
}
```

**In models:**
```java
public class Brick {
    @Schema(description = \"Identifiant unique.\", example = \"uuid-123\")
    private String id;
}
```

---

## Testing Services

See [Testing Conventions](./03-testing.md) for detailed testing patterns.

Quick example:
```java
@SpringBootTest
class BrickCreationServiceTest {

    @Autowired
    private BrickCreationService service;

    @MockBean
    private BrickRepository repository;

    @Test
    void shouldCreateBrick() {
        BrickCreationForm form = new BrickCreationForm();
        form.setNumber(\"60284\");
        form.setTitle(\"Test\");

        service.createBrick(form);

        verify(repository).save(any(BrickEntity.class));
    }
}
```

---

## References

- [Architecture Guide](./00-architecture.md)
- [Spring Data JPA Docs](https://spring.io/projects/spring-data-jpa)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Springdoc OpenAPI](https://springdoc.org/)
