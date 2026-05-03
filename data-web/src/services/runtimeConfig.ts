export const API_BASE_URL_STORAGE_KEY = 'api-base-url';
export const API_BASE_URL_DEFAULT = 'http://localhost:8081';

export const AUTH_BASE_URL_STORAGE_KEY = 'auth-base-url';
export const AUTH_BASE_URL_DEFAULT = 'https://auth2.tiogars.fr/';

export const AUTH_REALM_STORAGE_KEY = 'auth-realm';
export const AUTH_REALM_DEFAULT = 'data';

export const AUTH_CLIENT_ID_STORAGE_KEY = 'auth-client-id';
export const AUTH_CLIENT_ID_DEFAULT = 'data-web';

export const AUTH_SCOPE_STORAGE_KEY = 'auth-scope';
export const AUTH_SCOPE_DEFAULT = 'openid profile email';

const envApiBaseUrl = import.meta.env.VITE_API_BASE_URL?.trim();

export const isGatewayBaseUrlOverriddenByEnv = () => Boolean(envApiBaseUrl);

const canUseLocalStorage = () => typeof globalThis.window !== 'undefined' && Boolean(globalThis.window.localStorage);

const readFromLocalStorage = (key: string) => {
  if (!canUseLocalStorage()) {
    return null;
  }

  try {
    const value = globalThis.window.localStorage.getItem(key)?.trim();
    return value || null;
  } catch {
    return null;
  }
};

const writeToLocalStorage = (key: string, value: string) => {
  if (!canUseLocalStorage()) {
    return;
  }

  try {
    globalThis.window.localStorage.setItem(key, value);
  } catch {
    // Ignore localStorage write failures (private mode, browser policy, etc.)
  }
};

const normalizePathPart = (value: string) => value.trim().replace(/^\/+|\/+$/g, '');

const ensureTrailingSlash = (value: string) => (value.endsWith('/') ? value : `${value}/`);

export const getGatewayBaseUrl = () => {
  if (envApiBaseUrl) {
    return envApiBaseUrl;
  }

  const saved = readFromLocalStorage(API_BASE_URL_STORAGE_KEY);
  if (saved) {
    return saved;
  }

  writeToLocalStorage(API_BASE_URL_STORAGE_KEY, API_BASE_URL_DEFAULT);
  return API_BASE_URL_DEFAULT;
};

export const setGatewayBaseUrl = (value: string) => {
  if (envApiBaseUrl) {
    return;
  }

  const normalizedValue = value.trim();
  if (!normalizedValue) {
    return;
  }

  writeToLocalStorage(API_BASE_URL_STORAGE_KEY, normalizedValue);
};

export const resetGatewayBaseUrl = () => {
  if (envApiBaseUrl) {
    return;
  }

  writeToLocalStorage(API_BASE_URL_STORAGE_KEY, API_BASE_URL_DEFAULT);
};

export const getAuthBaseUrl = () => {
  const saved = readFromLocalStorage(AUTH_BASE_URL_STORAGE_KEY);
  if (saved) {
    return ensureTrailingSlash(saved);
  }

  writeToLocalStorage(AUTH_BASE_URL_STORAGE_KEY, AUTH_BASE_URL_DEFAULT);
  return AUTH_BASE_URL_DEFAULT;
};

export const setAuthBaseUrl = (value: string) => {
  const normalizedValue = value.trim();
  if (!normalizedValue) {
    return;
  }

  writeToLocalStorage(AUTH_BASE_URL_STORAGE_KEY, ensureTrailingSlash(normalizedValue));
};

export const resetAuthBaseUrl = () => {
  writeToLocalStorage(AUTH_BASE_URL_STORAGE_KEY, AUTH_BASE_URL_DEFAULT);
};

export const getAuthRealm = () => {
  const saved = readFromLocalStorage(AUTH_REALM_STORAGE_KEY);
  if (saved) {
    return normalizePathPart(saved);
  }

  writeToLocalStorage(AUTH_REALM_STORAGE_KEY, AUTH_REALM_DEFAULT);
  return AUTH_REALM_DEFAULT;
};

export const setAuthRealm = (value: string) => {
  const normalizedValue = normalizePathPart(value);
  if (!normalizedValue) {
    return;
  }

  writeToLocalStorage(AUTH_REALM_STORAGE_KEY, normalizedValue);
};

export const resetAuthRealm = () => {
  writeToLocalStorage(AUTH_REALM_STORAGE_KEY, AUTH_REALM_DEFAULT);
};

export const getAuthClientId = () => {
  const saved = readFromLocalStorage(AUTH_CLIENT_ID_STORAGE_KEY);
  if (saved) {
    return saved;
  }

  writeToLocalStorage(AUTH_CLIENT_ID_STORAGE_KEY, AUTH_CLIENT_ID_DEFAULT);
  return AUTH_CLIENT_ID_DEFAULT;
};

export const setAuthClientId = (value: string) => {
  const normalizedValue = value.trim();
  if (!normalizedValue) {
    return;
  }

  writeToLocalStorage(AUTH_CLIENT_ID_STORAGE_KEY, normalizedValue);
};

export const resetAuthClientId = () => {
  writeToLocalStorage(AUTH_CLIENT_ID_STORAGE_KEY, AUTH_CLIENT_ID_DEFAULT);
};

export const getAuthScope = () => {
  const saved = readFromLocalStorage(AUTH_SCOPE_STORAGE_KEY);
  if (saved) {
    return saved;
  }

  writeToLocalStorage(AUTH_SCOPE_STORAGE_KEY, AUTH_SCOPE_DEFAULT);
  return AUTH_SCOPE_DEFAULT;
};

export const setAuthScope = (value: string) => {
  const normalizedValue = value.trim();
  if (!normalizedValue) {
    return;
  }

  writeToLocalStorage(AUTH_SCOPE_STORAGE_KEY, normalizedValue);
};

export const resetAuthScope = () => {
  writeToLocalStorage(AUTH_SCOPE_STORAGE_KEY, AUTH_SCOPE_DEFAULT);
};

export const resetOidcRuntimeConfig = () => {
  resetAuthBaseUrl();
  resetAuthRealm();
  resetAuthClientId();
  resetAuthScope();
};

export const buildAuthorityUrl = () => {
  const authBaseUrl = getAuthBaseUrl();
  const realm = getAuthRealm();
  return `${ensureTrailingSlash(authBaseUrl)}realms/${realm}`;
};

export const buildProviderAccountUrl = () => `${buildAuthorityUrl()}/account`;
