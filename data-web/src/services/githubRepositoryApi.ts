import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["github-repository"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getGitHubRepositoryById: build.query<
                GetGitHubRepositoryByIdApiResponse,
                GetGitHubRepositoryByIdApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-repository/${queryArg.id}`,
                }),
                providesTags: ["github-repository"],
            }),
            updateGitHubRepository: build.mutation<
                UpdateGitHubRepositoryApiResponse,
                UpdateGitHubRepositoryApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-repository/${queryArg.id}`,
                    method: "PUT",
                    body: queryArg.gitHubRepository,
                }),
                invalidatesTags: ["github-repository"],
            }),
            deleteGitHubRepositoryById: build.mutation<
                DeleteGitHubRepositoryByIdApiResponse,
                DeleteGitHubRepositoryByIdApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-repository/${queryArg.id}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["github-repository"],
            }),
            listGitHubRepositories: build.query<
                ListGitHubRepositoriesApiResponse,
                ListGitHubRepositoriesApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-repository`,
                    params: {
                        page: queryArg.page,
                        size: queryArg.size,
                        q: queryArg.q,
                    },
                }),
                providesTags: ["github-repository"],
            }),
            createGitHubRepository: build.mutation<
                CreateGitHubRepositoryApiResponse,
                CreateGitHubRepositoryApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-repository`,
                    method: "POST",
                    body: queryArg.gitHubRepositoryCreationForm,
                }),
                invalidatesTags: ["github-repository"],
            }),
            deleteAllGitHubRepositories: build.mutation<
                DeleteAllGitHubRepositoriesApiResponse,
                DeleteAllGitHubRepositoriesApiArg
            >({
                query: () => ({ url: `/github-repository`, method: "DELETE" }),
                invalidatesTags: ["github-repository"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as githubRepositoryApi };
export type GetGitHubRepositoryByIdApiResponse =
    /** status 200 OK */ GitHubRepository;
export type GetGitHubRepositoryByIdApiArg = {
    id: string;
};
export type UpdateGitHubRepositoryApiResponse =
    /** status 200 OK */ GitHubRepository;
export type UpdateGitHubRepositoryApiArg = {
    id: string;
    gitHubRepository: GitHubRepository;
};
export type DeleteGitHubRepositoryByIdApiResponse = unknown;
export type DeleteGitHubRepositoryByIdApiArg = {
    id: string;
};
export type ListGitHubRepositoriesApiResponse =
    /** status 200 OK */ GitHubRepositoryListResponse;
export type ListGitHubRepositoriesApiArg = {
    /** Index de page (commence à 0). */
    page?: number;
    /** Nombre d'éléments par page. */
    size?: number;
    /** Texte libre de recherche (owner, name, fullName, URL, description, branche, langage). */
    q?: string;
};
export type CreateGitHubRepositoryApiResponse =
    /** status 200 OK */ GitHubRepository;
export type CreateGitHubRepositoryApiArg = {
    gitHubRepositoryCreationForm: GitHubRepositoryCreationForm;
};
export type DeleteAllGitHubRepositoriesApiResponse = unknown;
export type DeleteAllGitHubRepositoriesApiArg = void;
export type GitHubRepository = {
    /** Identifiant unique du repository. */
    id?: string;
    /** Le propriétaire du repository. */
    owner?: string;
    /** Le nom court du repository. */
    name?: string;
    /** Le nom complet owner/name du repository. */
    fullName?: string;
    /** L'URL du repository. */
    url?: string;
    /** Description fonctionnelle du repository. */
    description?: string;
    /** Branche par défaut. */
    defaultBranch?: string;
    /** Langage principal. */
    language?: string;
    /** Nombre d'étoiles. */
    stars?: number;
    /** Indique si le repository est archivé. */
    archived?: boolean;
    /** Indique si le repository existe toujours sur GitHub. */
    existsOnGitHub?: boolean;
};
export type GitHubRepositoryListResponse = {
    items?: GitHubRepository[];
    count?: number;
    page?: number;
    size?: number;
    query?: string;
};
export type GitHubRepositoryCreationForm = {
    /** Le propriétaire du repository. */
    owner?: string;
    /** Le nom du repository. */
    name?: string;
    /** L'URL du repository. */
    url?: string;
    /** Description fonctionnelle du repository. */
    description?: string;
    /** Branche par défaut. */
    defaultBranch?: string;
    /** Langage principal. */
    language?: string;
    /** Nombre d'étoiles. */
    stars?: number;
    /** Indique si le repository est archivé. */
    archived?: boolean;
};
export const {
    useGetGitHubRepositoryByIdQuery,
    useUpdateGitHubRepositoryMutation,
    useDeleteGitHubRepositoryByIdMutation,
    useListGitHubRepositoriesQuery,
    useCreateGitHubRepositoryMutation,
    useDeleteAllGitHubRepositoriesMutation,
} = injectedRtkApi;
