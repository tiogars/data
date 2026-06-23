import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["circonstance"] as const;
const injectedRtkApi = api.enhanceEndpoints({ addTagTypes }).injectEndpoints({
    endpoints: (build) => ({
        getCirconstance: build.query<GetCirconstanceApiResponse, GetCirconstanceApiArg>({ query: (queryArg) => ({ url: `/circonstance/${queryArg.id}` }), providesTags: ["circonstance"] }),
        updateCirconstance: build.mutation<UpdateCirconstanceApiResponse, UpdateCirconstanceApiArg>({ query: (queryArg) => ({ url: `/circonstance/${queryArg.id}`, method: "PUT", body: queryArg.circonstance }), invalidatesTags: ["circonstance"] }),
        deleteCirconstance: build.mutation<DeleteCirconstanceApiResponse, DeleteCirconstanceApiArg>({ query: (queryArg) => ({ url: `/circonstance/${queryArg.id}`, method: "DELETE" }), invalidatesTags: ["circonstance"] }),
        listCirconstances: build.query<ListCirconstancesApiResponse, ListCirconstancesApiArg>({ query: () => ({ url: `/circonstance/list` }), providesTags: ["circonstance"] }),
        searchCirconstances: build.query<SearchCirconstancesApiResponse, SearchCirconstancesApiArg>({ query: (queryArg) => ({ url: `/circonstance/search`, params: { page: queryArg.page, size: queryArg.size, q: queryArg.q } }), providesTags: ["circonstance"] }),
        createCirconstance: build.mutation<CreateCirconstanceApiResponse, CreateCirconstanceApiArg>({ query: (queryArg) => ({ url: `/circonstance`, method: "POST", body: queryArg.circonstanceCreationForm }), invalidatesTags: ["circonstance"] }),
        deleteAllCirconstances: build.mutation<DeleteAllCirconstancesApiResponse, DeleteAllCirconstancesApiArg>({ query: () => ({ url: `/circonstance`, method: "DELETE" }), invalidatesTags: ["circonstance"] }),
        exportCirconstances: build.query<ExportCirconstancesApiResponse, ExportCirconstancesApiArg>({ query: () => ({ url: `/circonstance/export` }), providesTags: ["circonstance"] }),
        importCirconstances: build.mutation<ImportCirconstancesApiResponse, ImportCirconstancesApiArg>({ query: (queryArg) => ({ url: `/circonstance/import`, method: "POST", body: queryArg.circonstanceImportForm }), invalidatesTags: ["circonstance"] }),
        exportCirconstancesCsv: build.query<ExportCirconstancesCsvApiResponse, ExportCirconstancesCsvApiArg>({ query: () => ({ url: `/circonstance/export/csv` }), providesTags: ["circonstance"] }),
        importCirconstancesCsv: build.mutation<ImportCirconstancesCsvApiResponse, ImportCirconstancesCsvApiArg>({ query: (queryArg) => ({ url: `/circonstance/import/csv`, method: "POST", body: queryArg.body }), invalidatesTags: ["circonstance"] }),
        printCirconstances: build.query<PrintCirconstancesApiResponse, PrintCirconstancesApiArg>({ query: (queryArg) => ({ url: `/circonstance/print`, params: { mode: queryArg.mode, name: queryArg.name } }), providesTags: ["circonstance"] }),
    }),
    overrideExisting: false,
});
export { injectedRtkApi as circonstanceApi };
export type GetCirconstanceApiResponse = Circonstance; export type GetCirconstanceApiArg = { id: string };
export type UpdateCirconstanceApiResponse = Circonstance; export type UpdateCirconstanceApiArg = { id: string; circonstance: Circonstance };
export type DeleteCirconstanceApiResponse = unknown; export type DeleteCirconstanceApiArg = { id: string };
export type ListCirconstancesApiResponse = CirconstanceListResponse; export type ListCirconstancesApiArg = void;
export type SearchCirconstancesApiResponse = CirconstanceSearchResponse; export type SearchCirconstancesApiArg = { page?: number; size?: number; q?: string };
export type CreateCirconstanceApiResponse = Circonstance; export type CreateCirconstanceApiArg = { circonstanceCreationForm: CirconstanceCreationForm };
export type DeleteAllCirconstancesApiResponse = unknown; export type DeleteAllCirconstancesApiArg = void;
export type ExportCirconstancesApiResponse = CirconstanceListResponse; export type ExportCirconstancesApiArg = void;
export type ImportCirconstancesApiResponse = CirconstanceImportResult; export type ImportCirconstancesApiArg = { circonstanceImportForm: CirconstanceImportForm };
export type ExportCirconstancesCsvApiResponse = string; export type ExportCirconstancesCsvApiArg = void;
export type ImportCirconstancesCsvApiResponse = CirconstanceImportResult; export type ImportCirconstancesCsvApiArg = { body: string };
export type PrintCirconstancesApiResponse = CirconstancePrintResponse; export type PrintCirconstancesApiArg = { mode?: string; name?: string };
export type Circonstance = { id: string; name?: string };
export type CirconstanceCreationForm = { name?: string };
export type CirconstanceListResponse = { items?: Circonstance[]; count?: number };
export type CirconstanceSearchResponse = { items?: Circonstance[]; count?: number; page?: number; size?: number; query?: string };
export type CirconstancePrintResponse = CirconstanceListResponse & { generatedAt?: string; total?: number };
export type CirconstanceImportForm = { text?: string; items?: Circonstance[] };
export type CirconstanceImportResult = { imported?: Circonstance[]; importedCount?: number; notAddedCount?: number; addedCount?: number; alreadyExistsCount?: number; invalidCount?: number; duplicateNames?: string[] };
export const { useGetCirconstanceQuery, useUpdateCirconstanceMutation, useDeleteCirconstanceMutation, useListCirconstancesQuery, useSearchCirconstancesQuery, useCreateCirconstanceMutation, useDeleteAllCirconstancesMutation, useExportCirconstancesQuery, useImportCirconstancesMutation, useExportCirconstancesCsvQuery, useImportCirconstancesCsvMutation, usePrintCirconstancesQuery } = injectedRtkApi;
