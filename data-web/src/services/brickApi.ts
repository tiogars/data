import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["brick"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getBrickById: build.query<
                GetBrickByIdApiResponse,
                GetBrickByIdApiArg
            >({
                query: (queryArg) => ({ url: `/brick/${queryArg.id}` }),
                providesTags: ["brick"],
            }),
            updateBrick: build.mutation<
                UpdateBrickApiResponse,
                UpdateBrickApiArg
            >({
                query: (queryArg) => ({
                    url: `/brick/${queryArg.id}`,
                    method: "PUT",
                    body: queryArg.brick,
                }),
                invalidatesTags: ["brick"],
            }),
            deleteBrickById: build.mutation<
                DeleteBrickByIdApiResponse,
                DeleteBrickByIdApiArg
            >({
                query: (queryArg) => ({
                    url: `/brick/${queryArg.id}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["brick"],
            }),
            getExternalLinkById: build.query<
                GetExternalLinkByIdApiResponse,
                GetExternalLinkByIdApiArg
            >({
                query: (queryArg) => ({
                    url: `/brick/external-link/${queryArg.id}`,
                }),
                providesTags: ["brick"],
            }),
            updateExternalLink: build.mutation<
                UpdateExternalLinkApiResponse,
                UpdateExternalLinkApiArg
            >({
                query: (queryArg) => ({
                    url: `/brick/external-link/${queryArg.id}`,
                    method: "PUT",
                    body: queryArg.externalLink,
                }),
                invalidatesTags: ["brick"],
            }),
            deleteExternalLinkById: build.mutation<
                DeleteExternalLinkByIdApiResponse,
                DeleteExternalLinkByIdApiArg
            >({
                query: (queryArg) => ({
                    url: `/brick/external-link/${queryArg.id}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["brick"],
            }),
            listBricks: build.query<ListBricksApiResponse, ListBricksApiArg>({
                query: () => ({ url: `/brick` }),
                providesTags: ["brick"],
            }),
            createBrick: build.mutation<
                CreateBrickApiResponse,
                CreateBrickApiArg
            >({
                query: (queryArg) => ({
                    url: `/brick`,
                    method: "POST",
                    body: queryArg.brickCreationForm,
                }),
                invalidatesTags: ["brick"],
            }),
            deleteAllBricks: build.mutation<
                DeleteAllBricksApiResponse,
                DeleteAllBricksApiArg
            >({
                query: () => ({ url: `/brick`, method: "DELETE" }),
                invalidatesTags: ["brick"],
            }),
            importBricks: build.mutation<
                ImportBricksApiResponse,
                ImportBricksApiArg
            >({
                query: (queryArg) => ({
                    url: `/brick/import`,
                    method: "POST",
                    body: queryArg.brickImportForm,
                }),
                invalidatesTags: ["brick"],
            }),
            listExternalLinks: build.query<
                ListExternalLinksApiResponse,
                ListExternalLinksApiArg
            >({
                query: () => ({ url: `/brick/external-link` }),
                providesTags: ["brick"],
            }),
            createExternalLink: build.mutation<
                CreateExternalLinkApiResponse,
                CreateExternalLinkApiArg
            >({
                query: (queryArg) => ({
                    url: `/brick/external-link`,
                    method: "POST",
                    body: queryArg.externalLinkCreationForm,
                }),
                invalidatesTags: ["brick"],
            }),
            exportBricks: build.query<
                ExportBricksApiResponse,
                ExportBricksApiArg
            >({
                query: () => ({ url: `/brick/export` }),
                providesTags: ["brick"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as brickApi };
export type GetBrickByIdApiResponse = /** status 200 OK */ Brick;
export type GetBrickByIdApiArg = {
    id: string;
};
export type UpdateBrickApiResponse = /** status 200 OK */ Brick;
export type UpdateBrickApiArg = {
    id: string;
    brick: Brick;
};
export type DeleteBrickByIdApiResponse = unknown;
export type DeleteBrickByIdApiArg = {
    id: string;
};
export type GetExternalLinkByIdApiResponse = /** status 200 OK */ ExternalLink;
export type GetExternalLinkByIdApiArg = {
    id: string;
};
export type UpdateExternalLinkApiResponse = /** status 200 OK */ ExternalLink;
export type UpdateExternalLinkApiArg = {
    id: string;
    externalLink: ExternalLink;
};
export type DeleteExternalLinkByIdApiResponse = unknown;
export type DeleteExternalLinkByIdApiArg = {
    id: string;
};
export type ListBricksApiResponse = /** status 200 OK */ BrickListResponse;
export type ListBricksApiArg = void;
export type CreateBrickApiResponse = /** status 200 OK */ Brick;
export type CreateBrickApiArg = {
    brickCreationForm: BrickCreationForm;
};
export type DeleteAllBricksApiResponse = unknown;
export type DeleteAllBricksApiArg = void;
export type ImportBricksApiResponse = /** status 200 OK */ BrickState;
export type ImportBricksApiArg = {
    brickImportForm: BrickImportForm;
};
export type ListExternalLinksApiResponse =
    /** status 200 OK */ ExternalLinkListResponse;
export type ListExternalLinksApiArg = void;
export type CreateExternalLinkApiResponse = /** status 200 OK */ ExternalLink;
export type CreateExternalLinkApiArg = {
    externalLinkCreationForm: ExternalLinkCreationForm;
};
export type ExportBricksApiResponse = /** status 200 OK */ BrickState;
export type ExportBricksApiArg = void;
export type Brick = {
    /** Identifiant unique de la brique. */
    id?: string;
    /** Numero de reference de la brique. */
    number?: string;
    /** Titre de la brique. */
    title?: string;
    /** Tags de classification de la brique. */
    tags?: string[];
    /** Image en data URL base64. */
    imageBase64?: string;
    /** Date de creation de la brique. */
    createdAt?: string;
    /** Date de derniere mise a jour de la brique. */
    updatedAt?: string;
};
export type ExternalLink = {
    /** Identifiant unique du lien externe. */
    id?: string;
    /** Nom du lien externe. */
    name?: string;
    /** URL de base ou template du lien externe. */
    url?: string;
    /** Indique si le lien est actif. */
    enabled?: boolean;
};
export type BrickListResponse = {
    items?: Brick[];
    count?: number;
};
export type BrickCreationForm = {
    /** Numero de reference de la brique. */
    number?: string;
    /** Titre de la brique. */
    title?: string;
    /** Tags de classification. */
    tags?: string[];
    /** Image en data URL base64. */
    imageBase64?: string;
};
export type BrickState = {
    /** Collection de briques. */
    bricks?: Brick[];
    /** Liste globale des tags disponibles. */
    tags?: string[];
    /** Liste globale des liens externes utilises pour les recherches web. */
    externalLinks?: ExternalLink[];
};
export type BrickImportForm = {
    /** Collection de briques a importer. */
    bricks?: Brick[];
    /** Liste globale de tags (optionnelle, recalculee lors de l'export). */
    tags?: string[];
    /** Liste globale de liens externes a importer. */
    externalLinks?: ExternalLink[];
};
export type ExternalLinkListResponse = {
    items?: ExternalLink[];
    count?: number;
};
export type ExternalLinkCreationForm = {
    /** Nom du lien externe. */
    name?: string;
    /** URL de base ou template. */
    url?: string;
    /** Indique si le lien est actif. */
    enabled?: boolean;
};
export const {
    useGetBrickByIdQuery,
    useUpdateBrickMutation,
    useDeleteBrickByIdMutation,
    useGetExternalLinkByIdQuery,
    useUpdateExternalLinkMutation,
    useDeleteExternalLinkByIdMutation,
    useListBricksQuery,
    useCreateBrickMutation,
    useDeleteAllBricksMutation,
    useImportBricksMutation,
    useListExternalLinksQuery,
    useCreateExternalLinkMutation,
    useExportBricksQuery,
} = injectedRtkApi;
