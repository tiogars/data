import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["vin-nom"] as const;
const injectedRtkApi = api.enhanceEndpoints({ addTagTypes }).injectEndpoints({
    endpoints: (build) => ({
        getVinNom: build.query<GetVinNomApiResponse, GetVinNomApiArg>({ query: (queryArg) => ({ url: `/vin-nom/${queryArg.id}` }), providesTags: ["vin-nom"] }),
        updateVinNom: build.mutation<UpdateVinNomApiResponse, UpdateVinNomApiArg>({ query: (queryArg) => ({ url: `/vin-nom/${queryArg.id}`, method: "PUT", body: queryArg.vinNom }), invalidatesTags: ["vin-nom"] }),
        deleteVinNom: build.mutation<DeleteVinNomApiResponse, DeleteVinNomApiArg>({ query: (queryArg) => ({ url: `/vin-nom/${queryArg.id}`, method: "DELETE" }), invalidatesTags: ["vin-nom"] }),
        listVinNoms: build.query<ListVinNomsApiResponse, ListVinNomsApiArg>({ query: () => ({ url: `/vin-nom/list` }), providesTags: ["vin-nom"] }),
        searchVinNoms: build.query<SearchVinNomsApiResponse, SearchVinNomsApiArg>({ query: (queryArg) => ({ url: `/vin-nom/search`, params: { page: queryArg.page, size: queryArg.size, q: queryArg.q } }), providesTags: ["vin-nom"] }),
        createVinNom: build.mutation<CreateVinNomApiResponse, CreateVinNomApiArg>({ query: (queryArg) => ({ url: `/vin-nom`, method: "POST", body: queryArg.vinNomCreationForm }), invalidatesTags: ["vin-nom"] }),
        deleteAllVinNoms: build.mutation<DeleteAllVinNomsApiResponse, DeleteAllVinNomsApiArg>({ query: () => ({ url: `/vin-nom`, method: "DELETE" }), invalidatesTags: ["vin-nom"] }),
        exportVinNoms: build.query<ExportVinNomsApiResponse, ExportVinNomsApiArg>({ query: () => ({ url: `/vin-nom/export` }), providesTags: ["vin-nom"] }),
        importVinNoms: build.mutation<ImportVinNomsApiResponse, ImportVinNomsApiArg>({ query: (queryArg) => ({ url: `/vin-nom/import`, method: "POST", body: queryArg.vinNomImportForm }), invalidatesTags: ["vin-nom"] }),
        exportVinNomsCsv: build.query<ExportVinNomsCsvApiResponse, ExportVinNomsCsvApiArg>({ query: () => ({ url: `/vin-nom/export/csv` }), providesTags: ["vin-nom"] }),
        importVinNomsCsv: build.mutation<ImportVinNomsCsvApiResponse, ImportVinNomsCsvApiArg>({ query: (queryArg) => ({ url: `/vin-nom/import/csv`, method: "POST", body: queryArg.body }), invalidatesTags: ["vin-nom"] }),
        printVinNoms: build.query<PrintVinNomsApiResponse, PrintVinNomsApiArg>({ query: (queryArg) => ({ url: `/vin-nom/print`, params: { mode: queryArg.mode, name: queryArg.name } }), providesTags: ["vin-nom"] }),
    }), overrideExisting: false,
});
export { injectedRtkApi as vinNomApi };
export type GetVinNomApiResponse = VinNom; export type GetVinNomApiArg = { id: string };
export type UpdateVinNomApiResponse = VinNom; export type UpdateVinNomApiArg = { id: string; vinNom: VinNom };
export type DeleteVinNomApiResponse = unknown; export type DeleteVinNomApiArg = { id: string };
export type ListVinNomsApiResponse = VinNomListResponse; export type ListVinNomsApiArg = void;
export type SearchVinNomsApiResponse = VinNomSearchResponse; export type SearchVinNomsApiArg = { page?: number; size?: number; q?: string };
export type CreateVinNomApiResponse = VinNom; export type CreateVinNomApiArg = { vinNomCreationForm: VinNomCreationForm };
export type DeleteAllVinNomsApiResponse = unknown; export type DeleteAllVinNomsApiArg = void;
export type ExportVinNomsApiResponse = VinNomListResponse; export type ExportVinNomsApiArg = void;
export type ImportVinNomsApiResponse = VinNomImportResult; export type ImportVinNomsApiArg = { vinNomImportForm: VinNomImportForm };
export type ExportVinNomsCsvApiResponse = string; export type ExportVinNomsCsvApiArg = void;
export type ImportVinNomsCsvApiResponse = VinNomImportResult; export type ImportVinNomsCsvApiArg = { body: string };
export type PrintVinNomsApiResponse = VinNomPrintResponse; export type PrintVinNomsApiArg = { mode?: string; name?: string };
export type VinNom = { id: string; name?: string; maisonId?: string; maisonName?: string };
export type VinNomCreationForm = { name?: string; maisonId?: string };
export type VinNomListResponse = { items?: VinNom[]; count?: number };
export type VinNomSearchResponse = { items?: VinNom[]; count?: number; page?: number; size?: number; query?: string };
export type VinNomPrintResponse = VinNomListResponse & { generatedAt?: string; total?: number };
export type VinNomImportForm = { text?: string; items?: VinNom[] };
export type VinNomImportResult = { imported?: VinNom[]; importedCount?: number; notAddedCount?: number; addedCount?: number; alreadyExistsCount?: number; invalidCount?: number; duplicateNames?: string[] };
export const { useGetVinNomQuery, useUpdateVinNomMutation, useDeleteVinNomMutation, useListVinNomsQuery, useSearchVinNomsQuery, useCreateVinNomMutation, useDeleteAllVinNomsMutation, useExportVinNomsQuery, useImportVinNomsMutation, useExportVinNomsCsvQuery, useImportVinNomsCsvMutation, usePrintVinNomsQuery } = injectedRtkApi;
