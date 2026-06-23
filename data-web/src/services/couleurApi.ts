import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["couleur"] as const;
const injectedRtkApi = api.enhanceEndpoints({ addTagTypes }).injectEndpoints({
    endpoints: (build) => ({
        getCouleur: build.query<GetCouleurApiResponse, GetCouleurApiArg>({ query: (queryArg) => ({ url: `/couleur/${queryArg.id}` }), providesTags: ["couleur"] }),
        updateCouleur: build.mutation<UpdateCouleurApiResponse, UpdateCouleurApiArg>({ query: (queryArg) => ({ url: `/couleur/${queryArg.id}`, method: "PUT", body: queryArg.couleur }), invalidatesTags: ["couleur"] }),
        deleteCouleur: build.mutation<DeleteCouleurApiResponse, DeleteCouleurApiArg>({ query: (queryArg) => ({ url: `/couleur/${queryArg.id}`, method: "DELETE" }), invalidatesTags: ["couleur"] }),
        listCouleurs: build.query<ListCouleursApiResponse, ListCouleursApiArg>({ query: () => ({ url: `/couleur/list` }), providesTags: ["couleur"] }),
        searchCouleurs: build.query<SearchCouleursApiResponse, SearchCouleursApiArg>({ query: (queryArg) => ({ url: `/couleur/search`, params: { page: queryArg.page, size: queryArg.size, q: queryArg.q } }), providesTags: ["couleur"] }),
        createCouleur: build.mutation<CreateCouleurApiResponse, CreateCouleurApiArg>({ query: (queryArg) => ({ url: `/couleur`, method: "POST", body: queryArg.couleurCreationForm }), invalidatesTags: ["couleur"] }),
        deleteAllCouleurs: build.mutation<DeleteAllCouleursApiResponse, DeleteAllCouleursApiArg>({ query: () => ({ url: `/couleur`, method: "DELETE" }), invalidatesTags: ["couleur"] }),
        exportCouleurs: build.query<ExportCouleursApiResponse, ExportCouleursApiArg>({ query: () => ({ url: `/couleur/export` }), providesTags: ["couleur"] }),
        importCouleurs: build.mutation<ImportCouleursApiResponse, ImportCouleursApiArg>({ query: (queryArg) => ({ url: `/couleur/import`, method: "POST", body: queryArg.couleurImportForm }), invalidatesTags: ["couleur"] }),
        exportCouleursCsv: build.query<ExportCouleursCsvApiResponse, ExportCouleursCsvApiArg>({ query: () => ({ url: `/couleur/export/csv` }), providesTags: ["couleur"] }),
        importCouleursCsv: build.mutation<ImportCouleursCsvApiResponse, ImportCouleursCsvApiArg>({ query: (queryArg) => ({ url: `/couleur/import/csv`, method: "POST", body: queryArg.body }), invalidatesTags: ["couleur"] }),
        printCouleurs: build.query<PrintCouleursApiResponse, PrintCouleursApiArg>({ query: (queryArg) => ({ url: `/couleur/print`, params: { mode: queryArg.mode, name: queryArg.name } }), providesTags: ["couleur"] }),
    }),
    overrideExisting: false,
});
export { injectedRtkApi as couleurApi };
export type GetCouleurApiResponse = Couleur; export type GetCouleurApiArg = { id: string };
export type UpdateCouleurApiResponse = Couleur; export type UpdateCouleurApiArg = { id: string; couleur: Couleur };
export type DeleteCouleurApiResponse = unknown; export type DeleteCouleurApiArg = { id: string };
export type ListCouleursApiResponse = CouleurListResponse; export type ListCouleursApiArg = void;
export type SearchCouleursApiResponse = CouleurSearchResponse; export type SearchCouleursApiArg = { page?: number; size?: number; q?: string };
export type CreateCouleurApiResponse = Couleur; export type CreateCouleurApiArg = { couleurCreationForm: CouleurCreationForm };
export type DeleteAllCouleursApiResponse = unknown; export type DeleteAllCouleursApiArg = void;
export type ExportCouleursApiResponse = CouleurListResponse; export type ExportCouleursApiArg = void;
export type ImportCouleursApiResponse = CouleurImportResult; export type ImportCouleursApiArg = { couleurImportForm: CouleurImportForm };
export type ExportCouleursCsvApiResponse = string; export type ExportCouleursCsvApiArg = void;
export type ImportCouleursCsvApiResponse = CouleurImportResult; export type ImportCouleursCsvApiArg = { body: string };
export type PrintCouleursApiResponse = CouleurPrintResponse; export type PrintCouleursApiArg = { mode?: string; name?: string };
export type Couleur = { id: string; name?: string };
export type CouleurCreationForm = { name?: string };
export type CouleurListResponse = { items?: Couleur[]; count?: number };
export type CouleurSearchResponse = { items?: Couleur[]; count?: number; page?: number; size?: number; query?: string };
export type CouleurPrintResponse = CouleurListResponse & { generatedAt?: string; total?: number };
export type CouleurImportForm = { text?: string; items?: Couleur[] };
export type CouleurImportResult = { imported?: Couleur[]; importedCount?: number; notAddedCount?: number; addedCount?: number; alreadyExistsCount?: number; invalidCount?: number; duplicateNames?: string[] };
export const { useGetCouleurQuery, useUpdateCouleurMutation, useDeleteCouleurMutation, useListCouleursQuery, useSearchCouleursQuery, useCreateCouleurMutation, useDeleteAllCouleursMutation, useExportCouleursQuery, useImportCouleursMutation, useExportCouleursCsvQuery, useImportCouleursCsvMutation, usePrintCouleursQuery } = injectedRtkApi;
