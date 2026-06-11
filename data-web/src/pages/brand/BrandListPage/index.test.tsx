import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { BrandListPage } from './index';

const {
  useSearchBrandsQueryMock,
  useDeleteBrandMutationMock,
  useImportBrandsMutationMock,
  useLazyExportBrandsQueryMock,
} = vi.hoisted(() => ({
  useSearchBrandsQueryMock: vi.fn(),
  useDeleteBrandMutationMock: vi.fn(),
  useImportBrandsMutationMock: vi.fn(),
  useLazyExportBrandsQueryMock: vi.fn(),
}));

vi.mock('../../../services/brandApi', () => ({
  useSearchBrandsQuery: useSearchBrandsQueryMock,
  useDeleteBrandMutation: useDeleteBrandMutationMock,
  useImportBrandsMutation: useImportBrandsMutationMock,
  brandApi: {
    useLazyExportBrandsQuery: useLazyExportBrandsQueryMock,
  },
}));

describe('BrandListPage', () => {
  beforeEach(() => {
    const refetch = vi.fn();
    useSearchBrandsQueryMock.mockImplementation((args: { page?: number; size?: number; q?: string }) => ({
      data: {
        items: [{ id: 'brand-1', name: 'LEGO', description: 'Briques' }],
        count: 25,
        page: args?.page ?? 0,
        size: args?.size ?? 10,
        query: args?.q,
      },
      isLoading: false,
      error: undefined,
      refetch,
    }));
    useDeleteBrandMutationMock.mockReturnValue([vi.fn(), { isLoading: false }]);
    useImportBrandsMutationMock.mockReturnValue([vi.fn(), { isLoading: false }]);
    useLazyExportBrandsQueryMock.mockReturnValue([vi.fn(), { isFetching: false }]);
  });

  it('conserve les actions metier et envoie q/page/size a la recherche', async () => {
    const user = userEvent.setup();

    render(
      <MemoryRouter>
        <BrandListPage />
      </MemoryRouter>
    );

    expect(screen.getByRole('button', { name: /Export JSON/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Import JSON/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Import texte/i })).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /Nouvelle marque/i })).toBeInTheDocument();

    const search = screen.getByLabelText('Recherche');
    await user.type(search, 'lego');

    await waitFor(() => {
      expect(useSearchBrandsQueryMock).toHaveBeenLastCalledWith(
        expect.objectContaining({ page: 0, size: 10, q: 'lego' }),
        expect.any(Object)
      );
    });

    const nextButtons = screen.getAllByLabelText(/next page|page suivante/i);
    await user.click(nextButtons[0]);

    await waitFor(() => {
      expect(useSearchBrandsQueryMock).toHaveBeenLastCalledWith(
        expect.objectContaining({ page: 1, size: 10 }),
        expect.any(Object)
      );
    });
  });
});
