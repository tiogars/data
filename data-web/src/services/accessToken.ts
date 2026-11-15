import { getAccessToken as getOidcAccessToken } from './oidcAuth';
import { getLocalAccessToken } from './localAuth';
import { isOidcAuthEnabled } from './runtimeConfig';

/** Retourne le jeton de la session active : OIDC si l authentification par URL est active, sinon compte utilisateur en base. */
export const getAccessToken = async (): Promise<string | null> => {
  if (isOidcAuthEnabled()) {
    return getOidcAccessToken();
  }

  return getLocalAccessToken();
};
