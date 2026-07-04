import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { type ReactNode } from 'react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import { SectionListPage } from './index';

const {
  useListSectionsQueryMock,
  useDeleteSectionByIdMutationMock,
  useListSectionDocumentsQueryMock,
} = vi.hoisted(() => ({
  useListSectionsQueryMock: vi.fn(),
  useDeleteSectionByIdMutationMock: vi.fn(),
  useListSectionDocumentsQueryMock: vi.fn(),
}));

vi.mock('../../../services/sectionApi', () => ({
  useListSectionsQuery: useListSectionsQueryMock,
  useDeleteSectionByIdMutation: useDeleteSectionByIdMutationMock,
}));

vi.mock('../../../services/sectionDocumentApi', () => ({
  useListSectionDocumentsQuery: useListSectionDocumentsQueryMock,
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
  toSectionTree: () => [{ id: 'section-1', name: 'Section A', description: 'Desc', displayOrder: 0, parentId: null, children: [] }],
  flattenSections: () => [{ id: 'section-1', name: 'Section A', description: 'Desc', displayOrder: 0, parentId: null, children: [] }],
  filterSectionTree: (sections: unknown) => sections,
  collectExpandableIds: () => [],
  collectTreeIds: () => ['section-1'],
  findSectionPath: () => ['section-1'],
  formatSectionOrder: () => '0',
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
    useListSectionDocumentsQueryMock.mockReturnValue({
      data: {
        items: [{ id: 'doc-1', name: 'Document A', storagePath: 'doc/a' }],
      },
      isLoading: false,
      error: undefined,
    });

    useListSectionsQueryMock.mockImplementation(() => ({
      data: {
        items: [{ id: 'section-1', name: 'Section A', description: 'Desc', displayOrder: 0, parentId: null }],
        count: 1,
      },
      isLoading: false,
      error: undefined,
      refetch,
    }));
    useDeleteSectionByIdMutationMock.mockReturnValue([vi.fn(), { isLoading: false }]);
  });

  it('affiche une navigation arborescente avec controles d expansion', async () => {
    const user = userEvent.setup();

    render(
      <MemoryRouter>
        <SectionListPage />
      </MemoryRouter>
    );

    expect(screen.getByRole('button', { name: /Nouvelle section/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /^Supprimer$/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Tout déplier/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Tout réduire/i })).toBeInTheDocument();

    const search = screen.getByLabelText('Recherche');
    await user.type(search, 'Section A');

    await waitFor(() => {
      expect((search as HTMLInputElement).value).toBe('Section A');
    });
  });
});
