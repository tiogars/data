import type { BaseQueryFn } from '@reduxjs/toolkit/query';
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import type { FetchArgs, FetchBaseQueryError } from '@reduxjs/toolkit/query/react';
import { getAccessToken } from './oidcAuth';
import {
  getGatewayBaseUrl,
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

  return baseQuery(args, api, extraOptions);
};

export const emptySplitApi = createApi({
  reducerPath: 'splitApi',
  baseQuery: dynamicBaseQuery,
  endpoints: () => ({}),
});
