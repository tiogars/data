import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["gtin"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getGtin: build.query<GetGtinApiResponse, GetGtinApiArg>({
                query: (queryArg) => ({ url: `/gtin/${queryArg.id}` }),
                providesTags: ["gtin"],
            }),
            updateGtin: build.mutation<UpdateGtinApiResponse, UpdateGtinApiArg>(
                {
                    query: (queryArg) => ({
                        url: `/gtin/${queryArg.id}`,
                        method: "PUT",
                        body: queryArg.gtin,
                    }),
                    invalidatesTags: ["gtin"],
                },
            ),
            deleteGtin: build.mutation<DeleteGtinApiResponse, DeleteGtinApiArg>(
                {
                    query: (queryArg) => ({
                        url: `/gtin/${queryArg.id}`,
                        method: "DELETE",
                    }),
                    invalidatesTags: ["gtin"],
                },
            ),
            listGtins: build.query<ListGtinsApiResponse, ListGtinsApiArg>({
                query: () => ({ url: `/gtin` }),
                providesTags: ["gtin"],
            }),
            createGtin: build.mutation<CreateGtinApiResponse, CreateGtinApiArg>(
                {
                    query: (queryArg) => ({
                        url: `/gtin`,
                        method: "POST",
                        body: queryArg.gtinCreationForm,
                    }),
                    invalidatesTags: ["gtin"],
                },
            ),
            deleteAllGtins: build.mutation<
                DeleteAllGtinsApiResponse,
                DeleteAllGtinsApiArg
            >({
                query: () => ({ url: `/gtin`, method: "DELETE" }),
                invalidatesTags: ["gtin"],
            }),
            importGtins: build.mutation<
                ImportGtinsApiResponse,
                ImportGtinsApiArg
            >({
                query: (queryArg) => ({
                    url: `/gtin/import`,
                    method: "POST",
                    body: queryArg.gtinImportForm,
                }),
                invalidatesTags: ["gtin"],
            }),
            exportGtins: build.query<ExportGtinsApiResponse, ExportGtinsApiArg>(
                {
                    query: () => ({ url: `/gtin/export` }),
                    providesTags: ["gtin"],
                },
            ),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as gtinApi };
export type GetGtinApiResponse = /** status 200 OK */ Gtin;
export type GetGtinApiArg = {
    id: string;
};
export type UpdateGtinApiResponse = /** status 200 OK */ Gtin;
export type UpdateGtinApiArg = {
    id: string;
    gtin: Gtin;
};
export type DeleteGtinApiResponse = unknown;
export type DeleteGtinApiArg = {
    id: string;
};
export type ListGtinsApiResponse = /** status 200 OK */ GtinListResponse;
export type ListGtinsApiArg = void;
export type CreateGtinApiResponse = /** status 200 OK */ Gtin;
export type CreateGtinApiArg = {
    gtinCreationForm: GtinCreationForm;
};
export type DeleteAllGtinsApiResponse = unknown;
export type DeleteAllGtinsApiArg = void;
export type ImportGtinsApiResponse = /** status 200 OK */ GtinImportResult;
export type ImportGtinsApiArg = {
    gtinImportForm: GtinImportForm;
};
export type ExportGtinsApiResponse = /** status 200 OK */ GtinListResponse;
export type ExportGtinsApiArg = void;
export type Gtin = {
    /** L'identifiant unique du GTIN. */
    id?: string;
    /** Le code GTIN. */
    code?: string;
    /** La description du GTIN. */
    description?: string;
};
export type GtinListResponse = {
    items?: Gtin[];
    count?: number;
};
export type GtinCreationForm = {
    /** Le code GTIN. */
    code?: string;
    /** La description du GTIN. */
    description?: string;
};
export type GtinImportResult = {
    imported?: Gtin[];
    importedCount?: number;
    duplicateCodes?: string[];
    skippedCount?: number;
};
export type GtinImportForm = {
    /** Liste des GTIN a importer. */
    items?: Gtin[];
};
export const {
    useGetGtinQuery,
    useUpdateGtinMutation,
    useDeleteGtinMutation,
    useListGtinsQuery,
    useCreateGtinMutation,
    useDeleteAllGtinsMutation,
    useImportGtinsMutation,
    useExportGtinsQuery,
} = injectedRtkApi;
