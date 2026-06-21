import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { UserAccountListPage } from './index';

const {
  useSearchUserAccountsQueryMock,
  useDeleteUserAccountMutationMock,
} = vi.hoisted(() => ({
  useSearchUserAccountsQueryMock: vi.fn(),
  useDeleteUserAccountMutationMock: vi.fn(),
}));

vi.mock('../../../services/userAccountApi', () => ({
  useSearchUserAccountsQuery: useSearchUserAccountsQueryMock,
  useDeleteUserAccountMutation: useDeleteUserAccountMutationMock,
}));

describe('UserAccountListPage', () => {
  beforeEach(() => {
    const refetch = vi.fn();
    useSearchUserAccountsQueryMock.mockImplementation((args: { page?: number; size?: number; q?: string }) => ({
      data: {
        items: [{ id: 'user-1', username: 'admin', role: 'ADMIN', enabled: true }],
        count: 25,
        page: args?.page ?? 0,
        size: args?.size ?? 10,
        query: args?.q,
      },
      isLoading: false,
      error: undefined,
      refetch,
    }));
    useDeleteUserAccountMutationMock.mockReturnValue([vi.fn(), { isLoading: false }]);
  });

  it('envoie q/page/size a la recherche et conserve l action de creation', async () => {
    const user = userEvent.setup();

    render(
      <MemoryRouter>
        <UserAccountListPage />
      </MemoryRouter>
    );

    expect(screen.getByRole('link', { name: /Nouveau compte/i })).toBeInTheDocument();

    const search = screen.getByLabelText('Recherche');
    await user.type(search, 'admin');

    await waitFor(() => {
      expect(useSearchUserAccountsQueryMock).toHaveBeenLastCalledWith(
        expect.objectContaining({ page: 0, size: 10, q: 'admin' }),
        expect.any(Object)
      );
    });

    const nextButtons = screen.getAllByLabelText(/next page|page suivante/i);
    await user.click(nextButtons[0]);

    await waitFor(() => {
      expect(useSearchUserAccountsQueryMock).toHaveBeenLastCalledWith(
        expect.objectContaining({ page: 1, size: 10 }),
        expect.any(Object)
      );
    });
  });
});
