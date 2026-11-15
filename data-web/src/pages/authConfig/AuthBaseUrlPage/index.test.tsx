import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { AuthBaseUrlPage } from './index';
import { AUTH_OIDC_ENABLED_STORAGE_KEY, isOidcAuthEnabled } from '../../../services/runtimeConfig';

const refreshUser = vi.fn().mockResolvedValue(undefined);

vi.mock('../../../auth/OidcAuthProvider', () => ({
  useOidcAuth: () => ({ refreshUser }),
}));

describe('AuthBaseUrlPage', () => {
  beforeEach(() => {
    localStorage.clear();
    refreshUser.mockClear();
  });

  it('shouldDisableOidcFieldsAndPersistFlagWhenSwitchIsTurnedOff', async () => {
    const user = userEvent.setup();
    render(<AuthBaseUrlPage />);

    const toggle = screen.getByRole('switch', { name: /URL d authentification active/i });
    expect(toggle).toBeChecked();
    expect(screen.getByLabelText('URL d authentification')).toBeEnabled();

    await user.click(toggle);

    expect(screen.getByLabelText('URL d authentification')).toBeDisabled();
    expect(screen.getByLabelText('Realm OIDC')).toBeDisabled();
    expect(screen.getByRole('button', { name: /Tester/i })).toBeDisabled();

    await user.click(screen.getByRole('button', { name: 'Enregistrer' }));

    await waitFor(() => expect(refreshUser).toHaveBeenCalledTimes(1));
    expect(isOidcAuthEnabled()).toBe(false);
    expect(localStorage.getItem(AUTH_OIDC_ENABLED_STORAGE_KEY)).toBe('false');
    expect(screen.getByText(/compte utilisateur base de donnees/i, { selector: 'p' })).toBeInTheDocument();
  });

  it('shouldAllowSavingWhenOidcIsDisabledAndFieldsAreInvalid', async () => {
    const user = userEvent.setup();
    render(<AuthBaseUrlPage />);

    fireEvent.change(screen.getByLabelText('Realm OIDC'), { target: { value: '' } });
    expect(screen.getByRole('button', { name: 'Enregistrer' })).toBeDisabled();

    await user.click(screen.getByRole('switch', { name: /URL d authentification active/i }));

    expect(screen.getByRole('button', { name: 'Enregistrer' })).toBeEnabled();
  });

  it('shouldRestoreOidcAuthenticationWhenResetIsClicked', async () => {
    localStorage.setItem(AUTH_OIDC_ENABLED_STORAGE_KEY, 'false');
    const user = userEvent.setup();
    render(<AuthBaseUrlPage />);

    expect(screen.getByRole('switch', { name: /URL d authentification active/i })).not.toBeChecked();

    await user.click(screen.getByRole('button', { name: 'Reinitialiser' }));

    await waitFor(() => expect(refreshUser).toHaveBeenCalledTimes(1));
    expect(isOidcAuthEnabled()).toBe(true);
    expect(screen.getByRole('switch', { name: /URL d authentification active/i })).toBeChecked();
  });
});
