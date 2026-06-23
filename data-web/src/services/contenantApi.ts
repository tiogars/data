import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["contenant"] as const;
const injectedRtkApi = api.enhanceEndpoints({ addTagTypes }).injectEndpoints({
    endpoints: (build) => ({
        getContenant: build.query<GetContenantApiResponse, GetContenantApiArg>({ query: (queryArg) => ({ url: `/contenant/${queryArg.id}` }), providesTags: ["contenant"] }),
        updateContenant: build.mutation<UpdateContenantApiResponse, UpdateContenantApiArg>({ query: (queryArg) => ({ url: `/contenant/${queryArg.id}`, method: "PUT", body: queryArg.contenant }), invalidatesTags: ["contenant"] }),
        deleteContenant: build.mutation<DeleteContenantApiResponse, DeleteContenantApiArg>({ query: (queryArg) => ({ url: `/contenant/${queryArg.id}`, method: "DELETE" }), invalidatesTags: ["contenant"] }),
        listContenants: build.query<ListContenantsApiResponse, ListContenantsApiArg>({ query: () => ({ url: `/contenant/list` }), providesTags: ["contenant"] }),
        searchContenants: build.query<SearchContenantsApiResponse, SearchContenantsApiArg>({ query: (queryArg) => ({ url: `/contenant/search`, params: { page: queryArg.page, size: queryArg.size, q: queryArg.q } }), providesTags: ["contenant"] }),
        createContenant: build.mutation<CreateContenantApiResponse, CreateContenantApiArg>({ query: (queryArg) => ({ url: `/contenant`, method: "POST", body: queryArg.contenantCreationForm }), invalidatesTags: ["contenant"] }),
        deleteAllContenants: build.mutation<DeleteAllContenantsApiResponse, DeleteAllContenantsApiArg>({ query: () => ({ url: `/contenant`, method: "DELETE" }), invalidatesTags: ["contenant"] }),
        exportContenants: build.query<ExportContenantsApiResponse, ExportContenantsApiArg>({ query: () => ({ url: `/contenant/export` }), providesTags: ["contenant"] }),
        importContenants: build.mutation<ImportContenantsApiResponse, ImportContenantsApiArg>({ query: (queryArg) => ({ url: `/contenant/import`, method: "POST", body: queryArg.contenantImportForm }), invalidatesTags: ["contenant"] }),
        exportContenantsCsv: build.query<ExportContenantsCsvApiResponse, ExportContenantsCsvApiArg>({ query: () => ({ url: `/contenant/export/csv` }), providesTags: ["contenant"] }),
        importContenantsCsv: build.mutation<ImportContenantsCsvApiResponse, ImportContenantsCsvApiArg>({ query: (queryArg) => ({ url: `/contenant/import/csv`, method: "POST", body: queryArg.body }), invalidatesTags: ["contenant"] }),
        printContenants: build.query<PrintContenantsApiResponse, PrintContenantsApiArg>({ query: (queryArg) => ({ url: `/contenant/print`, params: { mode: queryArg.mode, name: queryArg.name } }), providesTags: ["contenant"] }),
    }), overrideExisting: false,
});
export { injectedRtkApi as contenantApi };
export type GetContenantApiResponse = Contenant; export type GetContenantApiArg = { id: string };
export type UpdateContenantApiResponse = Contenant; export type UpdateContenantApiArg = { id: string; contenant: Contenant };
export type DeleteContenantApiResponse = unknown; export type DeleteContenantApiArg = { id: string };
export type ListContenantsApiResponse = ContenantListResponse; export type ListContenantsApiArg = void;
export type SearchContenantsApiResponse = ContenantSearchResponse; export type SearchContenantsApiArg = { page?: number; size?: number; q?: string };
export type CreateContenantApiResponse = Contenant; export type CreateContenantApiArg = { contenantCreationForm: ContenantCreationForm };
export type DeleteAllContenantsApiResponse = unknown; export type DeleteAllContenantsApiArg = void;
export type ExportContenantsApiResponse = ContenantListResponse; export type ExportContenantsApiArg = void;
export type ImportContenantsApiResponse = ContenantImportResult; export type ImportContenantsApiArg = { contenantImportForm: ContenantImportForm };
export type ExportContenantsCsvApiResponse = string; export type ExportContenantsCsvApiArg = void;
export type ImportContenantsCsvApiResponse = ContenantImportResult; export type ImportContenantsCsvApiArg = { body: string };
export type PrintContenantsApiResponse = ContenantPrintResponse; export type PrintContenantsApiArg = { mode?: string; name?: string };
export type Contenant = { id: string; name?: string; volumeCl?: number };
export type ContenantCreationForm = { name?: string; volumeCl?: number };
export type ContenantListResponse = { items?: Contenant[]; count?: number };
export type ContenantSearchResponse = { items?: Contenant[]; count?: number; page?: number; size?: number; query?: string };
export type ContenantPrintResponse = ContenantListResponse & { generatedAt?: string; total?: number };
export type ContenantImportForm = { text?: string; items?: Contenant[] };
export type ContenantImportResult = { imported?: Contenant[]; importedCount?: number; notAddedCount?: number; addedCount?: number; alreadyExistsCount?: number; invalidCount?: number; duplicateNames?: string[] };
export const { useGetContenantQuery, useUpdateContenantMutation, useDeleteContenantMutation, useListContenantsQuery, useSearchContenantsQuery, useCreateContenantMutation, useDeleteAllContenantsMutation, useExportContenantsQuery, useImportContenantsMutation, useExportContenantsCsvQuery, useImportContenantsCsvMutation, usePrintContenantsQuery } = injectedRtkApi;
