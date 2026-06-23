import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["cepage"] as const;
const injectedRtkApi = api.enhanceEndpoints({ addTagTypes }).injectEndpoints({
    endpoints: (build) => ({
        getCepage: build.query<GetCepageApiResponse, GetCepageApiArg>({ query: (queryArg) => ({ url: `/cepage/${queryArg.id}` }), providesTags: ["cepage"] }),
        updateCepage: build.mutation<UpdateCepageApiResponse, UpdateCepageApiArg>({ query: (queryArg) => ({ url: `/cepage/${queryArg.id}`, method: "PUT", body: queryArg.cepage }), invalidatesTags: ["cepage"] }),
        deleteCepage: build.mutation<DeleteCepageApiResponse, DeleteCepageApiArg>({ query: (queryArg) => ({ url: `/cepage/${queryArg.id}`, method: "DELETE" }), invalidatesTags: ["cepage"] }),
        listCepages: build.query<ListCepagesApiResponse, ListCepagesApiArg>({ query: () => ({ url: `/cepage/list` }), providesTags: ["cepage"] }),
        searchCepages: build.query<SearchCepagesApiResponse, SearchCepagesApiArg>({ query: (queryArg) => ({ url: `/cepage/search`, params: { page: queryArg.page, size: queryArg.size, q: queryArg.q } }), providesTags: ["cepage"] }),
        createCepage: build.mutation<CreateCepageApiResponse, CreateCepageApiArg>({ query: (queryArg) => ({ url: `/cepage`, method: "POST", body: queryArg.cepageCreationForm }), invalidatesTags: ["cepage"] }),
        deleteAllCepages: build.mutation<DeleteAllCepagesApiResponse, DeleteAllCepagesApiArg>({ query: () => ({ url: `/cepage`, method: "DELETE" }), invalidatesTags: ["cepage"] }),
        exportCepages: build.query<ExportCepagesApiResponse, ExportCepagesApiArg>({ query: () => ({ url: `/cepage/export` }), providesTags: ["cepage"] }),
        importCepages: build.mutation<ImportCepagesApiResponse, ImportCepagesApiArg>({ query: (queryArg) => ({ url: `/cepage/import`, method: "POST", body: queryArg.cepageImportForm }), invalidatesTags: ["cepage"] }),
        exportCepagesCsv: build.query<ExportCepagesCsvApiResponse, ExportCepagesCsvApiArg>({ query: () => ({ url: `/cepage/export/csv` }), providesTags: ["cepage"] }),
        importCepagesCsv: build.mutation<ImportCepagesCsvApiResponse, ImportCepagesCsvApiArg>({ query: (queryArg) => ({ url: `/cepage/import/csv`, method: "POST", body: queryArg.body }), invalidatesTags: ["cepage"] }),
        printCepages: build.query<PrintCepagesApiResponse, PrintCepagesApiArg>({ query: (queryArg) => ({ url: `/cepage/print`, params: { mode: queryArg.mode, name: queryArg.name } }), providesTags: ["cepage"] }),
    }),
    overrideExisting: false,
});
export { injectedRtkApi as cepageApi };
export type GetCepageApiResponse = Cepage; export type GetCepageApiArg = { id: string };
export type UpdateCepageApiResponse = Cepage; export type UpdateCepageApiArg = { id: string; cepage: Cepage };
export type DeleteCepageApiResponse = unknown; export type DeleteCepageApiArg = { id: string };
export type ListCepagesApiResponse = CepageListResponse; export type ListCepagesApiArg = void;
export type SearchCepagesApiResponse = CepageSearchResponse; export type SearchCepagesApiArg = { page?: number; size?: number; q?: string };
export type CreateCepageApiResponse = Cepage; export type CreateCepageApiArg = { cepageCreationForm: CepageCreationForm };
export type DeleteAllCepagesApiResponse = unknown; export type DeleteAllCepagesApiArg = void;
export type ExportCepagesApiResponse = CepageListResponse; export type ExportCepagesApiArg = void;
export type ImportCepagesApiResponse = CepageImportResult; export type ImportCepagesApiArg = { cepageImportForm: CepageImportForm };
export type ExportCepagesCsvApiResponse = string; export type ExportCepagesCsvApiArg = void;
export type ImportCepagesCsvApiResponse = CepageImportResult; export type ImportCepagesCsvApiArg = { body: string };
export type PrintCepagesApiResponse = CepagePrintResponse; export type PrintCepagesApiArg = { mode?: string; name?: string };
export type Cepage = { id: string; name?: string };
export type CepageCreationForm = { name?: string };
export type CepageListResponse = { items?: Cepage[]; count?: number };
export type CepageSearchResponse = { items?: Cepage[]; count?: number; page?: number; size?: number; query?: string };
export type CepagePrintResponse = CepageListResponse & { generatedAt?: string; total?: number };
export type CepageImportForm = { text?: string; items?: Cepage[] };
export type CepageImportResult = { imported?: Cepage[]; importedCount?: number; notAddedCount?: number; addedCount?: number; alreadyExistsCount?: number; invalidCount?: number; duplicateNames?: string[] };
export const { useGetCepageQuery, useUpdateCepageMutation, useDeleteCepageMutation, useListCepagesQuery, useSearchCepagesQuery, useCreateCepageMutation, useDeleteAllCepagesMutation, useExportCepagesQuery, useImportCepagesMutation, useExportCepagesCsvQuery, useImportCepagesCsvMutation, usePrintCepagesQuery } = injectedRtkApi;
