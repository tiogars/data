import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { GtinListPage } from './index';

const {
  useSearchGtinsQueryMock,
  useDeleteGtinMutationMock,
  useDeleteAllGtinsMutationMock,
  useImportGtinsMutationMock,
  useImportGtinsCsvMutationMock,
  useLazyExportGtinsQueryMock,
  useLazyExportGtinsCsvTextQueryMock,
} = vi.hoisted(() => ({
  useSearchGtinsQueryMock: vi.fn(),
  useDeleteGtinMutationMock: vi.fn(),
  useDeleteAllGtinsMutationMock: vi.fn(),
  useImportGtinsMutationMock: vi.fn(),
  useImportGtinsCsvMutationMock: vi.fn(),
  useLazyExportGtinsQueryMock: vi.fn(),
  useLazyExportGtinsCsvTextQueryMock: vi.fn(),
}));

vi.mock('../../../services/gtinApi', () => ({
  useSearchGtinsQuery: useSearchGtinsQueryMock,
  useDeleteGtinMutation: useDeleteGtinMutationMock,
  useDeleteAllGtinsMutation: useDeleteAllGtinsMutationMock,
  useImportGtinsMutation: useImportGtinsMutationMock,
  useImportGtinsCsvMutation: useImportGtinsCsvMutationMock,
  gtinApi: {
    useLazyExportGtinsQuery: useLazyExportGtinsQueryMock,
  },
}));

vi.mock('../../../services/gtinCsvApi', () => ({
  useLazyExportGtinsCsvTextQuery: useLazyExportGtinsCsvTextQueryMock,
}));

describe('GtinListPage', () => {
  beforeEach(() => {
    const refetch = vi.fn();
    useSearchGtinsQueryMock.mockImplementation((args: { page?: number; size?: number; q?: string }) => ({
      data: {
        items: [{ id: 'gtin-1', code: '123456', description: 'Produit' }],
        count: 40,
        page: args?.page ?? 0,
        size: args?.size ?? 10,
        query: args?.q,
      },
      isLoading: false,
      error: undefined,
      refetch,
    }));
    useDeleteGtinMutationMock.mockReturnValue([vi.fn(), { isLoading: false }]);
    useDeleteAllGtinsMutationMock.mockReturnValue([vi.fn(), { isLoading: false }]);
    useImportGtinsMutationMock.mockReturnValue([vi.fn(), { isLoading: false }]);
    useImportGtinsCsvMutationMock.mockReturnValue([vi.fn(), { isLoading: false }]);
    useLazyExportGtinsQueryMock.mockReturnValue([vi.fn(), { isFetching: false }]);
    useLazyExportGtinsCsvTextQueryMock.mockReturnValue([vi.fn(), { isFetching: false }]);
  });

  it('conserve les actions metier et met a jour q/page/size', async () => {
    const user = userEvent.setup();

    render(
      <MemoryRouter>
        <GtinListPage />
      </MemoryRouter>
    );

    expect(screen.getByRole('button', { name: /Export JSON/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Export CSV/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Import JSON/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Import CSV/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Tout supprimer/i })).toBeInTheDocument();

    const search = screen.getByLabelText('Recherche');
    await user.type(search, '123');

    await waitFor(() => {
      expect(useSearchGtinsQueryMock).toHaveBeenLastCalledWith(
        expect.objectContaining({ page: 0, size: 10, q: '123' }),
        expect.any(Object)
      );
    });

    const nextButtons = screen.getAllByLabelText(/next page|page suivante/i);
    await user.click(nextButtons[0]);

    await waitFor(() => {
      expect(useSearchGtinsQueryMock).toHaveBeenLastCalledWith(
        expect.objectContaining({ page: 1, size: 10 }),
        expect.any(Object)
      );
    });
  });
});
