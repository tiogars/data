import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from 'react';
import type { User } from 'oidc-client-ts';
import {
  completeLoginRedirect,
  completeLogoutRedirect,
  getOidcUser,
  getOidcUserManager,
  signinSilent,
  startLoginRedirect,
  startLogoutRedirect,
} from '../services/oidcAuth';
import {
  LOCAL_SESSION_CHANGED_EVENT,
  LOCAL_SESSION_STORAGE_KEY,
  clearLocalSession,
  getLocalSession,
  loginWithCredentials as requestLocalLogin,
  type LocalAuthSession,
} from '../services/localAuth';
import { isOidcAuthEnabled } from '../services/runtimeConfig';

export type AuthMode = 'oidc' | 'local';

interface OidcAuthContextValue {
  /** Utilisateur OIDC (null en mode compte utilisateur base de donnees). */
  user: User | null;
  /** Mode actif: URL d authentification OIDC ou compte utilisateur enregistre en base. */
  authMode: AuthMode;
  /** Nom de connexion de l utilisateur authentifie, quel que soit le mode. */
  username: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  errorMessage: string | null;
  refreshUser: () => Promise<void>;
  login: () => Promise<void>;
  loginWithCredentials: (username: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  handleSigninCallback: () => Promise<void>;
  handleSignoutCallback: () => Promise<void>;
}

const OidcAuthContext = createContext<OidcAuthContextValue | undefined>(undefined);

interface OidcAuthProviderProps {
  children: ReactNode;
}

// setTimeout plafonne les delais a 2^31-1 ms.
const MAX_TIMEOUT_MS = 2_147_483_647;

const resolveAuthMode = (): AuthMode => (isOidcAuthEnabled() ? 'oidc' : 'local');

const goToLoginPage = () => {
  globalThis.window.location.assign(`${import.meta.env.BASE_URL}login`);
};

export const OidcAuthProvider = ({ children }: OidcAuthProviderProps) => {
  const [authMode, setAuthMode] = useState<AuthMode>(resolveAuthMode);
  const [user, setUser] = useState<User | null>(null);
  const [localSession, setLocalSession] = useState<LocalAuthSession | null>(() =>
    resolveAuthMode() === 'local' ? getLocalSession() : null,
  );
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const refreshUser = useCallback(async () => {
    setIsLoading(true);
    const nextMode = resolveAuthMode();
    setAuthMode(nextMode);
    try {
      if (nextMode === 'oidc') {
        setUser(await getOidcUser());
        setLocalSession(null);
      } else {
        setUser(null);
        setLocalSession(getLocalSession());
      }
      setErrorMessage(null);
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Echec de lecture de session OIDC.');
      setUser(null);
      setLocalSession(null);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const login = useCallback(async () => {
    setErrorMessage(null);
    if (resolveAuthMode() === 'local') {
      goToLoginPage();
      return;
    }
    await startLoginRedirect();
  }, []);

  const loginWithCredentials = useCallback(async (username: string, password: string) => {
    const session = await requestLocalLogin(username, password);
    setLocalSession(session);
    setErrorMessage(null);
  }, []);

  const logout = useCallback(async () => {
    setErrorMessage(null);
    if (resolveAuthMode() === 'local') {
      clearLocalSession();
      setLocalSession(null);
      goToLoginPage();
      return;
    }
    await startLogoutRedirect();
  }, []);

  const handleSigninCallback = useCallback(async () => {
    setIsLoading(true);
    try {
      await completeLoginRedirect();
      const nextUser = await getOidcUser();
      setUser(nextUser);
      setErrorMessage(null);
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Echec du callback de connexion OIDC.');
      setUser(null);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, []);

  const handleSignoutCallback = useCallback(async () => {
    setIsLoading(true);
    try {
      await completeLogoutRedirect();
      setUser(null);
      setErrorMessage(null);
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Echec du callback de deconnexion OIDC.');
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    void refreshUser();
  }, [refreshUser]);

  useEffect(() => {
    if (authMode !== 'oidc') {
      return undefined;
    }

    const manager = getOidcUserManager();
    const handleUserLoaded = (loadedUser: User) => {
      setUser(loadedUser);
      setErrorMessage(null);
      setIsLoading(false);
    };

    const handleUserUnloaded = () => {
      setUser(null);
      setIsLoading(false);
    };

    const handleSilentRenewError = (error: Error) => {
      setErrorMessage(error.message);
    };

    const handleAccessTokenExpiring = () => {
      void signinSilent()
        .then((renewedUser) => {
          if (renewedUser) {
            setUser(renewedUser);
            setErrorMessage(null);
          }
        })
        .catch((error: unknown) => {
          setErrorMessage(error instanceof Error ? error.message : 'Echec du renouvellement silencieux.');
        });
    };

    manager.events.addUserLoaded(handleUserLoaded);
    manager.events.addUserUnloaded(handleUserUnloaded);
    manager.events.addSilentRenewError(handleSilentRenewError);
    manager.events.addAccessTokenExpiring(handleAccessTokenExpiring);

    return () => {
      manager.events.removeUserLoaded(handleUserLoaded);
      manager.events.removeUserUnloaded(handleUserUnloaded);
      manager.events.removeSilentRenewError(handleSilentRenewError);
      manager.events.removeAccessTokenExpiring(handleAccessTokenExpiring);
    };
  }, [authMode]);

  useEffect(() => {
    if (authMode !== 'local') {
      return undefined;
    }

    // Reflete les connexions/deconnexions faites dans cet onglet ou dans un autre.
    const syncSession = () => setLocalSession(getLocalSession());
    const handleStorage = (event: StorageEvent) => {
      if (event.key === null || event.key === LOCAL_SESSION_STORAGE_KEY) {
        syncSession();
      }
    };

    globalThis.window.addEventListener(LOCAL_SESSION_CHANGED_EVENT, syncSession);
    globalThis.window.addEventListener('storage', handleStorage);

    return () => {
      globalThis.window.removeEventListener(LOCAL_SESSION_CHANGED_EVENT, syncSession);
      globalThis.window.removeEventListener('storage', handleStorage);
    };
  }, [authMode]);

  useEffect(() => {
    if (authMode !== 'local' || !localSession) {
      return undefined;
    }

    const delay = Math.min(Math.max(localSession.expiresAt - Date.now(), 0), MAX_TIMEOUT_MS);
    const timer = globalThis.window.setTimeout(() => {
      setLocalSession(getLocalSession());
    }, delay);

    return () => globalThis.window.clearTimeout(timer);
  }, [authMode, localSession]);

  const value = useMemo<OidcAuthContextValue>(() => {
    const oidcUsername = typeof user?.profile?.preferred_username === 'string' ? user.profile.preferred_username : null;

    return {
      user,
      authMode,
      username: authMode === 'local' ? (localSession?.username ?? null) : oidcUsername,
      isAuthenticated: authMode === 'local' ? Boolean(localSession) : Boolean(user && !user.expired),
      isLoading,
      errorMessage,
      refreshUser,
      login,
      loginWithCredentials,
      logout,
      handleSigninCallback,
      handleSignoutCallback,
    };
  }, [
    authMode,
    errorMessage,
    handleSigninCallback,
    handleSignoutCallback,
    isLoading,
    localSession,
    login,
    loginWithCredentials,
    logout,
    refreshUser,
    user,
  ]);

  return <OidcAuthContext.Provider value={value}>{children}</OidcAuthContext.Provider>;
};

export const useOidcAuth = () => {
  const context = useContext(OidcAuthContext);
  if (!context) {
    throw new Error('useOidcAuth doit etre utilise dans OidcAuthProvider');
  }

  return context;
};
