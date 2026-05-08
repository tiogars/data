import { emptySplitApi as api } from './emptyApi';

const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    getBrickById: build.query<Brick, { id: string }>({
      query: (queryArg) => ({ url: `/brick/${queryArg.id}` }),
    }),
    updateBrick: build.mutation<Brick, { id: string; brick: Brick }>({
      query: (queryArg) => ({
        url: `/brick/${queryArg.id}`,
        method: 'PUT',
        body: queryArg.brick,
      }),
    }),
    deleteBrickById: build.mutation<unknown, { id: string }>({
      query: (queryArg) => ({
        url: `/brick/${queryArg.id}`,
        method: 'DELETE',
      }),
    }),
    listBricks: build.query<BrickListResponse, void>({
      query: () => ({ url: '/brick' }),
    }),
    createBrick: build.mutation<Brick, { brickCreationForm: BrickCreationForm }>({
      query: (queryArg) => ({
        url: '/brick',
        method: 'POST',
        body: queryArg.brickCreationForm,
      }),
    }),
    deleteAllBricks: build.mutation<unknown, void>({
      query: () => ({ url: '/brick', method: 'DELETE' }),
    }),
    exportBricks: build.query<BrickState, void>({
      query: () => ({ url: '/brick/export' }),
    }),
    importBricks: build.mutation<BrickState, { brickImportForm: BrickImportForm }>({
      query: (queryArg) => ({
        url: '/brick/import',
        method: 'POST',
        body: queryArg.brickImportForm,
      }),
    }),
    getExternalLinkById: build.query<ExternalLink, { id: string }>({
      query: (queryArg) => ({ url: `/brick/external-link/${queryArg.id}` }),
    }),
    updateExternalLink: build.mutation<ExternalLink, { id: string; externalLink: ExternalLink }>({
      query: (queryArg) => ({
        url: `/brick/external-link/${queryArg.id}`,
        method: 'PUT',
        body: queryArg.externalLink,
      }),
    }),
    deleteExternalLinkById: build.mutation<unknown, { id: string }>({
      query: (queryArg) => ({
        url: `/brick/external-link/${queryArg.id}`,
        method: 'DELETE',
      }),
    }),
    listExternalLinks: build.query<ExternalLinkListResponse, void>({
      query: () => ({ url: '/brick/external-link' }),
    }),
    createExternalLink: build.mutation<ExternalLink, { externalLinkCreationForm: ExternalLinkCreationForm }>({
      query: (queryArg) => ({
        url: '/brick/external-link',
        method: 'POST',
        body: queryArg.externalLinkCreationForm,
      }),
    }),
  }),
  overrideExisting: false,
});

export { injectedRtkApi as brickApi };

export type Brick = {
  id?: string;
  number?: string;
  title?: string;
  tags?: string[];
  imageBase64?: string;
  createdAt?: string;
  updatedAt?: string;
};

export type BrickListResponse = {
  items?: Brick[];
  count?: number;
};

export type BrickCreationForm = {
  number?: string;
  title?: string;
  tags?: string[];
  imageBase64?: string;
};

export type ExternalLink = {
  id?: string;
  name?: string;
  url?: string;
  enabled?: boolean;
};

export type ExternalLinkListResponse = {
  items?: ExternalLink[];
  count?: number;
};

export type ExternalLinkCreationForm = {
  name?: string;
  url?: string;
  enabled?: boolean;
};

export type BrickState = {
  bricks?: Brick[];
  tags?: string[];
  externalLinks?: ExternalLink[];
};

export type BrickImportForm = {
  bricks?: Brick[];
  tags?: string[];
  externalLinks?: ExternalLink[];
};

export const {
  useGetBrickByIdQuery,
  useUpdateBrickMutation,
  useDeleteBrickByIdMutation,
  useListBricksQuery,
  useCreateBrickMutation,
  useDeleteAllBricksMutation,
  useLazyExportBricksQuery,
  useImportBricksMutation,
  useGetExternalLinkByIdQuery,
  useUpdateExternalLinkMutation,
  useDeleteExternalLinkByIdMutation,
  useListExternalLinksQuery,
  useCreateExternalLinkMutation,
} = injectedRtkApi;
