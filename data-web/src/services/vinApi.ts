import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["vin"] as const;
const injectedRtkApi = api.enhanceEndpoints({ addTagTypes }).injectEndpoints({
    endpoints: (build) => ({
        getVin: build.query<GetVinApiResponse, GetVinApiArg>({ query: (queryArg) => ({ url: `/vin/${queryArg.id}` }), providesTags: ["vin"] }),
        updateVin: build.mutation<UpdateVinApiResponse, UpdateVinApiArg>({ query: (queryArg) => ({ url: `/vin/${queryArg.id}`, method: "PUT", body: queryArg.vin }), invalidatesTags: ["vin"] }),
        deleteVin: build.mutation<DeleteVinApiResponse, DeleteVinApiArg>({ query: (queryArg) => ({ url: `/vin/${queryArg.id}`, method: "DELETE" }), invalidatesTags: ["vin"] }),
        listVins: build.query<ListVinsApiResponse, ListVinsApiArg>({ query: () => ({ url: `/vin/list` }), providesTags: ["vin"] }),
        searchVins: build.query<SearchVinsApiResponse, SearchVinsApiArg>({ query: (queryArg) => ({ url: `/vin/search`, params: { page: queryArg.page, size: queryArg.size, q: queryArg.q, appellationId: queryArg.appellationId, couleurId: queryArg.couleurId, annee: queryArg.annee } }), providesTags: ["vin"] }),
        createVin: build.mutation<CreateVinApiResponse, CreateVinApiArg>({ query: (queryArg) => ({ url: `/vin`, method: "POST", body: queryArg.vinCreationForm }), invalidatesTags: ["vin"] }),
        deleteAllVins: build.mutation<DeleteAllVinsApiResponse, DeleteAllVinsApiArg>({ query: () => ({ url: `/vin`, method: "DELETE" }), invalidatesTags: ["vin"] }),
        exportVins: build.query<ExportVinsApiResponse, ExportVinsApiArg>({ query: () => ({ url: `/vin/export` }), providesTags: ["vin"] }),
        importVins: build.mutation<ImportVinsApiResponse, ImportVinsApiArg>({ query: (queryArg) => ({ url: `/vin/import`, method: "POST", body: queryArg.vinImportForm }), invalidatesTags: ["vin"] }),
        printVins: build.query<PrintVinsApiResponse, PrintVinsApiArg>({ query: (queryArg) => ({ url: `/vin/print`, params: { mode: queryArg.mode, q: queryArg.q, appellationId: queryArg.appellationId, couleurId: queryArg.couleurId, annee: queryArg.annee } }), providesTags: ["vin"] }),
    }), overrideExisting: false,
});
export { injectedRtkApi as vinApi };
export type GetVinApiResponse = Vin; export type GetVinApiArg = { id: string };
export type UpdateVinApiResponse = Vin; export type UpdateVinApiArg = { id: string; vin: Vin };
export type DeleteVinApiResponse = unknown; export type DeleteVinApiArg = { id: string };
export type ListVinsApiResponse = VinListResponse; export type ListVinsApiArg = void;
export type SearchVinsApiResponse = VinSearchResponse; export type SearchVinsApiArg = { page?: number; size?: number; q?: string; appellationId?: string; couleurId?: string; annee?: number };
export type CreateVinApiResponse = Vin; export type CreateVinApiArg = { vinCreationForm: VinCreationForm };
export type DeleteAllVinsApiResponse = unknown; export type DeleteAllVinsApiArg = void;
export type ExportVinsApiResponse = VinListResponse; export type ExportVinsApiArg = void;
export type ImportVinsApiResponse = VinImportResult; export type ImportVinsApiArg = { vinImportForm: VinImportForm };
export type PrintVinsApiResponse = VinPrintResponse; export type PrintVinsApiArg = { mode?: string; q?: string; appellationId?: string; couleurId?: string; annee?: number };
export type VinCepageEntry = { cepageId?: string; cepageName?: string; pourcentage?: number };
export type VinCepageEntryForm = { cepageId: string; pourcentage?: number };
export type Vin = { id: string; appellationId?: string; appellationName?: string; couleurId?: string; couleurName?: string; typeVinId?: string; typeVinName?: string; maisonId?: string; maisonName?: string; vinNomId?: string; vinNomName?: string; contenantId?: string; contenantName?: string; annee?: number; commune?: string; region?: string; commentaires?: string; accordsMetsVins?: string; cepages?: VinCepageEntry[]; circonstances?: string[]; circonstanceNames?: string[]; tags?: string[]; tagNames?: string[]; createdAt?: string; updatedAt?: string };
export type VinCreationForm = { appellationId?: string; couleurId?: string; typeVinId?: string; maisonId?: string; vinNomId?: string; contenantId?: string; annee?: number; commune?: string; region?: string; commentaires?: string; accordsMetsVins?: string; cepages?: VinCepageEntryForm[]; circonstanceIds?: string[]; tagIds?: string[] };
export type VinListResponse = { items?: Vin[]; count?: number };
export type VinSearchResponse = { items?: Vin[]; count?: number; page?: number; size?: number; query?: string };
export type VinPrintResponse = VinListResponse & { generatedAt?: string; total?: number };
export type VinImportForm = { text?: string; items?: Vin[] };
export type VinImportResult = { imported?: Vin[]; importedCount?: number; notAddedCount?: number; addedCount?: number; alreadyExistsCount?: number; invalidCount?: number; duplicateNames?: string[] };
export const { useGetVinQuery, useUpdateVinMutation, useDeleteVinMutation, useListVinsQuery, useSearchVinsQuery, useCreateVinMutation, useDeleteAllVinsMutation, useExportVinsQuery, useImportVinsMutation, usePrintVinsQuery } = injectedRtkApi;
