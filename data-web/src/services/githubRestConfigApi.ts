import { emptySplitApi as api } from './emptyApi';

const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    createGitHubRestConfig: build.mutation<
      CreateGitHubRestConfigApiResponse,
      CreateGitHubRestConfigApiArg
    >({
      query: (queryArg) => ({
        url: '/github-rest-config',
        method: 'POST',
        body: queryArg.gitHubRestConfigCreationForm,
      }),
    }),
    getGitHubRestConfigByIdentifier: build.query<
      GetGitHubRestConfigByIdentifierApiResponse,
      GetGitHubRestConfigByIdentifierApiArg
    >({
      query: (queryArg) => ({
        url: `/github-rest-config/${queryArg.identifier}`,
      }),
    }),
    listRequiredGitHubTokenPermissions: build.mutation<
      ListRequiredGitHubTokenPermissionsApiResponse,
      ListRequiredGitHubTokenPermissionsApiArg
    >({
      query: (queryArg) => ({
        url: '/github-rest-config/permissions',
        method: 'POST',
        body: queryArg.gitHubTokenPermissionRequest,
      }),
    }),
  }),
  overrideExisting: false,
});

export { injectedRtkApi as githubRestConfigApi };

export type CreateGitHubRestConfigApiResponse = GitHubRestConfig;
export type CreateGitHubRestConfigApiArg = {
  gitHubRestConfigCreationForm: GitHubRestConfigCreationForm;
};

export type GetGitHubRestConfigByIdentifierApiResponse = GitHubRestConfig;
export type GetGitHubRestConfigByIdentifierApiArg = {
  identifier: string;
};

export type ListRequiredGitHubTokenPermissionsApiResponse = GitHubTokenPermissionResponse;
export type ListRequiredGitHubTokenPermissionsApiArg = {
  gitHubTokenPermissionRequest: GitHubTokenPermissionRequest;
};

export type GitHubRestConfig = {
  id?: string;
  identifier?: string;
  tokenPreview?: string;
  comment?: string;
};

export type GitHubRestConfigCreationForm = {
  identifier: string;
  token: string;
  comment?: string;
};

export type GitHubTokenPermissionRequest = {
  operations: string[];
};

export type GitHubTokenPermission = {
  permission?: string;
  access?: string;
  reason?: string;
};

export type GitHubTokenPermissionResponse = {
  operations?: string[];
  unknownOperations?: string[];
  requiredPermissions?: GitHubTokenPermission[];
};

export const {
  useCreateGitHubRestConfigMutation,
  useLazyGetGitHubRestConfigByIdentifierQuery,
  useListRequiredGitHubTokenPermissionsMutation,
} = injectedRtkApi;
