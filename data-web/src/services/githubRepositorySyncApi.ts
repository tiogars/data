import { emptySplitApi as api } from './emptyApi';
import type { GitHubRepository } from './githubRepositoryApi';

const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    syncGitHubRepository: build.mutation<
      SyncGitHubRepositoryResult,
      SyncGitHubRepositoryApiArg
    >({
      query: (queryArg) => ({
        url: '/github-repository/sync',
        method: 'POST',
        body: queryArg.gitHubRepositorySyncForm,
      }),
    }),
    syncAllGitHubRepositories: build.mutation<
      GitHubRepositoryBulkSyncResult,
      SyncAllGitHubRepositoriesApiArg
    >({
      query: (queryArg) => ({
        url: '/github-repository/sync/all',
        method: 'POST',
        body: queryArg.gitHubRepositoryBulkSyncForm,
      }),
    }),
  }),
  overrideExisting: false,
});

export { injectedRtkApi as githubRepositorySyncApi };

export type GitHubRepositorySyncStatus = 'CREATED' | 'UPDATED' | 'MARKED_AS_MISSING';

export type GitHubRepositoryWithSyncStatus = GitHubRepository & {
  existsOnGitHub?: boolean;
};

export type SyncGitHubRepositoryResult = {
  status?: GitHubRepositorySyncStatus;
  repository?: GitHubRepositoryWithSyncStatus;
  usedConfigIdentifier?: string;
  message?: string;
};

export type GitHubRepositorySyncForm = {
  owner: string;
  name: string;
  gitHubRestConfigIdentifier: string;
};

export type SyncGitHubRepositoryApiArg = {
  gitHubRepositorySyncForm: GitHubRepositorySyncForm;
};

export type GitHubRepositoryBulkSyncResult = {
  created?: number;
  updated?: number;
  markedAsMissing?: number;
  total?: number;
  usedConfigIdentifier?: string;
};

export type GitHubRepositoryBulkSyncForm = {
  gitHubRestConfigIdentifier: string;
};

export type SyncAllGitHubRepositoriesApiArg = {
  gitHubRepositoryBulkSyncForm: GitHubRepositoryBulkSyncForm;
};

export const {
  useSyncGitHubRepositoryMutation,
  useSyncAllGitHubRepositoriesMutation,
} = injectedRtkApi;
