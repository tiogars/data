import { emptySplitApi as api } from './emptyApi';

export const addTagTypes = ['user-account'] as const;

const injectedRtkApi = api
  .enhanceEndpoints({
    addTagTypes,
  })
  .injectEndpoints({
    endpoints: (build) => ({
      getUserAccount: build.query<GetUserAccountApiResponse, GetUserAccountApiArg>({
        query: (queryArg) => ({ url: `/user-account/${queryArg.id}` }),
        providesTags: ['user-account'],
      }),
      updateUserAccount: build.mutation<UpdateUserAccountApiResponse, UpdateUserAccountApiArg>({
        query: (queryArg) => ({
          url: `/user-account/${queryArg.id}`,
          method: 'PUT',
          body: queryArg.userAccountUpdateForm,
        }),
        invalidatesTags: ['user-account'],
      }),
      deleteUserAccount: build.mutation<DeleteUserAccountApiResponse, DeleteUserAccountApiArg>({
        query: (queryArg) => ({
          url: `/user-account/${queryArg.id}`,
          method: 'DELETE',
        }),
        invalidatesTags: ['user-account'],
      }),
      createUserAccount: build.mutation<CreateUserAccountApiResponse, CreateUserAccountApiArg>({
        query: (queryArg) => ({
          url: '/user-account',
          method: 'POST',
          body: queryArg.userAccountCreationForm,
        }),
        invalidatesTags: ['user-account'],
      }),
      searchUserAccounts: build.query<SearchUserAccountsApiResponse, SearchUserAccountsApiArg>({
        query: (queryArg) => ({
          url: '/user-account/search',
          params: {
            page: queryArg.page,
            size: queryArg.size,
            q: queryArg.q,
          },
        }),
        providesTags: ['user-account'],
      }),
      listUserAccounts: build.query<ListUserAccountsApiResponse, ListUserAccountsApiArg>({
        query: () => ({ url: '/user-account/list' }),
        providesTags: ['user-account'],
      }),
    }),
    overrideExisting: false,
  });

export { injectedRtkApi as userAccountApi };

export type GetUserAccountApiResponse = UserAccount;
export type GetUserAccountApiArg = { id: string };

export type UpdateUserAccountApiResponse = UserAccount;
export type UpdateUserAccountApiArg = {
  id: string;
  userAccountUpdateForm: UserAccountUpdateForm;
};

export type DeleteUserAccountApiResponse = unknown;
export type DeleteUserAccountApiArg = { id: string };

export type CreateUserAccountApiResponse = UserAccount;
export type CreateUserAccountApiArg = {
  userAccountCreationForm: UserAccountCreationForm;
};

export type SearchUserAccountsApiResponse = UserAccountSearchResponse;
export type SearchUserAccountsApiArg = {
  page?: number;
  size?: number;
  q?: string;
};

export type ListUserAccountsApiResponse = UserAccountListResponse;
export type ListUserAccountsApiArg = void;

export type UserAccount = {
  id?: string;
  username?: string;
  role?: string;
  enabled?: boolean;
};

export type UserAccountUpdateForm = {
  id?: string;
  username?: string;
  password?: string;
  role?: string;
  enabled?: boolean;
};

export type UserAccountCreationForm = {
  username?: string;
  password?: string;
  role?: string;
  enabled?: boolean;
};

export type UserAccountSearchResponse = {
  items?: UserAccount[];
  count?: number;
  page?: number;
  size?: number;
  query?: string;
};

export type UserAccountListResponse = {
  items?: UserAccount[];
  count?: number;
};

export const {
  useGetUserAccountQuery,
  useUpdateUserAccountMutation,
  useDeleteUserAccountMutation,
  useCreateUserAccountMutation,
  useSearchUserAccountsQuery,
  useListUserAccountsQuery,
} = injectedRtkApi;
