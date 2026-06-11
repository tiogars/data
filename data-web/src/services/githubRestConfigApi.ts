import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["github-rest-config"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getByIdentifier: build.query<
                GetByIdentifierApiResponse,
                GetByIdentifierApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-rest-config/${queryArg.identifier}`,
                }),
                providesTags: ["github-rest-config"],
            }),
            updateByIdentifier: build.mutation<
                UpdateByIdentifierApiResponse,
                UpdateByIdentifierApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-rest-config/${queryArg.identifier}`,
                    method: "PUT",
                    body: queryArg.gitHubRestConfigUpdateForm,
                }),
                invalidatesTags: ["github-rest-config"],
            }),
            deleteByIdentifier: build.mutation<
                DeleteByIdentifierApiResponse,
                DeleteByIdentifierApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-rest-config/${queryArg.identifier}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["github-rest-config"],
            }),
            create: build.mutation<CreateApiResponse, CreateApiArg>({
                query: (queryArg) => ({
                    url: `/github-rest-config`,
                    method: "POST",
                    body: queryArg.gitHubRestConfigCreationForm,
                }),
                invalidatesTags: ["github-rest-config"],
            }),
            listRequiredPermissions: build.mutation<
                ListRequiredPermissionsApiResponse,
                ListRequiredPermissionsApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-rest-config/permissions`,
                    method: "POST",
                    body: queryArg.gitHubTokenPermissionRequest,
                }),
                invalidatesTags: ["github-rest-config"],
            }),
            searchGitHubRestConfigs: build.query<
                SearchGitHubRestConfigsApiResponse,
                SearchGitHubRestConfigsApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-rest-config/search`,
                    params: {
                        page: queryArg.page,
                        size: queryArg.size,
                        q: queryArg.q,
                    },
                }),
                providesTags: ["github-rest-config"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as githubRestConfigApi };
export type GetByIdentifierApiResponse = /** status 200 OK */ GitHubRestConfig;
export type GetByIdentifierApiArg = {
    identifier: string;
};
export type UpdateByIdentifierApiResponse =
    /** status 200 OK */ GitHubRestConfig;
export type UpdateByIdentifierApiArg = {
    identifier: string;
    gitHubRestConfigUpdateForm: GitHubRestConfigUpdateForm;
};
export type DeleteByIdentifierApiResponse = unknown;
export type DeleteByIdentifierApiArg = {
    identifier: string;
};
export type CreateApiResponse = /** status 200 OK */ GitHubRestConfig;
export type CreateApiArg = {
    gitHubRestConfigCreationForm: GitHubRestConfigCreationForm;
};
export type ListRequiredPermissionsApiResponse =
    /** status 200 OK */ GitHubTokenPermissionResponse;
export type ListRequiredPermissionsApiArg = {
    gitHubTokenPermissionRequest: GitHubTokenPermissionRequest;
};
export type SearchGitHubRestConfigsApiResponse =
    /** status 200 OK */ GitHubRestConfigListResponse;
export type SearchGitHubRestConfigsApiArg = {
    /** Index de page (commence a 0). */
    page?: number;
    /** Nombre d'elements par page. */
    size?: number;
    /** Texte libre de recherche (identifiant et commentaire). */
    q?: string;
};
export type GitHubRestConfig = {
    /** Identifiant technique unique. */
    id?: string;
    /** Identifiant fonctionnel du paramétrage. */
    identifier?: string;
    /** Token masqué pour éviter l'exposition en clair. */
    tokenPreview?: string;
    /** Commentaire de contexte. */
    comment?: string;
};
export type GitHubRestConfigUpdateForm = {
    /** Identifiant fonctionnel pour retrouver ce parametrage. */
    identifier?: string;
    /** Token d'acces GitHub REST. Si vide, le token actuel est conserve. */
    token?: string;
    /** Commentaire libre pour documenter l'usage du token. */
    comment?: string;
};
export type GitHubRestConfigCreationForm = {
    /** Identifiant fonctionnel pour retrouver ce paramétrage. */
    identifier?: string;
    /** Token d'accès GitHub REST. */
    token?: string;
    /** Commentaire libre pour documenter l'usage du token. */
    comment?: string;
};
export type GitHubTokenPermission = {
    /** Permission fine-grained GitHub à configurer. */
    permission?: string;
    /** Niveau d'accès minimal requis. */
    access?: string;
    /** Pourquoi cette permission est nécessaire. */
    reason?: string;
};
export type GitHubTokenPermissionResponse = {
    /** Liste normalisée des opérations reconnues. */
    operations?: string[];
    /** Liste des opérations non reconnues. */
    unknownOperations?: string[];
    /** Liste agrégée des permissions GitHub minimales requises. */
    requiredPermissions?: GitHubTokenPermission[];
};
export type GitHubTokenPermissionRequest = {
    operations?: string[];
};
export type GitHubRestConfigListResponse = {
    items?: GitHubRestConfig[];
    count?: number;
    page?: number;
    size?: number;
    query?: string;
};
export const {
    useGetByIdentifierQuery,
    useUpdateByIdentifierMutation,
    useDeleteByIdentifierMutation,
    useCreateMutation,
    useListRequiredPermissionsMutation,
    useSearchGitHubRestConfigsQuery,
} = injectedRtkApi;
