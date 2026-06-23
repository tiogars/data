import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["type-vin"] as const;
const injectedRtkApi = api.enhanceEndpoints({ addTagTypes }).injectEndpoints({
    endpoints: (build) => ({
        getTypeVin: build.query<GetTypeVinApiResponse, GetTypeVinApiArg>({ query: (queryArg) => ({ url: `/type-vin/${queryArg.id}` }), providesTags: ["type-vin"] }),
        updateTypeVin: build.mutation<UpdateTypeVinApiResponse, UpdateTypeVinApiArg>({ query: (queryArg) => ({ url: `/type-vin/${queryArg.id}`, method: "PUT", body: queryArg.typeVin }), invalidatesTags: ["type-vin"] }),
        deleteTypeVin: build.mutation<DeleteTypeVinApiResponse, DeleteTypeVinApiArg>({ query: (queryArg) => ({ url: `/type-vin/${queryArg.id}`, method: "DELETE" }), invalidatesTags: ["type-vin"] }),
        listTypeVins: build.query<ListTypeVinsApiResponse, ListTypeVinsApiArg>({ query: () => ({ url: `/type-vin/list` }), providesTags: ["type-vin"] }),
        searchTypeVins: build.query<SearchTypeVinsApiResponse, SearchTypeVinsApiArg>({ query: (queryArg) => ({ url: `/type-vin/search`, params: { page: queryArg.page, size: queryArg.size, q: queryArg.q } }), providesTags: ["type-vin"] }),
        createTypeVin: build.mutation<CreateTypeVinApiResponse, CreateTypeVinApiArg>({ query: (queryArg) => ({ url: `/type-vin`, method: "POST", body: queryArg.typeVinCreationForm }), invalidatesTags: ["type-vin"] }),
        deleteAllTypeVins: build.mutation<DeleteAllTypeVinsApiResponse, DeleteAllTypeVinsApiArg>({ query: () => ({ url: `/type-vin`, method: "DELETE" }), invalidatesTags: ["type-vin"] }),
        exportTypeVins: build.query<ExportTypeVinsApiResponse, ExportTypeVinsApiArg>({ query: () => ({ url: `/type-vin/export` }), providesTags: ["type-vin"] }),
        importTypeVins: build.mutation<ImportTypeVinsApiResponse, ImportTypeVinsApiArg>({ query: (queryArg) => ({ url: `/type-vin/import`, method: "POST", body: queryArg.typeVinImportForm }), invalidatesTags: ["type-vin"] }),
        exportTypeVinsCsv: build.query<ExportTypeVinsCsvApiResponse, ExportTypeVinsCsvApiArg>({ query: () => ({ url: `/type-vin/export/csv` }), providesTags: ["type-vin"] }),
        importTypeVinsCsv: build.mutation<ImportTypeVinsCsvApiResponse, ImportTypeVinsCsvApiArg>({ query: (queryArg) => ({ url: `/type-vin/import/csv`, method: "POST", body: queryArg.body }), invalidatesTags: ["type-vin"] }),
        printTypeVins: build.query<PrintTypeVinsApiResponse, PrintTypeVinsApiArg>({ query: (queryArg) => ({ url: `/type-vin/print`, params: { mode: queryArg.mode, name: queryArg.name } }), providesTags: ["type-vin"] }),
    }), overrideExisting: false,
});
export { injectedRtkApi as typeVinApi };
export type GetTypeVinApiResponse = TypeVin; export type GetTypeVinApiArg = { id: string };
export type UpdateTypeVinApiResponse = TypeVin; export type UpdateTypeVinApiArg = { id: string; typeVin: TypeVin };
export type DeleteTypeVinApiResponse = unknown; export type DeleteTypeVinApiArg = { id: string };
export type ListTypeVinsApiResponse = TypeVinListResponse; export type ListTypeVinsApiArg = void;
export type SearchTypeVinsApiResponse = TypeVinSearchResponse; export type SearchTypeVinsApiArg = { page?: number; size?: number; q?: string };
export type CreateTypeVinApiResponse = TypeVin; export type CreateTypeVinApiArg = { typeVinCreationForm: TypeVinCreationForm };
export type DeleteAllTypeVinsApiResponse = unknown; export type DeleteAllTypeVinsApiArg = void;
export type ExportTypeVinsApiResponse = TypeVinListResponse; export type ExportTypeVinsApiArg = void;
export type ImportTypeVinsApiResponse = TypeVinImportResult; export type ImportTypeVinsApiArg = { typeVinImportForm: TypeVinImportForm };
export type ExportTypeVinsCsvApiResponse = string; export type ExportTypeVinsCsvApiArg = void;
export type ImportTypeVinsCsvApiResponse = TypeVinImportResult; export type ImportTypeVinsCsvApiArg = { body: string };
export type PrintTypeVinsApiResponse = TypeVinPrintResponse; export type PrintTypeVinsApiArg = { mode?: string; name?: string };
export type TypeVin = { id: string; name?: string };
export type TypeVinCreationForm = { name?: string };
export type TypeVinListResponse = { items?: TypeVin[]; count?: number };
export type TypeVinSearchResponse = { items?: TypeVin[]; count?: number; page?: number; size?: number; query?: string };
export type TypeVinPrintResponse = TypeVinListResponse & { generatedAt?: string; total?: number };
export type TypeVinImportForm = { text?: string; items?: TypeVin[] };
export type TypeVinImportResult = { imported?: TypeVin[]; importedCount?: number; notAddedCount?: number; addedCount?: number; alreadyExistsCount?: number; invalidCount?: number; duplicateNames?: string[] };
export const { useGetTypeVinQuery, useUpdateTypeVinMutation, useDeleteTypeVinMutation, useListTypeVinsQuery, useSearchTypeVinsQuery, useCreateTypeVinMutation, useDeleteAllTypeVinsMutation, useExportTypeVinsQuery, useImportTypeVinsMutation, useExportTypeVinsCsvQuery, useImportTypeVinsCsvMutation, usePrintTypeVinsQuery } = injectedRtkApi;
