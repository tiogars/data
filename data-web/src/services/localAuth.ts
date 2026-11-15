import { getGatewayBaseUrl } from './runtimeConfig';

export const LOCAL_SESSION_STORAGE_KEY = 'auth-local-session';
export const LOCAL_SESSION_CHANGED_EVENT = 'auth-local-session-changed';

export interface LocalAuthSession {
  accessToken: string;
  /** Date d expiration du jeton, en millisecondes depuis l epoch. */
  expiresAt: number;
  username: string;
  role: string;
}

interface LoginResponsePayload {
  accessToken?: string;
  expiresIn?: number;
  username?: string;
  role?: string;
}

const canUseLocalStorage = () => typeof globalThis.window !== 'undefined' && Boolean(globalThis.window.localStorage);

const stripTrailingSlashes = (value: string) => {
  let normalized = value;
  while (normalized.endsWith('/')) {
    normalized = normalized.slice(0, -1);
  }
  return normalized;
};

const removeStoredSession = () => {
  if (!canUseLocalStorage()) {
    return;
  }

  try {
    globalThis.window.localStorage.removeItem(LOCAL_SESSION_STORAGE_KEY);
  } catch {
    // Ignore localStorage failures (private mode, browser policy, etc.)
  }
};

const notifySessionChanged = () => {
  if (typeof globalThis.window !== 'undefined') {
    globalThis.window.dispatchEvent(new Event(LOCAL_SESSION_CHANGED_EVENT));
  }
};

/** Supprime la session locale et previent les composants qui l affichent. */
export const clearLocalSession = () => {
  removeStoredSession();
  notifySessionChanged();
};

const isValidSession = (value: unknown): value is LocalAuthSession => {
  if (typeof value !== 'object' || value === null) {
    return false;
  }

  const candidate = value as Partial<LocalAuthSession>;
  return (
    typeof candidate.accessToken === 'string' &&
    candidate.accessToken.length > 0 &&
    typeof candidate.expiresAt === 'number' &&
    typeof candidate.username === 'string' &&
    typeof candidate.role === 'string'
  );
};

/** Retourne la session locale tant que le jeton n est pas expire, sinon la supprime et retourne null. */
export const getLocalSession = (): LocalAuthSession | null => {
  if (!canUseLocalStorage()) {
    return null;
  }

  try {
    const raw = globalThis.window.localStorage.getItem(LOCAL_SESSION_STORAGE_KEY);
    if (!raw) {
      return null;
    }

    const parsed: unknown = JSON.parse(raw);
    if (isValidSession(parsed) && parsed.expiresAt > Date.now()) {
      return parsed;
    }
  } catch {
    // Session illisible: elle est supprimee ci-dessous.
  }

  removeStoredSession();
  return null;
};

export const getLocalAccessToken = (): string | null => getLocalSession()?.accessToken ?? null;

const buildLoginErrorMessage = async (response: Response) => {
  if (response.status === 401) {
    return 'Nom utilisateur ou mot de passe incorrect.';
  }

  if (response.status === 429) {
    return 'Trop de tentatives de connexion. Reessayez dans quelques instants.';
  }

  if (response.status === 400) {
    try {
      const body = (await response.text()).trim();
      if (body) {
        return body;
      }
    } catch {
      // Message generique ci-dessous.
    }
  }

  return `Echec de la connexion (${response.status}).`;
};

/** Authentifie un compte utilisateur enregistre en base via la gateway et memorise la session. */
export const loginWithCredentials = async (username: string, password: string): Promise<LocalAuthSession> => {
  const endpoint = `${stripTrailingSlashes(getGatewayBaseUrl())}/auth/login`;

  let response: Response;
  try {
    response = await fetch(endpoint, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', Accept: 'application/json' },
      body: JSON.stringify({ username, password }),
    });
  } catch {
    throw new Error(`Impossible de joindre la gateway (${endpoint}).`);
  }

  if (!response.ok) {
    throw new Error(await buildLoginErrorMessage(response));
  }

  const payload = (await response.json()) as LoginResponsePayload;
  if (!payload.accessToken || typeof payload.expiresIn !== 'number') {
    throw new Error('Reponse de connexion invalide.');
  }

  const session: LocalAuthSession = {
    accessToken: payload.accessToken,
    expiresAt: Date.now() + payload.expiresIn * 1000,
    username: payload.username ?? username,
    role: payload.role ?? '',
  };

  if (canUseLocalStorage()) {
    try {
      globalThis.window.localStorage.setItem(LOCAL_SESSION_STORAGE_KEY, JSON.stringify(session));
    } catch {
      throw new Error('Impossible de memoriser la session dans le navigateur.');
    }
  }

  notifySessionChanged();
  return session;
};
