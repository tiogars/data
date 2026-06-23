import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["appellation"] as const;
const injectedRtkApi = api.enhanceEndpoints({ addTagTypes }).injectEndpoints({
    endpoints: (build) => ({
        getAppellation: build.query<GetAppellationApiResponse, GetAppellationApiArg>({ query: (queryArg) => ({ url: `/appellation/${queryArg.id}` }), providesTags: ["appellation"] }),
        updateAppellation: build.mutation<UpdateAppellationApiResponse, UpdateAppellationApiArg>({ query: (queryArg) => ({ url: `/appellation/${queryArg.id}`, method: "PUT", body: queryArg.appellation }), invalidatesTags: ["appellation"] }),
        deleteAppellation: build.mutation<DeleteAppellationApiResponse, DeleteAppellationApiArg>({ query: (queryArg) => ({ url: `/appellation/${queryArg.id}`, method: "DELETE" }), invalidatesTags: ["appellation"] }),
        listAppellations: build.query<ListAppellationsApiResponse, ListAppellationsApiArg>({ query: () => ({ url: `/appellation/list` }), providesTags: ["appellation"] }),
        searchAppellations: build.query<SearchAppellationsApiResponse, SearchAppellationsApiArg>({ query: (queryArg) => ({ url: `/appellation/search`, params: { page: queryArg.page, size: queryArg.size, q: queryArg.q } }), providesTags: ["appellation"] }),
        createAppellation: build.mutation<CreateAppellationApiResponse, CreateAppellationApiArg>({ query: (queryArg) => ({ url: `/appellation`, method: "POST", body: queryArg.appellationCreationForm }), invalidatesTags: ["appellation"] }),
        deleteAllAppellations: build.mutation<DeleteAllAppellationsApiResponse, DeleteAllAppellationsApiArg>({ query: () => ({ url: `/appellation`, method: "DELETE" }), invalidatesTags: ["appellation"] }),
        exportAppellations: build.query<ExportAppellationsApiResponse, ExportAppellationsApiArg>({ query: () => ({ url: `/appellation/export` }), providesTags: ["appellation"] }),
        importAppellations: build.mutation<ImportAppellationsApiResponse, ImportAppellationsApiArg>({ query: (queryArg) => ({ url: `/appellation/import`, method: "POST", body: queryArg.appellationImportForm }), invalidatesTags: ["appellation"] }),
        exportAppellationsCsv: build.query<ExportAppellationsCsvApiResponse, ExportAppellationsCsvApiArg>({ query: () => ({ url: `/appellation/export/csv` }), providesTags: ["appellation"] }),
        importAppellationsCsv: build.mutation<ImportAppellationsCsvApiResponse, ImportAppellationsCsvApiArg>({ query: (queryArg) => ({ url: `/appellation/import/csv`, method: "POST", body: queryArg.body }), invalidatesTags: ["appellation"] }),
        printAppellations: build.query<PrintAppellationsApiResponse, PrintAppellationsApiArg>({ query: (queryArg) => ({ url: `/appellation/print`, params: { mode: queryArg.mode, name: queryArg.name } }), providesTags: ["appellation"] }),
    }),
    overrideExisting: false,
});
export { injectedRtkApi as appellationApi };
export type GetAppellationApiResponse = Appellation; export type GetAppellationApiArg = { id: string };
export type UpdateAppellationApiResponse = Appellation; export type UpdateAppellationApiArg = { id: string; appellation: Appellation };
export type DeleteAppellationApiResponse = unknown; export type DeleteAppellationApiArg = { id: string };
export type ListAppellationsApiResponse = AppellationListResponse; export type ListAppellationsApiArg = void;
export type SearchAppellationsApiResponse = AppellationSearchResponse; export type SearchAppellationsApiArg = { page?: number; size?: number; q?: string };
export type CreateAppellationApiResponse = Appellation; export type CreateAppellationApiArg = { appellationCreationForm: AppellationCreationForm };
export type DeleteAllAppellationsApiResponse = unknown; export type DeleteAllAppellationsApiArg = void;
export type ExportAppellationsApiResponse = AppellationListResponse; export type ExportAppellationsApiArg = void;
export type ImportAppellationsApiResponse = AppellationImportResult; export type ImportAppellationsApiArg = { appellationImportForm: AppellationImportForm };
export type ExportAppellationsCsvApiResponse = string; export type ExportAppellationsCsvApiArg = void;
export type ImportAppellationsCsvApiResponse = AppellationImportResult; export type ImportAppellationsCsvApiArg = { body: string };
export type PrintAppellationsApiResponse = AppellationPrintResponse; export type PrintAppellationsApiArg = { mode?: string; name?: string };
export type Appellation = { id: string; name?: string };
export type AppellationCreationForm = { name?: string };
export type AppellationListResponse = { items?: Appellation[]; count?: number };
export type AppellationSearchResponse = { items?: Appellation[]; count?: number; page?: number; size?: number; query?: string };
export type AppellationPrintResponse = AppellationListResponse & { generatedAt?: string; total?: number };
export type AppellationImportForm = { text?: string; items?: Appellation[] };
export type AppellationImportResult = { imported?: Appellation[]; importedCount?: number; notAddedCount?: number; addedCount?: number; alreadyExistsCount?: number; invalidCount?: number; duplicateNames?: string[] };
export const { useGetAppellationQuery, useUpdateAppellationMutation, useDeleteAppellationMutation, useListAppellationsQuery, useSearchAppellationsQuery, useCreateAppellationMutation, useDeleteAllAppellationsMutation, useExportAppellationsQuery, useImportAppellationsMutation, useExportAppellationsCsvQuery, useImportAppellationsCsvMutation, usePrintAppellationsQuery } = injectedRtkApi;
