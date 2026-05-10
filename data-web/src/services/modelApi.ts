import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["model"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getModel: build.query<GetModelApiResponse, GetModelApiArg>({
                query: (queryArg) => ({ url: `/model/${queryArg.id}` }),
                providesTags: ["model"],
            }),
            updateModel: build.mutation<
                UpdateModelApiResponse,
                UpdateModelApiArg
            >({
                query: (queryArg) => ({
                    url: `/model/${queryArg.id}`,
                    method: "PUT",
                    body: queryArg.model,
                }),
                invalidatesTags: ["model"],
            }),
            deleteModel: build.mutation<
                DeleteModelApiResponse,
                DeleteModelApiArg
            >({
                query: (queryArg) => ({
                    url: `/model/${queryArg.id}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["model"],
            }),
            listModels: build.query<ListModelsApiResponse, ListModelsApiArg>({
                query: () => ({ url: `/model` }),
                providesTags: ["model"],
            }),
            createModel: build.mutation<
                CreateModelApiResponse,
                CreateModelApiArg
            >({
                query: (queryArg) => ({
                    url: `/model`,
                    method: "POST",
                    body: queryArg.modelCreationForm,
                }),
                invalidatesTags: ["model"],
            }),
            deleteAllModels: build.mutation<
                DeleteAllModelsApiResponse,
                DeleteAllModelsApiArg
            >({
                query: () => ({ url: `/model`, method: "DELETE" }),
                invalidatesTags: ["model"],
            }),
            importModels: build.mutation<
                ImportModelsApiResponse,
                ImportModelsApiArg
            >({
                query: (queryArg) => ({
                    url: `/model/import`,
                    method: "POST",
                    body: queryArg.modelImportForm,
                }),
                invalidatesTags: ["model"],
            }),
            getModelAiText: build.query<
                GetModelAiTextApiResponse,
                GetModelAiTextApiArg
            >({
                query: (queryArg) => ({ url: `/model/${queryArg.id}/ai-text` }),
                providesTags: ["model"],
            }),
            printModels: build.query<PrintModelsApiResponse, PrintModelsApiArg>(
                {
                    query: (queryArg) => ({
                        url: `/model/print`,
                        params: {
                            mode: queryArg.mode,
                            name: queryArg.name,
                            description: queryArg.description,
                        },
                    }),
                    providesTags: ["model"],
                },
            ),
            exportModels: build.query<
                ExportModelsApiResponse,
                ExportModelsApiArg
            >({
                query: () => ({ url: `/model/export` }),
                providesTags: ["model"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as modelApi };
export type GetModelApiResponse = /** status 200 OK */ Model;
export type GetModelApiArg = {
    id: string;
};
export type UpdateModelApiResponse = /** status 200 OK */ Model;
export type UpdateModelApiArg = {
    id: string;
    model: Model;
};
export type DeleteModelApiResponse = unknown;
export type DeleteModelApiArg = {
    id: string;
};
export type ListModelsApiResponse = /** status 200 OK */ ModelListResponse;
export type ListModelsApiArg = void;
export type CreateModelApiResponse = /** status 200 OK */ Model;
export type CreateModelApiArg = {
    modelCreationForm: ModelCreationForm;
};
export type DeleteAllModelsApiResponse = unknown;
export type DeleteAllModelsApiArg = void;
export type ImportModelsApiResponse = /** status 200 OK */ ModelImportResult;
export type ImportModelsApiArg = {
    modelImportForm: ModelImportForm;
};
export type GetModelAiTextApiResponse =
    /** status 200 OK */ ModelAiTextResponse;
export type GetModelAiTextApiArg = {
    id: string;
};
export type PrintModelsApiResponse = /** status 200 OK */ ModelPrintResponse;
export type PrintModelsApiArg = {
    mode?: string;
    name?: string;
    description?: string;
};
export type ExportModelsApiResponse = /** status 200 OK */ ModelListResponse;
export type ExportModelsApiArg = void;
export type ModelAttribute = {
    /** L'identifiant unique de l'attribut du modele. */
    id?: string;
    /** Le nom de l'attribut. */
    name?: string;
    /** La description de l'attribut. */
    description?: string;
};
export type Model = {
    /** L'identifiant unique du modele. */
    id?: string;
    /** Le nom du modele. */
    name?: string;
    /** La description du modele. */
    description?: string;
    /** Collection des attributs du modele. */
    modelAttributes?: ModelAttribute[];
};
export type ModelListResponse = {
    items?: Model[];
    count?: number;
};
export type ModelCreationForm = {
    /** Le nom du modele. */
    name?: string;
    /** La description du modele. */
    description?: string;
    /** Collection des attributs du modele. */
    modelAttributes?: ModelAttribute[];
};
export type ModelImportResult = {
    imported?: Model[];
    importedCount?: number;
    duplicateNames?: string[];
    skippedCount?: number;
};
export type ModelImportForm = {
    /** Liste des modeles a importer. */
    items?: Model[];
};
export type ModelAiTextResponse = {
    /** Identifiant du modele source. */
    modelId?: string;
    /** Texte formate pour une IA afin de recreer un modele. */
    text?: string;
};
export type ModelPrintResponse = {
    items?: Model[];
    count?: number;
    generatedAt?: string;
    total?: number;
};
export const {
    useGetModelQuery,
    useUpdateModelMutation,
    useDeleteModelMutation,
    useListModelsQuery,
    useCreateModelMutation,
    useDeleteAllModelsMutation,
    useImportModelsMutation,
    useGetModelAiTextQuery,
    usePrintModelsQuery,
    useExportModelsQuery,
} = injectedRtkApi;
