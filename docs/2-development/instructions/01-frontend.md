# 01 - Frontend Patterns (React, MUI, TypeScript)

## Component Architecture

### Functional Components (FC) with TypeScript

All components are **functional components** with explicit type annotations.

```typescript
import { FC, ReactNode } from 'react';
import Box from '@mui/material/Box';

type MyComponentProps = {
  children: ReactNode;
  disabled?: boolean;
};

export const MyComponent: FC<MyComponentProps> = ({ children, disabled = false }) => {
  return (
    <Box sx={{ /* styles */ }}>
      {children}
    </Box>
  );
};
```

**Rules:**
- Use `FC` type for functional components
- Define props as a separate `type` (not inline)
- Props type name: `<ComponentName>Props`
- Export components explicitly
- Use named exports (not default)

---

## Component Organization

### Page Components
Pages are in `pages/<domain>/` and orchestrate features:

```typescript
import { FC } from 'react';
import { useListBricksQuery } from '../../services/brickApi';
import { BrickForm } from '../../components/BrickForm';
import { BrickTable } from '../../components/BrickTable';

export const BrickPage: FC = () => {
  const { data, loading, error } = useListBricksQuery();

  return (
    <div>
      <BrickForm onSubmit={/* ... */} />
      <BrickTable items={data?.items ?? []} loading={loading} />
    </div>
  );
};
```

**Responsibilities:**
- Fetch data from API services
- Manage page-level state (e.g., filters)
- Compose multiple components
- Handle navigation

### Feature Components
Reusable domain-specific components in `components/<domain>/`:

```typescript
// BrickForm.tsx - form for creating/editing a brick
type BrickFormProps = {
  onSubmit: (data: BrickCreationForm) => Promise<void>;
  loading?: boolean;
};

export const BrickForm: FC<BrickFormProps> = ({ onSubmit, loading = false }) => {
  // Component logic
};

// BrickCard.tsx - displays a single brick
type BrickCardProps = {
  brick: Brick;
  onDelete?: (id: string) => void;
};

export const BrickCard: FC<BrickCardProps> = ({ brick, onDelete }) => {
  // Component logic
};
```

**Rules:**
- Components are single-responsibility
- Props are immutable (no mutation)
- Callbacks passed as props
- No direct routing calls (pages handle navigation)

### Presentational Components
Generic, reusable components in `components/`:

```typescript
// Breadcrumbs, Footer, Header, etc.
// These are agnostic to domain
```

---

## State Management

### Redux Toolkit + RTK Query

**Redux Toolkit** manages app-wide state. **RTK Query** handles server state (caching, refetching).

#### Store Setup
```typescript
// store.ts
import { configureStore } from '@reduxjs/toolkit';
import { emptySplitApi } from './services/emptyApi';
import { apiErrorSnackbarReducer } from './features/apiErrorSnackbar/slice';
import { rtkQueryErrorSnackbarMiddleware } from './middleware/rtkQueryErrorSnackbarMiddleware';

export const store = configureStore({
  reducer: {
    apiErrorSnackbar: apiErrorSnackbarReducer,
    [emptySplitApi.reducerPath]: emptySplitApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(rtkQueryErrorSnackbarMiddleware, emptySplitApi.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
```

#### Feature Slices
Local domain state in `features/<domain>/`:

```typescript
// features/menuItem/slice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

type MenuItemState = {
  selectedId: string | null;
  editMode: boolean;
};

const initialState: MenuItemState = {
  selectedId: null,
  editMode: false,
};

export const menuItemSlice = createSlice({
  name: 'menuItem',
  initialState,
  reducers: {
    selectItem: (state, action: PayloadAction<string>) => {
      state.selectedId = action.payload;
    },
    toggleEditMode: (state) => {
      state.editMode = !state.editMode;
    },
  },
});

export const { selectItem, toggleEditMode } = menuItemSlice.actions;
export const menuItemReducer = menuItemSlice.reducer;
```

**Rules:**
- One slice per domain feature
- Use `PayloadAction<T>` for type safety
- Keep slices small (avoid mega-reducers)
- Redux for UI state (not server state)

#### Generated API Services
API services are **auto-generated** from OpenAPI specs:

```typescript
// Generated: services/brickApi.ts
import { emptySplitApi } from './emptyApi';

export const brickApi = emptySplitApi.injectEndpoints({
  endpoints: (builder) => ({
    listBricks: builder.query<BrickListResponse, void>({
      query: () => '/brick',
    }),
    getBrickById: builder.query<Brick, string>({
      query: (id) => `/brick/${id}`,
    }),
    createBrick: builder.mutation<Brick, BrickCreationForm>({
      query: (form) => ({
        url: '/brick',
        method: 'POST',
        body: form,
      }),
      invalidatesTags: ['Brick'], // Auto-refetch list
    }),
  }),
});

export const { useListBricksQuery, useGetBrickByIdQuery, useCreateBrickMutation } = brickApi;
```

**CRITICAL:** Generated files are **read-only**. Do not manually edit.

To regenerate after API changes:
```bash
pnpm -C data-web run openapi:pull
pnpm -C data-web run rtk:codegen
```

---

## Form Handling with React Hook Form

Forms use **react-hook-form** + **MUI** components + **zod** (optional validation).

```typescript
import { useForm, useFormContext, FormProvider } from 'react-hook-form';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

type BrickFormValues = {
  number: string;
  title: string;
  tags: string[];
  imageBase64?: string;
};

export const BrickForm: FC<{ onSubmit: (data: BrickFormValues) => Promise<void> }> = ({ onSubmit }) => {
  const methods = useForm<BrickFormValues>({
    defaultValues: {
      number: '',
      title: '',
      tags: [],
    },
  });

  return (
    <FormProvider {...methods}>
      <form onSubmit={methods.handleSubmit(onSubmit)}>
        <Stack spacing={2}>
          <BrickNumberField />
          <BrickTitleField />
          <BrickTagsField />
          <Button type="submit" variant="contained">
            Save
          </Button>
        </Stack>
      </form>
    </FormProvider>
  );
};

// Field component (extracts logic)
const BrickNumberField: FC = () => {
  const {
    register,
    formState: { errors },
  } = useFormContext<BrickFormValues>();

  return (
    <TextField
      label=\"Brick Number\"
      fullWidth
      error={Boolean(errors.number)}
      helperText={errors.number?.message ?? 'Unique brick reference.'}
      {...register('number', {
        required: 'Number is required.',
        validate: (value) => value.trim().length > 0 || 'Number cannot be empty.',
      })}
    />
  );
};
```

**Rules:**
- Use `FormProvider` to share form state across nested components
- Extract field components to keep forms readable
- Validation in `register()` or via external schema
- Use MUI's `error` and `helperText` props for error display
- Loading states managed via `useFormState()` or dedicated Redux slice

---

## Responsive Design (Mobile-First)

### MUI sx Prop for Styling
Use `sx` prop for inline styles (preferred):

```typescript
<Box
  sx={{
    display: 'flex',
    flexDirection: { xs: 'column', md: 'row' },
    gap: 2,
  }}
>
  {/* Children */}
</Box>
```

### Desktop Table vs Mobile Card View
**Desktop:**
```typescript
import { DataGrid, GridColDef } from '@mui/x-data-grid';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 100 },
  { field: 'title', headerName: 'Title', width: 300 },
  // ...
];

export const BrickTable: FC<{ bricks: Brick[] }> = ({ bricks }) => (
  <DataGrid rows={bricks} columns={columns} />
);
```

**Mobile:**
```typescript
export const BrickCardList: FC<{ bricks: Brick[] }> = ({ bricks }) => (
  <Stack spacing={2}>
    {bricks.map((brick) => (
      <BrickCard key={brick.id} brick={brick} />
    ))}
  </Stack>
);
```

**Page-level Decision:**
```typescript
import { useMediaQuery } from '@mui/material';

export const BrickListPage: FC = () => {
  const isDesktop = useMediaQuery('(min-width: 960px)');
  const { data } = useListBricksQuery();

  return isDesktop ? (
    <BrickTable bricks={data?.items ?? []} />
  ) : (
    <BrickCardList bricks={data?.items ?? []} />
  );
};
```

**Rules:**
- Mobile-first: stack vertically, minimal horizontal scrolling
- Use MUI breakpoints (`xs`, `sm`, `md`, `lg`, `xl`)
- Maintain feature parity between views
- Touch-friendly targets (buttons ≥ 48px)

### List Printing UX (Required)
All data list pages must provide a print action with two modes:
- Print filtered results (uses current search/filter/sort context)
- Print all results (ignores active filters when allowed by business rules)

Recommended implementation rules:
- Add a visible `Print` action in page-level toolbar
- Keep print output aligned with backend response to avoid data mismatch
- Use a print-focused view (`@media print`) that hides navigation/actions
- Include print metadata: domain title, timestamp, and printed total

```typescript
type PrintMode = 'filtered' | 'all';

const handlePrint = async (mode: PrintMode) => {
  const payload = {
    mode,
    filters: mode === 'filtered' ? activeFilters : undefined,
    sort: activeSort,
  };

  const response = await printList(payload).unwrap();
  setPrintData(response);
  window.print();
};
```

### MUI v9 Typography & Styling
```typescript
import Typography from '@mui/material/Typography';

// GOOD: Use sx for fontWeight
<Typography sx={{ fontWeight: 600 }}>Bold text</Typography>

// AVOID: Direct prop fontWeight (TS overload issues)
// <Typography fontWeight={600}>Bold</Typography>
```

---

## Hooks & Custom Logic

### Custom Hooks Pattern
Extract reusable logic into custom hooks:

```typescript
// useSectionFormControllers.ts
export const useSectionFormControllers = () => {
  const [editingId, setEditingId] = useState<string | null>(null);
  const { data: sections } = useListSectionsQuery();
  const [createSection] = useCreateSectionMutation();
  const [updateSection] = useUpdateSectionMutation();

  const handleCreate = async (form: SectionCreationForm) => {
    await createSection(form).unwrap();
  };

  return {
    editingId,
    setEditingId,
    sections,
    handleCreate,
    // ...
  };
};
```

**Rules:**
- Custom hooks prefix with `use`
- Encapsulate related state + effects
- Return object for easy extension
- No side effects on initial call (wrap in `useEffect`)

---

## Error Handling

Errors are captured by RTK Query and displayed via Redux middleware:

```typescript
// middleware/rtkQueryErrorSnackbarMiddleware.ts
// Auto-catches API errors, dispatches snackbar action

// Usage: Any API error → snackbar appears automatically
```

**Manual error handling:**
```typescript
const [createBrick] = useCreateBrickMutation();

const handleSubmit = async (form: BrickCreationForm) => {
  try {
    await createBrick(form).unwrap();
    // Success
  } catch (error) {
    // Handle error (already shown in snackbar)
    console.error('Failed to create brick:', error);
  }
};
```

---

## Accessibility (A11y)

**Required for all components:**
- Semantic HTML (`<button>`, `<form>`, `<label>`)
- ARIA labels for icons: `<IconButton aria-label="delete" />`
- Color not only distinguisher (use patterns + text)
- Keyboard navigation support (MUI buttons by default)
- Focus visible indicators
- Alt text for images

```typescript
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';

<IconButton
  aria-label="delete brick"
  onClick={() => deleteBrick(id)}
>
  <DeleteIcon />
</IconButton>
```

---

## Testing Components

Test files colocated with components or in `__tests__` folder:

```typescript
// BrickForm.test.tsx
import { render, screen } from '@testing-library/react';
import { BrickForm } from './BrickForm';

describe('BrickForm', () => {
  it('should display the brick number field', () => {
    render(<BrickForm onSubmit={vi.fn()} />);
    expect(screen.getByLabelText(/brick number/i)).toBeInTheDocument();
  });
});
```

See [Testing Conventions](./03-testing.md) for full details.

---

## References

- [State Management in Redux Toolkit Docs](https://redux-toolkit.js.org/)
- [RTK Query Docs](https://redux-toolkit.js.org/rtk-query/overview)
- [React Hook Form Docs](https://react-hook-form.com/)
- [MUI Component API](https://mui.com/material-ui/getting-started/)
- [Architecture Guide](./00-architecture.md)
