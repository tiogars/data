import { carApi, type Car, type CarListResponse } from './carApi';

type CarImportForm = {
  items?: Car[];
};

type CarImportResult = {
  imported?: Car[];
  addedCount?: number;
  notAddedCount?: number;
  alreadyExistsCount?: number;
  invalidCount?: number;
};

const injectedCarImportExportApi = carApi.injectEndpoints({
  endpoints: (build) => ({
    exportCars: build.query<CarListResponse, void>({
      query: () => ({ url: '/car/export' }),
      providesTags: ['car'],
    }),
    exportCarsCsvText: build.query<string, void>({
      query: () => ({
        url: '/car/export/csv',
        responseHandler: 'text',
      }),
      providesTags: ['car'],
    }),
    importCars: build.mutation<CarImportResult, CarImportForm>({
      query: (body) => ({
        url: '/car/import',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['car'],
    }),
    importCarsCsv: build.mutation<CarImportResult, string>({
      query: (body) => ({
        url: '/car/import/csv',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['car'],
    }),
  }),
  overrideExisting: false,
});

export const {
  useLazyExportCarsQuery,
  useLazyExportCarsCsvTextQuery,
  useImportCarsMutation,
  useImportCarsCsvMutation,
} = injectedCarImportExportApi;
