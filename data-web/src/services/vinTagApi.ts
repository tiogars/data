import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["vin-tag"] as const;
const injectedRtkApi = api.enhanceEndpoints({ addTagTypes }).injectEndpoints({
    endpoints: (build) => ({
        getVinTag: build.query<GetVinTagApiResponse, GetVinTagApiArg>({ query: (queryArg) => ({ url: `/vin-tag/${queryArg.id}` }), providesTags: ["vin-tag"] }),
        updateVinTag: build.mutation<UpdateVinTagApiResponse, UpdateVinTagApiArg>({ query: (queryArg) => ({ url: `/vin-tag/${queryArg.id}`, method: "PUT", body: queryArg.vinTag }), invalidatesTags: ["vin-tag"] }),
        deleteVinTag: build.mutation<DeleteVinTagApiResponse, DeleteVinTagApiArg>({ query: (queryArg) => ({ url: `/vin-tag/${queryArg.id}`, method: "DELETE" }), invalidatesTags: ["vin-tag"] }),
        listVinTags: build.query<ListVinTagsApiResponse, ListVinTagsApiArg>({ query: () => ({ url: `/vin-tag/list` }), providesTags: ["vin-tag"] }),
        searchVinTags: build.query<SearchVinTagsApiResponse, SearchVinTagsApiArg>({ query: (queryArg) => ({ url: `/vin-tag/search`, params: { page: queryArg.page, size: queryArg.size, q: queryArg.q } }), providesTags: ["vin-tag"] }),
        createVinTag: build.mutation<CreateVinTagApiResponse, CreateVinTagApiArg>({ query: (queryArg) => ({ url: `/vin-tag`, method: "POST", body: queryArg.vinTagCreationForm }), invalidatesTags: ["vin-tag"] }),
        deleteAllVinTags: build.mutation<DeleteAllVinTagsApiResponse, DeleteAllVinTagsApiArg>({ query: () => ({ url: `/vin-tag`, method: "DELETE" }), invalidatesTags: ["vin-tag"] }),
        exportVinTags: build.query<ExportVinTagsApiResponse, ExportVinTagsApiArg>({ query: () => ({ url: `/vin-tag/export` }), providesTags: ["vin-tag"] }),
        importVinTags: build.mutation<ImportVinTagsApiResponse, ImportVinTagsApiArg>({ query: (queryArg) => ({ url: `/vin-tag/import`, method: "POST", body: queryArg.vinTagImportForm }), invalidatesTags: ["vin-tag"] }),
        exportVinTagsCsv: build.query<ExportVinTagsCsvApiResponse, ExportVinTagsCsvApiArg>({ query: () => ({ url: `/vin-tag/export/csv` }), providesTags: ["vin-tag"] }),
        importVinTagsCsv: build.mutation<ImportVinTagsCsvApiResponse, ImportVinTagsCsvApiArg>({ query: (queryArg) => ({ url: `/vin-tag/import/csv`, method: "POST", body: queryArg.body }), invalidatesTags: ["vin-tag"] }),
        printVinTags: build.query<PrintVinTagsApiResponse, PrintVinTagsApiArg>({ query: (queryArg) => ({ url: `/vin-tag/print`, params: { mode: queryArg.mode, name: queryArg.name } }), providesTags: ["vin-tag"] }),
    }), overrideExisting: false,
});
export { injectedRtkApi as vinTagApi };
export type GetVinTagApiResponse = VinTag; export type GetVinTagApiArg = { id: string };
export type UpdateVinTagApiResponse = VinTag; export type UpdateVinTagApiArg = { id: string; vinTag: VinTag };
export type DeleteVinTagApiResponse = unknown; export type DeleteVinTagApiArg = { id: string };
export type ListVinTagsApiResponse = VinTagListResponse; export type ListVinTagsApiArg = void;
export type SearchVinTagsApiResponse = VinTagSearchResponse; export type SearchVinTagsApiArg = { page?: number; size?: number; q?: string };
export type CreateVinTagApiResponse = VinTag; export type CreateVinTagApiArg = { vinTagCreationForm: VinTagCreationForm };
export type DeleteAllVinTagsApiResponse = unknown; export type DeleteAllVinTagsApiArg = void;
export type ExportVinTagsApiResponse = VinTagListResponse; export type ExportVinTagsApiArg = void;
export type ImportVinTagsApiResponse = VinTagImportResult; export type ImportVinTagsApiArg = { vinTagImportForm: VinTagImportForm };
export type ExportVinTagsCsvApiResponse = string; export type ExportVinTagsCsvApiArg = void;
export type ImportVinTagsCsvApiResponse = VinTagImportResult; export type ImportVinTagsCsvApiArg = { body: string };
export type PrintVinTagsApiResponse = VinTagPrintResponse; export type PrintVinTagsApiArg = { mode?: string; name?: string };
export type VinTag = { id: string; name?: string };
export type VinTagCreationForm = { name?: string };
export type VinTagListResponse = { items?: VinTag[]; count?: number };
export type VinTagSearchResponse = { items?: VinTag[]; count?: number; page?: number; size?: number; query?: string };
export type VinTagPrintResponse = VinTagListResponse & { generatedAt?: string; total?: number };
export type VinTagImportForm = { text?: string; items?: VinTag[] };
export type VinTagImportResult = { imported?: VinTag[]; importedCount?: number; notAddedCount?: number; addedCount?: number; alreadyExistsCount?: number; invalidCount?: number; duplicateNames?: string[] };
export const { useGetVinTagQuery, useUpdateVinTagMutation, useDeleteVinTagMutation, useListVinTagsQuery, useSearchVinTagsQuery, useCreateVinTagMutation, useDeleteAllVinTagsMutation, useExportVinTagsQuery, useImportVinTagsMutation, useExportVinTagsCsvQuery, useImportVinTagsCsvMutation, usePrintVinTagsQuery } = injectedRtkApi;
