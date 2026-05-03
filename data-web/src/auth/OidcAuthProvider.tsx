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

interface OidcAuthContextValue {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  errorMessage: string | null;
  refreshUser: () => Promise<void>;
  login: () => Promise<void>;
  logout: () => Promise<void>;
  handleSigninCallback: () => Promise<void>;
  handleSignoutCallback: () => Promise<void>;
}

const OidcAuthContext = createContext<OidcAuthContextValue | undefined>(undefined);

interface OidcAuthProviderProps {
  children: ReactNode;
}

export const OidcAuthProvider = ({ children }: OidcAuthProviderProps) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const refreshUser = useCallback(async () => {
    setIsLoading(true);
    try {
      const nextUser = await getOidcUser();
      setUser(nextUser);
      setErrorMessage(null);
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Echec de lecture de session OIDC.');
      setUser(null);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const login = useCallback(async () => {
    setErrorMessage(null);
    await startLoginRedirect();
  }, []);

  const logout = useCallback(async () => {
    setErrorMessage(null);
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
  }, [refreshUser]);

  const value = useMemo<OidcAuthContextValue>(
    () => ({
      user,
      isAuthenticated: Boolean(user && !user.expired),
      isLoading,
      errorMessage,
      refreshUser,
      login,
      logout,
      handleSigninCallback,
      handleSignoutCallback,
    }),
    [errorMessage, handleSigninCallback, handleSignoutCallback, isLoading, login, logout, refreshUser, user],
  );

  return <OidcAuthContext.Provider value={value}>{children}</OidcAuthContext.Provider>;
};

export const useOidcAuth = () => {
  const context = useContext(OidcAuthContext);
  if (!context) {
    throw new Error('useOidcAuth doit etre utilise dans OidcAuthProvider');
  }

  return context;
};
