import { emptySplitApi as api } from './emptyApi';

export const addTagTypes = ['android'] as const;

const injectedRtkApi = api
  .enhanceEndpoints({
    addTagTypes,
  })
  .injectEndpoints({
    endpoints: (build) => ({
      getAndroid: build.query<Android, { id: string }>({
        query: (queryArg) => ({ url: `/android/${queryArg.id}` }),
        providesTags: ['android'],
      }),
      updateAndroid: build.mutation<Android, { id: string; android: Android }>({
        query: (queryArg) => ({
          url: `/android/${queryArg.id}`,
          method: 'PUT',
          body: queryArg.android,
        }),
        invalidatesTags: ['android'],
      }),
      deleteAndroid: build.mutation<unknown, { id: string }>({
        query: (queryArg) => ({
          url: `/android/${queryArg.id}`,
          method: 'DELETE',
        }),
        invalidatesTags: ['android'],
      }),
      listAndroids: build.query<AndroidListResponse, void>({
        query: () => ({ url: `/android` }),
        providesTags: ['android'],
      }),
      createAndroid: build.mutation<Android, { androidCreationForm: AndroidCreationForm }>({
        query: (queryArg) => ({
          url: `/android`,
          method: 'POST',
          body: queryArg.androidCreationForm,
        }),
        invalidatesTags: ['android'],
      }),
      deleteAllAndroids: build.mutation<unknown, void>({
        query: () => ({ url: `/android`, method: 'DELETE' }),
        invalidatesTags: ['android'],
      }),
      importAndroids: build.mutation<AndroidImportResult, { androidImportForm: AndroidImportForm }>({
        query: (queryArg) => ({
          url: `/android/import`,
          method: 'POST',
          body: queryArg.androidImportForm,
        }),
        invalidatesTags: ['android'],
      }),
      importAndroidsCsv: build.mutation<AndroidImportResult, { body: string }>({
        query: (queryArg) => ({
          url: `/android/import/csv`,
          method: 'POST',
          body: queryArg.body,
        }),
        invalidatesTags: ['android'],
      }),
      exportAndroids: build.query<AndroidListResponse, void>({
        query: () => ({ url: `/android/export` }),
        providesTags: ['android'],
      }),
      printAndroids: build.query<AndroidPrintResponse, {
        mode?: string;
        name?: string;
        packageName?: string;
        category?: string;
        description?: string;
      }>({
        query: (queryArg) => ({
          url: `/android/print`,
          params: {
            mode: queryArg.mode,
            name: queryArg.name,
            packageName: queryArg.packageName,
            category: queryArg.category,
            description: queryArg.description,
          },
        }),
        providesTags: ['android'],
      }),
    }),
    overrideExisting: false,
  });

export { injectedRtkApi as androidApi };

export type Android = {
  /** L'identifiant unique de l'application Android. */
  id?: string;
  /** Le nom de l'application Android. */
  name?: string;
  /** Le nom du package Android. */
  packageName?: string;
  /** Les categories associees a l'application. */
  category?: string[];
  /** La description de l'application Android. */
  description?: string;
  /** L'icone de l'application au format URL ou base64. */
  icon?: string;
};
export type AndroidListResponse = {
  items?: Android[];
  count?: number;
};
export type AndroidCreationForm = {
  /** Le nom de l'application Android. */
  name?: string;
  /** Le nom du package Android. */
  packageName?: string;
  /** Les categories associees a l'application. */
  category?: string[];
  /** La description de l'application Android. */
  description?: string;
  /** L'icone de l'application au format URL ou base64. */
  icon?: string;
};
export type AndroidImportResult = {
  imported?: Android[];
  importedCount?: number;
  duplicatePackageNames?: string[];
  skippedCount?: number;
};
export type AndroidImportForm = {
  /** Liste des applications Android a importer. */
  items?: Android[];
};
export type AndroidPrintResponse = {
  items?: Android[];
  count?: number;
  generatedAt?: string;
  total?: number;
};
export const {
  useGetAndroidQuery,
  useUpdateAndroidMutation,
  useDeleteAndroidMutation,
  useListAndroidsQuery,
  useCreateAndroidMutation,
  useDeleteAllAndroidsMutation,
  useImportAndroidsMutation,
  useImportAndroidsCsvMutation,
  useExportAndroidsQuery,
  usePrintAndroidsQuery,
} = injectedRtkApi;