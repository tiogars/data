import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import {
  LOCAL_SESSION_CHANGED_EVENT,
  LOCAL_SESSION_STORAGE_KEY,
  getLocalAccessToken,
  getLocalSession,
  loginWithCredentials,
} from './localAuth';

const jsonResponse = (status: number, body: unknown) =>
  new Response(typeof body === 'string' ? body : JSON.stringify(body), { status });

describe('localAuth', () => {
  beforeEach(() => {
    localStorage.clear();
  });

  afterEach(() => {
    vi.unstubAllGlobals();
    vi.useRealTimers();
  });

  it('shouldStoreSessionAndNotifyWhenLoginSucceeds', async () => {
    const fetchMock = vi.fn().mockResolvedValue(
      jsonResponse(200, { accessToken: 'jwt-token', tokenType: 'Bearer', expiresIn: 3600, username: 'tiogars', role: 'ADMIN' }),
    );
    vi.stubGlobal('fetch', fetchMock);
    const listener = vi.fn();
    window.addEventListener(LOCAL_SESSION_CHANGED_EVENT, listener);

    const session = await loginWithCredentials('tiogars', 'secret');
    window.removeEventListener(LOCAL_SESSION_CHANGED_EVENT, listener);

    expect(session.username).toBe('tiogars');
    expect(getLocalAccessToken()).toBe('jwt-token');
    expect(listener).toHaveBeenCalledTimes(1);
    const [endpoint, init] = fetchMock.mock.calls[0] as [string, RequestInit];
    expect(endpoint.endsWith('/auth/login')).toBe(true);
    expect(JSON.parse(init.body as string)).toEqual({ username: 'tiogars', password: 'secret' });
  });

  it('shouldThrowFrenchMessageWhenCredentialsAreRejected', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(jsonResponse(401, 'Nom utilisateur ou mot de passe incorrect.')));

    await expect(loginWithCredentials('tiogars', 'bad')).rejects.toThrow('Nom utilisateur ou mot de passe incorrect.');
    expect(localStorage.getItem(LOCAL_SESSION_STORAGE_KEY)).toBeNull();
  });

  it('shouldThrowWhenLoginIsRateLimited', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(jsonResponse(429, '')));

    await expect(loginWithCredentials('tiogars', 'secret')).rejects.toThrow(/Trop de tentatives/);
  });

  it('shouldDropSessionWhenTokenIsExpired', () => {
    localStorage.setItem(
      LOCAL_SESSION_STORAGE_KEY,
      JSON.stringify({ accessToken: 'old', expiresAt: Date.now() - 1000, username: 'tiogars', role: 'ADMIN' }),
    );

    expect(getLocalSession()).toBeNull();
    expect(localStorage.getItem(LOCAL_SESSION_STORAGE_KEY)).toBeNull();
  });
});
