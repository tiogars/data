import { emptySplitApi as api } from './emptyApi';

const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    getBrandById: build.query<GetBrandByIdApiResponse, GetBrandByIdApiArg>({
      query: (queryArg) => ({ url: `/brand/${queryArg.id}` }),
    }),
    updateBrand: build.mutation<UpdateBrandApiResponse, UpdateBrandApiArg>({
      query: (queryArg) => ({
        url: `/brand/${queryArg.id}`,
        method: 'PUT',
        body: queryArg.brand,
      }),
    }),
    deleteBrandById: build.mutation<DeleteBrandByIdApiResponse, DeleteBrandByIdApiArg>({
      query: (queryArg) => ({
        url: `/brand/${queryArg.id}`,
        method: 'DELETE',
      }),
    }),
    listBrands: build.query<ListBrandsApiResponse, ListBrandsApiArg>({
      query: () => ({ url: '/brand' }),
    }),
    createBrand: build.mutation<CreateBrandApiResponse, CreateBrandApiArg>({
      query: (queryArg) => ({
        url: '/brand',
        method: 'POST',
        body: queryArg.brandCreationForm,
      }),
    }),
    deleteAllBrands: build.mutation<DeleteAllBrandsApiResponse, DeleteAllBrandsApiArg>({
      query: () => ({ url: '/brand', method: 'DELETE' }),
    }),
    exportBrands: build.query<ExportBrandsApiResponse, ExportBrandsApiArg>({
      query: () => ({ url: '/brand/export' }),
    }),
    importBrands: build.mutation<ImportBrandsApiResponse, ImportBrandsApiArg>({
      query: (queryArg) => ({
        url: '/brand/import',
        method: 'POST',
        body: queryArg.brandImportForm,
      }),
    }),
  }),
  overrideExisting: false,
});

export { injectedRtkApi as brandApi };

export type GetBrandByIdApiResponse = Brand;
export type GetBrandByIdApiArg = {
  id: string;
};

export type UpdateBrandApiResponse = Brand;
export type UpdateBrandApiArg = {
  id: string;
  brand: Brand;
};

export type DeleteBrandByIdApiResponse = unknown;
export type DeleteBrandByIdApiArg = {
  id: string;
};

export type ListBrandsApiResponse = BrandListResponse;
export type ListBrandsApiArg = void;

export type CreateBrandApiResponse = Brand;
export type CreateBrandApiArg = {
  brandCreationForm: BrandCreationForm;
};

export type DeleteAllBrandsApiResponse = unknown;
export type DeleteAllBrandsApiArg = void;

export type ExportBrandsApiResponse = BrandListResponse;
export type ExportBrandsApiArg = void;

export type ImportBrandsApiResponse = BrandImportResult;
export type ImportBrandsApiArg = {
  brandImportForm: BrandImportForm;
};

export type Brand = {
  id?: string;
  name?: string;
  description?: string;
};

export type BrandListResponse = {
  items?: Brand[];
  count?: number;
};

export type BrandCreationForm = {
  name?: string;
  description?: string;
};

export type BrandImportForm = {
  items?: Brand[];
};

export type BrandImportResult = {
  imported?: Brand[];
  importedCount?: number;
  duplicateNames?: string[];
  skippedCount?: number;
};

export const {
  useGetBrandByIdQuery,
  useUpdateBrandMutation,
  useDeleteBrandByIdMutation,
  useListBrandsQuery,
  useCreateBrandMutation,
  useDeleteAllBrandsMutation,
  useLazyExportBrandsQuery,
  useImportBrandsMutation,
} = injectedRtkApi;
