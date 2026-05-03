import { emptySplitApi as api } from './emptyApi';

const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    getGtinById: build.query<GetGtinByIdApiResponse, GetGtinByIdApiArg>({
      query: (queryArg) => ({ url: `/gtin/${queryArg.id}` }),
    }),
    updateGtin: build.mutation<UpdateGtinApiResponse, UpdateGtinApiArg>({
      query: (queryArg) => ({
        url: `/gtin/${queryArg.id}`,
        method: 'PUT',
        body: queryArg.gtin,
      }),
    }),
    deleteGtinById: build.mutation<DeleteGtinByIdApiResponse, DeleteGtinByIdApiArg>({
      query: (queryArg) => ({
        url: `/gtin/${queryArg.id}`,
        method: 'DELETE',
      }),
    }),
    listGtins: build.query<ListGtinsApiResponse, ListGtinsApiArg>({
      query: () => ({ url: '/gtin' }),
    }),
    createGtin: build.mutation<CreateGtinApiResponse, CreateGtinApiArg>({
      query: (queryArg) => ({
        url: '/gtin',
        method: 'POST',
        body: queryArg.gtinCreationForm,
      }),
    }),
    deleteAllGtins: build.mutation<DeleteAllGtinsApiResponse, DeleteAllGtinsApiArg>({
      query: () => ({ url: '/gtin', method: 'DELETE' }),
    }),
    exportGtins: build.query<ExportGtinsApiResponse, ExportGtinsApiArg>({
      query: () => ({ url: '/gtin/export' }),
    }),
    importGtins: build.mutation<ImportGtinsApiResponse, ImportGtinsApiArg>({
      query: (queryArg) => ({
        url: '/gtin/import',
        method: 'POST',
        body: queryArg.gtinImportForm,
      }),
    }),
  }),
  overrideExisting: false,
});

export { injectedRtkApi as gtinApi };

export type GetGtinByIdApiResponse = Gtin;
export type GetGtinByIdApiArg = {
  id: string;
};

export type UpdateGtinApiResponse = Gtin;
export type UpdateGtinApiArg = {
  id: string;
  gtin: Gtin;
};

export type DeleteGtinByIdApiResponse = unknown;
export type DeleteGtinByIdApiArg = {
  id: string;
};

export type ListGtinsApiResponse = GtinListResponse;
export type ListGtinsApiArg = void;

export type CreateGtinApiResponse = Gtin;
export type CreateGtinApiArg = {
  gtinCreationForm: GtinCreationForm;
};

export type DeleteAllGtinsApiResponse = unknown;
export type DeleteAllGtinsApiArg = void;

export type ExportGtinsApiResponse = GtinListResponse;
export type ExportGtinsApiArg = void;

export type ImportGtinsApiResponse = GtinListResponse;
export type ImportGtinsApiArg = {
  gtinImportForm: GtinImportForm;
};

export type Gtin = {
  id?: string;
  code?: string;
  description?: string;
};

export type GtinListResponse = {
  items?: Gtin[];
  count?: number;
};

export type GtinCreationForm = {
  code?: string;
  description?: string;
};

export type GtinImportForm = {
  items?: Gtin[];
};

export const {
  useGetGtinByIdQuery,
  useUpdateGtinMutation,
  useDeleteGtinByIdMutation,
  useListGtinsQuery,
  useCreateGtinMutation,
  useDeleteAllGtinsMutation,
  useLazyExportGtinsQuery,
  useImportGtinsMutation,
} = injectedRtkApi;
