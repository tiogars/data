import { wingetApi } from './wingetApi';

export type WingetImportForm = {
  wingetIdsText?: string;
};

export type WingetImportResponse = {
  createdCount?: number;
  skippedCount?: number;
  skippedWingetIds?: string[];
};

const wingetImportApi = wingetApi.injectEndpoints({
  endpoints: (build) => ({
    importWingets: build.mutation<WingetImportResponse, { wingetImportForm: WingetImportForm }>({
      query: (queryArg) => ({
        url: '/winget/import',
        method: 'POST',
        body: queryArg.wingetImportForm,
      }),
      invalidatesTags: ['winget'],
    }),
  }),
  overrideExisting: false,
});

export const { useImportWingetsMutation } = wingetImportApi;
