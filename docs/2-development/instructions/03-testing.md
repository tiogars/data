# 03 - Testing Conventions

## Testing Strategy

Testing follows a **pyramid approach**:

```
       /\\
      /  \\  E2E Tests (few, full flow)
     /----\\
    /      \\  Integration Tests (moderate, API+DB)
   /--------\\
  /          \\  Unit Tests (many, isolated logic)
 /____________\\
```

**Target coverage:** ≥ 80% for business logic, ≥ 70% overall.

---

## Unit Tests

Unit tests verify business logic in isolation.

### Backend Unit Tests (Java)

```java
package fr.tiogars.data.dev.docs.brick.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BrickCreationServiceTest {

    private BrickCreationService service;
    private BrickRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(BrickRepository.class);
        service = new BrickCreationService(repository);
    }

    @Test
    void shouldCreateBrickWithValidForm() {
        BrickCreationForm form = new BrickCreationForm();
        form.setNumber(\"60284\");
        form.setTitle(\"Le camion\");
        form.setTags(List.of(\"city\", \"truck\"));

        when(repository.findByNumber(\"60284\")).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(inv -> {
            BrickEntity entity = inv.getArgument(0);
            entity.setId(\"uuid-123\");
            return entity;
        });

        Brick result = service.createBrick(form);

        assertThat(result)
            .isNotNull()
            .hasFieldOrPropertyWithValue(\"id\", \"uuid-123\")
            .hasFieldOrPropertyWithValue(\"number\", \"60284\")
            .hasFieldOrPropertyWithValue(\"title\", \"Le camion\");
    }

    @Test
    void shouldThrowWhenNumberAlreadyExists() {
        BrickCreationForm form = new BrickCreationForm();
        form.setNumber(\"60284\");
        form.setTitle(\"Le camion\");

        when(repository.findByNumber(\"60284\"))
            .thenReturn(Optional.of(new BrickEntity()));

        assertThatThrownBy(() -> service.createBrick(form))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(\"deja utilise\");
    }

    @Test
    void shouldThrowWhenNumberIsEmpty() {
        BrickCreationForm form = new BrickCreationForm();
        form.setNumber(\"\");
        form.setTitle(\"Le camion\");

        assertThatThrownBy(() -> service.createBrick(form))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(\"obligatoire\");
    }
}
```

**Rules:**
- Use `@BeforeEach` for setup
- Mock external dependencies (repositories)
- Name tests: `should<Expected>When<Condition>` or `shouldThrow<Exception>When<Condition>`
- One assertion focus per test
- Use `assertThat()` from AssertJ (fluent API)

### Frontend Unit Tests (React)

```typescript
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrickForm } from './BrickForm';
import { vi } from 'vitest';

describe('BrickForm', () => {
  it('should display all form fields', () => {
    const handleSubmit = vi.fn();
    render(<BrickForm onSubmit={handleSubmit} />);

    expect(screen.getByLabelText(/brick number/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/title/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /save/i })).toBeInTheDocument();
  });

  it('should call onSubmit with form data when submitted', async () => {
    const handleSubmit = vi.fn();
    const user = userEvent.setup();
    render(<BrickForm onSubmit={handleSubmit} />);

    const numberInput = screen.getByLabelText(/brick number/i);
    const titleInput = screen.getByLabelText(/title/i);
    const submitButton = screen.getByRole('button', { name: /save/i });

    await user.type(numberInput, '60284');
    await user.type(titleInput, 'Le camion');
    await user.click(submitButton);

    expect(handleSubmit).toHaveBeenCalledWith(
      expect.objectContaining({
        number: '60284',
        title: 'Le camion',
      })
    );
  });

  it('should display validation error when number is missing', async () => {
    const handleSubmit = vi.fn();
    const user = userEvent.setup();
    render(<BrickForm onSubmit={handleSubmit} />);

    const submitButton = screen.getByRole('button', { name: /save/i });
    await user.click(submitButton);

    expect(screen.getByText(/number is required/i)).toBeInTheDocument();
  });
});
```

**Rules:**
- Use `@testing-library/react` (not Enzyme)
- Test user interactions, not implementation details
- Use `userEvent` for realistic interactions
- Test accessibility (query by role, label, etc.)
- One behavior focus per test

---

## Integration Tests

Integration tests verify components working together (API + DB, Pages + Services).

### Backend Integration Tests

```java
package fr.tiogars.data.dev.docs.brick.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BrickApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BrickRepository brickRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        brickRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateUpdateAndDeleteBrick() throws Exception {
        String createPayload = \"\"\"
            {
              \"number\": \"60284\",
              \"title\": \"Le camion de chantier\",
              \"tags\": [\"city\", \"truck\"]
            }
            \"\"\";

        // CREATE
        var createResult = mockMvc.perform(post(\"/brick\")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath(\"$.id\").isNotEmpty())
            .andExpect(jsonPath(\"$.number\").value(\"60284\"))
            .andReturn();

        String createdId = extractId(createResult);

        // LIST
        mockMvc.perform(get(\"/brick\"))
            .andExpect(status().isOk())
            .andExpect(jsonPath(\"$.count\").value(1))
            .andExpect(jsonPath(\"$.items[0].id\").value(createdId));

        // UPDATE
        String updatePayload = \"\"\"
            {
              \"id\": \"%s\",
              \"number\": \"60284\",
              \"title\": \"Updated title\",
              \"tags\": [\"city\"]
            }
            \"\"\".formatted(createdId);

        mockMvc.perform(put(\"/brick/{id}\", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath(\"$.title\").value(\"Updated title\"));

        // DELETE
        mockMvc.perform(delete(\"/brick/{id}\", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get(\"/brick/{id}\", createdId))
            .andExpect(status().isNotFound());
    }
}
```

**Rules:**
- Use `@SpringBootTest` for full app context
- Use `MockMvc` for API testing
- Clean database before each test (`@BeforeEach`)
- Test full CRUD flows (Create, Read, Update, Delete)
- Verify HTTP status codes and response structure

### Frontend Integration Tests

Test pages with mocked API services:

```typescript
import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { BrickPage } from './BrickPage';
import { brickApi } from '../services/brickApi';

// Mock the API endpoint
vi.mock('../services/brickApi', () => ({
  useListBricksQuery: vi.fn(() => ({
    data: { items: [{ id: '1', number: '60284', title: 'Test' }] },
    loading: false,
    error: null,
  })),
}));

describe('BrickPage', () => {
  it('should display list of bricks', async () => {
    const store = configureStore({
      reducer: {
        [brickApi.reducerPath]: brickApi.reducer,
      },
    });

    render(
      <Provider store={store}>
        <BrickPage />
      </Provider>
    );

    await waitFor(() => {
      expect(screen.getByText('Test')).toBeInTheDocument();
    });
  });
});
```

---

## Test Naming Convention

### Method Names

Follow the pattern: `should<ExpectedBehavior>When<Condition>`

**Examples:**
```java
shouldCreateBrickWithValidForm()
shouldThrowWhenNumberAlreadyExists()
shouldReturnBrickListWithCount()
shouldUpdateBrickTitleOnly()
shouldDeleteAllBricksSuccessfully()
shouldReturnNotFoundWhenIdDoesNotExist()
```

### Test Class Names

Pattern: `<ClassName>Test` or `<ClassName>IntegrationTest`

**Examples:**
```
BrickCreationServiceTest
BrickApiIntegrationTest
BrickFormTest
BrickGatewayIntegrationTest
```

---

## Test Organization

### File Structure

```
data-server/src/test/java/fr/tiogars/data/dev/docs/<domain>/
├── controllers/
│   └── <Entity>ApiIntegrationTest.java
├── services/
│   ├── <Entity>CreationServiceTest.java
│   ├── <Entity>ListServiceTest.java
│   └── ...
└── repositories/
    └── <Entity>RepositoryTest.java

data-web/src/
├── components/
│   └── <Component>.test.tsx
├── pages/
│   └── <Page>.test.tsx
└── services/
    └── <service>.test.ts
```

**Rule:** Tests colocated with source code (same directory structure).

---

## Coverage Requirements

- **Services:** ≥ 80% coverage
- **Controllers:** ≥ 60% coverage (structural testing)
- **Components:** ≥ 70% coverage
- **Overall:** ≥ 70% coverage

Run coverage:

**Backend:**
```bash
mvn clean test jacoco:report
# Report: target/site/jacoco/index.html
```

**Frontend:**
```bash
pnpm -C data-web test:coverage
# Report: data-web/coverage/index.html
```

---

## Testing Best Practices

### DO

✅ Test behavior, not implementation  
✅ Use descriptive test names  
✅ One logical assertion per test (or related assertions)  
✅ Mock external dependencies  
✅ Use fixtures for common test data  
✅ Test edge cases (null, empty, invalid input)  
✅ Verify error messages  

### DON'T

❌ Test implementation details (private methods)  
❌ Use multiple unrelated assertions in one test  
❌ Create complex test data (use factories/builders)  
❌ Test the framework (Spring, React, etc.) - trust it works  
❌ Hardcode test data in test methods  
❌ Test all permutations (focus on critical paths)  

---

## CI/CD Integration

Tests run automatically on:
- **Push to any branch** (via GitHub Actions)
- **Pull requests** (pre-merge validation)
- **Main branch** (pre-deployment)

Failure blocks merging. Ensure all tests pass locally before pushing:

```bash
# Backend
mvn clean test

# Frontend
pnpm -C data-web test
pnpm -C data-web lint
```

---

## References

- [JUnit 5 Documentation](https://junit.org/junit5/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ Documentation](https://assertj.github.io/assertj-core-features-highlight.html)
- [React Testing Library Docs](https://testing-library.com/react)
- [Vitest Documentation](https://vitest.dev/)
