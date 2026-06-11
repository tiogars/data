import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["android"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getAndroid: build.query<GetAndroidApiResponse, GetAndroidApiArg>({
                query: (queryArg) => ({ url: `/android/${queryArg.id}` }),
                providesTags: ["android"],
            }),
            updateAndroid: build.mutation<
                UpdateAndroidApiResponse,
                UpdateAndroidApiArg
            >({
                query: (queryArg) => ({
                    url: `/android/${queryArg.id}`,
                    method: "PUT",
                    body: queryArg.android,
                }),
                invalidatesTags: ["android"],
            }),
            deleteAndroid: build.mutation<
                DeleteAndroidApiResponse,
                DeleteAndroidApiArg
            >({
                query: (queryArg) => ({
                    url: `/android/${queryArg.id}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["android"],
            }),
            createAndroid: build.mutation<
                CreateAndroidApiResponse,
                CreateAndroidApiArg
            >({
                query: (queryArg) => ({
                    url: `/android`,
                    method: "POST",
                    body: queryArg.androidCreationForm,
                }),
                invalidatesTags: ["android"],
            }),
            deleteAllAndroids: build.mutation<
                DeleteAllAndroidsApiResponse,
                DeleteAllAndroidsApiArg
            >({
                query: () => ({ url: `/android`, method: "DELETE" }),
                invalidatesTags: ["android"],
            }),
            importAndroids: build.mutation<
                ImportAndroidsApiResponse,
                ImportAndroidsApiArg
            >({
                query: (queryArg) => ({
                    url: `/android/import`,
                    method: "POST",
                    body: queryArg.androidImportForm,
                }),
                invalidatesTags: ["android"],
            }),
            importAndroidsCsv: build.mutation<
                ImportAndroidsCsvApiResponse,
                ImportAndroidsCsvApiArg
            >({
                query: (queryArg) => ({
                    url: `/android/import/csv`,
                    method: "POST",
                    body: queryArg.body,
                }),
                invalidatesTags: ["android"],
            }),
            printAndroids: build.query<
                PrintAndroidsApiResponse,
                PrintAndroidsApiArg
            >({
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
                providesTags: ["android"],
            }),
            listAndroids: build.query<
                ListAndroidsApiResponse,
                ListAndroidsApiArg
            >({
                query: () => ({ url: `/android/list` }),
                providesTags: ["android"],
            }),
            exportAndroids: build.query<
                ExportAndroidsApiResponse,
                ExportAndroidsApiArg
            >({
                query: () => ({ url: `/android/export` }),
                providesTags: ["android"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as androidApi };
export type GetAndroidApiResponse = /** status 200 OK */ Android;
export type GetAndroidApiArg = {
    id: string;
};
export type UpdateAndroidApiResponse = /** status 200 OK */ Android;
export type UpdateAndroidApiArg = {
    id: string;
    android: Android;
};
export type DeleteAndroidApiResponse = unknown;
export type DeleteAndroidApiArg = {
    id: string;
};
export type CreateAndroidApiResponse = /** status 200 OK */ Android;
export type CreateAndroidApiArg = {
    androidCreationForm: AndroidCreationForm;
};
export type DeleteAllAndroidsApiResponse = unknown;
export type DeleteAllAndroidsApiArg = void;
export type ImportAndroidsApiResponse =
    /** status 200 OK */ AndroidImportResult;
export type ImportAndroidsApiArg = {
    androidImportForm: AndroidImportForm;
};
export type ImportAndroidsCsvApiResponse =
    /** status 200 OK */ AndroidImportResult;
export type ImportAndroidsCsvApiArg = {
    body: string;
};
export type PrintAndroidsApiResponse =
    /** status 200 OK */ AndroidPrintResponse;
export type PrintAndroidsApiArg = {
    mode?: string;
    name?: string;
    packageName?: string;
    category?: string;
    description?: string;
};
export type ListAndroidsApiResponse = /** status 200 OK */ AndroidListResponse;
export type ListAndroidsApiArg = void;
export type ExportAndroidsApiResponse =
    /** status 200 OK */ AndroidListResponse;
export type ExportAndroidsApiArg = void;
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
export type AndroidListResponse = {
    items?: Android[];
    count?: number;
};
export const {
    useGetAndroidQuery,
    useUpdateAndroidMutation,
    useDeleteAndroidMutation,
    useCreateAndroidMutation,
    useDeleteAllAndroidsMutation,
    useImportAndroidsMutation,
    useImportAndroidsCsvMutation,
    usePrintAndroidsQuery,
    useListAndroidsQuery,
    useExportAndroidsQuery,
} = injectedRtkApi;
