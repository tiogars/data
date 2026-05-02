import type { BaseQueryFn } from '@reduxjs/toolkit/query';
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import type { FetchArgs, FetchBaseQueryError } from '@reduxjs/toolkit/query/react';

export const API_BASE_URL_STORAGE_KEY = 'api-base-url';
export const API_BASE_URL_DEFAULT = 'http://localhost:8081';

const envApiBaseUrl = import.meta.env.VITE_API_BASE_URL?.trim();

export const isGatewayBaseUrlOverriddenByEnv = () => Boolean(envApiBaseUrl);

const canUseLocalStorage = () => typeof window !== 'undefined' && Boolean(window.localStorage);

const readGatewayBaseUrlFromLocalStorage = () => {
  if (!canUseLocalStorage()) {
    return null;
  }

  try {
    const value = window.localStorage.getItem(API_BASE_URL_STORAGE_KEY)?.trim();
    return value || null;
  } catch {
    return null;
  }
};

const writeGatewayBaseUrlToLocalStorage = (value: string) => {
  if (!canUseLocalStorage()) {
    return;
  }

  try {
    window.localStorage.setItem(API_BASE_URL_STORAGE_KEY, value);
  } catch {
    // Ignore localStorage write failures (private mode, browser policy, etc.)
  }
};

export const getGatewayBaseUrl = () => {
  if (envApiBaseUrl) {
    return envApiBaseUrl;
  }

  const saved = readGatewayBaseUrlFromLocalStorage();
  if (saved) {
    return saved;
  }

  writeGatewayBaseUrlToLocalStorage(API_BASE_URL_DEFAULT);
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

  writeGatewayBaseUrlToLocalStorage(normalizedValue);
};

export const resetGatewayBaseUrl = () => {
  if (envApiBaseUrl) {
    return;
  }

  writeGatewayBaseUrlToLocalStorage(API_BASE_URL_DEFAULT);
};

const dynamicBaseQuery: BaseQueryFn<string | FetchArgs, unknown, FetchBaseQueryError> = (args, api, extraOptions) => {
  const baseQuery = fetchBaseQuery({ baseUrl: getGatewayBaseUrl() });
  return baseQuery(args, api, extraOptions);
};

export const emptySplitApi = createApi({
  reducerPath: 'splitApi',
  baseQuery: dynamicBaseQuery,
  endpoints: () => ({}),
});
