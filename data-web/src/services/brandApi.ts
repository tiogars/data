import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["brand"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getBrand: build.query<GetBrandApiResponse, GetBrandApiArg>({
                query: (queryArg) => ({ url: `/brand/${queryArg.id}` }),
                providesTags: ["brand"],
            }),
            updateBrand: build.mutation<
                UpdateBrandApiResponse,
                UpdateBrandApiArg
            >({
                query: (queryArg) => ({
                    url: `/brand/${queryArg.id}`,
                    method: "PUT",
                    body: queryArg.brand,
                }),
                invalidatesTags: ["brand"],
            }),
            deleteBrand: build.mutation<
                DeleteBrandApiResponse,
                DeleteBrandApiArg
            >({
                query: (queryArg) => ({
                    url: `/brand/${queryArg.id}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["brand"],
            }),
            createBrand: build.mutation<
                CreateBrandApiResponse,
                CreateBrandApiArg
            >({
                query: (queryArg) => ({
                    url: `/brand`,
                    method: "POST",
                    body: queryArg.brandCreationForm,
                }),
                invalidatesTags: ["brand"],
            }),
            deleteAllBrands: build.mutation<
                DeleteAllBrandsApiResponse,
                DeleteAllBrandsApiArg
            >({
                query: () => ({ url: `/brand`, method: "DELETE" }),
                invalidatesTags: ["brand"],
            }),
            importBrands: build.mutation<
                ImportBrandsApiResponse,
                ImportBrandsApiArg
            >({
                query: (queryArg) => ({
                    url: `/brand/import`,
                    method: "POST",
                    body: queryArg.brandImportForm,
                }),
                invalidatesTags: ["brand"],
            }),
            listBrands: build.query<ListBrandsApiResponse, ListBrandsApiArg>({
                query: () => ({ url: `/brand/list` }),
                providesTags: ["brand"],
            }),
            exportBrands: build.query<
                ExportBrandsApiResponse,
                ExportBrandsApiArg
            >({
                query: () => ({ url: `/brand/export` }),
                providesTags: ["brand"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as brandApi };
export type GetBrandApiResponse = /** status 200 OK */ Brand;
export type GetBrandApiArg = {
    id: string;
};
export type UpdateBrandApiResponse = /** status 200 OK */ Brand;
export type UpdateBrandApiArg = {
    id: string;
    brand: Brand;
};
export type DeleteBrandApiResponse = unknown;
export type DeleteBrandApiArg = {
    id: string;
};
export type CreateBrandApiResponse = /** status 200 OK */ Brand;
export type CreateBrandApiArg = {
    brandCreationForm: BrandCreationForm;
};
export type DeleteAllBrandsApiResponse = unknown;
export type DeleteAllBrandsApiArg = void;
export type ImportBrandsApiResponse = /** status 200 OK */ BrandImportResult;
export type ImportBrandsApiArg = {
    brandImportForm: BrandImportForm;
};
export type ListBrandsApiResponse = /** status 200 OK */ BrandListResponse;
export type ListBrandsApiArg = void;
export type ExportBrandsApiResponse = /** status 200 OK */ BrandListResponse;
export type ExportBrandsApiArg = void;
export type Brand = {
    /** L'identifiant unique de la marque. */
    id?: string;
    /** Le nom de la marque. */
    name?: string;
    /** La description de la marque. */
    description?: string;
};
export type BrandCreationForm = {
    /** Le nom de la marque. */
    name?: string;
    /** La description de la marque. */
    description?: string;
};
export type BrandImportResult = {
    /** Liste des marques ajoutees pendant cet import. */
    imported?: Brand[];
    /** Nombre de marques ajoutees (champ historique). */
    importedCount?: number;
    /** Liste des noms detectes en doublon (champ historique). */
    duplicateNames?: string[];
    /** Nombre de marques non ajoutees (champ historique). */
    skippedCount?: number;
    /** Nombre de marques ajoutees. */
    addedCount?: number;
    /** Nombre total de marques non ajoutees. */
    notAddedCount?: number;
    /** Nombre de marques non ajoutees car deja presentes. */
    alreadyExistsCount?: number;
    /** Nombre de lignes non ajoutees a cause d'une erreur de validation ou de persistence. */
    invalidCount?: number;
};
export type BrandImportForm = {
    /** Texte a importer. Chaque ligne non vide represente un nom de marque. */
    text?: string;
    /** Format historique JSON: liste des marques a importer. */
    items?: Brand[];
};
export type BrandListResponse = {
    items?: Brand[];
    count?: number;
};
export const {
    useGetBrandQuery,
    useUpdateBrandMutation,
    useDeleteBrandMutation,
    useCreateBrandMutation,
    useDeleteAllBrandsMutation,
    useImportBrandsMutation,
    useListBrandsQuery,
    useExportBrandsQuery,
} = injectedRtkApi;
