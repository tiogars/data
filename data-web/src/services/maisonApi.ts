import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["maison"] as const;
const injectedRtkApi = api.enhanceEndpoints({ addTagTypes }).injectEndpoints({
    endpoints: (build) => ({
        getMaison: build.query<GetMaisonApiResponse, GetMaisonApiArg>({ query: (queryArg) => ({ url: `/maison/${queryArg.id}` }), providesTags: ["maison"] }),
        updateMaison: build.mutation<UpdateMaisonApiResponse, UpdateMaisonApiArg>({ query: (queryArg) => ({ url: `/maison/${queryArg.id}`, method: "PUT", body: queryArg.maison }), invalidatesTags: ["maison"] }),
        deleteMaison: build.mutation<DeleteMaisonApiResponse, DeleteMaisonApiArg>({ query: (queryArg) => ({ url: `/maison/${queryArg.id}`, method: "DELETE" }), invalidatesTags: ["maison"] }),
        listMaisons: build.query<ListMaisonsApiResponse, ListMaisonsApiArg>({ query: () => ({ url: `/maison/list` }), providesTags: ["maison"] }),
        searchMaisons: build.query<SearchMaisonsApiResponse, SearchMaisonsApiArg>({ query: (queryArg) => ({ url: `/maison/search`, params: { page: queryArg.page, size: queryArg.size, q: queryArg.q } }), providesTags: ["maison"] }),
        createMaison: build.mutation<CreateMaisonApiResponse, CreateMaisonApiArg>({ query: (queryArg) => ({ url: `/maison`, method: "POST", body: queryArg.maisonCreationForm }), invalidatesTags: ["maison"] }),
        deleteAllMaisons: build.mutation<DeleteAllMaisonsApiResponse, DeleteAllMaisonsApiArg>({ query: () => ({ url: `/maison`, method: "DELETE" }), invalidatesTags: ["maison"] }),
        exportMaisons: build.query<ExportMaisonsApiResponse, ExportMaisonsApiArg>({ query: () => ({ url: `/maison/export` }), providesTags: ["maison"] }),
        importMaisons: build.mutation<ImportMaisonsApiResponse, ImportMaisonsApiArg>({ query: (queryArg) => ({ url: `/maison/import`, method: "POST", body: queryArg.maisonImportForm }), invalidatesTags: ["maison"] }),
        exportMaisonsCsv: build.query<ExportMaisonsCsvApiResponse, ExportMaisonsCsvApiArg>({ query: () => ({ url: `/maison/export/csv` }), providesTags: ["maison"] }),
        importMaisonsCsv: build.mutation<ImportMaisonsCsvApiResponse, ImportMaisonsCsvApiArg>({ query: (queryArg) => ({ url: `/maison/import/csv`, method: "POST", body: queryArg.body }), invalidatesTags: ["maison"] }),
        printMaisons: build.query<PrintMaisonsApiResponse, PrintMaisonsApiArg>({ query: (queryArg) => ({ url: `/maison/print`, params: { mode: queryArg.mode, name: queryArg.name } }), providesTags: ["maison"] }),
    }), overrideExisting: false,
});
export { injectedRtkApi as maisonApi };
export type GetMaisonApiResponse = Maison; export type GetMaisonApiArg = { id: string };
export type UpdateMaisonApiResponse = Maison; export type UpdateMaisonApiArg = { id: string; maison: Maison };
export type DeleteMaisonApiResponse = unknown; export type DeleteMaisonApiArg = { id: string };
export type ListMaisonsApiResponse = MaisonListResponse; export type ListMaisonsApiArg = void;
export type SearchMaisonsApiResponse = MaisonSearchResponse; export type SearchMaisonsApiArg = { page?: number; size?: number; q?: string };
export type CreateMaisonApiResponse = Maison; export type CreateMaisonApiArg = { maisonCreationForm: MaisonCreationForm };
export type DeleteAllMaisonsApiResponse = unknown; export type DeleteAllMaisonsApiArg = void;
export type ExportMaisonsApiResponse = MaisonListResponse; export type ExportMaisonsApiArg = void;
export type ImportMaisonsApiResponse = MaisonImportResult; export type ImportMaisonsApiArg = { maisonImportForm: MaisonImportForm };
export type ExportMaisonsCsvApiResponse = string; export type ExportMaisonsCsvApiArg = void;
export type ImportMaisonsCsvApiResponse = MaisonImportResult; export type ImportMaisonsCsvApiArg = { body: string };
export type PrintMaisonsApiResponse = MaisonPrintResponse; export type PrintMaisonsApiArg = { mode?: string; name?: string };
export type Maison = { id: string; name?: string; website?: string };
export type MaisonCreationForm = { name?: string; website?: string };
export type MaisonListResponse = { items?: Maison[]; count?: number };
export type MaisonSearchResponse = { items?: Maison[]; count?: number; page?: number; size?: number; query?: string };
export type MaisonPrintResponse = MaisonListResponse & { generatedAt?: string; total?: number };
export type MaisonImportForm = { text?: string; items?: Maison[] };
export type MaisonImportResult = { imported?: Maison[]; importedCount?: number; notAddedCount?: number; addedCount?: number; alreadyExistsCount?: number; invalidCount?: number; duplicateNames?: string[] };
export const { useGetMaisonQuery, useUpdateMaisonMutation, useDeleteMaisonMutation, useListMaisonsQuery, useSearchMaisonsQuery, useCreateMaisonMutation, useDeleteAllMaisonsMutation, useExportMaisonsQuery, useImportMaisonsMutation, useExportMaisonsCsvQuery, useImportMaisonsCsvMutation, usePrintMaisonsQuery } = injectedRtkApi;
