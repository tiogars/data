import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, expect, it, vi } from 'vitest';
import Header from './index';

vi.mock('../../auth/OidcAuthProvider', () => ({
  useOidcAuth: () => ({
    user: null,
    isAuthenticated: false,
    isLoading: false,
    errorMessage: null,
    refreshUser: vi.fn(),
    login: vi.fn(),
    logout: vi.fn(),
    handleSigninCallback: vi.fn(),
    handleSignoutCallback: vi.fn(),
  }),
}));

vi.mock('../Breadcrumbs', () => ({
  default: () => <div>Breadcrumbs</div>,
}));

vi.mock('../ThemeModeSelector', () => ({
  default: () => <div>ThemeModeSelector</div>,
}));

describe('Header', () => {
  it('renders a documentation icon link in the header', () => {
    render(
      <MemoryRouter>
        <Header onMenuClick={vi.fn()} />
      </MemoryRouter>,
    );

    const documentationLink = screen.getByLabelText(/documentation/i);

    expect(documentationLink).toHaveAttribute('href', 'https://docs.data.tiogars.fr');
    expect(documentationLink).toHaveAttribute('target', '_blank');
    expect(documentationLink).toHaveAttribute('rel', expect.stringContaining('noopener'));
  });

  it('renders a GitHub issue link in the header', () => {
    render(
      <MemoryRouter>
        <Header onMenuClick={vi.fn()} />
      </MemoryRouter>,
    );

    const issueLink = screen.getByLabelText(/signaler un problème/i);

    expect(issueLink).toHaveAttribute('href', 'https://github.com/tiogars/data/issues/new');
    expect(issueLink).toHaveAttribute('target', '_blank');
    expect(issueLink).toHaveAttribute('rel', expect.stringContaining('noopener'));
  });
});
