import type { BaseQueryFn } from '@reduxjs/toolkit/query';
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import type { FetchArgs, FetchBaseQueryError } from '@reduxjs/toolkit/query/react';
import { getAccessToken } from './accessToken';
import { clearLocalSession } from './localAuth';
import {
  getGatewayBaseUrl,
  isOidcAuthEnabled,
} from './runtimeConfig';

export {
  API_BASE_URL_DEFAULT,
  API_BASE_URL_STORAGE_KEY,
  AUTH_BASE_URL_DEFAULT,
  AUTH_BASE_URL_STORAGE_KEY,
  getAuthBaseUrl,
  getGatewayBaseUrl,
  isGatewayBaseUrlOverriddenByEnv,
  resetAuthBaseUrl,
  resetGatewayBaseUrl,
  setAuthBaseUrl,
  setGatewayBaseUrl,
} from './runtimeConfig';

const dynamicBaseQuery: BaseQueryFn<string | FetchArgs, unknown, FetchBaseQueryError> = async (args, api, extraOptions) => {
  const accessToken = await getAccessToken();

  if (!accessToken) {
    return {
      error: {
        status: 401,
        data: { message: 'Session non authentifiee. Veuillez vous connecter.' },
      } as FetchBaseQueryError,
    };
  }

  const baseQuery = fetchBaseQuery({
    baseUrl: getGatewayBaseUrl(),
    prepareHeaders: (headers) => {
      headers.set('Authorization', `Bearer ${accessToken}`);

      return headers;
    },
  });

  const result = await baseQuery(args, api, extraOptions);

  // Jeton local rejete par la gateway (expire, cle du serveur renouvelee...) : la session n est plus valide.
  if (result.error?.status === 401 && !isOidcAuthEnabled()) {
    clearLocalSession();
  }

  return result;
};

export const emptySplitApi = createApi({
  reducerPath: 'splitApi',
  baseQuery: dynamicBaseQuery,
  endpoints: () => ({}),
});
