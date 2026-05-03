import { emptySplitApi as api } from './emptyApi';
import type { UrlManagerState } from '../features/urlManager/types';

const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    getUrlManagerState: build.query<UrlManagerState, void>({
      query: () => ({ url: '/url-manager/state' }),
    }),
    updateUrlManagerState: build.mutation<UrlManagerState, { urlManagerState: UrlManagerState }>({
      query: (queryArg) => ({
        url: '/url-manager/state',
        method: 'PUT',
        body: queryArg.urlManagerState,
      }),
    }),
    exportUrlManagerState: build.query<UrlManagerState, void>({
      query: () => ({ url: '/url-manager/export' }),
    }),
    importUrlManagerState: build.mutation<UrlManagerState, { urlManagerState: UrlManagerState }>({
      query: (queryArg) => ({
        url: '/url-manager/import',
        method: 'POST',
        body: queryArg.urlManagerState,
      }),
    }),
  }),
  overrideExisting: false,
});

export { injectedRtkApi as urlManagerApi };
export const {
  useGetUrlManagerStateQuery,
  useUpdateUrlManagerStateMutation,
  useLazyExportUrlManagerStateQuery,
  useImportUrlManagerStateMutation,
} = injectedRtkApi;
