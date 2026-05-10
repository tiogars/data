import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["github-repository"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            syncGitHubRepository: build.mutation<
                SyncGitHubRepositoryApiResponse,
                SyncGitHubRepositoryApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-repository/sync`,
                    method: "POST",
                    body: queryArg.gitHubRepositorySyncForm,
                }),
                invalidatesTags: ["github-repository"],
            }),
            syncAllGitHubRepositories: build.mutation<
                SyncAllGitHubRepositoriesApiResponse,
                SyncAllGitHubRepositoriesApiArg
            >({
                query: (queryArg) => ({
                    url: `/github-repository/sync/all`,
                    method: "POST",
                    body: queryArg.gitHubRepositoryBulkSyncForm,
                }),
                invalidatesTags: ["github-repository"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as githubRepositorySyncApi };
export type SyncGitHubRepositoryApiResponse =
    /** status 200 OK */ GitHubRepositorySyncResult;
export type SyncGitHubRepositoryApiArg = {
    gitHubRepositorySyncForm: GitHubRepositorySyncForm;
};
export type SyncAllGitHubRepositoriesApiResponse =
    /** status 200 OK */ GitHubRepositoryBulkSyncResult;
export type SyncAllGitHubRepositoriesApiArg = {
    gitHubRepositoryBulkSyncForm: GitHubRepositoryBulkSyncForm;
};
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
export type GitHubRepositorySyncResult = {
    /** Résultat de la synchronisation. */
    status?: "CREATED" | "UPDATED" | "MARKED_AS_MISSING";
    /** Repository local après synchronisation. */
    repository?: GitHubRepository;
    /** Configuration token GitHub REST utilisée. */
    usedConfigIdentifier?: string;
    /** Message de synthèse du traitement. */
    message?: string;
};
export type GitHubRepositorySyncForm = {
    /** Le propriétaire du repository à synchroniser. */
    owner?: string;
    /** Le nom du repository à synchroniser. */
    name?: string;
    /** L'identifiant de la configuration GitHub REST à utiliser. */
    gitHubRestConfigIdentifier?: string;
};
export type GitHubRepositoryBulkSyncResult = {
    /** Nombre de repositories créés localement depuis GitHub. */
    created?: number;
    /** Nombre de repositories mis à jour depuis GitHub. */
    updated?: number;
    /** Nombre de repositories marqués comme inexistants sur GitHub. */
    markedAsMissing?: number;
    /** Nombre total de repositories traités. */
    total?: number;
    /** Configuration token GitHub REST utilisée. */
    usedConfigIdentifier?: string;
};
export type GitHubRepositoryBulkSyncForm = {
    gitHubRestConfigIdentifier?: string;
};
export const {
    useSyncGitHubRepositoryMutation,
    useSyncAllGitHubRepositoriesMutation,
} = injectedRtkApi;
