import { carMileageApi, type CarMileage } from './carMileageApi';

type CarMileageListResponse = {
  items?: CarMileage[];
  count?: number;
};

type CarMileageImportForm = {
  items?: CarMileage[];
};

type CarMileageImportResult = {
  imported?: CarMileage[];
  addedCount?: number;
  notAddedCount?: number;
  alreadyExistsCount?: number;
  invalidCount?: number;
};

const injectedCarMileageImportExportApi = carMileageApi.injectEndpoints({
  endpoints: (build) => ({
    exportCarMileages: build.query<CarMileageListResponse, void>({
      query: () => ({ url: '/car-mileage/export' }),
      providesTags: ['car-mileage'],
    }),
    exportCarMileagesCsvText: build.query<string, void>({
      query: () => ({
        url: '/car-mileage/export/csv',
        responseHandler: 'text',
      }),
      providesTags: ['car-mileage'],
    }),
    importCarMileages: build.mutation<CarMileageImportResult, CarMileageImportForm>({
      query: (body) => ({
        url: '/car-mileage/import',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['car-mileage'],
    }),
    importCarMileagesCsv: build.mutation<CarMileageImportResult, string>({
      query: (body) => ({
        url: '/car-mileage/import/csv',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['car-mileage'],
    }),
  }),
  overrideExisting: false,
});

export const {
  useLazyExportCarMileagesQuery,
  useLazyExportCarMileagesCsvTextQuery,
  useImportCarMileagesMutation,
  useImportCarMileagesCsvMutation,
} = injectedCarMileageImportExportApi;
