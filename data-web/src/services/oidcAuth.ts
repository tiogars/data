import { UserManager, WebStorageStateStore, type User, type UserManagerSettings } from 'oidc-client-ts';
import { buildAuthorityUrl, getAuthClientId, getAuthScope } from './runtimeConfig';

const SIGNIN_CALLBACK_PATH = '/auth/callback';
const SIGNOUT_CALLBACK_PATH = '/auth/logout-callback';

let managerCache: UserManager | null = null;
let managerSignatureCache = '';

const canUseWindow = () => typeof globalThis.window !== 'undefined';

const buildRedirectUri = (path: string) => {
  if (!canUseWindow()) {
    return `http://localhost${path}`;
  }

  return `${globalThis.window.location.origin}${path}`;
};

const buildManagerSettings = (): UserManagerSettings => ({
  authority: buildAuthorityUrl(),
  client_id: getAuthClientId(),
  redirect_uri: buildRedirectUri(SIGNIN_CALLBACK_PATH),
  post_logout_redirect_uri: buildRedirectUri(SIGNOUT_CALLBACK_PATH),
  response_type: 'code',
  scope: getAuthScope(),
  userStore: canUseWindow() ? new WebStorageStateStore({ store: globalThis.window.localStorage }) : undefined,
  automaticSilentRenew: false,
});

const computeManagerSignature = (settings: UserManagerSettings) =>
  [settings.authority, settings.client_id, settings.redirect_uri, settings.post_logout_redirect_uri, settings.scope].join('|');

export const getOidcUserManager = () => {
  const settings = buildManagerSettings();
  const signature = computeManagerSignature(settings);

  if (!managerCache || managerSignatureCache !== signature) {
    managerCache = new UserManager(settings);
    managerSignatureCache = signature;
  }

  return managerCache;
};

export const getOidcUser = async (): Promise<User | null> => {
  const manager = getOidcUserManager();
  return manager.getUser();
};

export const getAccessToken = async (): Promise<string | null> => {
  const user = await getOidcUser();
  if (!user || user.expired) {
    return null;
  }

  return user.access_token;
};

export const startLoginRedirect = async () => {
  const manager = getOidcUserManager();
  await manager.signinRedirect();
};

export const completeLoginRedirect = async () => {
  const manager = getOidcUserManager();
  return manager.signinRedirectCallback();
};

export const startLogoutRedirect = async () => {
  const manager = getOidcUserManager();
  await manager.signoutRedirect();
};

export const completeLogoutRedirect = async () => {
  const manager = getOidcUserManager();
  await manager.signoutRedirectCallback();
};
