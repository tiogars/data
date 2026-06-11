import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { type ReactNode } from 'react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { SectionListPage } from './index';

const {
  useSearchSectionsQueryMock,
  useDeleteSectionByIdMutationMock,
} = vi.hoisted(() => ({
  useSearchSectionsQueryMock: vi.fn(),
  useDeleteSectionByIdMutationMock: vi.fn(),
}));

vi.mock('../../../services/sectionApi', () => ({
  useSearchSectionsQuery: useSearchSectionsQueryMock,
  useDeleteSectionByIdMutation: useDeleteSectionByIdMutationMock,
}));

vi.mock('../SectionCreatePage', () => ({
  SectionCreatePage: () => <div>SectionCreatePage</div>,
}));

vi.mock('../SectionDetailPage', () => ({
  SectionDetailPage: () => <div>SectionDetailPage</div>,
}));

vi.mock('../SectionEditPage', () => ({
  SectionEditPage: () => <div>SectionEditPage</div>,
}));

vi.mock('./sectionTree', () => ({
  toSectionTree: () => [{ id: 'section-1', name: 'Section A', description: 'Desc', parentId: null, children: [] }],
  flattenSections: () => [{ id: 'section-1', name: 'Section A', description: 'Desc', parentId: null, children: [] }],
  collectExpandableIds: () => [],
}));

vi.mock('@mui/x-tree-view', () => ({
  SimpleTreeView: ({ children }: { children: ReactNode }) => <div>{children}</div>,
  TreeItem: ({ children, label }: { children?: ReactNode; label: ReactNode }) => (
    <div>
      <div>{label}</div>
      {children}
    </div>
  ),
}));

describe('SectionListPage', () => {
  beforeEach(() => {
    const refetch = vi.fn();
    useSearchSectionsQueryMock.mockImplementation((args: { page?: number; size?: number; q?: string }) => ({
      data: {
        items: [{ id: 'section-1', name: 'Section A', description: 'Desc', parentId: null }],
        count: 30,
        page: args?.page ?? 0,
        size: args?.size ?? 20,
        query: args?.q,
      },
      isLoading: false,
      error: undefined,
      refetch,
    }));
    useDeleteSectionByIdMutationMock.mockReturnValue([vi.fn(), { isLoading: false }]);
  });

  it('met a jour la recherche et la pagination tout en gardant les actions de section', async () => {
    const user = userEvent.setup();

    render(<SectionListPage />);

    expect(screen.getByRole('button', { name: /Nouvelle section/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /^Supprimer$/i })).toBeInTheDocument();

    const search = screen.getByLabelText('Recherche');
    await user.type(search, 'Section A');

    await waitFor(() => {
      expect(useSearchSectionsQueryMock).toHaveBeenLastCalledWith(
        expect.objectContaining({ page: 0, size: 20, q: 'Section A' }),
        expect.any(Object)
      );
    });

    const nextButtons = screen.getAllByLabelText(/next page|page suivante/i);
    await user.click(nextButtons[0]);

    await waitFor(() => {
      expect(useSearchSectionsQueryMock).toHaveBeenLastCalledWith(
        expect.objectContaining({ page: 1, size: 20 }),
        expect.any(Object)
      );
    });
  });
});
