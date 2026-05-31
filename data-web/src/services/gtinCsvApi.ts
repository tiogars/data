import { gtinApi } from './gtinApi';

const injectedGtinCsvApi = gtinApi.injectEndpoints({
  endpoints: (build) => ({
    exportGtinsCsvText: build.query<string, void>({
      query: () => ({
        url: '/gtin/export/csv',
        responseHandler: 'text',
      }),
      providesTags: ['gtin'],
    }),
  }),
  overrideExisting: false,
});

export const {
  useLazyExportGtinsCsvTextQuery,
} = injectedGtinCsvApi;
