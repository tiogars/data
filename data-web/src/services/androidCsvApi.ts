import { androidApi } from './androidApi';

const injectedAndroidCsvApi = androidApi.injectEndpoints({
  endpoints: (build) => ({
    exportAndroidsCsvText: build.query<string, void>({
      query: () => ({
        url: '/android/export/csv',
        responseHandler: 'text',
      }),
      providesTags: ['android'],
    }),
  }),
  overrideExisting: false,
});

export const {
  useLazyExportAndroidsCsvTextQuery,
} = injectedAndroidCsvApi;