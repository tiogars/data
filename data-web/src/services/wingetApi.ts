import { emptySplitApi as api } from './emptyApi';

export const addTagTypes = ['winget'] as const;

const injectedRtkApi = api
  .enhanceEndpoints({ addTagTypes })
  .injectEndpoints({
    endpoints: (build) => ({
      getWinget: build.query<GetWingetApiResponse, GetWingetApiArg>({
        query: (queryArg) => ({ url: `/winget/${queryArg.id}` }),
        providesTags: ['winget'],
      }),
      updateWinget: build.mutation<UpdateWingetApiResponse, UpdateWingetApiArg>({
        query: (queryArg) => ({
          url: `/winget/${queryArg.id}`,
          method: 'PUT',
          body: queryArg.wingetUpdateForm,
        }),
        invalidatesTags: ['winget'],
      }),
      deleteWinget: build.mutation<DeleteWingetApiResponse, DeleteWingetApiArg>({
        query: (queryArg) => ({
          url: `/winget/${queryArg.id}`,
          method: 'DELETE',
        }),
        invalidatesTags: ['winget'],
      }),
      createWinget: build.mutation<CreateWingetApiResponse, CreateWingetApiArg>({
        query: (queryArg) => ({
          url: '/winget',
          method: 'POST',
          body: queryArg.wingetCreationForm,
        }),
        invalidatesTags: ['winget'],
      }),
      searchWingets: build.query<SearchWingetsApiResponse, SearchWingetsApiArg>({
        query: (queryArg) => ({
          url: '/winget/search',
          params: {
            page: queryArg.page,
            size: queryArg.size,
            q: queryArg.q,
          },
        }),
        providesTags: ['winget'],
      }),
      listWingets: build.query<ListWingetsApiResponse, void>({
        query: () => ({ url: '/winget/list' }),
        providesTags: ['winget'],
      }),
    }),
    overrideExisting: false,
  });

export { injectedRtkApi as wingetApi };

export type GetWingetApiResponse = Winget;
export type GetWingetApiArg = { id: string };

export type UpdateWingetApiResponse = Winget;
export type UpdateWingetApiArg = {
  id: string;
  wingetUpdateForm: WingetUpdateForm;
};

export type DeleteWingetApiResponse = unknown;
export type DeleteWingetApiArg = { id: string };

export type CreateWingetApiResponse = Winget;
export type CreateWingetApiArg = {
  wingetCreationForm: WingetCreationForm;
};

export type SearchWingetsApiResponse = WingetSearchResponse;
export type SearchWingetsApiArg = {
  page?: number;
  size?: number;
  q?: string;
};

export type ListWingetsApiResponse = WingetListResponse;

export type Winget = {
  id?: string;
  name?: string;
  description?: string;
  wingetId?: string;
  installCommand?: string;
  tags?: string[];
};

export type WingetCreationForm = {
  name?: string;
  description?: string;
  wingetId?: string;
  installCommand?: string;
  tags?: string[];
};

export type WingetUpdateForm = {
  id?: string;
  name?: string;
  description?: string;
  wingetId?: string;
  installCommand?: string;
  tags?: string[];
};

export type WingetSearchResponse = {
  items?: Winget[];
  count?: number;
  page?: number;
  size?: number;
  query?: string;
};

export type WingetListResponse = {
  items?: Winget[];
  count?: number;
};

export const {
  useGetWingetQuery,
  useUpdateWingetMutation,
  useDeleteWingetMutation,
  useCreateWingetMutation,
  useSearchWingetsQuery,
  useListWingetsQuery,
} = injectedRtkApi;
