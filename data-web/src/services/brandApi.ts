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
            listBrands: build.query<ListBrandsApiResponse, ListBrandsApiArg>({
                query: () => ({ url: `/brand` }),
                providesTags: ["brand"],
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
export type ListBrandsApiResponse = /** status 200 OK */ BrandListResponse;
export type ListBrandsApiArg = void;
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
export type BrandListResponse = {
    items?: Brand[];
    count?: number;
};
export type BrandCreationForm = {
    /** Le nom de la marque. */
    name?: string;
    /** La description de la marque. */
    description?: string;
};
export type BrandImportResult = {
    imported?: Brand[];
    importedCount?: number;
    duplicateNames?: string[];
    skippedCount?: number;
};
export type BrandImportForm = {
    /** Liste des marques a importer. */
    items?: Brand[];
};
export const {
    useGetBrandQuery,
    useUpdateBrandMutation,
    useDeleteBrandMutation,
    useListBrandsQuery,
    useCreateBrandMutation,
    useDeleteAllBrandsMutation,
    useImportBrandsMutation,
    useExportBrandsQuery,
} = injectedRtkApi;
